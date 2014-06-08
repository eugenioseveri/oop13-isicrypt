package gui.models;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

/**
 * Model for file exchange function. Loads and stores contacts
 * @author Filippo Vimini
 */
public class FileExchangeModel implements IFileExchangeModel {
	private  ContactInfo contactTemp;
	private  HashMap<String, String> contactList = new HashMap<String, String>();
	
	@Override
	public HashMap<String, String> getContactList(){
		return contactList;
	}
	
	@Override
	public void setContactList(final String host, final String name){
		contactList.put(host, name);
	}
	
	@Override
	public ContactInfo getContactInfo(){
		return contactTemp;
	}
	
	@Override
	public void setContactInfo(final ContactInfo setContact){
		contactTemp = setContact;
	}
	
	@Override
	public void saveContacts(final File path) throws FileNotFoundException, IOException{
		final ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path));
		oos.writeObject(contactList);
		oos.close();
	}
	@Override
	@SuppressWarnings("unchecked")
	public void loadContacts(final File path) throws FileNotFoundException, IOException, ClassNotFoundException{
		final ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path));
		contactList = (HashMap<String, String>)ois.readObject();
		ois.close();
		
	}
}
