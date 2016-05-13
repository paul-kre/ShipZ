package shipz;

import shipz.gamemode.TempKiGame;
import shipz.util.GameEventSource;

/**
 * Created by Paul on 06.05.2016.
 */
public abstract class Player extends GameEventSource implements Runnable {
    private String _name;

    public Player(){}
    
    public Player(String name) {
        _name = name;
    }

    public String name() {
        return _name;
    }
    
    
    /**
	 * Initiiert Beschuss auf eine Zelle
	 * shootField-Methode f�r die Klassen Netzwerk und GUI
	 * @return Abzuschie�ende Zelle auf dem Spielfeld
	 */
	public abstract int[] shootField();
    
	/**
	 * Initiiert Beschuss auf eine Zelle
	 * shootField-Methode f�r die Klasse Computer und deren Subklassen
	 * @return Abzuschie�ende Zelle auf dem Spielfeld
	 */
	public abstract int[] shootField(TempKiGame game);

	//public abstract void end();
}
