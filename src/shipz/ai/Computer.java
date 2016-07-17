package shipz.ai;

import java.util.*;

import shipz.Player;
import shipz.util.NoDrawException;


/**
 * Hauptklasse für die Künstliche Intelligenz<br><br>
 *
 * Enthält die notwendigen Grundelemente die alle Künstlichen
 * Intelligenzen zum Laufen brauchen:<br><br>
 *
 * - Speichern von schon beschossenen Koordinaten und getroffenen Schiffsteilen<br>
 * - Absuchen nach weiteren Schiffsteilen, nachdem ein Schiff getroffen wurde<br>
 * - Umgebung eines Schiffes speichern, nachdem ein Schiff zerstört wurde<br>
 * - Ausschließen von vollen Reihen und Spalten für die Generierung weiterer Koordinaten<br>
 * - Ergebnisse der eigenen Beschüsse auswerten und entsprechende Maßnahmen ergreifen.
 *   (Bezieht sich in dieser Klasse auf die Grundlegenen Elemente. In den nachfolgenden
 *   Subklassen werden weitere Ergebnisse ausgewertet und mehr Maßnahmen ausgeführt)
 *
 *
 * @author Artur Hergert
 *
 */
public abstract class Computer extends Player {


    //IV


    /** Die Feldgröße des aktuellen Spiels. Standardmäßig ist es 10 x 10  */
    protected int fieldSize = 10;

    /**
     * Zweidimensionales Byte-Array, welches als Kopie des zu beschießenen Feldes dient.<br>
     * Speichert die Felder die man schon getroffen hat und die man nicht mehr
     * beschießen darf.<br>
     * h
     * Die Indices können folgende Zustände aufweisen:<br>
     * 0: Noch nicht beschossen<br>
     * 1: Nicht mehr beschießbar (Wasser getroffen bzw. Umgebung eines Schiffes)<br>
     * 2: Schiffsteil getroffen<br>
     *  */
    private byte[][] mirrorField;

    /**
     * Speichert zur Identifikation die Koordinaten des Schiffsteil, welches zuerst bei einem Schiff getroffenen wurde,
     * um von dort aus alle Richtungen zu überrpüfen.<br><br>
     *
     * Werte werden beim instanziieren standardweise auf -1|-1 gesetzt.<br>
     * Dieser Zustand bedeutet, dass in dem Moment keine gültige,
     * zuletzt getroffene Koordinate existiert. Bei der Überprüfung dieses
     * Standardwerts weiß die KI, ob aktuell ein Schiff getroffen wurde
     * oder nicht
     * */
    private int[] firstShipTileHit = new int[] {-1, -1};

    /**
     * Die aktuell getroffenen Y- und X-Koordinaten eines Schiffes der KI werden zur
     * Weiterverwendung gespeichert.<br>
     *
     */
    private int[] currentShipTile = new int[]{0, 0};


    /**
     * Die aktuelle Richtung, in der die KI ein getroffenes Schiffsteil auf weitere
     * Schiffsteile überpüft<br><br>
     *
     * Variable kann folgende werte enthalten:<br>
     *     0: Norden wird überprüft<br>
     *     1: Süden wird überprüft<br>
     *     2: Westen wird überprüft<br>
     *     3: Osten wird überprüft<br><br>
     *
     */
    private byte currentDirection = 0;



    /** Random-Objekt zur Generierung von zufälligen Integer Zahlen */
    Random random = new Random();


    /**
     * Liste in der Zahlen von 0 bis <b>fieldSize</b>-1 gespeichert werden, die von der KI
     * noch zufällig generiert werden können.<br>
     * Sobald eine Reihe im Spielfeld voll ist, muss keine Koordinate mehr für diese erstellt werden und
     * die Reihe wird aus der ArrayListe gelöscht.<br>
     * Sorgt für mehr Effizienz bei der Generierung der Koordinaten,
     * da die maximale Durchlaufzahl dadurch verringert wird.
     */
    protected ArrayList<Integer> includedRows =  new ArrayList<>();


    /**
     * Liste in der Zahlen von 0 bis <b>fieldSize</b>-1 gespeichert werden, die von der KI
     * noch zufällig generiert werden können.<br>
     * Sobald eine Spalte im Spielfeld voll ist, muss keine Koordinate mehr für diese erstellt werden und
     * die Spalte wird aus der ArrayListe gelöscht.<br>
     * Sorgt für mehr Effizienz bei der Generierung der Koordinaten,
     * da die maximale Durchlaufzahl dadurch verringert wird.
     */
    protected ArrayList<Integer> includedColumns =  new ArrayList<>();




    /**
     * Optionsvariable für das Platzieren von Schiffen an der
     * Kante eines anderen Schiffes.<br>
     * Wenn es TRUE ist, ist das Platzieren erlaubt,
     * ansonsten nicht
     */
    private boolean placingShipsAtEdgePossible;


    /**
     * Speicherung der Anzahl der Schiffe,
     * die im Spiel auf dem Spielfeld
     * pro Spieler gesetzt werden.<br><br>
     *
     * Jeder Key repräsentiert eine Schiffsgröße und
     * dessen Value enthält die Anzahl der
     * entsprechenden Schiffe.<br>
     * Die Keys werden folgendermaßen eingeteilt:<br>
     *     1: 1er-Schiffe<br>
     *     2: 2er-Schiffe<br>
     *     3: 3er-Schiffe<br>
     *     4: 4er-Schiffe<br>
     *     5: 5-er Schiffe<br>
     */
    protected TreeMap<Integer, Integer> shipList = new TreeMap<>();



    /**
     * Zählt die Länge eines gerade beschossenen Schiffes. Sobald
     * das betrachtete Schiff zerstört wird, wird der Wert auf
     * den Anfangswert 1 gesetzt.<br><br>
     *
     * Der Anfangswert beträgt 1, weil das Schiffsteil, von dem
     * aus in alle Richtungen nach anderen Schiffsteilen gesucht wird, sich
     * nicht selber mitzählt, weshalb man immer ein Schiffsteil dazu addieren
     * muss.
     */
    private int hitShipLength = 1;





    //Constructor

    /**
     * Constructor der KI-Superklassen Computer.<br>
     * Computer kann nicht instanziiert werden; der Constructor
     * wird an die Subklassen vererbt.
     *
     * @param newFieldSize Die Feldgröße des aktuellen Spiels. Die zu erstellenden Zufallskoordinaten werden von 1 bis fieldSize generiert.
     * @param placingAtEdge Einstellung ob man Schiffe an der Kante von anderen Schiffen platzieren darf oder nicht
     * @param newShipList Größe und Anzahl von Schiffen die für dieses Spiel verwendet werden
     */
    public Computer(int newFieldSize, boolean placingAtEdge, List<Integer> newShipList){

        fieldSize = newFieldSize;
        placingShipsAtEdgePossible = placingAtEdge;
        initiateMirrorField();

        //ArrayList mit den gültigen Reihen und Spalten initialisieren
        initializeList(includedRows);
        initializeList(includedColumns);

        //Initialisieren der Schiffsliste
        setShipList(newShipList);

    }





    //IM


    /**
     * Implementierung der shootField-Methode von
     * Player ohne Inhalt.
     */
    public void shootField(int x, int y, byte result) { }


    /**
     * Abstrakte Methode die in den Subklassen implementiert wird<br><br>
     *
     * KI generiert mithilfe dessen Algorithmus die nächste
     * Koordinate, die beschossen werden soll und speichert diese
     * in die Instanzvariablen der Superklasse Player.
     */
    protected abstract void generateAICoordinates();




    /**
     * Überprüfen von Koordinaten, ob diese von der KI
     * beschossen wurden und eventuell ein Schiff getroffen/zerstört wurde<br><br>
     *
     * Methode sollte direkt nach dem Aufruf der Main Methode
     * ausgeführt werden, damit eine Generierung von redundanten
     * Koordinaten vermieden werden kann.
     *
     * @param yCoord Y-Koordinate der Zelle
     * @param xCoord X-Koordinate der Zelle
     * @param hitState Status der beschossenen Koordinate.
     *                 byte kann folgenden Status besitzen:<br>
     *                 0: Wasser wurde getroffen.<br>
     *                 1: Schiffsteil wurde getroffen.<br>
     *                 2: Schiffsteil wurde versenkt.<br>
     */
    public void shootResult(int yCoord, int xCoord, byte hitState ){

        /**
         * Erstellte Koordinaten werden gespeichert.
         * Haben die Koordinaten ein Schiff versenkt, wird die getroffene Koordinate
         * als Schiffsteil abgespeichert und die Umgebung des Schiffes wird abgespeichert
         * in das Spiegelfeld.
         *
         * Wenn ein Schiff getroffen, aber nicht versenkt wurde, wird die
         * Koordinate als Schiffsteil abgespeichert.
         *
         * Wenn kein Schiff getroffen wurde, wird die Koordinate nur
         * als "beschossen" markiert und in das Spiegelfeld gespeichert.
         */
        if ( hitState == 2 ){

            setCoordinateShipPart(yCoord, xCoord);

            // Schiff wurde zerstört, deshalb wird das aktuelle Schiff "vergessen"
            resetCurrentShipCheck();

            saveShipVicinity(yCoord, xCoord);

        } else if (hitState == 1){

            /** Wenn zum ersten mal das Schiffsteil eines Schiffes getroffen wurde,
             wird der erste Treffer zum Ankerpunkt für die Suche der anderen Schiffsteile
             */
            if (!isShipTileHit()){
                this.firstShipTileHit[0] = yCoord;
                this.firstShipTileHit[1] = xCoord;
            }

            //Die in der Richtung beschossene Koordinate hat etwas getroffen und wird als
            //aktuell beschossenen Schiffsteil betrachtet
            this.currentShipTile[0]= yCoord;
            this.currentShipTile[1]= xCoord;

            setCoordinateShipPart(yCoord, xCoord);

            //Reihe und Spalte prüfen ob Sie voll ist. Falls ja, werden diese aus den
            //weiteren Generierungen der Zufallszahlen ausgeschlossen
            excludeFullRows(yCoord);
            excludeFullColumns(xCoord);


        } else {

            //Wenn ein Beschuss in einer Richtung keinen Treffer erzielte,
            // Wird die nächste Richtung ab dem Ankerpunkt weitergesucht
            if (isShipTileHit()){
                resetCurrentHitToFirstHit();
                setNextDirection();
            }
            setCoordinateOccupied(yCoord, xCoord);

            //Reihe und Spalte prüfen ob Sie voll ist. Falls ja, werden diese aus den
            //weiteren Generierungen der Zufallszahlen ausgeschlossen
            excludeFullRows(yCoord);
            excludeFullColumns(xCoord);
        }


    }









    /**
     *
     * Initalisierung des Spiegelfeldes.<br>
     *
     * Alle Koordinaten werden auf 0 (noch nicht beschossen) gesetzt und sind
     * standardmäßig beschießbar.
     *
     */
    private void initiateMirrorField (){

        this.mirrorField = new byte[fieldSize][fieldSize];

        for(int i= 0; i < mirrorField.length; i++ ){

            for ( int j = 0; j < mirrorField[i].length; j++ ){

                mirrorField[i][j] = 0;
            }
        }
    }


    /**
     * Koordinaten im Spiegelfeld auf den unbeschiessbaren
     * Zustand ( Wert 1) setzen für das mirrorField
     *
     * @param yCoord Y-Koordinate der Zelle
     * @param xCoord X-Koordinate der Zelle
     *
     */
    private void setCoordinateOccupied(int yCoord, int xCoord) {

        this.mirrorField[yCoord][xCoord] = 1;


    }

    /**
     * Koordinate im Spielfeld auf den Zustand eines
     * getroffenen Schiffsteils (Wert 2)
     * setzen für das mirrorField
     *
     * @param yCoord Y-Koordinate der Zelle
     * @param xCoord X-Koordinate der Zelle
     *
     */
    private void setCoordinateShipPart(int yCoord, int xCoord) {

        this.mirrorField[yCoord][xCoord] = 2;


    }


    /**
     *
     * Prüfen ob eine Koordinate nicht mehr beschießbar ist (Wert 1)
     *
     * @param yCoord Y-Koordinate der Zelle
     * @param xCoord X-Koordinate der Zelle
     *
     * @return Ob eine Koordinate schon besetzt ist oder nicht
     */
    protected boolean isCoordinateOccupied (int yCoord, int xCoord){

        return (this.mirrorField[yCoord][xCoord] == 1);
    }

    /**
     *
     * Prüfen ob auf einer Koordinate im mirrorField ein
     * Schiffsteil getroffen wurde
     *
     * @param yCoord Y-Koordinate der Zelle
     * @param xCoord X-Koordinate der Zelle
     *
     * @return Ob eine Koordinate schon besetzt ist oder nicht
     */
    protected boolean isCoordinateShipPart (int yCoord, int xCoord){

        return (this.mirrorField[yCoord][xCoord] == 2);
    }












    /**
     * Überprüft ob eine Koordinate sich im Spielfeld
     * oder außerhalb befindet.
     * Zur Vorbeugung eines ArrayOutOfBounds
     *
     * @param yCoord Y-Koordinate der Zelle
     * @param xCoord X-Koordinate der Zelle
     * @return Ob eine Koordinate sich im Spielfeld befindet oder nicht
     */
    protected boolean isCoordinateInField(int yCoord, int xCoord){

        return ( (yCoord >= 0 && yCoord < this.fieldSize) && (xCoord >= 0 && xCoord < this.fieldSize) );
    }



    /**
     * Untersucht alle vier Richtungen um einen versenkten Treffer,
     * welches ein Schiff zerstört hat, und speichert die Umgebung ab
     *
     * @param yCoord Y-Koordinate der Zelle
     * @param xCoord X-Koordinate der Zelle
     */
    private void saveShipVicinity(int yCoord, int xCoord){


        //Nördliche Richtung untersuchen
        saveOneShipVicinityDirection (yCoord, xCoord, -1 , 0);

        //Südliche Richtung untersuchen
        saveOneShipVicinityDirection (yCoord, xCoord, 1 , 0);

        //Westliche Richtung untersuchen
        saveOneShipVicinityDirection (yCoord, xCoord, 0 , -1);

        //Östliche Richtung untersuchen
        saveOneShipVicinityDirection (yCoord, xCoord, 0 , 1);
    }


    /**
     * Speichert die vertikalen Seiten eines Schiffsteils als dessen
     * Umgebung ins Spiegelfeld ab<br><br>
     *
     * Geeignet nur für Nördliche und Südliche Richtung
     *
     * @param currentY Aktuelle vertikale Y-Koordinate
     * @param currentX Aktuelle vertikale X-Koordinate
     * @param yDirection X-Richtung für die Überprüfung, ob die untere
     *                   Reihe gespeichert werden kann, wenn die Option
     *                   "Schiffe an der Kante von anderen Schiffen
     *                   platzieren" aktiviert wurde
     * @param xDirection Y-Richtung für die Überprüfung, ob die obere
     *                   Reihe gespeichert werden kann, wenn die Option
     *                   "Schiffe an der Kante von anderen Schiffen
     *                   platzieren" aktiviert wurde
     */
    private void saveShipVerticalSideEdge(int currentY, int currentX, int yDirection, int xDirection){

        //Westlich von current speichern
        if ( isCoordinateInField(currentY, currentX-1) ){
            setCoordinateOccupied(currentY, currentX-1);
        }

        //Östlich von current speichern
        if ( isCoordinateInField(currentY, currentX+1) ){
            setCoordinateOccupied(currentY, currentX+1);
        }



        /**
         * Wenn die Option "Schiffe an der Kante platzieren" aktiviert wurde,
         * wird die obere Reihe nicht als Schiffsumgebung
         * mitgespeichert */
        if ( placingShipsAtEdgePossible && (yDirection == -1 && xDirection == 0) ){

            //Obere Kante wird nicht abgespeichert

        } else { //Bei allen anderen Fällen wird die obere Kante abgespeichert

            //Nord-Westlich von current speichern
            if ( isCoordinateInField(currentY-1, currentX-1) ){
                setCoordinateOccupied(currentY-1, currentX-1);
            }

            //Nord-Östlich von current speichern
            if ( isCoordinateInField(currentY-1, currentX+1) ){
                setCoordinateOccupied(currentY-1, currentX+1);
            }



        }




        /*Nachdem das vertikal platzierte Schiff mit samt Umgebung ins mirrorField gespeichert wurde,
        wird geprüft, ob in dessen Reihen noch freie Plätze sind oder ob die Reihe ausgeschlossen
        werden kann bei der Generierung der Zufallszahlen */
        excludeFullRows(currentY);
        excludeFullRows(currentY+1);
        excludeFullRows(currentY-1);

        excludeFullColumns(currentX);
        excludeFullColumns(currentX-1);
        excludeFullColumns(currentX+1);


    }


    /**
     * Speichert die horizontale Seiten eines Schiffsteils als dessen
     * Umgebung ins Spiegelfeld ab
     *
     * Geeignet nur für Westliche und Östliche Richtung
     *
     * @param currentY Aktuelle horizontale Y-Koordinate
     * @param currentX Aktuelle horizontale X-Koordinate
     * @param yDirection X-Richtung für die Überprüfung, ob die westliche/östliche
     *                   Reihe gespeichert werden kann, wenn die Option
     *                   "Schiffe an der Kante von anderen Schiffen
     *                   platzieren" aktiviert wurde
     * @param xDirection Y-Richtung für die Überprüfung, ob die westliche/östliche
     *                   Reihe gespeichert werden kann, wenn die Option
     *                   "Schiffe an der Kante von anderen Schiffen
     *                   platzieren" aktiviert wurde
     */
    private void saveShipHorizontalSideEdge(int currentY, int currentX, int yDirection, int xDirection){


        //Nördlich von current speichern
        if ( isCoordinateInField(currentY-1, currentX) ){
            setCoordinateOccupied(currentY-1, currentX);
        }

        //Südlich von current speichern
        if ( isCoordinateInField(currentY+1, currentX) ){
            setCoordinateOccupied(currentY+1, currentX);
        }



        /**
         * Wenn die Option "Schiffe an der Kante platzieren" aktiviert wurde,
         * wird die letzte Reihe westlich nicht als Schiffsumgebung
         * mitgespeichert */
        if (placingShipsAtEdgePossible && (yDirection == 0 && xDirection == -1)){

            //Westliche Reihe wird nicht abgespeichert

        } else{ //Bei allen anderen Fällen wird die westliche Kante abgespeichert

            //Süd-Westlich von current speichern
            if ( isCoordinateInField(currentY+1, currentX-1) ){
                setCoordinateOccupied(currentY+1, currentX-1);
            }


            //Nord-Westlich von current speichern
            if ( isCoordinateInField(currentY-1, currentX-1) ){
                setCoordinateOccupied(currentY-1, currentX-1);
            }


        }




        /*Nachdem das horizontal platzierte Schiff mit samt Umgebung in mirrorField gespeichert wurde,
        wird geprüft, ob in dessen Reihen noch freie Plätze sind oder ob die Spalte ausgeschlossen
        werden kann bei der Generierung der Zufallszahlen */
        excludeFullColumns(currentX);
        excludeFullColumns(currentX-1);
        excludeFullColumns(currentX+1);
    }


    /**
     * Speichert die vertikale Oberkante eines Schiffsteils als dessen
     * Umgebung ins Spiegelfeld ab<br><br>
     *
     * Geeignet nur für Nördliche und Südliche Richtung
     *
     * @param currentY Aktuelle horizontale Y-Koordinate
     * @param currentX Aktuelle horizontale X-Koordinate
     */
    private void saveShipHorizontalTopEdge (int currentY, int currentX){

        //current in das Spiegelfeld speichern
        setCoordinateOccupied(currentY, currentX);

        /*Nachdem das horizontal platzierte Schiff mit samt Umgebung in mirrorField gespeichert wurde,
        wird geprüft, ob in dessen Reihen noch freie Plätze sind oder ob die Reihe ausgeschlossen
        werden kann bei der Generierung der Zufallszahlen */
        excludeFullRows(currentY);

        /*Nachdem das horizontal platzierte Schiff mit samt Umgebung in mirrorField gespeichert wurde,
        wird geprüft, ob in dessen Reihen noch freie Plätze sind oder ob die Spalte ausgeschlossen
        werden kann bei der Generierung der Zufallszahlen */
        excludeFullColumns(currentX);



        /**
         * Wenn die Option "Schiffe an der Kante platzieren" aktiviert wurde,
         * werden die oberen und unteren Nachbarn der aktuellen Variable nicht
         * mitgespeichert */

        if (!placingShipsAtEdgePossible){

            //Linker und Rechter Nachbar von current speichern
            if ( isCoordinateInField(currentY, currentX-1) ){ //oben von current

                setCoordinateOccupied(currentY, currentX-1);
            }

            if ( isCoordinateInField(currentY, currentX+1) ){ //unten von current


                setCoordinateOccupied(currentY, currentX+1);
            }

            excludeFullColumns(currentX-1);
            excludeFullColumns(currentX+1);
        }

    }



    /**
     * Speichert die vertikale Oberkante eines Schiffsteils als dessen
     * Umgebung ins Spiegelfeld ab<br><br>
     *
     * Geeignet nur für Nördliche und Südliche Richtung
     *
     * @param currentY Aktuelle horizontale Y-Koordinate
     * @param currentX Aktuelle horizontale X-Koordinate
     */
    private void saveShipVerticalTopEdge (int currentY, int currentX){

        //current in das Spiegelfeld speichern
        setCoordinateOccupied(currentY, currentX);

        /*
        Nachdem das vertikal platzierte Schiff mit samt Umgebung in mirrorField gespeichert wurde,
        wird geprüft, ob in dessen Reihen noch freie Plätze sind oder ob die Reihe ausgeschlossen
        werden kann bei der Generierung der Zufallszahlen */
        excludeFullRows(currentY);



        /**
         * Wenn die Option "Schiffe an der Kante platzieren" aktiviert wurde,
         * werden die linken und rechten Nachbarn der aktuellen Variable nicht
         * mitgespeichert */


        if (!placingShipsAtEdgePossible){

            //Oberer und Unterer Nachbar von current speichern
            if ( isCoordinateInField(currentY-1, currentX) ){ //Links von current

                setCoordinateOccupied(currentY-1, currentX);
            }

            if ( isCoordinateInField(currentY+1, currentX) ){ //Rechts von current

                setCoordinateOccupied(currentY+1, currentX);
            }


            excludeFullRows(currentY+1);
            excludeFullRows(currentY-1);

        }

    }


    /**
     * Prüft ob die übergebene Y- und
     * X-Richtung in die nördliche oder
     * südliche Richtung prüfen
     *
     * @param yDirection X-Richtung
     * @param xDirection Y-Richtung
     *
     * @return Ob in nördliche/südliche Richtung geprüft wird
     */
    private boolean directionIsNorthOrSouth (int yDirection, int xDirection){
        return (yDirection == -1  && xDirection == 0) || (yDirection == 1  && xDirection == 0);
    }

    /**
     * Durchläuft eine Richtung rund um eine getroffene
     * Koordinate die ein Schiff zum Versenken gebracht hat
     * und speichert alle Umgebungskoordinaten des Schiffes ab
     *
     * @param yCoord Y-Koordinate der Zelle
     * @param xCoord X-Koordinate der Zelle
     * @param yDirection Durchsuchung in Y-Richtung
     * @param xDirection Durchsuchung in X-Richtung
     */
    private void saveOneShipVicinityDirection (int yCoord, int xCoord, int yDirection, int xDirection ){


        //Temporäre Koordinaten. Haben zu Beginn die Koordinaten des Treffers, welches ein
        //Schiff zum Versenken gebracht haben. Von da aus werden alle Richtungen geprüft
        int currentY = yCoord;
        int currentX = xCoord;

        //Flag-Variable zum Durchlaufen der Do - While-Schleife. Prüfen immer ob die Richtung weitergeprüft werden soll.
        boolean directionCheck;


        //Richtung durchlaufen
        do{


            directionCheck = false;

            //Es wird pro Schleifendurchgang immer eine Koordinate in die übergebene Richtung weitergegangen
            currentY = currentY + yDirection;
            currentX = currentX + xDirection;

            //Prüfen ob die Koordinate überhaupt im Spielfeld liegt
            if (isCoordinateInField(currentY, currentX) ){

                    //Schlussendeliche Überprüfung ob die Koordinate ein Teil des Schiffes ist
                    if( isCoordinateShipPart (currentY, currentX)  ){

                        //Prüfen ob Nord- oder Südrichtung durchlaufen wird
                        if ( directionIsNorthOrSouth (yDirection, xDirection) ){

                            //Vertikalen Seiten abspeichern
                            saveShipVerticalSideEdge(currentY,currentX, yDirection, xDirection);

                        } else { // Bei West- oder Ostrichtung

                            //Horizontal Seiten abspeichern
                            saveShipHorizontalSideEdge(currentY,currentX, yDirection, xDirection);
                        }



                        //Die Länge des aktuell betrachteten Schiffes wird gespeichert
                        this.hitShipLength++;

                        // Richtung wurde noch nicht zu Ende durchsucht, deshalb läuft die Schleife weiter
                        directionCheck = true;

                    } else { //Koordinate ist kein Schiffsteil

                        //Prüfen ob Nord- oder Südrichtung durchlaufen wird
                        if ( directionIsNorthOrSouth (yDirection, xDirection) ){

                            //Vertikale Seiten abspeichern
									/* Rechter und Linker Nachbar mit current abspeichern, da
									* an diesen Koordinaten keine weiteren Schiffsteile sind und nun die Umgebung
									* in der Richtung fertig gespeichert wird */
                            saveShipHorizontalTopEdge (currentY, currentX);

                        } else { // Bei West- oder Ostrichtung

                            //Horizontal Seiten abspeichern
									/* Oberer und Unterer Nachbar mit current abspeichern, da
									* an diesen Koordinaten keine weiteren Schiffsteile sind und nun die Umgebung
									* in der Richtung fertig gespeichert wird */
                            saveShipVerticalTopEdge (currentY, currentX);
                        }

                        // Schleife zu Ende

                    }

            } else {
                //current ist nicht im Spielfeld -> Schleife zu Ende

            }

        } while ( directionCheck); //end do-while

    }









    /**
     * Füllt eine Liste mit den Werten von 0 bis <b>fieldSize</b>-1 für die Generierung der
     * Zufallszahlen
     *
     * @param list Die Arrayliste die mit standardwerten von 0 bis <b>fieldSize</b> gefüllt werden soll
     */
    private void initializeList ( ArrayList<Integer> list){

        for (int i= 0; i < fieldSize; i++){
            list.add(i);
        }
    }


    /**
     * Gibt eine zufällige Integerzahl für die Verwendung als Y-Koordinate aus der ArrayListe <b>includedRows</b> zurück.<br>
     * Die zufälligen Zahlen können innerhalb 0 bis <b>fieldSize</b>-1 liegen.<br>
     * Ist die Reihe einer Y-Koordinate voll, wurde die Reihe ausgeschlossen und die entsprechende Zahl ist in der
     * ArrayListe nicht mehr verfügbar für den Rest des Spiels.
     *
     * @return zufällige Zahl zwischen 0 und <b>fieldSize</b>, dessen Reihen noch nicht voll sind
     */
    protected int randomRowInt() {


        if (includedRows.size() > 0){
            return this.includedRows.get(random.nextInt(includedRows.size()));
        }

        return random.nextInt(fieldSize);
    }

    /**
     * Gibt eine zufällige integerzahl für die Verwendung als X-Koordinate aus der ArrayListe <b>includedColumns</b> zurück.<br>
     * Die zufälligen Zahlen können innerhalb 0 bis <b>fieldSize</b>-1 liegen.<br>
     * Ist die Reihe einer X-Koordinate voll, wurde die Reihe ausgeschlossen und die entsprechende Zahl ist in der
     * ArrayListe nicht mehr verfügbar für den Rest des Spiels.
     *
     * @return zufällige Zahl zwischen 0 und <b>fieldSize</b>, dessen Spalten noch nicht voll sind
     */
    protected int randomColumnInt() {

        if (includedColumns.size() > 0) {
            return this.includedColumns.get(random.nextInt(includedColumns.size()));
        }

        return random.nextInt(fieldSize);
    }


    /**
     * Prüft eine Reihe, nachdem etwas in dieser gespeichert wurde, und schließt
     * die Generierung von Zufallskoordinaten für diese Reihe in den nächsten Zügen aus,
     * falls die Reihe voll ist.
     *
     * @param row Die Reihe die geprüft und eventuell ausgeschlossen wird. Ist im normalfall eine übergebene Y-Koordinate.
     */
        private void excludeFullRows( int row){

            excludeFullLine (row, 0);

        }


    /**
     * Prüft eine Spalte, nachdem etwas in dieser gespeichert wurde, und schließt
     * die Generierung von Zufallskoordinaten für diese Spalte in den nächsten Zügen aus,
     * falls die Spalte voll ist.
     *
     * @param column Die Reihe die geprüft und eventuell ausgeschlossen wird. Ist im normalfall eine übergebene Y-Koordinate.
     */
    private void excludeFullColumns( int column){

        excludeFullLine (column, 1);

    }


    /**
     * Allgemeine Methode die eine Reihe oder Spalte auf
     * Vollständigkeit überprüft
     *
     * @param line Angegebene Reihe oder Spalte
     * @param orientation Angabe ob es sich um eine Reihe (Wert "0")
     *                    oder um eine Spalte (Wert "1") handelt
     */
    private void excludeFullLine (int line,  int orientation){

        //Zu allererst wird geprüft ob die angegebene Reihe sich innerhalb des Spielfeldes befinden kann ( zwischen 0 und fieldSize)
        if ( line >= 0 && line < this.fieldSize){

            //Variablen die beim Schleifendurchlauf die Koordinaten der entsprechenden Spielfeldhälften prüfen
            int fieldCenter = this.fieldSize / 2;
            int decrementSide = fieldCenter -1;
            int incrementSide = fieldCenter +1;

            //Flag zum Abbrechen der Schleife falls ein leeres Feld gefunden wurde, da man in dem Fall weiss, dass die Reihe nicht voll ist
            boolean emptySlotFound = false;


            boolean fieldCenterIsFree;

            //Je nachdem ob eine Reihe oder Spalte angegeben wird, wird dessen Mittelpunkt überprüft ob es frei ist
            if (orientation == 0){

                fieldCenterIsFree = !isCoordinateOccupied (line, fieldCenter) && !isCoordinateShipPart (line, fieldCenter );
            }else{

                fieldCenterIsFree = !isCoordinateOccupied ( fieldCenter, line ) && !isCoordinateShipPart (fieldCenter, line );

            }



            //Überprüfung ob überhaupt die Mitte des Feldes frei ist. Wenn sie es ist, kann die Reihe schonmal nicht ausgeschlossen werden
            if ( fieldCenterIsFree){


                // Die Mitte der Reihe ist frei, deshalb kann diese noch nicht gelöscht werden. Die Methode endet hier.

            } else{


                do{

                    //Reihe überprüfen
                    if (orientation == 0){


                        //Like Spielfeldhälfte überprüfen

                        if (isCoordinateInField(line, decrementSide )){

                            if ( !isCoordinateOccupied (line, decrementSide) && !isCoordinateShipPart (line, decrementSide ) ){

                                emptySlotFound = true;
                            }
                        }

                        //Rechte Spielfeldhälfte überprüfen
                        if (isCoordinateInField(line, incrementSide )){

                            if ( !isCoordinateOccupied (line, incrementSide) && !isCoordinateShipPart (line, incrementSide ) ){

                                emptySlotFound = true;
                            }
                        }


                        //Spalte überprüfen
                    }else {

                        //Obere Spielfeldhälfte überprüfen

                        if (isCoordinateInField( decrementSide, line)){

                            if ( !isCoordinateOccupied (decrementSide, line) && !isCoordinateShipPart (decrementSide, line ) ){

                                emptySlotFound = true;
                            }
                        }

                        //Untere Spielfeldhälfte überprüfen
                        if (isCoordinateInField(incrementSide, line)){

                            if ( !isCoordinateOccupied (incrementSide, line) && !isCoordinateShipPart (incrementSide, line ) ){

                                emptySlotFound = true;
                            }
                        }

                    }





                    //Zählervariablen entsprechend in/decrementieren um von der Mitte aus pro Durchlauf in die erste und zweite
                    //Spielfeldhälfte zu prüfen
                    decrementSide--;
                    incrementSide++;

                }while ( (decrementSide >= 0  || incrementSide< this.fieldSize) && !emptySlotFound); //end while


                //Nachdem die Schleife durchgelaufen ist und kein freien Platz gefunden hat weiss man, dass die Reihe voll ist.
                //Die Reihe wird in de Fall ausgeschlossen
                if (orientation == 0){

                    if (!emptySlotFound){

                        deleteRowFromList(line);
                    }

                }else{


                    if (!emptySlotFound){

                        deleteColumnFromList(line);
                    }

                }

            }


        }


    }





    /**
     * Löscht eine bestimmte Reihe aus dem ArrayList <b>includedRows</b>, damit es nicht mehr bei der
     * Generierung der Zufallszahlen auftritt.
     *
     * @param row Die Reihe dessen Listenvariable gelöscht wird
     */
    private void deleteRowFromList(int row){

        //Integer.valueOf() muss angegeben werden, weil die Liste ansonsten die Zahl an der Stelle X löschen würde, nicht aber
        //die Zahl mit dem Wert X
        this.includedRows.remove(Integer.valueOf(row));
    }

    /**
     * Löscht eine bestimmte Spalte aus dem ArrayList <b>includedColumns</b>, damit es nicht mehr bei der
     * Generierung der Zufallszahlen auftritt.
     *
     * @param column Die Spalte dessen Listenvariable gelöscht wird
     */
    private void deleteColumnFromList(int column){

        //Integer.valueOf() muss angegeben werden, weil die Liste ansonsten die Zahl an der Stelle X löschen würde, nicht aber
        //die Zahl mit dem Wert X
        this.includedColumns.remove(Integer.valueOf(column));
    }








    /**
     * Prüft ob aktuell ein Schiff getroffen, aber noch nicht versenkt wurde
     *
     * @return Ob ein Schiff getroffen wurde oder nicht
     */
    protected boolean isShipTileHit(){

        return (this.firstShipTileHit[0] != -1  && this.firstShipTileHit[1] != -1);
    }




    /**
     * Die aktuell betrachtete Richtungskoordinate wird auf
     * die zuerst getroffene Koordinate des Schiffes zurückgesetzt
     */
    private void resetCurrentHitToFirstHit(){
        this.currentShipTile[0]= this.firstShipTileHit[0];
        this.currentShipTile[1]= this.firstShipTileHit[1];
    }



    /**
     * Die direkten Nachbaarkoordinaten einer getroffenen Koordinate werden
     * zur Überprüfung ausgewählt
     *
     */
    protected int[] selectNeighbourCoordinates(){


        //Flag, welches versichert, dass aufjedenfall eine Richtung Koordinaten an generateAICoordinates zurückgibt
        boolean directionReturnsNoCoord;

        //Falls schonmal ein Schiff getroffen wurde, wird die Umgebung um den Treffer auf weitere
        //Schiffsteile untersucht

        /** Es wird von dem Treffer in eine Richtung weitergesucht und die nächste Koordinate
         in der Richtung wird zurückgegeben. Wenn zurückgegebene Koordinate nicht im Spielfeld
         liegt, wird die nächste Richtung untersucht. */
        do{

            directionReturnsNoCoord = false;


            //Nordliche Richtung überprüfen
            if (this.currentDirection == 0){

                //Prüfen ob die aktuelle Koordinate in der Richtung überhaupt im Spielfeld liegt
                if( isCoordinateInField (this.currentShipTile[0] -1,  this.currentShipTile[1] ) && !isCoordinateOccupied (this.currentShipTile[0] -1, this.currentShipTile[1] ) ){

                    return new int[]{ this.currentShipTile[0] -1,  this.currentShipTile[1] } ;

                }

                //Südliche Richtung überprüfen
            } else if (this.currentDirection == 1){

                //Prüfen ob die aktuelle Koordinate in der Richtung überhaupt im Spielfeld liegt
                if( isCoordinateInField( this.currentShipTile[0] +1,  this.currentShipTile[1] ) && !isCoordinateOccupied (this.currentShipTile[0] +1,  this.currentShipTile[1]) ){

                    return new int[]{ this.currentShipTile[0] +1,  this.currentShipTile[1] } ;

                }


                //Westliche Richtung überprüfen
            } else if (this.currentDirection == 2){

                //Prüfen ob die aktuelle Koordinate in der Richtung überhaupt im Spielfeld liegt
                if( isCoordinateInField( this.currentShipTile[0] ,  this.currentShipTile[1] -1 ) && !isCoordinateOccupied (this.currentShipTile[0] ,  this.currentShipTile[1] -1)){

                    return new int[]{ this.currentShipTile[0] ,  this.currentShipTile[1] -1 } ;

                }


                //Östliche Richtung überprüfen
            }else if (this.currentDirection == 3){

                //Prüfen ob die aktuelle Koordinate in der Richtung überhaupt im Spielfeld liegt
                if( isCoordinateInField( this.currentShipTile[0],  this.currentShipTile[1] +1 ) && !isCoordinateOccupied (this.currentShipTile[0],  this.currentShipTile[1] +1)){

                    return new int[]{ this.currentShipTile[0] ,  this.currentShipTile[1] +1 } ;

                }


            }

            //Koordinate war nicht im Spielfeld bzw. Richtungskoordinate gibt kein leeres Feld zurück,
            //deshalb wird zur nächsten Richtung gewechselt
            setNextDirection();
            resetCurrentHitToFirstHit();
            directionReturnsNoCoord  = true;

        } while (directionReturnsNoCoord);



        return null;

    }


    /**
     * Nachdem ein getroffenes Schiff zerstört wurde, wird diese
     * Methode aufgerufen um alle Einstellungen für das nächste
     * zu Untersuchende Schiff zurückzusetzen.
     *
     */
    private void resetCurrentShipCheck(){

        this.firstShipTileHit = new int[] {-1, -1};
        this.currentShipTile = new int[]{0, 0};
        this.currentDirection = 0;

    }

    /**
     * Wechselt die aktuell betrachtende Richtung
     * um ein getroffenes Schiff
     *
     */
    private void setNextDirection(){

        this.currentDirection++;

        //Im Falle eines Redos, wird die
        //Richtung zurück auf nördlich gewechselt um die Schiffsteile erneut zu beschießen
        if( this.currentDirection > 3){

            this.currentDirection = 0;
        }
    }
















    /**
     * Schiffsliste wird mit der Anzahl der Schiffe
     * für das startende Spiel initialisiert
     *
     * @param list Übergebene Liste mit der Anzahl der Schiffen
     */
    private void setShipList (List<Integer> list){


        /**
         * Zuerst werden die Indices für die einzelnen
         * Schiffsgrößen erstellt und deren Values am
         * Anfang auf 0 gesetzt
         * */
        for (int i= 1; i <= 5; i++){
            this.shipList.put(i,0);
        }


        /**
         * Die Liste wird durchiteriert und jedes Schiff
         * wird entsprechend seinem Platz in dem TreeMap
         * dazu addiert
         */
        for( Integer l : list){

            this.shipList.put(l, shipList.get(l)+1 );
        }

    }


    /**
     * Das zuletzt zerstörte Schiff wird aus der
     * Schiffsliste gelöscht und die Variable
     * zur Speicherung der Länge des aktuell
     * beschossenen Schiffes wird resettet
     *
     */
    protected void updateShipList(){

        //Das zuletzt zerstörte Schiff aus der Schiffsliste streichen
        this.shipList.put(hitShipLength, (shipList.get(hitShipLength) - 1) );

        //Schiffslänge wieder auf den Anfangsstatus setzen
        this.hitShipLength = 1;
    }













    /**
     * Mit der Berücksichtigung der Schiffsliste
     * werden leere Felder auf dem Spielfeld, auf dem sich
     * keine Schiffe mehr befinden können, automatisch
     * aussortiert und nicht mehr von der KI
     * beachtet.<br><br>
     *
     * Beispiel: Wenn alle 1er- und 2er-Schiffe
     * zersört wurden, werden alle eingeschlossenen
     * 2er- und 1er-Felder auf dem Spielfeld ausgeschlossen.
     */
    protected void excludeInvalidFields(){


        //Scannen des Spielfeldes und Ausschließen von leeren 3er-Feldern wenn alle 3er-, 2er- und 1er-Schiffe zerstört wurden
        if ( (shipList.get(3) == 0) && (shipList.get(2) == 0)  && (shipList.get(1) == 0) ){

            scanTrappedFields(3);

        }



        //Scannen des Spielfeldes und Ausschließen von leeren 2er-Feldern wenn alle 2er- und 1er-Schiffe zerstört wurden
        if ( (shipList.get(2) == 0)  && (shipList.get(1) == 0) ){

            scanTrappedFields(2);

        }

        //Scannen des Spielfeldes und Ausschließen von leeren 1er-Feldern wenn alle 1er-Schiffe zerstört wurden
        if ( (shipList.get(1) == 0) ){

            scanTrappedFields(1);

        }

    }


    /**
     * Überprüft ob irgendwo auf dem Spielfeld zusammengehörige
     * Felder einer bestimmten Größe eingeschlossen sind und
     * schließt diese entsprechend aus der Berechnung der KI
     * aus
     *
     * @param trappedFieldSize Die Größe der eingeschlossenen Felder welche auf dem Spielfeld überprüft werden sollen
     */
    private void scanTrappedFields(int trappedFieldSize){


        for( int r = 0; r < includedRows.size(); r++ ){ //Äußere Schleife für die Überprüfung der Reihen

            for (int c = 0; c < includedColumns.size(); c++){   //Innere Schleife für die Spalten

                int i;

                if (trappedFieldsAreValid(includedRows.get(r), includedColumns.get(c), trappedFieldSize, 0) ){

                    for (i = 0; i < trappedFieldSize; i++){

                        setCoordinateOccupied(includedRows.get(r), includedColumns.get(c) + i);


                    }

                }

               if (trappedFieldsAreValid(includedRows.get(r), includedColumns.get(c), trappedFieldSize, 1) ){

                   for (i = 0; i < trappedFieldSize; i++){

                       setCoordinateOccupied(includedRows.get(r) + i, includedColumns.get(c) );


                   }

               }

            }

        }

    }




    /**
     * Überprüft von einer bestimmten Koordinate aus in einer Richtung,
     * ob die eingeschlossenen Felder zum Ausschließen geeignet sind
     * oder nicht
     *
     * @param yCoord Zu überprüfende Y-Koordinate
     * @param xCoord Zu überprüfende X-Koordinate
     * @param direction Die Richtung in welche die Felder geprüft werden.
     *                  Folgende Werte sind erlaubt:<br>
     *                  0: Horizontale Richtung<br>
     *                  1: Vertikale Richtung <br>
     *
     * @param trappedFieldSize Die Größe der eingeschlossenen Felder welche auf dem Spielfeld überprüft werden sollen
     *
     * @return  Ob die eingeschlossenen Felder zum Auschließen geeigntet sind oder nicht
     */
    private boolean trappedFieldsAreValid (int yCoord, int xCoord, int trappedFieldSize, int direction){

        boolean isValid = true;


        //Schritt 1: Links neben bzw. ein Feld über der aktuellen Koordinate überprüfen, ob das Feld dort
        //eingeschlossen ist

        if (direction == 0){ //Horizontale Richtung

            //Zuerst überprüfen ob die Koordinate im Spielfeld ist oder aus dem Spielfeld herausragt
            if (isCoordinateInField(yCoord, xCoord -1)){


                //Wenn Sie im Spielfeld ist und keine besetzte Koordinate ist, dann sind die
                //eingeschlossenen Felder nicht valide
                if( !isCoordinateOccupied(yCoord, xCoord -1) ){


                    isValid = false;

                }
            }

        } else {    //Vertikale Richtung


            //Zuerst überprüfen ob die Koordinate im Spielfeld ist oder aus dem Spielfeld herausragt
            if (isCoordinateInField(yCoord -1, xCoord )){


                //Wenn Sie im Spielfeld ist und keine besetzte Koordinate ist, dann sind die
                //eingeschlossenen Felder nicht valide
                if(!isCoordinateOccupied(yCoord -1, xCoord) ){

                    isValid = false;

                }
            }

        }


        //Schritt 2: Die Koordinaten die Frei sind und dessen oberen/unteren bzw. linken/rechten Nachbarkoordinaten
        //Werden überprüft

        for( int i = 0; i < trappedFieldSize; i++){


            if (direction == 0){ //Horizontale Richtung

                //Obere und untere Nachbarkoordinate des eingeschlossenen Feldes überprüfen

                if (isCoordinateInField(yCoord -1, xCoord + i) ){


                    if(!isCoordinateOccupied(yCoord -1, xCoord + i) ){ //Obere Nachbarkoordinate

                        isValid = false;

                    }

                }


                if (isCoordinateInField(yCoord +1, xCoord + i) ){


                    if(!isCoordinateOccupied(yCoord +1, xCoord + i) ){ //Untere Nachbarkoordinate

                        isValid = false;

                    }

                }

                /** Jetzt wird die eigentliche eingeschlossene Koordinate geprüft. Diese muss frei sein. Wenn i = 0 ist,
                 * wird die übergebene Koordinate zuerst geprüft. i wird dann incrementiert und die zu überprüfenden, eingeschlossenen
                 * Felder wandern nach rechts
                 * */


                if (isCoordinateInField(yCoord, xCoord + i) ){


                    if (isCoordinateOccupied(yCoord, xCoord + i) || isCoordinateShipPart(yCoord, xCoord + i)){

                        isValid = false;


                    }

                } else {

                    //Wenn die eigentliche eingeschlossene Koordinate nicht im Feld ist, wird das ganze n-große Feld
                    // nicht eingeschlossen
                    isValid = false;
                }



            }else { //Vertikale Richtung


                //Linke und rechte Nachbarkoordinate des eingeschlossenen Feldes überprüfen


                if (isCoordinateInField(yCoord + i, xCoord - 1) ){

                    if(!isCoordinateOccupied(yCoord + i, xCoord - 1) ){ //Linke Nachbarkoordinate

                        isValid = false;


                    }

                }


                if (isCoordinateInField(yCoord + i, xCoord + 1) ){

                    if(!isCoordinateOccupied(yCoord + i, xCoord + 1) ){ //Rechte Nachbarkoordinate

                        isValid = false;


                    }
                }


                /** Jetzt wird die eigentliche eingeschlossene Koordinate geprüft. Diese muss frei sein. Wenn i = 0 ist,
                 * wird die übergebene Koordinate zuerst geprüft. i wird dann incrementiert und die zu überprüfenden, eingeschlossenen
                 * Felder wandern nach unten
                 * */


                if (isCoordinateInField(yCoord + i, xCoord) ){

                    if (isCoordinateOccupied(yCoord + i, xCoord)|| isCoordinateShipPart(yCoord + i, xCoord) ){

                        isValid = false;

                    }

                } else {

                    //Wenn die eigentliche eingeschlossene Koordinate nicht im Feld ist, wird das ganze n-große Feld
                    // nicht eingeschlossen
                    isValid = false;
                }

            }

        }



        //Schritt 3: Letzter Schritt. Wie am Anfang auch, wird am Ende die rechte bzw. unterste Koordinate neben/unter der
        //letzten eingeschlossenen Koordinate geprüft, ob diese besetzt ist

        if (direction == 0){ //Ganz rechts untersuchen


            //Zuerst überprüfen ob die Koordinate im Spielfeld ist oder aus dem Spielfeld herausragt
            if (isCoordinateInField(yCoord, xCoord + trappedFieldSize)){

                //Besetzte Koordinate ganz Rechts untersuchen
                if(!isCoordinateOccupied(yCoord, xCoord + trappedFieldSize) ){

                    isValid = false;

                }
            }


        }else { //Ganz unten untersuchen


            //Zuerst überprüfen ob die Koordinate im Spielfeld ist oder aus dem Spielfeld herausragt
            if (isCoordinateInField(yCoord + trappedFieldSize, xCoord )){

                //Besetzte Koordinate ganz unten untersuchen
                if(!isCoordinateOccupied(yCoord + trappedFieldSize, xCoord) ){

                    isValid = false;

                }
            }

        }




        return isValid;
    }







    /**
     * Extrahiert aus einem String die Y-Koordinate
     *
     * @param stringCoord String von welchem extrahiert wird
     * @return Y-Koordinate
     */
    protected int extractYCoord( String stringCoord){
        return Integer.parseInt(stringCoord.split(",")[0]);
    }


    /**
     * Extrahiert aus einem String die X-Koordinate
     *
     * @param stringCoord String von welchem extrahiert wird
     * @return X-Koordinate
     */
    protected int extractXCoord( String stringCoord){
        return Integer.parseInt(stringCoord.split(",")[1]);
    }








    /**
     * Überschreiben der Methode aus Klasse Player<br><br>
     *
     * Zurückgenommene Koordinaten werden im mirrorField wieder
     * als Frei angezeigt
     *
     * @param coordinateString Liste der zurückgenommen Koordinaten. Spieler muss für seinen Algorithmus passend die
     *                        Koordinaten bearbeiten.
     *
     * @throws NoDrawException Wenn der String mit den Koordinaten falsch abgetrennt oder leer ist
     */
    public void undoHits(String coordinateString) throws NoDrawException {

        super.undoHits(coordinateString);

        //Y- und X-Koordinaten aus den Strings zwischenspeichern
        int yCoord;
        int xCoord;
        //Züge zurücknehmen
        for ( String draw : undoRedoCoordinates){

            yCoord = extractYCoord(draw);
            xCoord = extractXCoord(draw);

            mirrorField[yCoord][xCoord] = 0;

        }

        resetUndoRedoCoordinates();

    }


    /**
     * Überschreiben der Methode aus Klasse Player<br><br>
     *
     * Wiederhergestellte Koordinaten werden im mirrorField wieder
     * als besetzt angezeigt.
     *
     * @param coordinateString Liste der wiederhergsetellten Koordinaten. Spieler muss für seinen Algorithmus passend die
     *                        Koordinaten neu setzen.
     * @throws NoDrawException Wenn der String mit den Koordinaten falsch abgetrennt oder leer ist
     */
    public void redoHits(String coordinateString) throws NoDrawException {

        super.redoHits(coordinateString);

        //Y- und X-Koordinaten aus den Strings zwischenspeichern
        int yCoord;
        int xCoord;
        //Züge wiederherstellen
        for ( String draw : undoRedoCoordinates){

            yCoord = extractYCoord(draw);
            xCoord = extractXCoord(draw);

            setCoordinateOccupied(yCoord, xCoord);

        }

        resetUndoRedoCoordinates ();

    }





    /**
     * Aus einem vorherigen Spiel wird das <b>mirrorField</b>
     * geladen und weiterverwendet
     *
     * @param prevField Das geladene Mirrorfield
     */
    public void loadPreviousGame (String prevField){

        //Position der aktuellen Stelle im String
        int stringPointer = 0;
        for ( int row = 0; row < this.fieldSize; row ++){ //Äußere Schleife für Reihen
            for (int col = 0; col < this.fieldSize; col++){ //Innere Schleife für Spalten

                this.mirrorField[row][col] = (byte) prevField.charAt(stringPointer);
                stringPointer++;

            }

        }

    }


    /**
     * Speichert das aktuelle Spielfeld der KI und gibt diese
     * für die Speicherung zurück
     *
     * @return Das aktuelle Spielfeld der KI
     */
    public String saveCurrentGame (){

        String savedField = "";

        for ( int row = 0; row < this.fieldSize; row ++){ //Äußere Schleife für Reihen
            for (int col = 0; col < this.fieldSize; col++){ //Innere Schleife für Spalten

                savedField += "" + this.mirrorField[row][col];

            }

        }

        return savedField;
    }







}//end class Computer



