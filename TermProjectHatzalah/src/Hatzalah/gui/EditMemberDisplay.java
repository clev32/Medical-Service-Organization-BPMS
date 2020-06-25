package Hatzalah.gui;

import java.awt.GridLayout;
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

public class EditMemberDisplay {
	private Connection dbConnection;
	private Stage mainWindow;
	private MenuBar bar;
	private TextField memberID;
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
    private ComboBox<String> jobNames;
	Label dateJoinedAlert;
	Label dobAlert;
	Label numberAlert;
	Label jobNameAlert;
	Label maritialStatusAlert;
	boolean entered;

	public EditMemberDisplay(Connection dbConnection, Stage mainWindow, MenuBar bar) {
		this.dbConnection = dbConnection;
		this.mainWindow = mainWindow;
		mainWindow.setTitle("Edit Member");
		this.bar = bar;
		memberID = new TextField();
		entered = false;

		BorderPane borderLayout = new BorderPane();
		

		GridPane gridLayout = new GridPane();
		borderLayout.setTop(bar);
		Button editMemberButton = new Button("Edit Member");
		editMemberButton.setOnAction(new EditMemberController(dbConnection, gridLayout));
		gridLayout.add(new Label("Please enter the Member ID:"), 0, 0);
		gridLayout.add(memberID, 1, 0);
		gridLayout.add(editMemberButton, 1, 1);
		gridLayout.setAlignment(Pos.TOP_CENTER);
		gridLayout.setHgap(5); 
		gridLayout.setVgap(10); 
		
		borderLayout.setCenter(gridLayout);
		Scene scene = new Scene(borderLayout);
		gridLayout.getStyleClass().add("grid");

		scene.getStylesheets().add(getClass().getResource("Main.css").toExternalForm());

		mainWindow.setScene(scene);
	}

	public class EditMemberController implements EventHandler<ActionEvent> {
		Connection dbConnection;
		GridPane gridLayout;

		public EditMemberController(Connection dbConnection, GridPane gridLayout) {
			this.dbConnection = dbConnection;
			this.gridLayout = gridLayout;
		}

		@Override
		public void handle(ActionEvent arg0) {
			try {
				if (entered == false) {

					boolean exists = MemberIO.verifyMemberID(dbConnection, memberID.getText());
					if (exists) {
						entered = true;

						memberChangesDisplay(dbConnection, gridLayout);

					} else {
						JOptionPane.showMessageDialog(null, "Member does not exist. Please reenter a valid member.");

					}
				} else {
					JOptionPane.showMessageDialog(null,
							"To edit a new member, click 'Edit Member' from the Members tab");
				}

			} catch (SQLException sqle) {
				JOptionPane.showMessageDialog(null, sqle.getMessage());

			}
		}

		private void memberChangesDisplay(Connection connection, GridPane gridLayout) {

			gridLayout.add(
					new Label("Fill out ONLY the fields that you wish to change for Member " + memberID.getText()), 0,
					2);
			fName = new TextField();
			lName = new TextField();
			dateJoined = new TextField();
			number = new TextField();
			dob = new TextField();
			maritialStatus = new TextField();
			address = new TextField();
			stateOptions = new ComboBox<>();
			stateOptions.setItems( FXCollections.observableArrayList(USState.values()));
			city = new TextField();
			zip = new TextField();
			jobNames = new ComboBox<>();
			try {
				jobNames.setItems(FXCollections.observableArrayList(MemberIO.getJobNames(dbConnection)));
			}
			catch(SQLException sqle) {
				JOptionPane.showMessageDialog(null,  "Cannot retrieve list of jobs");
			}
			gridLayout.add(new Label("First Name:"), 0, 4);
			gridLayout.add(fName, 1, 4);
			gridLayout.add(new Label("Last Name:"), 0, 5);
			gridLayout.add(lName, 1, 5);
			gridLayout.add(new Label("Date Joined (yyyy-mm-dd):"), 0, 6);
			gridLayout.add(dateJoined, 1, 6);
			gridLayout.add(new Label("Phone Number (10 digits without spaces):"), 0, 7);
			gridLayout.add(number, 1, 7);
			gridLayout.add(new Label("Date Of Birth (yyyy-mm-dd):"), 0, 8);
			gridLayout.add(dob, 1, 8);
			gridLayout.add(new Label("Maritial Status: (M, S, D or W)"), 0, 9);
			gridLayout.add(maritialStatus, 1, 9);
			// They cannot change the branch ID because that would change the memberID
			// which would cause errors due to the other tables that reference that memberID
			// so if they change branches, they have to create a new member
			gridLayout.add(new Label("Address Line 1:"), 0, 10);
			gridLayout.add(address, 1, 10);
			gridLayout.add(new Label("City:"), 0, 11);
			gridLayout.add(city, 1, 11);
			gridLayout.add(new Label("State:"), 0, 12);
			gridLayout.add(stateOptions, 1, 12);
			gridLayout.add(new Label("Zip Code:"), 0, 13);
			gridLayout.add(zip, 1, 13);
			gridLayout.add(new Label("Job ID:"), 0, 14);
			gridLayout.add(jobNames, 1, 14);

			Button applyChangesButton = new Button("Apply Changes");
			applyChangesButton.setOnAction(new ApplyChangesController(dbConnection, gridLayout, memberID, fName, lName,
					dateJoined, number, dob, maritialStatus, address, city, stateOptions, zip, jobNames));
			gridLayout.add(applyChangesButton, 2, 18);
		
		}

	}


	public class ApplyChangesController implements EventHandler<ActionEvent> {
		Connection dbConnection;
		GridPane gridLayout;
		ComboBox<String> jobNames;
		ComboBox<USState> state;

		public ApplyChangesController(Connection dbConnection, GridPane gridLayout, TextField memberID, TextField fName,
				TextField lName, TextField dateJoined, TextField number, TextField dob, TextField maritialStatus,
				TextField address, TextField city, ComboBox<USState> state, TextField zip, ComboBox<String> jobNames) {
			this.dbConnection = dbConnection;
			this.gridLayout = gridLayout;
			this.jobNames = jobNames;
			this.state = state;
			dateJoinedAlert = new Label();
			dateJoinedAlert.setTextFill(Color.web("#cd0000"));
			gridLayout.add(dateJoinedAlert, 2, 6);

			numberAlert = new Label();
			numberAlert.setTextFill(Color.web("#cd0000"));
			gridLayout.add(numberAlert, 2, 7);

			dobAlert = new Label();
			dobAlert.setTextFill(Color.web("#cd0000"));
			gridLayout.add(dobAlert, 2, 8);

			maritialStatusAlert = new Label();
			maritialStatusAlert.setTextFill(Color.web("#cd0000"));
			gridLayout.add(maritialStatusAlert, 2, 9);
			
		}

		@Override
		public void handle(ActionEvent arg0) {
			boolean validData = true;
			dateJoinedAlert.setText("");
			numberAlert.setText("");
			dobAlert.setText("");
			maritialStatusAlert.setText("");
			dobAlert.setText("");

			if (!dateJoined.getText().equals("")) {
				if (dateJoined.getText().length() != 10 || !dateJoined.getText().substring(4, 5).equals("-")
						|| !dateJoined.getText().substring(7, 8).equals("-")) {
					dateJoinedAlert.setText("*Field must be in yyyy-mm-dd format");
					validData = false;
				}
			}

			if (!number.getText().equals("") && number.getText().length() != 10) {
				numberAlert.setText("*Number must be 10 digits without spaces");
				validData = false;
			}

			if (!dob.getText().equals("")) {
				if (dob.getText().length() != 10 || !dob.getText().substring(4, 5).equals("-")
						|| !dob.getText().substring(7, 8).equals("-")) {
					dobAlert.setText("*Field must be in yyyy-mm-dd format");
					validData = false;
				}
			}

			if (!maritialStatus.getText().equals("") && !maritialStatus.getText().toUpperCase().equals("M")
					&& !maritialStatus.getText().toUpperCase().equals("S")
					&& !maritialStatus.getText().toUpperCase().equals("D")
					&& !maritialStatus.getText().toUpperCase().equals("W")) {
				maritialStatusAlert.setText("*Please enter 'M', 'S', 'D' or 'W' ");
				validData = false;
			}

			
			if (validData) {
				int jobID;
				if (jobNames.getValue()!=null) {
					try {
						
						jobID = MemberIO.getJobID(dbConnection, jobNames.getValue());
					}
					catch(SQLException sqle) {
						jobID = -1;
					}
				}
				else {
					 jobID = -1;
				}
				String stateName ="";
				if (stateOptions.getValue()!=null) {
					stateName = stateOptions.getValue().toString();
				}

				try {
					boolean empty = MemberIO.applyChanges(dbConnection, memberID.getText(), fName.getText(), lName.getText(),
							dateJoined.getText(), number.getText(), dob.getText(), maritialStatus.getText(),
							address.getText(), city.getText(), stateName, zip.getText(), jobID);
					if (empty) {
						
						JOptionPane.showMessageDialog(null, "No changes");
					}
					else {
						JOptionPane.showMessageDialog(null, "Changes applied");
					}

				} catch (SQLException sqle) {
					JOptionPane.showMessageDialog(null, "No changes applied due to duplicate data in the database");

				} catch (IllegalArgumentException ex) {
					JOptionPane.showMessageDialog(null, "No changes applied due to invalid data");

				}
			}
		}

	}
}
