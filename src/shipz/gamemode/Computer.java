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

	/** Die zuletzt getroffenen X und Y-Koordinaten der KI werden zur<br>
	 * Weiterverwendung in einem zwei-feld großem Array gespeichert*/
	private int[] lastFieldHit;

	/** Gibt aus ob bei der Prüfung der direkten Nachbarkoordinaten eines Treffers alle<br>
	 * Nachbarn auf weitere mögliche Treffer geprüft wurden
	 */
	private boolean allNeigboursChecked = false;


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
	protected byte[][] mirrorField;

	/**
	 *  Array zum überprüfen aller Richtungen um eine getroffene Koordinate. <br><br>
	 *
	 *  Reihenfolge der Indices: Norden, Osten, Süden, Westen.<br><br>
	 *
	 *  Je nach Zustand werden die Werte in den entsprechenden Indices geändert.<br><br>
	 *
	 *  ZUSTÄNDE:<br>
	 *  0: Richung wurde noch nicht geprüft.<br>
	 *  -1: Richtung wurde geprüft und beeinhaltet keine beschiessbaren Koordinaten (mehr)<br>
	 *  1: Richtung wurde geprüft und beeinhaltet höchstwarscheinlich weitere beschiessbare Koordinaten<br><br>
	 *
	 *  Sobald alle Richtungen geprüft und auf -1 gesetzt wurden oder sobald eine Richtung von 1 auf -1 wechselt<br>
	 * 	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-> Alle Werte werden durch eine Methode auf 0 resettet für den nächsten Treffer
	 *
	 *
	 */
	private int[] allDirectionsChecked = new int[] {0, 0, 0, 0};

	/** Random-Object zur Generierung von zufälligen Integer Zahlen */
	protected Random random = new Random();


	//Contructor
	/**
	 *
	 * @param newFieldSize Die Feldgröße des aktuellen Spiels. Die zu erstellenden Zufallskoordinaten werden von 1 bis fieldSize generiert.
	 */
	public Computer(int newFieldSize){

		fieldSize = newFieldSize;
		initiateMirrorField(fieldSize);
		lastFieldHit = new int[2];

	}


	//IM

	/**
	 * Implementierung der shootField-Methode von Player für die KI
	 * mit Body
	 *
	 * Methode der KI zum erstellen der Zufallskoordinaten für einen<br>
	 * Beschuss des gegnerischen Spielfeldes<br>
	 *
	 * @return Die Koordinaten die abgeschossen werden sollen.
	 */
	public abstract String shootField(TempKiGame game);


	/**
	 * Implementierung der shootField-Methode von
	 * Player ohne Inhalt.
	 */
	public String shootField() { return null; }


	/**
	 * Zur Erstellung von Zufallskoordinaten: public int nextInt(int n)
	 */


	/**
	 * Die direkten Nachbaarkoordinaten einer getroffenen Koordinate werden
	 * zur Überprüfung ausgewählt
	 *
	 * @return Eine Nachbarkoordinate des zuletzt getroffenen Feldes. Wird durch die Methode searchAllDirections erstellt.
	 */
	protected int[] selectNeighbourCoordinates(){


		return ( searchAllDirections( 0, 0) );
	}


	/**
	 * Es wird von einer Koordinate aus in Nord-, Süd-, West- oder Ost-Richtung
	 * gesucht
	 *
	 * @param xAxis Richtungskoordinate für die Bewegung innerhalb der X-Achse
	 * @param yAxis Richtungskoordinate für die Bewegung innerhalb der Y-Achse
	 *
	 * @return Die nächstliegende Koordinate in einer Richtung
	 */
	protected int[ ] searchAllDirections ( int xAxis, int yAxis){

		return null;
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
	protected void initiateMirrorField (int fieldSize){

		this.mirrorField = new byte[fieldSize][fieldSize];

		for(int i= 0; i < mirrorField.length; i++ ){

			for ( int j = 0; j < mirrorField[i].length; j++ ){

				mirrorField[i][j] = 0;
			}
		}
	}


	/**
	 * Koordinaten im Spiegelfeld auf den unbeschiessbaren
	 * Zustand ( Wert 1) setzen für das MirrorField
	 *
	 * @param yCoord Y-Koordinate der Zelle
	 * @param xCoord X-Koordinate der Zelle
	 *
	 */
	protected void setCoordinateOccupied(int yCoord, int xCoord) {

		this.mirrorField[yCoord][xCoord] = 1;


	}

	/**
	 * Koordinate im Spielfeld auf den Zustand eines
	 * getroffenen Schiffsteils (Wert 2)
	 * setzen für das MirrorField
	 *
	 * @param yCoord Y-Koordinate der Zelle
	 * @param xCoord X-Koordinate der Zelle
	 *
	 */
	protected void setCoordinateShipPart(int yCoord, int xCoord) {

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
				} else {

					System.out.print(" " + "O");
				}
			}
			//Zeilenumbruch
			System.out.println();
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
	 * @param game Das Gameobjekt mit dem man prüft ob ein Koordinate zum Versenken geführt hat
	 * @param yDirection Durchsuchung in Y-Richtung
	 * @param xDirection Durchsuchung in X-Richtung
	 */
	private void saveOneShipVicinityDirection (int yCoord, int xCoord, TempKiGame game, int yDirection, int xDirection ){

		/***************************/
		int efficiency = 0;
		/****************************/


		//Temporäre Koordinaten. Haben zu Beginn die Koordinaten des Treffers, welches ein
		//Schiff zum Versenken gebracht haben. Von da aus werden alle Richtungen geprüft
		int currentY = yCoord;
		int currentX = xCoord;

		//Flag-Variable zum Durchlaufen der While-Schleifen. Prüfen immer ob die Richtung weitergeprüft werden soll.
		boolean directionCheck;


		//Richtung durchlaufen
		do{

			//Es wird pro Schleifendurchgang eine Koordinate in die Nordrichtung weitergegangen
			currentY = currentY + yDirection;
			currentX = currentX + yDirection;

			directionCheck = false;


			//Prüfen ob die nördliche Koordinate überhaupt im Spielfeld liegt
			if (isCoordinateInField(currentY, currentX) ){

				//Prüfen ob die nördliche Variable schonmal beschossen wurde
				if ( !isCoordinateOccupied (currentY, currentX )){

					//Schlussendeliche Überprüfung ob die Koordinate ein Teil des Schiffes ist
					if( game.checkTile(currentX, currentY) == 1){

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
	protected void saveShipVicinity(int yCoord, int xCoord, TempKiGame game){


		//Nördliche Richtung untersuchen
		saveOneShipVicinityDirection (yCoord, xCoord, game, -1 , 0);

		//Südliche Richtung untersuchen
		saveOneShipVicinityDirection (yCoord, xCoord, game, 1 , 0);

		//Westliche Richtung untersuchen
		saveOneShipVicinityDirection (yCoord, xCoord, game, 0 , -1);

		//Östliche Richtung untersuchen
		saveOneShipVicinityDirection (yCoord, xCoord, game, 0 , 1);
	}

}//end class Computer