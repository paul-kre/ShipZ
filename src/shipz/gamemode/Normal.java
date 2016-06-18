package shipz.gamemode;

import java.util.List;

/**
 * Normaler Schwierigkeitsgrad<br><br>
 *
 * Erstellte Koordinaten werden an die Verwaltung übergeben. <br>
 * KI berücksichtigt zalles was der einfache Schwierigkeitsgrad macht und
 * benutzt zusätzlich ein 3-Feld-Muster um alle Felder die mindestens drei
 * Felder groß sind mit so wenig Treffern wie möglich zu erwischen.<br>
 *
 * @author Artur Hergert
 *
 */
public class Normal extends Computer {



	//Constructor
	/**
	 * Constructor zur Initialisierung des
	 * normalen Schwierigkeitsgrades. <br><br>
	 *
	 * Ein neues Normal-Objekt enthält eine leere Abschussliste.<br>
	 * Alle Informationen bezüglich der schon beschossenen Feldern,
	 * der geprüften Richtungen und Treffern sind auf den Standardwert gesetzt.<br>
	 *
	 * @param newFieldSize Die Feldgröße des aktuellen Spiels. Die zu erstellenden Zufallskoordinaten werden von 1 bis fieldSize generiert.
     * @param placingAtEdge Einstellung ob man Schiffe an der Kante von anderen Schiffen platzieren darf oder nicht
     * @param newShipList Größe und Anzahl von Schiffen die für dieses Spiel verwendet werden
	 */
	public Normal (int newFieldSize, boolean placingAtEdge, List<Integer> newShipList) {

		super(newFieldSize,placingAtEdge, newShipList);
	}

	//IM
	/**
	 * Erstellt zufällige Koordinaten auf die geschossen werden
	 * sollen.<br><br>
	 *
	 * Implementierung der Methode aus der Abstrakten Superklasse Computer.<br>
     * Koordinaten werden mithilfe des 3-Feld-Musters (<b>randomThreePointPatternInt</b>)
     * erstellt und an die Verwaltung übergeben. <br><br>
     *
     * Sobald alle größeren Schiffe zerstört wurden, schwenkt das Muster auf das
     * erweiterte Schachbrettmuster Algorithmus (<b>enhancedChessBoardPattern</b>)
     * und es wird versucht mit so wenig Schüssen wie möglich die kleineren Schiffe
     * zu entdecken
	 *
	 */
	protected void generateAICoordinates() {
        //loopHoleVar = 0;


        /** Speicherung der Koordinate des Schachbrettmusters */
        String chessBoardCoordinate;




        //Falls schon ein Schiff getroffen wurde, wird um den Treffer gesucht und eine Koordinate zurückgegeben
        if (isShipTileHit()){

            int[] tempReturn =  selectNeighbourCoordinates();
            super.setY(tempReturn[0]);
            super.setX(tempReturn[1]);

        } else { //Falls noch kein Schiff getroffen wurde, werden zufällige erstellt und zurückgegeben.



            //Flag welches den Durchlauf der Schleifen bestimmt
            boolean loopAgain = true;

            do{


                //Solange das 3-Feld-Muster noch Koordinaten beschießen kann,
                //wird es ausgeführt
                if (threePointPatternIsStillValid()){

                    super.setY(super.randomRowInt());
                    super.setX(super.randomThreePointPatternInt(super.getY()));

                } else {
                    /**
                     * Wenn das 3-Feld-Muster zu Ende gegangen ist, wird das Schachbrettmuster ausgeführt
                     * Sobald auch das Schachbrettmuster fertig ist, werden die restlichen Einser-Felder
                     * beschossen, die noch auf dem Spielfeld stehen.
                     */

                    chessBoardCoordinate = chessBoardPatternString();
                    super.setY(super.extcractYCoord(chessBoardCoordinate));
                    super.setX(super.extcractXCoord(chessBoardCoordinate));

                    //Die erstelle Koordinate war gültig und wurde für die Verwaltung gespeichert, deshalb
                    // wird diese aus der Schachbrettmuster-Liste gelöscht
                    deleteChessBoardPatternCoordinate(chessBoardCoordinate);
                }


                //Es wird überprüft, ob die generierte Koordinate auf eine leere, nicht beschossene
                //Zelle verweist. Ansonsten wird die Koordinate neu generiert bis sie es ist.
                if ( !isCoordinateOccupied (super.getY(), super.getX()) && !isCoordinateShipPart(super.getY(), super.getX()) ){

                    loopAgain = false;


                }


            }while (loopAgain);


        }

        /**
         * Y- und X-Koordinaten wurden gespeichert und
         * es wird jetzt der Main Klasse mitgeteilt, dass fertige
         * Koordinaten bereitstehen
         * */
       // fireGameEvent(SHOOT_EVENT);
	}

	@Override
	public void run() {


	}

}//end class Normal