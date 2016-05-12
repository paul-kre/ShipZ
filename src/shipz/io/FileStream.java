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
	
	/**
	 * Konstruktor der Klasse.
	 */
	public FileStream() {
		score = new Score();
		undoredo = new UndoRedo();
		saveload = new SaveLoad();
	}
	
	// wtf warum klappt "commit and push" jetzt nicht mehr?
	// test
	
	// IM
	/**
	 * Referenz auf die Methode in der SaveLoad-Klasse.
	 * Erstellt einen Spielstand und speichert alle wichtigen Informationen als {@link String}.
	 * @param playerName Name des ersten Spielers
	 * @param opponentName Name des zweiten Spielers bzw. des Gegners
	 * @param boardSize Feldgröße
	 * @param board Das gesamte Feld als {@link String} gespeichert.
	 * @param game Der Spielverlauf als {@link String} gespeichert.
	 */
	public void newGame(String gameName, String playerName, String opponentName, int boardSize, String board, String drawHistoryPlayer1, String drawHistoryPlayer2) {
		saveload.newGame(gameName, playerName, opponentName, boardSize, board, drawHistoryPlayer1, drawHistoryPlayer2);
	}
	
	/**
	 * Referenz auf die Methode in der SaveLoad-Klasse.
	 * Gibt die Namen aller Spielstände als {@link String} zurück.
	 * Dies wird für die Auflistung aller Spielstände wichtig sein.
	 * Da die Weitergabe von Arrays nicht erlaubt ist,
	 * muss die Game-Klasse selbst aus dem String ein Array machen.
	 * Dies geht ganz einfach mit 
	 * getAllGameNames().split(",")
	 * @return Die Namen aller Spielstände als {@link String}
	 */
	public String getAllGameNames() {
		return saveload.getAllGameNames();
	}
	
	/**
	 * Referenz auf die Methode in der SaveLoad-Klasse.
	 * Diese Methode lädt aus dem gespeicherten Spielstand des Spielers das gespeicherte Spielfeld heraus.
	 * Dieses Spielfeld wird dann als {@link String} zurückgegeben.
	 * @param playerName Name des ersten Spielers zur Identifizierung des gespeicherten Spielstands.
	 * @return Das geladene Spielfeld
	 */
	public String getBoard(String gameName) {
		return saveload.getBoard(gameName);
	}
	
	/**
	 * Referenz auf die Methode in der SaveLoad-Klasse.
	 * Gibt den Spielernamen eines Speicherstands zurück.
	 * @param gameName der gewünschte Speicherstand
	 * @return Spielername eines Speicherstands
	 */
	public String getPlayerName(String gameName) {
		return saveload.getPlayerName(gameName);
	}
	
	/**
	 * Referenz auf die Methode in der SaveLoad-Klasse.
	 * Gibt den Namen des Gegners eines bestimmten Speicherstands zurück.
	 * @param gameName der gewünschte Speicherstand
	 * @return Namen des Gegners eines bestimmten Speicherstands
	 */
	public String getOpponentName(String gameName) {
		return saveload.getOpponentName(gameName);
	}
	
	/**
	 * Referenz auf die Methode in der SaveLoad-Klasse.
	 * Gibt die Feldgröße eines bestimmten Spielstands zurück.
	 * @param gameName der gewünschte Spielstand
	 * @return Feldgröße eines bestimmten Spielstands
	 */
	public int getBoardsize(String gameName) {
		return saveload.getBoardsize(gameName);
	}
	
	/**
	 * Referenz auf die Methode in der SaveLoad-Klasse.
	 * Gibt den String aus einem Spielstand zurück, der die Spielzüge des ersten Spielers speichert.
	 * @param gameName der gewünschte Spielstand
	 * @return Die Spielzüge als {@link String}
	 */
	public String getDrawHistoryPlayer1(String gameName) {
		return saveload.getDrawHistoryPlayer1(gameName);
	}
	
	/**
	 * Referenz auf die Methode in der SaveLoad-Klasse.
	 * Gibt den String aus einem Spielstand zurück, der die Spielzüge des zweiten Spielers speichert.
	 * @param gameName der gewünschte Spielstand
	 * @return Die Spielzüge als {@link String}
	 */
	public String getDrawHistoryPlayer2(String gameName) {
		return saveload.getDrawHistoryPlayer2(gameName);
	}
	
	/**
	 * Referenz auf die Methode in der SaveLoad-Klasse.
	 * Neue Methode, die effizienter aus der Datei liest.
	 * Es wird ein gewünschter Spielstand zurückgegeben.
	 * @param gameName der gewünschte Spielstand
	 * @return der Spielstand als {@link String}
	 */
	public String getGame(String gameName) {
		return saveload.getGame(gameName);
	}
	
	/**
	 * Referenz auf die Methode in der SaveLoad-Klasse.
	 * Löscht einen bestimmten Spielstand aus der Datei.
	 * @param gameName Name des Spielstands
	 */
	public void deleteGame(String gameName) {
		saveload.deleteGame(gameName);
	}
	
	
	/**
	 * Referenz auf die Methode in der SaveLoad-Klasse.
	 * Diese Methode speichert das Spielfeld, das als Parameter übergeben wird in den Spielstand gameName.
	 * @param gameName der Spielstand, bei dem das Spielbrett abgespeichert werden soll.
	 * @param board das Spielbrett als {@link String}
	 */
	public void setBoard(String gameName, String board) {
		saveload.setBoard(gameName, board);
	}
	
	/**
	 * Referenz auf die Methode in der SaveLoad-Klasse.
	 * Ändert den Namen des ersten Spielers zu playerName im Spielstand gameName.
	 * @param gameName der Spielstand, bei dem der Name des ersten Spielers geändert werden soll.
	 * @param playerName der neue Name des ersten Spielers
	 */
	public void setPlayerName(String gameName, String playerName) {
		saveload.setPlayerName(gameName, playerName);
	}
	
	/**
	 * Referenz auf die Methode in der SaveLoad-Klasse.
	 * Ändert den Namen des Gegners eines Spielstands.
	 * @param gameName der Spielstand
	 * @param opponentName
	 */
	public void setOpponentName(String gameName, String opponentName) {
		saveload.setOpponentName(gameName, opponentName);
	}
	
	/**
	 * Referenz auf die Methode in der SaveLoad-Klasse.
	 * Ändert die Feldgröße eines Spielstands.
	 * @param gameName der Spielstand
	 * @param boardSize
	 */
	public void setBoardsize(String gameName, int boardSize) {
		saveload.setBoardsize(gameName, boardSize);
	}
	
	/**
	 * Referenz auf die Methode in der SaveLoad-Klasse.
	 * Ändert den Spielverlauf des ersten Spielers in einem Spielstand.
	 * @param gameName der Spielstand
	 * @param drawHistoryPlayer1 der neue Spielverlauf des ersten Spielers
	 */
	public void setDrawHistoryPlayer1(String gameName, String drawHistoryPlayer1) {
		saveload.setDrawHistoryPlayer1(gameName, drawHistoryPlayer1);
	}
	
	/**
	 * Referenz auf die Methode in der SaveLoad-Klasse.
	 * Ändert den Spielverlauf des zweiten Spielers in einem Spielstand.
	 * @param gameName der Spielstand
	 * @param drawHistoryPlayer2 der neue Spielverlauf des zweiten Spielers
	 */
	public void setDrawHistoryPlayer2(String gameName, String drawHistoryPlayer2) {
		saveload.setDrawHistoryPlayer2(gameName, drawHistoryPlayer2);
	}
	
	/**
	 * Referenz auf die Methode in der SaveLoad-Klasse.
	 * Aktualisiert die Uhrzeit eines Spielstands.
	 * @param gameName der Spielstand
	 */
	public void updateTime(String gameName) {
		saveload.updateTime(gameName);
	}
	
	
	/**
	 * Referenz auf die Methode in der SaveLoad-Klasse.
	 * Methode, die den Spielstand in die Datei schreibt.
	 * Der gesamte Spielstand wird in der Instanz-Variable << savegame >> gespeichert.
	 * 
	 * Veraltete Methode, wird bald gelöscht.
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
	@Deprecated
	public void createBoard(String fileName, String board) {
//		saveload.createBoard(playerName, board);
	}
	
	/**
	 * Referenz auf die Methode in der UndoRedo-Klasse.
	 * Der letzte Zug wird rückgängig gemacht.
	 * Er wird dafür aus der Liste, die den Spielverlauf speichert gelöscht und in eine
	 * separate Liste geschrieben, die die rückgangig gemachten Züge speichert.
	 * Falls ein Redo ausgeführt wird, wird auf eben diese Liste zurückgegriffen.
	 * @return Der letzte Zug der Spielverlaufs-Liste, der in die Redoliste geschrieben wird
	 */
	public void undoDraw() {
		
	}
	
	/**
	 * Referenz auf die Methode in der UndoRedo-Klasse.
	 * Der letzte Eintrag aus der Redo-Liste wird gelöscht und wieder in die Liste geschrieben,
	 * die den Spielverlauf speichert.
	 * Der zuletzt rückgängig gemachte Zug wird also ausgeführt.
	 * @return Der letzte Zug der Redoliste als {@link String}, der in die Spielverlaufs-Liste geschrieben wird.
	 */
	public void redoDraw() {
		
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
	 * Referenz auf die Methode in der Score-Klasse.
	 * Setzt die Punktzahl eines bestimmten Spielers zu einem bestimmten Event.
	 * @param playerName Name des Spielers
	 * @param event Events: u für undo, h für hit, s für sink (weitere folgen)
	 */
	public void updateScoreOnEvent(String playerName, char event) {
		score.updateScoreOnEvent(playerName, event);
	}
	
}
