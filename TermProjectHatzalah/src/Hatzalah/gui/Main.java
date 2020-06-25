package Hatzalah.gui;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Main extends Application {

	public static void main(String[] args) {
		launch(args);

	}

	@Override
	public void start(Stage mainWindow) throws Exception {

		String url = "jdbc:sqlserver://localhost:1433;instance=SQLEXPRESS;databaseName=HATZALAHDB;integratedSecurity=true";
		Connection dbConnection = DriverManager.getConnection(url);
		mainWindow.setTitle("Main Menu");
		mainWindow.setHeight(800);
		mainWindow.setWidth(800);
		
		Menu branchMenu = new Menu("Branches");
		Menu donationMenu = new Menu("Donations");
		Menu membersMenu = new Menu("Members");
		Menu callsMenu = new Menu("Calls");
		Menu purchasesMenu = new Menu("Purchases");
	

		
		MenuItem addBranchItem = new MenuItem("Add Branch");
		MenuItem viewAllBranchesItem = new MenuItem("View All Branches");
	
		MenuItem addDonorItem = new MenuItem("Add Donor");
		MenuItem editDonorItem = new MenuItem("Edit Donor");
		MenuItem addDonationItem = new MenuItem("Add donation");
		
		MenuItem addMemberItem = new MenuItem("Add Member");
		MenuItem editMemberItem = new MenuItem("Edit Member");
		
		MenuItem addCallItem = new MenuItem("Add Call");
		MenuItem addSymptomItem = new MenuItem("Add Symptom");
		
		MenuItem addSupplyItem = new MenuItem("Add Medical Supply");
		MenuItem addEquipmentItem = new MenuItem("Add Medical Equipment");
		MenuItem addBusItem = new MenuItem("Add Bus");
		
		MenuItem statisticsItem = new MenuItem("Statistics");
			
		
		purchasesMenu.getItems().addAll(addSupplyItem, addEquipmentItem, addBusItem);
		branchMenu.getItems().addAll(addBranchItem, viewAllBranchesItem, new SeparatorMenuItem(), purchasesMenu, statisticsItem);
		donationMenu.getItems().addAll(addDonorItem , editDonorItem,  new SeparatorMenuItem(), addDonationItem);
		membersMenu.getItems().addAll(addMemberItem,editMemberItem);
		callsMenu.getItems().addAll(addCallItem, addSymptomItem);
		
		
		 MenuBar bar = new MenuBar();
		 bar.getMenus().addAll(branchMenu, donationMenu, membersMenu, callsMenu);
		
		
		 BorderPane borderLayout = new BorderPane();
		 
		 addDonorItem.setOnAction(e -> new AddDonorWindow(mainWindow, bar, dbConnection));
		 editDonorItem.setOnAction(e -> new EditDonorWindow(mainWindow, bar, dbConnection));
		 addDonationItem.setOnAction(e-> new AddDonationWindow(mainWindow, bar, dbConnection));
		 viewAllBranchesItem.setOnAction(e-> new ViewBranchesDisplay(dbConnection,mainWindow,bar ));
		 addBranchItem.setOnAction(e-> new AddBranchDisplay(dbConnection,mainWindow,bar ));
		 addMemberItem.setOnAction(e-> new AddMemberDisplay (dbConnection,mainWindow,bar ));
		 editMemberItem.setOnAction(e-> new EditMemberDisplay (dbConnection,mainWindow,bar ));
		 statisticsItem.setOnAction(e-> new StatisticsDisplay(dbConnection, mainWindow, bar));
		 addBusItem.setOnAction(e-> new AddBusDisplay(dbConnection, mainWindow, bar));
		 addSupplyItem.setOnAction(e-> new AddSupplyDisplay(dbConnection, mainWindow, bar));
		 addCallItem.setOnAction(e-> new AddCallWindow(mainWindow, bar, dbConnection));
		 addEquipmentItem.setOnAction(e-> new AddEquipmentWindow(mainWindow, bar, dbConnection));
		 addSymptomItem.setOnAction(e-> new AddSymptomDisplay(mainWindow, bar, dbConnection));
		 
		    borderLayout.setTop(bar);
		    Scene scene = new Scene(borderLayout);
		    borderLayout.getStyleClass().add("mainLayout");
		    scene.getStylesheets().add(getClass().getResource("Main.css").toExternalForm());

		    mainWindow.setOnCloseRequest(event -> {
		        try {
					dbConnection.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
		    });
		    mainWindow.setScene(scene);
		    mainWindow.show();
	}	

}
