package shipz.gamemode;

/**
 * Normaler Schwierigkeitsgrad<br><br>
 * 
 * Erstellte Koordinaten werden an die Verwaltung �bergeben. <br>
 * KI ber�cksichtigt zuletzt getroffene Felder und pr�ft
 * dementsprechend das herumliegende Gebiet.<br>
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
		 * Ein neues Normal-Objekt enth�lt eine leere Abschussliste.<br>
		 * Alle Informationen bez�glich der schon beschossenen Feldern,
		 * der gepr�ften Richtungen und Treffern sind auf den Standardwert gesetzt.<br>
		 * 
		 * @param newfieldSize Die Feldgr��e des aktuellen Spiels. Die zu erstellenden Zufallskoordinaten werden von 1 bis fieldSize generiert.
		 */
		public Normal (int newFieldSize) {
			
			super(newFieldSize);
		}
	
	//IM
	/**
	 * Erstellt zuf�llige Koordinaten auf die geschossen werden
	 * sollen.<br><br>
	 * 
	 * Implementierung der Methode aus der Abstrakten Superklasse Computer.<br>
	 * Koordinaten werden jede Runde neu zuf�llig erstellt und �bergeben. Bei dem normalen
	 * Schwierigkeitsgrad wird die zuletzt getroffene Koordinate ber�cksichtigt
	 * und die direkte Umgebung wird nach weiteren m�glichen Treffern untersucht.<br><br>
	 * 
	 * Sobald ein Richtungsmuster erkennbar ist, wird in die jeweilige Richtung
	 * geschossen bis ein Schiff zerst�rt wird oder ins Wasser getroffen wurde.<br><br>
	 * 
	 * Auch hier werden alle beschossenen Koordinaten und die Umgebung eines Schiffes
	 * ber�cksichtigt bei der Erstellung der Zufallskoordinaten.<br><br>
	 * 
	 * @return Zuf�llige Koordinaten die beschossen werden. Es werden pro Spiel nie dieselben Koordinaten von 
	 * der Ki zur�ckgegeben
	 */
	public int[] shootField(TempKiGame game) {
		
		return null;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

}//end class Normal
