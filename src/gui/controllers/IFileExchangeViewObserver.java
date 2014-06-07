package gui.controllers;

import javax.swing.table.TableModel;

/**
 * @author Filippo Vimini
 *Created 26/05/2014
 */
public interface IFileExchangeViewObserver {
	/**
	 * Send a generic File selected by user, through the  client to the server
	 */
	void selectFile();
	/**
	 * Hide a selected text on the selected image and send the image with hidden text, through the  client to the server
	 */
	void stegaImage();
	/**
	 * Send a compressed file with Gzip, through the  client to the server, then the server will decompress and save the file
	 */
	void selectCompressedFile();
	/**
	 * Select contact from JTable when click over the respective cell
	 */
	void selectContact();
	/**
	 * Add contact in ContactList, save the list on File, then update the Table for show new contact
	 */
	void addContact();
	/**
	 * delete contact in ContactList, save the list on File, then update the Table for hide the contact
	 */
	void deleteContact();
	/**
	 * Change current contact, closing the current connection and repaint the JTable
	 */
	void changeContact();
	/**
	 * Send to the server the text written on TextArea
	 */
	void sendText();
	/**
	 * Creae a TableModel with two columns that represents the host and the name of the reachable server.
	 * Set the cell of the table no editable and load from File the list of contact.
	 * 
	 * @return TableModel
	 */
	TableModel tableBuilder();
	/**
	 * Set JButton enable or disabled, created for better class formatting.
	 * 
	 * @param state		boolean that set the button enable or disable.
	 */
	void setEnableButton(boolean state);
	/**
	 * close the server connection, used like bridge for view from model
	 */
	void closeThread();
	
	void setProgressbar(int value);
	/**
	 * Append  text on JTextArea, with the formatting of text for the Client.
	 * 
	 * @param text		text that will be append.
	 */
	void textApendClient(String text);
	/**
	 *Append  text on JTextArea, with the formatting of text for the server.
	 * 
	 * @param text		text that will be append.
	 * @param name		name of the client.
	 */
	void textAppendServer(String text, String name);
	/**
	 *Append  text on JTextArea, with the formatting for the server.
	 *
	 * @param text		name of file.
	 * @param name		name of client.
	 * @return boolean
	 */
	boolean fileAppendServer(String text, String name);
}
