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
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import algorithms.AES;
import gui.models.IKeyringModel;
import gui.models.KeyringModel;
import gui.models.Triple;
import gui.views.IKeyringView;
import gui.views.OpenButtons;
import gui.views.StartScreenView;
import gui.views.OpenButtons.FyleTypes;

/**
 * Class used to implement the keyring function controller.
 * @author Eugenio Severi
 */
public class KeyringController implements IKeyringViewObserver, IGeneralViewObserver {

	private final static String ROW_NOT_SELECTED = "You must select a row first!";
	private final static String DELETE_RECORD_CHECK = "Do you really want to delete this entry?";
	private final static String SAVE_RECORDS_CHECK = "Do you really want to save?";
	private final static String KEY_SUCCESSFULLY_SET_MESSAGE = "A key is now set.";
	private final static String KEY_NOT_SET_WARNING = "A key has not been set. Saving is not possible until you set a key!";
	private final static String EMPTY_RECORD_MESSAGE = "You cannot enter an empty line!";
	private final static String ERROR_WHILE_SAVING = "An error has occurred while saving data!";
	private final static String ERROR_WHILE_LOADING = "An error has occurred while loading data!";
	private final static String ALREADY_EXISTING_ITEM = "This item has already been added!";
	private final static String[] TABLE_COLUMNS_NAMES = {"Host name", "Username", "Password"};
	private final static String USER_HOME_PATH = System.getProperty("user.home") + "/isicrypt"; //TODO: duplicato in pi� classi
	private final static String KEYRING_FILE_PATH = USER_HOME_PATH + "/keyring.dat";
	private byte[] aesKey;
	final private IKeyringView view;
	final private IKeyringModel model;
	
	/**
	 * Creates a new controller and asks the user to select a key to encrypt/decrypt the database (if available).
	 * If a correct key is specified, the controller tries to load a database if already exists.
	 * @param view The keyring view
	 * @param model The keyring model
	 */
	public KeyringController(IKeyringView view, IKeyringModel model) {
		this.view = view;
		this.model = model;
		this.view.attachViewObserver(this);
		this.view.getTable().setModel(this.tableBuilder());
		final File selectedFile = new OpenButtons().fileChooser(FyleTypes.GENERIC_FILE);
		if(selectedFile == null || !(selectedFile.exists())) {
			this.view.showMessageDialog(KEY_NOT_SET_WARNING);
		} else {
			ObjectInputStream ois;
			try {
				ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(selectedFile)));
				this.aesKey = (byte[]) ois.readObject();
				final File keyRingFile = new File(KEYRING_FILE_PATH);
				if(keyRingFile.exists() && keyRingFile.length() > 0) {
					this.model.loadData(new BufferedInputStream(new FileInputStream(KEYRING_FILE_PATH)), this.aesKey);
					this.view.getTable().setModel(this.tableBuilder());
				}
			} catch (IOException e) {
				this.view.showMessageDialog(ERROR_WHILE_LOADING);
			} catch (ClassNotFoundException e) {
				this.view.showMessageDialog(e.getMessage());
			} catch (InvalidKeyException e) {
				this.view.showMessageDialog(WRONG_KEYSIZE_ERROR);
			}
		}
	}

	@Override
	public void showStart() {
		StartScreenView.getFrame().setVisible(true);
		StartScreenView.redraw();
	}
	
	@Override
	public void command_addButton() {
		final String host = this.view.showInputDialog("Host name", "Enter host name:");
		final String user = this.view.showInputDialog("Username", "Enter username:");
		final String pass = this.view.showInputDialog("Password", "Enter password:");
		if(host != null && user != null && pass != null) {
			if(this.model.contains(host, user, pass)) {
				this.view.showMessageDialog(ALREADY_EXISTING_ITEM);
			} else {
				this.model.addItem(host, user, pass);
				this.view.getTable().setModel(this.tableBuilder());
			}
		} else {
			this.view.showMessageDialog(EMPTY_RECORD_MESSAGE);
		}
	}

	@Override
	public void command_modifyButton() {
		final int selectedRow = this.view.getTable().getSelectedRow();
		if(selectedRow == -1) {
			this.view.showMessageDialog(ROW_NOT_SELECTED);
		} else {
			final TableModel getTableModel = tableBuilder();
			final String host = (String)getTableModel.getValueAt(selectedRow, 0);
			final String user = (String)getTableModel.getValueAt(selectedRow, 1);
			final String pass = (String)getTableModel.getValueAt(selectedRow, 2);
			final String host2 = this.view.showInputDialog("Host name", host);
			final String user2 = this.view.showInputDialog("Username", user);
			final String pass2 = this.view.showInputDialog("Password", pass);
			this.model.removeItem(host, user, pass);
			this.model.addItem(host2, user2, pass2);
			this.view.getTable().setModel(this.tableBuilder());	
		}
	}

	@Override
	public void command_cancelButton() {
		final int selectedRow = this.view.getTable().getSelectedRow();
		if(selectedRow == -1) {
			this.view.showMessageDialog(ROW_NOT_SELECTED);
		} else {
			if(this.view.showYesNoOptionPane(DELETE_RECORD_CHECK)) {
				final TableModel getTableModel = tableBuilder();
				final String host = (String)getTableModel.getValueAt(selectedRow, 0);
				final String user = (String)getTableModel.getValueAt(selectedRow, 1);
				final String pass = (String)getTableModel.getValueAt(selectedRow, 2);
				this.model.removeItem(host, user, pass);
				this.view.getTable().setModel(this.tableBuilder());
			}
		}
		
	}

	@Override
	public void command_encryptButton() {
		final AES newEncryptor = new AES();
		if(this.aesKey == null) { // Generates a new key and stores it into a file
			ObjectOutputStream oos;
			File selectedFile = null;
			try {
				selectedFile = new OpenButtons().fileChooser(FyleTypes.GENERIC_FILE);
				if(selectedFile != null) {
					newEncryptor.generateKey(128);
					this.aesKey = newEncryptor.getSymmetricKeySpec().getEncoded();
					oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(selectedFile)));
					oos.writeObject(this.aesKey);
					oos.close();
				}
			} catch (InvalidKeyException e) {
				this.view.showMessageDialog(WRONG_KEYSIZE_ERROR);
			} catch (FileNotFoundException e) {
				this.view.showMessageDialog(FILE_NOT_FOUND_ERROR + selectedFile.getAbsolutePath());
			} catch (IOException e) {
				this.view.showMessageDialog(IO_WRITING_ERROR);
			}
		} else { // Reads the key from file
			ObjectInputStream ois;
			final File selectedFile = new OpenButtons().fileChooser(FyleTypes.GENERIC_FILE);
			try {
				if(selectedFile != null) {
					ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(selectedFile)));
					newEncryptor.setSymmetricKeySpec((byte[]) ois.readObject());
					this.aesKey = newEncryptor.getSymmetricKeySpec().getEncoded();
					ois.close();
				}
			} catch (IOException e) {
				this.view.showMessageDialog(IO_READING_ERROR);
			} catch (ClassNotFoundException e) {
				this.view.showMessageDialog(e.getMessage());
			} catch (InvalidKeyException e) {
				this.view.showMessageDialog(WRONG_KEYSIZE_ERROR);
			}
		}
		if(this.aesKey == null) {
			this.view.showMessageDialog(KEY_NOT_SET_WARNING);
		} else {
			this.view.showMessageDialog(KEY_SUCCESSFULLY_SET_MESSAGE);
		}
	}

	@Override
	public void command_saveButton() {
		if(this.aesKey == null) {
			this.view.showMessageDialog(KEY_NOT_SET_WARNING);
		} else {
			if(this.view.showYesNoOptionPane(SAVE_RECORDS_CHECK)) {
				final File saveFile = new File(KEYRING_FILE_PATH);
				try {
					this.model.saveData(new BufferedOutputStream(new FileOutputStream(saveFile)), this.aesKey);
				} catch (InvalidKeyException e) {
					this.view.showMessageDialog(WRONG_KEYSIZE_ERROR);
				} catch (IOException e) {
					this.view.showMessageDialog(ERROR_WHILE_SAVING);
				}
			}
		}
	}
	
	/**
	 * @return A new table model, used to store data for the JTable in the view
	 */
	private TableModel tableBuilder(){
		final DefaultTableModel tableModel = new DefaultTableModel(TABLE_COLUMNS_NAMES, 0 ){
			private static final long serialVersionUID = 737530902377505148L;
			public boolean isCellEditable(final int row, final int column){
				return false; // Sets the table as not editable
			}
		};
		for(Triple<String, String, String> entry: (KeyringModel)this.model) {
			tableModel.addRow(new Object[] { entry.getFirst(), entry.getSecond(), entry.getThird() });
		}
		return tableModel;
	}
	
}
