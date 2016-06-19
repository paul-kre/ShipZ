package shipz.network;

import shipz.Player;
import shipz.util.Timer;

import java.io.InputStream;
import java.util.Scanner;
import java.util.regex.Pattern;


/**
 * Created by Paul on 06.05.2016.
 */
public class Keyboard extends Player {

    private Scanner _keyboard;

    public Keyboard(Scanner in) {
        this(in, "Keyboard");
    }

    public Keyboard(Scanner in, String name) {
        super(name);
        _keyboard = in;
    }

    @Override
    public void run() {
        while(!isEnd()) {
            System.out.print("");
            if(isMyTurn()) shoot();
        }
    }

    private boolean validShot(String s) {
        return Pattern.matches("[0-9]{1,2}:[0-9]{1,2}", s);
    }

    private int convertX(String s) {
        String x = s.split(":")[0];
        return Integer.parseInt( x );
    }

    private int convertY(String s) {
        String y = s.split(":")[1];
        return Integer.parseInt( y );
    }

    public void shootField(int x, int y, char result) {
    }

    @Override
    public void shootResult(int y, int x, byte result) {
        System.out.println();
        System.out.println("You shot at: [" + x + ":" + y + "]");
        if(result == 1)
            System.out.println("It was a hit!");
    }

    @Override
    public void shootField(int y, int x, byte result) {
        System.out.println();
        System.out.println("Opponent shot at: [" + x + ":" + y + "]");
        if(result == 1)
            System.out.println("It was a hit!");
    }

    public void shoot() {
        System.out.println("It's your turn.");
        boolean done = false;
        while(!done && !isEnd()) {
            System.out.print("> ");
            String input = _keyboard.nextLine();

            if(input.equals("close")) {
                fireGameEvent(CLOSE_EVENT);
                end();
                done = true;
            } else if (validShot( input )) {
                setX(convertX(input));
                setY(convertY(input));
                fireGameEvent(SHOOT_EVENT);
                done = true;
            } else {
                System.out.println("Wrong input.\n\n");
                done = false;
            }
        }
    }

}
