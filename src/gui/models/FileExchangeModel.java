package gui.models;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
	public static void saveContacts(File path) throws FileNotFoundException, IOException{
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path));
		oos.writeObject(contactList);
		oos.close();
	}
	@SuppressWarnings("unchecked")
	public static void loadContacts(File path) throws FileNotFoundException, IOException, ClassNotFoundException{
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path));
		contactList = (HashMap<String, String>)ois.readObject();
		ois.close();
		
	}
}
