package shipz;

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
	 * shootField-Methode für die Klassen Netzwerk und GUI
	 * @return Abzuschießende Zelle auf dem Spielfeld
	 */
	public abstract String shootField();

	//public abstract void end();
}
