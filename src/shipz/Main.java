package shipz;

import javafx.application.Application;
import javafx.stage.Stage;
/**
 * Created by Max on 09.06.2016.
 */
public class Main extends Application{
    public static void main(String[] args) {
        launch(args);

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        new Game(10, 10, primaryStage);
    }
}
