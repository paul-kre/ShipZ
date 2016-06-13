package shipz.gamemode;

import java.util.ArrayList;
import java.util.Random;
import shipz.Player;


/**
 * Hauptklasse für die Künstliche Intelligenz<br><br>
 *
 * Die Klasse vererbt alle notwendigen Instanzvariablen und -methoden <br>
 * für die Schwierigkeitsgrade Easy, Normal und Hard <br>
 *
 * @author Artur Hergert
 *
 */
public abstract class Computer extends Player {


    //IV


    /** Die Feldgröße des aktuellen Spiels. Standardmäßig ist es 10 x 10  */
    private int fieldSize = 10;

    /**
     * Zweidimensionales Byte-Array, welches als Kopie des zu beschießenen Feldes dient<br>
     * Zur Speicherung der Felder die man schon getroffen hat und die man nicht mehr
     * beschießen darf.<br>
     *
     * Die Indices können folgende Zustände aufweisen:<br>
     * 0: Noch nicht beschossen<br>
     * 1: Nicht mehr beschießbar (Wasser getroffen bzw. Umgebung eines Schiffes)<br>
     * 2: Schiffsteil getroffen<br>
     *  */
    private byte[][] mirrorField;

    /**
     * Speichert zur Identifikation die Koordinaten des Schiffsteil, welches zuerst bei einem Schiff getroffenen wurde,
     * um von dort aus alle Richtungen zu überrpüfen <br>
     *
     * Werte werden beim instanziieren standardweise auf -1|-1 gesetzt.<br>
     *
     * Dieser Zustand bedeutet, dass es in dem Moment keine gültige,
     * zuletzt getroffene Koordinate existiert. Dies hat zur Folge, dass eine neue
     * Koordinate generiert wird.
     * */
    private int[] firstShipTileHit = new int[] {-1, -1};

    /**
     * Die aktuell getroffenen X und Y-Koordinaten eines Schiffes der KI werden zur
     * Weiterverwendung gespeichert.<br>
     *
     */
    private int[] currentShipTile = new int[]{0, 0};


    /** Die aktuelle Richtung in der die KI ein getroffenes Schiffsteil auf weitere
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



    /** Random-Object zur Generierung von zufälligen Integer Zahlen */
    private Random random = new Random();


    /** Liste in der Zahlen von 0 bis <b>fieldSize</b> gespeichert werden, die von der KI
     * noch zufällig generiert werden können.<br>
     * Sobald eine Reihe im Spielfeld voll ist, muss keine Koordinate mehr für diese erstellt werden und
     * die Reihe wird aus der ArrayListe gelöscht.<br>
     * Sorgt für mehr Effizienz bei der Generierung der Koordinaten,
     * da die maximale Durchlaufzahl auf die Anzahl der Reihen, die
     * freie Koordinaren haben, mal der Größe von <b>fieldSize</b>
     * beschränkt wird.
     */
    private ArrayList<Integer> includedRows =  new ArrayList<>();


    /** Liste in der Zahlen von 0 bis <b>fieldSize</b> gespeichert werden, die von der KI
     * noch zufällig generiert werden können.<br>
     * Sobald eine Spalte im Spielfeld voll ist, muss keine Koordinate mehr für diese erstellt werden und
     * die Spalte wird aus der ArrayListe gelöscht.<br>
     * Sorgt für mehr Effizienz bei der Generierung der Koordinaten,
     * da die maximale Durchlaufzahl auf die Anzahl der Spalten, die
     * freie Koordinaren haben, mal der Größe von <b>fieldSize</b>
     * beschränkt wird.
     */
    private ArrayList<Integer> includedColumns =  new ArrayList<>();


    /**
     * Sobald das Schachbrettmuster aktiviert wird, wird das Spielfeld
     * nach freien Koordinaten abgesucht, die dem Muster entsprechen und
     * diese werden in der Liste gespeichert, damit die KI diese entsprechenden
     * Koordinaten beschießen kann.
     */
    private ArrayList<String> validChessBoardPatternCoordinates = new ArrayList<>();


    //Contructor

    /**
     * Constructor der KI-Superklassen Computer.<br>
     * Computer kann nicht instanziiert werden; der Constructor
     * wird an die Subklassen vererbt.
     *
     * @param newFieldSize Die Feldgröße des aktuellen Spiels. Die zu erstellenden Zufallskoordinaten werden von 1 bis fieldSize generiert.
     */
    public Computer(int newFieldSize){

        fieldSize = newFieldSize;
        initiateMirrorField();

        //ArrayList mit den gültigen Reihen die generiert initialisieren
        initializeList(includedRows);
        initializeList(includedColumns);


    }





    //IM


    /**
     * Implementierung der shootField-Methode von
     * Player ohne Inhalt.
     */
    public void shootField(int x, int y, byte result) { }


    /**
     * Abstrakte Methode die in den Subklassen implementiert wird
     *
     * KI generiert mithilfe dessen Algorithmus die nächste
     * Koordinate die beschossen werden soll und speichert diese
     * in die Instanzvariablen der Superklasse Player.
     */
    protected abstract void generateAICoordinates();




    /**
     * Überprüfen von Koordinaten ob diese von der KI
     * beschossen und eventuell ein Schiff getroffen/zerstört wurden<br><br>
     *
     * Methode sollte direkt nach dem Aufruf des FireGameEvents in der Main Methode
     * ausgeführt werden, damit eine Generierung von redundanten
     * Koordinaten vermiedern werden kann.
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

        generateAICoordinates();
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
    private boolean isCoordinateInField(int yCoord, int xCoord){

        return ( (yCoord >= 0 && yCoord < this.fieldSize) && (xCoord >= 0 && xCoord < this.fieldSize) );
    }



    /**
     * Untersucht alle vier Richtungen um einen versenkten Treffer
     * welcher ein Schiff zerstört hat und speichert die Umgebung ab
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
     * Umgebung ins Spiegelfeld ab
     *
     * Geeignet nur für Nördliche und Südliche Richtung
     *
     * @param currentY Aktuelle vertikale Y-Koordinate
     * @param currentX Aktuelle vertikale X-Koordinate
     */
    private void saveShipVerticalSideEdge(int currentY, int currentX){

        //Süd-Westlich von current speichern
        if ( isCoordinateInField(currentY+1, currentX-1) ){
            setCoordinateOccupied(currentY+1, currentX-1);
        }

        //Westlich von current speichern
        if ( isCoordinateInField(currentY, currentX-1) ){
            setCoordinateOccupied(currentY, currentX-1);
        }

        //Nord-Westlich von current speichern
        if ( isCoordinateInField(currentY-1, currentX-1) ){
            setCoordinateOccupied(currentY-1, currentX-1);
        }

        //Süd-Östlich von current speichern
        if ( isCoordinateInField(currentY+1, currentX+1) ){
            setCoordinateOccupied(currentY+1, currentX+1);
        }

        //Östlich von current speichern
        if ( isCoordinateInField(currentY, currentX+1) ){
            setCoordinateOccupied(currentY, currentX+1);
        }

        //Nord-Östlich von current speichern
        if ( isCoordinateInField(currentY-1, currentX+1) ){
            setCoordinateOccupied(currentY-1, currentX+1);
        }


        /*Nachdem das vertikal platzierte Schiff mit samt Umgebung in mirrorField gespeichert wurde,
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
     */
    private void saveShipHorizontalSideEdge(int currentY, int currentX){

        //Süd-Westlich von current speichern
        if ( isCoordinateInField(currentY+1, currentX-1) ){
            setCoordinateOccupied(currentY+1, currentX-1);
        }

        //Südlich von current speichern
        if ( isCoordinateInField(currentY+1, currentX) ){
            setCoordinateOccupied(currentY+1, currentX);
        }

        //Süd-Östlich von current speichern
        if ( isCoordinateInField(currentY+1, currentX+1) ){
            setCoordinateOccupied(currentY+1, currentX+1);
        }

        //Nord-Westlich von current speichern
        if ( isCoordinateInField(currentY-1, currentX+1) ){
            setCoordinateOccupied(currentY-1, currentX+1);
        }

        //Nördlich von current speichern
        if ( isCoordinateInField(currentY-1, currentX) ){
            setCoordinateOccupied(currentY-1, currentX);
        }

        //Nord-Östlich von current speichern
        if ( isCoordinateInField(currentY-1, currentX+1) ){
            setCoordinateOccupied(currentY-1, currentX+1);
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
     * Umgebung ins Spiegelfeld ab
     *
     * Geeignet nur für Nördliche und Südliche Richtung
     *
     * @param currentY Aktuelle horizontale Y-Koordinate
     * @param currentX Aktuelle horizontale X-Koordinate
     */
    private void saveShipHorizontalTopEdge (int currentY, int currentX){

        //current in das Spiegelfeld speichern
        setCoordinateOccupied(currentY, currentX);

        //Linker und Rechter Nachbar von current speichern
        if ( isCoordinateInField(currentY, currentX-1) ){ //Links von current
            setCoordinateOccupied(currentY, currentX-1);
        }

        if ( isCoordinateInField(currentY, currentX+1) ){ //Rechts von current
            setCoordinateOccupied(currentY, currentX+1);
        }

        /*Nachdem das horizontal platzierte Schiff mit samt Umgebung in mirrorField gespeichert wurde,
        wird geprüft, ob in dessen Reihen noch freie Plätze sind oder ob die Reihe ausgeschlossen
        werden kann bei der Generierung der Zufallszahlen */
        excludeFullRows(currentY);

        /*Nachdem das horizontal platzierte Schiff mit samt Umgebung in mirrorField gespeichert wurde,
        wird geprüft, ob in dessen Reihen noch freie Plätze sind oder ob die Spalte ausgeschlossen
        werden kann bei der Generierung der Zufallszahlen */
        excludeFullColumns(currentX);
        excludeFullColumns(currentX-1);
        excludeFullColumns(currentX+1);

    }



    /**
     * Speichert die vertikale Oberkante eines Schiffsteils als dessen
     * Umgebung ins Spiegelfeld ab
     *
     * Geeignet nur für Nördliche und Südliche Richtung
     *
     * @param currentY Aktuelle horizontale Y-Koordinate
     * @param currentX Aktuelle horizontale X-Koordinate
     */
    private void saveShipVerticalTopEdge (int currentY, int currentX){

        //current in das Spiegelfeld speichern
        setCoordinateOccupied(currentY, currentX);

        //Oberer und Unterer Nachbar von current speichern
        if ( isCoordinateInField(currentY-1, currentX) ){ //Links von current
            setCoordinateOccupied(currentY-1, currentX);
        }

        if ( isCoordinateInField(currentY+1, currentX) ){ //Rechts von current
            setCoordinateOccupied(currentY+1, currentX);
        }

        /*Nachdem das vertikal platzierte Schiff mit samt Umgebung in mirrorField gespeichert wurde,
        wird geprüft, ob in dessen Reihen noch freie Plätze sind oder ob die Reihe ausgeschlossen
        werden kann bei der Generierung der Zufallszahlen */
        excludeFullRows(currentY);
        excludeFullRows(currentY+1);
        excludeFullRows(currentY-1);
    }


    /**
     * Prüft ob die übergebene X- und
     * Y-Richtung in die nördliche oder
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
     * Durchläuft eine Richtung um eine getroffene
     * Koordinate die ein Schiff zum Versenken gebracht hat
     * und speichert alle Umgebungskoordinaten des Schiffes
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

        //Flag-Variable zum Durchlaufen der While-Schleifen. Prüfen immer ob die Richtung weitergeprüft werden soll.
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
                            saveShipVerticalSideEdge(currentY,currentX);

                        } else { // Bei West- oder Ostrichtung

                            //Horizontal Seiten abspeichern
                            saveShipHorizontalSideEdge(currentY,currentX);
                        }


                        //Nördliche Richtung wurde noch nicht zu Ende durchsucht, deshalb läuft die Schleife weiter
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
     * Füllt eine Liste mit den Werten von 0 bis <b>fieldSize</b> für die Generierung der
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
     * Gibt eine zufällige integerzahl für die Verwendung als Y-Koordinate aus der ArrayListe <b>includedRows</b> zurück.<br>
     * Die zufälligen Zahlen können innerhalb 0 bis <b>fieldSize</b> liegen.<br>
     * Ist die Reihe einer Y-Koordinate voll, wurde die Reihe ausgeschlossen und die entsprechende Zahl ist in der
     * ArrayListe nicht mehr verfügbar für den Rest des Spiels.
     *
     * @return zufällige Zahl zwischen 0 und <b>fieldSize</b>, dessen Reihen noch nicht voll sind
     */
    protected int randomRowInt() {

        //@ToDo randomRowInt ThrowException machen

        if (includedRows.size() > 0){
            return this.includedRows.get(random.nextInt(includedRows.size()));
        }

        return random.nextInt(fieldSize);
    }

    /**
     * Gibt eine zufällige integerzahl für die Verwendung als X-Koordinate aus der ArrayListe <b>includedColumns</b> zurück.<br>
     * Die zufälligen Zahlen können innerhalb 0 bis <b>fieldSize</b> liegen.<br>
     * Ist die Reihe einer X-Koordinate voll, wurde die Reihe ausgeschlossen und die entsprechende Zahl ist in der
     * ArrayListe nicht mehr verfügbar für den Rest des Spiels.
     *
     * @return zufällige Zahl zwischen 0 und <b>fieldSize</b>, dessen Spalten noch nicht voll sind
     */
    protected int randomColumnInt() {

        //@ToDo randomColumnInt ThrowException machen
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

            //Zu allererst wird geprüft ob die angegebene Reihe sich innerhalb des Spielfeldes befinden kann ( zwischen 0 und fieldSize)
            if ( row >= 0 && row < this.fieldSize){

                //Variablen die beim Schleifendurchlauf die Koordinaten der entsprechenden Spielfeldhälften prüfen
                int fieldCenter = this.fieldSize / 2;
                int leftSide = fieldCenter -1;
                int rightSide = fieldCenter +1;

                //Flag zum Abbrechen der Schleife falls ein leeres Feld gefunden wurde, da man in dem Fall weiss, dass die Reihe nicht voll ist
                boolean emptySlotFound = false;

                //Überprüfung ob überhaupt die Mitte des Feldes frei ist. Wenn sie es ist, kann die Reihe schonmal nicht ausgeschlossen werden
                if ( !isCoordinateOccupied (row, fieldCenter) && !isCoordinateShipPart (row, fieldCenter ) ){


                    // Die Mitte des Spielfeldes ist frei, deshalb kann die Reihe noch nicht gelöscht werden. Die Methode endet hier.

                } else{


                    do{

                        //Linke Spielfeldhälfte überprüfen

                        if (isCoordinateInField(row, leftSide )){

                            if ( !isCoordinateOccupied (row, leftSide) && !isCoordinateShipPart (row, leftSide ) ){

                                emptySlotFound = true;
                            }
                        }

                        //Rechte Spielfeldhälfte überprüfen
                        if (isCoordinateInField(row, rightSide )){

                            if ( !isCoordinateOccupied (row, rightSide) && !isCoordinateShipPart (row, rightSide ) ){

                                emptySlotFound = true;
                            }
                        }


                        //Zählervariablen entsprechend in/decrementieren um von der Mitte aus pro Durchlauf in die linke und rechte
                        //Spielfeldhälfte zu prüfen
                        leftSide--;
                        rightSide++;

                    }while ( (leftSide >= 0  || rightSide < this.fieldSize) && !emptySlotFound); //end while


                    //Nachdem die Schleife durchgelaufen ist und kein freien Platz gefunden hat weiss man, dass die Reihe voll ist.
                    //Die Reihe wird in de Fall ausgeschlossen
                    if (!emptySlotFound){

                        deleteRowFromList(row);
                    }

                }


            }

        }




    /**
     * Prüft eine Spalte, nachdem etwas in dieser gespeichert wurde, und schließt
     * die Generierung von Zufallskoordinaten für diese Spalte in den nächsten Zügen aus,
     * falls die Spalte voll ist.
     *
     * @param column Die Reihe die geprüft und eventuell ausgeschlossen wird. Ist im normalfall eine übergebene Y-Koordinate.
     */
    private void excludeFullColumns( int column){

        //Zu allererst wird geprüft ob die angegebene Reihe sich innerhalb des Spielfeldes befinden kann ( zwischen 0 und fieldSize)
        if ( column >= 0 && column < this.fieldSize){

            //Variablen die beim Schleifendurchlauf die Koordinaten der entsprechenden Spielfeldhälften prüfen
            int fieldCenter = this.fieldSize / 2;
            int upperSide = fieldCenter -1;
            int lowerSide = fieldCenter +1;

            //Flag zum Abbrechen der Schleife falls ein leeres Feld gefunden wurde, da man in dem Fall weiss, dass die Reihe nicht voll ist
            boolean emptySlotFound = false;

            //Überprüfung ob überhaupt die Mitte des Feldes frei ist. Wenn sie es ist, kann die Reihe schonmal nicht ausgeschlossen werden

            if ( !isCoordinateOccupied ( fieldCenter, column ) && !isCoordinateShipPart (fieldCenter, column ) ){


                // Die Mitte des Spielfeldes ist frei, deshalb kann die Spalte noch nicht gelöscht werden. Die Methode endet hier.

            } else{


                do{

                    //Linke Spielfeldhälfte überprüfen

                    if (isCoordinateInField( upperSide, column)){

                        if ( !isCoordinateOccupied (upperSide, column) && !isCoordinateShipPart (upperSide, column ) ){

                            emptySlotFound = true;
                        }
                    }

                    //Rechte Spielfeldhälfte überprüfen
                    if (isCoordinateInField(lowerSide, column )){

                        if ( !isCoordinateOccupied (lowerSide, column) && !isCoordinateShipPart (lowerSide, column ) ){

                            emptySlotFound = true;
                        }
                    }


                    //Zählervariablen entsprechend in/decrementieren um von der Mitte aus pro Durchlauf in die obere und untere
                    //Spielfeldhälfte zu prüfen
                    upperSide--;
                    lowerSide++;

                }while ( (upperSide >= 0  || lowerSide < this.fieldSize) && !emptySlotFound); //end while


                //Nachdem die Schleife durchgelaufen ist und kein freien Platz gefunden hat weiss man, dass die Reihe voll ist.
                //Die Reihe wird in de Fall ausgeschlossen
                if (!emptySlotFound){

                    deleteColumnFromList(column);
                }

            }


        }

    }




    /**
     * Löscht eine bestimmte Reihe aus dem ArrayList<b>includedRows</b>, damit es nicht mehr bei der
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
     * Löscht eine bestimmte Spalte aus dem ArrayList<b>includedColumns</b>, damit es nicht mehr bei der
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
    }











    /**
     * Erstellt nach einem Muster, bei dem nur alle 3 felder und dessen nach unten-links
     * in diagonal gerichteten Koordinaten geprüft werden, die X-Koordinaten.
     *
     * @param yCoord Die zuvor erstellte Y-Koordinate, die für die Berechnung der X-Koordinate wihctig ist
     * @return Eine X-Koordinate die dem 3-Feld-Muster entspricht
     */
    protected int randomThreePointPatternInt( int yCoord ){


        /** Die EbenenStufe wird als 'level' gespeichert, damit berechnet werden kann, wie der
         * Abstand zwischen den X-Koordinaten immer sein müssen
         */

        int level = yCoord + 1;

        //Ebene wird überprüft ob es die Stufe 1,2 oder 3 hat und je nachdem wird weiterberechnet

        if (level  % 3 == 1 ){ //Prüfen ob X-Koordinate für eine Ebene der Stufe 1 berechnet werden soll

            return ( 2 + ( 3 * validThreePointPatternColumnInt(1) ));

        } else if ( level % 3 == 2){ //Prüfen ob X-Koordinate für eine Ebene der Stufe 2 berechnet werden soll

            return ( 1 + ( 3 * validThreePointPatternColumnInt(2) ));


        } else if ( level % 3 == 0){ //Prüfen ob X-Koordinate für eine Ebene der Stufe 3 berechnet werden soll

            return ( 3 * validThreePointPatternColumnInt(3) );

        }

        return -1;
    }


    /**
     * Berechnet eine gültige X-Koordinate aus den möglichen Feldern einer Ebene
     * und gibt diese für weitere Berechnungen zurück
     *
     * @param currentLevel DIe Ebene für die die X-Koordinate berechnet wird
     * @return Eine X-Koordinate die dem 3-Feld-Muster der entsprechenden Ebene entspricht
     */
    private int validThreePointPatternColumnInt(int currentLevel){


        //Flag zum Überrpüfen ob die erstellte X-Koordinate gültig ist (ob sie zwischen 0 und fieldSize ist UND dem Muster entpricht der aktuellen Ebene)
        boolean validXCoord = true;

        //Die X-Koordinate die zurückgegeben wird
        int patternXCoord;


        do{


            //Der X-Koordinate wird eine zufällige Zahl aus den gespeicherten, noch nicht leeren Spaltenkoordinaten zugewiesen
            patternXCoord = randomColumnInt() ;


            if (currentLevel == 1){


                if ( patternXCoordIsInRange(patternXCoord / 3, 1) && ( (patternXCoord - 1) % 3 == 1 )  ){

                    validXCoord = false;
                }


            } else if ( currentLevel == 2){


                if ( patternXCoordIsInRange(patternXCoord / 3, 2) && ( (patternXCoord + 1) % 3 == 2 )  ){

                    validXCoord = false;
                }

            } else  if (currentLevel == 3){

                if ( patternXCoordIsInRange(patternXCoord / 3, 3) && ( patternXCoord % 3 == 0 ) ){

                    validXCoord = false;
                }


            }


            //System.out.println("3-Feld-Looped");
        }while ( validXCoord);



        return (patternXCoord / 3);

    }


    /**
     * Prüft ob eine X-Koordinate, die nach dem 3-feld-Muster erstelt wurde, auch
     * in der entprechenden Ebene im gültigen Bereich liegt
     *
     * @param patternXCoord Die zu überprüfende X-Koordinate
     * @param currentLevel Die Ebene in der die X-Koordinate erstellt wird
     * @return Ob die X-Koordinate innerhalb des gültigen Bereichs der Eben bzw. des Musters liegt
     */
    private boolean patternXCoordIsInRange (int patternXCoord, int currentLevel){


        if (currentLevel == 1 || currentLevel == 2){

            return patternXCoord <= ( (this.fieldSize / 3) - 1);

        }else {

            return patternXCoord <= (this.fieldSize / 3) ;

        }

    }


    /**
     * Prüft ob das Drei-Feld-Muster noch laufen darf.
     * Gibt FALSE zurück, falls alle Musterkoordinaten
     * des Musters besetzt wurden und keine Koordinaten mehr
     * für das Muster erstellt werden können
     *
     * @return Ob das Muster noch aktiv sein darf oder nicht
     */
    protected boolean threePointPatternIsStillValid(){


        /**
         * Äußere For-Schleife repräsentiert die noch gültigen, verwendbaren Y-Koordinaten
         *
         * Im Inneren der Schleife wird anhand der Reihenzahl der Y-Koordinate berechnet, um welche
         * Ebene es sich bei dem 3-Feld-Muster handelt. Dies ist wichtig, da 2 von 3 Ebenen weniger durchlaufen
         * müssen die dritte.
         * Mit der Formel: ( Ebenenzahl + ( 3 * MultiplikatorDerInnerenSchleife) lassen sich in der inneren Schleife
         * alle entsprechenden Koordinaten des Musters überprüfen.
         * (Beispiel: Ebene1 hat für dessen Muster die X-Koordinate 2, 4 und 8)
         * Die jeweiligen Koordinaten werden überprüft ob diese noch frei sind. Wenn dem so ist,
         * beendet dieMEthode in dem sie TRUE zurückgibt und die KI weiss, dass
         * sie noch Koordinaten mit diesem Muster erstellen kann.
         * Andernfalls weiss die KI, dass alle Koordinaten für dieses Muster besetzt sind und kann
         * dementsprechend auf ein anderes Muster umsteigen.
         *
         */
        for ( int i = 0; i < includedRows.size(); i++){


            //Prüft ob die Y-Koordinate der Ebene 1 entspricht
            if ( (includedRows.get(i) + 1) % 3 == 1){

                //Innere Schleife für die Überprüfung der X-Koordinaten
                /**
                 * Das "((this.fieldSize/ 3) -1)" steht für den  höchsten Multiplikator, mit dem die Formel
                 * die X-Koordinaten der entsprechenden Ebene erstellt. Bei einem Standard 10x10 Feld
                 * hat die Ebene 1 und 2 einen max. Multiplikator von 2 und Ebene 3 einen von 3.
                 */
                for( int j = 0; j <= ((this.fieldSize/ 3) -1) ; j++){



                    if ( !isCoordinateOccupied(i, (2 + (3 * j)) ) && !isCoordinateShipPart(i, (2 + (3 * j)) )){

                        System.out.println("Ebene: " + ((includedRows.get(i) + 1) % 3 ) + "    Koordinate: " + i + "|" + (2 + (3 * j)) );
                        return true;
                    }

                }

                //Prüft ob die Y-Koordinate der Ebene 2 entspricht
            } else if ( (includedRows.get(i) + 1) % 3 == 2 ){

                for( int k = 0; k <= ((this.fieldSize/ 3) -1); k++){


                    if ( !isCoordinateOccupied(i, (1 + (3 * k)) ) && !isCoordinateShipPart(i, (1 + (3 * k)) )){

                        System.out.println("Ebene: " + ((includedRows.get(i) + 1) % 3 ) + "    Koordinate: " + i + "|" + (1 + (3 * k))  );
                        return true;
                    }

                }

                //Prüft ob die Y-Koordinate der Ebene 3 entspricht
            } else if ( (includedRows.get(i) + 1) % 3 == 0 ){

                for( int l= 0; l <= (this.fieldSize/ 3) ; l++){

                    if ( !isCoordinateOccupied(i, (3 * l))  && !isCoordinateShipPart(i, (3 * l) ) ){

                        System.out.println("Ebene: " + ((includedRows.get(i) + 1) % 3 ) + "    Koordinate: " + i + "|" + (3 * l) );
                        return true;
                    }

                }

            }


        }


        return false;
    }












    /**
     * Erstellt anhand der verbleibenden, freien Koordinaten auf dem Spielfeld
     * Blöcke und unterteilt diese mithilfe eines Schachbrettmusters.
     * Die Anzahl der Schüße um ein Schiffsteil damit zu treffen sinkt
     * und die Chance dabei etwas zu treffen erhöht sich.
     *
     * @return Eine beschießbare Koordinate aus einem Block
     */
    protected String chessBoardPatternString(){

        //Falls die Liste des Schachbrettmusters leer ist (beim ersten
        // Aufruf) wird das Feld gescannt und die Liste initialisiert
        if ( validChessBoardPatternCoordinates.isEmpty()){

            System.out.println("SCHACHBRETTMUSTER!");
            initiazileChessBoardPattern();
        }


        return validChessBoardPatternCoordinates.get(random.nextInt(validChessBoardPatternCoordinates.size()));


    }


    /**
     * Scannt das Spielfeld nach freien Feldern und
     * speichert mithilfe des Schachbrettmusters
     * die Koordinaten in das <b>validChessBoardPatternCoordinates</b>
     * ab
     */
    private void initiazileChessBoardPattern(){

        //Flag-Variable die angibt, ob bei der Prüfung der nördlichen Koordinate die aktuelle gespeichert wurde
        boolean wasSaved;


        for ( int i = 0; i < includedRows.size(); i++){

            for (int j = 0; j < includedColumns.size(); j++){

                wasSaved = false;


                //Aktuelle Koordinate prüfen ob sie frei ist, ansonsten wird zum nächsten weitergegangen
                if ( !isCoordinateOccupied( includedRows.get(i), includedColumns.get(j) ) && !isCoordinateShipPart( includedRows.get(i), includedColumns.get(j))){

                    //Nördliche Koordinate prüfen
                    if( isCoordinateInField( includedRows.get(i) -1, includedColumns.get(j) ) && !isCoordinateShipPart(includedRows.get(i) -1, includedColumns.get(j)) && !isCoordinateOccupied(includedRows.get(i) -1, includedColumns.get(j))){

                       // System.out.println("In Field? " +  (includedRows.get(i) -1) + "|" + includedColumns.get(j)  + " -> " + isCoordinateInField( includedRows.get(i) -1, includedColumns.get(j) ));
                        //Wenn die nördliche Koordinate nicht gespeichert wurde, wird die aktuelle Koordinate in die Liste
                        //gespeichert
                        if( !validChessBoardPatternCoordinates.contains("" + (includedRows.get(i) - 1) + "," + includedColumns.get(j)  )){

                            //System.out.println("Speichern --> " + "" + includedRows.get(i) + "," + includedColumns.get(j) );
                            validChessBoardPatternCoordinates.add("" + includedRows.get(i) + "," + includedColumns.get(j) );
                            wasSaved = true;
                        }

                    }

                    //Wenn die aktuelle Koordinate bei der Überrprüfung des Nordens nicht gespeichert wurde, wird die
                    //westliche Koordinate überprüft
                    if ( !wasSaved){

                        //Westliche Koordinate prüfen
                        if(isCoordinateInField( includedRows.get(i), includedColumns.get(j) - 1) && !isCoordinateShipPart(includedRows.get(i), includedColumns.get(j) - 1) && !isCoordinateOccupied(includedRows.get(i), includedColumns.get(j) - 1) ){

                            //System.out.println("In Field? " +  (includedRows.get(i)) + "|" + (includedColumns.get(j) - 1)  + " -> " + isCoordinateInField( includedRows.get(i), includedColumns.get(j) ));
                            //Wenn die westliche Koordinate nicht gespeichert wurde, wird die aktuelle Koordinate in die Liste
                            //gespeichert
                            if( !validChessBoardPatternCoordinates.contains("" + includedRows.get(i)  + "," + (includedColumns.get(j) -1) )){

                                //System.out.println("Speichern --> " + "" + includedRows.get(i) + "," + includedColumns.get(j) );
                                validChessBoardPatternCoordinates.add("" + includedRows.get(i) + "," + includedColumns.get(j) );

                            }

                        }

                    }

                }

            }

        }

    }


    /**
     * Prüft ob eine Koordinate in der Liste für das
     * Schachbrettmuster vorhanden ist und löscht diese dann
     *
     * @param coord Die zu löschende Koordinate
     */
    protected void deleteChessBoardPatternCoordinate( String coord){

        if (validChessBoardPatternCoordinates.contains(coord)){

            validChessBoardPatternCoordinates.remove(coord);
        }

    }
















    /**
     * DEBUGGING METHODE FÜR MIRRORFIELD
     *
     * Zeigt das SpiegelFeld als ein Spielfeld.
     * Nichtbeschossene Felder sind leer, beschossene Felder enthalten ein 'O'
     */
    public void displayMirrorField() {
        //Ausgabe der oberen Feldbeschriftung
        System.out.println("  0 1 2 3 4 5 6 7 8 9");
        //1. Zähler
        int y;
        //2. Zähler
        int x;
        //doppelte Schleife für Durchlauf durch alle Felder

        for(y=0; y  < mirrorField.length; y++) {
            //Ausgabe der seitlichen Feldbeschriftung
            System.out.print(y);
            for(x=0; x < mirrorField[y].length; x++) {

                //Ausgabe der einzelnen Zellen


                if (mirrorField[y][x] == 0){

                    System.out.print(" " + " ");

                } else if (mirrorField[y][x] == 1){

                    System.out.print(" " + "o");

                } else if( mirrorField[y][x] == 2){

                    System.out.print(" " + "x");
                }
            }

            //Zeilenumbruch
            System.out.println();
        }
    }



    public void printRowAndColumnItems(){

        System.out.print("\nIncluded Rows: ");
        for (int i = 0; i < includedRows.size(); i++){

            System.out.print(includedRows.get(i) +",");
        }
        System.out.println();


        System.out.print("Included Columns: ");
        for (int i = 0; i < includedColumns.size(); i++){

            System.out.print(includedColumns.get(i) +",");
        }
        System.out.println("\n\n");


        System.out.print("Schachbrettkoordinaten: ");
        for (int i = 0; i < validChessBoardPatternCoordinates.size(); i++){

            System.out.print(validChessBoardPatternCoordinates.get(i) +" | ");
        }
        System.out.println("\n\n");

    }


    /**
     *
     * Extrahiert aus einem String die Y-Koordinate
     * @param stringCoord String von welchem extrahiert wird
     * @return Y-Koordinate
     */
    protected int extcractYCoord( String stringCoord){
        return Integer.parseInt(stringCoord.split(",")[0]);
    }


    /**
     *
     * Extrahiert aus einem String die X-Koordinate
     * @param stringCoord String von welchem extrahiert wird
     * @return X-Koordinate
     */
    protected int extcractXCoord( String stringCoord){
        return Integer.parseInt(stringCoord.split(",")[1]);
    }


}//end class Computer



