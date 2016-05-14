package shipz.gamemode;

/**
 * Normaler Schwierigkeitsgrad<br><br>
 *
 * Erstellte Koordinaten werden an die Verwaltung übergeben. <br>
 * KI berücksichtigt zuletzt getroffene Felder und prüft
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
	 * Ein neues Normal-Objekt enthält eine leere Abschussliste.<br>
	 * Alle Informationen bezüglich der schon beschossenen Feldern,
	 * der geprüften Richtungen und Treffern sind auf den Standardwert gesetzt.<br>
	 *
	 * @param newfieldSize Die Feldgröße des aktuellen Spiels. Die zu erstellenden Zufallskoordinaten werden von 1 bis fieldSize generiert.
	 */
	public Normal (int newFieldSize) {

		super(newFieldSize);
	}

	//IM
	/**
	 * Erstellt zufällige Koordinaten auf die geschossen werden
	 * sollen.<br><br>
	 *
	 * Implementierung der Methode aus der Abstrakten Superklasse Computer.<br>
	 * Koordinaten werden jede Runde neu zufällig erstellt und übergeben. Bei dem normalen
	 * Schwierigkeitsgrad wird die zuletzt getroffene Koordinate berücksichtigt
	 * und die direkte Umgebung wird nach weiteren möglichen Treffern untersucht.<br><br>
	 *
	 * Sobald ein Richtungsmuster erkennbar ist, wird in die jeweilige Richtung
	 * geschossen bis ein Schiff zerstört wird oder ins Wasser getroffen wurde.<br><br>
	 *
	 * Auch hier werden alle beschossenen Koordinaten und die Umgebung eines Schiffes
	 * berücksichtigt bei der Erstellung der Zufallskoordinaten.<br><br>
	 *
	 * @return Zufällige Koordinaten die beschossen werden. Es werden pro Spiel nie dieselben Koordinaten von
	 * der Ki zurückgegeben
	 */
	public String shootField(TempKiGame game) {

		return null;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

}//end class Normal