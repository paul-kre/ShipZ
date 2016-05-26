package shipz.gamemode;

/**
 * Schwerer Schwierigkeitsgrad<br><br>
 *
 * Erstellte Koordinaten werden an die Verwaltung übergeben.
 * KI berücksichtigt zuletzt getroffene Felder und prüft
 * dementsprechend das herumliegende Gebiet.<br><br>
 *
 * Zusätlich werden Koordinaten die alleine stehen bzw. keine Nachbarkoordinaten
 * haben nicht berücksichtigt. <br><br>
 *
 * Für die Generierung der Koordinaten scannt das Feld ab und wählt dementsprechend
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
	 * Ein neues Hard-Objekt enthält eine leere Abschussliste.
	 * Alle Informationen bezüglich der schon beschossenen Feldern,
	 * der geprüften Richtungen, der erkundeten Bereiche, verwendeteten Taktiken
	 * und Treffern sind auf den Standardwert gesetzt.<br>
	 *
	 * @param newFieldSize Die Feldgröße des aktuellen Spiels. Die zu erstellenden Zufallskoordinaten werden von 1 bis fieldSize generiert.
	 */
	public Hard (int newFieldSize) {

		super(newFieldSize);
	}



	//IM


	/**
	 * Erstellt zufällige Koordinaten auf die geschossen werden
	 * sollen.<br><br>
	 *
	 * Implementierung der Methode aus der Abstrakten Superklasse Computer.<br>
	 * Koordinaten werden jede Runde neu nach einem Muster erstellt und übergeben.<br><br>
	 *
	 * Bei dem schweren Schwierigkeitsgrad wird die zuletzt getroffene Koordinate
	 * berücksichtigt und die direkte Umgebung wird nach weiteren möglichen
	 * Treffern untersucht.<br><br>
	 *
	 * Ebenso wird das Spielfeld in verschieden Bereiche unterteilt die je nach
	 * Warscheinlichkeit eines Treffers eher oder zuletzt berücksichtigt werden.<br><br>
	 *
	 * Sobald ein Richtungsmuster erkennbar ist, wird in die jeweilige Richtung
	 * geschossen bis ein Schiff zerstört wird oder ins Wasser getroffen wurde.<br><br>
	 *
	 * Auch hier werden alle beschossenen Koordinaten und die Umgebung eines Schiffes
	 * berücksichtigt bei der Erstellung der Zufallskoordinaten.<br>
	 *
	 */
	public void generateAICoordinates() {

        maxHitProbabilityCoordinates ();
	}

	/**
	 * Untersucht die Liste der schon beschossenen Koordinaten und generiert nach einem
	 * Muster die zu beschiessenden Koordinaten die die höchste Trefferwahrscheinlichkeit
	 * besitzt.<br>
	 *
	 * @return Zu beschiessende Koordinaten mit der höchsten Trefferwarscheinlichkeit zum Zeitpunkt
	 * des Spielfeldes
	 *
	 */
	private String maxHitProbabilityCoordinates () {

		return null;
	}



	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

}// end class Hard