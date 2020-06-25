package Hatzalah.gui;

import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import Hatzalah.data.MemberIO;
import Hatzalah.gui.AddMemberDisplay.AddMemberController;
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
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class AddMemberDisplay {
	
	private Connection dbConnection;
	private Stage mainWindow;
	private MenuBar bar;
	private TextField fName;
    private TextField lName;
    private TextField dateJoined;
    private TextField number;
    private TextField dob;
    private TextField maritialStatus;
    private TextField address;
	private ComboBox<USState> stateOptions;
    private TextField city;
    private TextField zip;
    private Label fNameAlert;
    private Label lNameAlert;
    private Label dateJoinedAlert;
    private Label numberAlert;
    private Label dobAlert;
    private Label maritialStatusAlert;
    private Label branchNameAlert;
    private Label addressAlert;
    private Label stateAlert;
    private Label cityAlert;
    private Label zipAlert;
    private Label jobNamesAlert;
    private ComboBox<String> branchNames;
    private ComboBox<String> jobNames;
    
	public AddMemberDisplay(Connection dbConnection, Stage mainWindow, MenuBar bar) {
		this.dbConnection = dbConnection;
		this.mainWindow = mainWindow;
		mainWindow.setTitle("Add Member");

		this.bar= bar;
		fName= new TextField();
	    lName= new TextField();
		dateJoined= new TextField();
		number= new TextField();
		dob= new TextField();
		maritialStatus= new TextField();
		branchNames = new ComboBox<>();
		try {
			branchNames.setItems(FXCollections.observableArrayList(MemberIO.getBranchNames(dbConnection)));
			branchNames.setPromptText("---Branches---");
		}
		catch(SQLException sqle) {
			JOptionPane.showMessageDialog(null,  "Cannot retrieve list of branches");
		}
		address= new TextField();
		stateOptions = new ComboBox<>();
		stateOptions.setItems( FXCollections.observableArrayList(USState.values()));
		stateOptions.setPromptText("---States---");
		city= new TextField();
		zip= new TextField();
		jobNames = new ComboBox<>();
		try {
			jobNames.setItems(FXCollections.observableArrayList(MemberIO.getJobNames(dbConnection)));
			jobNames.setPromptText("---Jobs---");
		}
		catch(SQLException sqle) {
			JOptionPane.showMessageDialog(null,  "Cannot retrieve list of jobs");
		}
		BorderPane borderLayout = new BorderPane();
		GridPane gridLayout = new GridPane();
		gridLayout.setAlignment(Pos.CENTER);
		gridLayout.setHgap(5); 
		gridLayout.setVgap(10); 
		gridLayout.getStyleClass().add("grid");


		gridLayout.add(new Label("First Name:"), 0, 0);
		gridLayout.add(fName,1 , 0);
		gridLayout.add(new Label("Last Name:"), 0, 1);
		gridLayout.add(lName, 1, 1);
		gridLayout.add(new Label("Date Joined (yyyy-mm-dd):"), 0, 2);
		gridLayout.add(dateJoined, 1, 2);
		gridLayout.add(new Label("Phone Number (10 digits without spaces):"), 0, 3);
		gridLayout.add(number, 1, 3);
		gridLayout.add(new Label("Date Of Birth (yyyy-mm-dd):"), 0, 4);
		gridLayout.add(dob, 1, 4);
		gridLayout.add(new Label("Maritial Status: (M, S, D or W)"), 0, 5);
		gridLayout.add(maritialStatus, 1, 5);
		gridLayout.add(new Label("Branch:"), 0, 6);
		gridLayout.add(branchNames, 1, 6);
		gridLayout.add(new Label("Address Line 1:"), 0, 7);
		gridLayout.add(address, 1, 7);
		gridLayout.add(new Label("City:"), 0, 8);
		gridLayout.add(city, 1, 8);
		gridLayout.add(new Label("State:"), 0, 9);
		gridLayout.add(stateOptions, 1, 9);
		gridLayout.add(new Label("Zip Code:"), 0, 10);
		gridLayout.add(zip, 1, 10);
		gridLayout.add(new Label("Job:"), 0, 11);
		gridLayout.add(jobNames, 1, 11);
		
		fNameAlert = new Label();
		fNameAlert.setTextFill(Color.web("#cd0000"));
		gridLayout.add(fNameAlert, 2, 0);
		
		lNameAlert = new Label();
		lNameAlert.setTextFill(Color.web("#cd0000"));
		gridLayout.add(lNameAlert, 2, 1);
		
		 dateJoinedAlert = new Label();
		dateJoinedAlert.setTextFill(Color.web("#cd0000"));
		gridLayout.add(dateJoinedAlert, 2, 2);
		
		 numberAlert = new Label();
		numberAlert.setTextFill(Color.web("#cd0000"));
		gridLayout.add(numberAlert, 2, 3);
		
		 dobAlert = new Label();
		dobAlert.setTextFill(Color.web("#cd0000"));
		gridLayout.add(dobAlert, 2, 4);
		
		 maritialStatusAlert = new Label();
		maritialStatusAlert.setTextFill(Color.web("#cd0000"));
		gridLayout.add(maritialStatusAlert, 2, 5);
		
		 branchNameAlert = new Label();
		branchNameAlert.setTextFill(Color.web("#cd0000"));
		gridLayout.add(branchNameAlert, 2, 6);
		
		 addressAlert = new Label();
		addressAlert.setTextFill(Color.web("#cd0000"));
		gridLayout.add(addressAlert, 2, 7);
		
		 cityAlert = new Label();
		cityAlert.setTextFill(Color.web("#cd0000"));
		gridLayout.add(cityAlert, 2, 8);
		
		 stateAlert = new Label();
		stateAlert.setTextFill(Color.web("#cd0000"));
		gridLayout.add(stateAlert, 2, 9);
		
		 zipAlert = new Label();
		zipAlert.setTextFill(Color.web("#cd0000"));
		gridLayout.add(zipAlert, 2, 10);
		
		 jobNamesAlert = new Label();
		jobNamesAlert.setTextFill(Color.web("#cd0000"));
		gridLayout.add(jobNamesAlert, 2, 11);
		
		Button addButton = new Button("Add Member");
		addButton.setOnAction(new AddMemberController(dbConnection));
		gridLayout.add(addButton, 1, 12);

		borderLayout.setTop(bar);

		borderLayout.setCenter(gridLayout);
		Scene scene = new Scene(borderLayout);
		scene.getStylesheets().add(getClass().getResource("Main.css").toExternalForm());

		mainWindow.setScene(scene);
	}
	public class AddMemberController implements EventHandler<ActionEvent> {
		Connection dbConnection;
		public AddMemberController(Connection dbConnection) {
			this.dbConnection = dbConnection;
		}

		@Override
		public void handle(ActionEvent arg0) {
			boolean validData = true;
			reset();
		

			if (fName.getText().equals("")){
				fNameAlert.setText("*Field cannot be empty!");
				validData = false;
			}
			if(lName.getText().equals ("")) {
				lNameAlert.setText("*Field cannot be empty!");
				validData = false;
			}
			if(dateJoined.getText().equals ("")) {
				dateJoinedAlert.setText("*Field cannot be empty!");
				validData = false;
			}
			else if(dateJoined.getText().length()!=10|| !dateJoined.getText().substring(4,5).equals("-")
					||!dateJoined.getText().substring(7,8).equals("-")) {
				dateJoinedAlert.setText("*Field must be in yyyy-mm-dd format");
				validData = false;
			}
			if(number.getText().equals ("")) {
				numberAlert.setText("*Field cannot be empty!");
				validData = false;
			}
			else if(number.getText().length() !=10) {
				numberAlert.setText("*Number must be 10 digits without spaces");
				validData = false;
			}
			if(dob.getText().equals ("")) {
				dobAlert.setText("*Field cannot be empty!");
				validData = false;
			}
			else if(dob.getText().length()!=10 || !dob.getText().substring(4,5).equals("-")
					||!dob.getText().substring(7,8).equals("-")) {
				dobAlert.setText("*Field must be in yyyy-mm-dd format");
				validData = false;
			}
			
			if(maritialStatus.getText().equals ("")) {
				maritialStatusAlert.setText("*Field cannot be empty!");
				validData = false;
			}
			else if(!maritialStatus.getText().toUpperCase().equals("M") &&
					!maritialStatus.getText().toUpperCase().equals("S")  &&
					!maritialStatus.getText().toUpperCase().equals("D")  &&
					!maritialStatus.getText().toUpperCase().equals("W") ) {
				maritialStatusAlert.setText("*Please enter 'M', 'S', 'D' or 'W' ");
				validData = false;
			}
			if (branchNames.getValue()==null) {
				branchNameAlert.setText("*Field cannot be empty!");
				validData = false;
			}
			
			if(address.getText().equals ("")) {
				addressAlert.setText("*Field cannot be empty!");
				validData = false;
			}
			if(stateOptions.getValue()==null) {
				stateAlert.setText("*Field cannot be empty!");
				validData = false;
			}
			if(city.getText().equals ("")) {
				cityAlert.setText("*Field cannot be empty!");
				validData = false;
			}
			if(zip.getText().equals ("")) {
				zipAlert.setText("*Field cannot be empty!");
				validData = false;
			}
			if(jobNames.getValue()==null) {
				jobNamesAlert.setText("*Field cannot be empty!");
				validData = false;
			}
			
			
			
			if (validData) {
				try {
					int branchID = MemberIO.getBranchID(dbConnection, branchNames.getValue());

					int jobID = MemberIO.getJobID(dbConnection, jobNames.getValue());
					
					MemberIO.addMemberData(dbConnection, fName.getText(), lName.getText(), dateJoined.getText(), 
							number.getText(), dob.getText(),maritialStatus.getText(), branchID, address.getText(), 
							city.getText(), stateOptions.getValue().toString(), zip.getText(), jobID);
					JOptionPane.showMessageDialog(null, "Member added");

				}
				catch(SQLException sqle) {
					JOptionPane.showMessageDialog(null, "Member was not added due to duplicate data in the database");

				}
				catch(IllegalArgumentException ex) {
					JOptionPane.showMessageDialog(null, "Member was not added due to invalid information");

				}
			}
			

		}

		private void reset() {
			fNameAlert.setText("");
			lNameAlert.setText("");
			dateJoinedAlert.setText("");
			numberAlert.setText("");
			dobAlert.setText("");
			maritialStatusAlert.setText("");
			branchNameAlert.setText("");
			addressAlert.setText("");
			cityAlert.setText("");
			stateAlert.setText("");
			zipAlert.setText("");
			jobNamesAlert.setText("");
		}

	}
	
}
