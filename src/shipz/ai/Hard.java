package shipz.ai;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Schwerer Schwierigkeitsgrad<br><br>
 *
 * Erstellte Koordinaten werden an die Verwaltung übergeben.
 * KI berücksichtigt zuletzt getroffene Felder und prüft
 * dementsprechend das herumliegende Gebiet.<br><br>
 *
 * Zusätzlich werden Koordinaten die alleine stehen bzw. keine Nachbarkoordinaten
 * haben nicht berücksichtigt. <br><br>
 *
 * Für die Generierung der Koordinaten scannt das Feld ab und wählt dementsprechend
 * eine Taktik.<br>
 *
 * @author Artur Hergert
 *
 */
public class Hard extends Computer {



    //IV

    /**
     * Speichert für jede Y- und X-Koordinate dessen
     * Trefferwahrscheinlichket für ein Schiffsteil ein.
     * Je höher der gespeicherte Wert, desto wahrscheinlicher
     * ist es dort ein Schiff zu treffen.<br>
     * Koordinaten die schon beschossenen wurden und daher nicht mehr
     * gewertet werden können, haben eine Trefferwahrscheinlichkeit
     * von 0. Alle anderen haben mindestens 1.
     */
    private byte[][] probabilityField;



	//Constructor
	/**
	 * Constructor zur Initialisierung des
	 * schweren Schwierigkeitsgrades. <br><br>
	 *
	 * Ein neues Hard-Objekt enthält eine leere Abschussliste.
	 * Alle Informationen bezüglich der schon beschossenen Feldern,
	 * der geprüften Richtungen, der erkundeten Bereiche, verwendeteten Taktiken
	 * und Treffern sind auf den Standardwert gesetzt.<br>
	 *
	 * @param newFieldSize Die Feldgröße des aktuellen Spiels. Die zu erstellenden Zufallskoordinaten werden von 1 bis fieldSize generiert.
     * @param placingAtEdge Einstellung ob man Schiffe an der Kante von anderen Schiffen platzieren darf oder nicht
     * @param newShipList Größe und Anzahl von Schiffen die für dieses Spiel verwendet werden
	 */
	public Hard (int newFieldSize, boolean placingAtEdge, List<Integer> newShipList) {

		super(newFieldSize,placingAtEdge, newShipList);

        //Beim instanziieren werden schon die ersten Koordinaten generiert
        generateAICoordinates();
	}


	//IM


    /**
     * Erhöht die Trefferwahrscheinlichkeit einer einzelnen Koordinate um
     * einen bestimmten Betrag.<br><br>
     *
     * Kann benutzt werden um Bereiche, in der der Gegner
     * Schiffe platziert wo geringere Trefferwahrscheinlichkeiten
     * herrschen, zu verstärken.<br>
     * Die Wahrscheinlichkeiten werden nur erhöht, wenn die angegebene
     * Koordinate noch frei ist, sonst bleibt diese auf "0"
     *
     * @param yCoord Y-Koordinate
     * @param xCoord X-Koordinate
     * @param increase Anzahl um der die Wahrscheinlichkeit der Koordinate erhöht werden soll
     */
    private void increaseOneCoordProbability (int yCoord, int xCoord, int increase){

        if (isCoordinateInField(yCoord,xCoord)){

            if ( !isCoordinateOccupied(yCoord,xCoord) && !isCoordinateShipPart(yCoord,xCoord) ){

                probabilityField[yCoord][xCoord] += increase;
            }
        }
    }


    /**
     * Die Trefferwahrscheinlichkeiten jeder Koordinate im
     * <b>probabilityField</b> werden auf 0 zurückgesetzt um
     * die bestmöglichste Trefferwahrscheinlichkeit für
     * eine neue Runde zu bestimmen.
     */
    private void resetProbabilities (){

        probabilityField = new byte[fieldSize][fieldSize];

        for ( int r = 0; r < super.fieldSize; r++){ //Äußere Schleife für die Reihen

            for (int c = 0; c < super.fieldSize; c++){  //Innere Schleife für Spalten

                probabilityField[r][c] = 0;

            }
        }



    }


    /**
     * Berechnet die Trefferwahrscheinlichkeiten für jede noch frei Koordinate
     * auf dem Spielfeld in dem versucht wird alle noch verbleibenden Schiffstypen horizontal
     * und vertikal auf jede Koordinate zu platzieren. Immer wenn ein Schiff auf der Koordinate
     * platziert werden kann, erhöht sich die Trefferwahrscheinlichkeit für die entsprechenden
     * Koordinaten
     */
    private void generateHitProbabilities (){

        //Zuerst werden die Wahrscheinlichkeiten zurückgesetzt, damit diese jede Runde
        //immer dem aktuellen Status entsprechend neu aktualisiert werden können
        resetProbabilities();


        /** Jede Schiffsgröße wird auf jeder Koordinate versucht zu platzieren
         * um die Trefferwahrscheinlichkeiten berechnen zu können
         */
        for (Map.Entry<Integer,Integer> currentShipSize : super.shipList.entrySet()){

            //Zuerst wird geprüft ob die aktuelle Schiffsgröße noch im Spiel ist
            if (currentShipSize.getValue() > 0 ){

                for ( Integer row : super.includedRows){ //Äußere Schleife für Reihen

                    for ( Integer column: super.includedColumns){   //Innere Schleife für Spalten

                        //Horizontal prüfen
                        calculateProbabilityForShipSize (row, column, currentShipSize.getKey(), 0);

                        //Vertikal prüfen
                        calculateProbabilityForShipSize (row, column, currentShipSize.getKey(), 1);

                    }
                }

            }

        }

        /**
         * Die berechneten Trefferwahrscheinlichkeiten haben die geringsten Werte in den
         * Ecken. Dies kann sich der Gegner als Vorteil verschaffen und seine Schiffe
         * dort platzieren. In dem wir künstlich die Wahrscheinlichkeiten der Ecken,
         * die noch frei sind, erhöhen, können wir die Schwachstelle aushebeln.
         */
        int increaseEdge = 18;
        increaseOneCoordProbability(0,0,increaseEdge);  // Linke-Obere Ecke
        increaseOneCoordProbability(0, fieldSize -1, increaseEdge); //Rechte-Obere Ecke
        increaseOneCoordProbability(fieldSize-1,0,increaseEdge);    //Linke-Untere Ecke
        increaseOneCoordProbability(fieldSize-1,fieldSize-1,increaseEdge);  //Rechte-Untere Ecke

    }


    /**
     *
     * Platziert eine Schiffsgröße auf eine Koordinate und erhöht
     * die Trefferwahrscheinlichkeiten für die betroffenen Koordinaten, falls
     * das Schiff dort platzierbar ist
     *
     * @param currentY Aktuelle Y-Koordinate
     * @param currentX Aktuelle X-Koordinate
     * @param shipSize Die zu untersuchende Schiffsgröße
     * @param direction Die Richtung in der die Schiffsgrößen platziert werden sollen.
     *                  Folgende Werte können übergeben werden:<br>
     *                  0: Horizontal<br>
     *                  1: Vertikal<br>
     */
    private void calculateProbabilityForShipSize ( int currentY, int currentX, int shipSize, int direction){

        /** Speichert ob die Schiffsgröße an den Stellen platziert werden kann oder nicht.
         * Standardweise auf TRUE und ist später dafür verantwortlich, ob die Treffer-
         * wahrscheinlichkeiten incrementiert werden.
         */
        boolean shipSizeFits = true;


        //Die Schiffsgröße wird versucht auf das Spielfeld platziert zu werden
        int i;
        for ( i = 0; i < shipSize; i++){

            //Schiff horizontal platzieren
            if (direction == 0){


                if (isCoordinateInField(currentY, currentX + i)){

                    if( isCoordinateOccupied(currentY, currentX + i) || isCoordinateShipPart(currentY, currentX + i) ){

                        //Die betrachtete Koordinate war besetzt, weshalb das Schiff dort nicht platziert und die
                        //Trefferwahrscheinlichkeit nicht erhöhrt werden kann
                        shipSizeFits = false;
                    }

                }else{

                    //Schiff kann nicht ganz in das Spielfeld platziert werden, weshalb die
                    //Trefferwahrscheinlichkeit nicht erhöht werden kann
                    shipSizeFits = false;
                }

            }else { //Schiff vertikal platzieren


                if (isCoordinateInField(currentY + i, currentX )){

                    if( isCoordinateOccupied(currentY + i, currentX ) || isCoordinateShipPart(currentY + i, currentX ) ){

                        //Die betrachtete Koordinate war besetzt, weshalb das Schiff dort nicht platziert und die
                        //Trefferwahrscheinlichkeit nicht erhöhrt werden kann
                        shipSizeFits = false;
                    }

                }else{

                    //Schiff kann nicht ganz in das Spielfeld platziert werden, weshalb die
                    //Trefferwahrscheinlichkeit nicht erhöht werden kann
                    shipSizeFits = false;
                }

            }
        }



        //Wenn die Schiffsgröße erfolgreich auf der Koordinate platziert wurde, werden die Trefferwahrscheinlichkeiten
        //an den Stellen erhöht

        if (shipSizeFits){

            for (i = 0; i < shipSize; i++){


                if (direction == 0){

                    probabilityField[currentY][currentX + i] += 1;

                }else {

                    probabilityField[currentY + i][currentX] += 1;

                }

            }
        }


    }



    /**
     * Gibt eine Koordinate zurück mit der höchsten Trefferquote<br><br>
     *
     * Die Koordinaten und deren Trefferwahrscheinlichkeiten werden überprüft
     * und es wird aus einer Sammlung der höchsten Wahrscheinlichkeiten eine
     * zufällige Koordinate zurückgegeben
     *
     * @return Eine Koordinate mit der höchsten Trefferwahrscheinlichkeit.
     *         Die zurückgegebene Koordinate muss in seine Einzelteile
     *         zerlegt werden. Der Aufbau ist wie folgt:<br>
     *              <Wahrscheinlichkeit>,<Y-Koordinate>,<X-Koordinate>
     *
     */
    private String maxHitProbabilityCoordinate (){

        /** Speichert temporär die größte Trefferwahrscheinlichkeit, die eine
         *  Koordinate auf dem Spielfeld hat
         * */
        int highestProbability = 0;

        /**
         * Speichert temporär die Wahrscheinlichkeit der Koordinate
         * aus der Liste ab
         * */
        int tempProbability;


        /** Temporäre Liste speichert alle potenziellen
         * Koordinaten ab, die die höchsten Treffer-
         * wahrscheinlichkeiten besitzen
         */
        ArrayList<String> tempProbabilityCoords = new ArrayList<>();

        /**
         * Enthält am Ende alle Koordinaten mit den höchsten
         * Wahrscheinlichkeiten aus denen einer beschossen wird
         */
        ArrayList<String> finalMaxProbabilityCoords = new ArrayList<>();



        for ( int row = 0; row < probabilityField.length; row++ ){

            for( int col = 0; col < probabilityField[row].length; col++){


                if ( probabilityField[row][col] >= highestProbability){

                    /**
                     * Koordinaten werden in dieser Form abgespeichert: <Wahrscheinlichkeit>,<Y-Koordinate>,<X-Koordinate>
                     * Beispiel: "10,0,0"
                     */
                    tempProbabilityCoords.add("" + probabilityField[row][col] + "," + row + "," + col);

                    //Größte existierende Wahrscheinlichkeit aktualisieren
                    highestProbability = probabilityField[row][col];
                }
            }
        }


        /**
         * Da man nicht durch eine Liste iterieren kann und gleichzeitig die Werte löschen kann,
         * die eine geringere Wahrscheinlichkeit als <b>highestProbability</b> haben, müss
         * man daher alle Koordinaten mit der höchsten Wahrscheinlichkeit in eine extra
         * liste übernehmen
         */
        for (String coord : tempProbabilityCoords){


            //Der aktuellen Koordinate wird die Wahrscheinlichkeit
            //extrahiert, damit man sie vergleichbar ist mit der höchsten Wahrscheinlichkeit
            tempProbability = Integer.parseInt(coord.split(",")[0]);

            //Wenn die Wahrscheinlichkeit der betrachteten mit derf Höchstwahrscheinlichkeit übereinstimmt,
            //wird diese übernommen
            if ( tempProbability == highestProbability){

                finalMaxProbabilityCoords.add(coord);
            }
        }


        //Von den gültigen Koordinaten eine zufällige zurückgeben
        return finalMaxProbabilityCoords.get(random.nextInt(finalMaxProbabilityCoords.size()));

    }




	/**
	 * Erstellt zufällige Koordinaten auf die geschossen werden
	 * sollen.<br><br>
	 *
	 * Implementierung der Methode aus der Abstrakten Superklasse Computer.<br>
	 * Koordinaten werden jede Runde neu nach einem Muster erstellt und übergeben.<br><br>
	 *
	 * Bei dem schweren Schwierigkeitsgrad wird die zuletzt getroffene Koordinate
	 * berücksichtigt und die direkte Umgebung wird nach weiteren möglichen
	 * Treffern untersucht.<br><br>
	 *
	 * Ebenso wird das Spielfeld in verschieden Bereiche unterteilt die je nach
	 * Warscheinlichkeit eines Treffers eher oder zuletzt berücksichtigt werden.<br><br>
	 *
	 * Sobald ein Richtungsmuster erkennbar ist, wird in die jeweilige Richtung
	 * geschossen bis ein Schiff zerstört wird oder ins Wasser getroffen wurde.<br><br>
	 *
	 * Auch hier werden alle beschossenen Koordinaten und die Umgebung eines Schiffes
	 * berücksichtigt bei der Erstellung der Zufallskoordinaten.<br>
	 *
	 */
	protected void generateAICoordinates() {


        //Beim Treffer eines Schiffes werden die herumliegenden Koordinaten nach weiteren
        //Schiffsteilen abgesucht
        if (isShipTileHit()){

            int[] tempReturn =  selectNeighbourCoordinates();
            super.setY(tempReturn[0]);
            super.setX(tempReturn[1]);

        } else {

            //Im Normalfall, wenn kein Schiff getroffen wurde, werden die
            //Wahrscheinlichkeiten für den nächsten Zug berechnet
            generateHitProbabilities();

            displayProbabilityField();
            //Flag welches den Durchlauf der Schleifen bestimmt
            boolean loopAgain = true;

            do{
                    //Eine Koordinate mit der höchsten Trefferwahrscheinlichkeit zurückgeben
                    String returnCoordinate = maxHitProbabilityCoordinate();

                    super.setY(Integer.parseInt(returnCoordinate.split(",")[1]) );
                    super.setX(Integer.parseInt(returnCoordinate.split(",")[2]));


                //Es wird überprüft, ob die generierte Koordinate auf eine leere, nicht beschossene
                //Zelle verweist. Ansonsten wird die Koordinate neu generiert bis sie es ist.
                if ( !isCoordinateOccupied (super.getY(), super.getX()) && !isCoordinateShipPart(super.getY(), super.getX()) ){

                    loopAgain = false;


                }


            }while (loopAgain);

            //displayProbabilityField();

        }


	}




    /**
     * Überschreibung der shootResult-Methode der Klasse
     * Computer.<br><br>
     *
     * Wertet die Ergebnisse des letzten Beschusses aus und
     * aktualisiert die Schiffsliste
     *
     *
     * @param yCoord Y-Koordinate der Zelle
     * @param xCoord X-Koordinate der Zelle
     * @param hitState Status der beschossenen Koordinate.
     *                 byte kann folgenden Status besitzen:<br>
     *                 0: Wasser wurde getroffen.<br>
     *                 1: Schiffsteil wurde getroffen.<br>
     */
    public void shootResult(int yCoord, int xCoord, byte hitState ){

        super.shootResult(yCoord, xCoord,hitState);

        //Aktualisierung der Schiffsliste
        if (hitState == 2){

            updateShipList();

        }

        //Eingeschlossene Felder ausschließen
       excludeInvalidFields();


        //Nach der Auswertung der Ergebnisse wird die nächste Koordinate generiert
        generateAICoordinates();

    }



    public void displayProbabilityField() {
        //Ausgabe der oberen Feldbeschriftung
        System.out.println("  0  1  2  3  4  5  6  7  8  9");
        //1. Zähler
        int y;
        //2. Zähler
        int x;
        //doppelte Schleife für Durchlauf durch alle Felder

        for(y=0; y  < probabilityField.length; y++) {
            //Ausgabe der seitlichen Feldbeschriftung
            System.out.print(y);
            for(x=0; x < probabilityField[y].length; x++) {

                //Ausgabe der einzelnen Zellen
                if ( probabilityField[y][x] < 10){

                    System.out.print(" 0" + probabilityField[y][x]);
                }else {
                    System.out.print(" " + probabilityField[y][x]);
                }

            }

            //Zeilenumbruch
            System.out.println();
        }
    }

	@Override
	public void run() {

	}

}// end class Hard