package shipz;

import javafx.stage.Stage;
import shipz.gui.GUI;
import shipz.io.FileStream;
import shipz.network.Network;
import shipz.util.GameEvent;
import shipz.util.GameEventListener;
import shipz.util.NoDrawException;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

import shipz.ai.*;

/**
 * Spielverwaltung
 * @author Max
 * @version	1.0
 */
public class Game implements GameEventListener {

    //IV
    /** Spielfeld des 1. Spielers */
    public char[][] board1;
    /** Spielfeld des 2. Spielers */
    public char[][] board2;
    /** Verweis auf den 1. Spieler */
    private Player player1;
    /** Verweis auf den 2. Spieler */
    private Player player2;
    /** gibt an, ob Spieler 1 aktiv ist */
    private boolean player1active;
    /** Netzwerkverbindung */
    private Network network;
    /** grafische Nutzeroberfläche */
    private GUI gui;
    /** Spielstandverwaltung */
    private FileStream filestream;
    /** Schwierigkeitsgrad der ersten KI */
    private int player1diff;
    /** Schwierigkeitsgrad der zweiten KI */
    private int player2diff;
    /** Liste mit den zu verwendenden Schiffen */
    public List<Integer> shipList;
    /** aktive x-Koordinate */
    private int aX;
    /** aktive y-Koordinate */
    private int aY;
    /** letztberechnetes Ergebnis */
    private byte aResult;
    /** Gibt an, in welchem Spielmodus das Spiel abläuft
     *  1   PvP
     *  2   PvK
     *  3   KvK
     */
    private byte mode = 0;

    /** Gibt an, ob die Instanz zurzeit als Host fungiert */
    boolean isHost = false;


    //Constructor
    /**
     * erstellt ein neues Spiel mit leeren Feldern
     * @param primaryStage  wird für das Erstellen der GUI verwendet
     */
    public Game(Stage primaryStage) {
        gui = new GUI(primaryStage);
        gui.setEventListener(this);
        player1active = true;
        filestream = new FileStream();

    }
    //Methoden

    /** Setz alle Zellen der Spielfelder auf Wasser */
    private void initiateBoards() {
        //1. Zähler
        int y;
        //2. Zähler
        int x;
        //doppelte Schleife für Durchlauf durch alle Felder
        for(y=0; y<board1.length; y++) {
            for(x=0; x<board1[y].length; x++) {
                board1[y][x] = 'w';
            }//Ende ySchleife
        }//Ende xSchleife
        for(y=0; y<board1.length; y++) {
            for(x=0; x<board1[y].length; x++) {
                board2[y][x] = 'w';
            }//Ende ySchleife
        }//Ende xSchleife
    }


    /**
     * überprüft die übergebenen Koordinaten auf Schiffelemente und ruft eventuell sink() auf
     * @param x 	Koordinate
     * @param y 	Koordinate
     * @return      0 Wasser getroffen, 1 Schiff getroffen, 2 Schiff versenkt
     */
    private byte checkTile(int x, int y) {
        byte r = 0;
        if(player1active) {
            if (board2[y][x] == 'x') {
                r = 1;
                if (sink(x, y)) {
                    r = 2;
                }
                //System.out.println("Es wurde ein Schiffelement zerstört");
                return r;
            } else {
                //System.out.println("Es wurde kein Schiffelement zerstört");
                board2[y][x] = 'W';
                return r;
            }
        }
        else {
            if (board1[y][x] == 'x') {
                r = 1;
                if (sink(x, y)) {
                    r = 2;
                }
                //System.out.println("Es wurde ein Schiffelement zerstört");
                return r;
            } else {
                //System.out.println("Es wurde kein Schiffelement zerstört");
                board1[y][x] = 'W';
                return r;
            }
        }
    }

    /**
     * setzt die angegebene Zelle auf ein zerstörtes Schiffelement
     * @param x		Koordinate
     * @param y 	Koordinate
     * @return		gibt an, ob das gesamte Schiff versenkt wurde
     */
    private boolean sink(int x, int y) {
        if(player1active) {
            board2[y][x] = 'z';
            return (checkShipDestroyed(x, y));
        }
        else {
            board1[y][x] = 'z';
            return (checkShipDestroyed(x, y));
        }
    }

    /**
     * prüft den Gesamtzustand des Schiffs
     * @param x		Koordinate
     * @param y 	Koordinate
     * @return		gibt an, ob das gesamte Schiff versenkt wurde
     */
    private boolean checkShipDestroyed(int x, int y) {
        return (checkShipDestroyedUp(x, y) && checkShipDestroyedRight(x, y) && checkShipDestroyedDown(x, y) && checkShipDestroyedLeft(x, y));
    }

    /**
     * prüft den Gesamtzustand des Schiffs nach oben
     * @param x		Koordinate
     * @param y 	Koordinate
     * @return		gibt an, ob das gesamte Schiff versenkt wurde
     */
    private boolean checkShipDestroyedUp(int x, int y) {
        if(y-1 >= 0) {
            if(player1active) {
                if (board2[y - 1][x] == 'w') {                        //Wasser auf angrenzendem Feld
                    return true;                                //Schiff wurde vielleicht versenkt
                } else if (board2[y - 1][x] == 'x') {                    //Schiffelement auf angrenzendem Feld
                    return false;                                //Schiff wurde nicht versenkt
                } else {                                            //zerstörtes Schiffelement auf angrenzendem Feld
                    return checkShipDestroyedUp(x, y - 1);    //weitere Prüfung vom angrenzenden Feld
                }
            }
            else {
                if (board1[y - 1][x] == 'w') {                        //Wasser auf angrenzendem Feld
                    return true;                                //Schiff wurde vielleicht versenkt
                } else if (board1[y - 1][x] == 'x') {                    //Schiffelement auf angrenzendem Feld
                    return false;                                //Schiff wurde nicht versenkt
                } else {                                            //zerstörtes Schiffelement auf angrenzendem Feld
                    return checkShipDestroyedUp(x, y - 1);    //weitere Prüfung vom angrenzenden Feld
                }
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
     * @return		gibt an, ob das gesamte Schiff versenkt wurde
     */
    private boolean checkShipDestroyedRight(int x, int y) {
        if(player1active) {
            if (x + 1 < board2[y].length) {
                if (board2[y][x + 1] == 'w') {                            //Wasser auf angrenzendem Feld
                    return true;                                    //Schiff wurde vielleicht versenkt
                } else if (board2[y][x + 1] == 'x') {                        //Schiffelement auf angrenzendem Feld
                    return false;                                    //Schiff wurde nicht versenkt
                } else {                                                //zerstörtes Schiffelement auf angrenzendem Feld
                    return checkShipDestroyedRight(x + 1, y);    //weitere Prüfung vom angrenzenden Feld
                }
            } else {
                return true;
            }
        }
        else {
            if (x + 1 < board1[y].length) {
                if (board1[y][x + 1] == 'w') {                            //Wasser auf angrenzendem Feld
                    return true;                                    //Schiff wurde vielleicht versenkt
                } else if (board1[y][x + 1] == 'x') {                        //Schiffelement auf angrenzendem Feld
                    return false;                                    //Schiff wurde nicht versenkt
                } else {                                                //zerstörtes Schiffelement auf angrenzendem Feld
                    return checkShipDestroyedRight(x + 1, y);    //weitere Prüfung vom angrenzenden Feld
                }
            } else {
                return true;
            }
        }
    }

    /**
     * prüft den Gesamtzustand des Schiffs nach unten
     * @param x		Koordinate
     * @param y 	Koordinate
     * @return		gibt an, ob das gesamte Schiff versenkt wurde
     */
    private boolean checkShipDestroyedDown(int x, int y) {
        if(player1active) {
            if (y + 1 < board2.length) {
                if (board2[y + 1][x] == 'w') {                        //Wasser auf angrenzendem Feld
                    return true;                                //Schiff wurde vielleicht versenkt
                } else if (board2[y + 1][x] == 'x') {                    //Schiffelement auf angrenzendem Feld
                    return false;                                //Schiff wurde nicht versenkt
                } else {                                            //zerstörtes Schiffelement auf angrenzendem Feld
                    return checkShipDestroyedDown(x, y + 1);    //weitere Prüfung vom angrenzenden Feld
                }
            } else {
                return true;
            }
        }
        else {
            if (y + 1 < board1.length) {
                if (board1[y + 1][x] == 'w') {                        //Wasser auf angrenzendem Feld
                    return true;                                //Schiff wurde vielleicht versenkt
                } else if (board1[y + 1][x] == 'x') {                    //Schiffelement auf angrenzendem Feld
                    return false;                                //Schiff wurde nicht versenkt
                } else {                                            //zerstörtes Schiffelement auf angrenzendem Feld
                    return checkShipDestroyedDown(x, y + 1);    //weitere Prüfung vom angrenzenden Feld
                }
            } else {
                return true;
            }
        }
    }

    /**
     * prüft den Gesamtzustand des Schiffs nach links
     * @param x		Koordinate
     * @param y 	Koordinate
     * @return		gibt an, ob das gesamte Schiff versenkt wurde
     */
    private boolean checkShipDestroyedLeft(int x, int y) {
        if(x-1 >= 0) {
            if(player1active) {
                if (board2[y][x - 1] == 'w') {                            //Wasser auf angrenzendem Feld
                    return true;                                    //Schiff wurde vielleicht versenkt
                } else if (board2[y][x - 1] == 'x') {                        //Schiffelement auf angrenzendem Feld
                    return false;                                    //Schiff wurde nicht versenkt
                } else {                                                //zerstörtes Schiffelement auf angrenzendem Feld
                    return checkShipDestroyedLeft(x - 1, y);    //weitere Prüfung vom angrenzenden Feld
                }
            }
            else {
                if (board1[y][x - 1] == 'w') {                            //Wasser auf angrenzendem Feld
                    return true;                                    //Schiff wurde vielleicht versenkt
                } else if (board1[y][x - 1] == 'x') {                        //Schiffelement auf angrenzendem Feld
                    return false;                                    //Schiff wurde nicht versenkt
                } else {                                                //zerstörtes Schiffelement auf angrenzendem Feld
                    return checkShipDestroyedLeft(x - 1, y);    //weitere Prüfung vom angrenzenden Feld
                }
            }
        }
        else {
            return true;
        }
    }

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
     * platziert alle Schiffe eines Spielers auf seinem Feld
     * @param player	Feld
     */
    public void placeShips(int player) {
        for(Integer i : shipList) {
            if(player == 1) {
                while (placeSingleShip(randomNumber(0, board1.length), randomNumber(0, board1[0].length), i, player) == false) {
                }
            }
            else {
                while (placeSingleShip(randomNumber(0, board2.length), randomNumber(0, board2[0].length), i, player) == false) {
                }
            }
        }
        swapChars('b', 'w', player);
    }

    /**
     * platziert ein einzelnes Schiff
     * @param y			Koordinate
     * @param x			Koordinate
     * @param length	Schifflänge
     * @return			gibt an, ob das Schiff erfolgreich platziert wurde
     */
    private boolean placeSingleShip(int y, int x, int length, int player) {
        boolean shipPlaced = false;
        boolean used0 = false;
        boolean used1 = false;
        boolean used2 = false;
        boolean used3 = false;
        boolean usedAll = false;
        while(usedAll == false && shipPlaced == false) {				//wird wiederholt, bis alle Richtungen probiert wurden oder das Schiff gesetzt wird
            int dir = randomNumber(0, 3);	//Richtung: 0=hoch, 1=recht, 2=runter, 3=links
            if(dir == 0 && used0 == false) {
                shipPlaced = checkShipUp(y, x, length, player);
                used0 = true;
            }
            if(dir == 1 && used1 == false) {
                shipPlaced = checkShipRight(y, x, length, player);
                used1 = true;
            }
            if(dir == 2 && used2 == false) {
                shipPlaced = checkShipDown(y, x, length, player);
                used2 = true;
            }
            if(dir == 3 && used3 == false) {
                shipPlaced = checkShipLeft(y, x, length, player);
                used3 = true;
            }
            usedAll = used0 && used1 && used2 && used3;
        }
        return shipPlaced;
    }

    /**
     * überprüft, ob ein einzelnes Schiff gesetzt werden kann (nach oben), und ruft eventuell placeShip auf
     * @param y			Koordinate
     * @param x			Koordinate
     * @param length	Schifflänge
     * @param player	Feld
     * @return			gibt an, ob das Schiff erfolgreich platziert wurde
     */
    private boolean checkShipUp(int y, int x, int length, int player) {
        boolean placeable = false;
        //Prüfung, ob alle zu prüfenden Zellen auf dem Feld liegen
        if(player == 1) {
            if (y >= 0 && y < board1.length && x >= 0 && x < board1[y].length && y - length + 1 >= 0) {
                placeable = true;
                //Prüfung, ob alle zu prüfenden Zellen nutzbares Wasser sind
                for (int i = 0; i < length; i++) {
                    if (board1[y - i][x] != 'w') {
                        placeable = false;
                    }
                }
            }
        }
        else {
            if (y >= 0 && y < board2.length && x >= 0 && x < board2[y].length && y - length + 1 >= 0) {
                placeable = true;
                //Prüfung, ob alle zu prüfenden Zellen nutzbares Wasser sind
                for (int i = 0; i < length; i++) {
                    if (board2[y - i][x] != 'w') {
                        placeable = false;
                    }
                }
            }
        }
        if(placeable) {
            placeShipUp(y, x, length, player);
            return true;
        }
        return false;
    }

    /**
     * platziert ein einzelnes Schiff (nach oben)
     * @param y			Koordinate
     * @param x			Koordinate
     * @param length	Schifflänge
     * @param player	Feld
     */
    private void placeShipUp(int y, int x, int length, int player) {
        for(int i = 0; i < length; i++) {
            if(player == 1) {
                board1[y - i][x] = 'x';
                gui.draw(y-i, x, 1, 1);
            }
            else {
                board2[y - i][x] = 'x';
                gui.draw(y-i, x, 2, 1);
            }
        }
        blockTiles(player, 0, length, x, y);
        //gui.drawShip(y, x, player, length, 'r');
    }

    /**
     * überprüft, ob ein einzelnes Schiff gesetzt werden kann (nach rechts), und ruft eventuell placeShip auf
     * @param y			Koordinate
     * @param x			Koordinate
     * @param length	Schifflänge
     * @param player	Feld
     * @return			gibt an, ob das Schiff erfolgreich platziert wurde
     */
    private boolean checkShipRight(int y, int x, int length, int player) {
        boolean placeable = false;
        if(player == 1) {
            //Prüfung, ob alle zu prüfenden Zellen auf dem Feld liegen
            if (y >= 0 && y < board1.length && x >= 0 && x < board1[y].length && x + length - 1 < board1[y].length) {
                placeable = true;
                //Prüfung, ob alle zu prüfenden Zellen nutzbares Wasser sind
                for (int i = 0; i < length; i++) {
                    if (board1[y][x + i] != 'w') {
                        placeable = false;
                    }
                }
            }
            if (placeable) {
                placeShipRight(y, x, length, player);
                return true;
            }
            return false;
        }
        else {
            //Prüfung, ob alle zu prüfenden Zellen auf dem Feld liegen
            if (y >= 0 && y < board2.length && x >= 0 && x < board2[y].length && x + length - 1 < board2[y].length) {
                placeable = true;
                //Prüfung, ob alle zu prüfenden Zellen nutzbares Wasser sind
                for (int i = 0; i < length; i++) {
                    if (board2[y][x + i] != 'w') {
                        placeable = false;
                    }
                }
            }
            if (placeable) {
                placeShipRight(y, x, length, player);
                return true;
            }
            return false;
        }
    }

    /**
     * platziert ein einzelnes Schiff (nach rechts)
     * @param y			Koordinate
     * @param x			Koordinate
     * @param length	Schifflänge
     * @param player	Feld
     */
    private void placeShipRight(int y, int x, int length, int player) {
        for(int i = 0; i < length; i++) {
            if (player == 1) {
                board1[y][x + i] = 'x';
                gui.draw(y, x+i, 1, 1);
            }
            else {
                board2[y][x + i] = 'x';
                gui.draw(y, x+i, 2, 1);
            }
        }
        blockTiles(player, 1, length, x, y);
        //gui.drawShip(y, x, player, length, 'u');
    }

    /**
     * überprüft, ob ein einzelnes Schiff gesetzt werden kann (nach unten), und ruft eventuell placeShip auf
     * @param y			Koordinate
     * @param x			Koordinate
     * @param length	Schifflänge
     * @param player	Feld
     * @return			gibt an, ob das Schiff erfolgreich platziert wurde
     */
    private boolean checkShipDown(int y, int x, int length, int player) {
        boolean placeable = false;
        if(player == 1) {
            //Prüfung, ob alle zu prüfenden Zellen auf dem Feld liegen
            if (y >= 0 && y < board1.length && x >= 0 && x < board1[y].length && y + length - 1 < board1.length) {
                placeable = true;
                //Prüfung, ob alle zu prüfenden Zellen nutzbares Wasser sind
                for (int i = 0; i < length; i++) {
                    if (board1[y + i][x] != 'w') {
                        placeable = false;
                    }
                }
            }
            if (placeable) {
                placeShipDown(y, x, length, player);
                return true;
            }
            return false;
        }
        else{
            //Prüfung, ob alle zu prüfenden Zellen auf dem Feld liegen
            if (y >= 0 && y < board2.length && x >= 0 && x < board2[y].length && y + length - 1 < board2.length) {
                placeable = true;
                //Prüfung, ob alle zu prüfenden Zellen nutzbares Wasser sind
                for (int i = 0; i < length; i++) {
                    if (board2[y + i][x] != 'w') {
                        placeable = false;
                    }
                }
            }
            if (placeable) {
                placeShipDown(y, x, length, player);
                return true;
            }
            return false;
        }
    }

    /**
     * platziert ein einzelnes Schiff (nach unten)
     * @param y			Koordinate
     * @param x			Koordinate
     * @param length	Schifflänge
     * @param player	Feld
     */
    private void placeShipDown(int y, int x, int length, int player) {
        for(int i = 0; i < length; i++) {
            if(player == 1) {
                board1[y + i][x] = 'x';
                gui.draw(y+i, x, 1, 1);
            }
            else {
                board2[y + i][x] = 'x';
                gui.draw(y+i, x, 2, 1);
            }
        }
        blockTiles(player, 2, length, x, y);
        //gui.drawShip(y, x, player, length, 'l');
    }

    /**
     * überprüft, ob ein einzelnes Schiff gesetzt werden kann (nach links), und ruft eventuell placeShip auf
     * @param y			Koordinate
     * @param x			Koordinate
     * @param length	Schifflänge
     * @param player	Feld
     * @return			gibt an, ob das Schiff erfolgreich platziert wurde
     */
    private boolean checkShipLeft(int y, int x, int length, int player) {
        boolean placeable = false;
        //Prüfung, ob alle zu prüfenden Zellen auf dem Feld liegen
        if(player == 1) {
            if (y >= 0 && y < board1.length && x >= 0 && x < board1[y].length && x - length + 1 >= 0) {
                placeable = true;
                //Prüfung, ob alle zu prüfenden Zellen nutzbares Wasser sind
                for (int i = 0; i < length; i++) {
                    if (board1[y][x - i] != 'w') {
                        placeable = false;
                    }
                }
            }
        }
        else {
            if (y >= 0 && y < board2.length && x >= 0 && x < board2[y].length && x - length + 1 >= 0) {
                placeable = true;
                //Prüfung, ob alle zu prüfenden Zellen nutzbares Wasser sind
                for (int i = 0; i < length; i++) {
                    if (board2[y][x - i] != 'w') {
                        placeable = false;
                    }
                }
            }
        }
        if(placeable) {
            placeShipLeft(y, x, length, player);
            return true;
        }
        return false;
    }

    /**
     * platziert ein einzelnes Schiff (nach links)
     * @param y			Koordinate
     * @param x			Koordinate
     * @param length	Schifflänge
     * @param player	Feld
     */
    private void placeShipLeft(int y, int x, int length, int player) {
        for(int i = 0; i < length; i++) {
            if(player == 1) {
                board1[y][x - i] = 'x';
                gui.draw(y, x-i, 1, 1);
            }
            else {
                board2[y][x - i] = 'x';
                gui.draw(y, x-i, 2, 1);
            }
        }
        blockTiles(player, 3, length, x, y);
        //gui.drawShip(y, x, player, length, 'u');
    }

    /**
     * blockiert Zellen auf dem Spielfeld, sodass Schiffe nicht aneinander gesetzt werden
     * @param player	Feld
     * @param dir		Richtung in die das zu blockierende Schiff gesetzt wurde
     * @param length	Schiffslänge
     * @param x			Koordinate, von der aus das Schiff gesetzt wurde
     * @param y			Koordinate, von der aus das Schiff gesetzt wurde
     */
    private void blockTiles(int player, int dir, int length, int x, int y) {
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
                if(player == 1) {
                    //Prüfung, ob die berechnete Koordinate im Feld liegt
                    if (y >= 0 && y < board1.length && x >= 0 && x < board1[y].length) {
                        //Prüfung, ob das zu blockierende Feld noch unbelegt ist
                        if (board1[y][x] == 'w') {
                            board1[y][x] = 'b';
                        }
                    } //Ende der Prüfungen
                }
                else {
                    //Prüfung, ob die berechnete Koordinate im Feld liegt
                    if (y >= 0 && y < board2.length && x >= 0 && x < board2[y].length) {
                        //Prüfung, ob das zu blockierende Feld noch unbelegt ist
                        if (board2[y][x] == 'w') {
                            board2[y][x] = 'b';
                        }
                    } //Ende der Prüfungen
                }
            }
        } //Ende der Schleifen
    }

    /**
     * ersetzt alle chars auf einem Spielfeld
     * @param a		    ursprüngliche chars
     * @param b		    neue chars
     * @param player    Feld
     */
    public void swapChars(char a, char b, int player) {
        int x, y;
        if(player == 1) {
            for (y = 0; y < board1.length; y++) {
                for (x = 0; x < board1[y].length; x++) {
                    if (board1[y][x] == a) {
                        board1[y][x] = b;
                    }
                }
            }
        }
        else {
            for (y = 0; y < board2.length; y++) {
                for (x = 0; x < board2[y].length; x++) {
                    if (board2[y][x] == a) {
                        board2[y][x] = b;
                    }
                }
            }
        }
    }

    /**
     * generiert eine zufällige Zahl
     * @param min	kleinstmögliche gewünschte Zahl
     * @param max	größtmögliche gewünschte Zahl
     * @return		zufällige Zahl zwischen min und max
     */
    private int randomNumber(int min, int max) {
        int range = (max - min) + 1;
        return (int)(Math.random() * range) + min;
    }

    /**
     * erstellt aus einem String eine Schiffsliste
     * @param s	numerischer Wert eines chars steht für die Länge eines Schiffs
     * @return	Integer-Liste mit einem Eintrag für jedes Schiff
     */
    public List<Integer> createShipList(String s) {
        List<Integer> r = new ArrayList<Integer>();
        for(int i=0; i<s.length(); i++) {
            r.add(Character.getNumericValue(s.charAt(i)));
        }
        return r;
    }

    /**
     * zeichnet alle Schiffe auf die GUI
     */
    public void drawShipOnGUI() {
        int x, y;
        for(y=0; y<board1.length; y++) {
            for (x = 0; x < board1[y].length; x++) {
                if(board1[y][x] == 'W') {
                    gui.draw(y, x, 1, 0);
                }
                if(board1[y][x] == 'z') {
                    gui.draw(y, x, 1, 2);
                }
            }
        }
        for(y=0; y<board2.length; y++) {
            for (x = 0; x < board2[y].length; x++) {
                if(board2[y][x] == 'W') {
                    gui.draw(y, x, 2, 0);
                }
                if(board2[y][x] == 'z') {
                    gui.draw(y, x, 2, 2);
                }
            }
        }
    }

    private void clearGUI() {
        int x, y;
        for(y=0; y<board1.length; y++) {
            for (x = 0; x < board1[y].length; x++) {
                gui.draw(y, x, 1, -1);
                gui.draw(y, x, 2, -1);
            }
        }
    }

    /**
     * führt die nächste Runde eine KI-Spielers aus
     */
    private void nextRoundAI() {
        Player activePlayer;
        //int aiTimer = filestream.getAiTimer();
        if (player1active) {
            activePlayer = player1;
        }
        else {
            activePlayer = player2;
        }
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {@Override
            public void run() {
                aX = activePlayer.getX();
                aY = activePlayer.getY();
                aResult = checkTile(aX, aY);
                activePlayer.shootResult(aY, aX, aResult);
                System.out.println("Spieler " + activePlayer() + ": " + aY + "/" + aX + " => " + aResult);
                if(player1active) {
                    if (aResult == 0) {
                        gui.draw(aY, aX, 2, 0);
                    } else if(aResult == 1) {
                        gui.draw(aY, aX, 2, 2);
                    } else {
                        gui.draw(aY, aX, 2, 3);
                    }
                }
                else {
                    if (aResult == 0) {
                        gui.draw(aY, aX, 1, 0);
                    } else if(aResult == 1) {
                        gui.draw(aY, aX, 1, 2);
                    } else {
                        gui.draw(aY, aX, 1, 3);
                    }
                }
            }
    }, 1000);
    }

    /**
     * Führt einen Zug für den Spielmodus PvK aus
     */
    private void nextRoundHumanVsAi() {
        if (player1active) {
            gui.setEnableField(2);
        } else if (!player1active) {
            aX = player2.getX();
            aY = player2.getY();
            aResult = checkTile(aX, aY);
            player2.shootResult(aY, aX, aResult);
            System.out.println("Spieler " + activePlayer() + ": " + aY + "/" + aX + " => " + aResult);

            if (aResult == 0) {
                gui.draw(aY, aX, 1, 0);
            } else if(aResult == 1){
                gui.draw(aY, aX, 1, 2);
            } else {
                gui.draw(aY, aX, 1, 3);
            }
        }
    }

    /**
     * Führt einen Zug aus, wenn beide Spieler menschlich sind
     */
    private void nextRoundHuman() {
        if(isHost || network == null) {
            aX = gui.getX();
            aY = gui.getY();
        }
        if(network != null && !player1active) {
            aX = network.getY();
            aY = network.getX();
        }
        aResult = checkTile(aX, aY);
        System.out.println("Spieler " + activePlayer() + ": " + aY + "/" + aX + " => " + aResult);

        if(aResult == 0) {
            if(isHost) {
                network.send(NET_SHOOT_EVENT + ":" + aY + "," + aX + "," + reverseActivePlayer() + "," + 0);
            }
            gui.draw(aY, aX, reverseActivePlayer(), 0);
        }
        else if(aResult == 1){
            if(isHost) {
                network.send(NET_SHOOT_EVENT + ":" + aY + "," + aX + "," + reverseActivePlayer() + "," + 2);
            }
            gui.draw(aY, aX, reverseActivePlayer(), 2);
        }
        else if(aResult == 2){
            if(isHost) {
                network.send(NET_SHOOT_EVENT + ":" + aY + "," + aX + "," + reverseActivePlayer() + "," + 3);
            }
            gui.draw(aY, aX, reverseActivePlayer(), 3);
        }
    }


    /**
     * Überprüft den aktuellen Spielstand
     * @return gibt an, ob das Spiel beendet wurde und wer gewonnen hat
     */
    private byte gameFinished() {
        boolean r1 = true;
        boolean r2 = true;
        //1. Zähler
        int y;
        //2. Zähler
        int x;

        //doppelte Schleife für Durchlauf durch alle Felder
        for (y = 0; y < board1.length; y++) {
            for (x = 0; x < board1[y].length; x++) {
                if (board1[y][x] == 'x') {
                    r1 = false;
                }
            }//Ende ySchleife
        }//Ende xSchleife

        //doppelte Schleife für Durchlauf durch alle Felder
        for (y = 0; y < board2.length; y++) {
            for (x = 0; x < board2[y].length; x++) {
                if (board2[y][x] == 'x') {
                    r2 = false;
                }
            }//Ende ySchleife
        }//Ende xSchleife

        if(r1 && !r2) {
            return 2;
        }
        else if(!r1 && r2) {
            return 1;
        }
        else {
            return 0;
        }
    }


    /**
     * wird ausgeführt, wenn ein Zug ausgeführt werden soll
     */
    private void cycle() {
        if(mode == 1) {
            if(player1active) {
                gui.setEnableField(2);
                if(isHost) {
                    network.send(NET_ENABLE_GUI + ":" + 0);
                }
            }
            else {
                gui.setEnableField(1);
                if(isHost) {
                    gui.setEnableField(0);
                    network.send(NET_ENABLE_GUI + ":" + 1);
                }

            }
        }
        if(mode == 2) {
           nextRoundHumanVsAi();
        }
        if(mode == 3) {
            nextRoundAI();
        }
    }

    @Override
    /**
     * verwaltet die eingehenden Events
     * @param e zu bearbeitendes Event
     */
    public void eventReceived(GameEvent e) {
        int id = e.getId();

        switch(id) {
            case GUI_SHOOT_EVENT:
                if(isHost || network == null) {
                    gui.setEnableField(0);
                    nextRoundHuman();
                    cycle();
                }
                else {
                    network.shootRequest(gui.getX(), gui.getY());
                }

                break;
            case NET_SHOOT_REQUEST:
                if(isHost) {
                    gui.setEnableField(0);
                    network.send(NET_ENABLE_GUI + ":" + 0);
                    nextRoundHuman();
                    cycle();
                }

                break;
            case FILL_EVENT:
                clear();
                board1 = new char[gui.getFieldSize()][gui.getFieldSize()];
                board2 = new char[gui.getFieldSize()][gui.getFieldSize()];
                initiateBoards();
                String s = "5443332";
                if(gui.getFieldSize() >= 15) {
                    s += s;
                    if(gui.getFieldSize() == 20) {
                        s += s;
                    }
                }
                shipList = createShipList(s);
                placeShips(1);
                placeShips(2);
                displayBoards();
                if(mode == 2 || mode == 3) {
                    int ki2mode = gui.getKi2Mode();
                    if(ki2mode == 1) {
                        player2 = new Easy(gui.getFieldSize(), true, shipList);
                    }
                    else if(ki2mode == 2) {
                        player2 = new Normal(gui.getFieldSize(), true, shipList);
                    }
                    else {
                        player2 = new Hard(gui.getFieldSize(), true, shipList);
                    }
                    System.out.println("Player 2: " + ki2mode);
                }
                if(mode == 3) {
                    int ki1mode = gui.getKi1Mode();
                    if(ki1mode == 1) {
                        player1 = new Easy(gui.getFieldSize(), true, shipList);
                    }
                    else if(ki1mode == 2) {
                        player1 = new Normal(gui.getFieldSize(), true, shipList);
                    }
                    else {
                        player1 = new Hard(gui.getFieldSize(), true, shipList);
                    }
                    System.out.println("Player 1: " + ki1mode);
                }
                break;
            case LOCK_EVENT:
                clearGUI();
                cycle();
                break;
            case FINISHED_ROUND:
                displaySingleBoard(board1);
                gui.setEnableField(0);
            	filestream.newDraw(aX, aY, activePlayer(), aResult);
            	gui.setComboLabel(filestream.getComboValue(1), 1);
            	gui.setComboLabel(filestream.getComboValue(2), 2);
            	gui.setScoreLabel(filestream.getScore(1), 1);
            	gui.setScoreLabel(filestream.getScore(2), 2);
                if(network != null && isHost) {
                    network.send(
                        NET_HIGHSCORE + ":"
                        + filestream.getComboValue(1) + ","
                        + filestream.getComboValue(2) + ","
                        + filestream.getScore(1) + ","
                        + filestream.getScore(2)
                    );

                }
                if(gameFinished() == 0) {
                    if(aResult == 0) {
                        changeActivePlayer();
                    }
                    cycle();
                }
                else {
                    System.out.println("Spieler " + gameFinished() + " hat das Spiel gewonnen");
                    //gui.setNewRow(filestream.get)
                    filestream.saveScoreToFile(gui.getPlayername(1), gui.getPlayername(2));
                    gui.setEndMessage(1);
                    gui.setEndScreen();
                }
                break;
            case UNDO_EVENT:
            	try {
                    if(player2 != null) {
                        player2.undoHits(filestream.undoDraw(activePlayer()));
                    }
            		undo(filestream.undoDraw(activePlayer()));
            	} catch (NoDrawException x) {
            		x.printStackTrace();
            		gui.drawWarning();
            	}
            	break;
            case REDO_EVENT:
				try {
					redo(filestream.redoDraw(activePlayer()));
				} catch (NoDrawException x) {
					System.out.println("Es sind keine Züge mehr vorhanden, die wiederholt werden können!"); // muss noch als Dialog umgesetzt werden
					gui.drawWarning();
				}
				break;
            case SAVE_EVENT:
                if(mode == 1) {
                    filestream.saveGame(gui.getFilename(), checkPlayername(gui.getPlayername(1)), checkPlayername(gui.getPlayername(2)), boardToString(1), boardToString(2), (int)boardSize(), activePlayer(), null, mode+",0,0");
                } else if(mode == 2) {
            		filestream.saveGame(gui.getFilename(), checkPlayername(gui.getPlayername(1)), checkPlayername(gui.getPlayername(2)), boardToString(1), boardToString(2), (int)boardSize(), activePlayer(), null, mode+",0,"+player2diff, player1.saveCurrentGame());
                } else if(mode == 3) {
                    filestream.saveGame(gui.getFilename(), checkPlayername(gui.getPlayername(1)), checkPlayername(gui.getPlayername(2)), boardToString(1), boardToString(2), (int) boardSize(), activePlayer(), null, mode+","+player1diff+","+player2diff, player1.saveCurrentGame(), player2.saveCurrentGame());
                }
            	break;
            case LOAD_EVENT:
            	String[] saves = filestream.getAllGameNames().split(",");
//            	String gameName = saves[saves.length-1];
//            	loadGame(gameName);
//            	System.out.println("Der Spielstand " + gameName + " wurde geladen!");
            	gui.clearTables();
            	for(int i = 0; i < saves.length; i++) {
            		gui.setSavedGame(saves[i], filestream.getPlayerName(saves[i]), filestream.getOpponentName(saves[i]), filestream.getGamemode(saves[i]), filestream.getTime(saves[i]).replaceAll("_", " "));
            	}
            	break;
            case LOAD_TABLE_EVENT:
            	System.out.println("Loading Game " + gui.getGamename() + "...");
            	loadGame(gui.getGamename());
            	System.out.println("Game " + gui.getGamename() + " successfully loaded!");
            case PVP_EVENT:
                mode = 1;
                System.out.println("Mode = " + mode);   //Test
                break;
            case PVK_EVENT:
                mode = 2;
                System.out.println("Mode = " + mode);   //Test
                break;
            case KVK_EVENT:
                mode = 3;
                System.out.println("Mode = " + mode);   //Test
                break;
            case HIGHSCORE_EVENT:
                highscore();
                break;
            case CONNECT_EVENT:
                isHost = gui.isHost();

                int port;
                if(Pattern.matches("[0-9]*", gui.getPort())) port = Integer.parseInt(gui.getPort());
                else port = -1;

                String ip = gui.getIp();

                network = new Network(isHost);
                gui.setIsNetwork(true);
                try {
                    if(isHost) network.connect(port);
                    else network.connect(ip, port);

                    network.setEventListener(this);
                    (new Thread(network)).start();

                    gui.setConnected(true);
                    //if(isHost) st
                } catch(Exception ex) {
                    System.out.println(ex.getMessage());
                }
                break;
            case SEND_EVENT:
                String msg = network.getMessage();
                System.out.println(msg);
                byte action = Byte.parseByte(msg.split(":")[0]);
                String data = msg.split(":")[1];

                switch(action) {
                    case NET_SHOOT_EVENT:
                        String[] values = data.split(",");
                        int y = Integer.parseInt(values[0]);
                        int x = Integer.parseInt(values[1]);
                        int board = Integer.parseInt(values[2]);
                        int result = Integer.parseInt(values[3]);
                        gui.draw(y, x, board, result);
                        break;
                    case NET_ENABLE_GUI:
                        int i = Integer.parseInt(data);
                        gui.setEnableField(i);
                        break;
                    case NET_HIGHSCORE:
                        String[] score = data.split(",");
                        int comboValue1 = Integer.parseInt(score[0]);
                        int comboValue2 = Integer.parseInt(score[1]);
                        int score1 = Integer.parseInt(score[2]);
                        int score2 = Integer.parseInt(score[3]);
                        gui.setComboLabel(comboValue1, 1);
                        gui.setComboLabel(comboValue2, 2);
                        gui.setScoreLabel(score1, 1);
                        gui.setScoreLabel(score2, 2);
                        break;
                    case NET_GAMEOVER:
                        gameOver();
                        break;
                }
        }
    }

    /**
     * Gibt die abgespeicherten Highscores zur Ausgabe an die GUI
     */
    private void highscore() {
        gui.clearTables();
        String s = filestream.highscore();
        String [] eintraege = s.split(",");
        for(int i = 0; i < eintraege.length; i++) {
            gui.setNewRow(i+1 + "", eintraege[i].split("=")[0].split("#")[0], eintraege[i].split("=")[1], eintraege[i].split("=")[0].split("#")[1].replaceAll("_"," "));
        }
        System.out.print(s);
    }

    /**
     * Macht die ZÜge rückgängig.
     * @param str String, der die rückgängig gemachten Züge speichert.
     */
    private void undo(String str) {
    	String[] draws = str.split(";");
    	int x, y, result, playerIndex;
    	for(int i = 0; i < draws.length; i++) {
            System.out.println("test " + draws[i]);
            System.out.println("TEST " + draws[i].split("/")[1].split(",")[0]);
    		x = Integer.parseInt(draws[i].split("/")[1].split(",")[0]);
    		y = Integer.parseInt(draws[i].split("/")[1].split(",")[1]);
    		result = Integer.parseInt(draws[i].split("/")[2]);
    		playerIndex = Integer.parseInt(draws[i].split("/")[0]);
    		if(playerIndex == 1) {
                //Wasser
                if(result == 0){
                    board2[y][x] = 'w';
                    gui.draw(y, x, 2, -1);
                }
                //Treffer/versenkt
                else {
                    board2[y][x] = 'x';
                    gui.draw(y, x, 2, 2);
                }
            }
            else {
                //Wasser
                if(result == 0){
                    board1[y][x] = 'w';
                    gui.draw(y, x, 1, -1);
                }
                //Treffer/versenkt
                else {
                    board1[y][x] = 'x';
                    gui.draw(y, x, 1, 2);
                }
            }
    	}
    }

    /**
     * Wiederholt zurückgenommene Züge.
     * @param str String, der die zu wiederholenden Züge speichert.
     */
    private void redo(String str) {
    	String[] draws = str.split(";");
    	int x, y, result, playerIndex;
    	for(int i = 0; i < draws.length; i++) {
    		x = Integer.parseInt(draws[i].split("/")[1].split(",")[0]);
    		y = Integer.parseInt(draws[i].split("/")[1].split(",")[1]);
    		result = Integer.parseInt(draws[i].split("/")[2]);
    		playerIndex = Integer.parseInt(draws[i].split("/")[0]);
            if(playerIndex == 1) {
                //Wasser
                if(result == 0){
                    board2[y][x] = 'w';
                    gui.draw(y, x, 2, -1);
                }
                //Treffer/versenkt
                else {
                    board2[y][x] = 'x';
                    gui.draw(y, x, 2, 2);
                }
            }
            else {
                //Wasser
                if (result == 0) {
                    board1[y][x] = 'w';
                    gui.draw(y, x, 1, -1);
                }
                //Treffer/versenkt
                else {
                    board1[y][x] = 'x';
                    gui.draw(y, x, 1, 2);
                }
            }
    	}

    }

    /**
     * Erstellt aus einem Spielfeld einen String
     * @param playerIndex   Spieler, dessen  Spielfeld bearbeitet werden soll
     * @return              Spielfeld des Spielers als String
     */
    private String boardToString(int playerIndex) {
    	String str = "";
    	char[][] activeBoard;
    	if(playerIndex == 1) {
    		activeBoard = board1;
    	} else if(playerIndex == 2) {
    		activeBoard = board2;
    	} else {
    		throw new RuntimeException("Ungültiger PlayerIndex");
    	}

        for(int i = 0; i < activeBoard.length; i++) {
            for(int j=0; j<activeBoard[i].length; j++) {
                str += activeBoard[i][j];
            }
        }
        return str;
    }

    /**
     * Wenn der Name eines Spielstands angegeben wird,
     * wird das Spiel geladen, in dem die IVs aktualisiert werden.
     * @param gameName Name unter dem das Spiel abgespeichert ist
     */
    private void loadGame(String gameName) {
    	filestream.loadDrawsAndScore(gameName); // aktualisiert die IVs in den Klassen f�r Punkte und Z�ge
    	char[] lb1 = filestream.getBoardPlayerOne(gameName).toCharArray();
    	char[] lb2 = filestream.getBoardPlayerTwo(gameName).toCharArray();

    	int boardsize = filestream.getBoardsize(gameName);
        gui.setFieldSize(boardsize);
        gui.createField();
        board1 = new char[boardsize][boardsize];
        board2 = new char[boardsize][boardsize];
        String g = filestream.getGamemode(gameName);
        clearGUI();
        
        if(g.charAt(0) == '2') {
	        if(g.split(",")[2].equals("1")) {
	        	player2 = new Easy(10,false,shipList);
	        } else if(g.split(",")[2].equals("2")) {
	        	player2 = new Normal(10,false,shipList);
	        } else if(g.split(",")[2].equals("3")) {
	        	player2 = new Hard(10,false,shipList);
	        }
        } else if(g.charAt(0) == '3') {
	        if(g.split(",")[1].equals("1")) {
	        	player1 = new Easy(10,false,shipList);
	        } else if(g.split(",")[1].equals("2")) {
	        	player1 = new Normal(10,false,shipList);
	        } else if(g.split(",")[1].equals("3")) {
	        	player1 = new Hard(10,false,shipList);
	        } else if(g.split(",")[2].equals("1")) {
	        	player2 = new Easy(10,false,shipList);
	        } else if(g.split(",")[2].equals("2")) {
	        	player2 = new Normal(10,false,shipList);
	        } else if(g.split(",")[2].equals("3")) {
	        	player2 = new Hard(10,false,shipList);
	        }
        }

        if(player1 instanceof Computer) {
        	player1.loadPreviousGame(filestream.getMirrorFieldOne(gameName));
        } else if(player2 instanceof Computer) {
        	player2.loadPreviousGame(filestream.getMirrorFieldTwo(gameName));
        }
        
        int i = 0;
        for(int y=0; y<board1.length; y++) {
            for(int x=0; x<board1[y].length; x++) {
                board1[y][x] = lb1[i];
                board2[y][x] = lb2[i];
                i++;
            }//Ende ySchleife
        }//Ende xSchleife
        drawShipOnGUI();

        gui.setPlayernames(filestream.getPlayerName(gameName), filestream.getOpponentName(gameName));
        gui.setComboLabel(filestream.getComboValue(1), 1);
        gui.setScoreLabel(filestream.getScore(1), 1);
        gui.setComboLabel(filestream.getComboValue(2), 2);
        gui.setScoreLabel(filestream.getScore(2), 2);

    	if(filestream.getActivePlayer(gameName) == 1) {
    		player1active = true;
    		if(player1 == null) {
    			gui.setEnableField(2);
    		}
    	} else {
    		player1active = false;
    		if(player2 == null) {
    			gui.setEnableField(1);
    		}
    	}
    }

    /**
     * berechnet die Seitenlänge der Spielfelder
     * @return Seitenlänge
     */
    private double boardSize() {
    	return Math.sqrt(boardToString(1).length());
    }

    /**
     * @return  gibt den aktive Spieler anhand eines int an
     */
    private int activePlayer() {
    	if(player1active) {
    		return 1;
    	}else {
    		return 2;
    	}
    }

    /**
     * @return  gibt das Spielfeld an, dass der aktive Spieler beschießt
     */
    private int reverseActivePlayer() {
        if(player1active) {
            return 2;
        }else {
            return 1;
        }
    }

    /**
     * Beendet das Spiel.
     */

    private void gameOver() {
        disableNetwork();


    }

    /**
     * Setzt alle mit dem Netzwerk verbundenen Variablen zurück auf den Startzustand
     */
    private void disableNetwork() {
        if(network != null) { // Wenn eine Verbindung über das Netzwerk existiert...
            // Sage dem Client Bescheid, dass das Spiel zuende ist.
            if(isHost) network.send(NET_GAMEOVER + ":");
            // Kappe die Verbindung und beende den Network Thread.
            network.end();
            network = null;
            isHost = false;
            gui.setIsHost(false);
            gui.setIsNetwork(false);
            gui.setIsNetwork(false);
            gui.setConnected(false);
        }
    }

    /**
     * wechselt den aktiven Spieler
     */
    private void changeActivePlayer() {
        if(player1active) {
            player1active = false;
        }
        else {
            player1active = true;
        }
    }

    /**
     *
     * Wird nach beenden eines Spiels aufgerufen, um Variablen zurückzusetzten
     */
    private void clear() {
        board1 = null;
        board2 = null;
        player1 = null;
        player2 = null;
        network = null;
        isHost = false;
        player1active = true;
        shipList = null;
        filestream = new FileStream();
    }
    
    /**
     * Entfernt unzulässige Zeichen aus einem Spielernamen
     * @param pName Spielername
     * @return Spielername ohne verbotene Zeichen
     */
    private String checkPlayername(String pName) {
    	String[] illegal = filestream.forbiddenCharacters().split("/");
    	String r = pName;
    	for(String s : illegal) {
    		if(r.contains(s)) {
    			r = r.replaceAll(s, "");
    		}
    	}
    	return r;
    }

}

