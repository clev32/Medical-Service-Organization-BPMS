package Hatzalah.business;

import java.time.LocalDate;

public class Branch {

	private int ID;
	private String name;
	private String yearEstablished;
	
	public Branch(int iD, String name, String yearEstablished) {
		this.ID = iD;
		this.name = name;
		this.yearEstablished = yearEstablished;
	}

	public int getID() {
		return ID;
	}

	public String getName() {
		return name;
	}

	public String getYearEstablished() {
		return yearEstablished;
	}
	
	@Override
	public String toString() {
		return "Branch [ID=" + ID + ", name=" + name + ", yearEstablished=" + yearEstablished + "]";
	}


}
