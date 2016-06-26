package shipz;

import shipz.util.GameEventSource;
import shipz.util.NoDrawException;

import java.util.ArrayList;
import java.util.StringJoiner;
import java.util.TreeMap;

public abstract class Player extends GameEventSource implements Runnable {
    private String _name;
    private int _x;
    private int _y;
    private byte _result;
    private boolean _turn;

    private boolean _end;

    protected ArrayList<String> undoRedoCoordinates;

    public Player(){
        this("Player");
    }

    public Player(String name) {
        _name = name;
        _end = false;
        _turn = false;
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

    protected void setResult(byte res) {
        _result = res;
    }


    /**
     * Leert die zurückgenommen bzw. wiederhergestellten Züge.<br>
     * Nachdem <b>undoHits</b> oder <b>redoHits</b> aufgerufen wurde, sollte
     * diese Methode ausgeführt werden um beim nächsten Undo/Redo keine Restkoordinaten
     * vom letzten mal bei zu behalten
     */
    protected void resetUndoRedoCoordinates () { undoRedoCoordinates = new ArrayList<>();}

    public int getX() {
        return _x;
    }

    public int getY() {
        return _y;
    }

    public byte getResult() {
        return _result;
    }






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


    /**
     *
     * @param coordinates
     * @return
     */
    private ArrayList<String> extractValidCoordinates ( String coordinates){

        ArrayList<String> extractedCoordinates = new ArrayList<>();

        //String in einzelne Züge unterteilen
        String singleDraws[] = coordinates.split(";");

        /**
         * Der String wird durchiteriert und es werden immer alle Parts,
         * die Koordinaten enthalten, in die Liste gepackt
         */
        for ( int i = 0; i < singleDraws.length; i++){

            extractedCoordinates.add(singleDraws[i].split("\\|")[1]);
        }


        return extractedCoordinates;
    }


    /**
     * Nachricht an den Spieler, dass bestimmte
     * Koordinaten von ihm von dem Spielfeld
     * entfernt wurden.<br>
     *
     * @param coordinateString Liste der zurückgenommen Koordinaten. Spieler muss für seinen Algorithmus passend die
     *                        Koordinaten bearbeiten.
     *
     * @throws NoDrawException Wenn der String mit den Koordinaten falsch abgetrennt oder leer ist
     */
    public void undoHits(String coordinateString) throws NoDrawException {

        //Wenn der String leer ist, können keine Koordinaten zurückgenommen werden
        if ( coordinateString.isEmpty()){
            throw new NoDrawException("No existing draws to undo!");
        }

        //Aus der übergebenen Undo Liste die entgültigen Züge herausziehen
        undoRedoCoordinates = extractValidCoordinates(coordinateString);

    }


    /**
     * Nachricht an den Spieler, dass bestimmte
     * Koordinaten von ihm auf dem Spielfeld
     * wiederhergestellt wurden.<br>
     *
     * @param coordinateString Liste der wiederhergsetellten Koordinaten. Spieler muss für seinen Algorithmus passend die
     *                        Koordinaten neu setzen.
     * @throws NoDrawException Wenn der String mit den Koordinaten falsch abgetrennt oder leer ist
     */
    public void redoHits(String coordinateString) throws NoDrawException {

        //Wenn der String leer ist, können keine Koordinaten wiederhergestellt werden
        if ( coordinateString.isEmpty()){
            throw new NoDrawException("No existing draws to redo!");
        }

        //Aus der übergebenen Redo Liste die entgültigen Züge herausziehen
         undoRedoCoordinates = extractValidCoordinates(coordinateString);

    }



    public void shootField(int x, int y, byte result) {}

    public void turn() {
        _turn = true;
    }

    public boolean isMyTurn() {
        if(_turn) {
            _turn = false;
            return true;
        } else return false;
    }

	public void end() {
        _end = true;
    }

    protected boolean isEnd() {
        return _end;
    }

    @Override
    public String toString() {
        return _name;
    }
}
