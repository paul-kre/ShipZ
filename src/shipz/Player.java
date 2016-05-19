package shipz;

import shipz.util.GameEventSource;

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

    /**
     * Rückgabeinformationen der Verwaltung über den aktuellen Status,
     * nachdem man das Feld beschossen hat.
     *
     * @param yCoord Y-Koordinate
     * @param xCoord X-Koordinate
     * @param result Ergebnis der beschossenen Y- und X-Koordinaten. Klassen werten für sich das Ergebnis aus
     *               und führen entsprechende Aktionen aus.
     */
    public abstract void shootResult (int yCoord, int xCoord, byte result);

	//public abstract void end();
}
