package shipz.io;

import java.util.Stack;

/**
 * Diese Klasse speichert alle Züge die getätigt werden und behandelt die Undo-Redo-Mechanik.
 * @author Florian Osterberg
 */
public class UndoRedo {

	// IV
	/** In diesem Stack werden alle Züge als Strings gespeichert. */
	private Stack<String> game;
	/** In diesem Stack werden alle rückgängig gemachten Züge als Strings gespeichert. */
	private Stack<String> redo;
	/** Score-Objekt */
	private Score score;
	/** SaveLoad-Objekt */
	private SaveLoad saveload;
	/** Der String, der einzelne Züge in der Datei trennt. */
	private String drawSeparator = ",";
	
	// TODO
	// In SaveLoad:
		// Draws statt DrawHistory1 & DrawHistory2
	
	// Konstruktor
	/**
	 * Initialisiert ein UndoRedo-Objekt.
	 * Die Array-Listen werden initialisiert.
	 * @param gameName 
	 */
	public UndoRedo() {
		game = new Stack<String>();
		redo = new Stack<String>();
		score = new Score();
		saveload = new SaveLoad();
	}
	
	// IM
	/**
	 * Wenn ein neuer Zug getätigt wird, wird dieser in den String, der den Spielverlauf speichert, geschrieben.
	 * @param draw Der Zug, der getätigt wird als {@link String}
	 * @param playerIndex 1 für den ersten Spieler, 2 für den zweiten Spieler
	 */
	protected void newDraw(String draw, int playerIndex) {
		game.push(playerIndex+"|"+draw);
	}
	
	/**
	 * Der letzte Zug wird rückgängig gemacht.
	 * Er wird dafür aus der Liste, die den Spielverlauf speichert gelöscht und in eine
	 * separate Liste geschrieben, die die rückgangig gemachten Züge speichert.
	 * Falls ein Redo ausgeführt wird, wird auf eben diese Liste zurückgegriffen.
	 * @param gameName Name des Spiels zur Zuordnung
	 * @param playerIndex 1 für den ersten Spieler, 2 für den zweiten Spieler
	 * @return Der letzte Zug der Spielverlaufs-Liste, der in die Redoliste geschrieben wird
	 */
	protected String undoDraw(int playerIndex) {
		String result = "";
		String draw = "";
		while(!(draw.startsWith(playerIndex+""))) {
			draw = game.pop();
			redo.push(draw);
			result += draw + ";";
		}
		score.setScore(playerIndex, 'u');
		
		return result;
	}
	
	/**
	 * Der letzte Eintrag aus der Redo-Liste wird gelöscht und wieder in die Liste geschrieben,
	 * die den Spielverlauf speichert.
	 * Der zuletzt rückgängig gemachte Zug wird also ausgeführt.
	 * @param gameName Name des Spiels zur Zuordnung
	 * @param playerIndex 1 für den ersten Spieler, 2 für den zweiten Spieler
	 * @return Der letzte Zug der Redoliste als {@link String}, der in die Spielverlaufs-Liste geschrieben wird.
	 */
	protected String redoDraw() {
/*		String lastRedoneDraw1 = redoPlayer1.pop();
		String lastRedoneDraw2 = redoPlayer2.pop();
		
		gamePlayer1.push(lastRedoneDraw1);
		gamePlayer2.push(lastRedoneDraw2);
		
		return lastRedoneDraw1 + ";" + lastRedoneDraw2; */
		return "";
	}
	
	/**
	 * Gibt den Stack für die Züge des ersten Spielers als String zurück.
	 * @return die ArrayList für die Züge des ersten Spielers als String
	 */
	protected String getDraws() {
		return game.toString();
	}
	
	/**
	 * Gibt den Stack für die rückgängig gemachten Züge des ersten Spielers zurück.
	 * @return der Stack für die rückgängig gemachten Züge des ersten Spielers
	 */
	protected String getRedoneDraws() {
		return redo.toString();
	}
	
	/**
	 * Die beiden Instanz-Variablen, die die Züge der Spieler speichern,
	 * speichern hiermit ihren Inhalt in der Datei
	 * @param gameName Name des Spiels zur Zuordnung
	 */
	protected void saveToFile(String gameName) {
//		saveload.setDrawHistoryPlayerOne(gameName, gamePlayer1.toString().replaceAll(", ", drawSeparator).replaceAll("]", "").substring(1));
//		saveload.setDrawHistoryPlayerTwo(gameName, gamePlayer2.toString().replaceAll(", ", drawSeparator).replaceAll("]", "").substring(1));
	}
	
	/**
	 * Leert alle Stacks.
	 * Alle getätigten Züge werden gelöscht,
	 * sie werden NICHT in der Datei gespeichert.
	 * Um die Daten zu speichern sollte vorher die Methode
	 * <i>.saveToFile()</i> aufgerufen werden.
	 */
	protected void clear() {
		game.clear();
		redo.clear();
	}
	
	/**
	 * main method
	 * @param args arguments
	 */
	public static void main(String[] args) {
		UndoRedo ur = new UndoRedo();
		ur.newDraw("eins1", 1);
		ur.newDraw("zwei1", 1);
		ur.newDraw("drei1", 1);
		ur.newDraw("eins2", 2);
		ur.newDraw("zwei2", 2);
		ur.newDraw("eins_1", 1);
		ur.newDraw("zwei_1", 1);
		ur.newDraw("eins_2", 2);
		System.out.println(ur.getDraws());
		System.out.println(ur.undoDraw(1));
		System.out.println(ur.getDraws());
		
	}
	
}
