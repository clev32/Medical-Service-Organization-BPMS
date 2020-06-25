package Hatzalah.gui;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import javax.swing.JOptionPane;

import Hatzalah.data.MemberIO;
import Hatzalah.data.PurchaseIO;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
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
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class AddEquipmentWindow {
	private ComboBox<String> branches;
	private ComboBox<String> equipment;
	private Connection dbConnection;
	private MenuBar bar;
	private TextField datePurchased;
	private TextField price;
	private TextField equip;
	private GridPane gridLayout;
	Label dateAlert;
	Label branchAlert;
	Label priceAlert;
	Label qtyAlert;
	private DatePicker date;


	public AddEquipmentWindow(Stage mainWindow, MenuBar bar, Connection dbConnection) {
		this.dbConnection = dbConnection;
		this.bar = bar;
		mainWindow.setTitle("Add Medical Equipment");
		
		
		equipment = new ComboBox<String>();
		try {
			
			equipment.setItems(FXCollections.observableArrayList(PurchaseIO.getEquipment(dbConnection)));
		}
		catch(SQLException sqle) {
			JOptionPane.showMessageDialog(null,  "Cannot retrieve equipmentList");
		}
		branches = new ComboBox<String>();
		try {
			
			branches.setItems(FXCollections.observableArrayList(MemberIO.getBranchNames(dbConnection)));
		}
		catch(SQLException sqle) {
			JOptionPane.showMessageDialog(null,  "Cannot retrieve branches ");

		}
		BorderPane borderLayout = new BorderPane();
		GridPane gridLayout = new GridPane();
		borderLayout.setTop(bar);
		borderLayout.setCenter(gridLayout);
		gridLayout.add(new Label("Equipment: "), 0, 0);
		gridLayout.add(equipment, 1, 0);
		gridLayout.setAlignment(Pos.CENTER);

		
		datePurchased = new TextField();
		price = new TextField();

		gridLayout.setHgap(8);
		gridLayout.setVgap(8);

		Button goBtn = new Button("GO");
		gridLayout.add(goBtn, 2, 0);
		gridLayout.getStyleClass().add("grid");

		goBtn.setOnAction(new GoBtnHandler(dbConnection, gridLayout));
		
		Scene scene = new Scene(borderLayout);
		scene.getStylesheets().add(getClass().getResource("Main.css").toExternalForm());

		mainWindow.setScene(scene);

	}

	class GoBtnHandler implements EventHandler<ActionEvent> {
		Connection dbConnection;
		GridPane gridLayout;
		
		public GoBtnHandler (Connection dbConnection, GridPane gridLayout) {
			this.dbConnection = dbConnection;
			this.gridLayout = gridLayout;
			date = new DatePicker();
			equip = new TextField();
			price = new TextField();
			branchAlert = new Label();
			branchAlert.setTextFill(Color.web("#cd0000"));
			priceAlert = new Label();
			priceAlert.setTextFill(Color.web("#cd0000"));
			
			dateAlert = new Label();
			dateAlert.setTextFill(Color.web("#cd0000"));
		}
		@Override
		public void handle(ActionEvent arg0) {

			if (equipment.getValue() == null) {
				JOptionPane.showMessageDialog(null, "Please choose a piece of  equipment!");
			} else if (equipment.getValue().equals("OTHER")) {
				gridLayout.add(new Label("Please enter the equipment name: "), 0, 1);
				gridLayout.add(equip, 1, 1);
				Button addBtn = new Button("ADD");
				gridLayout.add(addBtn, 2, 1);
				addBtn.setOnAction(e -> {
					if (equip.getText().equals("")) {
						JOptionPane.showMessageDialog(null, "Please enter an equipment!");
					} else {
						try {
							PurchaseIO.addEquipment(equip.getText(), dbConnection);
							infoDisplay(equip.getText());
						} catch (SQLException sqle) {
							JOptionPane.showMessageDialog(null, "Error adding " + equip.getText());
						}
					}
				});

			} else {
				infoDisplay(equipment.getValue());
			}
		}

		private void infoDisplay(String equipment) {
			gridLayout.add(new Label("Branch: "), 0, 3);
			gridLayout.add(branches, 1, 3);
			gridLayout.add(branchAlert, 2, 3);
			gridLayout.add(new Label("Date Purchased: "), 0, 4);
			gridLayout.add(date, 1, 4);
			gridLayout.add(dateAlert, 2, 4);
			
			gridLayout.add(new Label("Price: "), 0, 5);
			gridLayout.add(price, 1, 5);
			gridLayout.add(priceAlert, 2, 5);
			Button addEquipmentBtn = new Button("Add Equipment");
			
			addEquipmentBtn.setOnAction(new AddEquipHandler( equipment));
			gridLayout.add(addEquipmentBtn, 1, 6);

		}
	}

	class AddEquipHandler implements EventHandler<ActionEvent> {
		String equipment;
		public AddEquipHandler(String equipment) {
			this.equipment = equipment;
		}
		@Override
		public void handle(ActionEvent arg0) {
// TODO Auto-generated method stub
			try {
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
				
				if (valid) {
					
					PurchaseIO.addEquipment(dbConnection, branches.getValue(), equipment,
							purchaseDate.toString(), Double.parseDouble(price.getText()));
					JOptionPane.showMessageDialog(null, "record added");
				}

			} catch (SQLException ex) {
				JOptionPane.showMessageDialog(null, "error occurred");

			}
		}
	}
}
