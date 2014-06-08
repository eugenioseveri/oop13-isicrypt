package gui.models;
/**
 * 
 * @author Filippo
 *Created 10/05/2014
 *
 *Class that store info about contact for client/server
 */
public class ContactInfo {
	private String name;
	private String host; 
	
	public ContactInfo(final String name, final String host){
		this.name = name;
		this.host = host;
	}
	
	public String getName(){
		return this.name;
	}
	
	public String getHost(){
		return this.host;
	}
	
	public void setName( final String name){
		this.name = name;
	}
	
	public void setHost(final String host){
		this.host = host;
	}
}
