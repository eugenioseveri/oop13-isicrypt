package gui.models;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

public class FileExchangeModel implements IFileExchangeModel {
	private  ContactInfo contactTemp;
	private  HashMap<String, String> contactList = new HashMap<String, String>();
	
	@Override
	public HashMap<String, String> getContactList(){
		return contactList;
	}
	
	@Override
	public void setContactList(String host, String name){
		contactList.put(host, name);
	}
	
	@Override
	public ContactInfo getContactInfo(){
		return contactTemp;
	}
	
	@Override
	public void setContactInfo(ContactInfo setContact){
		contactTemp = setContact;
	}
	
	@Override
	public void saveContacts(File path) throws FileNotFoundException, IOException{
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path));
		oos.writeObject(contactList);
		oos.close();
	}
	@Override
	@SuppressWarnings("unchecked")
	public void loadContacts(File path) throws FileNotFoundException, IOException, ClassNotFoundException{
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path));
		contactList = (HashMap<String, String>)ois.readObject();
		ois.close();
		
	}
}
