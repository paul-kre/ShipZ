package shipz.network;

import shipz.Player;

import java.util.Scanner;

import gamemode.TempKiGame;

/**
 * Created by Paul on 06.05.2016.
 */
public class Keyboard extends Player implements Runnable {

    private Scanner _keyboard;

    public Keyboard(String name) {
        super(name);
        _keyboard = new Scanner(System.in);
    }

    @Override
    public void run() {

    }

	@Override
	public int[] shootField() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] shootField(TempKiGame game) {
		// TODO Auto-generated method stub
		return null;
	}
}
