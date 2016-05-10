package shipz.io;

import java.util.ArrayList;

/**
 * Diese Klasse speichert alle Z�ge die get�tigt werden und behandelt die Undo-Redo-Mechanik.
 * @author Florian Osterberg
 */
public class UndoRedo {

	// IV
	/** Der Spielverlauf des ersten Spielers als ArrayList f�r Strings gespeichert. 
	 * Hier werden alle Z�ge nacheinander gespeichert. */
	private ArrayList<String> gamePlayer1;
	/** Der Spielverlauf des zweiten Spielers als ArrayList f�r Strings gespeichert. 
	 * Hier werden alle Z�ge nacheinander gespeichert. */
	private ArrayList<String> gamePlayer2;
	/** Diese Liste speichert die r�ckg�ngig gemachten Z�ge des ersten Spielers.
	 * Bei einem Redo wird der Zug wieder aus dieser Liste genommen und gel�scht. */
	private ArrayList<String> redoPlayer1;
	/** Diese Liste speichert die r�ckg�ngig gemachten Z�ge des zweiten Spielers.
	 * Bei einem Redo wird der Zug wieder aus dieser Liste genommen und gel�scht. */
	private ArrayList<String> redoPlayer2;
	/** Score-Objekt */
	private Score score;
	/** SaveLoad-Objekt */
	private SaveLoad saveload;
	
	// Konstruktor
	/**
	 * Initialisiert ein UndoRedo-Objekt.
	 * Die Array-Listen werden initialisiert.
	 */
	public UndoRedo() {
		gamePlayer1 = new ArrayList<String>();
		gamePlayer2 = new ArrayList<String>();
		redoPlayer1 = new ArrayList<String>();
		redoPlayer2 = new ArrayList<String>();
		score = new Score();
		saveload = new SaveLoad();
	}
	
	// IM
	/**
	 * Wenn ein neuer Zug get�tigt wird, wird dieser in den String, der den Spielverlauf speichert, geschrieben.
	 * @param draw Der Zug, der get�tigt wird als {@link String}
	 * @param playerIndex 1 f�r den ersten Spieler, 2 f�r den zweiten Spieler
	 */
	protected void newDraw(String draw, int playerIndex) {
		if(playerIndex == 1) {
			gamePlayer1.add(draw);
		} else if(playerIndex == 2) {
			gamePlayer2.add(draw);
		} else {
			System.err.println("Fehler, playerIndex muss entweder 1 oder 2 sein.");
		}
	}
	
	/**
	 * Der letzte Zug wird r�ckg�ngig gemacht.
	 * Er wird daf�r aus der Liste, die den Spielverlauf speichert gel�scht und in eine
	 * separate Liste geschrieben, die die r�ckgangig gemachten Z�ge speichert.
	 * Falls ein Redo ausgef�hrt wird, wird auf eben diese Liste zur�ckgegriffen.
	 * @return Der letzte Zug der Spielverlaufs-Liste, der in die Redoliste geschrieben wird
	 */
	protected String undoDraw(int playerIndex) {
		String lastDraw;
		if(playerIndex == 1) {
			lastDraw = gamePlayer1.get(gamePlayer1.size());
			redoPlayer1.set(redoPlayer1.size(), lastDraw);
		} else if(playerIndex == 2) {
			lastDraw = gamePlayer2.get(gamePlayer2.size());
			redoPlayer2.set(redoPlayer2.size(), lastDraw);
		} else {
			lastDraw = "";
			System.err.println("Fehler, playerIndex muss entweder 1 oder 2 sein.");
		}
//		score.updateScoreOnEvent(playerIndex);
		return lastDraw;
	}
	
	/**
	 * Der letzte Eintrag aus der Redo-Liste wird gel�scht und wieder in die Liste geschrieben,
	 * die den Spielverlauf speichert.
	 * Der zuletzt r�ckg�ngig gemachte Zug wird also ausgef�hrt.
	 * @return Der letzte Zug der Redoliste als {@link String}, der in die Spielverlaufs-Liste geschrieben wird.
	 */
	protected String redoDraw(int playerIndex) {
		String lastRedoneDraw;
		if(playerIndex == 1) {
			lastRedoneDraw = redoPlayer1.get(redoPlayer1.size());
			gamePlayer1.set(gamePlayer1.size(), lastRedoneDraw);
		} else if(playerIndex == 2) {
			lastRedoneDraw = redoPlayer2.get(redoPlayer2.size());
			gamePlayer2.set(gamePlayer2.size(), lastRedoneDraw);
		} else {
			lastRedoneDraw = "";
			System.err.println("Fehler, playerIndex muss entweder 1 oder 2 sein.");
		}
		return lastRedoneDraw; 
	}
	
	protected String getGamePlayer1() {
		return gamePlayer1.toString();
	}
	
	protected String getGamePlayer2() {
		return gamePlayer2.toString();
	}
	
	/**
	 * Leert alle Listen.
	 */
	protected void clear() {
		gamePlayer1.clear();
		gamePlayer2.clear();
		redoPlayer1.clear();
		redoPlayer2.clear();
	}
	
	protected void saveToFile() {
		
	}
	
	// ---
	
	/**
	 * main method
	 * @param args arguments
	 */
	public static void main(String[] args) {
		ArrayList<String> ar = new ArrayList<String>();
		ar.add(0, "a");
		ar.add(1, "b");
		ar.add(2, "c");
//		System.out.println(u.listToString(ar));
		System.out.println(ar.toString());
	}
	
}
