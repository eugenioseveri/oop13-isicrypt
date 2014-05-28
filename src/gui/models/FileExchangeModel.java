package gui.models;

import java.util.HashMap;

public class FileExchangeModel {
	//Static nei campi significa che non dipendono dallo stato della classe, infatti non vengono mai toccati dal costruttore
	//Serve come controllo che client e server parlino fra loro
	private static ContactInfo contactTemp;
	private static HashMap<String, String> contactList = new HashMap<String, String>();
	
	public static HashMap<String, String> getContactList(){
		return FileExchangeModel.contactList;
	}
	public static void setContactList(String host, String name){
		contactList.put(host, name);
	}
	public static ContactInfo getContactInfo(){
		return FileExchangeModel.contactTemp;
	}
	public static void setContactInfo(ContactInfo setContact){
		FileExchangeModel.contactTemp = setContact;
	}
}
