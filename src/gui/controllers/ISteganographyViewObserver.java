package gui.controllers;

import gui.views.ISteganographyView;
/**
 * 
 * @author Filippo
 *Inteface for SteganographyController
 */
public interface ISteganographyViewObserver {
	/**
	 * Select the image and check if even the text is selected, 
	 * the show the start button for doing the merge
	 */
	void selectImage();
	/**
	 * Select the image and check if even the text is selected,
	 *  the show the start button for doing the merge
	 */
	void selectText();
	/**
	 * when both image and text are selected,
	 *  the start button launch the Steganography,
	 *  then set disable "insert text"
	 * button for not allow concurrency
	 */
	void start();
	/**
	 * Select an image and find hidden text if there is
	 */
	void findText();
	/**
	 * reset all settings
	 */
	void clearSetting();
	/**
	 * take text from TextArea and use that for Steganography, 
	 * then set disable "select text"
	 * button for not allow concurrency
	 */
	void insertText();
	/**
	 * link view to the controller
	 * 
	 * @param view		view to be lined
	 */
	void setView(ISteganographyView view);
}
