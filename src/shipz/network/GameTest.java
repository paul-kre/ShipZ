package shipz.network;

import shipz.Player;
import shipz.util.GameEventListener;
import shipz.util.MessageEvent;
import shipz.util.ShootEvent;

import java.util.EventObject;
import java.util.Scanner;

/**
 * Created by Paul on 05.05.2016.
 */
public class GameTest implements GameEventListener {
    Player _player1;
    Network _player2;

    Scanner _in;

    boolean _gameOver;


    public GameTest() {
        _in = new Scanner( System.in );
        _gameOver = false;
    }

    private void start() {
        Thread t1 = new Thread(_player1);
        Thread t2 = new Thread(_player2);
        t1.start();
        t2.start();

        _player1.setEventListener(this);
        _player2.setEventListener(this);

    }

    @Override
    public void onShoot(ShootEvent e) {
        Player source = (Player) e.getSource();
        System.out.println(source.name() + " fired at: X: " + e.x() + ", Y: " + e.y());
        e.setHit('x');
    }

    @Override
    public void onSurrender(EventObject e) {
        Player source = (Player) e.getSource();
        System.out.println(source.name() + " surrenders.");

        ((Network) _player2).disconnect();
        ((Keyboard) _player1).end();

        _gameOver = true;
    }

    @Override
    public void onClose(EventObject e) {
        Network source = (Network) e.getSource();
        source.disconnect();

        ((Network) _player2).disconnect();
        _gameOver = true;

    }

    @Override
    public void onDisconnect(EventObject e) {
        Network source = (Network) e.getSource();
        System.err.println("The opponent disconnected. \nReconnecting ...");
        source.reconnect();
        if(source.connected()) {
            System.out.println("The opponent reconnected successfully!");
            source.run();
        } else {
            System.err.println("Failed to reconnect: " + source.error());
            source.close();
        }
    }

    @Override
    public void onMessage(MessageEvent e) {
        Player p = (Player) e.getSource();
        System.out.println("\n" + p.name() + " sagt: " + e.msg());

        if(p != _player2)
            _player2.send(e.msg());
    }

    public static void main(String[] args) {
        GameTest game = new GameTest();
        game.init();
    }



































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
            _player2 = new Network("Client", true);

            while(!_player2.connected()) {
                _player2.connect(getPort());
                if(!_player2.connected()) {
                    System.err.println(_player2.error());
                    System.out.println("\n\nTry again ...\n");
                }
            }
        } else {
            _player2 = new Network("Host", false);

            while(!_player2.connected()) {
                _player2.connect(getIp(), getPort());
                if(!_player2.connected()) {
                    System.err.println(_player2.error());
                    System.out.println("\n\nTry again ...\n");
                }
            }
        }

        start();


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
        String[] split = s.split(".");
        if(split.length !=4) return false;
        for (String str : split) {
            if(str.length() != 3) return false;
        }

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
