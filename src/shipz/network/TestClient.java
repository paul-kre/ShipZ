import java.util.Scanner;

public class TestClient {

    public static void main(String[] args) {
        Game game = new Game();
        game.setupNetwork('c');
        game.send("hi server");
        // hallo
        // hallo zum zweiten mal
        // hallo nummer 3
        // hallo 4
        //TESTSRTSRT
    }

}
