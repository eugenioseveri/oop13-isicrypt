package gui.views;

import javax.swing.JTable;

import gui.controllers.IKeyringViewObserver;

/**
 * Interface for KeyringView
 * @author Eugenio Severi
 */
public interface IKeyringView {
	void attachViewObserver(IKeyringViewObserver listener);
	JTable getTable();
	void showMessageDialog(String message);
	boolean showYesNoOptionPane(String message);
	String showInputDialog(String title, String editableText);
}
