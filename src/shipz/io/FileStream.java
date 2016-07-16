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
	
	// Konstruktor
	/**
	 * Konstruktor der Klasse, die die Objekte der anderen Klassen initialisiert.
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
	 * @param mirrorFieldOne Spielfeld der 1. KI
	 * @param mirrorFieldTwo Spielfeld der 2. KI
	 */
	public void saveGame(String gameName, String playerName, String opponentName, String boardPlayerOne, String boardPlayerTwo, int boardsize, int activePlayer, String preferences, String gamemode, String mirrorFieldOne, String mirrorFieldTwo) {
		saveload.saveGame(gameName, playerName, opponentName, boardPlayerOne, boardPlayerTwo, boardsize, activePlayer, preferences, gamemode, mirrorFieldOne, mirrorFieldTwo);
		undoredo.saveToFile(gameName);
	}
	
	/**
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
	 * @param mirrorFieldOne Spielfeld der 1. KI
	 */
	public void saveGame(String gameName, String playerName, String opponentName, String boardPlayerOne, String boardPlayerTwo, int boardsize, int activePlayer, String preferences, String gamemode, String mirrorFieldOne) {
		saveGame(gameName, playerName, opponentName, boardPlayerOne, boardPlayerTwo, boardsize, activePlayer, preferences, gamemode, mirrorFieldOne, null);
	}
	
	/**
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
	public void saveGame(String gameName, String playerName, String opponentName, String boardPlayerOne, String boardPlayerTwo, int boardsize, int activePlayer, String preferences, String gamemode) {
		saveGame(gameName, playerName, opponentName, boardPlayerOne, boardPlayerTwo, boardsize, activePlayer, preferences, gamemode, null, null);
	}
	
	/**
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
	 * Diese Methode lädt aus dem gespeicherten Spielstand das gespeicherte Spielfeld des zweiten Spielers heraus.
	 * Wird zum Laden eines Spielstands benötigt.
	 * Dieses Spielfeld wird dann als {@link String} zurückgegeben.
	 * @param gameName Dateiname zur Identifizierung des gespeicherten Spielstands.
	 * @return Das geladene Spielfeld
	 */
	public String getBoardPlayerTwo(String gameName) {
		return saveload.getBoardPlayerTwo(gameName);
	}
	
	/**
	 * Gibt den Spielernamen eines Spielstands zurück.
	 * Wird zum Laden eines Spielstands benötigt.
	 * @param gameName der gewünschte Spielstand
	 * @return Spielername eines Spielstand
	 */
	public String getPlayerName(String gameName) {
		return saveload.getPlayerName(gameName);
	}
	
	/**
	 * Gibt den Namen des Gegners eines bestimmten Spielstands zurück.
	 * Wird zum Laden eines Spielstands benötigt.
	 * @param gameName der gewünschte Spielstand
	 * @return Namen des Gegners eines bestimmten Spielstands
	 */
	public String getOpponentName(String gameName) {
		return saveload.getOpponentName(gameName);
	}
	
	/**
	 * Gibt die Feldgr��e eines bestimmten Spielstands zurück.
	 * Wird zum Laden eines Spielstands benötigt.
	 * @param gameName der gewünschte Spielstand
	 * @return Feldgr��e eines bestimmten Spielstands als {@link String}
	 */
	public int getBoardsize(String gameName) {
		return saveload.getBoardsize(gameName);
	}
	
	/**
	 * Liest den aktuellen Spieler eines Spiels aus der Datei aus und gibt ihn zurück.
	 * Wird zum Laden eines Spielstands benötigt.
	 * @param gameName Name des Spielstands
	 * @return der aktive Spieler
	 */
	public int getActivePlayer(String gameName) {
		return saveload.getActivePlayer(gameName);
	}
	
	/**
	 * Gibt die gespeicherte Uhrzeit eines Spielstands zurück.
	 * Wird zum Laden eines Spielstands benötigt.
	 * @param gameName der gewünschte Spielstand
	 * @return die gespeicherte Uhrzeit als {@link String}
	 */
	public String getTime(String gameName) {
		return saveload.getTime(gameName);
	}
	
	/**
	 * Gibt die Einstellungen eines Spielstands zurück.
	 * Wird zum Laden eines Spielstands benötigt.
	 * @param gameName Name des Spielstands
	 * @return Einstellungen als String
	 */
	public String getPreferences(String gameName) {
		return saveload.getPreferences(gameName);
	}
	
	public String getMirrorFieldOne(String gameName) {
		return saveload.getMirrorFieldOne(gameName);
	}
	
	public String getMirrorFieldTwo(String gameName) {
		return saveload.getMirrorFieldTwo(gameName);
	}
	
	public String getGamemode(String gameName) {
		return saveload.getGamemode(gameName);
	}
	
	/**
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
	 * Die rückgängig zu machenden Züge werden vom Stack genommen, der die im Spiel getätigten
	 * Züge speichert, und auf den Stack gelegt, der die rückgängig gemachten Züge speichert.
	 * Alle rückgängig gemachten Züge werden als String zurückgegeben.
	 * @param playerIndex 1 für den ersten Spieler, 2 für den zweiten Spieler
	 * @return Die Züge die rückgängig gemacht werden als {@link String}
	 * @throws NoDrawException tritt auf, falls keine weiteren Züge rückgängig gemacht werden k�nnen
	 */
	public String undoDraw(int playerIndex) throws NoDrawException {
		score.setScore(playerIndex, 3);
		return undoredo.undoDraw(playerIndex);
	}
	
	/**
	 * Die Züge, die wiederhergestellt werden, werdem vom Redo-Stack genommen und wieder oben
	 * auf den Stack gelegt, der die Züge des Spiels gespeichert, gelegt.
	 * Alle wiederhergestellten Züge werden als String zurückgegeben.
	 * @param playerIndex 1 für den ersten Spieler, 2 für den zweiten Spieler
	 * @return Die wiederhergestellten Züge als {@link String}
	 * @throws NoDrawException tritt auf, falls keine weiteren Züge wiederholt werden k�nnen
	 */
	public String redoDraw(int playerIndex) throws NoDrawException {
		return undoredo.redoDraw(playerIndex);
	}
	
	/**
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
	 * Leert alle Stacks und lädt die Züge aus einer Datei in die Instanz-Variablen.
	 * Außerdem werden die Instanz-Variablen, die die Punkte- und Combo-Werte speichern,
	 * in der Score-Klasse aktualisiert.
	 * Wird verwendet, wenn ein Spielstand geladen wird.
	 * @param gameName Name des Spielstands
	 */
	public void loadDrawsAndScore(String gameName) {
		undoredo.loadDraws(gameName);
		score.loadScore(gameName);
	}
	
	/*
	 * SCORE
	 * CLASS
	 */
	
	/**
	 * Gibt die Punktzahl eines Spielers zurück.
	 * @param playerIndex 1 = Spieler1, 2 = Spieler2
	 * @return Punktzahl des Spielers
	 */
	public int getScore(int playerIndex) {
		return score.getScore(playerIndex);
	}
	
	/**
	 * Liest alle Punktzahlen aus dem Highscore-File
	 * und erstellt eine absteigend sortierte Highscore-Liste.
	 * Es werden maximal die zehn besten Spieler angezeigt.
	 * @return Die Highscore-Liste als String
	 */
	public String highscore() {
		return score.highscore(settings.getHighscoreMaximum());
	}
	
	/**
	 * Der Wert der Combo wird zurückgegeben, damit die GUI darstellen kann, welche Combo der Spieler aktuell erreicht hat.
	 * @param playerIndex 1 = Spieler1, 2 = Spieler2
	 * @return aktueller Combowert des Spielers
	 */
	public int getComboValue(int playerIndex) {
		return score.getComboValue(playerIndex);
	}
	
	/**
	 * Wenn das Spiel vorbei ist, wird diese Methode ausgeführt, 
	 * damit die Punkte in der Datei abgespeichert werden.
	 * @param playerName Name des ersten Spielers
	 * @param opponentName Name des zweiten Spielers
	 */
	public void saveScoreToFile(String playerName, String opponentName) {
		score.saveScoreToFile(playerName, opponentName, settings.getHighscoreMaximum());
	}
	
	/*
	 * SETTINGS
	 * CLASS
	 */
	
	/**
	 * Ändert den Wert, wie viele Spieler maximal im Highscore angezeigt werden.
	 * @param max Spieler-Maximum im Highscore
	 */
	public void setHighscoreMaximum(int max) {
		settings.setHighscoreMaximum(max);
	}
	
	/**
	 * Liest aus der Datei und gibt den Wert zurück,
	 * der die Länge der Pausen zwischen den KI-Zügen
	 * in Millisekunden speichert.
	 * @return Pause zwischen KI-Zügen in ms
	 */
	public int getAiTimer() {
		return settings.getAiTimer();
	}
	
	/**
	 * Ändert den Wert der Pausen zwischen den KI-Zügen in der Config.
	 * @param ms neuer Wert für Pausen zwischen KI-Zügen
	 */
	public void setAiTimer(int ms) {
		settings.setAiTimer(ms);
	}
	
	/*
	 * MISCELLANEOUS
	 */
	
	/**
	 * Gibt alle Buchstaben und Sonderzeichen zurück,
	 * die in einem Namen nicht verwendet werden d�rfen.
	 * Um diesen String in ein Char-Array umzuwandeln:
	 * <i>forbiddenCharacters().toCharArray()</i>
	 * @return die verbotenen Zeichen als String
	 */
	public String forbiddenCharacters() {
		return "=,{}<>;#~";
	}
	
}
