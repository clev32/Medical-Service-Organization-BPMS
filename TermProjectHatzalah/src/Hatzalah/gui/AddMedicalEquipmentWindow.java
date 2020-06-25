package Hatzalah.gui;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import Hatzalah.data.CallIO;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AddMedicalEquipmentWindow extends Stage {
	private Connection dbConnection;
	private ArrayList<String> equipmentList;
	private VBox vBox;
	private int callID;

	public AddMedicalEquipmentWindow(Connection dbConnection, int callID) {
		this.dbConnection = dbConnection;
		this.setTitle("Add Medical Equipment");
		this.setWidth(600);
		this.setHeight(600);
		this.callID = callID;
		

		BorderPane borderLayout = new BorderPane();
		vBox = new VBox();
		vBox.setAlignment(Pos.CENTER);

		try {
			equipmentList = CallIO.getMedicalEquipment(dbConnection);
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Could not retrieve list of equipment");
		}

		for (String equip : equipmentList) {
			CheckBox checkBox = new CheckBox(equip);
			vBox.getChildren().add(checkBox);
		}

		Button addSymptom = new Button("ADD");
		addSymptom.setOnAction(new addSymptomHandler());

		Label choiceLbl = new Label("Please select all that apply:");
		borderLayout.setTop(choiceLbl);
		borderLayout.setCenter(vBox);
		borderLayout.setBottom(addSymptom);

		Scene scene = new Scene(borderLayout);
		scene.getStylesheets().add(getClass().getResource("Main.css").toExternalForm());

		this.setScene(scene);
		this.show();
	}

	class addSymptomHandler implements EventHandler<ActionEvent> {
		ArrayList<String> checked = new ArrayList<String>();

		@Override
		public void handle(ActionEvent arg0) {

			for (Node node : vBox.getChildren()) {
				CheckBox checkBx = (CheckBox) node;
				if (checkBx.isSelected()) {
					checked.add(checkBx.getText());
				}
			}

			try {
				CallIO.insertEquipment(dbConnection, checked, callID);
				JOptionPane.showMessageDialog(null, "Equipment added successfully");
			} catch (SQLException sqle) {
				JOptionPane.showMessageDialog(null, "Something went wrong");

			}
		}

	}
}
