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
	
	// TODO LIST

 
	// Instanz-Variablen in der SaveLoad-Klasse ?
	
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
	 * Erstellt einen Spielstand und speichert alle wichtigen Informationen als {@link String}.
	 * @param playerName Name des ersten Spielers
	 * @param opponentName Name des zweiten Spielers bzw. des Gegners
	 * @param boardSize Feldgr��e
	 * @param board Das gesamte Feld als {@link String} gespeichert.
	 * @param game Der Spielverlauf als {@link String} gespeichert.
	 */
	public void newGame(String gameName, String playerName, String opponentName, String boardPlayer1, String boardPlayer2, String boardsize) {
		saveload.newGame(gameName, playerName, opponentName, boardPlayer1, boardPlayer2, boardsize);
		if(!(score.doesPlayerExist(playerName))) {
			score.addPlayerIntoHighscore(playerName);
		}
		if(!(score.doesPlayerExist(opponentName))) {
			score.addPlayerIntoHighscore(opponentName);
		}
	}
	
	/**
	 * Referenz auf die Methode in der SaveLoad-Klasse.
	 * Gibt die Namen aller Spielst�nde als {@link String} zur�ck.
	 * Dies wird f�r die Auflistung aller Spielst�nde wichtig sein.
	 * Da die Weitergabe von Arrays nicht erlaubt ist,
	 * muss die Game-Klasse selbst aus dem String ein Array machen.
	 * Dies geht ganz einfach mit 
	 * getAllGameNames().split(",")
	 * @return Die Namen aller Spielst�nde als {@link String}
	 */
	public String getAllGameNames() {
		return saveload.getAllGameNames();
	}
	
	/**
	 * Referenz auf die Methode in der SaveLoad-Klasse.
	 * Diese Methode l�dt aus dem gespeicherten Spielstand des Spielers das gespeicherte Spielfeld heraus.
	 * Dieses Spielfeld wird dann als {@link String} zur�ckgegeben.
	 * @param playerName Name des ersten Spielers zur Identifizierung des gespeicherten Spielstands.
	 * @return Das geladene Spielfeld
	 */
	public String getBoardPlayer1(String gameName) {
		return saveload.getBoardPlayer1(gameName);
	}
	
	/**
	 * Referenz auf die Methode in der SaveLoad-Klasse.
	 * Gibt den Spielernamen eines Speicherstands zur�ck.
	 * @param gameName der gew�nschte Speicherstand
	 * @return Spielername eines Speicherstands
	 */
	public String getPlayerName(String gameName) {
		return saveload.getPlayerName(gameName);
	}
	
	/**
	 * Referenz auf die Methode in der SaveLoad-Klasse.
	 * Gibt den Namen des Gegners eines bestimmten Speicherstands zur�ck.
	 * @param gameName der gew�nschte Speicherstand
	 * @return Namen des Gegners eines bestimmten Speicherstands
	 */
	public String getOpponentName(String gameName) {
		return saveload.getOpponentName(gameName);
	}
	
	/**
	 * Referenz auf die Methode in der SaveLoad-Klasse.
	 * Gibt die Feldgr��e eines bestimmten Spielstands zur�ck.
	 * @param gameName der gew�nschte Spielstand
	 * @return Feldgr��e eines bestimmten Spielstands
	 */
	public String getBoardsize(String gameName) {
		return saveload.getBoardsize(gameName);
	}
	
	/**
	 * Referenz auf die Methode in der SaveLoad-Klasse.
	 * Gibt den String aus einem Spielstand zur�ck, der die Spielz�ge des ersten Spielers speichert.
	 * @param gameName der gew�nschte Spielstand
	 * @return Die Spielz�ge als {@link String}
	 */
	public String getDrawHistoryPlayer1(String gameName) {
		return saveload.getDrawHistoryPlayer1(gameName);
	}
	
	/**
	 * Referenz auf die Methode in der SaveLoad-Klasse.
	 * Gibt den String aus einem Spielstand zur�ck, der die Spielz�ge des zweiten Spielers speichert.
	 * @param gameName der gew�nschte Spielstand
	 * @return Die Spielz�ge als {@link String}
	 */
	public String getDrawHistoryPlayer2(String gameName) {
		return saveload.getDrawHistoryPlayer2(gameName);
	}
	
	/**
	 * Referenz auf die Methode in der SaveLoad-Klasse.
	 * Neue Methode, die effizienter aus der Datei liest.
	 * Es wird ein gew�nschter Spielstand zur�ckgegeben.
	 * @param gameName der gew�nschte Spielstand
	 * @return der Spielstand als {@link String}
	 */
	public String getGame(String gameName) {
		return saveload.getGame(gameName);
	}
	
	/**
	 * Referenz auf die Methode in der SaveLoad-Klasse.
	 * L�scht einen bestimmten Spielstand aus der Datei.
	 * @param gameName Name des Spielstands
	 */
	public void deleteGame(String gameName) {
		saveload.deleteGame(gameName);
	}
	
	
	/**
	 * Referenz auf die Methode in der SaveLoad-Klasse.
	 * Diese Methode speichert das Spielfeld, das als Parameter �bergeben wird in den Spielstand gameName.
	 * @param gameName der Spielstand, bei dem das Spielbrett abgespeichert werden soll.
	 * @param board das Spielbrett als {@link String}
	 */
	public void setBoard(String gameName, String board) {
//		saveload.setBoard(gameName, board);
	}
	
	/**
	 * Referenz auf die Methode in der SaveLoad-Klasse.
	 * �ndert den Namen des ersten Spielers zu playerName im Spielstand gameName.
	 * @param gameName der Spielstand, bei dem der Name des ersten Spielers ge�ndert werden soll.
	 * @param playerName der neue Name des ersten Spielers
	 */
	public void setPlayerName(String gameName, String playerName) {
		saveload.setPlayerName(gameName, playerName);
	}
	
	/**
	 * Referenz auf die Methode in der SaveLoad-Klasse.
	 * �ndert den Namen des Gegners eines Spielstands.
	 * @param gameName der Spielstand
	 * @param opponentName
	 */
	public void setOpponentName(String gameName, String opponentName) {
		saveload.setOpponentName(gameName, opponentName);
	}
	
	/**
	 * Referenz auf die Methode in der SaveLoad-Klasse.
	 * �ndert die Feldgr��e eines Spielstands.
	 * @param gameName der Spielstand
	 * @param boardSize
	 */
	public void setBoardsize(String gameName, String boardSize) {
		saveload.setBoardsize(gameName, boardSize);
	}
	
	/**
	 * Referenz auf die Methode in der SaveLoad-Klasse.
	 * �ndert den Spielverlauf des ersten Spielers in einem Spielstand.
	 * @param gameName der Spielstand
	 * @param drawHistoryPlayer1 der neue Spielverlauf des ersten Spielers
	 */
	public void setDrawHistoryPlayer1(String gameName, String drawHistoryPlayer1) {
		saveload.setDrawHistoryPlayer1(gameName, drawHistoryPlayer1);
	}
	
	/**
	 * Referenz auf die Methode in der SaveLoad-Klasse.
	 * �ndert den Spielverlauf des zweiten Spielers in einem Spielstand.
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
	
	/*
	 * UNDO-REDO
	 * CLASS
	 */
	
	/**
	 * Referenz auf die Methode in der UndoRedo-Klasse.
	 * Der letzte Zug wird r�ckg�ngig gemacht.
	 * Er wird daf�r aus der Liste, die den Spielverlauf speichert gel�scht und in eine
	 * separate Liste geschrieben, die die r�ckgangig gemachten Z�ge speichert.
	 * Falls ein Redo ausgef�hrt wird, wird auf eben diese Liste zur�ckgegriffen.
	 * @param gameName Name des Spiels zur Zuordnung
	 * @param playerIndex 1 f�r den ersten Spieler, 2 f�r den zweiten Spieler
	 * @return Der letzte Zug der Spielverlaufs-Liste, der in die Redoliste geschrieben wird
	 */
	public void undoDraw(String gameName, int playerIndex) {
		undoredo.undoDraw(gameName, playerIndex);
	}
	
	/**
	 * Referenz auf die Methode in der UndoRedo-Klasse.
	 * Der letzte Eintrag aus der Redo-Liste wird gel�scht und wieder in die Liste geschrieben,
	 * die den Spielverlauf speichert.
	 * Der zuletzt r�ckg�ngig gemachte Zug wird also ausgef�hrt.
	 * @return Der letzte Zug der Redoliste als {@link String}, der in die Spielverlaufs-Liste geschrieben wird.
	 */
	public void redoDraw() {
		undoredo.redoDraw();
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
	
	/**
	 * Referenz auf die Methode in der UndoRedo-Klasse.
	 * Die beiden Instanz-Variablen, die die Z�ge der Spieler speichern,
	 * speichern hiermit ihren Inhalt in der Datei
	 * @param gameName Name des Spiels zur Zuordnung
	 */
	public void saveDrawsToFile(String gameName) {
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
	 * @param event Events: u f�r undo, h f�r hit, s f�r sink (weitere folgen)
	 */
	public void updateScoreOnEvent(String gameName, String playerName, char event) {
		score.updateScoreOnEvent(gameName, playerName, event);
	}
	
	/**
	 * Referenz auf die Methode in der Score-Klasse.
	 * Ein bestimmter Spieler wird in die Highscore-Liste eingef�gt.
	 * @param playerName Der Spieler, der eingef�gt werden soll.
	 */
	public void addPlayerIntoHighscore(String playerName) {
		score.addPlayerIntoHighscore(playerName);
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
	
	public static void main(String[] args) {
		
		FileStream fs = new FileStream();
		
		fs.newGame("einTestSpiel", "Anna", "Bernd", "WWWWWCCCCDSDFDSFWWWW", "WWWWWWWDSFDFWWWWWW", "10,10");
		
		fs.newDraw("firstdraw", 1);
		fs.newDraw("anotherdraw", 2);
		
	}
	
}
