package shipz.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

/**
 * Diese Klasse ist für die Punktevergabe zuständig,
 * außerdem für das Speichern der Punkte in der Datei
 * und zusätzlich für das Verwalten und Generieren der Highscore-Liste.
 * @author Florian Osterberg
 */
public class Score {

	// IV
	/** SaveLoad-Objekt, das benötigt wird, um einige Methoden auszuführen. */
	private SaveLoad saveload;
	/** Highscore-File */
	private File highscoreFile;
	/** Scanner, der aus einer Datei liest. */
	private Scanner scanner;
	/** Zählt die Combos des ersten Spielers */
	private double comboPlayer1;
	/** Zählt die Combos des zweiten Spielers */
	private double comboPlayer2;
	/** Trennzeichen zwischen Namen und Punkten.
	 * Format: <i>spielername=punkte</i> */
	private String scoreSeparator = "=";
	/** Punktestand des ersten Spielers */
	private int scorePlayer1;
	/** Punktestand des zweiten Spielers */
	private int scorePlayer2;
	
	// Konstruktor
	/**
	 * Konstruktor von Score.
	 * Die IVs werden initialisiert und die Highscore-Datei wird erstellt.
	 */
	public Score() {
		saveload = new SaveLoad();
		highscoreFile = new File(saveload.fileDirectory() + File.separator + "highscore.shipz");
		saveload.makeDirectory(highscoreFile);
		comboPlayer1 = 1;
		comboPlayer2 = 1;
		scorePlayer1 = 0;
		scorePlayer2 = 0;
	}
	
	// IM
	/**
	 * Setzt die Punktzahl eines bestimmten Spielers zu einem bestimmten Event.
	 * @param playerName Name des Spielers
	 * @param result 0 für wasser, 1 für treffer, 2 für versenkt, 3 für undo
	 * @param event Events: <b>u</b> für undo, <b>h</b> für hit, <b>s</b> für sink <i>(weitere folgen eventuell. Das liegt nicht in meiner Hand)</i>
	 */
	protected void setScore(int playerIndex, byte result) {
		switch(result) {
		case 3:
			if(playerIndex == 1) {
				scorePlayer1 -= 30;
			} else if(playerIndex == 2){
				scorePlayer2 -= 30;
			} else System.err.println("Fehler! \nUnzulässiger playerIndex. \n1 oder 2 erlaubt.");
			break;
		case 1:
			combo(playerIndex, result);
			if(playerIndex == 1) scorePlayer1 += 50*comboPlayer1;
			else if(playerIndex == 2) scorePlayer2 += 50*comboPlayer2;
			break;
		case 2:
			combo(playerIndex, result);
			if(playerIndex == 1) scorePlayer1 += 300*comboPlayer1;
			else if(playerIndex == 2) scorePlayer2 += 300*comboPlayer2;
			break;
		default: break;
		}
	}
	
	/**
	 * Methode, die die Combos verwaltet und den Combo-Counter hochzählt.
	 * @param playerName Spielername
	 * @param event Aktion, die sich auf den Combowert beeinflusst. 
	 * @param result 0 für wasser, 1 für treffer, 2 für versenkt, 3 für undo
	 * Zugelassene Werte: <b>h</b> für einen Treffer, <b>s</b> für ein versenktes Schiff.
	 */
	private void combo(int playerIndex, byte result) {
		if(playerIndex == 1) {
			if(result == 1) {
				comboPlayer1 += 1;
				comboPlayer2 = 1;
			} else if (result == 2){
				comboPlayer1 = comboPlayer1*2;
				comboPlayer2 = 1;
			} else if(result == 0) {
				comboPlayer1 = 1;
			}
		} else if(playerIndex == 2) {
			if(result == 1) {
				comboPlayer2 += 1;
				comboPlayer1 = 1;
			} else if (result == 2) {
				comboPlayer2 = comboPlayer2*2;
				comboPlayer1 = 1;
			} else if(result == 0) {
				comboPlayer2 = 1;
			}
		} else {
			System.err.println("Fehler! \nUnzulässiger playerIndex. \n1 oder 2 erlaubt.");
		}
		
	}
	
	/**
	 * Ein bestimmter Spieler wird in die Highscore-Liste eingefügt.
	 * @param playerName Der Spieler, der eingefügt werden soll.
	 * @param score Punktzahl, die gesetzt werden soll.
	 */
	private void addPlayerIntoHighscore(String playerName, int score) {
		if(!doesPlayerExist(playerName)) {
			saveload.writeFile(highscoreFile, saveload.readFile(highscoreFile)+playerName+"#"+saveload.timestamp()+scoreSeparator+score);
		} else {
			addPlayerIntoHighscore(playerName+"~", score);
		}
	}
	
	/**
	 * Überprüft, ob ein Spieler bereits in der Score-Datei steht.
	 * @param playerName Der Spieler nach dem gesucht wird
	 * @return Steht der Spieler bereits in der Score-Datei?
	 */
	private boolean doesPlayerExist(String playerName) {
		try {
			scanner = new Scanner(highscoreFile);
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		}
		
		boolean exists = false;
		while(exists == false && scanner.hasNextLine()) {
			String str = scanner.nextLine();
			
			if(str.startsWith(playerName+scoreSeparator)) {
				exists = true;
			}
		}
		return exists;
	}
	
	/**
	 * Liest alle Punktzahlen aus dem Highscore-File
	 * und erstellt eine absteigend sortierte Highscore-Liste.
	 * Format: "spieler1=punkte1,spieler2=punkte2,spieler3=punkte3,..."
	 * @return Die Highscore-Liste als String
	 */
	protected String highscore() {
		String str = highscoreToSortedMap().toString().replaceAll(" ", "").replaceAll("}", "").substring(1);
		String[] a = str.split(",");
		String result = "";
		if(a.length < 10) {
			for(int i = 0; i < a.length; i++) {
				if(a[i].contains((CharSequence)"#")) {
					String[] temp = a[i].split("=");
					result += a[i].split("#")[0] + "=" + temp[1] + ",";
				} else {
					result += a[i] + ",";
				}
			}
		} else {
			for(int i = 0; i < 10; i++) {
				if(a[i].contains((CharSequence)"#")) {
					String[] temp = a[i].split("=");
					result += a[i].split("#")[0] + "=" + temp[1] + ",";
				} else {
					result += a[i] + ",";
				}
			}
		}
		return result;
	}

	/**
	 * Diese Methode lädt alle Einträge aus der Highscore-Datei.
	 * Die Einträge werden dann sortiert und in einer TreeMap
	 * zurückgegeben. Dies ist notwendig, um einen String zu generieren,
	 * der den Highscore repräsentiert.
	 * @return die TreeMap mit den sortierten Einträgen
	 */
	private Map<String, Integer> highscoreToSortedMap() {
		try {
			scanner = new Scanner(highscoreFile);
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		}
		
		String line = "";
		ArrayList<String> result = new ArrayList<>();
		
		while(scanner.hasNextLine()) {
			line = scanner.nextLine();
			String[] s = line.split(scoreSeparator);
			result.add(s[0]);
			result.add(s[1]);
		}
		
		TreeMap<String, Integer> map = new TreeMap<>();
		
		ArrayList<String> h = result;
		for(int i = 0; i < h.size(); i++) {
			map.put(h.get(i), Integer.parseInt(h.get(i+1)));
			i++;
		}
		
		TreeMap<String, Integer> tm = map;
		
		Comparator<String> comp = new Comparator<String>() {
			public int compare(String a, String b) {
				int result = tm.get(b).compareTo(tm.get(a));
				if(result == 0)
					return 1;
				else
					return result;
			}
		};
		
		Map<String, Integer> sortedMap = new TreeMap<>(comp);
		sortedMap.putAll(tm);
		return sortedMap;
	}
	
	/**
	 * Löscht alle Daten, die nicht im Highscore stehen,
	 * und somit nicht mehr benötigt werden.
	 * 
	 * Noch nicht vollständig und korrekt implementiert,
	 * daher noch nicht zur Benutzung geeignet.
	 */
	private void cleanHighscoreFile() {
		String str = highscoreToSortedMap().toString().replaceAll(" ", "").replaceAll("}", "").substring(1);
		String[] a = str.split(",");
		String result = "";
		if(a.length < 10) {
			for(int i = 0; i < a.length; i++) {
				result += a[i] + ",";
			}
		} else {
			for(int i = 0; i < 10; i++) {
				result += a[i] + ",";
			}
		}
		saveload.writeFile(highscoreFile, result.replaceAll(",", "\n"));
	}
	
	/**
	 * Wenn das Spiel vorbei ist, wird diese Methode ausgeführt,
	 * damit die Punkte in der Datei abgespeichert werden.
	 * @param gameName 
	 * @param playerName
	 * @param opponentName
	 */
	protected void saveScoreToFile(String playerName, String opponentName) {
		addPlayerIntoHighscore(playerName, scorePlayer1);
		addPlayerIntoHighscore(opponentName, scorePlayer2);
		cleanHighscoreFile();
	}
	
	/**
	 * Gibt die Punktzahl eines Spielers zurück.
	 * @param 1 = Spieler1, 2 = Spieler2
	 * @return Punktzahl des Spielers
	 */
	protected int getScore(int playerIndex) {
		switch(playerIndex) {
		case 1: return scorePlayer1;
		case 2: return scorePlayer2;
		default: System.err.println("Fehler! \nUnzulässiger playerIndex. \n1 oder 2 erlaubt."); return 0;
		}
	}
	
	/**
	 * Der Wert der Combo wird zurückgegeben,
	 * damit die GUI darstellen kann, welche Combo der Spieler
	 * aktuell erreicht hat.
	 * @param playerIndex
	 * @return
	 */
	protected double getComboValue(int playerIndex) {
		switch(playerIndex) {
		case 1: return comboPlayer1;
		case 2: return comboPlayer2;
		default: System.err.println("Fehler! \nUnzulässiger playerIndex. \n1 oder 2 erlaubt."); return 0.0;
		}
	}
	
	/**
	 * main method
	 * @param args arguments
	 */
	public static void main(String[] args) {
		Score s = new Score();
/*		s.setScore(1, 's');
		s.setScore(1, 's');
		s.setScore(1, 's');
		s.setScore(1, 's');
		s.setScore(2, 's');
		s.setScore(2, 's');
		s.setScore(2, 's');*/
//		s.saveScoreToFile("TestSpieler1", "TestSpieler2");
		System.out.println(s.highscore());
		
	}

}
