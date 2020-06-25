package Hatzalah.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PurchaseIO {

	public static void addBus(Connection dbConnection, String vin, LocalDate purchaseDate, LocalDate maintenanceDate, Double price, String branchName) throws SQLException {
		int id = 0;
		
		try {
		    dbConnection.setAutoCommit(false);

			Statement statement = dbConnection.createStatement();

			String branchQuery = "select branchID from branch where branchName =" + "'" + branchName + "'";
			
			ResultSet rs = statement.executeQuery(branchQuery);
			
			while(rs.next()) {
			 id = rs.getInt("branchID");
			}

			String query = "insert into BUS (Vin, datePurchased, dateLastMaintained, purchasePrice, branchID) values(?,?,?,?,?)";

			PreparedStatement pStatement = dbConnection.prepareStatement(query);
		    pStatement.setString(1, vin);
		    pStatement.setDate(2, java.sql.Date.valueOf(purchaseDate));
		    if(maintenanceDate == null) {
			    pStatement.setDate(3, null);
		    }
		    else {
		    pStatement.setDate(3, java.sql.Date.valueOf(maintenanceDate));
		    }
		    pStatement.setDouble(4, price);
		    pStatement.setInt(5, id);
			
			pStatement.executeUpdate();
		    pStatement.close();
		    
		 	dbConnection.commit();
	}
		catch(SQLException sqle) {
			dbConnection.rollback();
			System.out.println(sqle.getMessage());
			throw sqle;
		}
		
	}
	
	public static ArrayList<String> getSupplies(Connection dbConnection) throws SQLException{
		ArrayList<String> supplies = new ArrayList<String>();
		String sql = "select medicalSupplyDesc from MEDICALSUPPLIES";
	
			PreparedStatement pStatement = dbConnection.prepareStatement(sql);
			ResultSet rs = pStatement.executeQuery();
			while(rs.next()) {
				supplies.add(rs.getString("medicalSupplyDesc"));
			}
			supplies.add("OTHER");
			return supplies;
		
	}

	public static void addSupply(String text, Connection dbConnection) throws SQLException {
		String sql = "insert into medicalsupplies values(?)";
		PreparedStatement pStatement = dbConnection.prepareStatement(sql);
		pStatement.setString(1, text);
		pStatement.execute();
		
	}

	public static void purchaseSupply(Connection dbConnection, String supplyName, String branch, String date, String price, String qty)throws SQLException {
		int branchID = MemberIO.getBranchID(dbConnection, branch);
		int supplyID = PurchaseIO.getSupplyId (dbConnection,  supplyName);
		String sql = "insert into medicalsuppliespurchase values (?, ?, ?, ?, ?)";
		PreparedStatement pStatement = dbConnection.prepareStatement(sql);
		pStatement.setInt(1, supplyID);
		pStatement.setInt(2, branchID);
		pStatement.setDate(3, java.sql.Date.valueOf(date));
		pStatement.setDouble(4, Double.valueOf(price));
		pStatement.setInt(5,  Integer.valueOf(qty));
		pStatement.execute();
		
		
	}

	private static int getSupplyId(Connection dbConnection, String supplyName) throws SQLException {
		int id = 0;
		String sql = "select medicalSupplyID from medicalsupplies where medicalSupplyDesc = ?";
		PreparedStatement pStatement = dbConnection.prepareStatement(sql);
		pStatement.setString(1,supplyName);
		ResultSet rs = pStatement.executeQuery();
		while(rs.next()) {
			id = rs.getInt("medicalSupplyID");
		}
		return id;
	}
	
	public static List<String> getEquipment(Connection dbConnection) throws SQLException{
		ArrayList<String> list = new ArrayList<String>();
		String sql = "select medicalEquipmentDesc from medicalEquipment";
		
		
		PreparedStatement pStatement = dbConnection.prepareStatement(sql);
		ResultSet rs = pStatement.executeQuery();
		while (rs.next()) {
			list.add(rs.getString("medicalEquipmentDesc"));
		}
		list.add("OTHER");
		return list;
	}

	public static void addEquipment(String text, Connection dbConnection) throws SQLException{
		String sql = "insert into medicalEquipment values(?)";
		PreparedStatement pStatement = dbConnection.prepareStatement(sql);
		pStatement.setString(1, text);
		pStatement.execute();
		
	}

	public static void addEquipment(Connection dbConnection, String branch, String equipment, String datePurchased, double price
			)  throws SQLException{

		int branchID = MemberIO.getBranchID(dbConnection, branch);
		int equipmentID = PurchaseIO.getEquipmentID (dbConnection,  equipment);
		String sql = "insert into medicalequipmentpurchase values (?, ?, ?, ? )";
		PreparedStatement pStatement = dbConnection.prepareStatement(sql);
		pStatement.setInt(1, equipmentID);
		pStatement.setInt(2, branchID);
		pStatement.setDate(3, java.sql.Date.valueOf(datePurchased));
		pStatement.setDouble(4, Double.valueOf(price));
		pStatement.execute();
	}

	private static int getEquipmentID(Connection dbConnection, String equipment) throws SQLException {
		int id = 0;
		String sql = "select medicalEquipmentID from medicalEquipment where medicalEquipmentDesc = ?";
		PreparedStatement pStatement = dbConnection.prepareStatement(sql);
		pStatement.setString(1,equipment);
		ResultSet rs = pStatement.executeQuery();
		while(rs.next()) {
			id = rs.getInt("medicalEquipmentID");
		}
		return id;
	}
}

