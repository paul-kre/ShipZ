package shipz.gamemode;

/**
 * Schwerer Schwierigkeitsgrad<br><br>
 * 
 * Erstellte Koordinaten werden an die Verwaltung �bergeben. 
 * KI ber�cksichtigt zuletzt getroffene Felder und pr�ft
 * dementsprechend das herumliegende Gebiet.<br><br>
 * 
 * Zus�tlich werden Koordinaten die alleine stehen bzw. keine Nachbarkoordinaten
 * haben nicht ber�cksichtigt. <br><br>
 * 
 * F�r die Generierung der Koordinaten scannt das Feld ab und w�hlt dementsprechend
 * eine Taktik.<br>
 * 
 * @author Artur Hergert
 *
 */
public class Hard extends Computer {

	//Constructor
	/**
	 * Constructor zur Initialisierung des
	 * schweren Schwierigkeitsgrades. <br><br>
	 * 
	 * Ein neues Hard-Objekt enth�lt eine leere Abschussliste.
	 * Alle Informationen bez�glich der schon beschossenen Feldern,
	 * der gepr�ften Richtungen, der erkundeten Bereiche, verwendeteten Taktiken
	 * und Treffern sind auf den Standardwert gesetzt.<br>
	 * 
	 * @param newFieldSize Die Feldgr��e des aktuellen Spiels. Die zu erstellenden Zufallskoordinaten werden von 1 bis fieldSize generiert.
	 */
	public Hard (int newFieldSize) {
		
		super(newFieldSize);
	}

	
	
	//IM
	
	
	/**
	 * Erstellt zuf�llige Koordinaten auf die geschossen werden
	 * sollen.<br><br>
	 * 
	 * Implementierung der Methode aus der Abstrakten Superklasse Computer.<br>
	 * Koordinaten werden jede Runde neu nach einem Muster erstellt und �bergeben.<br><br>
	 *  
	 * Bei dem schweren Schwierigkeitsgrad wird die zuletzt getroffene Koordinate 
	 * ber�cksichtigt und die direkte Umgebung wird nach weiteren m�glichen 
	 * Treffern untersucht.<br><br>
	 * 
	 * Ebenso wird das Spielfeld in verschieden Bereiche unterteilt die je nach 
	 * Warscheinlichkeit eines Treffers eher oder zuletzt ber�cksichtigt werden.<br><br>
	 * 
	 * Sobald ein Richtungsmuster erkennbar ist, wird in die jeweilige Richtung
	 * geschossen bis ein Schiff zerst�rt wird oder ins Wasser getroffen wurde.<br><br>
	 * 
	 * Auch hier werden alle beschossenen Koordinaten und die Umgebung eines Schiffes
	 * ber�cksichtigt bei der Erstellung der Zufallskoordinaten.<br>
	 * 
	 * @return Nach einem Muster erstellte Zufallskoordinaten. Es werden pro Spiel nie diesselben Koordinaten von 
	 * der Ki zur�ckgegeben
	 */
	public int[] shootField(TempKiGame game) {
		
		return maxHitProbabilityCoordinates ();
	}
	
	/**
	 * Untersucht die Liste der schon beschossenen Koordinaten und generiert nach einem 
	 * Muster die zu beschiessenden Koordinaten die die h�chste Trefferwahrscheinlichkeit 
	 * besitzt.<br>
	 * 
	 * @return Zu beschiessende Koordinaten mit der h�chsten Trefferwarscheinlichkeit zum Zeitpunkt
	 * des Spielfeldes
	 * 
	 */
	private int[] maxHitProbabilityCoordinates () {
		
		return null;
	}



	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
}// end class Hard
