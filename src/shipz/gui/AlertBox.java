package shipz.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import shipz.util.GameEventSource;

public class AlertBox extends GameEventSource {

	Stage window = new Stage();
	newGUI gui;
	boolean r = false;

	public AlertBox(newGUI gui) {
		this.gui = gui;
	}

	public void displaySave() {

		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle("Save your game");
		window.setMinWidth(450);

		Label label = new Label();
		label.setText("Please enter a name to save your game.");
		Label label2 = new Label();
		label2.setText("Keep in mind that if you enter an existing name,");
		Label label3 = new Label();
		label3.setText("the saved game will be overwritten with the current one.");
		TextField txtfFilename = new TextField("Name");
		Button btnSave = new Button("Save");
		Button btnClose = new Button("Cancel");
		btnClose.setOnAction(e -> window.close());

		btnSave.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				gui.setFilename(txtfFilename.getText());
				window.close();
			}
		});

		VBox layout = new VBox(10);
		layout.getChildren().addAll(label, label2, label3, txtfFilename, btnSave, btnClose);
		layout.setAlignment(Pos.CENTER);

		Scene scene = new Scene(layout);
		window.setScene(scene);
		window.showAndWait();

	}
	
	public boolean displayLoad() {
		
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle("Are you sure?");
		window.setMinWidth(450);
		
		Label label = new Label();
		label.setText("You should save the game before you load another one.");
		Label label2 = new Label();
		label2.setText("Are you sure you want to leave?");
		
		Button btnYes = new Button("Yes");
		Button btnNo = new Button("No");
		
		btnNo.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				r = false;
				window.close();
			}
		});
		
		btnYes.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				r = true;
				window.close();
			}
		});
		
		VBox layout = new VBox(10);
		layout.getChildren().addAll(label, label2, btnYes, btnNo);
		layout.setAlignment(Pos.CENTER);
		
		Scene scene = new Scene(layout);
		window.setScene(scene);
		window.showAndWait();
		
		return r;
	}
	
	public void displayWarning() {
		
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle("No draws left");
		window.setMinWidth(450);
		
		Label label = new Label();
		label.setText("You can't undo/ redo any draws.");
		
		Button btnOk = new Button("Ok");
		
		btnOk.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				window.close();
			}
		});
		
		VBox layout = new VBox(10);
		layout.getChildren().addAll(label, btnOk);
		layout.setAlignment(Pos.CENTER);
		
		Scene scene = new Scene(layout);
		window.setScene(scene);
		window.showAndWait();
		
	}

}