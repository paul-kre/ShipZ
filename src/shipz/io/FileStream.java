package shipz.io;

/**
 * Diese Klasse verwaltet die anderen vier Klassen.
 * Es werden alle wichtigen Methoden, die die Verwaltung des Spiels
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
	
	// TODO LIST

	// Redo Funktion implementieren
	
	/**
	 * Konstruktor der Klasse.
	 */
	public FileStream() {
		score = new Score();
		undoredo = new UndoRedo();
		saveload = new SaveLoad();
	}
	
	/*
	 * SAVE-LOAD
	 * CLASS
	 */
	
	// IM
	/**
	 * Referenz auf die Methode in der SaveLoad-Klasse.
	 * Speichert die Informationen eines bestimmten Spiels.
	 * Wird von der Game-Klasse genutzt, um ein Spiel zwischenzeitlich abzuspeichern.
	 * @param gameName Name des Spielstands
	 * @param playerName Name des ersten Spielers
	 * @param opponentName Name des Gegners / zweiten Spielers
	 * @param boardPlayerOne Spielbrett des ersten Spielers als {@link String}
	 * @param boardPlayerTwo Spielbrett des zweiten Spielers als {@link String}
	 * @param activePlayer aktiver Spieler
	 */
	public void saveGame(String gameName, String playerName, String opponentName, String boardPlayerOne, String boardPlayerTwo, String boardsize, int activePlayer) {
		saveload.saveGame(gameName, playerName, opponentName, boardPlayerOne, boardPlayerTwo, boardsize, activePlayer);
		undoredo.saveToFile(gameName);
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
	 * Der letzte Zug wird rückgängig gemacht.
	 * Er wird dafür aus der Liste, die den Spielverlauf speichert gelöscht und in eine
	 * separate Liste geschrieben, die die rückgangig gemachten Züge speichert.
	 * Falls ein Redo ausgeführt wird, wird auf eben diese Liste zurückgegriffen.
	 * @param gameName Name des Spiels zur Zuordnung
	 * @param playerIndex 1 für den ersten Spieler, 2 für den zweiten Spieler
	 * @return Der letzte Zug der Spielverlaufs-Liste, der in die Redoliste geschrieben wird
	 */
	public void undoDraw(int playerIndex) {
		undoredo.undoDraw(playerIndex);
	}
	
	/**
	 * Referenz auf die Methode in der UndoRedo-Klasse.
	 * Der letzte Eintrag aus der Redo-Liste wird gelöscht und wieder in die Liste geschrieben,
	 * die den Spielverlauf speichert.
	 * Der zuletzt rückgängig gemachte Zug wird also ausgeführt.
	 * @return Der letzte Zug der Redoliste als {@link String}, der in die Spielverlaufs-Liste geschrieben wird.
	 */
	public void redoDraw() {
		undoredo.redoDraw();
	}
	
	/**
	 * Referenz auf die Methode in der UndoRedo-Klasse.
	 * Wenn ein neuer Zug getätigt wird, wird dieser in den String, der den Spielverlauf speichert, geschrieben.
	 * @param draw Der Zug, der getätigt wird als {@link String}
	 * @param playerIndex 1 für den ersten Spieler, 2 für den zweiten Spieler
	 */
	public void newDraw(String draw, int playerIndex) {
		undoredo.newDraw(draw, playerIndex);
	}
	
	/**
	 * Referenz auf die Methode in der UndoRedo-Klasse.
	 * Die Instanz-Variable, die die Züge der Spieler speichert,
	 * speichert hiermit ihren Inhalt in der Datei
	 * @param gameName Name des Spielstands
	 */
	public void saveToFile(String gameName) {
		undoredo.saveToFile(gameName);
	}
	
	/*
	 * SCORE
	 * CLASS
	 */
	
	/**
	 * Referenz auf die Methode in der Score-Klasse.
	 * Setzt die Punktzahl eines bestimmten Spielers zu einem bestimmten Event.
	 * @param playerName Name des Spielers
	 * @param event Events: u für undo, h für hit, s für sink (weitere folgen)
	 */
	public void setScore(int playerIndex, char event) {
		score.setScore(playerIndex, event);
	}
	
	/**
	 * Referenz auf die Methode in der Score-Klasse.
	 * Liest alle Punktzahlen aus dem Highscore-File
	 * und erstellt eine absteigend sortierte Highscore-Liste.
	 * @return Die Highscore-Liste als String
	 */
	public String highscore() {
		return score.highscore();
	}
	
	/**
	 * Referenz auf die Methode in der Score-Klasse.
	 * Wenn das Spiel vorbei ist, wird diese Methode ausgeführt, 
	 * damit die Punkte in der Datei abgespeichert werden.
	 * @param playerName Name des ersten Spielers
	 * @param opponentName Name des zweiten Spielers
	 */
	public void saveScoreToFile(String playerName, String opponentName) {
		score.saveScoreToFile(playerName, opponentName);
	}
	
	/**
	 * Referenz auf die Methode in der Score-Klasse.
	 * Der Wert der Combo wird zurückgegeben, damit die GUI darstellen kann, 
	 * welche Combo der Spieler aktuell erreicht hat.
	 * @param playerIndex 
	 * @return
	 */
	public double getComboValue(int playerIndex) {
		return score.getComboValue(playerIndex);
	}
	
	public static void main(String[] args) {
		
	}
	
}
