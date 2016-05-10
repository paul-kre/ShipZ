package shipz.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Scanner;

import javax.swing.JFileChooser;

/**
 * Diese Klasse verwaltet die Speicherprozesse der anderen Klassen.
 * @author Florian Osterberg
 */
public class SaveLoad {

	// IV
	/** String, der den Namen des ersten Spielers bzw. des Hosts speichert. */
//	private String playerName;
	/** String, der die gesamte Information über einen Spielstand speichert. */
//	private String savegame;
	/** String, der die Informationen über das Spielfeld speichert, also was sich an welcher Position befindet.*/
//	private String board;
	/** Datei, in der die einzelnen Spielstände gespeichert werden. */
	private File file;
	/** Writer, der in eine Datei schreibt. */
	private BufferedWriter writer;
	/** Scanner, der eine Datei liest. */
	private Scanner scanner;
	/** Trennzeichen zwischen den einzelnen Spielständen. */
	private String separator = "~~~~~";

	//Konstruktor
	/**
	 * Konstruktor der Klasse, der das File-Objekt initialisiert 
	 * und diesem einen Dateipfad zuordnet.
	 */
	public SaveLoad() {
		file = new File(userDirectory() + File.separator + "shipZ" + File.separator + "saves.shipz");
		makeDirectory(file);
	}
	
	// IM
	/**
	 * Methode, die alle benötigten Informationen über ein Spiel in einen String speichert,
	 * der dafür da ist, einen Spielstand zu speichern.
	 * Dieser String wird dann in die Speicher-Datei geschrieben.
	 * @param fileName Dateiname für den Spielstand
	 * @param playerName Name des ersten Spielers
	 * @param opponentName Name des zweiten Spielers bzw. des Gegners
	 * @param boardSize Feldgröße
	 * @param board Das gesamte Feld als {@link String} gespeichert.
	 * @param drawHistoryPlayer1 Der Spielverlauf des ersten Spielers als {@link String} gespeichert.
	 * @param drawHistoryPlayer2 Der Spielverlauf des zweiten Spielers als {@link String} gespeichert.
	 */
	protected void newGame(String fileName, String playerName, String opponentName, int boardSize, String board, String drawHistoryPlayer1, String drawHistoryPlayer2) {
		String savegame = 
				"gameName:" + fileName + ":" // Eindeutiger Name des Spielstands
				+ "player:" + playerName + ":"
				+ "time:" + timestamp() + ":"
				+ "opponent:" + opponentName + ":"
				+ "boardsize:" + boardSize + ":"
				+ "board:" + board + ":"
				+ "drawHistoryPlayer1:" + drawHistoryPlayer1 + ":"
				+ "drawHistoryPlayer2:" + drawHistoryPlayer2 + ":"
				+ separator + "\n"; // Trennzeichen, damit erkannt wird, wann ein Spielstand zu Ende ist
		
		if(doesFileExist(fileName) == false) {
			String s = readFile();
			writeFile(s+savegame);
//			appendToFile(file, savegame);
		} else {
			System.err.println("Fehler beim Erstellen eines neuen Spielstands! Dieser Spielstand existiert bereits.");
		}
	}
	
	/**
	 * Falls die Dateien noch nicht vorhanden sind, werden sie erstellt.
	 * @param file Datei, die erstellt werden soll.
	 */
	protected void makeDirectory(File file) {
		if(!(file.exists())) {
			try{
				new File(userDirectory() + File.separator + "shipZ").mkdir();
			} catch (SecurityException x) {
				x.printStackTrace();
			}
			
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Leert die gesamte Datei, die die Spielstände speichert.
	 */
	protected void clearFile() {
		writeFile("");
	}
	
	/**
	 * Leert eine gesamte Datei.
	 * @param file die zu leerende Datei
	 */
	protected void clearFile(File file) {
		writeFile(file, "");
	}
	
	/**
	 * Gibt die Namen aller Spielstände als {@link String} zurück.
	 * Dies wird für die Auflistung aller Spielstände wichtig sein.
	 * Da die Weitergabe von Arrays nicht erlaubt ist,
	 * muss die Game-Klasse selbst aus dem String ein Array machen.
	 * Dies geht ganz einfach mit 
	 * getAllGameNames().split(",")
	 * @return Die Namen aller Spielstände als {@link String}
	 */
	protected String getAllGameNames() {
		String[] a = readFile().replaceAll("\n", "").split(separator);
		String s = "";
		for(int i = 0; i < a.length; i++) {
			s += a[i];
		}
		String[] b = s.split(":");
		String result = "";
		
		for(int i = 0; i < b.length; i++) {
			if(b[i].equalsIgnoreCase("gameName")) {
				result += b[i+1] + ",";
			}
		}
		
		return result;
	}
	
	/**
	 * Diese Methode lädt aus dem gespeicherten Spielstand des Spielers das gespeicherte Spielfeld heraus.
	 * Dieses Spielfeld wird dann als {@link String} zurückgegeben.
	 * @param fileName Dateiname zur Identifizierung des gespeicherten Spielstands.
	 * @return Das geladene Spielfeld
	 */
	protected String getBoard(String fileName) {
		return getGame(fileName).replaceAll("\n", "").split(":")[11];
	}
	
	/**
	 * Gibt den Spielernamen eines Speicherstands zurück.
	 * @param fileName der gewünschte Speicherstand
	 * @return Spielername eines Speicherstands
	 */
	protected String getPlayerName(String fileName) {
		return getGame(fileName).replaceAll("\n", "").split(":")[3];
	}
	
	/**
	 * Gibt den Namen des Gegners eines bestimmten Speicherstands zurück.
	 * @param fileName der gewünschte Speicherstand
	 * @return Namen des Gegners eines bestimmten Speicherstands
	 */
	protected String getOpponentName(String fileName) {
		return getGame(fileName).replaceAll("\n", "").split(":")[7];
	}
	
	/**
	 * Gibt die Feldgröße eines bestimmten Speicherstands zurück.
	 * @param fileName der gewünschte Speicherstand
	 * @return Feldgröße eines bestimmten Speicherstands
	 */
	protected int getBoardsize(String fileName) {
		return Integer.parseInt(getGame(fileName).replaceAll("\n", "").split(":")[9]);
	}
	
	/**
	 * Gibt den String aus einem Spielstand zurück, der die Spielzüge des ersten Spielers speichert.
	 * @param fileName der gewünschte Speicherstand
	 * @return Die Spielzüge als {@link String}
	 */
	protected String getDrawHistoryPlayer1(String fileName) {
		return getGame(fileName).replaceAll("\n", "").split(":")[13];
	}
	
	/**
	 * Gibt den String aus einem Spielstand zurück, der die Spielzüge des zweiten Spielers speichert.
	 * @param fileName der gewünschte Speicherstand
	 * @return Die Spielzüge als {@link String}
	 */
	protected String getDrawHistoryPlayer2(String fileName) {
		return getGame(fileName).replaceAll("\n", "").split(":")[15];
	}
	
	/**
	 * Gibt die gespeicherte Uhrzeit eines Speicherstands zurück.
	 * @param fileName der gewünschte Speicherstand
	 * @return die gespeicherte Uhrzeit als {@link String}
	 */
	protected String getTime(String fileName) {
		return getGame(fileName).replaceAll("\n", "").split(":")[5];
	}
	
	/**
	 * Gibt den gesamten Inhalt eines bestimmten Speicherstands zurück.
	 * Veraltete Methode, wird bald gelöscht.
	 * @param fileName der gewünschte Speicherstand
	 * @return Der gesamte Speicherstand als {@link String}
	 */
	
	@Deprecated
	protected String getFile(String fileName) {
		String r = "";
		
		String[] a = readFile().split("~~~~~");
		String[] b;
		for(int i = 0; i < a.length-1; i++) {
			b = a[i].split(":");
			if(b[1].equalsIgnoreCase(fileName)) {
				r = a[i];
			}
		}
		return r;
	}
	
	/**
	 * Diese Methode speichert das Spielfeld, das als Parameter übergeben wird in den Speicherstand fileName.
	 * @param fileName der Spielstand, bei dem das Spielbrett abgespeichert werden soll.
	 * @param board das Spielbrett als {@link String}
	 */
	protected void setBoard(String fileName, String board) {
		writeFile(readFile().replaceAll(getGame(fileName), getGame(fileName).replaceAll("board:" + getBoard(fileName) + ":", "board:" + board + ":")));
	}
	
	/**
	 * Ändert den Namen des ersten Spielers zu playerName im Spielstand fileName.
	 * @param fileName der Spielstand, bei dem der Name des ersten Spielers geändert werden soll.
	 * @param playerName der neue Name des ersten Spielers
	 */
	protected void setPlayerName(String fileName, String playerName) {
		writeFile(readFile().replaceAll(getGame(fileName), getGame(fileName).replaceAll("player:" + getPlayerName(fileName) + ":", "player:" + playerName + ":")));
	}
	
	/**
	 * Ändert den Namen des Gegners eines Spielstands.
	 * @param fileName der Spielstand
	 * @param opponentName
	 */
	protected void setOpponentName(String fileName, String opponentName) {
		writeFile(readFile().replaceAll(getGame(fileName), getGame(fileName).replaceAll("opponent:" + getOpponentName(fileName) + ":", "opponent:" + opponentName + ":")));
	}
	
	/**
	 * Ändert die Feldgröße eines Spielstands.
	 * @param fileName der Spielstand
	 * @param boardSize
	 */
	protected void setBoardsize(String fileName, int boardSize) {
		writeFile(readFile().replaceAll(getGame(fileName), getGame(fileName).replaceAll("boardsize:" + getBoardsize(fileName) + ":", "boardsize:" + boardSize + ":")));
	}
	
	/**
	 * Ändert den Spielverlauf des ersten Spielers in einem Spielstand.
	 * @param fileName der Spielstand
	 * @param drawHistoryPlayer1 der neue Spielverlauf des ersten Spielers
	 */
	protected void setDrawHistoryPlayer1(String fileName, String drawHistoryPlayer1) {
		writeFile(readFile().replaceAll(getGame(fileName), getGame(fileName).replaceAll("drawHistoryPlayer1:" + getDrawHistoryPlayer1(fileName) + ":", "drawHistoryPlayer1:" + drawHistoryPlayer1 + ":")));
	}
	
	/**
	 * Ändert den Spielverlauf des zweiten Spielers in einem Spielstand.
	 * @param fileName der Spielstand
	 * @param drawHistoryPlayer2 der neue Spielverlauf des zweiten Spielers
	 */
	protected void setDrawHistoryPlayer2(String fileName, String drawHistoryPlayer2) {
		writeFile(readFile().replaceAll(getGame(fileName), getGame(fileName).replaceAll("drawHistoryPlayer2:" + getDrawHistoryPlayer2(fileName) + ":", "drawHistoryPlayer2:" + drawHistoryPlayer2 + ":")));
	}
	
	/**
	 * Aktualisiert die Uhrzeit eines Spielstands.
	 * @param fileName der Spielstand
	 */
	protected void updateTime(String fileName) {
		writeFile(readFile().replaceAll(getGame(fileName), getGame(fileName).replaceAll("time:" + getTime(fileName) + ":", "time:" + timestamp() + ":")));
	}
	
	/**
	 * Überprüft ob ein Speicherstand mit bestimmtem Namen vorhanden ist.
	 * @param fileName der gewünschte Speicherstand
	 * @return Ist der Speicherstand vorhanden?
	 */
	protected boolean doesFileExist(String fileName) {
		boolean exists = false;
		String[] a = readFile().split(separator);
		String[] b;
		
		for(int i = 0; i < a.length-1; i++) {
			b = a[i].split(":");
			if(b[1].equalsIgnoreCase(fileName)) {
				exists = true;
			}
		}
		return exists;
	}
	
	/**
	 * Liest aus einer Datei den gesamten Inhalt aus
	 * und gibt ihn als {@link String} zurück.
	 * @param file Die gewünschte Datei.
	 * @return Inhalt der Datei als {@link String}.
	 */
	protected String readFile(File file) {
		String s = "";
		try {
			scanner = new Scanner(file);
		} catch (FileNotFoundException x) {
			x.printStackTrace();
		}
		while(scanner.hasNextLine()) {
			s += scanner.nextLine() + "\n";
		}
		return s;
	}
	
	/**
	 * Liest aus der Speicher-Datei den gesamten Inhalt aus
	 * und gibt ihn als {@link String} zurück.
	 * @return Inhalt der Speicherdatei als {@link String}.
	 */
	protected String readFile() {
		String s = "";
		try {
			scanner = new Scanner(file);
		} catch (FileNotFoundException x) {
			x.printStackTrace();
		}
		while(scanner.hasNextLine()) {
			s += scanner.nextLine() + "\n";
		}
		return s;
	}
	
	/**
	 * Überschreibt eine Datei komplett mit einem String.
	 * @param file die zu überschreibende Datei.
	 * @param str der String, der in die Datei geschrieben werden soll.
	 */
	protected void writeFile(File file, String str) {
		try {
			writer = new BufferedWriter(new FileWriter(file.getAbsolutePath()));
			writer.write(str);
			writer.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Überschreibt die Datei, die die Spielstände speichert komplett mit einem String.
	 * @param str der String, der in die Datei geschrieben werden soll.
	 */
	protected void writeFile(String str) {
		writeFile(file, str);
	}
	
	/**
	 * Löscht einen bestimmten Spielstand aus der Datei.
	 * @param fileName Name des Spielstands
	 */
	protected void deleteGame(String gameName) {
		String r = readFile();
		String s = getGame(gameName);
		r = r.replaceAll(s+separator, "");
		writeFile(r);
	}
	
	/**
	 * Neue Methode, die effizienter aus der Datei liest.
	 * Es wird ein gewünschter Spielstand zurückgegeben.
	 * @param gameName der gewünschte Spielstand
	 * @return der Spielstand als {@link String}
	 */
	protected String getGame(String gameName) {
		return searchLineInFile("gameName:" + gameName + ":");
	}
	
	/**
	 * Es wird in der Spielstand-Datei mit Hilfe eines Prefix nach einer 
	 * bestimmten Zeile bzw. einem bestimmten Spielstand gesucht.
	 * Dabei wird nicht die ganze Datei gelesen, sondern nur bis zur Zeile, die gesucht wird.
	 * Die gefundene Zeile wird als {@link String} zurückgegeben.
	 * @param prefix Prefix der gesuchten Zeile
	 * @return die gefundene Zeile
	 */
	protected String searchLineInFile(String prefix) {
		String r = "";
		
		try {
			scanner = new Scanner(file);
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		}
		
		boolean found = false;
		while(found == false) {
			if(scanner.hasNextLine()) {
				String s = scanner.nextLine();
				
				if(s.startsWith(prefix)) {
					r = s;
					found = true;
				}
			}
		}
		
		return r.replaceAll(separator, "");
	}
	
	/**
	 * Es wird in einer Datei mit Hilfe eines Prefix nach einer bestimmten Zeile gesucht.
	 * Dabei wird nicht die ganze Datei gelesen, sondern nur bis zur Zeile, die gesucht wird.
	 * Die gefundene Zeile wird als {@link String} zurückgegeben.
	 * @param file Die Datei, in der gesucht werden soll.
	 * @param prefix Prefix der gesuchten Zeile
	 * @return die gefundene Zeile
	 */
	protected String searchLineInFile(File file, String prefix) {
		String r = "";
		
		try {
			scanner = new Scanner(file);
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		}
		
		boolean found = false;
		while(found == false) {
			if(scanner.hasNextLine()) {
				String s = scanner.nextLine();
				
				if(s.startsWith(prefix)) {
					r = s;
					found = true;
				}
			}
		}
		
		return r.replaceAll(separator, "");
	}
	
	/**
	 * Gibt den Dateipfad für "Meine Dokumente" des Benutzers zurück.
	 * @return Pfad
	 */
	protected String userDirectory() {
		JFileChooser jf = new JFileChooser();
		return jf.getFileSystemView().getDefaultDirectory().toString();
	}
	
	/**
	 * Methode, die immer die aktuelle Zeit berechnet.
	 * Wird für die korrekte Speicherung der Spielstände benötigt, damit man diese auseinanderhalten kann.
	 * @return Gibt die aktuelle Zeit zurück.
	 */
	private String timestamp() {
		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.y_HH-mm-ss");
		return sdf.format(c.getTime());
	}
	
	/**
	 * main method
	 * für tests
	 * @param args arguments
	 */
	public static void main(String[] args) {
		//SaveLoad sl = new SaveLoad();
		//sl.clearFile();
		//sl.newGame("xXxSupaHotFirexXx69420", "xxxno0b1447xxx", 42, "AAAADJJJSHDHHDCC", "SADSD ASVDSVDSFDS F SD FDS FDS  F");
//		System.out.println(sl.savegame);
		//sl.saveGameFile();
		SaveLoad saveload = new SaveLoad();
		saveload.clearFile();
//		saveload.newGame("test", "gegnerTest", 8, "fesfdsgfdb", "dsfsdfgbfgb");
//		saveload.saveGameFile();
		//saveload.clearFile();
		saveload.newGame("sjifsd", "xXxSupaHotFirexXx69420", "xxxno0b1447xxx", 42, "AAAADJJJSHDHHDCC", "SADSD ASVDSVDSFDS F SD FDS FDS  F", "GSNHDFDF");
		saveload.newGame("blabla", "hallo", "hgjghj", 16, "ASFSDASD", "SDASDDSVBSF", "FSUIJFS");
//		System.out.println(saveload.userDirectory() + saveload.file.separator + "shipZ" + saveload.file.separator + "saves.dat");
//		System.out.println(sl.file.getAbsolutePath());
		
//		String[] a = saveload.readFile().split("~~~~~");
//		System.out.println(a.length);
/*		for(int i = 0; i < a.length; i++) {
			System.out.println(a[i]);
		}*/
//		System.out.println(a[0]);
//		String[] b = a[0].split(":");
		/*for(int i = 0; i < b.length; i++) {
			System.out.println(b[i]);
		}*/
//		System.out.println(b[1]);
		
		
		System.out.println(saveload.getGame("sjifsd"));
//		System.out.println(saveload.doesFileExist("sjifsd"));
//		System.out.println(saveload.doesFileExist("sdfvfdg"));
		System.out.println();
		
		System.out.println(saveload.getPlayerName("sjifsd"));
		System.out.println(saveload.getTime("sjifsd"));
		System.out.println(saveload.getOpponentName("sjifsd"));
		System.out.println(saveload.getBoardsize("sjifsd"));
		System.out.println(saveload.getBoard("sjifsd"));
		System.out.println(saveload.getDrawHistoryPlayer1("sjifsd"));
		System.out.println(saveload.getDrawHistoryPlayer2("sjifsd"));
		
//		saveload.newGame("blabla", "hallo", "gegnerTest", 8, "AAAAAAAA", "ABABABABAAB");
		
		/*for(int i = 0; i < saveload.getAllFileNames().length; i++) {
			System.out.println(saveload.getAllFileNames()[i]);
		}*/

		System.out.println();
		System.out.println(saveload.getAllGameNames());
		
//		saveload.deleteGame("blabla");
		
		saveload.setBoard("blabla", "TEEEEEEEEEEEEEEEEEST");
		
		System.out.println(saveload.getGame("sjifsd"));
		
	}
	
}
