package Hatzalah.gui;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;

import javax.swing.JOptionPane;

import Hatzalah.data.BranchIO;
import Hatzalah.gui.AddDonorWindow.AddDonorHandler;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class AddBranchDisplay {

	private Connection dbConnection;
	private TextField branchLocation_txt;
	private TextField yearEstablished_txt;
	private Label locationAlert;
	private Label yearAlert;
	
	public AddBranchDisplay(Connection dbConnection, Stage mainWindow, MenuBar bar) {
		this.dbConnection= dbConnection;
		mainWindow.setTitle("Add Branch");
		BorderPane pane = new BorderPane();
		GridPane gridLayout = new GridPane();
		gridLayout.setAlignment(Pos.CENTER);
		gridLayout.getStyleClass().add("grid");

		gridLayout.setHgap(5); 
		gridLayout.setVgap(10); 
		
		Label locationLbl = new Label("Location:");
		branchLocation_txt = new TextField();

		Label yearEstablishedLbl = new Label("Established in: ");
		yearEstablished_txt= new TextField();
		
		locationAlert = new Label("*");
		locationAlert.setId("AlertLabel");
		yearAlert = new Label("*");
		yearAlert.setId("AlertLabel");
		
		gridLayout.add(locationLbl, 0, 0);
		gridLayout.add(branchLocation_txt, 1, 0);
		gridLayout.add(yearEstablishedLbl, 0, 1);
		gridLayout.add(yearEstablished_txt, 1, 1);
		gridLayout.add(locationAlert, 2, 0);
		gridLayout.add(yearAlert, 2, 1);

		Button addButton = new Button("ADD");
		addButton.setOnAction(new AddBranchHandler());
		gridLayout.add(addButton, 1, 2);
		
		pane.setTop(bar);

		pane.setCenter(gridLayout);
		Scene addBranchScene = new Scene(pane);
		mainWindow.setScene(addBranchScene);
		
		addBranchScene.getStylesheets().add(getClass().getResource("Main.css").toExternalForm());

		
	}
	
	class AddBranchHandler implements EventHandler<ActionEvent>{

		@Override
		public void handle(ActionEvent arg0) {
			reset();
			boolean isValid = true;
			int currYear = LocalDate.now().getYear();
			String location = branchLocation_txt.getText();
			String year = yearEstablished_txt.getText();
			int yearEst = 0;
			
			try {
			 yearEst = Integer.valueOf(year);
			}
			catch(NumberFormatException e) {
				yearAlert.setText("*Please enter a valid year");
				isValid = false;
			}
			
			if(location.trim().equals("")) {
				locationAlert.setText("*This field is required");
				isValid = false;
			}
			
			if(year.trim().equals("")) {
				yearAlert.setText("*This field is required");
				isValid = false;
			}
			
			if(yearEst <= 1900 || yearEst> currYear) {
				yearAlert.setText("*Year must be between 1901 and " + currYear);
				isValid = false;
			}
			
			if(isValid) {
				try {
					BranchIO.addBranch(dbConnection, location, year);
					JOptionPane.showMessageDialog(null, "Branch Added");
				}
				catch(SQLException sqlE) {
					JOptionPane.showMessageDialog(null, "Something went wrong. Check that a branch at this location does"
							+ " not already exist");
				}
			}
		}
		
		private void reset() {
			locationAlert.setText("*");
			yearAlert.setText("*");
		}
		
	}

}
