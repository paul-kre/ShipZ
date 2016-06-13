
package shipz.network;

import shipz.util.EventIds;
import shipz.util.GameEventListener;
import shipz.util.GameEvent;

import java.util.Scanner;

/**
 * Created by Paul on 05.05.2016.
 */
@Deprecated
public class GameTest implements GameEventListener {
    private PlayerTest _player1;
    private PlayerTest _player2;

    private Scanner _in;

    private boolean _gameOver;

    private boolean _isHost;


    public GameTest() {
        _in = new Scanner( System.in );
        _gameOver = false;
        _isHost = true;
    }

    public void start() {
        Thread t1 = new Thread(_player1);
        Thread t2 = new Thread(_player2);
        t1.start();
        t2.start();

        _player1.setEventListener(this);
        _player2.setEventListener(this);

    }

    @Override
    public void eventReceived(GameEvent e) {
        PlayerTest source = (PlayerTest) e.getSource();
        PlayerTest opponent;

        if(source == _player1)
            opponent = _player2;
        else
            opponent = _player1;

        int id = e.getId();

        int x;
        int y;
        char res = ' ';

        switch (id) {

            case SHOOT_EVENT: // Shoot
                x = source.getX();
                y = source.getY();

                if(_isHost) {
                    res = 'x'; // Check tile
                    source.shootResult(x, y, res);
                }

                opponent.shootField(x, y, res);

                break;
            case SHOOT_RESULT: // Shoot Info
                x = source.getX();
                y = source.getY();
                res = source.getResult();

                if(_isHost) { // is host
                    res = 'x'; // Check tile
                    source.shootResult(x, y, res);
                }

                if(!_isHost) { // is client
                    res = source.getResult();
                    opponent.shootResult(x, y, res);
                }

                break;
            case CLOSE_EVENT: // Close
                System.out.println();
                System.out.println("Game Over.");
                source.end();
                opponent.end();
                break;
            case DISCONNECT_EVENT: // Disconnect
                System.out.println();
                System.err.println("Connection lost. Trying to reconnect ...");

                Network nw = (Network) source;

                nw.reconnect();

                if(nw.connected()) {
                    System.out.println("Reconnected successfully!");
                    nw.run();
                } else {
                    System.err.println("Reconnecting failed: " + nw.error());
                    source.end();
                    opponent.end();
                }
                break;
            default:
                break;
        }
    }
/*
    public static void main(String[] args) {
        GameTest game = new GameTest();
        game.init();
        game.start();
    }
    */



































    // Trash

    public void init() {
        System.out.println("--- SHIPZ GAME ---\n\n");
        boolean valid;

        String name = "";
        char hostOrClient = ' ';

        valid = false;
        while(!valid) {
            name = ask("What is your name? ( max. 15 characters )");
            if(name.length() > 15)
                System.err.println("\nThe name is too long ...\n");
            else if(name.isEmpty())
                System.err.println("\nYou need a name ...\n");
            else
                valid = true;

        }

        _player1 = new Keyboard(name);

        valid = false;
        while(!valid) {
            hostOrClient = ask("Do you want to be host or client? ( h / c )").charAt(0);
            if(hostOrClient == 'h' || hostOrClient == 'c')
                valid = true;
            else
                System.err.println("\nIncorrect input ...\n");
        }

        if(hostOrClient == 'h') {
            _player2 = createHost();

        } else {
            _player2 = createClient();
        }
    }


    private Network createHost() {
        Network host = new Network("Client", true);

        int port = getPort();

        while(!host.connected()) {
            host.connect(port);
            if(!host.connected()) {
                System.err.println(host.error());
                System.out.println("\n\nTry again ...\n");
            }
        }

        _isHost = true;

        return host;
    }

    private Network createClient() {
        Network client = new Network("Host", false);

        String ip = getIp();
        int port = getPort();

        while(!client.connected()) {
            client.connect(ip, port);
            if(!client.connected()) {
                System.err.println(client.error());
                System.out.println("\n\nTry again ...\n");
            }
        }

        _isHost = false;

        return client;
    }

    private String getIp() {
        String ip = "";
        boolean valid = false;
        while(!valid) {
            ip = ask("Please enter your opponent's IP-address ( XXX.XXX.XXX.XXX or \"localhost\" )");
            valid = validIp(ip) || ip.equals("localhost");
            if(!valid)
                System.err.println("\nIncorrect input ...\n");
        }
        return ip;
    }

    private int getPort() {
        int port = -1;
        boolean valid = false;
        while(!valid) {
            try {
                port = Integer.parseInt( ask("Please enter a valid port") );
            } catch (NumberFormatException e) {

            }
            valid = (port > 0 && port < 1000000);
            if(!valid)
                System.err.println("\nIncorrect input ...\n");
        }
        return port;
    }

    private boolean validIp(String s) {
        String[] split = s.split("\\.");
        if(split.length !=4) return false;

        return true;
    }

    private String ask(String question) {
        System.out.print(question + " > ");
        String s = _in.nextLine();
        if(s.equals("quit"))
            System.exit(0);
        System.out.println();
        return s;
    }


}
