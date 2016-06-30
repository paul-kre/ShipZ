package shipz.ai;

import java.util.ArrayList;
import java.util.List;

/**
 * Normaler Schwierigkeitsgrad<br><br>
 *
 * Erstellte Koordinaten werden an die Verwaltung übergeben. <br>
 * KI berücksichtigt alles was der einfache Schwierigkeitsgrad macht und
 * benutzt zusätzlich ein 3-Feld-Muster um alle Felder, die mindestens drei
 * Felder groß sind, mit so wenig Treffern wie möglich zu erwischen.<br>
 *
 * @author Artur Hergert
 *
 */
public class Normal extends Computer {

    //IV

    /**
     * Sobald das Schachbrettmuster aktiviert wird, wird das Spielfeld
     * nach freien Koordinaten abgesucht, die dem Muster entsprechen und
     * diese werden in der Liste gespeichert, damit die KI diese entsprechenden
     * Koordinaten beschießen kann.
     */
    private ArrayList<String> validChessBoardPatternCoordinates = new ArrayList<>();

    /**
     * Flagvaribale die abspeichert ob das Schachbrettmuster initialisiert wurde oder nicht<br><br>
     *
     * Sobald es einmal initialisiert wurde, bleibt der Wert auf TRUE, damit nach
     * dem Abschuss aller Koordinaten des Schachbrettmuster keine weiteren mehr erzeugt werden
     * müssen, da danach nur noch Einser-Felder existieren und diese nicht dem Schachbrettmuster
     * entsprechen.<br>
     *
     * */
    private boolean chessBoardPatternActivated = false;


    /**
     * Speicherung des zuletzt getroffenen Spielfeldviertels.<br>
     * Das Spielfeldviertel-Muster wird parallel zum 3-Feld-Muster
     * ausgeführt und sorgt dafür, dass das 3-Feld-Muster nicht nur
     * in einem Bereich in diagonalen schießt, sodass es für den
     * menschlichen Spieler sofort ersichtlich ist wie die KI vor geht. Durch
     * das Spielfeldviertel-Muster werden die Beschüsse des 3-Feld-Musters
     * besser verstreut<br>
     *
     * <b>lastHitQuarter</b> hat vor der Initialisierung den Wert -1.
     * Sobald das Viertel-Muster parallel mit dem 3-Feld-Muster einsetzt, kann
     * diese Variable folgende Werte enthalten:<br>
     *     0: Oberes-Linkes Viertel<br>
     *     1: Oberes-Rechtes Viertel<br>
     *     2: Unteres-Linkes Viertel<br>
     *     3: Unteres-Rechte Viertel<br><br>
     *
     *  Der Wert sorgt dafür, dass die generierten Koordinaten im nächsten
     *  Zug nicht im selben Viertel wie davor beschossen wird.
     *
     *
     */
    private byte lastHitQuarter = -1;




	//Constructor
	/**
	 * Konstruktor zur Initialisierung des
	 * normalen Schwierigkeitsgrades. <br><br>
	 *
	 * Ein neues Normal-Objekt enthält eine leere Abschussliste.<br>
	 * Alle Informationen bezüglich der schon beschossenen Feldern,
	 * der geprüften Richtungen und Treffern sind auf den Standardwert gesetzt.<br>
     * Beim instanziieren wird direkt eine neue Koordinate generiert.
	 *
	 * @param newFieldSize Die Feldgröße des aktuellen Spiels. Die zu erstellenden Zufallskoordinaten werden von 1 bis fieldSize generiert.
     * @param placingAtEdge Einstellung ob man Schiffe an der Kante von anderen Schiffen platzieren darf oder nicht
     * @param newShipList Größe und Anzahl von Schiffen die für dieses Spiel verwendet werden
	 */
	public Normal (int newFieldSize, boolean placingAtEdge, List<Integer> newShipList) {

		super(newFieldSize,placingAtEdge, newShipList);

        //Beim instanziieren werden schon die ersten Koordinaten generiert
        generateAICoordinates();
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
     * Sobald mit dem 3-Feld-Muster keine weiteren Koordinaten mehr beschiebar sind,
     * setzt das Schachbrettmuster ein (<b>chessBoardPattern</b>), welches die restlichen
     * Koordinaten in Blöcke einteilt und Koordinaten zum Beschuss ermittelt, wo man am
     * ehesten möglichst viele Schiffsteile treffen könnte.<br><br>
     *
     * Zusätzlich werden freie Felder, die zwischen anderen besetzten Feldern eingesperrt sind,
     * von der KI ausgeschlossen, sobald es keine Schiffe dessen Größe mehr im Spiel
     * vorhanden sein können.
	 *
	 */
	protected void generateAICoordinates() {


        /** Speicherung der Koordinate des Schachbrettmusters */
        String chessBoardCoordinate;




        //Falls schon ein Schiff getroffen wurde, wird um den Treffer gesucht und eine Koordinate zurückgegeben
        if (isShipTileHit()){

            int[] tempReturn =  selectNeighbourCoordinates();
            super.setY(tempReturn[0]);
            super.setX(tempReturn[1]);

        } else {
            //Falls noch kein Schiff getroffen wurde, werden die Koordinaten nach den Mustern erstellt und zurückgegeben.



            //Flag welches den Durchlauf der Schleifen bestimmt
            boolean loopAgain = true;

            do{


                //Solange das 3-Feld-Muster noch Koordinaten beschießen kann,
                //wird es ausgeführt
                if (threePointPatternIsStillValid()){

                    //Zur besseren Streuung werden die Koordinaten mittels
                    // des Viertel-Musters generiert und überprüft
                    do {

                        super.setY(super.randomRowInt());
                        super.setX(randomThreePointPatternInt(super.getY()));

                    }while( !fieldQuarterPatternIsValid());


                    if (super.random.nextInt(5) == 2){

                            super.setX(super.randomColumnInt());

                    }



                } else {
                    /**
                     * Wenn das 3-Feld-Muster zu Ende gegangen ist, wird das Schachbrettmuster ausgeführt
                     * Sobald auch das Schachbrettmuster fertig ist, werden die restlichen Einser-Felder
                     * beschossen, die noch auf dem Spielfeld stehen.
                     */

                    chessBoardCoordinate = chessBoardPatternString();
                    super.setY(super.extractYCoord(chessBoardCoordinate));
                    super.setX(super.extractXCoord(chessBoardCoordinate));

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
	}


    /**
     * Überschreibung der shootResult-Methode der Klasse
     * Computer.<br><br>
     *
     * Nach der Auswertung der Ergebnisse wird das Spielfeld
     * für weitere Berechnung untersucht.<br>
     * Folgendes wird untersucht:<br>
     *      - Aktualisierung der Schiffsliste und ausschließen von Feldern die
     *      keine Schiffe mehr enthalten können (Beispiel:
     *      Alle 2er-Schiffe wurden zerstört -> Alle restlichen, zusammenstehenden
     *      2er-Felder werden für die Generierung weiterer Koordinaten ausgeschlossen)<br><br>
     *
     *      - Streuverhalten der Abschüsse durch das Viertel-Muster bestimmen und entsprechend kalibrieren,
     *      sodass während des 3-Feld-Musters immer möglichst alle Bereiche gleich viel beschossen werden<br>
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

        //Streuverhalten des 3-Feld-Musters mit dem Viertel-Muster kalibrieren//
        updateFieldQuarterPattern();

        //Aktualisierung der Schiffsliste
        if (hitState == 2){

            updateShipList();

        }

        //Eingeschlossene Felder ausschließen
        excludeInvalidFields();


        generateAICoordinates();

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
         * Abstand zwischen den X-Koordinaten sein muss
         */

        int level = yCoord + 1;

        int threePointPatternInt = -1;

        //Ebene wird überprüft ob es die Stufe 1,2 oder 3 hat und je nachdem wird weiterberechnet

        if (level  % 3 == 1 ){ //Prüfen ob X-Koordinate für eine Ebene der Stufe 1 berechnet werden soll

            threePointPatternInt = ( 2 + ( 3 * validThreePointPatternColumnInt(1) ));

        } else if ( level % 3 == 2){ //Prüfen ob X-Koordinate für eine Ebene der Stufe 2 berechnet werden soll

            threePointPatternInt = ( 1 + ( 3 * validThreePointPatternColumnInt(2) ));


        } else if ( level % 3 == 0){ //Prüfen ob X-Koordinate für eine Ebene der Stufe 3 berechnet werden soll

            threePointPatternInt = ( 3 * validThreePointPatternColumnInt(3) );

        }

        return threePointPatternInt;

    }


    /**
     * Berechnet eine gültige X-Koordinate aus den möglichen Feldern einer Ebene
     * und gibt diese für weitere Berechnungen zurück
     *
     * @param currentLevel Die Ebene für die die X-Koordinate berechnet wird
     * @return Eine X-Koordinate die dem 3-Feld-Muster der entsprechenden Ebene entspricht
     */
    private int validThreePointPatternColumnInt(int currentLevel){

        //loopHoleVar = 0;

        //Flag zum Überrpüfen ob die erstellte X-Koordinate gültig ist (ob sie zwischen 0 und fieldSize ist UND dem Muster entpricht der aktuellen Ebene)
        boolean validXCoord = false;

        //Die X-Koordinate die zurückgegeben wird
        int patternXCoord;


        do{


            //Der X-Koordinate wird eine zufällige Zahl aus den gespeicherten, noch nicht leeren Spaltenkoordinaten zugewiesen
            patternXCoord = randomColumnInt() ;


            if (currentLevel == 1){


                if ( patternXCoordIsInRange( (patternXCoord / 3), 1) ){

                    validXCoord = true;
                }


            } else if ( currentLevel == 2){


                if ( patternXCoordIsInRange( (patternXCoord / 3), 2) ){

                    validXCoord = true;
                }

            } else  if (currentLevel == 3){

                if ( patternXCoordIsInRange( (patternXCoord / 3), 3) ){

                    validXCoord = true;
                }


            }


        }while ( !validXCoord);

        return (patternXCoord / 3);

    }


    /**
     * Prüft ob eine X-Koordinate, die nach dem 3-feld-Muster erstellt wurde, auch
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
         * beendet die Methode in dem sie TRUE zurückgibt und die KI weiss, dass
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



                    if ( !isCoordinateOccupied(includedRows.get(i), (2 + (3 * j)) ) && !isCoordinateShipPart(includedRows.get(i), (2 + (3 * j)) )){

                        return true;
                    }

                }

                //Prüft ob die Y-Koordinate der Ebene 2 entspricht
            } else if ( (includedRows.get(i) + 1) % 3 == 2 ){

                for( int k = 0; k <= ((this.fieldSize/ 3) -1); k++){


                    if ( !isCoordinateOccupied(includedRows.get(i), (1 + (3 * k)) ) && !isCoordinateShipPart(includedRows.get(i), (1 + (3 * k)) )){

                        return true;
                    }

                }

                //Prüft ob die Y-Koordinate der Ebene 3 entspricht
            } else if ( (includedRows.get(i) + 1) % 3 == 0 ){

                for( int l= 0; l <= (this.fieldSize/ 3) ; l++){

                    if ( !isCoordinateOccupied(includedRows.get(i), (3 * l))  && !isCoordinateShipPart(includedRows.get(i), (3 * l) ) ){

                        return true;
                    }

                }

            }

        }


        return false;
    }




    /**
     * Überprüfung ob die generierten Koordinaten der KI
     * sich im angegebenen Viertel aufhält oder nicht
     *
     * @param rowBeginn Anfangspunkt der Reihe
     * @param rowEnd Endpunkt der Reihe
     * @param columnBeginn Anfangspunkt der Spalte
     * @param columnEnd Endpunkt der Spalte
     * @return Ob sich eine Koordinate in einem angegebenen Viertel aufhält oder nicht
     */
    private boolean isInFieldQuarter ( int rowBeginn, int rowEnd, int columnBeginn, int columnEnd){



        for ( int r = rowBeginn; r < rowEnd; r++ ){ //Äußere FOR-Schleife läuft die Y-Koordinaten durch

            for ( int c = columnBeginn; c < columnEnd; c++){ //Innere FOR-Schleife läuft die X-Koordinaten durch


                if ( (super.getY() == r) && (super.getX() == c)){

                    //Sobald im Viertel die angegebenen Koordinaten erkannt wurden, wird die Methode beendet
                    return true;

                }

            }
        }

        //Alle Koordinaten des Viertels wurden durchlaufen und die Koordinaten waren nicht im angegebenen Viertel
        return false;
    }


    /**
     * Die erstellten Koordinaten der KI werden überprüft, ob
     * sich das Viertel, in dem die zuletzt erstellte, an die Main übergebene
     * Koordinate mit dem Viertel der neu generierten unterscheidet. Nur
     * wenn sie sich unterscheiden wird TRUE ausgegeben.
     *
     * @return Ob sich die das letzte Viertel mit dem neuen unterscheidet oder nicht
     */
    protected boolean fieldQuarterPatternIsValid (){

        //Beim ersten Aufruf gab es noch keinen letzten Zug, deshalb ist der erste automatisch gültig
        if (lastHitQuarter == -1){
            return true;
        }



        //Prüfen ob der neue Zug im Oberen-Linken Viertel und daher FALSE ist
        if ( lastHitQuarter == 0){

            return  !isInFieldQuarter(0, (fieldSize/2), 0,(fieldSize/2));
        }

        //Prüfen ob der neue Zug im Oberen-Rechten Viertel und daher FALSE ist
        if ( lastHitQuarter == 1){

            return  !isInFieldQuarter(0, (fieldSize/2), (fieldSize/2), fieldSize);
        }

        //Prüfen ob der neue Zug im Unteren-Linken Viertel und daher FALSE ist
        if ( lastHitQuarter == 2){

            return  !isInFieldQuarter( (fieldSize/2), fieldSize, 0,(fieldSize/2));
        }

        //Prüfen ob der neue Zug im Unteren-Rechten Viertel und daher FALSE ist
        if ( lastHitQuarter == 3){

            return  !isInFieldQuarter((fieldSize/2), fieldSize, (fieldSize/2), fieldSize);

        }



        //Der neue Zug entspricht dem Viertel-Muster und ist daher gültig
        return true;



    }


    /**
     * Das zuletzt beschossene Viertel wird abgespeichert,
     * sodass es nicht im nächsten Zug nochmal beschossen
     * werden kann
     */
    protected void updateFieldQuarterPattern(){

        //Prüfen ob sich die beschossene Koordinate im Oberen-Linken Viertel befindet
        if ( isInFieldQuarter(0, (fieldSize/2), 0,(fieldSize/2)) ){

            lastHitQuarter = 0;
        }

        //Prüfen ob sich die beschossene Koordinate im Oberen-Rechten Viertel befindet
        if ( isInFieldQuarter(0, (fieldSize/2), (fieldSize/2), fieldSize) ){

            lastHitQuarter = 1;
        }

        //Prüfen ob sich die beschossene Koordinate im Unteren-Linken Viertel befindet
        if ( isInFieldQuarter( (fieldSize/2), fieldSize, 0,(fieldSize/2)) ){

            lastHitQuarter = 2;
        }

        //Prüfen ob sich die beschossene Koordinate im Unteren-Rechten Viertel befindet
        if ( isInFieldQuarter((fieldSize/2), fieldSize, (fieldSize/2), fieldSize) ){

            lastHitQuarter = 3;
        }

    }





    /**
     * Erstellt anhand der verbleibenden, freien Koordinaten auf dem Spielfeld
     * Blöcke und unterteilt diese mithilfe eines Schachbrettmusters.<br>
     * Die Anzahl der Schüsse um ein Schiffsteil damit zu treffen sinkt
     * und die Chance dabei etwas zu treffen erhöht sich.
     *
     * @return Eine beschießbare Koordinate aus einem Block
     */
    protected String chessBoardPatternString(){

        //Falls die Liste des Schachbrettmusters leer ist (beim ersten
        // Aufruf), wird das Feld gescannt und die Liste initialisiert
        if ( !chessBoardPatternActivated){

            initializeChessBoardPattern();
            chessBoardPatternActivated = true;
        }


        /**
         * Nachdem geprüft wurde ob das Schachbrettmuster initialisiert wurde oder nicht,
         * wird eine Koordinate aus der Liste zurückgegeben.
         * Wenn die Liste allerdings leer ist (da nur noch Einser-Schiffe auf dem Spielfeld
         * existieren), werden die restlichen Felder zufällig beschossen.
         */
        if  (!validChessBoardPatternCoordinates.isEmpty()){

            return validChessBoardPatternCoordinates.get(random.nextInt(validChessBoardPatternCoordinates.size()));
        }else {
            return "" + randomRowInt() + "," + randomColumnInt();
        }



    }


    /**
     * Scannt das Spielfeld nach freien Feldern und
     * speichert mithilfe des Schachbrettmusters
     * die Koordinaten in das <b>validChessBoardPatternCoordinates</b>
     * ab
     */
    private void initializeChessBoardPattern(){

        //Flag-Variable die angibt, ob bei der Prüfung der nördlichen Koordinate die aktuelle gespeichert wurde
        boolean wasSaved;


        for ( int i = 0; i < includedRows.size(); i++){

            for (int j = 0; j < includedColumns.size(); j++){

                wasSaved = false;


                //Aktuelle Koordinate prüfen ob sie frei ist, ansonsten wird zum nächsten weitergegangen
                if ( !isCoordinateOccupied( includedRows.get(i), includedColumns.get(j) ) && !isCoordinateShipPart( includedRows.get(i), includedColumns.get(j))){

                    //Nördliche Koordinate prüfen
                    if( isCoordinateInField( includedRows.get(i) -1, includedColumns.get(j) ) && !isCoordinateShipPart(includedRows.get(i) -1, includedColumns.get(j)) && !isCoordinateOccupied(includedRows.get(i) -1, includedColumns.get(j))){


                        //Wenn die nördliche Koordinate nicht gespeichert wurde, wird die aktuelle Koordinate in die Liste
                        //gespeichert
                        if( !validChessBoardPatternCoordinates.contains("" + (includedRows.get(i) - 1) + "," + includedColumns.get(j)  )){

                            validChessBoardPatternCoordinates.add("" + includedRows.get(i) + "," + includedColumns.get(j) );
                            wasSaved = true;
                        }

                    }

                    //Wenn die aktuelle Koordinate bei der Überrprüfung des Nordens nicht gespeichert wurde, wird die
                    //westliche Koordinate überprüft
                    if ( !wasSaved){

                        //Westliche Koordinate prüfen
                        if(isCoordinateInField( includedRows.get(i), includedColumns.get(j) - 1) && !isCoordinateShipPart(includedRows.get(i), includedColumns.get(j) - 1) && !isCoordinateOccupied(includedRows.get(i), includedColumns.get(j) - 1) ){

                            //Wenn die westliche Koordinate nicht gespeichert wurde, wird die aktuelle Koordinate in die Liste
                            //gespeichert
                            if( !validChessBoardPatternCoordinates.contains("" + includedRows.get(i)  + "," + (includedColumns.get(j) -1) )){

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
     * Implementierung der Methode aus
     * der Klasse Computer ohne Inhalt
     */
	public void run() {


	}

}//end class Normal