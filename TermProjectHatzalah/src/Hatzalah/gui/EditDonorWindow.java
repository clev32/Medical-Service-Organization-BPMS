package Hatzalah.gui;

import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import Hatzalah.data.DonorIO;
import Hatzalah.gui.AddDonorWindow.AddDonorHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class EditDonorWindow {

	private Connection dbConnection;
	private TextField donorFName_txt;
	private TextField donorLName_txt;
	private TextField streetAddress_txt;
	private TextField city_txt;
	private TextField zip_txt;
	private TextField donorID_txt;
	private ComboBox<USState> stateOptions;
	private Label zipAlert;

	
	public EditDonorWindow(Stage mainWindow, MenuBar bar, Connection dbConnection)
	{
		this.dbConnection = dbConnection;
		
		mainWindow.setTitle("Edit Donor");
		
		BorderPane pane = new BorderPane();
		GridPane gridLayout = new GridPane();
		gridLayout.setHgap(5); 
		gridLayout.setVgap(10); 
		gridLayout.setAlignment(Pos.CENTER);
		gridLayout.getStyleClass().add("grid");

		pane.getStyleClass().add("pane");

		
		
		Label donorIDLBl = new Label("Donor ID:");
		donorID_txt = new TextField();
		
		Button editDonorBtn = new Button("Edit Donor");

		editDonorBtn.setOnAction(e ->{
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

				Label fNameLbl = new Label("First Name:");
				donorFName_txt = new TextField();
				
				Label lNameLbl = new Label("Last Name:");
				donorLName_txt = new TextField();
				
				Label streetAddressLbl = new Label("Street Address:");
				streetAddress_txt = new TextField();
				
				Label stateLbl = new Label("State:");
				stateOptions = new ComboBox<>();
				stateOptions.setItems( FXCollections.observableArrayList(USState.values()));
				stateOptions.setPromptText("---States---");
						
				Label cityLbl = new Label("City:");
				city_txt = new TextField();
				
				Label zipLbl = new Label("Zip:");
				zip_txt = new TextField();
				

				gridLayout2.add(fNameLbl, 0, 0);
				gridLayout2.add(donorFName_txt, 1, 0);
				gridLayout2.add(lNameLbl, 0, 1);
				gridLayout2.add(donorLName_txt, 1, 1);
				gridLayout2.add(streetAddressLbl, 0, 2);
				gridLayout2.add(streetAddress_txt, 1, 2);
				gridLayout2.add(cityLbl, 0, 3);
				gridLayout2.add(city_txt, 1, 3);
				gridLayout2.add(stateLbl, 0, 4);
				gridLayout2.add(stateOptions, 1, 4);
				gridLayout2.add(zipLbl, 0, 5);
				gridLayout2.add(zip_txt, 1, 5);
		
				
				zipAlert = new Label();
				
			 	gridLayout2.add(zipAlert, 2, 5);
			 	
			 	zipAlert.setId("AlertLabel");
				Button applyChangesBtn = new Button("APPLY CHANGES");

				applyChangesBtn.setOnAction(new EditDonorHandler());
				
				gridLayout2.add(applyChangesBtn, 1, 6);
				
				pane.setCenter(gridLayout2);
				
			}
			else {
				JOptionPane.showMessageDialog(null, "Donor does not exist. Please reenter a valid donor.");

			}
			
		}
		catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, "error occurred");
		}
		
		catch(NumberFormatException ex)
		{
			JOptionPane.showMessageDialog(null, "Invalid Number");
		}
		
		
		}
		);
		

		
		gridLayout.add(donorIDLBl, 0, 0);
		gridLayout.add(donorID_txt, 1, 0);
		gridLayout.add(editDonorBtn, 1, 1);
		
	

		pane.setTop(bar);
		pane.setCenter(gridLayout);
		Scene EditDonorScene = new Scene(pane);
		mainWindow.setScene(EditDonorScene);
		EditDonorScene.getStylesheets().add(getClass().getResource("Main.css").toExternalForm());

	}
	
	

class EditDonorHandler implements EventHandler<ActionEvent>{

	@Override
	public void handle(ActionEvent arg0) {

		reset();

		String fName = donorFName_txt.getText();
		String lName = donorLName_txt.getText();
		String address = streetAddress_txt.getText();
		String city = city_txt.getText();
		String state =  String.valueOf(stateOptions.getValue());
		String zip = zip_txt.getText();
		int donorID = Integer.valueOf(donorID_txt.getText());
	

		if(!zip.equals("") && zip.length() != 5) {
			zipAlert.setText("*zip must be 5 digits long");
		}
		
	
		
		else {
			try {
				boolean edited = DonorIO.editDonor(dbConnection, fName, lName, address, city, state, zip, donorID);
				if(edited) {
				JOptionPane.showMessageDialog(null,"Donor Information Updated");
				}
				else {
					JOptionPane.showMessageDialog(null,"Nothing was updated");
				}
			}
			catch(SQLException sqlE) 
			{
				JOptionPane.showMessageDialog(null, "Donor Information was not updated. Please make sure info is correct");
			}
			}
		
	}
	
		
		private void reset() {
			zipAlert.setText("");
		}
	
}

}
