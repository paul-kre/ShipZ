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
	public void saveGame(String gameName, String playerName, String opponentName, String boardPlayerOne, String boardPlayerTwo, String boardsize, int activePlayer, String preferences) {
		saveload.saveGame(gameName, playerName, opponentName, boardPlayerOne, boardPlayerTwo, boardsize, activePlayer, preferences);
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
	 * Die rückgängig zu machenden Züge werden vom Stack genommen, der die im Spiel getätigten
	 * Züge speichert, und auf den Stack gelegt, der die rückgängig gemachten Züge speichert.
	 * Alle rückgängig gemachten Züge werden als String zurückgegeben.
	 * @param playerIndex 1 für den ersten Spieler, 2 für den zweiten Spieler
	 * @return Die Züge die rückgängig gemacht werden als {@link String}
	 * @throws NoDrawException tritt auf, falls keine weiteren Züge rückgängig gemacht werden können
	 */
	public String undoDraw(int playerIndex) throws NoDrawException {
		score.setScore(playerIndex, (byte)3);
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
	public void newDraw(int x, int y, int playerIndex, byte result) {
		undoredo.newDraw(x, y, playerIndex, result);
		score.setScore(playerIndex, result);
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
	 * Wenn das Spiel vorbei ist, wird diese Methode ausgeführt, 
	 * damit die Punkte in der Datei abgespeichert werden.
	 * Die Punkte werden zusammen mit dem Namen und der aktuellen Uhrzeit
	 * abgespeichert im Format <i>Name#Uhrzeit=Punkte</i>.
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
	 * @param playerIndex 1 = Spieler1, 2 = Spieler2
	 * @return aktueller Combowert des Spielers
	 */
	public int getComboValue(int playerIndex) {
		return score.getComboValue(playerIndex);
	}
	
	/*
	 * MISCELLANEOUS
	 */
	
	/**
	 * Gibt alle Buchstaben und Sonderzeichen zurück,
	 * die in einem Namen nicht verwendet werden dürfen.
	 * Um diesen String in ein Char-Array umzuwandeln:
	 * <i>forbiddenCharacters().toCharArray()</i>
	 * @return die verbotenen Zeichen
	 */
	public String forbiddenCharacters() {
		char[] c = { '=', ',', '{', '}', '<', '>', ';', '#' };
		String r = "";
		for(int i = 0; i < c.length; i++) {
			r += c[i];
		}
		return r;
	}
	
	/**
	 * Main-Methode zum Testen
	 * @param args
	 */
	public static void main(String[] args) {
		FileStream fs = new FileStream();
		fs.newDraw(0, 0, 1, (byte)1);
		fs.newDraw(0, 0, 1, (byte)1);
		fs.newDraw(0, 0, 1, (byte)2);
		fs.newDraw(0, 0, 1, (byte)0);
		System.out.println(fs.getScore(1));
		System.out.println(fs.getComboValue(1));
		
	}
	
}
