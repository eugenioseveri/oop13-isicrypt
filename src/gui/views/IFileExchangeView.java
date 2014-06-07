package gui.views;

import java.awt.Component;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.TableModel;

import gui.controllers.IFileExchangeViewObserver;

public interface IFileExchangeView {
	//View Observer attacher
	void attachFileExchangeViewObserve(IFileExchangeViewObserver controller);
	JTextArea getVisualtextarea();
	JTextArea getChattextarea();
	JTable getContactTable();
	Component getScrollpanetable();
	JScrollPane getScrollpanevisual();
	JButton getFilebutton();
	JButton getStegabutton();
	JButton getZipbutton();
	JFrame getFrame();
	JButton getDeleteContactButton();
	JButton getChangecontactbutton();
	JButton getAddcontactbutton();
	JButton getSendbutton();
	void setSendprogress(int value);
	void optionPanel(Object error);
	String setOptionPane(String name, String text);
	TableModel getModel();
}