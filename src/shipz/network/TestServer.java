package shipz.network;

import shipz.network.GameTest;

import java.util.Scanner;

public class TestServer {

    public static void main(String[] args) {
        GameTest game = new GameTest();
        game.setupNetwork('s');
        game.send("hello client");
    }

}
