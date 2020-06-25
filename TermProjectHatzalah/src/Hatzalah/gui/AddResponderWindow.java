package Hatzalah.gui;

import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import Hatzalah.data.CallIO;
import Hatzalah.data.DonorIO;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class AddResponderWindow extends Stage {
	private Connection dbConnection;
	private int callID;
	private ComboBox<String> members;


	public AddResponderWindow(Connection dbConnection, int callID){
		this.dbConnection = dbConnection;
		this.setTitle("Add Responder");
		this.setWidth(600);
		this.setHeight(600);
		this.callID = callID;
		
		BorderPane borderLayout = new BorderPane();		
		
		GridPane gridLayout = new GridPane();
		
		Label addMemberLbl = new Label("Choose from the following members");
		
		members = new ComboBox<>();
		
		try {
			members.setItems( FXCollections.observableArrayList(CallIO.getMemberIDs(dbConnection)));
			members.setPromptText("---Members---");
		} 
		catch (SQLException sqlE) {
			JOptionPane.showMessageDialog(null, "Cannot retrieve list of members");
		}
		
		Button addMemberBtn = new Button("ADD");
		addMemberBtn.setOnAction(new AddMemberHandler());
		
		gridLayout.add(addMemberLbl, 0, 0);
		gridLayout.add(members, 0, 1);
		gridLayout.add(addMemberBtn, 0, 2);

		borderLayout.setCenter(gridLayout);
		Scene scene = new Scene(borderLayout);
		scene.getStylesheets().add(getClass().getResource("Main.css").toExternalForm());

		this.setScene(scene);
		this.show();
	}
	class AddMemberHandler implements EventHandler<ActionEvent>{

		@Override
		public void handle(ActionEvent arg0) {
			String ID = members.getValue();
			if(ID == null) {
				JOptionPane.showMessageDialog(null, "Please select a member");
			}
			else {
				try {
					CallIO.addMembertoCall(dbConnection, callID, ID);
					JOptionPane.showMessageDialog(null, "Member added");

				} catch (SQLException e) {
					JOptionPane.showMessageDialog(null, "Something went wrong");

				}
			}
		}
		
	}
}
