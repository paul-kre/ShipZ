package shipz.io;

import java.util.Stack;

import shipz.util.NoDrawException;

/**
 * Diese Klasse speichert alle Züge die getätigt werden und behandelt die Undo-Redo-Funktion.
 * @author Florian Osterberg
 */
public class UndoRedo {

	// IV
	/** In diesem Stack werden alle Züge als Strings gespeichert. */
	private Stack<String> draws;
	/** In diesem Stack werden alle rückgängig gemachten Züge als Strings gespeichert. */
	private Stack<String> redoneDraws;
	/** SaveLoad-Objekt */
	private SaveLoad saveload;
	
	// Konstanten
	/** Der String, der einzelne Züge in der Datei trennt. */
	private static final String DRAW_SEPARATOR = ";";
	
	// Konstruktor
	/**
	 * Initialisiert ein UndoRedo-Objekt.
	 * Die Stacks werden initialisiert.
	 */
	UndoRedo() {
		draws = new Stack<String>();
		redoneDraws = new Stack<String>();
		saveload = new SaveLoad();
	}
	
	// IM
	/**
	 * Wenn ein neuer Zug getätigt wird, wird dieser in den String, der den Spielverlauf speichert, geschrieben.
	 * Format des fertigen Strings:
	 * <b>playerIndex</b>/<b>x</b>,<b>y</b>/<b>result</b>
	 * @param x x-Koordinate des Zugs
	 * @param y y-Koordinate des Zugs
	 * @param playerIndex 1 für den ersten Spieler, 2 für den zweiten Spieler
	 * @param result 0=wasser, 1=treffer, 2=versenkt, 3=undo
	 */
	void newDraw(int x, int y, int playerIndex, int result) {
		draws.push(playerIndex + "/" + x + "," + y + "/" + result); // Zug wird auf den Stack gelegt
	}
	
	/**
	 * Der letzte Zug wird rückgängig gemacht.
	 * Er wird dafür aus der Liste, die den Spielverlauf speichert gelöscht und in eine
	 * separate Liste geschrieben, die die rückgängig gemachten Züge speichert.
	 * Falls ein Redo ausgeführt wird, wird auf eben diese Liste zurückgegriffen.
	 * @param playerIndex 1 für den ersten Spieler, 2 für den zweiten Spieler
	 * @return Die letzten Züge der Spielverlaufs-Liste, der in die Redoliste geschrieben wird
	 * @throws NoDrawException tritt auf falls keine weiteren Züge auf dem Stack sind die rückgängig gemacht werden können.
	 */
	String undoDraw(int playerIndex) throws NoDrawException {
		String result = "";
		String draw = "";
		while(!(draw.startsWith(playerIndex+""))) {
			if(!draws.isEmpty()) {
				draw = draws.pop();
				redoneDraws.push(draw);
				result += draw + DRAW_SEPARATOR;
			} else {
				throw new NoDrawException();
			}
		}
		return result;
	}
	
	/**
	 * Der letzte Eintrag aus der Redo-Liste wird gelöscht und wieder in die Liste geschrieben,
	 * die den Spielverlauf speichert.
	 * Der zuletzt rückgängig gemachte Zug wird also ausgeführt.
	 * @param playerIndex 1 für den ersten Spieler, 2 für den zweiten Spieler
	 * @return Der letzte Zug der Redoliste als {@link String}, der in die Spielverlaufs-Liste geschrieben wird.
	 * @throws NoDrawException tritt auf, falls keine weiteren Züge auf dem Stack sind, die wiederholt werden können.
	 */
	String redoDraw(int playerIndex) throws NoDrawException {
		String result = "";
		String draw = "";
		while(!(draw.startsWith(playerIndex+""))) {
			if(!redoneDraws.empty()) {
				draw = redoneDraws.pop();
				draws.push(draw);
				result += draw + DRAW_SEPARATOR;
			} else {
				throw new NoDrawException();
			}
		}
		return result;
	}
	
	/**
	 * Gibt den Stack für die Züge des ersten Spielers als String zurück.
	 * @return die ArrayList für die Züge als String
	 */
	String getDraws() {
		return draws.toString();
	}
	
	/**
	 * Gibt den Stack für die rückgängig gemachten Züge des ersten Spielers zurück.
	 * @return der Stack für die rückgängig gemachten Züge
	 */
	String getRedoneDraws() {
		return redoneDraws.toString();
	}
	
	/**
	 * Die Instanz-Variable, die die Züge der Spieler speichert,
	 * speichert hiermit ihren Inhalt in der Datei
	 * @param gameName Name des Spiels zur Zuordnung
	 */
	void saveToFile(String gameName) {
		saveload.setDraws(gameName, draws.toString().replaceAll(", ", DRAW_SEPARATOR).replaceAll("]", "").substring(1));
		saveload.setRedoneDraws(gameName, redoneDraws.toString().replaceAll(", ", DRAW_SEPARATOR).replaceAll("]", "").substring(1));
	}
	
	/**
	 * Leert den Stack, der die getätigten Züge speichert und lädt die Züge aus einem Spielstand in die Instanz-Variable.
	 * Wird verwendet, wenn ein Spielstand geladen wird.
	 * @param gameName Name des Spielstands
	 */
	void loadDraws(String gameName) {
		draws.clear();
		
		if(saveload.getDraws(gameName) != "") {
			String[] draws = saveload.getDraws(gameName).split(DRAW_SEPARATOR);
			int x = 0, y = 0, playerIndex = 0;
			byte result = 0;
			for(int i = 0; i < draws.length; i++) {
				x = Integer.parseInt(draws[i].split("/")[1].split(",")[0]);
				y = Integer.parseInt(draws[i].split("/")[1].split(",")[1]);
				result = Byte.parseByte(draws[i].split("/")[2]);
				playerIndex = Integer.parseInt(draws[i].split("/")[0]);
				newDraw(x, y, playerIndex, result);
			}
		}
		
	}
	
	/**
	 * Leert den Stack, der die rückgängig gemachten Züge speichert und lädt die Züge aus einem Spielstand in die Instanz-Variable.
	 * Wird verwendet, wenn ein Spielstand geladen wird.
	 * @param gameName Name des Spielstands
	 */
	void loadRedoneDraws(String gameName) {
		redoneDraws.clear();
		
		if(saveload.getRedoneDraws(gameName) != "") {
			String[] redoneDraws = saveload.getRedoneDraws(gameName).split(DRAW_SEPARATOR);
			int x = 0, y = 0, playerIndex = 0;
			byte result = 0;
			
			for(int i = 0; i < redoneDraws.length; i++) {
				x = Integer.parseInt(redoneDraws[i].split("/")[1].split(",")[0]);
				y = Integer.parseInt(redoneDraws[i].split("/")[1].split(",")[1]);
				result = Byte.parseByte(redoneDraws[i].split("/")[2]);
				playerIndex = Integer.parseInt(redoneDraws[i].split("/")[0]);
				this.redoneDraws.push(playerIndex + "/" + x + "," + y + "/" + result);
			}
		}
	}
	
}
