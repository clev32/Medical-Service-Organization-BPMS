package Hatzalah.gui;

import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import Hatzalah.data.CallIO;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class AddSymptomDisplay {

	private Connection dbConnection;
	private TextField desc_txt;
	
	public AddSymptomDisplay(Stage mainWindow, MenuBar bar, Connection dbConnection) {
		this.dbConnection= dbConnection;
		mainWindow.setTitle("Add Symptom");
		
		BorderPane pane = new BorderPane();
		GridPane gridLayout = new GridPane();
		Label descLbl = new Label("Symptom Description");
		Label descAlert = new Label("*");
		descAlert.setId("AlertLabel");
		
		desc_txt = new TextField();
		gridLayout.add(descLbl, 0, 0);
		gridLayout.add(desc_txt, 1,0);
		gridLayout.add(descAlert, 2,0);
		gridLayout.setAlignment(Pos.CENTER);
		gridLayout.getStyleClass().add("grid");

		Button addSymptomBtn = new Button("ADD");
		
		addSymptomBtn.setOnAction(e->{
			String desc = desc_txt.getText();
			if(desc.trim().equals("")) {
				descAlert.setText("*Required field");
			}
			else {
				try {
					CallIO.addSymptom(dbConnection, desc);
					JOptionPane.showMessageDialog(null, "Symptom added");

				} catch (SQLException e1) {
					JOptionPane.showMessageDialog(null, "Something went wrong. Symptom may have been added already");
				}
			}
			
		});
		gridLayout.add(addSymptomBtn, 0,2);

		pane.setTop(bar);
		pane.setCenter(gridLayout);
		Scene addSymptomScene = new Scene(pane);
		addSymptomScene.getStylesheets().add(getClass().getResource("Main.css").toExternalForm());

		mainWindow.setScene(addSymptomScene);
		mainWindow.show();
		
	}

}
