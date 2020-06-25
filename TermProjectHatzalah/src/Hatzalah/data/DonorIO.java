package Hatzalah.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javafx.collections.ObservableList;

public class DonorIO {
	
	public static void addDonor(Connection dbConnection, String fName, String lName, String address, String city, String state, String zip) throws SQLException
	{
		try {
			String query = "insert into DONORS(firstName, lastName, streetAddress, city, state, zip) values (?,?,?,?,?,?)";

			PreparedStatement pStatement = dbConnection.prepareStatement(query);
		
		    dbConnection.setAutoCommit(false);
		    
			pStatement.setString(1, fName);
			pStatement.setString(2, lName);
			pStatement.setString(3, address);
			pStatement.setString(4, city);
			pStatement.setString(5, state);
			pStatement.setString(6, zip);
			
			pStatement.executeUpdate();
		    pStatement.close();
		    
		 	dbConnection.commit();
		}
		catch(SQLException sqlE)
		{
			dbConnection.rollback(); 
			throw sqlE;
		}
	}
	
	public static boolean editDonor(Connection dbConnection, String fName, String lName, String address, String city, String state, String zip, int donorID) throws SQLException
	{
		boolean updated = false;
		try
		{
		    dbConnection.setAutoCommit(false);

			if(!fName.equals("")){
				String query = "UPDATE DONORS SET firstName = ? where donorid = ?";
				PreparedStatement pStatement = dbConnection.prepareStatement(query);
				pStatement.setString(1, fName);
				pStatement.setInt(2, donorID);
			    pStatement.executeUpdate();
				pStatement.close();
				updated = true;

			}
	
			if(!lName.equals("")) {
				String query = "UPDATE DONORS SET lastName = ? where donorid= ?";
				PreparedStatement pStatement = dbConnection.prepareStatement(query);
				pStatement.setString(1, lName);
				pStatement.setInt(2, donorID);
				pStatement.executeUpdate();
				pStatement.close();
				updated = true;

			}
		
			
			if(!address.equals("")) {
				String query = "UPDATE DONORS SET streetAddress = ? where donorid= ?";
				PreparedStatement pStatement = dbConnection.prepareStatement(query);
				pStatement.setString(1, address);
				pStatement.setInt(2, donorID);
				pStatement.executeUpdate();
				pStatement.close();
				updated = true;

			}
			
			if(!city.equals("")) {
				String query = "UPDATE DONORS SET city = ? where donorid= ?";
				PreparedStatement pStatement = dbConnection.prepareStatement(query);
				pStatement.setString(1, city);
				pStatement.setInt(2, donorID);
				pStatement.executeUpdate();
				updated = true;
				pStatement.close();
			}
			
			if(!state.equals("null")) {
				String query = "UPDATE DONORS SET state = ? where donorid= ?";
				PreparedStatement pStatement = dbConnection.prepareStatement(query);
				pStatement.setString(1, state);
				pStatement.setInt(2, donorID);
				pStatement.executeUpdate();
				updated = true;
				pStatement.close();
			}
			
			if(!zip.equals("")) {
				String query = "UPDATE DONORS SET zip = ? where donorid= ?";
				PreparedStatement pStatement = dbConnection.prepareStatement(query);
				pStatement.setString(1, zip);
				pStatement.setInt(2, donorID);
				pStatement.executeUpdate();
				updated = true;
				pStatement.close();
			}
		    
		 	dbConnection.commit();
		 
		    	
		}
		catch(SQLException sqlE)
		{
			dbConnection.rollback();
			throw sqlE;
		}
		return updated;
	}
	
	
	public static void addDonation(Connection dbConnection, double donationAmnt, int donorID, String branchName) throws SQLException
	{
		int id = 0;
		try {
		    dbConnection.setAutoCommit(false);

			Statement statement = dbConnection.createStatement();

			String branchQuery = "select branchID from branch where branchName ='" + branchName + "'";
			
			ResultSet rs = statement.executeQuery(branchQuery);
			
			while(rs.next()) {
			 id = rs.getInt("branchID");
			}
			String query = "insert into DONATIONS(donationAmnt, donorID, branchID) values (?, ?, ?)";

			PreparedStatement pStatement = dbConnection.prepareStatement(query);
		    
			pStatement.setDouble(1, donationAmnt);
			pStatement.setInt(2, donorID);
			pStatement.setInt(3, id);
			
			pStatement.executeUpdate();
		    pStatement.close();
		    
		 	dbConnection.commit();
		}
		catch(SQLException sqlE)
		{
			dbConnection.rollback(); 
			throw sqlE;
		}
	}
	
	public static boolean verifyDonorID(Connection dbConnection, int donorID)throws SQLException {
		String sql = "select * from donors where donorID = ?";
		PreparedStatement pStatement = dbConnection.prepareStatement(sql);
		pStatement.setInt(1, donorID);
		ResultSet rs = pStatement.executeQuery();
		if(rs.next()) {
			return true;
		}
		else {
			return false;
		}
		
	}
	
	public static ArrayList<String> getBranchNames(Connection dbConnection)throws SQLException
	{
		try {
		
		ArrayList<String> branchNames = new ArrayList<String>();
		Statement statement = dbConnection.createStatement();
		ResultSet rs = statement.executeQuery("select branchName from branch");
		while (rs.next())
		{
			branchNames.add(rs.getString("branchName"));	
		}
		
		rs.close();
		statement.close();
		return branchNames;
		}
		catch(SQLException sqlE)
		{
			throw sqlE;
		}
	}
}
