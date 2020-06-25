package Hatzalah.gui;

import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import Hatzalah.data.DonorIO;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class AddDonationWindow {

	private Connection dbConnection;
	private MenuBar bar;
	private TextField donationAmnt_txt;
	private TextField donorID_txt;
	private ComboBox<String> branchNames;
	private Label amntAlert;
	private Label branchNamesAlert;
	
	public AddDonationWindow(Stage mainWindow, MenuBar bar, Connection dbConnection) 
	{
		
		this.dbConnection = dbConnection;
		this.bar = bar;
		mainWindow.setTitle("Add Donation");

		BorderPane pane = new BorderPane();
		GridPane gridLayout = new GridPane();
		gridLayout.setHgap(5); 
		gridLayout.setVgap(10); 
		gridLayout.setAlignment(Pos.CENTER);
		gridLayout.getStyleClass().add("grid");

		pane.getStyleClass().add("pane");

		Label donorIDLbl = new Label("Donor ID:");
		donorID_txt = new TextField();		

		Button addDonationBtn = new Button("add donation");
		addDonationBtn.setOnAction(e -> 
		{
		int donorID = Integer.valueOf(donorID_txt.getText());
		try {
			boolean exists = DonorIO.verifyDonorID(dbConnection, donorID);
			if(exists)
			{
				GridPane gridLayout2 = new GridPane();
				
				
				gridLayout2.setHgap(5); 
				gridLayout2.setVgap(10); 
				gridLayout2.setAlignment(Pos.CENTER);
				gridLayout2.getStyleClass().add("grid");

				Label donationAmntLbl = new Label("Donation Amount");
				donationAmnt_txt = new TextField();
				
				Label branchName = new Label("Branch Name");
				branchNames = new ComboBox<>();
				try {
					branchNames.setItems( FXCollections.observableArrayList(DonorIO.getBranchNames(dbConnection)));
					branchNames.setPromptText("---Branches---");
				} 
				catch (SQLException sqlE) {
					JOptionPane.showMessageDialog(null, "Cannot retrieve list of branches");
				}
				
				amntAlert = new Label("*");
				branchNamesAlert = new Label("*");
				
				gridLayout2.add(donationAmntLbl, 0, 0);
				gridLayout2.add(donationAmnt_txt, 1, 0);
				gridLayout2.add(branchName, 0, 1);
				gridLayout2.add(branchNames, 1, 1);
				gridLayout2.add(amntAlert, 2, 0);
				gridLayout2.add(branchNamesAlert, 2, 1);
				
				amntAlert.setId("AlertLabel");
				branchNamesAlert.setId("AlertLabel");
				
				Button okButton = new Button("OK");

				okButton.setOnAction(new AddDonationHandler());
				
				gridLayout2.add(okButton, 1, 4);
				
				//reset pane
			   pane.setCenter(gridLayout2);
			}
			else {
				JOptionPane.showMessageDialog(null, "Donor does not exist. Please reenter a valid donor.");
			}
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, "error occurred");
		}
				
		});
		
		
		gridLayout.add(donorIDLbl, 0, 0);
		gridLayout.add(donorID_txt, 1, 0);
		gridLayout.add(addDonationBtn, 1, 1);

		pane.setTop(bar);
		pane.setCenter(gridLayout);
		Scene addDonationScene = new Scene(pane);
		mainWindow.setScene(addDonationScene);
		addDonationScene.getStylesheets().add(getClass().getResource("Main.css").toExternalForm());

	}

	class AddDonationHandler implements EventHandler<ActionEvent>{

		@Override
		public void handle(ActionEvent event) {
			reset();
			boolean isValid = true;
			try {
			double donationAmnt = Double.valueOf(donationAmnt_txt .getText());
			int donorID = Integer.valueOf(donorID_txt.getText());
			String branchName = branchNames.getValue();

			if(branchName == null) {
			branchNamesAlert.setText("*You must choose a branch from the drop down");
			isValid =false;
			}
			
			if(isValid) {
				DonorIO.addDonation(dbConnection, donationAmnt, donorID, branchName);
				JOptionPane.showMessageDialog(null,"Thank you for your generous donation of $" + donationAmnt + " dollars!");

			}
			
			}
			
			catch(NumberFormatException ex)
			{
				amntAlert.setText("*Please enter valid number");
				isValid= false;
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(null, "error occurred");
			}
		}
		
		private void reset() {
			branchNamesAlert.setText("");
			amntAlert.setText("");
		}

		
	}
}
