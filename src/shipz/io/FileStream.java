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
	public void saveGame(String gameName, String playerName, String opponentName, String boardPlayerOne, String boardPlayerTwo, int boardsize, int activePlayer, String preferences, String mirrorFieldOne, String mirrorFieldTwo) {
		saveload.saveGame(gameName, playerName, opponentName, boardPlayerOne, boardPlayerTwo, boardsize, activePlayer, preferences, mirrorFieldOne, mirrorFieldTwo);
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
	public void saveGame(String gameName, String playerName, String opponentName, String boardPlayerOne, String boardPlayerTwo, int boardsize, int activePlayer, String preferences, String mirrorFieldOne) {
		saveGame(gameName, playerName, opponentName, boardPlayerOne, boardPlayerTwo, boardsize, activePlayer, preferences, mirrorFieldOne, null);
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
	public void saveGame(String gameName, String playerName, String opponentName, String boardPlayerOne, String boardPlayerTwo, int boardsize, int activePlayer, String preferences) {
		saveGame(gameName, playerName, opponentName, boardPlayerOne, boardPlayerTwo, boardsize, activePlayer, preferences, null, null);
	}
	
	/**
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
	 * Diese Methode l�dt aus dem gespeicherten Spielstand das gespeicherte Spielfeld des zweiten Spielers heraus.
	 * Wird zum Laden eines Spielstands ben�tigt.
	 * Dieses Spielfeld wird dann als {@link String} zur�ckgegeben.
	 * @param gameName Dateiname zur Identifizierung des gespeicherten Spielstands.
	 * @return Das geladene Spielfeld
	 */
	public String getBoardPlayerTwo(String gameName) {
		return saveload.getBoardPlayerTwo(gameName);
	}
	
	/**
	 * Gibt den Spielernamen eines Spielstands zur�ck.
	 * Wird zum Laden eines Spielstands ben�tigt.
	 * @param gameName der gew�nschte Spielstand
	 * @return Spielername eines Spielstand
	 */
	public String getPlayerName(String gameName) {
		return saveload.getPlayerName(gameName);
	}
	
	/**
	 * Gibt den Namen des Gegners eines bestimmten Spielstands zur�ck.
	 * Wird zum Laden eines Spielstands ben�tigt.
	 * @param gameName der gew�nschte Spielstand
	 * @return Namen des Gegners eines bestimmten Spielstands
	 */
	public String getOpponentName(String gameName) {
		return saveload.getOpponentName(gameName);
	}
	
	/**
	 * Gibt die Feldgr��e eines bestimmten Spielstands zur�ck.
	 * Wird zum Laden eines Spielstands ben�tigt.
	 * @param gameName der gew�nschte Spielstand
	 * @return Feldgr��e eines bestimmten Spielstands als {@link String}
	 */
	public int getBoardsize(String gameName) {
		return saveload.getBoardsize(gameName);
	}
	
	/**
	 * Liest den aktuellen Spieler eines Spiels aus der Datei aus und gibt ihn zur�ck.
	 * Wird zum Laden eines Spielstands ben�tigt.
	 * @param gameName Name des Spielstands
	 * @return der aktive Spieler
	 */
	public int getActivePlayer(String gameName) {
		return saveload.getActivePlayer(gameName);
	}
	
	/**
	 * Gibt die gespeicherte Uhrzeit eines Spielstands zur�ck.
	 * Wird zum Laden eines Spielstands ben�tigt.
	 * @param gameName der gew�nschte Spielstand
	 * @return die gespeicherte Uhrzeit als {@link String}
	 */
	public String getTime(String gameName) {
		return saveload.getTime(gameName);
	}
	
	/**
	 * Gibt die Einstellungen eines Spielstands zur�ck.
	 * Wird zum Laden eines Spielstands ben�tigt.
	 * @param gameName Name des Spielstands
	 * @return Einstellungen als String
	 */
	public String getPreferences(String gameName) {
		return saveload.getPreferences(gameName);
	}
	
	/**
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
	 * Leert alle Stacks und l�dt die Z�ge aus einer Datei in die Instanz-Variablen.
	 * Au�erdem werden die Instanz-Variablen, die die Punkte- und Combo-Werte speichern,
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
	 * Gibt die Punktzahl eines Spielers zur�ck.
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
	 * Der Wert der Combo wird zur�ckgegeben, damit die GUI darstellen kann, welche Combo der Spieler aktuell erreicht hat.
	 * @param playerIndex 1 = Spieler1, 2 = Spieler2
	 * @return aktueller Combowert des Spielers
	 */
	public int getComboValue(int playerIndex) {
		return score.getComboValue(playerIndex);
	}
	
	/**
	 * Wenn das Spiel vorbei ist, wird diese Methode ausgef�hrt, 
	 * damit die Punkte in der Datei abgespeichert werden.
	 * @param playerName Name des ersten Spielers
	 * @param opponentName Name des zweiten Spielers
	 */
	public void saveScoreToFile(String playerName, String opponentName) {
		score.saveScoreToFile(playerName, opponentName);
	}
	
	/*
	 * SETTINGS
	 * CLASS
	 */
	
	/**
	 * �ndert den Wert, wie viele Spieler maximal im Highscore angezeigt werden.
	 * @param max Spieler-Maximum im Highscore
	 */
	public void setHighscoreMaximum(int max) {
		settings.setHighscoreMaximum(max);
	}
	
	/**
	 * Liest aus der Datei und gibt den Wert zur�ck,
	 * der die L�nge der Pausen zwischen den KI-Z�gen
	 * in Millisekunden speichert.
	 * @return Pause zwischen KI-Z�gen in ms
	 */
	public int getAiTimer() {
		return settings.getAiTimer();
	}
	
	/**
	 * �ndert den Wert der Pausen zwischen den KI-Z�gen in der Config.
	 * @param ms neuer Wert f�r Pausen zwischen KI-Z�gen
	 */
	public void setAiTimer(int ms) {
		settings.setAiTimer(ms);
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
		return "=,{}<>;#~";
	}
	
}
