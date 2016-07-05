package shipz.gui;

import javafx.application.Application;
import javafx.stage.Stage;
import shipz.gui.newGUI;

public class Test extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		Stage pStage = new Stage();
		newGUI newG = new newGUI(pStage);
		
	}

}
