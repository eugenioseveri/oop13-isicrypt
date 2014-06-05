package gui.controllers;

/**
 * @author Filippo Vimini
 *Created 26/05/2014
 */
public interface IFileExchangeViewObserver {
	void selectFile();
	void stegaImage();
	void selectCompressedFile();
	void selectContact();
	void addContact();
	void deleteContact();
	void changeContact();
	void sendText();
}
