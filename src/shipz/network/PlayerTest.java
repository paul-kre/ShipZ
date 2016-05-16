package shipz.network;

import shipz.util.GameEventSource;

/**
 * Created by Paul on 06.05.2016.
 */
public abstract class PlayerTest extends GameEventSource implements Runnable {
    private String _name;
    protected Shot _shot;

    public PlayerTest(){}

    public PlayerTest(String name) {
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
	public abstract String shootField();

    public abstract Shot getShot();
    public abstract void shootField(Shot shot);
    public abstract void shootInfo(Shot shot);

    public abstract void end();
}
