package shipz.gamemode;

/**
 * Einfacher Schwierigkeitsgrad<br><br>
 * 
 * Erstellte Koordinaten werden an die Verwaltung �bergeben.<br> 
 * KI speichert getroffene Felder, beschie�t jede Runde eine
 * neue Koordinate und ber�cksichtigt keine Treffer.<br>
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
	 * Ein neues Easy-Objekt enth�lt eine leere Abschussliste.<br>
	 * Alle Informationen bez�glich der schon beschossenen Feldern
	 * und Treffern sind standardm��ig auf null.<br>
	 * 
	 * @param newFieldSize Die Feldgr��e des aktuellen Spiels. Die zu erstellenden Zufallskoordinaten werden von 1 bis fieldSize generiert.
	 */
	public Easy (int newFieldSize) {
		
		super(newFieldSize);
	}
	
	
	/**
	 * Erstellt zuf�llige Koordinaten auf die geschossen werden
	 * sollen.<br><br>
	 * 
	 * Implementierung der Methode aus der Abstrakten Superklasse Computer.<br>
	 * Koordinaten werden jede Runde neu zuf�llig erstellt und �bergeben. Bei dem einfachen 
	 * Schwierigkeitsgrad wird die zuletzt getroffene Koordinate nicht 
	 * ber�cksichtigt, einzig die Liste mit den schon beschossenen Feldern
	 * und der Umgebung eines zerst�rten Schiffes.<br>
	 * 
	 * @return Zuf�llige Koordinaten die beschossen werden. Es werden pro Spiel nie dieselben Koordinaten von 
	 * der Ki zur�ckgegeben
	 */
	public int[] shootField(TempKiGame game) {
		
		//Zwischenspeichern der generierten X- und Y-Koordinaten
		int yCoord = 0;
		int xCoord = 0;
		
		
		
		//Zufallskoordinaten solange erstellen und speichern bis sie g�ltig sind und beschie�bar sind
		
		//Flag welches den Durchlauf der Schleife bestimmt
		boolean loopAgain = true;
		
		while(loopAgain){
			
			yCoord = super.random.nextInt(super.fieldSize);
			xCoord = super.random.nextInt(super.fieldSize);
			
			
			if (isCoordinateOccupied (yCoord, xCoord)  == false){
				
				loopAgain = false;
			}
			
		}
		
		
		//Koordinaten sind jetzt g�ltig und werden beschossen. Zuvor werden sie schon in das Spiegelfeld mit�bernommen.
		fillMirrorField(yCoord, xCoord);
		
		//Beschossene Koordinaten �berpr�fen, ob sie zum Sinken eines Schiffes gef�hrt haben.
		//Wurde durch den Beschuss ein Schiff versenkt, wird die direkte Umgebung in das Spiegelfeld gespeichert,
		//da diese keine Schiffsteile enthalten
		
		
		if ( game.checkTile (xCoord, yCoord) == 2 ){
			
			saveShipVicinity(yCoord, xCoord, game);
		}
		
		
		return new int[]{yCoord, xCoord};
	}
	
	


	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

}//end class Easy
