package gui.models;

public class ContactInfo {
	String name = null;
	String host = null; 
	public ContactInfo(String name, String host){
		this.name = name;
		this.host = host;
	}
	
	public String getName(){
		return this.name;
	}
	public String getHost(){
		return this.host;
	}
	public void setName(String name){
		this.name = name;
	}
	public void setHost(String host){
		this.host = host;
	}
}
