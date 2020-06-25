package Hatzalah.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javafx.util.Callback;

public class StatisticsIO {

	public static String getNumMembers(Connection dbConnection, String branchName) throws SQLException {
		Integer count = 0;
		String sql = "select count(memberID) as num from members inner join branch on members.BranchID = branch.branchID where branchName = ? ";
		PreparedStatement pStatement = dbConnection.prepareStatement(sql);
		pStatement.setString(1, branchName);
		ResultSet rs = pStatement.executeQuery();
		while (rs.next()) {
			count = rs.getInt("num");
		}
		return count.toString();

	}

	public static String getNumBusses(Connection dbConnection, String branchName) throws SQLException {
		Integer count = 0;
		String sql = "select count(bus.branchID) as num from bus inner join branch on bus.BranchID = branch.branchID where branchName =  ? ";
		PreparedStatement pStatement = dbConnection.prepareStatement(sql);
		pStatement.setString(1, branchName);
		ResultSet rs = pStatement.executeQuery();
		while (rs.next()) {
			count = rs.getInt("num");
		}
		return count.toString();
	}

	public static String getAvgCallsPerYear(Connection dbConnection, String branchName) throws SQLException {
		Integer count = 0;
		String sql = "select avg(nums) as average from (\r\n"
				+ "select  year(timereceived) as year, count(callID) as nums from calls\r\n"
				+ "inner join branch on calls.BranchID = branch.branchID\r\n" + "where branchName = ?\r\n"
				+ "group by year(timereceived)\r\n" + ") as groupedyears ";
		PreparedStatement pStatement = dbConnection.prepareStatement(sql);
		pStatement.setString(1, branchName);
		ResultSet rs = pStatement.executeQuery();
		while (rs.next()) {
			count = rs.getInt("average");
		}
		return count.toString();
	}

	public static String getNumDonors(Connection dbConnection, String branchName) throws SQLException {
		Integer count = 0;
		String sql = "select  count (distinct donorID) as total from donations inner join branch on donations.branchid = branch.branchid where branchName = ?";
		PreparedStatement pStatement = dbConnection.prepareStatement(sql);
		pStatement.setString(1, branchName);
		ResultSet rs = pStatement.executeQuery();
		while (rs.next()) {
			count = rs.getInt("total");
		}
		return count.toString();
	}

	public static String getTotalDonations(Connection dbConnection, String branchName) throws SQLException {

		Integer count = 0;
		String sql = "select sum(donationAmnt) as total from donations inner join branch on donations.branchid = branch.branchid where branchName = ?";
		PreparedStatement pStatement = dbConnection.prepareStatement(sql);
		pStatement.setString(1, branchName);
		ResultSet rs = pStatement.executeQuery();
		while (rs.next()) {
			count = rs.getInt("total");
		}
		return count.toString();
	}

	public static String getNumCoordinators(Connection dbConnection, String branchName) throws SQLException {

		Integer count = 0;
		String sql = "select count (memberID) as amt from members inner join branch on members.branchId = branch.branchId where jobID = 400 and branchName = ?";
		PreparedStatement pStatement = dbConnection.prepareStatement(sql);
		pStatement.setString(1, branchName);
		ResultSet rs = pStatement.executeQuery();
		while (rs.next()) {
			count = rs.getInt("amt");
		}
		return count.toString();
	}
	public static String getNumDispatchers(Connection dbConnection, String branchName) throws SQLException {

		Integer count = 0;
		String sql = "select count (memberID) as amt from members inner join branch on members.branchId = branch.branchId where jobID = 401 and branchName = ?";
		PreparedStatement pStatement = dbConnection.prepareStatement(sql);
		pStatement.setString(1, branchName);
		ResultSet rs = pStatement.executeQuery();
		while (rs.next()) {
			count = rs.getInt("amt");
		}
		return count.toString();
	}
	public static String getNumResponders(Connection dbConnection, String branchName) throws SQLException {

		Integer count = 0;
		String sql = "select count (memberID) as amt from members inner join branch on members.branchId = branch.branchId where jobID = 402 and branchName = ?";
		PreparedStatement pStatement = dbConnection.prepareStatement(sql);
		pStatement.setString(1, branchName);
		ResultSet rs = pStatement.executeQuery();
		while (rs.next()) {
			count = rs.getInt("amt");
		}
		return count.toString();
	}

	public static ArrayList<String>  getPurchaseYearOptions(Connection dbConnection, String branch)throws SQLException {
		ArrayList<String> years = new ArrayList<String>();
		int branchId = MemberIO.getBranchID(dbConnection, branch);
		String supplyYears = "select distinct year(datePurchased) as years from MEDICALsuppliesPURCHASE inner join branch on  medicalsuppliespurchase.branchID = branch.branchID where medicalSuppliesPurchase.branchID = ? ";
		String busYears = "select distinct year(datePurchased) as years from bus inner join branch on  bus.branchID = branch.branchID where bus.branchID = ?";
		String equipmentYears = "select distinct year(datePurchased) as years from MEDICALEQUIPMENTPURCHASE inner join branch on  MEDICALEQUIPMENTPURCHASE.branchID = branch.branchID where MEDICALEQUIPMENTPURCHASE.branchID = ?";
			PreparedStatement supply = dbConnection.prepareStatement(supplyYears);
			supply.setInt(1,  branchId);
			ResultSet supplySet = supply.executeQuery();
			while(supplySet.next()) {
				years.add(supplySet.getString("years"));
			}
			PreparedStatement bus = dbConnection.prepareStatement(busYears);
			bus.setInt(1, branchId);
			ResultSet busSet = bus.executeQuery();
			while (busSet.next()) {
				String year = busSet.getString("years");
				if (!years.contains(year)) {
					years.add(year);
				}
			}
			PreparedStatement equipment = dbConnection.prepareStatement(equipmentYears);
			equipment.setInt(1, branchId);
			ResultSet equipmentSet = equipment.executeQuery();
			while (equipmentSet.next()) {
				String year = equipmentSet.getString("years");
				if (!years.contains(year)) {
					years.add(year);
				}
			}
			
			return years;
	}

	public static String getTotalPurchases(Connection dbConnection, String branch) throws SQLException {
		Double count = 0.0;
		String busTotal = "select sum(purchasePrice) as total from bus inner join branch on bus.branchid = branch.branchid where branchName = ?";
		PreparedStatement pStatement = dbConnection.prepareStatement(busTotal);
		pStatement.setString(1, branch);
		ResultSet rs = pStatement.executeQuery();
		while (rs.next()) {
			count += rs.getDouble("total");
		}
		String suppliesTotal ="select sum(purchasePrice *qtyPurchased) as total from medicalSuppliespurchase inner join branch on medicalSuppliespurchase.branchid = branch.branchid where branchName = ?";
		PreparedStatement pStatement2 = dbConnection.prepareStatement(suppliesTotal);
		pStatement2.setString(1, branch);
		ResultSet rs2 = pStatement2.executeQuery();
		while (rs2.next()) {
			count += rs2.getDouble("total");
		}
		String equipmentTotal ="select sum(purchasePrice) as total from medicalequipmentpurchase inner join branch on medicalequipmentpurchase.branchid = branch.branchid where branchName = ?";
		PreparedStatement pStatement3 = dbConnection.prepareStatement(equipmentTotal);
		pStatement3.setString(1, branch);
		ResultSet rs3 = pStatement3.executeQuery();
		while (rs3.next()) {
			count += rs3.getDouble("total");
		}
		return count.toString();
	}

	public static String getPurchasesForYear(Connection dbConnection, String year, String branch) throws SQLException {
		int branchId = MemberIO.getBranchID(dbConnection, branch);
		Double count = 0.00;
		String busTotal = "	select sum(purchasePrice) as total from bus where branchId = ? and year(datePurchased) = ?";
		PreparedStatement pStatement = dbConnection.prepareStatement(busTotal);
		pStatement.setInt(1, branchId);
		pStatement.setString(2, year);
		ResultSet rs = pStatement.executeQuery();
		while (rs.next()) {
			count += rs.getDouble("total");
		}
		String equipmentTotal ="select sum(purchasePrice) as total from medicalequipmentpurchase where branchId = ? and year(datePurchased) = ?";
		PreparedStatement pStatement2 = dbConnection.prepareStatement(equipmentTotal);
		pStatement2.setInt(1, branchId);
		pStatement2.setString(2, year);
		ResultSet rs2 = pStatement2.executeQuery();
		while (rs2.next()) {
			count += rs2.getDouble("total");
		}
		String supplyTotal ="select sum(purchasePrice) as total from MEDICALSUPPLIESPURCHASE where branchId = ? and year(datePurchased) = ?";
		PreparedStatement pStatement3 = dbConnection.prepareStatement(supplyTotal);
		pStatement3.setInt(1, branchId);
		pStatement3.setString(2,year);
		ResultSet rs3 = pStatement3.executeQuery();
		while (rs3.next()) {
			count += rs3.getDouble("total");
		}
		return count.toString();
	}

	public static ArrayList<String> getCallYearOptions(Connection dbConnection, String branch) throws SQLException {
		ArrayList<String> years = new ArrayList<String>();
		int branchId = MemberIO.getBranchID(dbConnection, branch);
		String sql = "select distinct year(timeReceived) as years from calls where branchID = ?";
			PreparedStatement ps = dbConnection.prepareStatement(sql);
			ps.setInt(1,  branchId);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				years.add(rs.getString("years"));
			}
						
			
			return years;
	}

	public static String getCallsForYear(Connection dbConnection, String year, String branch) throws SQLException{
		int branchId = MemberIO.getBranchID(dbConnection, branch);
		Integer count = 0;
		String sql = " select count(callID) as num from calls where branchID = ? and year(timeReceived) =?";
		PreparedStatement pStatement = dbConnection.prepareStatement(sql);
		pStatement.setInt(1, branchId);
		pStatement.setString(2, year);
		ResultSet rs = pStatement.executeQuery();
		while (rs.next()) {
			count = rs.getInt("num");
		}
		return count.toString();
	}

}
