package shipz.gamemode;


/**
 * Einfacher Schwierigkeitsgrad<br><br>
 *
 * Erstellte Koordinaten werden an die Verwaltung übergeben.<br>
 * KI speichert getroffene Felder, beschießt jede Runde eine
 * neue Koordinate und berücksichtigt keine Treffer.<br>
 *
 * @author Artur Hergert
 *
 */
public class Easy extends Computer {

    /** Klassenvariable für Debug-Zwecke. Zählt die Anzahl der Durchläufe der KI und somit dessen Effizienz */
    public static int efficiency = 0;

    /** Klassenvariable die die höchste Durchlaufzahl speichert */
    public static int maxLoop = 0;

    /** Debugging Klassenvariablen. Werden in der generateAICoordinates Methode zurückgegeben um ein exaktes Schiff auf dem
     * Spielfeld zu zerstören
     */
    static int debugY = 4;
    static int debugX = 2;



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
     */
    public Easy (int newFieldSize) {

        super(newFieldSize);
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
        /****************************************/
        efficiency = 0;
        /*****************************************************/



        //Falls schon ein Schiff getroffen wurde, wird um den Treffer gesucht und eine Koordinate zurückgegeben
        if (isShipTileHit()){

            /****************************************/
            efficiency++;
            //System.out.println("Durchlaeufe: " + efficiency);
            /*****************************************************/

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
                    /*****************************************/
                    efficiency--;
                    /*****************************************/

                }
                /*****************************************/
                efficiency++;
                /*****************************************/
            }while (loopAgain);


            /****************************************
             efficiency++;
             System.out.println("Durchlaeufe: " + efficiency);

             if ( efficiency > maxLoop){

             maxLoop = efficiency;
             }

             System.out.println("Laengste Durchlaufzeit pro Koordinate: " + maxLoop);


             ****************************************************/


        /*
        if (debugY < 5){
            debugY++;
            return "" + debugY + "," + debugX;
        }
        */

        }

        /**
         * Y- und X-Koordinaten wurden gespeichert und
         * die fertigen Koordinaten werden mit dem
         * SHOOT_EVENT an die Main Klasse
         * */
        fireGameEvent(SHOOT_EVENT);


    }



     public void run() {
        // TODO Auto-generated method stub

    }

}//end class Easy