package Hatzalah.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import Hatzalah.business.Branch;

public class BranchIO {

	public static ArrayList<Branch> getBranches(Connection dbConnection) throws SQLException {
		try {
			
			ArrayList<Branch> branches = new ArrayList<Branch>();
			Branch branch;
			
			Statement statement = dbConnection.createStatement();
			ResultSet rs = statement.executeQuery("select branchid, branchName, branchyear from branch");
			while (rs.next())
			{
				branch = new Branch(rs.getInt("branchid"), rs.getString("branchName"), rs.getString("branchyear"));
				branches.add(branch);
			}
			
			rs.close();
			statement.close();
			return branches;
			}
			catch(SQLException sqlE)
			{
				throw sqlE;
			}
		}
		
		
	


	public static void addBranch(Connection dbConnection, String location, String year) throws SQLException {
	    
		try {
		String query = "insert into branch(branchName, branchYear) values (?,?)";
		PreparedStatement pStatement = dbConnection.prepareStatement(query);
		dbConnection.setAutoCommit(false);
	
		pStatement.setString(1, location);
		pStatement.setString(2, year);	
		
		pStatement.executeUpdate();
	    pStatement.close();
	    
	 	dbConnection.commit();
		} 
		catch (SQLException sqlE) {
			dbConnection.rollback(); 
			throw sqlE;
		}
	    
		
	}
	
	
}