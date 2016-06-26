package shipz.io;

import shipz.util.NoDrawException;

/**
 * Diese Klasse verwaltet die anderen drei Klassen.
 * Es werden alle wichtigen Methoden, die die Verwaltung-Klasse des Spiels {@link shipz.Game}
 * zum Speichern, Laden, Undo/ Redo und f�r die Punkte- und Highscore-Verwaltung
 * ben�tigt, redefiniert und bilden somit eine Referenz zu den anderen Klassen.
 * @author Florian Osterberg
 */
public class FileStream {

	// IV
	/** Referenz auf die Score-Klasse */
	private Score score;
	/** Referenz auf die Undoredo-Klasse */
	private UndoRedo undoredo;
	/** Referenz auf die Speichern-Laden-Klasse */
	private SaveLoad saveload;
	/** Referenz auf die Settings-Klasse, die die Einstellungen verwaltet. */
	private Settings settings;
	
	// TODO
	// SaveLoad Code s�ubern
	// Kommentare �berarbeiten
	
	// Konstruktor
	/**
	 * Konstruktor der Klasse.
	 */
	public FileStream() {
		score = new Score();
		undoredo = new UndoRedo();
		saveload = new SaveLoad();
		settings = new Settings();
	}
	
	/*
	 * SAVE-LOAD
	 * CLASS
	 */
	
	// IM
	/**
	 * Referenz auf die Methode in der SaveLoad-Klasse.
	 * Speichert die Informationen eines bestimmten Spiels.
	 * Wird von {@link shipz.Game} genutzt, um ein Spiel abzuspeichern.
	 * @param gameName Name des Spielstands
	 * @param playerName Name des ersten Spielers
	 * @param opponentName Name des Gegners / zweiten Spielers
	 * @param boardPlayerOne Spielbrett des ersten Spielers als {@link String}
	 * @param boardPlayerTwo Spielbrett des zweiten Spielers als {@link String}
	 * @param boardsize Gr��e des Spielfelds
	 * @param activePlayer aktiver Spieler
	 * @param preferences die Einstellungen des Spiels
	 */
	public void saveGame(String gameName, String playerName, String opponentName, String boardPlayerOne, String boardPlayerTwo, int boardsize, int activePlayer, String preferences) {
		saveload.saveGame(gameName, playerName, opponentName, boardPlayerOne, boardPlayerTwo, boardsize, activePlayer, preferences);
		undoredo.saveToFile(gameName);
		score.saveScoreToFile(playerName, opponentName);
	}
	
	/**
	 * Referenz auf die Methode in der SaveLoad-Klasse.
	 * Gibt die Namen aller Spielst�nde als {@link String} zur�ck.
	 * Dies wird f�r die Auflistung aller Spielst�nde wichtig sein.
	 * Da die Weitergabe von Arrays nicht erlaubt ist,
	 * muss die Game-Klasse selbst aus dem String ein Array machen.
	 * Dies geht ganz einfach mit 
	 * <i>getAllGameNames().split(",")</i>
	 * @return Die Namen aller Spielst�nde als {@link String}
	 */
	public String getAllGameNames() {
		return saveload.getAllGameNames();
	}
	
	/**
	 * Referenz auf die Methode in der SaveLoad-Klasse.
	 * Wird zum Laden eines Spielstands ben�tigt.
	 * Diese Methode l�dt aus dem gespeicherten Spielstand das gespeicherte Spielfeld des ersten Spielers heraus.
	 * Dieses Spielfeld wird dann als {@link String} zur�ckgegeben.
	 * @param gameName Dateiname zur Identifizierung des gespeicherten Spielstands.
	 * @return Das geladene Spielfeld
	 */
	public String getBoardPlayerOne(String gameName) {
		return saveload.getBoardPlayerOne(gameName);
	}
	
	/**
	 * Referenz auf die Methode in der SaveLoad-Klasse.
	 * Wird zum Laden eines Spielstands ben�tigt.
	 * Diese Methode l�dt aus dem gespeicherten Spielstand das gespeicherte Spielfeld des zweiten Spielers heraus.
	 * Dieses Spielfeld wird dann als {@link String} zur�ckgegeben.
	 * @param gameName Dateiname zur Identifizierung des gespeicherten Spielstands.
	 * @return Das geladene Spielfeld
	 */
	public String getBoardPlayerTwo(String gameName) {
		return saveload.getBoardPlayerTwo(gameName);
	}
	
	/**
	 * Referenz auf die Methode in der SaveLoad-Klasse.
	 * Wird zum Laden eines Spielstands ben�tigt.
	 * Gibt den Spielernamen eines Spielstands zur�ck.
	 * @param gameName der gew�nschte Spielstand
	 * @return Spielername eines Spielstand
	 */
	public String getPlayerName(String gameName) {
		return saveload.getPlayerName(gameName);
	}
	
	/**
	 * Referenz auf die Methode in der SaveLoad-Klasse.
	 * Wird zum Laden eines Spielstands ben�tigt.
	 * Gibt den Namen des Gegners eines bestimmten Spielstands zur�ck.
	 * @param gameName der gew�nschte Spielstand
	 * @return Namen des Gegners eines bestimmten Spielstands
	 */
	public String getOpponentName(String gameName) {
		return saveload.getOpponentName(gameName);
	}
	
	/**
	 * Referenz auf die Methode in der SaveLoad-Klasse.
	 * Wird zum Laden eines Spielstands ben�tigt.
	 * Gibt die Feldgr��e eines bestimmten Spielstands zur�ck.
	 * @param gameName der gew�nschte Spielstand
	 * @return Feldgr��e eines bestimmten Spielstands als {@link String}
	 */
	public int getBoardsize(String gameName) {
		return saveload.getBoardsize(gameName);
	}
	
	/**
	 * Referenz auf die Methode in der SaveLoad-Klasse.
	 * Wird zum Laden eines Spielstands ben�tigt.
	 * Liest den aktuellen Spieler eines Spiels aus der Datei aus und gibt ihn zur�ck.
	 * @param gameName Name des Spielstands
	 * @return der aktive Spieler
	 */
	public int getActivePlayer(String gameName) {
		return saveload.getActivePlayer(gameName);
	}
	
	/**
	 * Referenz auf die Methode in der SaveLoad-Klasse.
	 * Wird zum Laden eines Spielstands ben�tigt.
	 * Gibt die gespeicherte Uhrzeit eines Spielstands zur�ck.
	 * @param gameName der gew�nschte Spielstand
	 * @return die gespeicherte Uhrzeit als {@link String}
	 */
	public String getTime(String gameName) {
		return saveload.getTime(gameName);
	}
	
	/**
	 * Referenz auf die Methode in der SaveLoad-Klasse.
	 * Wird zum Laden eines Spielstands ben�tigt.
	 * Gibt die Einstellungen eines Spielstands zur�ck.
	 * @param gameName Name des Spielstands
	 * @return Einstellungen als String
	 */
	public String getPreferences(String gameName) {
		return saveload.getPreferences(gameName);
	}
	
	/**
	 * Referenz auf die Methode in der SaveLoad-Klasse.
	 * L�scht einen bestimmten Spielstand aus der Datei.
	 * @param gameName Name des Spielstands
	 */
	public void deleteGame(String gameName) {
		saveload.deleteGame(gameName);
	}
	
	/*
	 * UNDO-REDO
	 * CLASS
	 */
	
	/**
	 * Referenz auf die Methode in der UndoRedo-Klasse.
	 * Die r�ckg�ngig zu machenden Z�ge werden vom Stack genommen, der die im Spiel get�tigten
	 * Z�ge speichert, und auf den Stack gelegt, der die r�ckg�ngig gemachten Z�ge speichert.
	 * Alle r�ckg�ngig gemachten Z�ge werden als String zur�ckgegeben.
	 * @param playerIndex 1 f�r den ersten Spieler, 2 f�r den zweiten Spieler
	 * @return Die Z�ge die r�ckg�ngig gemacht werden als {@link String}
	 * @throws NoDrawException tritt auf, falls keine weiteren Z�ge r�ckg�ngig gemacht werden k�nnen
	 */
	public String undoDraw(int playerIndex) throws NoDrawException {
		score.setScore(playerIndex, 3);
		return undoredo.undoDraw(playerIndex);
	}
	
	/**
	 * Referenz auf die Methode in der UndoRedo-Klasse.
	 * Die Z�ge, die wiederhergestellt werden, werdem vom Redo-Stack genommen und wieder oben
	 * auf den Stack gelegt, der die Z�ge des Spiels gespeichert, gelegt.
	 * Alle wiederhergestellten Z�ge werden als String zur�ckgegeben.
	 * @param playerIndex 1 f�r den ersten Spieler, 2 f�r den zweiten Spieler
	 * @return Die wiederhergestellten Z�ge als {@link String}
	 * @throws NoDrawException tritt auf, falls keine weiteren Z�ge wiederholt werden k�nnen
	 */
	public String redoDraw(int playerIndex) throws NoDrawException {
		return undoredo.redoDraw(playerIndex);
	}
	
	/**
	 * Referenz auf die Methode in der UndoRedo-Klasse.
	 * Wenn ein neuer Zug get�tigt wird, wird dieser in den String, der den Spielverlauf speichert, geschrieben.
	 * Au�erdem wird anhand der Variable <b>result</b> die Punktzahl des Spielers aktualisiert.
	 * @param x x-Koordinate des Zugs
	 * @param y y-Koordinate des Zugs
	 * @param playerIndex 1 f�r den ersten Spieler, 2 f�r den zweiten Spieler
	 * @param result <b>0</b> = kein Treffer, <b>1</b> = Treffer, <b>2</b> = Schiff versenkt, <b>3</b> = Zug r�ckg�ngig gemacht
	 */
	public void newDraw(int x, int y, int playerIndex, int result) {
		undoredo.newDraw(x, y, playerIndex, result);
		score.setScore(playerIndex, result);
	}
	
	/**
	 * Referenz auf die Methode in der UndoRedo-Klasse.
	 * Leert alle Stacks und l�dt die Z�ge aus einer Datei in die Instanz-Variablen.
	 * Au�erdem werden die Instanz-Variablen, die die Punkte- und Combo-Werte speichern,
	 * in der Score-Klasse aktualisiert.
	 * Wird verwendet, wenn ein Spielstand geladen wird.
	 * @param gameName Name des Spielstands
	 */
	public void loadDraws(String gameName) {
		undoredo.loadDraws(gameName);
		score.loadScore(gameName);
	}
	
	/*
	 * SCORE
	 * CLASS
	 */
	
	/**
	 * Referenz auf die Methode in der Score-Klasse.
	 * Gibt die Punktzahl eines Spielers zur�ck.
	 * @param playerIndex 1 = Spieler1, 2 = Spieler2
	 * @return Punktzahl des Spielers
	 */
	public int getScore(int playerIndex) {
		return score.getScore(playerIndex);
	}
	
	/**
	 * Referenz auf die Methode in der Score-Klasse.
	 * Liest alle Punktzahlen aus dem Highscore-File
	 * und erstellt eine absteigend sortierte Highscore-Liste.
	 * Es werden maximal die zehn besten Spieler angezeigt.
	 * @return Die Highscore-Liste als String
	 */
	public String highscore() {
		return score.highscore();
	}
	
	/**
	 * Referenz auf die Methode in der Score-Klasse.
	 * Der Wert der Combo wird zur�ckgegeben, damit die GUI darstellen kann, 
	 * welche Combo der Spieler aktuell erreicht hat.
	 * @param playerIndex 1 = Spieler1, 2 = Spieler2
	 * @return aktueller Combowert des Spielers
	 */
	public int getComboValue(int playerIndex) {
		return score.getComboValue(playerIndex);
	}
	
	/*
	 * SETTINGS
	 * CLASS
	 */
	
	/**
	 * Referenz auf die Methode in der Settings-Klasse.
	 * Liest den Wert aus der Config und gibt zur�ck, wie viele Spieler
	 * maximal in der Highscore-Liste angezeigt werden sollen.
	 * @return Spieler-Maximum im Highscore
	 */
	public int getHighscoreMaximum() {
		return settings.getHighscoreMaximum();
	}
	
	/**
	 * Referenz auf die Methode in der Settings-Klasse.
	 * �ndert den Wert, wie viele Spieler maximal im Highscore angezeigt werden.
	 * @param max Spieler-Maximum im Highscore
	 */
	public void setHighscoreMaximum(int max) {
		settings.setHighscoreMaximum(max);
	}
	
	/*
	 * MISCELLANEOUS
	 */
	
	/**
	 * Gibt alle Buchstaben und Sonderzeichen zur�ck,
	 * die in einem Namen nicht verwendet werden d�rfen.
	 * Um diesen String in ein Char-Array umzuwandeln:
	 * <i>forbiddenCharacters().toCharArray()</i>
	 * @return die verbotenen Zeichen als String
	 */
	public String forbiddenCharacters() {
		return "=,{}<>;#";
	}
	
	/**
	 * Main-Methode zum Testen
	 * @param args
	 */
	public static void main(String[] args) {
		FileStream fs = new FileStream();
		fs.newDraw(0, 0, 1, 1);
		fs.newDraw(0, 0, 1, 1);
		fs.newDraw(0, 0, 1, 2);
		fs.newDraw(0, 0, 1, 0);
		fs.newDraw(0, 0, 2, 1);
		System.out.println(fs.getScore(1));
		System.out.println(fs.getComboValue(1));
		fs.saveGame("testSpielxx", "Dieter", "Heinz", "WWWWWWW", "WWFFWWWWW", 8, 1, "a");
		
	}
	
}
