package tracking;

import java.util.ArrayList;
import java.util.List;

public class Person {

	
	private String firstname; 
	private String lastname; 
	private String mail; 
	private String googleID; 
	private String token;
	
	private List<GPS> gpsData = new ArrayList<GPS>();
	private List<Light>lightData = new ArrayList<Light>(); 
	private List<Accelerator>acceleratorData = new ArrayList<Accelerator>(); 
	
	
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public String getGoogleID() {
		return googleID;
	}
	public void setGoogleID(String googleID) {
		this.googleID = googleID;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	} 
	
	
	public void addGPS(GPS gps) {
		gpsData.add(gps); 
	}
	
	
	public void addLight(Light light) {
		lightData.add(light); 
	}
	
	public void addAccelerator(Accelerator accelerator) {
		acceleratorData.add(accelerator); 
	}
	
	
	public void print() {
		
	}
	
	
}
