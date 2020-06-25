package Hatzalah.gui;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

 import Hatzalah.business.Branch;
import Hatzalah.data.BranchIO;
 import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ViewBranchesDisplay {

	private Connection dbConnection;
    TableView <Branch> branchTable;

	private ArrayList<Branch> branchInfo;
	private ObservableList<Branch> branches;
	
	public ViewBranchesDisplay(Connection dbConnection, Stage mainWindow, MenuBar bar) {
		
		this.dbConnection = dbConnection;
		mainWindow.setTitle("View Branches");
		BorderPane pane = new BorderPane();
		TableColumn<Branch,Integer> IDColumn = new TableColumn<>("ID");
		TableColumn<Branch,String> locationColumn = new TableColumn<>("Location");
		TableColumn<Branch,String> yearColumn = new TableColumn<>("Year Established");
		IDColumn.setCellValueFactory(new PropertyValueFactory<>("ID"));
		locationColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
		yearColumn.setCellValueFactory(new PropertyValueFactory<>("yearEstablished"));
		
		try {
			branchInfo = BranchIO.getBranches(dbConnection);
			branches = FXCollections.observableArrayList(branchInfo);

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Something went wrong");
		}
		
		branchTable = new TableView<Branch>();
		branchTable.setItems(branches);
		IDColumn.setMinWidth(200);
		locationColumn.setMinWidth(200);
		yearColumn.setMinWidth(200);
		
		branchTable.getColumns().addAll(IDColumn, locationColumn, yearColumn);
		Label branchLbl  = new Label("Branches");
		branchLbl.setId("branchLbl")
		;
		VBox vBox = new VBox(10);
		vBox.setMaxSize(650, 500);
		 
		vBox.getChildren().addAll(branchLbl, branchTable);
		vBox.setAlignment(Pos.CENTER);
		pane.setTop(bar);
		pane.setCenter(vBox);	
		Scene viewBranchesScene = new Scene(pane);
		viewBranchesScene.getStylesheets().add(getClass().getResource("Main.css").toExternalForm());

		mainWindow.setScene(viewBranchesScene);
		mainWindow.show();
	}

}
