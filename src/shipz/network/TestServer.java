import java.util.Scanner;

public class TestServer {

    public static void main(String[] args) {
        Game game = new Game();
        game.setupNetwork('s');
        game.send("hello client");
    }

}
