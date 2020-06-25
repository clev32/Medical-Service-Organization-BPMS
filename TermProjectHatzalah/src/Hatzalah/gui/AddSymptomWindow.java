package Hatzalah.gui;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import Hatzalah.data.CallIO;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AddSymptomWindow extends Stage {

	private Connection dbConnection;
	private VBox vBox;
	
	public AddSymptomWindow(Connection dbConnection, int callID) {
		this.dbConnection = dbConnection;
		this.setTitle("Add a Symptom ");

		GridPane gridLayout = new GridPane();
		BorderPane pane = new BorderPane();
		vBox = new VBox();
		vBox.setAlignment(Pos.CENTER);
		List<String> symptoms = CallIO.getSymptoms(dbConnection);
		Button submitSymptomBtn = new Button("Submit Symptoms");
		gridLayout.add(submitSymptomBtn, 1, 0);

		for (String symptom : symptoms) {
			CheckBox checkBox = new CheckBox(symptom);
			vBox.getChildren().add(checkBox);
		}

		submitSymptomBtn.setOnAction(e -> {

			for (Node node : vBox.getChildren()) {
				CheckBox checkBx = (CheckBox) node;
				if (checkBx.isSelected()) {
					try {

						CallIO.addSymptoms(dbConnection, checkBx.getText(), callID);
						JOptionPane.showMessageDialog(null, "Symptom added");

						
					} catch (SQLException sqle) {
						JOptionPane.showMessageDialog(null, "Error Occurred");
					}
				}
			}

		});

		pane.setCenter(vBox);
		pane.setBottom(submitSymptomBtn);
		Scene addCallScene = new Scene(pane);
		this.setScene(addCallScene);
		this.show();
	}

}
