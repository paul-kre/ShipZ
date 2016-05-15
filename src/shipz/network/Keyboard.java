package shipz.network;

import shipz.Player;

import java.util.Scanner;


/**
 * Created by Paul on 06.05.2016.
 */
public class Keyboard extends Player implements Runnable {

    private Scanner _keyboard;
    private boolean _end;

    public Keyboard(String name) {
        super(name);
        _keyboard = new Scanner(System.in);
        _end = false;
    }

    @Override
    public void run() {
        while(!_end) {
            System.out.print("> ");
            String input = _keyboard.nextLine();
            if(input.equals("close")) {
                //fireSurrenderEvent();
                _end = true;
            } else if (!input.isEmpty()) {
                //fireMessageEvent(input);
            }
        }
    }

    public void end() {
        _end = true;
    }

	@Override
	public String shootField() {
		// TODO Auto-generated method stub
		return null;
	}

}
