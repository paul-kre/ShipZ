import java.util.Scanner;

public class TestClient {

    public static void main(String[] args) {
        Game game = new Game();
        game.setupNetwork('c');
        game.send("hi server");
    }

}
