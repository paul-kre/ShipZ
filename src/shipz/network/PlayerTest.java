package shipz.network;

import shipz.util.GameEventSource;

/**
 * Created by Paul on 06.05.2016.
 */
public abstract class PlayerTest extends GameEventSource implements Runnable {
    private String _name;
    private int _x;
    private int _y;
    private char _result;

    public PlayerTest(String name) {
        _name = name;
    }

    public String name() {
        return _name;
    }




    protected void setX(int x) {
        _x = x;
    }

    protected void setY(int y) {
        _y = y;
    }

    protected void setResult(char res) {
        _result = res;
    }

    public int getX() {
        return _x;
    }

    public int getY() {
        return _y;
    }

    public char getResult() {
        return _result;
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
     *
     */
    public abstract void shootResult (int yCoord, int xCoord, byte result);

    public abstract void shootField(int x, int y, char result);
    public abstract void shootResult(int x, int y, char result);

    public abstract void end();
}
