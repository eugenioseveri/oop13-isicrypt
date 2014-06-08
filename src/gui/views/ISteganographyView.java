package gui.views;

import gui.controllers.ISteganographyViewObserver;

import java.awt.Component;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;

/**
 * Interface for SteganographyView
 * @author Filippo Vimini
 */
public interface ISteganographyView {
	/**
	 * link the controller to the view
	 * @param controller		controller to be linked
	 */
	void attackSteganographyViewObserver(ISteganographyViewObserver controller);
	/**
	 * set a dialog for show the errors at the users
	 * 
	 * @param error		error to shown
	 */
	void optionPane(Object error);

	JLabel getIconLabel();

	Component getInsertTextButton();

	Component getClearSettingButton();

	Component getFindTextButton();

	JTextArea getTextArea();

	Component getStartButton();

	Component getSelectTextButton();

	Component getSelectImageButton();

	JFrame getDialog();
}