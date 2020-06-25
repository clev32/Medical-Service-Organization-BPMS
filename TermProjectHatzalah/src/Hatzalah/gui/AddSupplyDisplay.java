package Hatzalah.gui;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;

import javax.swing.JOptionPane;

import Hatzalah.data.MemberIO;
import Hatzalah.data.PurchaseIO;
import Hatzalah.gui.StatisticsDisplay.GoBtnController;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class AddSupplyDisplay {
	Connection dbConnection;
	Stage mainWindow;
	MenuBar bar;
	ComboBox<String> supplies;
	Label dateAlert;
	Label branchAlert;
	Label priceAlert;
	Label qtyAlert;
	private DatePicker date;

	TextField supply;
	TextField price;
	TextField qty;
	boolean supplyButtonClicked;

	public AddSupplyDisplay(Connection dbConnection, Stage mainWindow, MenuBar bar) {
		this.dbConnection = dbConnection;
		this.mainWindow = mainWindow;
		this.bar = bar;
		supplies = new ComboBox<>();
		try {
			supplies.setItems(FXCollections.observableArrayList(PurchaseIO.getSupplies(dbConnection)));
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, "Cannot retrieve list of supplies");
		}

		supplyButtonClicked = false;
		BorderPane borderLayout = new BorderPane();
		GridPane gridLayout = new GridPane();
		borderLayout.setTop(bar);
		gridLayout.getStyleClass().add("grid");
		gridLayout.setVgap(5);
		gridLayout.setHgap(10);

		gridLayout.add(new Label("Select Supply: "), 0, 0);
		gridLayout.add(supplies, 1, 0);
		Button goBtn = new Button("GO");
		gridLayout.add(goBtn, 2, 0);
		goBtn.setOnAction(new GoBtnController(dbConnection, gridLayout));
		borderLayout.setCenter(gridLayout);
		gridLayout.setAlignment(Pos.TOP_CENTER);

		Scene scene = new Scene(borderLayout);
		scene.getStylesheets().add(getClass().getResource("Main.css").toExternalForm());

		mainWindow.setScene(scene);
	}

	public class GoBtnController implements EventHandler<ActionEvent> {
		Connection dbConnection;
		GridPane gridLayout;

		ComboBox<String> branches;

		public GoBtnController(Connection dbConnection, GridPane gridLayout) {
			this.dbConnection = dbConnection;
			this.gridLayout = gridLayout;
			date = new DatePicker();
			supply = new TextField();
			price = new TextField();
			qty = new TextField();
			branchAlert = new Label();
			branchAlert.setTextFill(Color.web("#cd0000"));
			priceAlert = new Label();
			priceAlert.setTextFill(Color.web("#cd0000"));
			qtyAlert = new Label();
			qtyAlert.setTextFill(Color.web("#cd0000"));
		}

		@Override
		public void handle(ActionEvent arg0) {

			if (supplies.getValue() == null) {
				JOptionPane.showMessageDialog(null, "Please choose a supply!");
			} else if (supplies.getValue().equals("OTHER")) {
				gridLayout.add(new Label("Please enter the supply name: "), 0, 1);
				gridLayout.add(supply, 1, 1);
				Button addSupplyButton = new Button("ADD SUPPLY");
				gridLayout.add(addSupplyButton, 2, 1);
				addSupplyButton.setOnAction(e -> {
					if (supply.getText().equals("")) {
						JOptionPane.showMessageDialog(null, "Please enter a supply!");
					} else {
						if (supplyButtonClicked) {
							JOptionPane.showMessageDialog(null, "Can only enter one Supply at a time!");
						} else {

							try {

								PurchaseIO.addSupply(supply.getText(), dbConnection);
								supplyButtonClicked = true;
								infoDisplay(supply.getText());

							} catch (SQLException sqle) {
								JOptionPane.showMessageDialog(null, "Error adding " + supply.getText());
							}
						}
					}
				});
			} else {
//they chose an existing supply
				infoDisplay(supplies.getValue());
			}

		}

		private void infoDisplay(String supplyName) {
			branches = new ComboBox<>();
			try {
				branches.setItems(FXCollections.observableArrayList(MemberIO.getBranchNames(dbConnection)));
			} catch (SQLException sqle) {
				JOptionPane.showMessageDialog(null, "Cannot retrieve list of branches");
			}
			gridLayout.add(new Label("Choose Branch Name: "), 0, 2);
			gridLayout.add(branches, 1, 2);
			gridLayout.add(branchAlert, 2, 2);
			gridLayout.add(new Label("Date Purchased: "), 0, 3);
			gridLayout.add(date, 1, 3);
			
			dateAlert = new Label("");
			dateAlert.setTextFill(Color.web("#cd0000"));

			gridLayout.add(dateAlert, 2, 3);
			gridLayout.add(new Label("Purchase Price "), 0, 4);
			gridLayout.add(price, 1, 4);
			gridLayout.add(priceAlert, 2, 4);
			gridLayout.add(new Label("Quantity "), 0, 5);
			gridLayout.add(qty, 1, 5);
			gridLayout.add(qtyAlert, 2, 5);
			Button addPurchaseBtn = new Button("Add Purchase");
			addPurchaseBtn.setOnAction(
					new addPurchaseBtnController(dbConnection, gridLayout, supplyName, branches, date, price, qty, branchAlert));
			gridLayout.add(addPurchaseBtn, 1, 6);

		}
	}

	public class addPurchaseBtnController implements EventHandler<ActionEvent> {
		Connection dbConnection;
		GridPane gridLayout;
		String supplyName;
		ComboBox<String> branches;
		DatePicker date;
		TextField price;
		TextField qty;

		public addPurchaseBtnController(Connection dbConnection, GridPane gridLayout, String supplyName,
				ComboBox<String> branches, DatePicker date, TextField price, TextField qty, Label branchAlert) {
			this.dbConnection = dbConnection;
			this.gridLayout = gridLayout;
			this.supplyName = supplyName;
			this.date = date;
			this.price = price;
			this.qty = qty;
			this.branches = branches;
			
		}

		@Override
		public void handle(ActionEvent arg0) {
			LocalDate purchaseDate = null;
			LocalDate today = LocalDate.now();
			dateAlert.setText("");

			try {
				purchaseDate = date.getValue();
			} catch (NullPointerException ex) {
				purchaseDate = today;
			}

			boolean valid = true;
			if (branches.getValue() == null) {
				valid = false;
				branchAlert.setText("*Field is required");
			}
			else {
				branchAlert.setText("");
			}

			if (purchaseDate != null && purchaseDate.compareTo(today) > 0) {
				dateAlert.setText("*Date cannot be in the future");
				valid = false;
			}
			else {
				dateAlert.setText("*Field is required");
			}

			/*
			 * if (!date.getText().equals("")&&date.getText().length()!=10 ||
			 * !date.getText().substring(4,5).equals("-")
			 * ||!date.getText().substring(7,8).equals("-")) {
			 * dateAlert.setText("*Field must be in yyyy-mm-dd format"); valid = false;
			 * 
			 * }
			 */
			if (price.getText().equals("")) {
				valid = false;
				priceAlert.setText("*Field is required");
			} else {
				try {
					Double.parseDouble(price.getText());
					priceAlert.setText("");
				} catch (Exception ex) {
					valid = false;

					priceAlert.setText("*Field requires a decimal value");
				}
			}
			if (qty.getText().equals("")) {
				valid = false;
				qtyAlert.setText("*Field is required");

			} else {
				try {
					Integer.parseInt(qty.getText());
					qtyAlert.setText("");
				} catch (Exception ex) {
					valid = false;

					qtyAlert.setText("*Field requires an integer value");
				}
			}
			if (valid) {
				try {
					PurchaseIO.purchaseSupply(dbConnection, supplyName, branches.getValue(), purchaseDate.toString(),
							price.getText(), qty.getText());
					JOptionPane.showMessageDialog(null, "Purchase Added");

				} catch (SQLException sql) {
					JOptionPane.showMessageDialog(null, "Purchase not added due to invalid data");
				}
			}

		}
	}

}
