package shipz.network;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
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
 * 	if(server.connect(5555)) {
 * 		// Server is running and connected to the client
 *
 * 		server.close();
 * 	} else {
 * 		// Something went wrong.
 * 		System.err.println(server._error());
 * 	}
 * </pre></blockquote><p>
 * This is how to connect to a server as a client:
 * <blockquote><pre>
 * 	Network client = new Network(false);
 * 	if(client.connect("localhost", 5555)) {
 * 		// Client is connected to the client.
 *
 * 		client.close();
 * 	} else {
 * 		System.err.println(client._error());
 * 	}
 * </pre></blockquote>
 *
 * @author Paul Kretschel
 *
 */

public class Network extends GameEventSource implements Runnable {

    private final static int CONNECTION_TIMEOUT = 5000;

    private boolean _isHost;

    private Socket _socket;
    private BufferedReader _in;
    private PrintWriter _out;

    private String _error;
    private boolean _connected;


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
    }



    public void run() {
        try {

            String s;
            boolean done = false;
            while(!done && _connected) {

                if((s = _in.readLine()) != null)

                    if(s.equals(".close")) {
                        send(".close");
                        _connected = false;
                    } else {
                        //System.out.println("\nReceived: " + s);
                        received(s);
                    }

            }

            close();

        } catch (IOException e) {
            _error = e.getMessage();
        }
    }

    /**
     * Starts a new server and waits for a client to connect.
     * Also checks if the connection was successful.
     *
     * @param 	port
     * 			The port that the new server is going to use. (Better choose a high number)
     * @return	{@code True} if the connection was successful, {@code false} if it failed.
     */

    public void connect(int port) {
        if(!_isHost)
            return;

        try {
            System.out.println("Binding to port " + port + " ...");
            ServerSocket serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout( CONNECTION_TIMEOUT );

            System.out.println("Server started.");
            System.out.println("Waiting for a client ...");
            _socket = serverSocket.accept();
            serverSocket.close();
            serverSocket = null;

            System.out.println("Client accepted: " + _socket.getInetAddress());

            open();

            _connected = true;

        } catch(SocketTimeoutException s) {
            _error = "Connection timeout";
        } catch(IOException e) {
            _error = e.getMessage();
        } catch(IllegalArgumentException i) {
            _error = i.getMessage();
        }
    }

    /**
     * Connects to a server with the specified ip and port.
     * Also checks if the connection was successful.
     *
     * @param	ip
     * 			The ip that the server is using.
     * 			"localhost" if client and server are running on the same machine.
     * @param 	port
     * 			The port that the server is using.
     * @return	{@code True} if the connection was successful, {@code false} if it failed.
     */

    public void connect(String ip, int port) {
        if (_isHost)
            return;

        try {
            System.out.println("Connecting to port " + port + " ...");
            _socket = new Socket();
            _socket.connect(new InetSocketAddress(ip, port), CONNECTION_TIMEOUT );
            System.out.println("Client connected.");

            open();

            _connected = true;


        } catch(SocketTimeoutException s) {
            _error = "Connection timeout";
        } catch(IOException e) {
            _error = e.getMessage();
        } catch(IllegalArgumentException i) {
            _error = i.getMessage();
        }
    }



    /**
     * Opens the connection.
     */

    private void open() throws IOException {
        InputStream inputStream;

        inputStream = _socket.getInputStream();
        _in = new BufferedReader( new InputStreamReader( inputStream ));

        OutputStream outputStream = _socket.getOutputStream();
        _out = new PrintWriter( outputStream );

        _connected = true;
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

            System.out.println("Connection closed.");

        } catch (IOException e) { _error = e.getMessage(); }

    }



    /**
     * Listens for a message from the connected computer and returns it as a {@code String}.
     *
     * @return	A String that contains the connected computer's data.
     */

    public String listen() {
        String s = null;
        boolean done = false;
        while(!done) {

            try {
                if((s = _in.readLine()) != null)
                    done = true;

            } catch (IOException e) {
                _error = e.getMessage();
                done = true;
            }

        }
        return s;
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
    }

    public boolean connected() {
        return _connected;
    }

    public String error() {
        return _error;
    }

    public String toString() {
        if(_isHost)
            return "Host";
        else
            return "Client";
    }

}
