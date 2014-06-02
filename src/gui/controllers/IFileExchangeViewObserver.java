package gui.controllers;

/**
 * @author Filippo Vimini
 *Created 26/05/2014
 */
public interface IFileExchangeViewObserver {
	void selectFile();
	void stegaImage();
	void selectCompress();
	void selectContact();
	void addContact();
	void closeConnection();
	void changeContact();
	void sendText();
	void showStart();
}
