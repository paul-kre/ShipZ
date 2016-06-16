package shipz.gamemode;


/**
 * Einfacher Schwierigkeitsgrad<br><br>
 *
 * Erstellte Koordinaten werden an die Verwaltung übergeben.<br>
 * KI speichert getroffene Felder, beschießt zufällige Felder, außer
 * es trifft ein Schiffsteil. In dem Fall wird die Gegend des Schiffes
 * untersucht und in die Richtung entlang beschossen.<br>
 *
 * @author Artur Hergert
 *
 */
public class Easy extends Computer {


    //Constructor
    /**
     * Constructor zur Initialisierung des
     * einfachen Schwierigkeitsgrades. <br><br>
     *
     * Ein neues Easy-Objekt enthält eine leere Abschussliste.<br>
     * Alle Informationen bezüglich der schon beschossenen Feldern
     * und Treffern sind standardmäßig auf null.<br>
     *
     * @param newFieldSize Die Feldgröße des aktuellen Spiels. Die zu erstellenden Zufallskoordinaten werden von 1 bis fieldSize generiert.
     * @param placingAtEdge Einstellung ob man Schiffe an der Kante von anderen Schiffen platzieren darf oder nicht
     */
    public Easy (int newFieldSize,boolean placingAtEdge) {

        super(newFieldSize,placingAtEdge);
       generateAICoordinates();

    }


    /**
     * Erstellt zufällige Koordinaten auf die geschossen werden
     * sollen.<br><br>
     *
     * Implementierung der Methode aus der Abstrakten Superklasse Computer.<br>
     * Koordinaten werden jede Runde neu zufällig erstellt und übergeben. Bei dem einfachen
     * Schwierigkeitsgrad wird die zuletzt getroffene Koordinate
     * berücksichtigt, sodass in der nächsten Runde der Umkreis der getroffenen Koordinate überprüft wird. <br>
     * Ebenso wird die Liste mit den schon beschossenen Feldern
     * und der Umgebung eines zerstörten Schiffes beachtet.<br>
     *
     */
    protected void generateAICoordinates() {


        //Falls schon ein Schiff getroffen wurde, wird um den Treffer gesucht und eine Koordinate zurückgegeben
        if (isShipTileHit()){

            int[] tempReturn =  selectNeighbourCoordinates();
            super.setY(tempReturn[0]);
            super.setX(tempReturn[1]);

        } else { //Falls noch kein Schiff getroffen wurde, werden zufällige erstellt und zurückgegeben.

            //Zufallskoordinaten solange erstellen und speichern bis sie gültig sind und beschießbar sind

            //Flag welches den Durchlauf der Schleifen bestimmt
            boolean loopAgain = true;

            do{

                super.setY(super.randomRowInt());
                super.setX(super.randomColumnInt());

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
        //fireGameEvent(AI_SHOOT_EVENT);


    }


    public static void main (String args[]){
        int patternXCoord = 3;
        System.out.println( (patternXCoord - 1) % 3);
    }

     public void run() {
        // TODO Auto-generated method stub

    }

}//end class Easy