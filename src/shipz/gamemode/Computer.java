package shipz.gamemode;

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
	protected int fieldSize = 10;

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
	protected Random random = new Random();


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
		initiateMirrorField(fieldSize);

	}



	/**
	 * Implementierung der shootField-Methode von
	 * Player ohne Inhalt.
	 */
	public String shootField() { return null; }


	/**
	 * Zur Erstellung von Zufallskoordinaten: public int nextInt(int n)
	 */


    /**
     * Prüft ob aktuell ein Schiff getroffen, aber noch nicht versenkt wurde
     *
     * @return Ob ein Schiff getroffen wurde oder nicht
     */
    protected boolean isShipTileHit(){

        return (this.firstShipTileHit[0] != -1  && this.firstShipTileHit[1] != -1);
    }
	/**
	 * Die direkten Nachbaarkoordinaten einer getroffenen Koordinate werden
	 * zur Überprüfung ausgewählt
	 *
	 */
	protected int[] selectNeighbourCoordinates(){

        //Flag, welches versichert, dass aufjedenfall eine Richtung Koordinaten an shootField zurückgibt
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
                   if( isCoordinateInField( (this.currentShipTile[0]) -1,  this.currentShipTile[1] ) ){

                       return new int[]{ this.currentShipTile[0] -1,  this.currentShipTile[1] } ;

                    } else {

                       //Koordinate war nicht im Spielfeld, deshalb wird zur nächsten Richtung gewechselt
                        setNextDirection();
                       directionReturnsNoCoord  = true;

                   }

                    //Südliche Richtung überprüfen
                } else if (this.currentDirection == 1){

                    //Prüfen ob die aktuelle Koordinate in der Richtung überhaupt im Spielfeld liegt
                    if( isCoordinateInField( (this.currentShipTile[0]) +1,  this.currentShipTile[1] ) ){

                        return new int[]{ this.currentShipTile[0] +1,  this.currentShipTile[1] } ;

                    }else {

                        //Koordinate war nicht im Spielfeld, deshalb wird zur nächsten Richtung gewechselt
                        setNextDirection();
                        directionReturnsNoCoord  = true;

                    }


                //Westliche Richtung überprüfen
                } else if (this.currentDirection == 2){

                    //Prüfen ob die aktuelle Koordinate in der Richtung überhaupt im Spielfeld liegt
                    if( isCoordinateInField( (this.currentShipTile[0]) ,  this.currentShipTile[1] -1 ) ){

                        return new int[]{ this.currentShipTile[0] ,  this.currentShipTile[1] -1 } ;

                    }else {

                        //Koordinate war nicht im Spielfeld, deshalb wird zur nächsten Richtung gewechselt
                        setNextDirection();
                        directionReturnsNoCoord  = true;

                    }


                    //Östliche Richtung überprüfen
                }else if (this.currentDirection == 3){

                    //Prüfen ob die aktuelle Koordinate in der Richtung überhaupt im Spielfeld liegt
                    if( isCoordinateInField( (this.currentShipTile[0]),  this.currentShipTile[1] +1 ) ){

                        return new int[]{ this.currentShipTile[0] ,  this.currentShipTile[1] +1 } ;

                    }else {

                        //Koordinate war nicht im Spielfeld, deshalb wird zur nächsten Richtung gewechselt
                        setNextDirection();
                        directionReturnsNoCoord  = true;

                    }


                }


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
	 *
	 * Initalisierung des Spiegelfeldes.<br>
	 *
	 * Alle Koordinaten werden auf 0 (noch nicht beschossen) gesetzt und sind
	 * standardmäßig beschießbar.
	 *
	 * @param fieldSize Feldgröße des Spiegelfeldes. Sollte die gleiche Größe wie das benutzte Spieler- bzw KI-Feld sein.
	 */
	private void initiateMirrorField (int fieldSize){

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
	 * DEBUGGING METHODE FÜR MIRRORFIELD
	 *
	 * Zeigt das SpiegelFeld als ein Spielfeld.
	 * Nichtbeschossene Felder sind leer, beschossene Felder enthalten ein 'O'
	 */
	public void displayMirrorField() {
		//Ausgabe der oberen Feldbeschriftung
		System.out.println("  A B C D E F G H I J");
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



    /**
     * Überprüfen von Koordinaten ob diese von der KI
     * beschossen und eventuell ein Schiff getroffen/zerstört wurden
     *
     * Methode sollte direkt nach dem Aufruf der shootField Methode
     * der KI ausgeführt werden, damit eine Generierung von redundanten
     * Koordinaten vermiedern werden kann.
     *
     * @param yCoord Y-Koordinate der Zelle
     * @param xCoord X-Koordinate der Zelle
     * @param hitState Status der beschossenen Koordinate.
     *                 byte kann folgenden Status besitzen:
     *                 0: Wasser wurde getroffen.
     *                 1: Schiffsteil wurde getroffen.
     *                 2: Schiffsteil wurde versenkt.
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
        } else {

            //Wenn ein Beschuss in einer Richtung keinen Treffer erzielte,
            // Wird die nächste Richtung ab dem Ankerpunkt weitergesucht
            if (isShipTileHit()){
                this.currentShipTile[0]= this.firstShipTileHit[0];
                this.currentShipTile[1]= this.firstShipTileHit[1];
                setNextDirection();
            }
            setCoordinateOccupied(yCoord, xCoord);
        }
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

		//Linker und Rechter Nachbar von current speichern
		if ( isCoordinateInField(currentY-1, currentX) ){ //Links von current
			setCoordinateOccupied(currentY-1, currentX);
		}

		if ( isCoordinateInField(currentY+1, currentX) ){ //Rechts von current
			setCoordinateOccupied(currentY+1, currentX);
		}
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

		/***************************/
		int efficiency = 0;
		/****************************/


		//Temporäre Koordinaten. Haben zu Beginn die Koordinaten des Treffers, welches ein
		//Schiff zum Versenken gebracht haben. Von da aus werden alle Richtungen geprüft
		int currentY = yCoord;
		int currentX = xCoord;

		//Flag-Variable zum Durchlaufen der While-Schleifen. Prüfen immer ob die Richtung weitergeprüft werden soll.
		boolean directionCheck = false;


		//Richtung durchlaufen
		do{


            directionCheck = false;

			//Es wird pro Schleifendurchgang eine Koordinate in die Nordrichtung weitergegangen
			currentY = currentY + yDirection;
			currentX = currentX + xDirection;

			//Prüfen ob die nördliche Koordinate überhaupt im Spielfeld liegt
			if (isCoordinateInField(currentY, currentX) ){

				//Prüfen ob die nördliche Variable schonmal beschossen wurde
				if ( isCoordinateOccupied (currentY, currentX ) || isCoordinateShipPart(currentY, currentX)){

					//Schlussendeliche Überprüfung ob die Koordinate ein Teil des Schiffes ist
					if( isCoordinateShipPart (currentY, currentX)  ){

						//Prüfen ob Nord- oder Südrichtung durchlaufen wird
						if ( (yDirection == -1  && xDirection == 0) || (yDirection == 1  && xDirection == 0) ){

							//Vertikalen Seiten abspeichern
							saveShipVerticalSideEdge(currentY,currentX);

						} else { // Bei West- oder Ostrichtung

							//Horizontal Seiten abspeichern
							saveShipHorizontalSideEdge(currentY,currentX);
						}


						//Nördliche Richtung wurde noch nicht zu Ende durchsucht, deshalb läuft die Schleife weiter
						directionCheck = true;

						/******************************/
						efficiency++;
						/********************************/

					} else { //Koordinate ist kein Schiffsteil

						//Prüfen ob Nord- oder Südrichtung durchlaufen wird
						if ( (yDirection == -1  && xDirection == 0) || (yDirection == 1  && xDirection == 0) ){

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

					//Prüfen ob Nord- oder Südrichtung durchlaufen wird
					if ( (yDirection == -1  && xDirection == 0) || (yDirection == 1  && xDirection == 0) ){

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


		/***************************/
		//System.out.println("+ Effizienz Richtung (" + yDirection + "|"+ xDirection +"+) :" + efficiency );
		/****************************/

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
     * Extrahiert aus einem String
     * die Y-Koordinate für weitere
     * Berechnungen
     *
     * @param stringCoord Koordinate als Stringkette
     *
     * @return Y-Koordinate als integer Wert
     *
     * (c) Credits an Florian Osterberg
     */
    public int extractYCoord (String stringCoord){

        return Integer.parseInt(stringCoord.split(",")[0]);


    }

    /**
     * Extrahiert aus einem String
     * die X-Koordinate für weitere
     * Berechnungen
     *
     * @param stringCoord Koordinate als Stringkette
     *
     * @return X-Koordinate als integer Wert
     *
     * (c) Credits an Florian Osterberg
     */
    public int extractXCoord (String stringCoord){

        return Integer.parseInt(stringCoord.split(",")[1]);

    }





}//end class Computer