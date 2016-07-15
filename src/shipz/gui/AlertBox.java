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

	public AlertBox(newGUI gui) {
		this.gui = gui;
	}

	public void display(String title, String message) {


		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle(title);
		window.setMinWidth(450);

		Label label = new Label();
		label.setText(message);
		TextField txtfFilename = new TextField("Filename");
		Button btnSave = new Button("Save");
		Button btnClose = new Button("Close");
		btnClose.setOnAction(e -> window.close());

		btnSave.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				gui.setFilename(txtfFilename.getText());
				window.close();
			}
		});

		VBox layout = new VBox(10);
		layout.getChildren().addAll(label, txtfFilename, btnSave, btnClose);
		layout.setAlignment(Pos.CENTER);

		Scene scene = new Scene(layout);
		window.setScene(scene);
		window.showAndWait();

	}

}