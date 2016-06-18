package shipz.network;

import shipz.util.Event;

import shipz.Player;
import shipz.util.GameEvent;
import shipz.util.GameEventSource;
import shipz.util.Timer;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;


/**
 * The {@code Network} class establishes a connection to another execution of the game.
 * It represents either a server, that waits for the response of a client, or a client, that connects to a server.
 * The methods of this class allow you to send and recieve information from a connected computer.
 * <p>
 * The following example shows how to start a server and accept a connecting client.
 * <blockquote><pre>
 * 	Network server = new Network(true);
 * </pre></blockquote><p>
 * This is how to connect to a server as a client:
 * <blockquote><pre>
 * 	Network client = new Network(false);
 * </pre></blockquote>
 *
 * @author Paul Kretschel
 *
 */

public class Network extends GameEventSource implements Runnable {

    private final static char PING_ACTION = 0;

    private final static int CONNECTION_TIMEOUT = 5000;

    private boolean _isHost;

    private Socket _socket;
    private BufferedReader _in;
    private PrintWriter _out;

    private boolean _connected;

    private String _ip;
    private int _port;

    /**
     * Initializes a newly created {@code Network} object, so that it can esablish a new connection.
     *
     * @param 	isHost
     * 			The type of the {@code Network}.
     * 			True:	The object is a server.
     * 			False:	The object is a client.
     *
     */

    public Network(boolean isHost) {

        _isHost = isHost;
        _connected = false;

        _socket = null;
        _in = null;
        _out = null;

        _ip = null;
    }



    public void run() {
        Timer timer = new Timer(100);


        send(PING_ACTION + "//");

        String s;
        while(_connected && timer.hasTime()) {

            try {
                s = _in.readLine();
                if(s != null && !s.isEmpty()) { // Message received

                    timer.reset();

                    evaluateString(s);

                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        if(_connected) { // Connection was interrupted
            _connected = false;
            fireGameEvent(Event.DISCONNECT_EVENT);
        } else {
            close();
        }

    }

    /**
     * Connects to the given ip and port.
     *
     * @param 	port
     * 			The port that the new server is going to use. (Better choose a high number)
     * @return	{@code True} if the connection was successful, {@code false} if it failed.
     */

    public void connect(String ip, int port) throws Exception {
        _port = port;
        _ip = ip;

        if(_isHost) connectHost();
        else connectClient();
    }

    /**
     * Starts a new server and waits for a client to connect.
     * Also checks if the connection was successful.
     *
     * @return	{@code True} if the connection was successful, {@code false} if it failed.
     */

    private void connectHost() throws Exception {
        if(!_isHost)
            return;

        try {
            System.out.println("Binding to port " + _port + " ...");
            ServerSocket serverSocket = new ServerSocket(_port);
            serverSocket.setSoTimeout( CONNECTION_TIMEOUT );

            System.out.println("Waiting for a client ...");
            _socket = serverSocket.accept();
            serverSocket.close();
            serverSocket = null;

            System.out.println("Client accepted.");

            String connectedIp = _socket.getInetAddress().getHostAddress();
            // If the connected ip is not the same as the specified -> repeat connect.
            if(!_ip.equals(connectedIp)) connectHost();

            _connected = true;

            open();

        } catch(SocketTimeoutException s) {
            throw new Exception("Connection timeout.");
        }
    }

    /**
     * Connects to a server with the specified ip and port.
     * Also checks if the connection was successful.
     */

    private void connectClient() throws Exception {
        if (_isHost)
            return;


        System.out.println("Connecting to port " + _port + " ...");

        Timer timer = new Timer(CONNECTION_TIMEOUT);
        while(!_connected && timer.hasTime()) {
            try {

                _socket = new Socket(_ip, _port);
                System.out.println("Client connected.");
                _connected = true;
                open();

            } catch(IOException e) {
                throw new Exception("Failed to connect: " + e.getMessage());
            }
        }

        if(!timer.hasTime())
            throw new Exception("Connection timeout.");
    }

    private void evaluateString(String s) {
        byte action = getAction(s);

        if(action == PING_ACTION) return;

        switch (action) {
            case SHOOT_EVENT:
                if( !validShot(s) ) break;

                convertShot(s);

                fireGameEvent(SHOOT_EVENT);
                break;
            case SHOOT_RESULT:
                if( !validShot(s) ) break;

                convertShot(s);

                fireGameEvent(SHOOT_RESULT);
                break;
            case CLOSE_EVENT:
                fireGameEvent(CLOSE_EVENT);
                break;
            default:
                break;
        }
    }

    private void convertShot(String s) {
        String values = s.split("//")[1];
    }

    private byte getAction(String s) {
        byte action;
        try {
            action = (byte) Integer.parseInt( s.split("//")[0] );
        } catch(NumberFormatException e) {
            action = -1;
        }
        return action;
    }

    public void shootField(int x, int y, char res) {
        send( SHOOT_EVENT + "//" + x + ":" + y + ":" + res);
    }

    public void shootResult(int x, int y, char res) {
        if(_isHost) {
            send( SHOOT_RESULT + "//" + x + ":" + y + ":" + res);
        }
    }

    private boolean validShot(String s) {
        String[] split = s.split("//");
        if(split.length != 2) return false;
        String[] split2 = split[1].split(":");
        if(split2.length != 3) return false;
        if(split2[2].length() != 1) return false;
        int x, y;
        try {
            x = Integer.parseInt( split2[0] );
            y = Integer.parseInt( split2[1] );
        } catch (NumberFormatException e) {
            return false;
        }
        if(x < 0 && y < 0) return false;

        return true;
    }

    private int convertX(String s) {
        String x = s.split(":")[0];
        return Integer.parseInt( x );
    }

    private int convertY(String s) {
        String y = s.split(":")[1];
        return Integer.parseInt( y );
    }

    private char convertResult(String s) {
        String hit = s.split(":")[2];
        return hit.charAt(0);
    }

    public void reconnect() throws Exception {
        if(_connected)
            return;

        if(_isHost) {
            connect(_ip, _port);
        }
        if(!_isHost) {
            connect(_ip, _port);
        }
    }



    /**
     * Opens the connection.
     */

    private void open() throws Exception {
        if(!_connected)
            return;

        try {
            InputStream inputStream = _socket.getInputStream();
            _in = new BufferedReader( new InputStreamReader( inputStream ));

            OutputStream outputStream = _socket.getOutputStream();
            _out = new PrintWriter( outputStream );

        } catch(IOException e) {
            throw new Exception("Failed to open connection: " + e.getMessage());
        }
    }



    /**
     * Closes the connection.
     * This should always be done at the end when the {@code Network} is not being used anymore.
     */

    public void close() {
        try{
            _connected = false;

            if(_socket != null)
                _socket.close();

            if(_in != null)
                _in.close();

            if(_out != null)
                _out.close();

            _socket = null;
            _in = null;
            _out = null;

        } catch (IOException e) { System.err.println("Failed to close: " + e.getMessage()); }

    }

    /**
     * Sends a message to the connected computer.
     *
     * @param 	msg
     * 			The message that is supposed to be sent.
     */

    public void send(String msg) {
        _out.write(msg + "\n");
        _out.flush();
    }

    public void disconnect() {
        _connected = false;
        close();
    }

    public boolean connected() {
        return _connected;
    }

    public String toString() {
        return "Network";
    }

    public void end() {
        if(_connected)
            send( CLOSE_EVENT + "");
        disconnect();
    }

}
