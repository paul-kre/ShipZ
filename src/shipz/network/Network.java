package shipz.network;

import shipz.Player;
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
import java.util.regex.Pattern;


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

public class Network extends Player {

    private final static char pingAction = 0;
    private final static int connectionTimeout = 60000;

    private boolean _isHost;

    private Socket _socket;
    private BufferedReader _in;
    private PrintWriter _out;

    private boolean _connected;

    private String _ip;
    private int _port;

    private String _msg;

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
        this("Network", isHost);
    }

    public Network(String name, boolean isHost) {
        super(name);

        _isHost = isHost;
        _connected = false;

        _socket = null;
        _in = null;
        _out = null;

        _ip = null;
    }

    private boolean isValidIP(String ip) {
        return Pattern.matches("((1?[0-9][0-9]?|2[0-4][0-9]|25[0-5])\\.){3}((25[0-5])|(2[0-4][0-9])|(1?[0-9][0-9]?))", ip);
    }

    private boolean isValidPort(int port) {
        return Pattern.matches("[0-9]{1,8}", port + "");
    }

    /**
     * Connects to the given ip and port.
     *
     * @param 	port
     * 			The port that the new server is going to use. (Better choose a high number)
     * @return	{@code True} if the connection was successful, {@code false} if it failed.
     */

    public void connect(String ip, int port) throws Exception {
        if (!isValidIP(ip)) throw new Exception("Please enter a correct ip-adress.");
        if (!isValidPort(port)) throw new Exception("Please enter a correct ip-adress.");

        _port = port;
        _ip = ip;

        if(!_isHost) connectClient();
    }

    public void connect(int port) throws Exception {
        _port = port;

        if(_isHost) connectHost();
    }

    /**
     * Starts a new server and waits for a client to connect.
     * Also checks if the connection was successful.
     *
     * @return	{@code True} if the connection was successful, {@code false} if it failed.
     */

    private void connectHost() throws Exception {
        if(!_isHost) return;

        try {
            System.out.println("Binding to port " + _port + " ...");
            ServerSocket serverSocket = new ServerSocket(_port);
            serverSocket.setSoTimeout( connectionTimeout );

            System.out.println("Waiting for a client ...");
            _socket = serverSocket.accept();
            serverSocket.close();
            serverSocket = null;

            System.out.println("Client accepted.");

            String connectedIp = _socket.getInetAddress().getHostAddress();

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
        if (_isHost) return;


        System.out.println("Connecting to port " + _port + " ...");

        Timer timer = new Timer(connectionTimeout);
        while(!_connected && timer.hasTime()) {
            try {

                _socket = new Socket(_ip, _port);
                System.out.println("Client connected.");
                _connected = true;
                open();

            } catch(IOException e) {
                System.out.println("Failed to connect: " + e.getMessage());
            }
        }

        if(!timer.hasTime())
            throw new Exception("Connection timeout.");
    }

    @Override
    public void run() {
        //Timer timer = new Timer(500);

        send(pingAction + "");

        String s;
        while(!isEnd()) {
            try {
                s = _in.readLine();
                if(s != null) { // Message received
                    if(s.charAt(0) != pingAction) {
                        _msg = s;
                        fireGameEvent(SEND_EVENT);
                    }
                     //evaluateString(s);
                }
            } catch (Exception e) { }
        }

        /*
        if(!timer.hasTime() && !isEnd()) {
            System.out.println("Connection error.");
            connectionError();
        }
        */

        close();
    }

    public String getMessage() {
        return _msg;
    }

    private void connectionError() {
        _connected = false;
        fireGameEvent(DISCONNECT_EVENT);
    }

    private void evaluateString(String s) {
        if(validMessage(s))  {
            byte action = getAction(s);

            switch (action) {
                case SHOOT_EVENT:
                    convertShot(s);

                    fireGameEvent(SHOOT_EVENT);
                    break;
                case SHOOT_RESULT:
                    convertShot(s);

                    fireGameEvent(SHOOT_RESULT);
                    break;
                case CLOSE_EVENT:
                    close();
                    end();
                    fireGameEvent(CLOSE_EVENT);
                    break;
                default:
                    break;
            }

        }
    }

    private boolean validMessage(String s) {
        return Pattern.matches("[0-9]{1,3}(//[0-9]{1,2}(:[0-9]{1,2}){2})?", s);
    }

    private void convertShot(String s) {
        if(validMessage(s)) {
            String values = s.split("//")[1];
            int x = Integer.parseInt( values.split(":")[0] );
            int y = Integer.parseInt( values.split(":")[1] );
            byte res = (byte) Integer.parseInt( values.split(":")[2] );
            setX(x);
            setY(y);
            setResult(res);
        }
    }

    private byte getAction(String s) {
        if(validMessage(s)) {
            String action = s.split("//")[0];
            return (byte) Integer.parseInt(action);
        } else return pingAction;
    }

    public void shootField(int y, int x, byte res) {
        send( SHOOT_EVENT + "//" + x + ":" + y + ":" + res);
    }

    public void shootResult(int y, int x, byte res) {
        if(_isHost) {
            send( SHOOT_RESULT + "//" + x + ":" + y + ":" + res);
        }
    }

    public void reconnect() throws Exception {
        if(_connected) return;

        connect(_ip, _port);
    }



    /**
     * Opens the connection.
     */

    private void open() throws Exception {
        if(!_connected) return;

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
        _connected = false;

        try{
            if(_socket != null) _socket.close();
            if(_in != null) _in.close();
            if(_out != null) _out.close();

            _socket = null;
            _in = null;
            _out = null;


        } catch (IOException e) { System.out.println("Failed to close: " + e.getMessage()); }

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

    public boolean connected() {
        return _connected;
    }

    @Override
    public void end() {
        if(_connected) send( CLOSE_EVENT + "");
        super.end();
    }
}
