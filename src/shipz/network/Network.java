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
 * The {@code Network} class establishes a connection to another instance of the game.
 * It represents either a host, that waits for the response of a client, or a client, that connects to a host.
 * The methods of this class allow you to send and recieve information from a connected computer.
 * <p>
 * The following example shows you how to start a host and accept a connecting client.
 * <blockquote><pre>
 *  Network host = new Network(true); // create a host
 *  int port = 60000; // chose a port
 *  try {
 *      // try to connect to the client
 *      host.connect( port );
 *  } catch(Exception e) {
 *      // output an error message, in case the connection went wrong.
 *      // (send it to the GUI, so it can be shown to the user)
 *      System.err.println(e.getMessage());
 *  }
 * </pre></blockquote><p>
 * This is how to connect to a host as a client:
 * <blockquote><pre>
 *  Network client = new Network(false); // create a client
 *  int port = 60000; // chose the same port as the host
 *  String ip = "127.0.0.1"; // chose the host's IP-adress
 *  try {
 *      client.connect( port, ip );
 *  } catch(Exception e) {
 *      System.err.println(e.getMessage());
 *  }
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

    /**
     * Checks if an IP-adress has a correct format.
     *
     * @param   ip
     *          The IP-adress.
     * @return  {@code True}: The IP-adress is valid.
     *          {@code False}: the IP-adress is invalid
     */

    private boolean isValidIP(String ip) {
        return Pattern.matches("((1?[0-9][0-9]?|2[0-4][0-9]|25[0-5])\\.){3}((25[0-5])|(2[0-4][0-9])|(1?[0-9][0-9]?))", ip);
    }

    /**
     * Checks if a port is acceptable for establishing a connection.
     *
     * @param   port
     *          The port.
     * @return  {@code True}: The port is valid.
     *          {@code False}: the port is invalid
     */

    private boolean isValidPort(int port) {
        return Pattern.matches("[0-9]{1,8}", port + "");
    }

    /**
     * Connects a client to the given ip and port.
     *
     * @param   ip
     *          The host's IP-adress.
     * @param   port
     *          The port that the host uses.
     * @throws  Exception
     */

    public void connect(String ip, int port) throws Exception {
        if (!isValidIP(ip)) throw new Exception("Please enter a correct ip-adress.");
        if (!isValidPort(port)) throw new Exception("Please enter a correct ip-adress.");

        _port = port;
        _ip = ip;

        if(!_isHost) connectClient();
    }

    /**
     * Connects a host with the specified port to a client.
     *
     * @param   port
     *          The port (better chose a high number)
     * @throws  Exception
     */

    public void connect(int port) throws Exception {
        _port = port;

        if(_isHost) connectHost();
    }

    /**
     * Starts a new server and waits for a client to connect.
     * Also checks if the connection was successful.
     * Throws an exception if something went wrong.
     *
     * @throws Exception
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
     * Connects to a client to a host with the specified ip and port.
     * Also checks if the connection was successful.
     * Throws an exception if something went wrong.
     *
     * @throws Exception
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

    /**
     * Checks continuously, if there is a new message from the other network of the other side.
     * If there is new data, it fires an event, so the Main knows about it.
     */

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
                        evaluateString(s);
                    }
                }
            } catch (Exception e) { }
        }

        close();
    }

    /**
     * Returns the latest message, which the network received from the other side.
     *
     * @return  The message
     */

    public String getMessage() {
        return _msg;
    }

    /**
     * Evaluates the message from the other side of the network.
     * Fires an ebent if needed.
     *
     * @param   s
     *          The message.
     */

    private void evaluateString(String s) {
        byte action = Byte.parseByte(s.split(":")[0]);

        switch (action) {
            case NET_SHOOT_REQUEST:
                String[] values = s.split(":")[1].split(",");
                setY(Integer.parseInt(values[0]));
                setX(Integer.parseInt(values[1]));
                fireGameEvent(NET_SHOOT_REQUEST);
                break;
            default:
                break;
        }
    }

    /**
     * Tells the host, that the client wants to shoot on a field with the specified coordinates.
     * @param   x
     *          X-value
     * @param   y
     *          Y-value
     */

    public void shootRequest(int x, int y) {
        send(NET_SHOOT_REQUEST + ":" + x + "," + y);
    }

    /**
     * Reconnects the network, in case it deconnected.
     *
     * @throws Exception
     */

    public void reconnect() throws Exception {
        if(_connected) return;

        connect(_ip, _port);
    }

    /**
     * Creates the input and output streams.
     * @throws Exception
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
     * Closes the streams and the socket.
     * This should always be done at the end when the {@code Network} is not being used anymore.
     */

    private void close() {
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
     * Sends a message to the other side of the network.
     *
     * @param 	msg
     * 			The message.
     */

    public void send(String msg) {
        _out.write(msg + "\n");
        _out.flush();
    }

    /**
     * Checks if the network is connected.
     *
     * @return  {@code True}: The network is connected.
     *          {@code False}: The network is deconnected.
     */

    public boolean connected() {
        return _connected;
    }

    @Override
    public void shootResult(int yCoord, int xCoord, byte result) {

    }

    /**
     * Closes the connection with all its streams and resets the network.
     * Also quits the runnable network threat.
     */

    @Override
    public void end() {
        if(_connected) send( CLOSE_EVENT + "");
        super.end();
    }


    public String saveCurrentGame() { return null;}
    public void loadPreviousGame (String prevField) {};
}
