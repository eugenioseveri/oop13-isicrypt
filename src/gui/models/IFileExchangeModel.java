package gui.models;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
/**
 * 
 * @author Filippo
 *Model of pattern MVC that contain the data of server that the client will contact
 */
public interface IFileExchangeModel {
	/**
	 * Return the hashmap that contain a String for name and host of the server
	 * 
	 * @return HashMap
	 */
	HashMap<String, String> getContactList();
	/**
	 * Put a new String for name and host on the HashMap
	 * 
	 * @param host		String that contain the ip of the server
	 * @param name		String that contain the name of the server
	 */
	void setContactList(String host, String name);
	/**
	 * Return a ContactInfo that represent the current contact for server
	 * 
	 * @return ContactInfo
	 */
	ContactInfo getContactInfo();
	/**
	 * Set a ContactInfo for represent the cu+rrent connection with the server
	 * 
	 * @param setContact
	 */
	void setContactInfo(ContactInfo setContact);
	/**
	 * Save te current HashMap in a file on disk
	 * @param path						path of the file on disk
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	void saveContacts(File path) throws FileNotFoundException, IOException;
	/**
	 * Load the HashMap from disk
	 * 
	 * @param path						path of the file on disk
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	void loadContacts(File path) throws FileNotFoundException, IOException,ClassNotFoundException;

}