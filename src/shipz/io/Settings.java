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
		initFile();
	}
	
	/**
	 * Erstellt die Struktur des config-Files mit den default-Werten.
	 */
	private void initFile() {
		if(!configFile.exists()) {
			saveload.makeDirectory(configFile);
			saveload.writeFile( // Standardwerte
					configFile, 
					"highscoreMax" + CONFIG_SEPARATOR + "10\n"+
					"aiTimer" + CONFIG_SEPARATOR + "250\n");
		}
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
		return Integer.parseInt(saveload.searchLine(configFile, "highscoreMax" + CONFIG_SEPARATOR).split(CONFIG_SEPARATOR)[1].replaceAll("\n", ""));
	}
	
	/**
	 * Ändert den Wert der Pausen zwischen den KI-Zügen
	 * in der Config.
	 * @param timer neuer Wert für Pausen zwischen KI-Zügen
	 */
	protected void setAiTimer(int ms) {
		saveload.writeFile(configFile, saveload.readFile(configFile).replaceAll("aiTimer"+CONFIG_SEPARATOR+getAiTimer(), "aiTimer"+CONFIG_SEPARATOR+ms));
	}
	
	/**
	 * Liest aus der Datei und gibt den Wert zurück,
	 * der die Länge der Pausen zwischen den KI-Zügen
	 * in Millisekunden speichert.
	 * @return Pause zwischen KI-Zügen in ms
	 */
	protected int getAiTimer() {
		return Integer.parseInt(saveload.searchLine(configFile, "aiTimer" + CONFIG_SEPARATOR).split(CONFIG_SEPARATOR)[1].replaceAll("\n", ""));
	}
	
	public static void main(String[] args) {
		Settings s = new Settings();
		s.setHighscoreMaximum(10);
		System.out.println(s.getHighscoreMaximum());
	}
	
}
