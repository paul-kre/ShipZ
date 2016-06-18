
package shipz.network;

import shipz.util.GameEventListener;
import shipz.util.GameEvent;

import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Created by Paul on 05.05.2016.
 */

public class GameTest implements GameEventListener {

    private Network network;

    private static Scanner in = new Scanner(System.in);

    public static void main(String[] args) {
        GameTest game = new GameTest();
        game.init();
    }

    public void init() {
        System.out.println("--- ShipZ Game ---\n");
        String horj = ask("Host or join game? (h/j)", "[h|j]");

        if(horj.charAt(0) == 'h')
            network = new Network(true);
        else
            network = new Network(false);

        String ip = ask("Please enter your opponent's ip adress.", "((1?[0-9][0-9]?|2[0-4][0-9]|25[0-5])\\.){3}((25[0-5])|(2[0-4][0-9])|(1?[0-9][0-9]?))");
        int port = Integer.parseInt( ask("Please enter the port.", "[0-9]{1,8}") );

        network.connect(ip, port);



    }

    private static String ask(String q, String regex) {
        System.out.println(q);
        String input = null;
        boolean valid = false;
        while(!valid) {
            input = in.nextLine();
            if(!Pattern.matches(regex, input))
                System.err.println("Wrong input.\n");
            else valid = true;
        }
        return input;
    }

    @Override
    public void eventReceived(GameEvent e) {

    }
}
