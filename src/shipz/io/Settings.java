package shipz.io;

import java.io.File;

/**
 * Klasse, die die Config-Datei verwaltet.
 * @author Florian Osterberg
 */
public class Settings {

	// IV
	/** Referenz auf die SaveLoad-Klasse. */
	private SaveLoad saveload;
	/** Die Config-Datei. */
	private File configFile;
	
	// Konstanten
	/** Trennzeichen zwischen einem bestimmten Parameter in der Config und dem dazugehörigen Wert. */
	private final static String CONFIG_SEPARATOR = "=";
	
	// Konstruktor
	/**
	 * Konstruktor der Settings-Klasse,
	 * der die Dateien erstellt und initialisiert.
	 */
	public Settings() {
		saveload = new SaveLoad();
		configFile = new File(saveload.fileDirectory() + File.separator + "config.shipz");
		saveload.makeDirectory(configFile);
		initFile();
	}
	
	/**
	 * Erstellt die Struktur des config-Files mit den default-Werten.
	 */
	private void initFile() {
		saveload.writeFile(configFile, "highscoreMax" + CONFIG_SEPARATOR + "10");
	}
	
	/**
	 * Ändert den Wert, wie viele Spieler maximal im Highscore angezeigt werden.
	 * @param max Wie viele Spieler maximal im Highscore angezeigt werden sollen
	 */
	protected void setHighscoreMaximum(int max) {
		saveload.writeFile(configFile, saveload.readFile(configFile).replaceAll("highscoreMax"+CONFIG_SEPARATOR+getHighscoreMaximum(), "highscoreMax"+CONFIG_SEPARATOR+max));
	}

	/**
	 * Gibt den Wert zurück, wie viele Spieler
	 * maximal in der Highscore-Liste angezeigt werden sollen.
	 * @return Spieler-Maximum der Highscore-Liste
	 */
	protected int getHighscoreMaximum() {
		return Integer.parseInt(saveload.searchLine(configFile, "highscoreMax" + CONFIG_SEPARATOR).split(CONFIG_SEPARATOR)[1]);
	}
	
	// Idee für eine Einstellung
		// Timer wie schnell die KI abläuft
	
	public static void main(String[] args) {
		Settings s = new Settings();
		s.setHighscoreMaximum(10);
		System.out.println(s.getHighscoreMaximum());
	}
	
}
