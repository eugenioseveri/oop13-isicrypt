package gui.models;
/**
 * 
 * @author Filippo
 *Created 10/05/2014
 *
 *Class that store info about contact for client/server
 */
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
