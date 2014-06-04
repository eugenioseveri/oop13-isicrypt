package gui.controllers;

import static algorithms.ErrorMessages.*;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.InvalidKeyException;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import algorithms.AES;
import gui.models.IKeyringModel;
import gui.models.KeyringModel;
import gui.models.OpenButtons;
import gui.models.Triple;
import gui.models.OpenButtons.FileTypes;
import gui.views.IKeyringView;
import gui.views.KeyringView;
import gui.views.StartScreenView;

public class KeyringController implements IKeyringViewObserver, IGeneralViewObserver {

	private final static String ROW_NOT_SELECTED = "You must select a row first!";
	private final static String DELETE_RECORD_CHECK = "Do you really want to delete this entry?";
	private final static String SAVE_RECORDS_CHECK = "Do you really want to save?";
	private final static String KEY_SUCCESSFULLY_SET_MESSAGE = "A key is now set.";
	private final static String KEY_NOT_SET_WARNING = "A key has not been set. Saving is not possible until you set a key!";
	private final static String EMPTY_RECORD_MESSAGE = "You cannot enter an empty line!";
	private final static String ERROR_WHILE_SAVING = "An error has occurred while saving data!";
	private final static String ERROR_WHILE_LOADING = "An error has occurred while loading data!";
	private final static String[] TABLE_COLUMNS_NAMES = {"Host name", "Username", "Password"};
	private final static String USER_HOME_PATH = System.getProperty("user.home") + "\\isicrypt"; //TODO: duplicato in più classi
	private final static String KEYRING_FILE_PATH = USER_HOME_PATH + "\\keyring.dat";
	private byte[] aesKey = null;
	private IKeyringView view;
	private IKeyringModel model;
	
	public KeyringController(IKeyringView view, IKeyringModel model) {
		this.view = view;
		this.model = model;
		this.view.attachViewObserver(this);
		this.view.getTable().setModel(this.tableBuilder());
		File selectedFile = new OpenButtons().fileChooser(FileTypes.GENERIC_FILE);
		if(selectedFile == null || !(selectedFile.exists())) {
			JOptionPane.showMessageDialog((KeyringView)this.view, KEY_NOT_SET_WARNING);
		} else {
			ObjectInputStream ois;
			try {
				ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(selectedFile)));
				this.aesKey = (byte[]) ois.readObject();
				File keyRingFile = new File(KEYRING_FILE_PATH);
				if(keyRingFile.exists() && keyRingFile.length() > 0) {
					this.model.loadData(new BufferedInputStream(new FileInputStream(KEYRING_FILE_PATH)), this.aesKey);
					this.view.getTable().setModel(this.tableBuilder());
				}
				
			} catch (IOException e) {
				JOptionPane.showMessageDialog((KeyringView)this.view, ERROR_WHILE_LOADING);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (InvalidKeyException e) {
				JOptionPane.showMessageDialog((KeyringView)this.view, WRONG_KEYSIZE_ERROR);
			}
		}
		if(this.aesKey == null) {
			//TODO: chiudere il keyring
		}
	}

	@Override
	public void showStart() {
		StartScreenView.getFrame().setVisible(true);
		//StartScreenView.redraw(); // TODO: uncomment quando arriva il metodo redraw()
	}
	
	@Override
	public void command_addButton() {
		String host = JOptionPane.showInputDialog("Host name", "Enter host name:");
		String user = JOptionPane.showInputDialog("Username", "Enter username:");
		String pass = JOptionPane.showInputDialog("Password", "Enter password:");
		if(host != null && user != null && pass != null) {
			this.model.addItem(host, user, pass);
			this.view.getTable().setModel(this.tableBuilder()); // TODO: controllare che non sia un duplicato
		} else {
			JOptionPane.showMessageDialog((KeyringView)this.view, EMPTY_RECORD_MESSAGE);
		}
	}

	@Override
	public void command_modifyButton() {
		int selectedRow = this.view.getTable().getSelectedRow();
		if(selectedRow != -1) {
			TableModel getTableModel = tableBuilder();
			String host = (String)getTableModel.getValueAt(selectedRow, 0);
			String user = (String)getTableModel.getValueAt(selectedRow, 1);
			String pass = (String)getTableModel.getValueAt(selectedRow, 2);
			String host2 = JOptionPane.showInputDialog("Host name", host);
			String user2 = JOptionPane.showInputDialog("Username", user);
			String pass2 = JOptionPane.showInputDialog("Password", pass);
			this.model.removeItem(host, user, pass);
			this.model.addItem(host2, user2, pass2);
			this.view.getTable().setModel(this.tableBuilder());	
		} else {
			JOptionPane.showMessageDialog((KeyringView)this.view, ROW_NOT_SELECTED);
		}
	}

	@Override
	public void command_cancelButton() {
		int selectedRow = this.view.getTable().getSelectedRow();
		if(selectedRow != -1) {
			int choice = JOptionPane.showConfirmDialog((KeyringView)this.view, DELETE_RECORD_CHECK, "", JOptionPane.YES_NO_OPTION);
			if(choice == JOptionPane.YES_OPTION) {
				TableModel getTableModel = tableBuilder();
				String host = (String)getTableModel.getValueAt(selectedRow, 0);
				String user = (String)getTableModel.getValueAt(selectedRow, 1);
				String pass = (String)getTableModel.getValueAt(selectedRow, 2);
				this.model.removeItem(host, user, pass);
				this.view.getTable().setModel(this.tableBuilder());
			}
		} else {
			JOptionPane.showMessageDialog((KeyringView)this.view, ROW_NOT_SELECTED);
		}
		
	}

	@Override
	public void command_encryptButton() {
		AES newEncryptor = new AES();
		if(this.aesKey == null) { // Generates a new key and stores it into a file
			ObjectOutputStream oos;
			File selectedFile = null;
			try {
				selectedFile = new OpenButtons().fileChooser(FileTypes.GENERIC_FILE);
				if(selectedFile != null) {
					newEncryptor.generateKey(128);
					this.aesKey = newEncryptor.getSymmetricKeySpec().getEncoded();
					oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(selectedFile)));
					oos.writeObject(this.aesKey);
					oos.close();
				}
			} catch (InvalidKeyException e) {
				JOptionPane.showMessageDialog((KeyringView)this.view, WRONG_KEYSIZE_ERROR);
			} catch (FileNotFoundException e) {
				JOptionPane.showMessageDialog((KeyringView)this.view, FILE_NOT_FOUND_ERROR + selectedFile.getAbsolutePath());
			} catch (IOException e) {
				JOptionPane.showMessageDialog((KeyringView)this.view, IO_WRITING_ERROR);
			}
		} else { // Reads the key from file
			ObjectInputStream ois;
			File selectedFile = new OpenButtons().fileChooser(FileTypes.GENERIC_FILE);
			try {
				if(selectedFile != null) {
					ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(selectedFile)));
					newEncryptor.setSymmetricKeySpec((byte[]) ois.readObject());
					this.aesKey = newEncryptor.getSymmetricKeySpec().getEncoded();
					ois.close();
				}
			} catch (IOException e) {
				JOptionPane.showMessageDialog((KeyringView)this.view, IO_READING_ERROR);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (InvalidKeyException e) {
				JOptionPane.showMessageDialog((KeyringView)this.view, WRONG_KEYSIZE_ERROR);
			}
		}
		if(this.aesKey == null) {
			JOptionPane.showMessageDialog((KeyringView)this.view, KEY_NOT_SET_WARNING);
		} else {
			JOptionPane.showMessageDialog((KeyringView)this.view, KEY_SUCCESSFULLY_SET_MESSAGE);
		}
	}

	@Override
	public void command_saveButton() {
		if(this.aesKey == null) {
			JOptionPane.showMessageDialog((KeyringView)this.view, KEY_NOT_SET_WARNING);
		} else {
			int choice = JOptionPane.showConfirmDialog((KeyringView)this.view, SAVE_RECORDS_CHECK, "", JOptionPane.YES_NO_OPTION);
			if(choice == JOptionPane.YES_OPTION) {
				File saveFile = new File(KEYRING_FILE_PATH);
				try {
					this.model.saveData(new BufferedOutputStream(new FileOutputStream(saveFile)), this.aesKey);
				} catch (InvalidKeyException e) {
					JOptionPane.showMessageDialog((KeyringView)this.view, WRONG_KEYSIZE_ERROR);
				} catch (IOException e) {
					JOptionPane.showMessageDialog((KeyringView)this.view, ERROR_WHILE_SAVING);
				}
			}
		}
	}
	
	private TableModel tableBuilder(){
		DefaultTableModel tableModel = new DefaultTableModel(TABLE_COLUMNS_NAMES, 0 ){
			private static final long serialVersionUID = 737530902377505148L;
			/*public boolean isCellEditable(int row, int column){
				return false;
			}*/
		};
		for(Triple<String, String, String> entry: (KeyringModel)this.model) {
			tableModel.addRow(new Object[] { entry.getFirst(), entry.getSecond(), entry.getThird() });
		}
		return tableModel;
	}
	
}
