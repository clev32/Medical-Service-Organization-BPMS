package Hatzalah.gui;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;

import javax.swing.JOptionPane;

import Hatzalah.data.DonorIO;
import Hatzalah.data.PurchaseIO;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class AddBusDisplay {
	private Connection dbConnection;
	private TextField vin_txt;
	private DatePicker purchaseDate;
	private DatePicker maintenanceDate;
	private TextField purchasePrice_txt;
	private ComboBox<String> branchNames;
	private Label vinAlert;   
	private Label purchaseDateAlert;
	private Label maintenanceDateAlert;  
	private Label purchasePriceAlert;
	private Label branchNamesAlert;

	public AddBusDisplay(Connection dbConnection, Stage mainWindow, MenuBar bar) {
		
		this.dbConnection = dbConnection;
		mainWindow.setTitle("Add Bus");
		BorderPane pane = new BorderPane();
		GridPane gridLayout = new GridPane();
		gridLayout.setAlignment(Pos.CENTER);
		gridLayout.getStyleClass().add("grid");

		gridLayout.setHgap(5); 
		gridLayout.setVgap(10); 
		
		Label vinLbl = new Label("VIN:");
		vin_txt = new TextField();
		
		Label purchaseDateLbl = new Label("Purchase Date");
		purchaseDate = new DatePicker();
		
		Label maintenanceLbl = new Label("Date Last Maintained (optional):");
		maintenanceDate = new DatePicker();
		
		Label purchasePriceLbl = new Label("Purchase Price:");
		purchasePrice_txt = new TextField();
		
		Label branchName = new Label("Branch Name");
		branchNames = new ComboBox<>();
		
		try {
			branchNames.setItems( FXCollections.observableArrayList(DonorIO.getBranchNames(dbConnection)));
			branchNames.setPromptText("---Branches---");
		} 
		catch (SQLException sqlE) {
			JOptionPane.showMessageDialog(null, "Cannot retrieve list of branches");
		}
		
		Group alertLblGroup = new Group();
		vinAlert = new Label("*");
		purchaseDateAlert = new Label("*");
		maintenanceDateAlert = new Label();
		purchasePriceAlert = new Label("*");
		branchNamesAlert = new Label("*");
		
		alertLblGroup.getChildren().addAll(vinAlert, purchaseDateAlert, maintenanceDateAlert, 
				purchasePriceAlert, branchNamesAlert);
		for(Node node: alertLblGroup.getChildren()) {
			node.setId("AlertLabel");
		}
		
		gridLayout.add(vinLbl, 0, 0);
		gridLayout.add(vin_txt, 1, 0);
		gridLayout.add(purchaseDateLbl, 0, 1);
		gridLayout.add(purchaseDate, 1, 1);
		gridLayout.add(maintenanceLbl, 0, 2);
		gridLayout.add(maintenanceDate, 1, 2);
		gridLayout.add(purchasePriceLbl, 0, 3);
		gridLayout.add(purchasePrice_txt, 1, 3);
		gridLayout.add(branchName, 0, 4);
		gridLayout.add(branchNames, 1, 4);
		
		gridLayout.add(vinAlert, 2, 0);
		gridLayout.add(purchaseDateAlert, 2, 1);
		gridLayout.add(maintenanceDateAlert, 2, 2);
		gridLayout.add(purchasePriceAlert, 2, 3);
		gridLayout.add(branchNamesAlert, 2, 4);
		
		Button addBusButton = new Button("ADD");
		addBusButton.setOnAction(new AddBusHandler());
		
		gridLayout.add(addBusButton, 1, 5);
		
		pane.setTop(bar);
		pane.setCenter(gridLayout);
		Scene addBusScene = new Scene(pane);
		mainWindow.setScene(addBusScene);
		addBusScene.getStylesheets().add(getClass().getResource("Main.css").toExternalForm());
	}

	class AddBusHandler implements EventHandler<ActionEvent>{

		@Override
		public void handle(ActionEvent arg0) {
			reset();
			Boolean isValid = true;
			LocalDate mDate = null;
			
			try {
			
			String Vin = vin_txt.getText();
			LocalDate today = LocalDate.now();
			LocalDate pDate = purchaseDate.getValue();
			try {
			mDate = maintenanceDate.getValue();
			}
			catch(NullPointerException ex) {
				//the maintenance date is allowed to be null
				}
			Double price = Double.valueOf(purchasePrice_txt.getText());
			String branchName = branchNames.getValue();

			if(Vin.trim().equals("") || Vin.length() != 17) {
				vinAlert.setText("*Vin must be 17 characters long");
				isValid = false;		
			}
			
			if(pDate == null || pDate.compareTo(today) > 0){
				purchaseDateAlert.setText("*Please select valid date");
				isValid = false;		

			}
			
			if(mDate != null && mDate.compareTo(pDate) < 0) {
				maintenanceDateAlert.setText("*Maintenance date cannot be before purchase date!");
				isValid = false;		

			}
			
			if(price <=0) {
				purchasePriceAlert.setText("*Price must be greater than 0");
				isValid = false;		

			}
			
			if(branchName==null) {
				branchNamesAlert.setText("*You must choose a branch from the drop down");
				isValid =false;
			}
			
			if(isValid) {
				PurchaseIO.addBus(dbConnection, Vin, pDate, mDate, price, branchName);
				JOptionPane.showMessageDialog(null, "Bus Added");
			}
			
		}
		
		catch(NumberFormatException numEx) {
			purchasePriceAlert.setText("*Please enter valid number");
			isValid = false;		

		}
	
		catch(SQLException sqle) {
			JOptionPane.showMessageDialog(null, "Something went wrong");
		}
	}
		
		private void reset() {
			vinAlert.setText(""); 
			purchaseDateAlert.setText("");
			maintenanceDateAlert.setText("");
			purchasePriceAlert.setText("");
			branchNamesAlert.setText("");
		}
		}

}
