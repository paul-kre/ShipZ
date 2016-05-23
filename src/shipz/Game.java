package shipz;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Spielverwaltung
 * @author Max
 * @version	0.1
 */
public class Game {

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
    /** grafische Nutzeroberfl�che */
    //private GUI gui;
    /** Spielstandverwaltung */
    //private FileStream filestream;

    //Constructor
    /**
     * erstellt ein neues Spiel mit leeren Feldern
     * @param widht		Feldbreite
     * @param height	Feldh�he
     */
    private Game(int width, int height) {
        board1 = new char[width][height];
        board2 = new char[width][height];
        initiateBoard(board1);
        initiateBoard(board2);
    }

    //Methoden
    /**
     * startet ein neues oder geladenes Spiel mit den ausgew�hlten Optionen
     */
    private void startGame(){};

    /**
     * setzt alle Zellen eines Felds auf Wasser
     * @param board	zu f�llendes Feld
     */
    private void initiateBoard(char[][] board) {
        //1. Z�hler
        int y;
        //2. Z�hler
        int x;
        //doppelte Schleife f�r Durchlauf durch alle Felder
        for(y=0; y<board.length; y++) {
            for(x=0; x<board[y].length; x++) {
                board[y][x] = 'w';
            }//Ende ySchleife
        }//Ende xSchleife
    }

    /**
     * �berpr�ft das Ende des Spiels und leitet eventuelle Benachrichtigungen ein
     * @return gibt an, ob das Spiel beendet ist
     */
    private boolean checkGameOver() {return false;}

    /**
     * �berpr�ft die �bergebenen Koordinaten auf Schiffelemente und ruft eventuell sink() auf
     * @param x 	Koordinate
     * @param y 	Koordinate
     * @param board	Spielfeld
     * @return 0	Wasser getroffen
     * @return 1	Schiff getroffen
     * @return 2	Schiff versenkt
     */
    private byte checkTile(int x, int y, char[][] board) {
        byte r = 0;
        if(board[y][x] == 'x') {
            r = 1;
            if (sink(x, y, board)) {
                r = 2;
            }
            //System.out.println("Es wurde ein Schiffelement zerst�rt");
            return r;
        }
        else {
            //System.out.println("Es wurde kein Schiffelement zerst�rt");
            return r;
        }
    }

    /**
     * setzt die angegebene Zelle auf ein zerst�rtes Schiffelement
     * @param x		Koordinate
     * @param y 	Koordinate
     * @param board	Spielfeld
     * @return		gibt an, ob das gesamte Schiff versenkt wurde
     */
    private boolean sink(int x, int y, char[][] board) {
        board[y][x] = 'z';
        if (checkShipDestroyed(x, y, board)) {
            //System.out.println("Es wurde ein gesamtes Schiff zerst�rt");
        }
        return (checkShipDestroyed(x, y, board));
    }

    /**
     * pr�ft den Gesamtzustand des Schiffs
     * @param x		Koordinate
     * @param y 	Koordinate
     * @param board	Spielfeld
     * @return		gibt an, ob das gesamte Schiff versenkt wurde
     */
    private boolean checkShipDestroyed(int x, int y, char[][] board) {
        return (checkShipDestroyedUp(x, y, board) && checkShipDestroyedRight(x, y, board) && checkShipDestroyedDown(x, y, board) && checkShipDestroyedLeft(x, y, board));
    }

    /**
     * pr�ft den Gesamtzustand des Schiffs nach oben
     * @param x		Koordinate
     * @param y 	Koordinate
     * @param board	Spielfeld
     * @return		gibt an, ob das gesamte Schiff versenkt wurde
     */
    private boolean checkShipDestroyedUp(int x, int y, char[][] board) {
        if(y-1 >= 0) {
            if(board[y-1][x] == 'w') {						//Wasser auf angrenzendem Feld
                return true;								//Schiff wurde vielleicht versenkt
            }
            else if(board[y-1][x] == 'x') {					//Schiffelement auf angrenzendem Feld
                return false;								//Schiff wurde nicht versenkt
            }
            else {											//zerst�rtes Schiffelement auf angrenzendem Feld
                return checkShipDestroyedUp(x, y-1, board);	//weitere Pr�fung vom angrenzenden Feld
            }
        }
        else {
            return true;
        }
    }

    /**
     * pr�ft den Gesamtzustand des Schiffs nach rechts
     * @param x		Koordinate
     * @param y 	Koordinate
     * @param board	Spielfeld
     * @return		gibt an, ob das gesamte Schiff versenkt wurde
     */
    private boolean checkShipDestroyedRight(int x, int y, char[][] board) {
        if(x+1 < board[y].length) {
            if(board[y][x+1] == 'w') {							//Wasser auf angrenzendem Feld
                return true;									//Schiff wurde vielleicht versenkt
            }
            else if(board[y][x+1] == 'x') {						//Schiffelement auf angrenzendem Feld
                return false;									//Schiff wurde nicht versenkt
            }
            else {												//zerst�rtes Schiffelement auf angrenzendem Feld
                return checkShipDestroyedRight(x+1, y, board);	//weitere Pr�fung vom angrenzenden Feld
            }
        }
        else {
            return true;
        }
    }

    /**
     * pr�ft den Gesamtzustand des Schiffs nach unten
     * @param x		Koordinate
     * @param y 	Koordinate
     * @param board	Spielfeld
     * @return		gibt an, ob das gesamte Schiff versenkt wurde
     */
    private boolean checkShipDestroyedDown(int x, int y, char[][] board) {
        if(y+1 < board.length) {
            if(board[y+1][x] == 'w') {						//Wasser auf angrenzendem Feld
                return true;								//Schiff wurde vielleicht versenkt
            }
            else if(board[y+1][x] == 'x') {					//Schiffelement auf angrenzendem Feld
                return false;								//Schiff wurde nicht versenkt
            }
            else {											//zerst�rtes Schiffelement auf angrenzendem Feld
                return checkShipDestroyedUp(x, y+1, board);	//weitere Pr�fung vom angrenzenden Feld
            }
        }
        else {
            return true;
        }
    }

    /**
     * pr�ft den Gesamtzustand des Schiffs nach links
     * @param x		Koordinate
     * @param y 	Koordinate
     * @param board	Spielfeld
     * @return		gibt an, ob das gesamte Schiff versenkt wurde
     */
    private boolean checkShipDestroyedLeft(int x, int y, char[][] board) {
        if(x-1 >= 0) {
            if(board[y][x-1] == 'w') {							//Wasser auf angrenzendem Feld
                return true;									//Schiff wurde vielleicht versenkt
            }
            else if(board[y][x-1] == 'x') {						//Schiffelement auf angrenzendem Feld
                return false;									//Schiff wurde nicht versenkt
            }
            else {												//zerst�rtes Schiffelement auf angrenzendem Feld
                return checkShipDestroyedLeft(x-1, y, board);	//weitere Pr�fung vom angrenzenden Feld
            }
        }
        else {
            return true;
        }
    }

    /**
     * z�hlt die Anzahl der Schiffe auf einem Feld
     * @param board	zu z�hlendes Feld
     * @return		Anzahl der Schiffe
     */
    private int shipCount(char[][] board) {return 0;}

    /**
     * gibt beide Felder hintereinander auf der Konsole aus (f�r Testzwecke)
     */
    public void displayBoards() {
        System.out.println("Spieler 1");
        displaySingleBoard(board1);
        System.out.println();
        System.out.println("Spieler 2");
        displaySingleBoard(board2);
    }

    /**
     * gibt ein Feld auf der Konsole aus (f�r Testzwecke)
     * @param board auszugebendes Feld
     */
    public void displaySingleBoard(char[][] board) {
        //Ausgabe der oberen Feldbeschriftung
        System.out.println("  A B C D E F G H I J");
        //1. Z�hler
        int y;
        //2. Z�hler
        int x;
        //doppelte Schleife f�r Durchlauf durch alle Felder
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
     * gibt ein Spiegel auf der Konsole aus (f�r Testzwecke)
     * @param board auszugebendes Feld
     */
    public void displaySingleBoard(boolean[][] board) {
        //Ausgabe der oberen Feldbeschriftung
        System.out.println("  A B C D E F G H I J");
        //1. Z�hler
        int y;
        //2. Z�hler
        int x;
        //doppelte Schleife f�r Durchlauf durch alle Felder
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
        while(placeSingleShip(ThreadLocalRandom.current().nextInt(0, board.length + 1), ThreadLocalRandom.current().nextInt(0, board[0].length + 1), 5, board) == false) {}
        while(placeSingleShip(ThreadLocalRandom.current().nextInt(0, board.length + 1), ThreadLocalRandom.current().nextInt(0, board[0].length + 1), 4, board) == false) {}
        while(placeSingleShip(ThreadLocalRandom.current().nextInt(0, board.length + 1), ThreadLocalRandom.current().nextInt(0, board[0].length + 1), 4, board) == false) {}
        while(placeSingleShip(ThreadLocalRandom.current().nextInt(0, board.length + 1), ThreadLocalRandom.current().nextInt(0, board[0].length + 1), 3, board) == false) {}
        while(placeSingleShip(ThreadLocalRandom.current().nextInt(0, board.length + 1), ThreadLocalRandom.current().nextInt(0, board[0].length + 1), 3, board) == false) {}
        while(placeSingleShip(ThreadLocalRandom.current().nextInt(0, board.length + 1), ThreadLocalRandom.current().nextInt(0, board[0].length + 1), 3, board) == false) {}
        while(placeSingleShip(ThreadLocalRandom.current().nextInt(0, board.length + 1), ThreadLocalRandom.current().nextInt(0, board[0].length + 1), 2, board) == false) {}
        swapChars('b', 'w', board);
    }

    /**
     * platziert ein einzelnes Schiff
     * @param y			Koordinate
     * @param x			Koordinate
     * @param length	Schiffl�nge
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
                shipPlaced = checkShipUp(y, x, length, board);
                used0 = true;
            }
            if(dir == 1 && used1 == false) {
                shipPlaced = checkShipRight(y, x, length, board);
                used1 = true;
            }
            if(dir == 2 && used2 == false) {
                shipPlaced = checkShipDown(y, x, length, board);
                used2 = true;
            }
            if(dir == 3 && used3 == false) {
                shipPlaced = checkShipLeft(y, x, length, board);
                used3 = true;
            }
            usedAll = used0 && used1 && used2 && used3;
        }
        return shipPlaced;
    }

    /**
     * �berpr�ft, ob ein einzelnes Schiff gesetzt werden kann (nach oben), und ruft eventuell placeShip auf
     * @param y			Koordinate
     * @param x			Koordinate
     * @param length	Schiffl�nge
     * @param board		Feld
     * @return			gibt an, ob das Schiff erfolgreich platziert wurde
     */
    private boolean checkShipUp(int y, int x, int length, char[][] board) {
        boolean placeable = false;
        //Pr�fung, ob alle zu pr�fenden Zellen auf dem Feld liegen
        if(y >= 0 && y < board.length && x >= 0 && x < board[y].length && y-length+1 >= 0) {
            placeable = true ;
            //Pr�fung, ob alle zu pr�fenden Zellen nutzbares Wasser sind
            for(int i = 0; i < length; i++) {
                if(board[y-i][x] != 'w') {
                    placeable = false;
                }
            }
        }
        if(placeable) {
            placeShipUp(y, x, length, board);
            return true;
        }
        return false;
    }

    /**
     * platziert ein einzelnes Schiff (nach oben)
     * @param y			Koordinate
     * @param x			Koordinate
     * @param length	Schiffl�nge
     * @param board		Feld
     */
    private void placeShipUp(int y, int x, int length, char[][] board) {
        for(int i = 0; i < length; i++) {
            board[y-i][x] = 'x';
        }
        blockTiles(board, 0, length, x, y);
    }

    /**
     * �berpr�ft, ob ein einzelnes Schiff gesetzt werden kann (nach rechts), und ruft eventuell placeShip auf
     * @param y			Koordinate
     * @param x			Koordinate
     * @param length	Schiffl�nge
     * @param board		Feld
     * @return			gibt an, ob das Schiff erfolgreich platziert wurde
     */
    private boolean checkShipRight(int y, int x, int length, char[][] board) {
        boolean placeable = false;
        //Pr�fung, ob alle zu pr�fenden Zellen auf dem Feld liegen
        if(y >= 0 && y < board.length && x >= 0 && x < board[y].length && x+length-1 < board[y].length) {
            placeable = true ;
            //Pr�fung, ob alle zu pr�fenden Zellen nutzbares Wasser sind
            for(int i = 0; i < length; i++) {
                if(board[y][x+i] != 'w') {
                    placeable = false;
                }
            }
        }
        if(placeable) {
            placeShipRight(y, x, length, board);
            return true;
        }
        return false;
    }

    /**
     * platziert ein einzelnes Schiff (nach rechts)
     * @param y			Koordinate
     * @param x			Koordinate
     * @param length	Schiffl�nge
     * @param board		Feld
     */
    private void placeShipRight(int y, int x, int length, char[][] board) {
        for(int i = 0; i < length; i++) {
            board[y][x+i] = 'x';
        }
        blockTiles(board, 1, length, x, y);
    }

    /**
     * �berpr�ft, ob ein einzelnes Schiff gesetzt werden kann (nach unten), und ruft eventuell placeShip auf
     * @param y			Koordinate
     * @param x			Koordinate
     * @param length	Schiffl�nge
     * @param board		Feld
     * @return			gibt an, ob das Schiff erfolgreich platziert wurde
     */
    private boolean checkShipDown(int y, int x, int length, char[][] board) {
        boolean placeable = false;
        //Pr�fung, ob alle zu pr�fenden Zellen auf dem Feld liegen
        if(y >= 0 && y < board.length && x >= 0 && x < board[y].length && y+length-1 < board.length) {
            placeable = true ;
            //Pr�fung, ob alle zu pr�fenden Zellen nutzbares Wasser sind
            for(int i = 0; i < length; i++) {
                if(board[y+i][x] != 'w') {
                    placeable = false;
                }
            }
        }
        if(placeable) {
            placeShipDown(y, x, length, board);
            return true;
        }
        return false;
    }

    /**
     * platziert ein einzelnes Schiff (nach unten)
     * @param y			Koordinate
     * @param x			Koordinate
     * @param length	Schiffl�nge
     * @param board		Feld
     */
    private void placeShipDown(int y, int x, int length, char[][] board) {
        for(int i = 0; i < length; i++) {
            board[y+i][x] = 'x';
        }
        blockTiles(board, 2, length, x, y);
    }

    /**
     * �berpr�ft, ob ein einzelnes Schiff gesetzt werden kann (nach links), und ruft eventuell placeShip auf
     * @param y			Koordinate
     * @param x			Koordinate
     * @param length	Schiffl�nge
     * @param board		Feld
     * @return			gibt an, ob das Schiff erfolgreich platziert wurde
     */
    private boolean checkShipLeft(int y, int x, int length, char[][] board) {
        boolean placeable = false;
        //Pr�fung, ob alle zu pr�fenden Zellen auf dem Feld liegen
        if(y >= 0 && y < board.length && x >= 0 && x < board[y].length && x-length+1 >= 0) {
            placeable = true ;
            //Pr�fung, ob alle zu pr�fenden Zellen nutzbares Wasser sind
            for(int i = 0; i < length; i++) {
                if(board[y][x-i] != 'w') {
                    placeable = false;
                }
            }
        }
        if(placeable) {
            placeShipLeft(y, x, length, board);
            return true;
        }
        return false;
    }

    /**
     * platziert ein einzelnes Schiff (nach links)
     * @param y			Koordinate
     * @param x			Koordinate
     * @param length	Schiffl�nge
     * @param board		Feld
     */
    private void placeShipLeft(int y, int x, int length, char[][] board) {
        for(int i = 0; i < length; i++) {
            board[y][x-i] = 'x';
        }
        blockTiles(board, 3, length, x, y);
    }

    /**
     * blockiert Zellen auf dem Spielfeld, sodass Schiffe nicht aneinander gesetzt werden
     * @param board		Feld
     * @param dir		Richtung in die das zu blockierende Schiff gesetzt wurde
     * @param length	Schiffsl�nge
     * @param x			Koordinate, von der aus das Schiff gesetzt wurde
     * @param y			Koordinate, von der aus das Schiff gesetzt wurde
     */
    private void blockTiles(char[][] board, int dir, int length, int x, int y) {
        //Positon der Schiffsenden
        int posAX;
        int posAY;
        int posBX;
        int posBY;
        //Schiff wurde nach oben gesetzt
        if(dir == 0){
            posAX = x;
            posAY = y-length+1;
            posBX = x;
            posBY = y;
        }
        //Schiff wurde nach unten gesetzt
        else if(dir == 2){
            posAX = x;
            posAY = y;
            posBX = x;
            posBY = y+length-1;
        }
        //Schiff wurde nach rechts gesetzt
        else if(dir == 1){
            posAX = x;
            posAY = y;
            posBX = x+length-1;
            posBY = y;
        }
        //Schiff wurde nach links gesetzt
        else{
            posAX = x-length+1;
            posAY = y;
            posBX = x;
            posBY = y;
        }
        //Schleife zur Aktualisierung des char[][]
        for(y=posAY-1; y<=posBY+1; y++) {
            for(x=posAX-1; x<=posBX+1; x++) {
                //Pr�fung, ob die berechnete Koordinate im Feld liegt
                if(y >= 0 && y < board.length && x >= 0 && x < board[y].length) {
                    //Pr�fung, ob das zu blockierende Feld noch unbelegt ist
                    if(board[y][x] == 'w') {
                        board[y][x] = 'b';
                    }
                } //Ende der Pr�fungen
            }
        } //Ende der Schleifen
    }

    /**
     * ersetzt alle chars auf einem Spielfeld
     * @param a		urspr�ngliche chars
     * @param b		neue chars
     * @param board	Feld
     */
    private void swapChars(char a, char b, char[][] board) {
        int x, y;
        for(y=0; y<board.length; y++) {
            for(x=0; x<board[y].length; x++) {
                if(board[y][x] == a) {
                    board[y][x] = b;
                }
            }
        }
    }

    /**
     * Main-Methode
     * @param args
     */
    public static void main(String[] args) {
        Game g = new Game(10, 10);
        g.placeShips(g.board2);
        g.displayBoards();
    }
}

/*
 * w = Wasser
 * x = Schiff
 * z = zerst�rtes Schiff
 * b = blockierte Zelle
 */
