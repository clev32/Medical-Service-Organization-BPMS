package Hatzalah.gui;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.JOptionPane;

import Hatzalah.data.MemberIO;
import Hatzalah.data.StatisticsIO;
import Hatzalah.gui.AddMemberDisplay.AddMemberController;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class StatisticsDisplay {
	private Connection dbConnection;
	private Stage mainWindow;
	private MenuBar bar;
	private ComboBox<String> branchNames;
	private TableView<String> statisticsTable;
	private TableColumn<String, String> numMembersCol;
	private TableColumn<String, String> numBussesCol;
	private TableColumn<String, String> avgCallsCol;
	private TableColumn<String, String> numDonorsCol;
	private TableColumn<String, String> amtDonationsCol;
	private TableColumn<String, String> amtCoordinatorsCol;
	private TableColumn<String, String> amtDispatchersCol;
	private TableColumn<String, String> amtRespondersCol;
	private TableColumn<String, String> amtPurchasesCol;

	public StatisticsDisplay(Connection dbConnection, Stage mainWindow, MenuBar bar) {
		this.dbConnection = dbConnection;
		this.mainWindow = mainWindow;
		mainWindow.setTitle("Statistics");

		this.bar = bar;
		branchNames = new ComboBox<>();
		try {
			branchNames.setItems(FXCollections.observableArrayList(MemberIO.getBranchNames(dbConnection)));
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, "Cannot retrieve list of branches");
		}

		BorderPane borderLayout = new BorderPane();
		GridPane gridLayout = new GridPane();
		borderLayout.setTop(bar);

		gridLayout.add(new Label("Select the branch name you would like to view statistics for: "), 0, 0);
		gridLayout.add(branchNames, 1, 0);
		Button goBtn = new Button("GO");
		gridLayout.add(goBtn, 2, 0);
		goBtn.setOnAction(new GoBtnController(dbConnection, gridLayout));
		
		
		
		gridLayout.setAlignment(Pos.TOP_CENTER);

		statisticsTable = new TableView<String>();
		numMembersCol = new TableColumn<String, String>("Number of Members:");
		numBussesCol = new TableColumn<String, String>("Number of Busses:");
		avgCallsCol = new TableColumn<String, String>("Average Calls:");
		numDonorsCol = new TableColumn<String, String>("Number of Donors:");
		amtDonationsCol = new TableColumn<String, String>("Total Donations this year:");
		amtCoordinatorsCol = new TableColumn<String, String>("Number of Coordinators:");
		amtDispatchersCol = new TableColumn<String, String>("Number of Dispatchers: ");
		amtRespondersCol = new TableColumn<String, String>("Number of First-Responders: ");
		amtPurchasesCol = new TableColumn<String, String>("Total amount of Purchases");

		numMembersCol.setMinWidth(150);
		numBussesCol.setMinWidth(150);
		avgCallsCol.setMinWidth(150);
		numDonorsCol.setMinWidth(150);
		amtDonationsCol.setMinWidth(150);
		amtCoordinatorsCol.setMinWidth(150);
		amtDispatchersCol.setMinWidth(150);
		amtRespondersCol.setMinWidth(150);
		amtPurchasesCol.setMinWidth(150);

		statisticsTable.getColumns().addAll(numMembersCol, numBussesCol, avgCallsCol, numDonorsCol, amtDonationsCol,
				amtCoordinatorsCol, amtDispatchersCol, amtRespondersCol, amtPurchasesCol);

		VBox vBox = new VBox(10);

		vBox.getChildren().addAll(gridLayout, statisticsTable);
		vBox.setAlignment(Pos.CENTER);
		borderLayout.setCenter(vBox);

		Scene scene = new Scene(borderLayout);
		scene.getStylesheets().add(getClass().getResource("Main.css").toExternalForm());
		mainWindow.setScene(scene);
	}

	public class GoBtnController implements EventHandler<ActionEvent> {
		Connection dbConnection;
		BorderPane borderLayout;
		GridPane gridLayout;

		Label numMember;
		Label numBusses;
		Label avgCalls;
		Label numDonors;
		Label amtDonations;
		Label amtCoordinators;
		Label amtDispatchers;
		Label amtResponders;
		Label totalPurchases;

		public GoBtnController(Connection dbConnection, GridPane gridLayout) {
			this.dbConnection = dbConnection;
			this.gridLayout = gridLayout;
		}

		@Override
		public void handle(ActionEvent arg0) {
			if (branchNames.getValue() == null) {
				JOptionPane.showMessageDialog(null, "Please choose a branch name!");
			} else {

				try {
					Button callYear = new Button ("Find amount of calls for specific year");
					gridLayout.add(callYear, 1, 1);
					Button purchaseYear = new Button ("Find total purchases for specific year");
					gridLayout.add(purchaseYear, 1, 2);
					callYear.setOnAction(e-> new CallYearController(dbConnection, branchNames.getValue()));
					purchaseYear.setOnAction(e->new PurchaseYearController(dbConnection, branchNames.getValue()));
					statisticsTable.getItems().clear();

			
					String numMembers = StatisticsIO.getNumMembers(dbConnection, branchNames.getValue());
					String numBusses = StatisticsIO.getNumBusses(dbConnection, branchNames.getValue());
					String avgCalls = StatisticsIO.getAvgCallsPerYear(dbConnection, branchNames.getValue());
					String numDonors = StatisticsIO.getNumDonors(dbConnection, branchNames.getValue());
					String totalDonations = "$" + StatisticsIO.getTotalDonations(dbConnection, branchNames.getValue());
					String numCoordinators = StatisticsIO.getNumCoordinators(dbConnection, branchNames.getValue());
					String numDispatchers = StatisticsIO.getNumDispatchers(dbConnection, branchNames.getValue());
					String numResponders = StatisticsIO.getNumResponders(dbConnection, branchNames.getValue());
					String numPurchases = "$" + StatisticsIO.getTotalPurchases(dbConnection, branchNames.getValue());
					

					numMembersCol.setCellValueFactory(c -> new SimpleStringProperty((numMembers)));
					numBussesCol.setCellValueFactory(c -> new SimpleStringProperty((numBusses)));
					avgCallsCol.setCellValueFactory(data -> new SimpleStringProperty(avgCalls));
					numDonorsCol.setCellValueFactory(data -> new SimpleStringProperty(numDonors));
					amtDonationsCol.setCellValueFactory(data -> new SimpleStringProperty(totalDonations));
					amtCoordinatorsCol.setCellValueFactory(data -> new SimpleStringProperty(numCoordinators));
					amtDispatchersCol.setCellValueFactory(data -> new SimpleStringProperty(numDispatchers));
					amtRespondersCol.setCellValueFactory(data -> new SimpleStringProperty(numResponders));
					amtPurchasesCol.setCellValueFactory(data-> new SimpleStringProperty(numPurchases));

					statisticsTable.getItems().addAll("Column");
				} catch (SQLException sqle) {
					JOptionPane.showMessageDialog(null, "Error retrieving data from the database");

				}

			}
		}

	}
	public class CallYearController extends Stage{
	      private Connection dbConnection;
	      private String branch;
	      Label results;
		public CallYearController(Connection dbConnection, String branch) {
			this.dbConnection = dbConnection;
			this.branch = branch;
			this.setTitle("Calls Per Year");
			this.setHeight(300);
			this.setWidth(600);
			BorderPane borderLayout = new BorderPane();
			
			HBox hBox= new HBox();
			Label instructionLabel = new Label ("Choose the year that you would like to find the amount of calls for " + branch );
			ComboBox<String> years = new ComboBox<String>();
			try {
				years.setItems(FXCollections.observableArrayList(StatisticsIO.getCallYearOptions(dbConnection, branch)));
				years.setPromptText("---Years---");
			} catch (SQLException sqle) {
				JOptionPane.showMessageDialog(null, "Cannot retrieve list of years");
			}
			Button go = new Button("GO");
			borderLayout.setTop(hBox);
			Button okButton = new Button ("OK");
			hBox.getChildren().addAll(instructionLabel,years, okButton);
			results = new Label();
			borderLayout.setCenter(results);
			okButton.setOnAction(e->{
				if (years.getValue()==null) {
					results.setText("Please choose a year!");
					
				}
				else {
					try {
						String year = years.getValue();
						results.setText(StatisticsIO.getCallsForYear(dbConnection, year, branch)+ " calls in " + year);
						
					}
					catch(SQLException sqle) {
						JOptionPane.showMessageDialog(null,  "Error Occurred!");
					}
				}
			});
			Scene theScene = new Scene(borderLayout);
			this.setScene(theScene);
			this.show();
			
			
			
		}
	}
	public class PurchaseYearController extends Stage{
	      private Connection dbConnection;
	      private String branch;
	      Label results;
		public PurchaseYearController(Connection dbConnection, String branch) {
			this.dbConnection = dbConnection;
			this.branch = branch;
			this.setTitle("Purchases Per Year");
			this.setHeight(300);
			this.setWidth(600);
			BorderPane borderLayout = new BorderPane();
			
			HBox hBox= new HBox();
			Label instructionLabel = new Label ("Choose the year that you would like to find total purchases for " + branch );
			ComboBox<String> years = new ComboBox<String>();
			try {
				years.setItems(FXCollections.observableArrayList(StatisticsIO.getPurchaseYearOptions(dbConnection, branch)));
				years.setPromptText("---Years---");
			} catch (SQLException sqle) {
				JOptionPane.showMessageDialog(null, "Cannot retrieve list of years");
			}
			Button go = new Button("GO");
			borderLayout.setTop(hBox);
			Button okButton = new Button ("OK");
			hBox.getChildren().addAll(instructionLabel,years, okButton);
			results = new Label();
			borderLayout.setCenter(results);
			okButton.setOnAction(e->{
				if (years.getValue()==null) {
					results.setText("Please choose a year!");
					
				}
				else {
					try {
						String year = years.getValue();
						results.setText("$" + StatisticsIO.getPurchasesForYear(dbConnection, year, branch));
						
					}
					catch(SQLException sqle) {
						JOptionPane.showMessageDialog(null,  "Error Occurred!");
					}
				}
			});
			Scene theScene = new Scene(borderLayout);
			this.setScene(theScene);
			this.show();
			
			
			
		}
	}
}