package shipz.io;

import java.util.Stack;

/**
 * Diese Klasse speichert alle Z�ge die get�tigt werden und behandelt die Undo-Redo-Mechanik.
 * @author Florian Osterberg
 */
public class UndoRedo {

	// IV
	/** Der Spielverlauf des ersten Spielers als Stack f�r Strings gespeichert. 
	 * Hier werden alle Z�ge nacheinander gespeichert. */
	private Stack<String> gamePlayer1;
	/** Der Spielverlauf des zweiten Spielers als Stack f�r Strings gespeichert. 
	 * Hier werden alle Z�ge nacheinander gespeichert. */
	private Stack<String> gamePlayer2;
	/** Diese Liste speichert die r�ckg�ngig gemachten Z�ge des ersten Spielers.
	 * Bei einem Redo wird der Zug wieder vom Stack genommen und gel�scht. */
	private Stack<String> redoPlayer1;
	/** Diese Liste speichert die r�ckg�ngig gemachten Z�ge des zweiten Spielers.
	 * Bei einem Redo wird der Zug wieder vom Stack genommen und gel�scht. */
	private Stack<String> redoPlayer2;
	/** Score-Objekt */
	private Score score;
	/** SaveLoad-Objekt */
	private SaveLoad saveload;
	/** Der String, der einzelne Z�ge in der Datei trennt. */
	private String drawSeparator = ",";
	
	// Konstruktor
	/**
	 * Initialisiert ein UndoRedo-Objekt.
	 * Die Array-Listen werden initialisiert.
	 * @param gameName 
	 */
	public UndoRedo() {
		gamePlayer1 = new Stack<String>();
		gamePlayer2 = new Stack<String>();
		redoPlayer1 = new Stack<String>();
		redoPlayer2 = new Stack<String>();
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
			gamePlayer1.push(draw);
		} else if(playerIndex == 2) {
			gamePlayer2.push(draw);
		} else {
			System.err.println("Fehler, playerIndex muss entweder 1 oder 2 sein.");
		}
	}
	
	/**
	 * Der letzte Zug wird r�ckg�ngig gemacht.
	 * Er wird daf�r aus der Liste, die den Spielverlauf speichert gel�scht und in eine
	 * separate Liste geschrieben, die die r�ckgangig gemachten Z�ge speichert.
	 * Falls ein Redo ausgef�hrt wird, wird auf eben diese Liste zur�ckgegriffen.
	 * @param gameName Name des Spiels zur Zuordnung
	 * @param playerIndex 1 f�r den ersten Spieler, 2 f�r den zweiten Spieler
	 * @return Der letzte Zug der Spielverlaufs-Liste, der in die Redoliste geschrieben wird
	 */
	protected /*String*/void undoDraw(String gameName, int playerIndex) {
		String lastDraw1 = gamePlayer1.pop();
		String lastDraw2 = gamePlayer2.pop();
		
		redoPlayer1.push(lastDraw1);
		redoPlayer2.push(lastDraw2);
		
		if(playerIndex == 1) {
			score.updateScoreOnEvent(gameName, saveload.getPlayerName(gameName), 'u');
		} else if(playerIndex == 2) {
			score.updateScoreOnEvent(gameName, saveload.getOpponentName(gameName), 'u');
		} else {
			System.err.println("Fehler, playerIndex muss entweder 1 oder 2 sein.");
		}
		
//		return lastDraw;
	}
	
	/**
	 * Der letzte Eintrag aus der Redo-Liste wird gel�scht und wieder in die Liste geschrieben,
	 * die den Spielverlauf speichert.
	 * Der zuletzt r�ckg�ngig gemachte Zug wird also ausgef�hrt.
	 * @param gameName Name des Spiels zur Zuordnung
	 * @param playerIndex 1 f�r den ersten Spieler, 2 f�r den zweiten Spieler
	 * @return Der letzte Zug der Redoliste als {@link String}, der in die Spielverlaufs-Liste geschrieben wird.
	 */
	protected /*String*/void redoDraw() {
		String lastRedoneDraw1 = redoPlayer1.pop();
		String lastRedoneDraw2 = redoPlayer2.pop();
		
		gamePlayer1.push(lastRedoneDraw1);
		gamePlayer2.push(lastRedoneDraw2);
		
//		return lastRedoneDraw; 
	}
	
	/**
	 * Gibt den Stack f�r die Z�ge des ersten Spielers als String zur�ck.
	 * @return die ArrayList f�r die Z�ge des ersten Spielers als String
	 */
	protected String getDrawsPlayer1() {
		return gamePlayer1.toString();
	}
	
	/**
	 * Gibt den Stack f�r die Z�ge des zweiten Spielers als String zur�ck.
	 * @return die ArrayList f�r die Z�ge des zweiten Spielers als String
	 */
	protected String getDrawsPlayer2() {
		return gamePlayer2.toString();
	}
	
	/**
	 * Gibt den Stack f�r die r�ckg�ngig gemachten Z�ge des ersten Spielers zur�ck.
	 * @return der Stack f�r die r�ckg�ngig gemachten Z�ge des ersten Spielers
	 */
	protected String getRedoneDrawsPlayer1() {
		return redoPlayer1.toString();
	}
	
	/**
	 * Gibt den Stack f�r die r�ckg�ngig gemachten Z�ge des zweiten Spielers zur�ck.
	 * @return der Stack f�r die r�ckg�ngig gemachten Z�ge des zweiten Spielers
	 */
	protected String getRedoneDrawsPlayer2() {
		return redoPlayer2.toString();
	}
	
	/**
	 * Die beiden Instanz-Variablen, die die Z�ge der Spieler speichern,
	 * speichern hiermit ihren Inhalt in der Datei
	 * @param gameName Name des Spiels zur Zuordnung
	 */
	protected void saveToFile(String gameName) {
		saveload.setDrawHistoryPlayerOne(gameName, gamePlayer1.toString().replaceAll(", ", drawSeparator).replaceAll("]", "").substring(1));
		saveload.setDrawHistoryPlayerTwo(gameName, gamePlayer2.toString().replaceAll(", ", drawSeparator).replaceAll("]", "").substring(1));
	}
	
	/**
	 * Leert alle Stacks.
	 * Alle get�tigten Z�ge werden gel�scht,
	 * sie werden NICHT in der Datei gespeichert.
	 * Um die Daten zu speichern sollte vorher die Methode
	 * <i>.saveToFile()</i> aufgerufen werden.
	 */
	protected void clear() {
		gamePlayer1.clear();
		gamePlayer2.clear();
		redoPlayer1.clear();
		redoPlayer2.clear();
	}
	
	/**
	 * main method
	 * @param args arguments
	 */
	public static void main(String[] args) {
/*		ArrayList<String> ar = new ArrayList<String>();
		ar.add(0, "a");
		ar.add(1, "b");
		ar.add(2, "c");
		System.out.println(u.listToString(ar)); 
		System.out.println(ar.toString()); */
		
		UndoRedo ur = new UndoRedo();
//		ur.newDraw("ErsterZug", 1);
//		ur.newDraw("ZweiterZug", 1);
//		ur.newDraw("1sterZug", 2);
//		ur.saveToFile("blabla");
		
		
/*		Stack<String> test = new Stack<String>();
		test.push("1");
		test.push("2");
		test.push("3");
		System.out.println(test.toString());
		System.out.println(test.pop());
		System.out.println(test.toString()); */
		
		ur.newDraw("FirstDraw", 1);
		ur.newDraw("First Draw for player 2", 2);
		ur.newDraw("next Draw", 1);
		ur.newDraw("another Draw", 2);
		ur.newDraw("another one", 1);
		ur.newDraw("and another one", 2);
		
		System.out.println(ur.getDrawsPlayer1());
		System.out.println(ur.getDrawsPlayer2()+"\n");
		
		ur.undoDraw("testspiel", 1);
		System.out.println(ur.getDrawsPlayer1());
		System.out.println(ur.getDrawsPlayer2()+"\n");
		
		ur.redoDraw();
		System.out.println(ur.getDrawsPlayer1());
		System.out.println(ur.getDrawsPlayer2());
		
	}
	
}
