
package shipz.network;

import shipz.Player;
import shipz.util.GameEventListener;
import shipz.util.GameEvent;

import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Created by Paul on 05.05.2016.
 */

public class GameTest implements GameEventListener {

    private Player player1;
    private Player player2;

    private Network network;
    private boolean isHost;

    private Scanner in;

    public GameTest() {
        player1 = null;
        player2 = null;
        network = null;
        in = new Scanner(System.in);
        isHost = true;
    }

    public static void main(String[] args) {
        GameTest game = new GameTest();
        game.init();
    }

    public void init() {
        System.out.println("--- ShipZ Game ---\n");

        initPlayers();

    }

    private void initPlayers() {
        player1 = new Keyboard(in);

        String horj = ask("Host or join game? (h/j)", "(h|j)");
        if(horj.charAt(0) == 'h')
            isHost = true;
        else
            isHost = false;
        network = createNetwork();

        player2 = network;

        player1.setEventListener(this);
        player2.setEventListener(this);

        (new Thread(player1)).start();
        (new Thread(player2)).start();

        if(isHost) player1.turn();
    }

    private void gameOver() {
        player1.end();
        player2.end();
        System.out.println("GAME OVER");
    }

    private Network createNetwork() {
        if(isHost) return createHost();
        else return createClient();
    }

    private Network createHost() {
        Network nw = new Network(true);
        boolean done = false;
        while(!done) {
            try {
                nw.connect(getPort());
                done = true;
            } catch (Exception e) {
                System.out.println("Failed to create host: " + e.getMessage() + "\n\n");
                done = false;
            }
        }

        return nw;
    }

    private Network createClient() {
        Network nw = new Network(false);
        boolean done = false;
        while(!done) {
            try {
                nw.connect(getIp(), getPort());
                done = true;
            } catch (Exception e) {
                System.out.println("Failed to create client: " + e.getMessage() + "\n\n");
                done = false;
            }
        }

        return nw;
    }

    private int getPort() {
        return Integer.parseInt( ask("Please enter the port.", "[0-9]{1,8}") );
    }

    private String getIp() {
        return ask("Please enter your opponent's ip adress.", "((1?[0-9][0-9]?|2[0-4][0-9]|25[0-5])\\.){3}((25[0-5])|(2[0-4][0-9])|(1?[0-9][0-9]?))");
    }

    private void connect(Network nw) {
        boolean done = false;
        while(!done) {
            String ip = ask("Please enter your opponent's ip adress.", "((1?[0-9][0-9]?|2[0-4][0-9]|25[0-5])\\.){3}((25[0-5])|(2[0-4][0-9])|(1?[0-9][0-9]?))");
            int port = Integer.parseInt( ask("Please enter the port.", "[0-9]{1,8}") );

            try {
                nw.connect(ip, port);
                nw.setEventListener(this);
                done = true;
            } catch (Exception e) {
                System.out.println(e.getMessage() + "\n\n");
                done = false;
            }
        }
    }



    private String ask(String q, String regex) {
        System.out.println(q);
        String input = null;
        boolean valid = false;
        while(!valid) {
            input = in.nextLine();
            if(!Pattern.matches(regex, input))
                System.out.println("Wrong input.\n");
            else valid = true;
        }
        return input;
    }

    private void reconnect(Network nw) {
        System.out.println("Trying to reconnect ...");
        try {
            nw.reconnect();
            System.out.println("Reconnecting was successful!");
        } catch (Exception e) {
            System.out.println("Reconnecting failed: " + e.getMessage() + "\n\n");
            gameOver();
        }
    }

    @Override
    public void eventReceived(GameEvent e) {
        Player p = (Player) e.getSource();
        int id = e.getId();

        Player op;
        if(p == player1) op = player2;
        else op = player1;

        switch(id) {
            case SHOOT_EVENT:
            case NET_SHOOT_EVENT:
                int x = p.getX();
                int y = p.getY();
                byte res = 1;
                p.shootResult(y, x, res);
                op.shootField(y, x, res);
                op.turn();
                break;
            case DISCONNECT_EVENT:
                Network nw = (Network) p;
                System.out.println("Connection lost.");
                String reco = ask("Do you want to try to reconnect? (j/n)", "(j|n)");
                if(reco.charAt(0) == 'j') reconnect(nw);
                else gameOver();
                break;
            case CLOSE_EVENT:
                System.out.println(p + " left the game.");
                gameOver();
                break;
        }
    }
}
