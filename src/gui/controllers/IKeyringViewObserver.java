package gui.controllers;

/**
 * Interface for the keyring function controller.
 * @author Eugenio Severi
 */
public interface IKeyringViewObserver {
	
	/**
	 * Adds a new item to the model controller and to the view.
	 */
	void command_addButton();
	
	/**
	 * Modifies the selected row of the table and updates the model.
	 */
	void command_modifyButton();
	
	/**
	 * Deletes the selected row of the table and updates the model.
	 */
	void command_cancelButton();
	
	/**
	 * Generates a new AES key, if not already set.
	 * If a key has already been set, asks the user for another key to load.
	 */
	void command_encryptButton();
	
	/**
	 * Asks the controller to save to file the current records.
	 */
	void command_saveButton();
}
