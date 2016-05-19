package shipz.network;

import shipz.Player;

import java.util.Scanner;


/**
 * Created by Paul on 06.05.2016.
 */
public class Keyboard extends PlayerTest {

    private Scanner _keyboard;
    private boolean _end;

    public Keyboard(String name) {
        super(name);
        _keyboard = new Scanner(System.in);
        _end = false;
        _shot = null;
    }

    @Override
    public void run() {
        while(!_end) {
            System.out.print("> ");
            String input = _keyboard.nextLine();

            if(!_end)
                if(input.equals("close")) {
                    fireGameEvent(CLOSE_EVENT);
                    _end = true;
                } else if (validShot( input )) {
                    int x = convertX(input);
                    int y = convertY(input);
                    _shot = new Shot(x, y);
                    fireGameEvent(SHOOT_EVENT);
                }
        }
    }

    private boolean validShot(String s) {
        String[] split = s.split(":");
        if(split.length != 2) return false;
        try {
            int x = Integer.parseInt(split[0]);
            int y = Integer.parseInt(split[1]);
        } catch (NumberFormatException e) {
            return false;
        }

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

    public Shot getShot() {
        return _shot;
    }

    public void shootField(Shot shot) {
        System.out.println();
        System.out.println("Opponent shot at: [" + shot.getX() + ":" + shot.getY() + "]");
        if(shot.getHit() == 'x')
            System.out.println("It was a hit!");
    }

    public void shootInfo(Shot shot) {
        System.out.println("You shot at: [" + shot.getX() + ":" + shot.getY() + "]");
        if(shot.getHit() == 'x')
            System.out.println("It was a hit!");
    }

	@Override
	public String shootField() {
		// TODO Auto-generated method stub

		return null;
	}

    @Override
    public void shootResult(int yCoord, int xCoord, byte result) {

    }

    public void end() {
        _end = true;
    }

}
