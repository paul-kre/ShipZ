package shipz.network;

import java.util.Scanner;

public class TestClient {

    public static void main(String[] args) {
        GameTest game = new GameTest();
        game.setupNetwork('c');
        game.send("s:x=5;y=2");
    }

}
