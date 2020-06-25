package Hatzalah.gui;

import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import Hatzalah.data.DonorIO;
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
import javafx.scene.control.Labeled;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class AddDonorWindow {
	
	private Connection dbConnection;
	private TextField donorFName_txt;
	private TextField donorLName_txt;
	private TextField streetAddress_txt;
	private TextField city_txt;
	private TextField zip_txt;
	private ComboBox<USState> stateOptions;
	private Label fNameAlert;
	private Label lNameAlert;
	private Label streetAddressAlert;
	private Label stateAlert;
	private Label cityAlert;
	private Label zipAlert;
	private Group alertLblGroup;
	
	public AddDonorWindow(Stage mainWindow, MenuBar bar, Connection dbConnection) {
	
		this.dbConnection = dbConnection;
		
		mainWindow.setTitle("Add Donor");
		
		BorderPane pane = new BorderPane();
		GridPane gridLayout = new GridPane();
		gridLayout.setAlignment(Pos.CENTER);
		gridLayout.getStyleClass().add("grid");

		gridLayout.setHgap(5); 
		gridLayout.setVgap(10); 
		
		Label fNameLbl = new Label("First Name:");
		donorFName_txt = new TextField();
		
		Label lNameLbl = new Label("Last Name:");
		donorLName_txt = new TextField();
		
		Label streetAddressLbl = new Label("Street Address:");
		streetAddress_txt = new TextField();
		
		Label stateLbl = new Label("State:");
		stateOptions = new ComboBox<>();
		stateOptions.setItems( FXCollections.observableArrayList(USState.values()));
		stateOptions.setPromptText("---State---");
				
		Label cityLbl = new Label("City:");
		city_txt = new TextField();
		
		Label zipLbl = new Label("Zip:");
		zip_txt = new TextField();
		
		gridLayout.add(fNameLbl, 0, 0);
		gridLayout.add(donorFName_txt, 1, 0);
		gridLayout.add(lNameLbl, 0, 1);
		gridLayout.add(donorLName_txt, 1, 1);
		gridLayout.add(streetAddressLbl, 0, 2);
		gridLayout.add(streetAddress_txt, 1, 2);
		gridLayout.add(cityLbl, 0, 3);
		gridLayout.add(city_txt, 1, 3);
		gridLayout.add(stateLbl, 0, 4);
		gridLayout.add(stateOptions, 1, 4);
		gridLayout.add(zipLbl, 0, 5);
		gridLayout.add(zip_txt, 1, 5);
		
	
		
		
		//Create a group to hold the alert labels so that they can be styled
		alertLblGroup = new Group();
		fNameAlert = new Label("*");		
		lNameAlert = new Label("*");
		streetAddressAlert = new Label("*");
		stateAlert = new Label("*");
		cityAlert = new Label("*");
		zipAlert = new Label("*");
		
		alertLblGroup.getChildren().addAll(fNameAlert, lNameAlert, streetAddressAlert, stateAlert, cityAlert, zipAlert);
		
		//create a css id for each label in the group
		for(Node node: alertLblGroup.getChildren()) {
			node.setId("AlertLabel");
		}

		gridLayout.add(fNameAlert, 2, 0);
		gridLayout.add(lNameAlert, 2, 1);
		gridLayout.add(streetAddressAlert, 2, 2);		
		gridLayout.add(cityAlert, 2, 3);
		gridLayout.add(stateAlert, 2, 4);
	 	gridLayout.add(zipAlert, 2, 5);
	 	
		Button addButton = new Button("ADD");
		addButton.setOnAction(new AddDonorHandler());
		gridLayout.add(addButton, 1, 6);
		
		pane.setTop(bar);

		pane.setCenter(gridLayout);
		Scene addDonorScene = new Scene(pane);
		mainWindow.setScene(addDonorScene);
		
		addDonorScene.getStylesheets().add(getClass().getResource("Main.css").toExternalForm());

		
	}
	
	class AddDonorHandler implements EventHandler<ActionEvent>{
	

		@Override
		public void handle(ActionEvent arg0) {
			reset();
			String fName = donorFName_txt.getText();
			String lName = donorLName_txt.getText();
			String address = streetAddress_txt.getText();
			String city = city_txt.getText();
			String state =  String.valueOf(stateOptions.getValue());
			String zip = zip_txt.getText();
			
		
			
			if(fName.trim().equals(""))
			{
				fNameAlert.setText("*Field cannot be empty!");
			}
			
			if(lName.trim().equals(""))
			{
				lNameAlert.setText("*Field cannot be empty!");
			}
			
			if(address.trim().equals(""))
			{
				streetAddressAlert.setText("*Field cannot be empty!");
			}
			
			if(city.trim().equals(""))
			{
				cityAlert.setText("*Field cannot be empty!");
			}
			if(state.equals("null"))
			{
				stateAlert.setText("*You must choose a state from the drop down");
			}
			
			if(zip.length() != 5) 
			{
				zipAlert.setText("*zip must be 5 digits long");
			}
			
			else {
				try {
					DonorIO.addDonor(dbConnection, fName, lName, address, city, state, zip);
					JOptionPane.showMessageDialog(null,"Donor added");
				}
				catch(SQLException sqlE) 
				{
					JOptionPane.showMessageDialog(null, "Error occurred. Make sure you aren't adding the same donor 2x");
				}
				}
			
		}
		
	
		
		private void reset() {
			fNameAlert.setText("");
			lNameAlert.setText("");
			streetAddressAlert.setText("");
			stateAlert.setText("");
			cityAlert.setText("");
			zipAlert.setText("");

			
			}
		}
	}
	
	


