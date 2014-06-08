package gui.controllers;

/**
 * Interface for StartScreenController
 * @author Filippo Vimini
 */
public interface IStartScreenViewObserver {
	/**
	 * link controller to view and start Cryptography class
	 */
	void selectCryptography();
	/**
	 * link controller to view and start Steganography class
	 */
	void selectSteganography();
	/**
	 * link controller to view and model then start Keyring class
	 */
	void selectKeyring();
	/**
	 * link controller to view and model then start FileExcahnge class
	 */
	void selectFileExchange();
	/**
	 * Select theme form dialog
	 */
	void selectTheme();
}