package gui.models;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidKeyException;

public interface IKeyringModel {
	
	/**
	 * Saves current HashSet to an output stream, crypted with AES.
	 * @param outputFile The output stream where you want to save the Set
	 * @param aesKey The AES key used to encrypt the data.
	 * @throws IOException If an error occurs while saving the data
	 * @throws InvalidKeyException If the previously set AES key is not valid
	 */
	void saveData(OutputStream outputFile, byte[] aesKey) throws IOException, InvalidKeyException;
	
	/**
	 * Loads a HashSet from the specified AES encrypted stream. 
	 * @param inputFile The input stream containing a previously saved Set
	 * @param aesKey The AES key used to decrypt the data.
	 * @throws IOException If an error occurs while reading/writing a stream
	 * @throws InvalidKeyException If the key size is not valid for AES
	 * @throws ClassNotFoundException If an error occurs while reading the Set object
	 */
	void loadData(InputStream inputFile, byte[] aesKey) throws IOException, InvalidKeyException, ClassNotFoundException;
	
	/**
	 * Adds an item into the Set.
	 * @param host
	 * @param username
	 * @param password
	 */
	void addItem(String host, String username, String password);
	
	/**
	 * Removes an item from the Set.
	 * @param host
	 * @param username
	 * @param password
	 */
	void removeItem(String host, String username, String password);
	
	/**
	 * Iterates the elements of the Set.
	 * @return The current element
	 */
	java.util.Iterator<Triple<String, String, String>> iterator();
	
	boolean contains(String host, String username, String password);
}
