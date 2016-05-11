package shipz.io;

/**
 * Diese Klasse verwaltet die anderen vier Klassen.
 * Es werden alle wichtigen Methoden, die die Verwaltung des Spiels
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
	
	// kleiner Test
	// um zu schauen
	// ob git auch auf meinem Laptop funktioniert
	
	
	/**
	 * Konstruktor der Klasse.
	 */
	public FileStream() {
		score = new Score();
		undoredo = new UndoRedo();
		saveload = new SaveLoad();
	}
	
	// IM
	/**
	 * Referenz auf die Methode in der SaveLoad-Klasse.
	 * Erstellt einen Spielstand und speichert alle wichtigen Informationen als {@link String}.
	 * @param playerName Name des ersten Spielers
	 * @param opponentName Name des zweiten Spielers bzw. des Gegners
	 * @param boardSize Feldgr��e
	 * @param board Das gesamte Feld als {@link String} gespeichert.
	 * @param game Der Spielverlauf als {@link String} gespeichert.
	 */
	public void createGame(String playerName, String opponentName, int boardSize, String board, String game) {
//		saveload.createGame(playerName, opponentName, boardSize, board, game);
	}
	
	/**
	 * Referenz auf die Methode in der SaveLoad-Klasse.
	 * Methode, die den Spielstand in die Datei schreibt.
	 * Der gesamte Spielstand wird in der Instanz-Variable << savegame >> gespeichert.
	 */
	@Deprecated
	public void saveGameFile() {
//		saveload.saveGameFile();
	}
	
	/**
	 * Referenz auf die Methode in der SaveLoad-Klasse.
	 * Methode, die das Spielfeld in die IV abspeichert.
	 * @param playerName Name des Spielers
	 * @param board Das komplette Spielfeld als {@link String}
	 */
	public void createBoard(String fileName, String board) {
//		saveload.createBoard(playerName, board);
	}
	
	/**
	 * Referenz auf die Methode in der SaveLoad-Klasse.
	 * Diese Methode l�dt aus dem gespeicherten Spielstand des Spielers das gespeicherte Spielfeld heraus.
	 * Dieses Spielfeld wird dann als {@link String} zur�ckgegeben.
	 * @param playerName Name des ersten Spielers zur Identifizierung des gespeicherten Spielstands.
	 * @return Das geladene Spielfeld
	 */
	public String getBoard(String fileName) {
		return saveload.getBoard(fileName);
	}
	
	/**
	 * Referenz auf die Methode in der UndoRedo-Klasse.
	 * Der letzte Zug wird r�ckg�ngig gemacht.
	 * Er wird daf�r aus der Liste, die den Spielverlauf speichert gel�scht und in eine
	 * separate Liste geschrieben, die die r�ckgangig gemachten Z�ge speichert.
	 * Falls ein Redo ausgef�hrt wird, wird auf eben diese Liste zur�ckgegriffen.
	 * @return Der letzte Zug der Spielverlaufs-Liste, der in die Redoliste geschrieben wird
	 */
	public void undoDraw() {
//		undoredo.undoDraw();
	}
	
	/**
	 * Referenz auf die Methode in der UndoRedo-Klasse.
	 * Der letzte Eintrag aus der Redo-Liste wird gel�scht und wieder in die Liste geschrieben,
	 * die den Spielverlauf speichert.
	 * Der zuletzt r�ckg�ngig gemachte Zug wird also ausgef�hrt.
	 * @return Der letzte Zug der Redoliste als {@link String}, der in die Spielverlaufs-Liste geschrieben wird.
	 */
	public void redoDraw() {
//		undoredo.redoDraw();
	}
	
	/**
	 * Referenz auf die Methode in der UndoRedo-Klasse.
	 * Wenn ein neuer Zug get�tigt wird, wird dieser in den String, der den Spielverlauf speichert, geschrieben.
	 * @param draw Der Zug, der get�tigt wird als {@link String}
	 * @param playerIndex 1 f�r den ersten Spieler, 2 f�r den zweiten Spieler
	 */
	public void newDraw(String draw, int playerIndex) {
		undoredo.newDraw(draw, playerIndex);
	}
	
	public void updateScoreOnEvent(String playerName, char event) {
		score.updateScoreOnEvent(playerName, event);
	}
	
}
