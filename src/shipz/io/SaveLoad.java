package shipz.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;

import javax.swing.JFileChooser;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

/**
 * Diese Klasse verwaltet die Speicherprozesse der anderen Klassen.
 * @author Florian Osterberg
 */
public class SaveLoad {

	// IV
	/** Datei, in der die einzelnen Spielstände gespeichert werden. */
	private File file;
	/** Writer, der in eine Datei schreibt. */
	private BufferedWriter writer;
	/** Scanner, der eine Datei liest. */
	private Scanner scanner;
	/** Writer, der in die XML-Datei schreibt. */
	private XMLOutputter xmlOutput;
	/** das XML-Dokument */
	private Document document;
	/** Wurzelelement des XML-Dokuments */
	private Element root;
	/** SAXBuilder, der das ausgelesene XML in das Document-Objekt schreibt. */
	private SAXBuilder builder;
	/** Filewriter-Objekt, mit dem in die XML-Datei geschrieben wird */
	private FileWriter filewriter;
	
	//Konstruktor
	/**
	 * Konstruktor der Klasse, der das File-Objekt initialisiert 
	 * und diesem einen Dateipfad zuordnet.
	 */
	public SaveLoad() {
		file = new File(fileDirectory() + File.separator + "saves.xml");
		makeDirectory(file);
		document = new Document();
		root = new Element("saves");
		document.setContent(root);
		builder = new SAXBuilder();
		xmlOutput = new XMLOutputter();
	}
	
	// IM
	/**
	 * Methode, die mit allen benötigten Informationen ein neues Spiel im XML erstellt.
	 * @param gameName Dateiname für den Spielstand
	 * @param playerName Name des ersten Spielers
	 * @param opponentName Name des zweiten Spielers bzw. des Gegners
	 * @param boardPlayer1 Das gesamte Feld des ersten Spielers als {@link String} gespeichert. Außerdem wird die Feldgröße gespeichert.
	 * @param boardPlayer2 Das gesamte Feld des zweiten Spielers als {@link String} gespeichert. Außerdem wird die Feldgröße gespeichert.
	 * @param boardsize Größe des Feldes. Format: "Höhe,Breite"
	 */
	protected void newGame(String gameName, String playerName, String opponentName, String boardPlayerOne, String boardPlayerTwo, String boardsize) {
		boolean b = false;
		
		if(root.getChildren() != null) {
			updateXML();
			b = doesGameExist(gameName);
		}
		
		if(b == false) {
			Element gameElement = new Element("game");
			gameElement.addContent(new Element("gameName").setText(gameName));
			gameElement.addContent(new Element("time").setText(timestamp()));
			gameElement.addContent(new Element("playerName").setText(playerName));
			gameElement.addContent(new Element("opponentName").setText(opponentName));
			gameElement.addContent(new Element("boardPlayerOne").setText(boardPlayerOne));
			gameElement.addContent(new Element("boardPlayerTwo").setText(boardPlayerTwo));
			gameElement.addContent(new Element("boardsize").setText(boardsize));
			gameElement.addContent(new Element("drawHistoryPlayerOne").setText("null"));
			gameElement.addContent(new Element("drawHistoryPlayerTwo").setText("null"));
			gameElement.addContent(new Element("activePlayer").setText("1"));
			
			root.addContent(gameElement);
			document.setContent(root);
			writeXML();
		} else {
			System.err.println("Fehler beim Erstellen des Spielstands! Dieser Spielstand existiert bereits!");
		}
	}
	
	/**
	 * Speichert die Informationen eines bestimmten Spiels.
	 * @param gameName Name des Spiels
	 * @param playerName Name des ersten Spielers
	 * @param opponentName Name des zweiten Spielers
	 * @param boardPlayer1 Das gesamte Feld des ersten Spielers als {@link String} gespeichert. Außerdem wird die Feldgröße gespeichert. Format: "Feldhöhe,Feldbreite,Spielfeld"
	 */
	protected void saveGame(String gameName, String playerName, String opponentName, String boardPlayerOne, String boardPlayerTwo, int activePlayer) {
		setPlayerName(gameName, playerName);
		setOpponentName(gameName, opponentName);
		setBoardPlayerOne(gameName, boardPlayerOne);
		setBoardPlayerTwo(gameName, boardPlayerTwo);
		setActivePlayer(gameName, activePlayer);
	}
	
	/**
	 * Falls die Dateien noch nicht vorhanden sind, werden sie erstellt.
	 * @param file Datei, die erstellt werden soll.
	 */
	protected void makeDirectory(File file) {
		if(!(file.exists())) {
			try{
				new File(fileDirectory()).mkdir();
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
	 * Diese Methode gibt den Inhalt eines Knotens eines Spielstands
	 * als String zurück.
	 * @param gameName Name des Spiels
	 * @param node Knoten dessen Text zurückgegeben werden soll
	 * @return Inhalt des Knotens als String
	 */
	private String getNode(String gameName, String node) {
		updateXML();
		String str = "";
		
		List<Element> list = root.getChildren();
		for(Element e : list) {
			if(e.getChild("gameName").getText().equals(gameName)) {
				str = e.getChild(node).getText();
			}
		}
		
		return str;
	}
	
	/**
	 * Mit dieser Methode lässt sich ein einzelner Knoten eines Spielstands bearbeiten.
	 * @param gameName Name des Spielstands
	 * @param node zu bearbeitender Knoten
	 * @param text Text, der im Knoten eingefügt werden soll
	 */
	private void setNode(String gameName, String node, String text) {
		updateXML();
		List<Element> list = root.getChildren();
		for(Element e : list) {
			if(e.getChild("gameName").getText().equals(gameName)) {
				e.getChild(node).setText(text);
			}
		}
		document.setContent(root);
		writeXML();
	}
	
	/**
	 * Das XML-Dokument wird ausgelesen und die Instanz-Variablen,
	 * die Wurzel-Element und das Dokument speichern,
	 * werden somit aktualisiert.
	 */
	private void updateXML() {
		if(root.getChildren() != new Element("saves").getChildren()) {
			try {
				FileInputStream fileinput = new FileInputStream(file);
				document = builder.build(fileinput);
				root = document.detachRootElement();
				fileinput.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (JDOMException | IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Das aktuell in den Instanz-Variablen abgespeicherte XML-Dokument
	 * wird in die XML-Datei geschrieben.
	 */
	private void writeXML() {
		try {
			filewriter = new FileWriter(file);
			xmlOutput.setFormat(Format.getPrettyFormat());
			xmlOutput.output(document, filewriter);
			filewriter.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Löscht einen bestimmten Spielstand aus der Datei.
	 * @param gameName Name des Spielstands
	 */
	protected void deleteGame(String gameName) {
		updateXML();
		if(doesGameExist(gameName)) {
			List<Element> list = root.getChildren();
			Element temp = null;
			for(Element e : list) {
				if(e.getChild("gameName").getText().equals(gameName)) {
					temp = e;
				}
			}
			root.removeContent(temp);
			document.setContent(root);
			writeXML();
		} else {
			System.err.println("Fehler beim Löschen des Spielstands! Dieser Spielstand existiert nicht!");
		}
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
		String str = "";
		updateXML();
		
		List<Element> list = root.getChildren();
		for(Element e : list) {
			str += e.getChild("gameName").getText() + ",";
		}
		
		return str;
	}
	
	/**
	 * Diese Methode lï¿½dt aus dem gespeicherten Spielstand das gespeicherte Spielfeld des ersten Spielers heraus.
	 * Dieses Spielfeld wird dann als {@link String} zurückgegeben.
	 * @param gameName Dateiname zur Identifizierung des gespeicherten Spielstands.
	 * @return Das geladene Spielfeld
	 */
	protected String getBoardPlayerOne(String gameName) {
		return getNode(gameName, "boardPlayerOne");
	}
	
	/**
	 * Diese Methode lï¿½dt aus dem gespeicherten Spielstand das gespeicherte Spielfeld des zweiten Spielers heraus.
	 * Dieses Spielfeld wird dann als {@link String} zurückgegeben.
	 * @param gameName Dateiname zur Identifizierung des gespeicherten Spielstands.
	 * @return Das geladene Spielfeld
	 */
	protected String getBoardPlayerTwo(String gameName) {
		return getNode(gameName, "boardPlayerTwo");
	}
	
	/**
	 * Gibt den Spielernamen eines Spielstands zurück.
	 * @param gameName der gewünschte Spielstand
	 * @return Spielername eines Spielstand
	 */
	protected String getPlayerName(String gameName) {
		return getNode(gameName, "playerName");
	}
	
	/**
	 * Gibt den Namen des Gegners eines bestimmten Spielstands zurück.
	 * @param gameName der gewünschte Spielstand
	 * @return Namen des Gegners eines bestimmten Spielstands
	 */
	protected String getOpponentName(String gameName) {
		return getNode(gameName, "opponentName");
	}
	
	/**
	 * Gibt die Feldgröße eines bestimmten Spielstands zurück.
	 * @param gameName der gewünschte Spielstand
	 * @return Feldgröße eines bestimmten Spielstands als {@link String}
	 */
	protected String getBoardsize(String gameName) {
		return getNode(gameName, "boardsize");
	}
	
	/**
	 * Gibt den String aus einem Spielstand zurück, der die Spielzï¿½ge des ersten Spielers speichert.
	 * @param gameName der gewünschte Spielstand
	 * @return Die Spielzï¿½ge als {@link String}
	 */
	protected String getDrawHistoryPlayerOne(String gameName) {
		return getNode(gameName, "drawHistoryPlayerOne");
	}
	
	/**
	 * Gibt den String aus einem Spielstand zurück, der die Spielzï¿½ge des zweiten Spielers speichert.
	 * @param gameName der gewünschte Spielstand
	 * @return Die Spielzï¿½ge als {@link String}
	 */
	protected String getDrawHistoryPlayerTwo(String gameName) {
		return getNode(gameName, "drawHistoryPlayerTwo");
	}
	
	/**
	 * Liest den aktuellen Spieler eines Spiels aus der Datei aus und gibt ihn zurück.
	 * @param gameName Name des Spielstands
	 * @return der aktive Spieler
	 */
	protected int getActivePlayer(String gameName) {
		return Integer.parseInt(getNode(gameName, "activePlayer"));
	}
	
	/**
	 * Gibt die gespeicherte Uhrzeit eines Spielstands zurück.
	 * @param gameName der gewünschte Spielstand
	 * @return die gespeicherte Uhrzeit als {@link String}
	 */
	protected String getTime(String gameName) {
		return getNode(gameName, "time");
	}
	
	/**
	 * Diese Methode speichert das Spielfeld des zweiten Spielers, das als Parameter ï¿½bergeben wird in den Spielstand gameName.
	 * @param gameName der Spielstand, bei dem das Spielbrett abgespeichert werden soll.
	 * @param board das Spielbrett als {@link String}
	 */
	protected void setBoardPlayerOne(String gameName, String boardPlayerOne) {
		setNode(gameName, "boardPlayerOne", boardPlayerOne);
	}
	
	/**
	 * Diese Methode speichert das Spielfeld des zweiten Spielers, das als Parameter ï¿½bergeben wird in den Spielstand gameName.
	 * @param gameName der Spielstand, bei dem das Spielbrett abgespeichert werden soll.
	 * @param board das Spielbrett als {@link String}
	 */
	protected void setBoardPlayerTwo(String gameName, String boardPlayerTwo) {
		setNode(gameName, "boardPlayerTwo", boardPlayerTwo);
	}
	
	/**
	 * ändert den Namen des ersten Spielers zu playerName im Spielstand gameName.
	 * @param gameName der Spielstand, bei dem der Name des ersten Spielers geändert werden soll.
	 * @param playerName der neue Name des ersten Spielers
	 */
	protected void setPlayerName(String gameName, String playerName) {
		setNode(gameName, "playerName", playerName);
	}
	
	/**
	 * ändert den Namen des Gegners eines Spielstands.
	 * @param gameName der Spielstand
	 * @param opponentName
	 */
	protected void setOpponentName(String gameName, String opponentName) {
		setNode(gameName, "opponentName", opponentName);
	}
	
	/**
	 * ändert die Feldgröße eines Spielstands.
	 * @param gameName der Spielstand
	 * @param boardsize
	 */
	protected void setBoardsize(String gameName, String boardsize) {
		setNode(gameName, "boardsize", boardsize);
	}
	
	/**
	 * ändert den Spielverlauf des ersten Spielers in einem Spielstand.
	 * @param gameName der Spielstand
	 * @param drawHistoryPlayer1 der neue Spielverlauf des ersten Spielers
	 */
	protected void setDrawHistoryPlayerOne(String gameName, String drawHistoryPlayerOne) {
		setNode(gameName, "drawHistoryPlayerOne", drawHistoryPlayerOne);
	}
	
	/**
	 * ändert den Spielverlauf des zweiten Spielers in einem Spielstand.
	 * @param gameName der Spielstand
	 * @param drawHistoryPlayer2 der neue Spielverlauf des zweiten Spielers
	 */
	protected void setDrawHistoryPlayerTwo(String gameName, String drawHistoryPlayerTwo) {
		setNode(gameName, "drawHistoryPlayerTwo", drawHistoryPlayerTwo);
	}
	
	/**
	 * ändert den aktuellen Spieler in der Datei.
	 * @param gameName Name des Spielstands
	 * @param activePlayer neuer aktueller Spieler
	 */
	protected void setActivePlayer(String gameName, int activePlayer) {
		setNode(gameName, "activePlayer", ""+activePlayer);
	}
	
	/**
	 * Gibt anhand eines Namen eines Spielstands und einem Spielernamen zurück,
	 * ob es sich bei dem Spieler um den ersten, oder um den zweiten
	 * Spieler handelt.
	 * Dies ist wichtig für die Verwaltung der Combos in der Score-Klasse.
	 * @param gameName Name des Spielstands
	 * @param playerName Name des Spielers
	 * @return 1 = Spieler ist der erste Spieler, 2 = Spieler ist der zweite Spieler, 0 = error
	 */
	protected byte getPlayersNumber(String gameName, String playerName) {
		byte result = 0;
		
		if(playerName.equals(getPlayerName(gameName))) {
			result = 1;
		} else if(playerName.equals(getOpponentName(gameName))) {
			result = 2;
		} else {
			result = 0;
		}
		
		return result;
	}
	
	/**
	 * Aktualisiert die Uhrzeit eines Spielstands.
	 * @param gameName der Spielstand
	 */
	protected void updateTime(String gameName) {
		setNode(gameName, "time", timestamp());
	}
	
	/**
	 * überprüft ob ein Spielstand mit bestimmtem Namen vorhanden ist.
	 * @param gameName der gewünschte Spielstand
	 * @return Ist der Spielstand vorhanden?
	 */
	private boolean doesGameExist(String gameName) {
		updateXML();
		boolean doesGameExist = false;
		List<Element> list = root.getChildren();
		
		for(Element e : list) {
			if(e.getChild("gameName").getText().equals(gameName)) {
				doesGameExist = true;
			}
		}
		
		return doesGameExist;
	}
	
	/**
	 * Sucht in einer Datei nach einer bestimmten Zeile.
	 * Es wird mit nach einem Prefix gesucht.
	 * Wenn die Zeile gefunden wurde, wird sie als String zurückgegeben.
	 * @param file Die Datei, in der gesucht werden soll.
	 * @param prefix Prefix der Zeile, nach der gesucht werden soll.
	 * @return Die gefundene Zeile
	 */
	protected String searchLine(File file, String prefix) {
		try {
			scanner = new Scanner(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		String line = scanner.nextLine();
		while(!(line.startsWith(prefix))) {
			line = scanner.nextLine(); // geht so lange weiter, bis er die Zeile gefunden hat
		}
		
		return line;
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
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		while(scanner.hasNextLine()) {
			s += scanner.nextLine() + "\n";
		}
		return s;
	}
	
	/**
	 * überschreibt eine Datei komplett mit einem String.
	 * @param file die zu überschreibende Datei.
	 * @param str der {@link String}, der in die Datei geschrieben werden soll.
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
	 * Gibt den Dateipfad zurück, in dem Dateien des Spiels gespeichert werden.
	 * @return Pfad
	 */
	protected String fileDirectory() {
		JFileChooser jf = new JFileChooser();
		return jf.getFileSystemView().getDefaultDirectory().toString() + File.separator + "shipZ";
	}
	
	/**
	 * Methode, die immer die aktuelle Zeit berechnet.
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
		SaveLoad saveload = new SaveLoad();
//		saveload.newGame("nkjkjjk", "nur", "noch", "ein", "kleiner", "test");
		saveload.setPlayerName("nkjkjjk", "TEST");
		
	}
	
}