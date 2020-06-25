package Hatzalah.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Hatzalah.business.Branch;

public class CallIO {

	public static ArrayList<String> getMedicalEquipment(Connection dbConnection) throws SQLException{
		try {
			
			ArrayList<String> equipment = new ArrayList<String>();
			
			Statement statement = dbConnection.createStatement();
			ResultSet rs = statement.executeQuery("select medicalEquipmentDesc from MEDICALEQUIPMENT");
			while (rs.next())
			{
				equipment.add(rs.getString("medicalEquipmentDesc"));
			}
			
			rs.close();
			statement.close();
			return equipment;
			}
			catch(SQLException sqlE)
			{
				throw sqlE;
			}
		}
	
	public static void insertEquipment(Connection dbConnection, ArrayList<String> equipment, int callID) throws SQLException {
		int equipID = 0;
		try {
		   dbConnection.setAutoCommit(false);
		  
		for(String desc: equipment) {
			
				Statement statement = dbConnection.createStatement();
				ResultSet rs = statement.executeQuery("select medicalEquipmentID from MEDICALEQUIPMENT where medicalEquipmentDesc =" + "'" + desc + "'");
				
				if(rs.next()) {
					equipID = rs.getInt("medicalEquipmentID");
				}
				
				 PreparedStatement pStatement =  dbConnection.prepareStatement("Insert into calls_medicalEquipment (CallID, medicalEquipmentID) values(?,?)");

				pStatement.setInt(1, callID);
				pStatement.setInt(2, equipID);
				pStatement.execute();
			} 


		}
		catch (SQLException sqle) {
			dbConnection.rollback();
			throw sqle;
		}
	}
	
	public static ArrayList<String> getMemberIDs(Connection dbConnection) throws SQLException{
		
		ArrayList<String> ids = new ArrayList<String>();
		try {
		Statement statement = dbConnection.createStatement();
		ResultSet rs = statement.executeQuery("select memberid from members");
		while (rs.next())
		{
			ids.add(rs.getString("memberid"));
		}
		
		rs.close();
		statement.close();
		return ids;
		}
		
		catch(SQLException sqlE)
		{
			dbConnection.rollback();
			throw sqlE;
		}
	}
	
	public static void addMembertoCall(Connection dbConnection, int callID, String memberID) throws SQLException {
		try {
		dbConnection.setAutoCommit(false);
		String query = "insert into CALLS_MEMBERS (callid, memberid) values (?,?)";
		 PreparedStatement pStatement =  dbConnection.prepareStatement(query);
		 pStatement.setInt(1, callID);
		 pStatement.setString(2, memberID);
		 pStatement.execute();
		 pStatement.close();
			
		}
		catch(SQLException sqle) {
			dbConnection.rollback();
			throw sqle;
		}
		
		}
	
	public static void addCall(Connection dbConnection, int age, String address, String city, String state,
			 String bus, String fname, String lname, int branchId) throws SQLException{
		
			String sql = "insert into calls(CallerAddress, CallerState, CallerCity, CallerAge, VIN, callerfname, callerlname,branchId)"
					+ "values(?, ?, ?, ?, ?, ?,?, ?) ";
			
			PreparedStatement pStatement = dbConnection.prepareStatement(sql);
			pStatement.setString(1, address);
			pStatement.setString(2, state);
			pStatement.setString(3, city);
			pStatement.setInt(4, age);
			pStatement.setString(5, bus);
			pStatement.setString(6, fname);
			pStatement.setString(7, lname);
			pStatement.setInt(8, branchId);
			
			
			pStatement.executeUpdate();
			
		
	}

	public static List<String> getResponders(Connection dbConnection) {
		String sql = "select memberID from members";
		Statement statement;
		List<String> responders = new ArrayList<>();
		try {
			statement = dbConnection.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			while (rs.next()) {
				responders.add(rs.getString("memberID"));
			}
			rs.close();
			statement.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return responders;

	}
	public static List<String> getSymptoms(Connection dbConnection) {
		String sql = "select symptomsDesc from symptoms";
		Statement statement;
		List<String> symptoms = new ArrayList<>();
		try {
			statement = dbConnection.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			while (rs.next()) {
				symptoms.add(rs.getString("symptomsDesc"));
			}
			rs.close();
			statement.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return symptoms;

	}
	public static List<String> getBranches(Connection dbConnection) {
		String sql = "select branchId from branch";
		Statement statement;
		List<String> branches = new ArrayList<>();
		try {
			statement = dbConnection.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			while (rs.next()) {
				branches.add(rs.getString("branchId"));
			}
			rs.close();
			statement.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return branches;

	}

	public static List<String> getBuses(Connection dbConnection) {
		String sql = "select vin from bus";
		Statement statement;
		List<String> buses = new ArrayList<>();
		try {
			statement = dbConnection.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			while (rs.next()) {
				buses.add(rs.getString("vin"));
			}
			rs.close();
			statement.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return buses;
	}

	public static int getCallId(Connection dbConnection, String fname, String lname, int age) throws SQLException {

		String sql = "select callID from calls where callerfname = ? and callerlname = ? and callerAge = ?";
		PreparedStatement pStatement = dbConnection.prepareStatement(sql);
		pStatement.setString(1, fname);
		pStatement.setString(2, lname);
		pStatement.setInt(3, age);
		ResultSet rs = pStatement.executeQuery();
		rs.next();
		int callId = rs.getInt("callId");
		return callId;
	}

	public static void addSymptoms(Connection dbConnection, String symptom, int callId)throws SQLException {
		int symptomId = 0;
		Statement st = dbConnection.createStatement();
		ResultSet rs = st.executeQuery("select symptomsID from symptoms where symptomsdesc = '"+ symptom + "'");
		if(rs.next()) {
			symptomId = rs.getInt("SymptomsId");
		}
		String sql = " insert into calls_symptoms values (?,?)";
		PreparedStatement ps = dbConnection.prepareStatement(sql);
		ps.setInt(2, symptomId);
		ps.setInt(1, callId);
		ps.execute();
		ps.close();
	}
	
	public static void addSymptom(Connection dbConnection, String symptom) throws SQLException {
		String sql = " insert into symptoms values (?)";
		PreparedStatement pStatement = dbConnection.prepareStatement(sql);
		pStatement.setString(1, symptom);
		pStatement.execute();
		pStatement.close();
	}
		
		
	}
