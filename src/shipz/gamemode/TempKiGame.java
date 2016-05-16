package shipz.gamemode;

import java.util.concurrent.ThreadLocalRandom;

import shipz.Player;


/**
 * Spielverwaltung
 * @author Max
 * @version	0.1
 */

public class TempKiGame{

	//IV
	/** Spielfeld des 1. Spielers */
	private char[][] board1;
	/** Spielfeld des 2. Spielers */
	private char[][] board2;
	/** Verweis auf den 1. Spieler */
	private Player player1;
	/** Verweis auf den 2. Spieler */
	private Player player2;
	/** Netzwerkverbindung */
	//private Network network;
	/** grafische Nutzeroberfläche */
	//private GUI gui;
	/** Spielstandverwaltung */
	//private FileStream filestream;
	private char water = ' ';


	//Constructor
	/**
	 * erstellt ein neues Spiel mit leeren Feldern
	 * @param width		Feldbreite
	 * @param height	Feldhöhe
	 */
	private TempKiGame(int width, int height) {
		board1 = new char[width][height];
		board2 = new char[width][height];
		initiateBoard(board1);
		initiateBoard(board2);
	}


	//Methoden


	/**
	 * setzt alle Zellen eines Felds auf Wasser
	 * @param board	zu füllendes Feld
	 */
	private void initiateBoard(char[][] board) {
		//1. Zähler
		int y;
		//2. Zähler
		int x;
		//doppelte Schleife für Durchlauf durch alle Felder
		for(y=0; y<board.length; y++) {
			for(x=0; x<board[y].length; x++) {
				board[y][x] = water;
			}//Ende ySchleife
		}//Ende xSchleife


		/** FÜR TESTZWECKE */

		/*
		board[4][2] = 'x';
		board[5][2] = 'x';
		board[6][2] = 'x';
		board[7][2] = 'x';

		board[1][1] = 'x';
		board[1][2] = 'x';
		board[1][3] = 'x';
		board[1][4] = 'x';

		board[5][5] = 'x';
		board[5][6] = 'x';
		board[5][7] = 'x';
		board[5][8] = 'x';
		board[5][9] = 'x';

		board[3][6] = 'x';
		board[2][6] = 'x';
		*/

	}

	/**
	 * überprüft das Ende des Spiels und leitet eventuelle Benachrichtigungen ein
	 * @return gibt an, ob das Spiel beendet ist
	 */
	private boolean checkGameOver() {return false;}

	/**
	 * überprüft die übergebenen Koordinaten auf Schiffelemente und ruft eventuell sink() auf
	 * @param x 	Koordinate
	 * @param y 	Koordinate
	 * @return 0	Wasser getroffen
	 * @return 1	Schiff getroffen
	 * @return 2	Schiff versenkt
	 */
	public byte checkTile(int x, int y) {
		byte r = 0;
		if(board1[y][x] == 'x') {
			r = 1;
			if (sink(x, y, board1)) {
				r = 2;
			}
			//System.out.println("Es wurde ein Schiffelement zerstört");
			return r;
		}
		else {
			//System.out.println("Es wurde kein Schiffelement zerstört");
			return r;
		}
	}

	/**
	 * setzt die angegebene Zelle auf ein zerstörtes Schiffelement
	 * @param x		Koordinate
	 * @param y 	Koordinate
	 * @param board	Spielfeld
	 * @return		gibt an, ob das gesamte Schiff versenkt wurde
	 */
	private boolean sink(int x, int y, char[][] board) {
		board[y][x] = 'z';
		if (checkShipDestroyed(x, y, board)) {
			//System.out.println("Es wurde ein gesamtes Schiff zerstört");
		}
		return (checkShipDestroyed(x, y, board));
	}

	/**
	 * prüft den Gesamtzustand des Schiffs
	 * @param x		Koordinate
	 * @param y 	Koordinate
	 * @param board	Spielfeld
	 * @return		gibt an, ob das gesamte Schiff versenkt wurde
	 */
	private boolean checkShipDestroyed(int x, int y, char[][] board) {
		return (checkShipDestroyedUp(x, y, board) && checkShipDestroyedRight(x, y, board) && checkShipDestroyedDown(x, y, board) && checkShipDestroyedLeft(x, y, board));
	}

	/**
	 * prüft den Gesamtzustand des Schiffs nach oben
	 * @param x		Koordinate
	 * @param y 	Koordinate
	 * @param board	Spielfeld
	 * @return		gibt an, ob das gesamte Schiff versenkt wurde
	 */
	private boolean checkShipDestroyedUp(int x, int y, char[][] board) {
		if(y-1 >= 0) {
			if(board[y-1][x] == water) {						//Wasser auf angrenzendem Feld
				return true;								//Schiff wurde vielleicht versenkt
			}
			else if(board[y-1][x] == 'x') {					//Schiffelement auf angrenzendem Feld
				return false;								//Schiff wurde nicht versenkt
			}
			else {											//zerstörtes Schiffelement auf angrenzendem Feld
				return checkShipDestroyedUp(x, y-1, board);	//weitere Prüfung vom angrenzenden Feld
			}
		}
		else {
			return true;
		}
	}

	/**
	 * prüft den Gesamtzustand des Schiffs nach rechts
	 * @param x		Koordinate
	 * @param y 	Koordinate
	 * @param board	Spielfeld
	 * @return		gibt an, ob das gesamte Schiff versenkt wurde
	 */
	private boolean checkShipDestroyedRight(int x, int y, char[][] board) {
		if(x+1 < board[y].length) {
			if(board[y][x+1] == water) {							//Wasser auf angrenzendem Feld
				return true;									//Schiff wurde vielleicht versenkt
			}
			else if(board[y][x+1] == 'x') {						//Schiffelement auf angrenzendem Feld
				return false;									//Schiff wurde nicht versenkt
			}
			else {												//zerstörtes Schiffelement auf angrenzendem Feld
				return checkShipDestroyedRight(x+1, y, board);	//weitere Prüfung vom angrenzenden Feld
			}
		}
		else {
			return true;
		}
	}

	/**
	 * prüft den Gesamtzustand des Schiffs nach unten
	 * @param x		Koordinate
	 * @param y 	Koordinate
	 * @param board	Spielfeld
	 * @return		gibt an, ob das gesamte Schiff versenkt wurde
	 */
	private boolean checkShipDestroyedDown(int x, int y, char[][] board) {
		if(y+1 < board.length) {
			if(board[y+1][x] == water) {						//Wasser auf angrenzendem Feld
				return true;								//Schiff wurde vielleicht versenkt
			}
			else if(board[y+1][x] == 'x') {					//Schiffelement auf angrenzendem Feld
				return false;								//Schiff wurde nicht versenkt
			}
			else {											//zerstörtes Schiffelement auf angrenzendem Feld
				return checkShipDestroyedUp(x, y+1, board);	//weitere Prüfung vom angrenzenden Feld
			}
		}
		else {
			return true;
		}
	}

	/**
	 * prüft den Gesamtzustand des Schiffs nach links
	 * @param x		Koordinate
	 * @param y 	Koordinate
	 * @param board	Spielfeld
	 * @return		gibt an, ob das gesamte Schiff versenkt wurde
	 */
	private boolean checkShipDestroyedLeft(int x, int y, char[][] board) {
		if(x-1 >= 0) {
			if(board[y][x-1] == water) {							//Wasser auf angrenzendem Feld
				return true;									//Schiff wurde vielleicht versenkt
			}
			else if(board[y][x-1] == 'x') {						//Schiffelement auf angrenzendem Feld
				return false;									//Schiff wurde nicht versenkt
			}
			else {												//zerstörtes Schiffelement auf angrenzendem Feld
				return checkShipDestroyedLeft(x-1, y, board);	//weitere Prüfung vom angrenzenden Feld
			}
		}
		else {
			return true;
		}
	}

	/**
	 * zählt die Anzahl der Schiffe auf einem Feld
	 * @param board	zu zählendes Feld
	 * @return		Anzahl der Schiffe
	 */
	private int shipCount(char[][] board) {return 0;}

	/**
	 * gibt beide Felder hintereinander auf der Konsole aus (für Testzwecke)
	 */
	public void displayBoards() {
		System.out.println("Spieler 1");
		displaySingleBoard(board1);
		System.out.println();
		System.out.println("Spieler 2");
		displaySingleBoard(board2);
	}

	/**
	 * gibt ein Feld auf der Konsole aus (für Testzwecke)
	 * @param board auszugebendes Feld
	 */
	public void displaySingleBoard(char[][] board) {
		//Ausgabe der oberen Feldbeschriftung
		System.out.println("  A B C D E F G H I J");
		//1. Zähler
		int y;
		//2. Zähler
		int x;
		//doppelte Schleife für Durchlauf durch alle Felder
		for(y=0; y<board.length; y++) {
			//Ausgabe der seitlichen Feldbeschriftung
			System.out.print(y);
			for(x=0; x<board[y].length; x++) {
				//Ausgabe der einzelnen Zellen
				System.out.print(" " + board[y][x]);
			}
			//Zeilenumbruch
			System.out.println();
		}
	}

	/**
	 * gibt ein Spiegel auf der Konsole aus (für Testzwecke)
	 * @param board auszugebendes Feld
	 */
	public void displaySingleBoard(boolean[][] board) {
		//Ausgabe der oberen Feldbeschriftung
		System.out.println("  A B C D E F G H I J");
		//1. Zähler
		int y;
		//2. Zähler
		int x;
		//doppelte Schleife für Durchlauf durch alle Felder
		for(y=0; y<board.length; y++) {
			//Ausgabe der seitlichen Feldbeschriftung
			System.out.print(y);
			for(x=0; x<board[y].length; x++) {
				//Ausgabe der einzelnen Zellen
				if(board[y][x]) {
					System.out.print(" " + "1");
				}
				else {
					System.out.print(" " + "0");
				}
			}
			//Zeilenumbruch
			System.out.println();
		}
	}

	/**
	 * platziert alle Schiffe eines Spielers auf seinem Feld
	 * @param board	Feld
	 */
	private void placeShips(char[][] board) {
		while(placeSingleShip(ThreadLocalRandom.current().nextInt(0, board.length + 1), ThreadLocalRandom.current().nextInt(0, board[0].length + 1), 5, board)) {}
	}

	/**
	 * platziert ein einzelnes Schiff
	 * @param y			Koordinate
	 * @param x			Koordinate
	 * @param length	Schifflänge
	 * @param board		Feld
	 * @return			gibt an, ob das Schiff erfolgreich platziert wurde
	 */
	private boolean placeSingleShip(int y, int x, int length, char[][] board) {
		boolean shipPlaced = false;
		boolean used0 = false;
		boolean used1 = false;
		boolean used2 = false;
		boolean used3 = false;
		boolean usedAll = false;
		while(usedAll == false && shipPlaced == false) {				//wird wiederholt, bis alle Richtungen probiert wurden oder das Schiff gesetzt wird
			int dir = ThreadLocalRandom.current().nextInt(0, 3 + 1);	//Richtung: 0=hoch, 1=recht, 2=runter, 3=links
			if(dir == 0 && used0 == false) {
				used0 = true;
			}
			if(dir == 1 && used1 == false) {
				used1 = true;
			}
			if(dir == 2 && used2 == false) {
				used2 = true;
			}
			if(dir == 3 && used3 == false) {
				used3 = true;
			}
			usedAll = used0 && used1 && used2 && used3;
		}
		return shipPlaced;
	}

	/**
	 * Main-Methode
	 * @param args f
	 */
	public static void main(String[] args) {
		TempKiGame game = new TempKiGame(10, 10);

		game.startGame();
	}//end main TempKiGame





	/**
	 * startet ein neues oder geladenes Spiel mit den ausgewählten Optionen
	 */
	private void startGame(){
		TempKiGame game = new TempKiGame(10, 10);

		Computer ki = new Easy(10);

		for (int i= 0; i < 100; i++){

			System.out.println(i+1 + ". Beschuss:");
			String coords = ki.shootField();
            int yCoord = ki.extractYCoord(coords);
            int xCoord = ki.extractXCoord(coords);

            ki.saveReturnedCoordinates(yCoord, xCoord, checkTile( xCoord, yCoord) );

			System.out.println("Treffer auf :" + coords );
			System.out.println("Anzeige des Spiegelfeldes der Ki:");
			ki.displayMirrorField();
			System.out.println("\n");
		}


	}//end startgame


}// //end TempKiGame