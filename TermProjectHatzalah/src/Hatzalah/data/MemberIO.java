package Hatzalah.data;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Callback;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class MemberIO {
	public static void addMemberData(Connection dbConnection, String fName, String lName, String dateJoined,
			String number, String dob, String maritialStatus, int branchID, String address, String city,
			String state, String zip, int jobID) throws SQLException, IllegalArgumentException {
		
		String sql = "exec usp_generateMemberID ?, ?,?, ?, ?, ?, ?, ?, ?,?, ?, ?";

		PreparedStatement pStatement = dbConnection.prepareStatement(sql);
		pStatement.setString(1, fName);
		pStatement.setString(2, lName);
		pStatement.setDate(3, java.sql.Date.valueOf(dateJoined));
		pStatement.setString(4, number);
		pStatement.setDate(5, java.sql.Date.valueOf(dob));
		pStatement.setString(6, maritialStatus.toUpperCase());
		pStatement.setInt(7, branchID);
		pStatement.setString(8, address);
		pStatement.setString(9, state);
		pStatement.setString(10, city);
		pStatement.setString(11, zip);
		pStatement.setInt(12, jobID);
		pStatement.execute();

	}

	public static int getBranchID(Connection dbConnection, String branchName)throws SQLException {
		String branchIDSql = "SELECT BRANCHID FROM BRANCH WHERE BRANCHNAME = ?";
		PreparedStatement getBranchId = dbConnection.prepareStatement(branchIDSql);
		getBranchId.setString(1, branchName);
		ResultSet b = getBranchId.executeQuery();
		int branchID = -1;
		while (b.next()) {
	 		branchID = b.getInt("BranchID");

		}
		return branchID;
	}
	public static int getJobID (Connection dbConnection, String jobDesc) throws SQLException{
		String jobIDSql = "SELECT JOBID FROM JOB WHERE JOBDESC = ?";
		PreparedStatement getJobId = dbConnection.prepareStatement(jobIDSql);
		getJobId.setString(1, jobDesc);
		ResultSet j = getJobId.executeQuery();
		int jobID = -1;
		while (j.next()) {
	 		jobID = j.getInt("JOBID");

		}
		return jobID;
	}
	public static boolean verifyMemberID(Connection dbConnection, String text) throws SQLException {
		String sql = "select * from members where memberID = ?";
		PreparedStatement pStatement = dbConnection.prepareStatement(sql);
		pStatement.setString(1, text);
		ResultSet rs = pStatement.executeQuery();
		if (rs.next()) {
			return true;
		} else {
			return false;
		}

	}

	public static boolean verifyBranchID(Connection dbConnection, String text) throws SQLException {
		String sql = "select * from branch where branchID = ?";
		PreparedStatement pStatement = dbConnection.prepareStatement(sql);
		pStatement.setString(1, text);
		ResultSet rs = pStatement.executeQuery();
		if (rs.next()) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean verifyJobID(Connection dbConnection, String text) throws SQLException {
		String sql = "select * from job where jobID = ?";
		PreparedStatement pStatement = dbConnection.prepareStatement(sql);
		pStatement.setString(1, text);
		ResultSet rs = pStatement.executeQuery();
		if (rs.next()) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean applyChanges(Connection dbConnection, String memberID, String fName, String lName,
			String dateJoined, String number, String dob, String maritialStatus, String address, String city,
			String state, String zip, int jobID) throws SQLException {

		dbConnection.setAutoCommit(false);
		//if none of the fields were filled out
		boolean empty = true;
		try {

			if (!fName.equals("")) {
				empty = false;
				String sql = "update members  set fName = ? where memberID = ?";
				PreparedStatement pStatement = dbConnection.prepareStatement(sql);
				pStatement.setString(1, fName);
				pStatement.setString(2, memberID);
				pStatement.execute();
			}
			if (!lName.equals("")) {
				empty = false;
				String sql = "update members  set lName = ? where memberID = ?";
				PreparedStatement pStatement = dbConnection.prepareStatement(sql);
				pStatement.setString(1, lName);
				pStatement.setString(2, memberID);
				pStatement.execute();
			}
			if (!dateJoined.equals("")) {
				empty = false;
				String sql = "update members set dateJoined = ? where memberID = ? ";
				PreparedStatement pStatement = dbConnection.prepareStatement(sql);
				pStatement.setDate(1, java.sql.Date.valueOf(dateJoined));
				pStatement.setString(2, memberID);
				pStatement.execute();
			}
			if (!number.equals("")) {
				empty = false;
				String sql = "update members  set phoneNumber = ? where memberID = ?";
				PreparedStatement pStatement = dbConnection.prepareStatement(sql);
				pStatement.setString(1, number);
				pStatement.setString(2, memberID);
				pStatement.execute();
			}
			if (!dob.equals("")) {
				empty = false;
				String sql = "update members set dob = ? where memberID = ? ";
				PreparedStatement pStatement = dbConnection.prepareStatement(sql);
				pStatement.setDate(1, java.sql.Date.valueOf(dob));
				pStatement.setString(2, memberID);
				pStatement.execute();
			}
			if (!maritialStatus.equals("")) {
				empty = false;
				String sql = "update members set maritialStatus = ? where memberID = ? ";
				PreparedStatement pStatement = dbConnection.prepareStatement(sql);
				pStatement.setString(1, maritialStatus);
				pStatement.setString(2, memberID);
				pStatement.execute();
			}
			if (!address.equals("")) {
				empty = false;
				String sql = "update members set address = ? where memberID = ? ";
				PreparedStatement pStatement = dbConnection.prepareStatement(sql);
				pStatement.setString(1, address);
				pStatement.setString(2, memberID);
				pStatement.execute();
			}
			if (!city.equals("")) {
				empty = false;
				String sql = "update members set city = ? where memberID = ? ";
				PreparedStatement pStatement = dbConnection.prepareStatement(sql);
				pStatement.setString(1, city);
				pStatement.setString(2, memberID);
				pStatement.execute();
			}
			if (!state.equals("")) {
				empty = false;
				String sql = "update members  set state = ? where memberID = ?";
				PreparedStatement pStatement = dbConnection.prepareStatement(sql);
				pStatement.setString(1, state);
				pStatement.setString(2, memberID);
				pStatement.execute();
			}
			if (!zip.equals("")) {
				empty = false;
				String sql = "update members set zip = ? where memberID = ? ";
				PreparedStatement pStatement = dbConnection.prepareStatement(sql);
				pStatement.setString(1, zip);
				pStatement.setString(2, memberID);
				pStatement.execute();
			}
			if (jobID!=-1) {
				empty = false;
				String sql = "update members set jobID = ? where memberID = ? ";
				PreparedStatement pStatement = dbConnection.prepareStatement(sql);
				pStatement.setInt(1, Integer.valueOf(jobID));
				pStatement.setString(2, memberID);
				pStatement.execute();
			}
			dbConnection.commit();
			return empty;
		} catch (SQLException sqle) {
			dbConnection.rollback();
			throw sqle;
		} catch (IllegalArgumentException ex) {
			dbConnection.rollback();
			throw ex;
		}

	}

	public static ArrayList<String> getBranchNames(Connection dbConnection) throws SQLException{
		ArrayList<String> names = new ArrayList<String>();
		String sql = "select branchName from branch";
	
			PreparedStatement pStatement = dbConnection.prepareStatement(sql);
			ResultSet rs = pStatement.executeQuery();
			while(rs.next()) {
				names.add(rs.getString("branchName"));
			}
			return names;
		
	}

	public static ArrayList<String> getJobNames(Connection dbConnection) throws SQLException{
		ArrayList<String> names = new ArrayList<String>();
		String sql = "select jobDesc from job";
	
			PreparedStatement pStatement = dbConnection.prepareStatement(sql);
			ResultSet rs = pStatement.executeQuery();
			while(rs.next()) {
				names.add(rs.getString("jobDesc"));
			}
			return names;
	}

}
