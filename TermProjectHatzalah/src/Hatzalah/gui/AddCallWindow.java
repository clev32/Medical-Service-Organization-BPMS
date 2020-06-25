package Hatzalah.gui;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import Hatzalah.data.CallIO;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class AddCallWindow {
	private Connection dbConnection;
	private MenuBar bar;
	private TextField fname_txt;
	private TextField lname_txt;
	private TextField age_txt;
	private TextField address_txt;
	private TextField city_txt;
	private CheckBox transported;
	//private ComboBox<String> responders;
	private ComboBox<String> branches;
	private ComboBox<String> buses;
	private ComboBox<USState> stateOptions;
	private GridPane gridLayout;
	private int callID;
	private Button addSymptomBtn;
	private Button addResponderBtn;
	private Button equipmentBtn;
	
	public AddCallWindow(Stage mainWindow, MenuBar bar, Connection dbConnection) {
		this.dbConnection = dbConnection;
		this.bar = bar;
		this.gridLayout = new GridPane();
		mainWindow.setTitle("Add a Call");

		BorderPane pane = new BorderPane();
		gridLayout.setHgap(8);
		gridLayout.setVgap(8);
		gridLayout.getStyleClass().add("grid");
		gridLayout.setAlignment(Pos.CENTER);

		fname_txt = new TextField();
		lname_txt = new TextField();
		age_txt  = new TextField();
		address_txt  = new TextField();
		city_txt = new TextField();
		
		Label fnameLbl = new Label("Patient First Name:");
		Label lnameLbl = new Label("Patient Last Name:");
		Label ageLbl = new Label("Patient Age:");
		Label addressLbl = new Label("Patient Address:");
		Label cityLbl = new Label("Patient City:");
		Label stateLbl = new Label("Patient State:");
		Label branchLbl = new Label("Branch Id:");
		Button addCallBtn = new Button("Add Call");
		stateOptions = new ComboBox<>();
		stateOptions.setItems( FXCollections.observableArrayList(USState.values()));
		branches = new ComboBox<>();
		branches.setItems( FXCollections.observableArrayList(CallIO.getBranches(dbConnection)));

		buses = new ComboBox<>();
		buses.setItems( FXCollections.observableArrayList(CallIO.getBuses(dbConnection)));

		addSymptomBtn = new Button("Add a Symptom");
		addResponderBtn = new Button("Add Responder");
		equipmentBtn = new Button("Add Equipment");
		addSymptomBtn.setDisable(true);
		addResponderBtn.setDisable(true);
		equipmentBtn.setDisable(true);
		
		pane.setCenter(gridLayout);
		addCallBtn.setOnAction(new AddCallHandler());
		addResponderBtn.setOnAction(e -> new AddResponderWindow(dbConnection, callID));
		addSymptomBtn.setOnAction(e->new AddSymptomWindow(dbConnection, callID));
		equipmentBtn.setOnAction(e-> new AddMedicalEquipmentWindow(dbConnection, callID));
		
		transported = new CheckBox("Patient taken to hospital ");
		gridLayout.add(fnameLbl, 0, 0);
		gridLayout.add(fname_txt, 1, 0);
		gridLayout.add(lnameLbl, 0, 1);
		gridLayout.add(lname_txt, 1, 1);
		gridLayout.add(addressLbl, 0, 2);
		gridLayout.add(address_txt, 1, 2);		
		gridLayout.add(cityLbl, 0, 3);
		gridLayout.add(city_txt, 1, 3);
		gridLayout.add(stateLbl, 0, 4);
		gridLayout.add(stateOptions, 1, 4);
		gridLayout.add(branchLbl, 0, 5);
		gridLayout.add(branches, 1, 5);		
		gridLayout.add(ageLbl, 0, 7);
		gridLayout.add(age_txt, 1, 7);
		gridLayout.add(transported, 1, 8);
		gridLayout.add(addCallBtn, 0, 10);
		gridLayout.add(addResponderBtn, 0, 13);
		gridLayout.add(addSymptomBtn, 0, 11);
		gridLayout.add(equipmentBtn, 0, 12);
		
		Label busLbl = new Label();
		gridLayout.add(busLbl, 0, 9);

		transported.setOnAction(e -> {
			busLbl.setText("BUS: ");
			gridLayout.add(buses, 1, 9);
			
		});
			
	
		pane.setTop(bar);
		Scene addCallScene = new Scene(pane);
		addCallScene.getStylesheets().add(getClass().getResource("Main.css").toExternalForm());

		mainWindow.setScene(addCallScene);
		
	}
	class AddCallHandler implements EventHandler<ActionEvent>{


		@Override
		public void handle(ActionEvent event) {
			
			try {
				String fname = fname_txt.getText();
				String lname = lname_txt.getText();
				int age = Integer.valueOf(age_txt.getText());
				String address = address_txt.getText();
				String city = city_txt.getText();
				String state = String.valueOf(stateOptions.getValue());
				String bus =  buses.getValue();
				int branchId = Integer.valueOf(branches.getValue());
				
				CallIO.addCall(dbConnection, age, address, city, state, bus, fname, lname, branchId);
				JOptionPane.showMessageDialog(null, "Call added");
				
				callID = CallIO.getCallId(dbConnection, fname, lname, age);
				
				addSymptomBtn.setDisable(false);
				addResponderBtn.setDisable(false);
				equipmentBtn.setDisable(false);
				
			

			} catch( SQLException e) {
				JOptionPane.showMessageDialog(null, e.getMessage());

			}
		}
	}
	
	
	

}
