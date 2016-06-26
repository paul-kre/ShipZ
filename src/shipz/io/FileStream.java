package shipz.io;

import shipz.util.NoDrawException;

/**
 * Diese Klasse verwaltet die anderen drei Klassen.
 * Es werden alle wichtigen Methoden, die die Verwaltung-Klasse des Spiels {@link shipz.Game}
 * zum Speichern, Laden, Undo/ Redo und für die Punkte- und Highscore-Verwaltung
 * benötigt, redefiniert und bilden somit eine Referenz zu den anderen Klassen.
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
	// SaveLoad Code säubern
	// Kommentare überarbeiten
	
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
	 * @param boardsize Größe des Spielfelds
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
	 * Gibt die Namen aller Spielstände als {@link String} zurück.
	 * Dies wird für die Auflistung aller Spielstände wichtig sein.
	 * Da die Weitergabe von Arrays nicht erlaubt ist,
	 * muss die Game-Klasse selbst aus dem String ein Array machen.
	 * Dies geht ganz einfach mit 
	 * <i>getAllGameNames().split(",")</i>
	 * @return Die Namen aller Spielstände als {@link String}
	 */
	public String getAllGameNames() {
		return saveload.getAllGameNames();
	}
	
	/**
	 * Referenz auf die Methode in der SaveLoad-Klasse.
	 * Wird zum Laden eines Spielstands benötigt.
	 * Diese Methode lädt aus dem gespeicherten Spielstand das gespeicherte Spielfeld des ersten Spielers heraus.
	 * Dieses Spielfeld wird dann als {@link String} zurückgegeben.
	 * @param gameName Dateiname zur Identifizierung des gespeicherten Spielstands.
	 * @return Das geladene Spielfeld
	 */
	public String getBoardPlayerOne(String gameName) {
		return saveload.getBoardPlayerOne(gameName);
	}
	
	/**
	 * Referenz auf die Methode in der SaveLoad-Klasse.
	 * Wird zum Laden eines Spielstands benötigt.
	 * Diese Methode lädt aus dem gespeicherten Spielstand das gespeicherte Spielfeld des zweiten Spielers heraus.
	 * Dieses Spielfeld wird dann als {@link String} zurückgegeben.
	 * @param gameName Dateiname zur Identifizierung des gespeicherten Spielstands.
	 * @return Das geladene Spielfeld
	 */
	public String getBoardPlayerTwo(String gameName) {
		return saveload.getBoardPlayerTwo(gameName);
	}
	
	/**
	 * Referenz auf die Methode in der SaveLoad-Klasse.
	 * Wird zum Laden eines Spielstands benötigt.
	 * Gibt den Spielernamen eines Spielstands zurück.
	 * @param gameName der gewünschte Spielstand
	 * @return Spielername eines Spielstand
	 */
	public String getPlayerName(String gameName) {
		return saveload.getPlayerName(gameName);
	}
	
	/**
	 * Referenz auf die Methode in der SaveLoad-Klasse.
	 * Wird zum Laden eines Spielstands benötigt.
	 * Gibt den Namen des Gegners eines bestimmten Spielstands zurück.
	 * @param gameName der gewünschte Spielstand
	 * @return Namen des Gegners eines bestimmten Spielstands
	 */
	public String getOpponentName(String gameName) {
		return saveload.getOpponentName(gameName);
	}
	
	/**
	 * Referenz auf die Methode in der SaveLoad-Klasse.
	 * Wird zum Laden eines Spielstands benötigt.
	 * Gibt die Feldgröße eines bestimmten Spielstands zurück.
	 * @param gameName der gewünschte Spielstand
	 * @return Feldgröße eines bestimmten Spielstands als {@link String}
	 */
	public int getBoardsize(String gameName) {
		return saveload.getBoardsize(gameName);
	}
	
	/**
	 * Referenz auf die Methode in der SaveLoad-Klasse.
	 * Wird zum Laden eines Spielstands benötigt.
	 * Liest den aktuellen Spieler eines Spiels aus der Datei aus und gibt ihn zurück.
	 * @param gameName Name des Spielstands
	 * @return der aktive Spieler
	 */
	public int getActivePlayer(String gameName) {
		return saveload.getActivePlayer(gameName);
	}
	
	/**
	 * Referenz auf die Methode in der SaveLoad-Klasse.
	 * Wird zum Laden eines Spielstands benötigt.
	 * Gibt die gespeicherte Uhrzeit eines Spielstands zurück.
	 * @param gameName der gewünschte Spielstand
	 * @return die gespeicherte Uhrzeit als {@link String}
	 */
	public String getTime(String gameName) {
		return saveload.getTime(gameName);
	}
	
	/**
	 * Referenz auf die Methode in der SaveLoad-Klasse.
	 * Wird zum Laden eines Spielstands benötigt.
	 * Gibt die Einstellungen eines Spielstands zurück.
	 * @param gameName Name des Spielstands
	 * @return Einstellungen als String
	 */
	public String getPreferences(String gameName) {
		return saveload.getPreferences(gameName);
	}
	
	/**
	 * Referenz auf die Methode in der SaveLoad-Klasse.
	 * Löscht einen bestimmten Spielstand aus der Datei.
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
	 * Die rückgängig zu machenden Züge werden vom Stack genommen, der die im Spiel getätigten
	 * Züge speichert, und auf den Stack gelegt, der die rückgängig gemachten Züge speichert.
	 * Alle rückgängig gemachten Züge werden als String zurückgegeben.
	 * @param playerIndex 1 für den ersten Spieler, 2 für den zweiten Spieler
	 * @return Die Züge die rückgängig gemacht werden als {@link String}
	 * @throws NoDrawException tritt auf, falls keine weiteren Züge rückgängig gemacht werden können
	 */
	public String undoDraw(int playerIndex) throws NoDrawException {
		score.setScore(playerIndex, 3);
		return undoredo.undoDraw(playerIndex);
	}
	
	/**
	 * Referenz auf die Methode in der UndoRedo-Klasse.
	 * Die Züge, die wiederhergestellt werden, werdem vom Redo-Stack genommen und wieder oben
	 * auf den Stack gelegt, der die Züge des Spiels gespeichert, gelegt.
	 * Alle wiederhergestellten Züge werden als String zurückgegeben.
	 * @param playerIndex 1 für den ersten Spieler, 2 für den zweiten Spieler
	 * @return Die wiederhergestellten Züge als {@link String}
	 * @throws NoDrawException tritt auf, falls keine weiteren Züge wiederholt werden können
	 */
	public String redoDraw(int playerIndex) throws NoDrawException {
		return undoredo.redoDraw(playerIndex);
	}
	
	/**
	 * Referenz auf die Methode in der UndoRedo-Klasse.
	 * Wenn ein neuer Zug getätigt wird, wird dieser in den String, der den Spielverlauf speichert, geschrieben.
	 * Außerdem wird anhand der Variable <b>result</b> die Punktzahl des Spielers aktualisiert.
	 * @param x x-Koordinate des Zugs
	 * @param y y-Koordinate des Zugs
	 * @param playerIndex 1 für den ersten Spieler, 2 für den zweiten Spieler
	 * @param result <b>0</b> = kein Treffer, <b>1</b> = Treffer, <b>2</b> = Schiff versenkt, <b>3</b> = Zug rückgängig gemacht
	 */
	public void newDraw(int x, int y, int playerIndex, int result) {
		undoredo.newDraw(x, y, playerIndex, result);
		score.setScore(playerIndex, result);
	}
	
	/**
	 * Referenz auf die Methode in der UndoRedo-Klasse.
	 * Leert alle Stacks und lädt die Züge aus einer Datei in die Instanz-Variablen.
	 * Außerdem werden die Instanz-Variablen, die die Punkte- und Combo-Werte speichern,
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
	 * Gibt die Punktzahl eines Spielers zurück.
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
	 * Der Wert der Combo wird zurückgegeben, damit die GUI darstellen kann, 
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
	 * Liest den Wert aus der Config und gibt zurück, wie viele Spieler
	 * maximal in der Highscore-Liste angezeigt werden sollen.
	 * @return Spieler-Maximum im Highscore
	 */
	public int getHighscoreMaximum() {
		return settings.getHighscoreMaximum();
	}
	
	/**
	 * Referenz auf die Methode in der Settings-Klasse.
	 * Ändert den Wert, wie viele Spieler maximal im Highscore angezeigt werden.
	 * @param max Spieler-Maximum im Highscore
	 */
	public void setHighscoreMaximum(int max) {
		settings.setHighscoreMaximum(max);
	}
	
	/*
	 * MISCELLANEOUS
	 */
	
	/**
	 * Gibt alle Buchstaben und Sonderzeichen zurück,
	 * die in einem Namen nicht verwendet werden dürfen.
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
