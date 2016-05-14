package shipz.gamemode;

import shipz.Player;

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
	 * Schwierigkeitsgrad wird die zuletzt getroffene Koordinate nicht
	 * berücksichtigt, einzig die Liste mit den schon beschossenen Feldern
	 * und der Umgebung eines zerstörten Schiffes.<br>
	 *
	 * @return Zufällige Koordinaten die beschossen werden. Es werden pro Spiel nie dieselben Koordinaten von
	 * der Ki zurückgegeben
	 */
	public String shootField(TempKiGame game) {
		/****************************************/
		efficiency = 0;
		/*****************************************************/


		//Zwischenspeichern der generierten X- und Y-Koordinaten
		int yCoord = 0;
		int xCoord = 0;



		//Zufallskoordinaten solange erstellen und speichern bis sie gültig sind und beschießbar sind

		//Flag welches den Durchlauf der Schleife bestimmt
		boolean loopAgain = true;

		do{

			yCoord = super.random.nextInt(super.fieldSize);
			xCoord = super.random.nextInt(super.fieldSize);


			if (isCoordinateOccupied (yCoord, xCoord)  == false){

				loopAgain = false;
				/*****************************************/
				efficiency--;
				/*****************************************/

			}
			/*****************************************/
			efficiency++;
			/*****************************************/
		}while (loopAgain);


		//Koordinaten sind jetzt gültig und werden beschossen. Zuvor werden sie schon in das Spiegelfeld mitübernommen.
		if (game.checkTile (xCoord, yCoord) == 2);

		//Beschossene Koordinaten überprüfen, ob sie zum Sinken eines Schiffes geführt haben.
		//Wurde durch den Beschuss ein Schiff versenkt, wird die direkte Umgebung in das Spiegelfeld gespeichert,
		//da diese keine Schiffsteile enthalten


		if ( game.checkTile (xCoord, yCoord) == 2 ){

			saveShipVicinity(yCoord, xCoord, game);
		}

		/****************************************/
		efficiency++;
		System.out.println("Durchläufe: " + efficiency);
		/*****************************************************/
		return "" + yCoord + xCoord;
	}




	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

}//end class Easy