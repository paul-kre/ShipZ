package shipz.io;

import java.io.File;
import java.io.FileNotFoundException;
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
	private int comboPlayer1;
	/** Zählt die Combos des zweiten Spielers */
	private int comboPlayer2;
	/** Punktestand des ersten Spielers */
	private int scorePlayer1;
	/** Punktestand des zweiten Spielers */
	private int scorePlayer2;
	
	// Konstanten
	/** Trennzeichen zwischen Namen und Punkten.
	 * Format: <i>spielername=punkte</i> */
	private final static String SCORE_SEPARATOR = "=";
	
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
	 * @param result <b>0</b> entspricht einem Fehltreffer, <b>1</b> für einen Treffer, <b>2</b> für ein versenktes Schiff, <b>3</b> für Undo
	 */
	protected void setScore(int playerIndex, int result) {
		switch(result) {
		case 1:
			combo(playerIndex, result);
			if(playerIndex == 1) scorePlayer1 += 100*comboPlayer1;
			else if(playerIndex == 2) scorePlayer2 += 100*comboPlayer2;
			else throw new RuntimeException("Unzulässiger playerIndex, erlaubt ist 1 oder 2.");
			break;
		case 2:
			combo(playerIndex, result);
			if(playerIndex == 1) scorePlayer1 += 300*comboPlayer1;
			else if(playerIndex == 2) scorePlayer2 += 300*comboPlayer2;
			else throw new RuntimeException("Unzulässiger playerIndex, erlaubt ist 1 oder 2.");
			break;
		case 3:
			if(playerIndex == 1) scorePlayer1 -= 50;
			else if(playerIndex == 2)scorePlayer2 -= 50;
			else throw new RuntimeException("Unzulässiger playerIndex, erlaubt ist 1 oder 2.");
			break;
		default: break;
		}
	}
	
	/**
	 * Methode, die die Combos verwaltet und den Combo-Counter hochzählt.
	 * @param playerName Spielername
	 * @param result 0 für wasser, 1 für treffer, 2 für versenkt, 3 für undo
	 */
	private void combo(int playerIndex, int result) {
		if(playerIndex == 1) {
			if(result == 1) {
				comboPlayer1 += 1;
				comboPlayer2 = 1;
			} else if (result == 2){
				comboPlayer1 = comboPlayer1*2;
				comboPlayer2 = 1;
			} else if(result == 0 || result == 3) {
				comboPlayer1 = 1;
			}
		} else if(playerIndex == 2) {
			if(result == 1) {
				comboPlayer2 += 1;
				comboPlayer1 = 1;
			} else if (result == 2) {
				comboPlayer2 = comboPlayer2*2;
				comboPlayer1 = 1;
			} else if(result == 0 || result == 3) {
				comboPlayer2 = 1;
			}
		} else {
			throw new RuntimeException("Unzulässiger playerIndex, erlaubt ist 1 oder 2.");
		}
	}
	
	/**
	 * Ein bestimmter Spieler wird in die Highscore-Liste eingefügt.
	 * @param playerName Der Spieler, der eingefügt werden soll.
	 * @param score Punktzahl, die gesetzt werden soll.
	 */
	private void addPlayerIntoHighscore(String playerName, int score) {
		if(!doesPlayerExist(playerName)) {
			saveload.writeFile(highscoreFile, saveload.readFile(highscoreFile)+playerName+"#"+saveload.timestamp()+SCORE_SEPARATOR+score);
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
			
			if(str.startsWith(playerName+SCORE_SEPARATOR)) {
				exists = true;
			}
		}
		return exists;
	}
	
	/**
	 * Liest alle Punktzahlen aus dem Highscore-File
	 * und erstellt eine absteigend sortierte Highscore-Liste.
	 * Format: "spieler1=punkte1,spieler2=punkte2,spieler3=punkte3,..."
	 * @param max Limit, wie viele Spieler im Highscore angezeigt werden (Standardwert 10). Kann vom Spieler
	 * in den Einstellungen festgelegt werden.
	 * @return Die Highscore-Liste als String
	 */
	protected String highscore(int max) {
		String str = highscoreToSortedMap().toString().replaceAll(" ", "").replaceAll("}", "").substring(1);
		String[] a = str.split(",");
		String result = "";
		if(a.length < max) { // wenn weniger als zehn Spieler im Highscore stehen
			for(int i = 0; i < a.length; i++) { // ... werden je nach dem wie viele es sind an den String angehangen
				result += a[i] + ",";
			}
		} else { // wenn mehr als 10 Spieler im Highscore stehen
			for(int i = 0; i < max; i++) { // werden nur die ersten zehn an den String angehangen
				result += a[i] + ",";
			}
		}
		return result;
	}

	/**
	 * Diese Methode lädt alle Einträge aus der Highscore-Datei.
	 * Diese werden dann sortiert und in eine TreeMap geschrieben.
 	 * Dies ist notwendig, um einen String zu generieren,
	 * der den Highscore wiedergibt.
	 * @return die TreeMap mit den sortierten Einträgen
	 */
	private Map<String, Integer> highscoreToSortedMap() {
		// Scanner-Objekt zum Lesen der Highscore-Datei
		try {
			scanner = new Scanner(highscoreFile);
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		}
		
		String line = "";
		TreeMap<String, Integer> map = new TreeMap<>(); // TreeMap, die die Highscore-Einträge als KeyValue-Paare speichert
		
		while(scanner.hasNextLine()) {
			line = scanner.nextLine();
			String[] s = line.split(SCORE_SEPARATOR);
			map.put(s[0], Integer.parseInt(s[1]));
		}
		
		// Comparator, vergleicht die Punktzahlen und ordnet die Einträge nach Punktzahlen
		Comparator<String> comp = new Comparator<String>() { // anonymer Funktionsaufruf, Redefinierung einer Methode eines Interfaces
			/**
			 * Vergleicht die Values (Punkte) zweier Keys (Spielernamen)
			 * miteinander.
			 * @param a erster Key/ Spielername
			 * @param b zweiter Key/ Spielername
			 */
			public int compare(String a, String b) {
				int result = map.get(b).compareTo(map.get(a));
				if(result == 0)
					return 1;
				else
					return result;
			}
		};
		
		Map<String, Integer> sortedMap = new TreeMap<>(comp); // neue TreeMap, die mit dem oben definierten Comparator arbeitet
		sortedMap.putAll(map); // Einträge des Highscores werden durch das Einfügen sofort sortiert, da der Comparator im Konstruktor übergeben wurde
		return sortedMap;
	}
	
	/**
	 * Löscht alle Daten, die nicht im Highscore stehen,
	 * und somit nicht mehr benötigt werden.
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
		default: throw new RuntimeException("Unzulässiger playerIndex, erlaubt ist 1 oder 2.");
		}
	}
	
	/**
	 * Der Wert der Combo wird zurückgegeben,
	 * damit die GUI darstellen kann, welche Combo der Spieler
	 * aktuell erreicht hat.
	 * @param playerIndex 1 = Spieler1, 2 = Spieler2
	 * @return Combo-Wert des Spielers
	 */
	protected int getComboValue(int playerIndex) {
		switch(playerIndex) {
		case 1: return comboPlayer1;
		case 2: return comboPlayer2;
		default: throw new RuntimeException("Unzulässiger playerIndex, erlaubt ist 1 oder 2.");
		}
	}
	
	/**
	 * Lädt die Züge aus einer Datei und aktualisiert damit die Punkte-
	 * und Combo-Werte. Diese Methode wird für das Laden von Spielständen benötigt.
	 * @param gameName Name des Spielstands
	 */
	protected void loadScore(String gameName) {
		String[] draws = saveload.getDraws(gameName).split(";");
		int playerIndex, result;
		for(int i = 0; i < draws.length; i++) {
			playerIndex = Integer.parseInt(draws[i].split("|")[0]);
			result = Integer.parseInt(draws[i].split("|")[2]);
			setScore(playerIndex, result);
		}
	}
	
	/**
	 * So ähnlich wird der Highscore in der Game-Klasse für die GUI dargestellt.
	 */
	private void test() {
		System.out.println("RANK\tNAME\t\t\tSCORE\t\tDATE");
		String[] highscoreArray = highscore(10).split(",");
		for(int i = 0; i < highscoreArray.length; i++) {
			String score = highscoreArray[i].split("=")[1];
			String name = highscoreArray[i].split("=")[0].split("#")[0];
			String date = highscoreArray[i].split("=")[0].split("#")[1].replaceAll("_", " ").replaceAll("-", ":");
			System.out.println(i+1 + "\t" + name + "\t\t" + score + "\t\t" + date);
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
		System.out.println(s.highscore(10));
		s.test();
		
	}

}
