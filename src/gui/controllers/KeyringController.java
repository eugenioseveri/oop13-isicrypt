package gui.controllers;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import gui.models.IKeyringModel;
import gui.models.KeyringModel;
import gui.models.Triple;
import gui.views.IKeyringView;
import gui.views.KeyringView;
import gui.views.StartScreenView;

public class KeyringController implements IKeyringViewObserver, IGeneralViewObserver {

	private final static String USER_HOME_PATH = System.getProperty("user.home") + "\\isicrypt"; //TODO: duplicato in più classi
	private final static String KEYRING_FILE_PATH = USER_HOME_PATH + "\\keyring.dat";
	private IKeyringView view;
	private IKeyringModel model;
	
	public KeyringController(IKeyringView view, IKeyringModel model) {
		this.view = view;
		this.model = model;
		this.view.attachViewObserver(this);
	}

	@Override
	public void showStart() {
		StartScreenView.getFrame().setVisible(true);
		//StartScreenView.redraw(); // TODO: uncomment quando arriva il metodo redraw()
	}
	
	@Override
	public void command_addButton() {
		String host =  JOptionPane.showInputDialog("Host name", "Enter host name:");
		String user =  JOptionPane.showInputDialog("Username", "Enter username:");
		String pass =  JOptionPane.showInputDialog("Password", "Enter password:");
		this.model.addItem(host, user, pass);
		this.view.getTable().setModel(this.tableBuilder()); // TODO: controllare che non sia un duplicato
	}

	@Override
	public void command_modifyButton() {
		int selectedRow = this.view.getTable().getSelectedRow();
		if(selectedRow != -1) {
			int i = 0;
			Triple<String, String, String> current = null;
			/*for(int i=0; i==selectedRow; i++) {
				current = this.model.iterator().next();
			}*/
			while(i<=selectedRow+2) { // TODO:  NOT WORKING
				current = this.model.iterator().next();
				System.out.println(current.toString());
				i++;
			}
			System.out.println(selectedRow);
			System.out.println(current.toString());

				
		}
		// TODO Auto-generated method stub
	}

	@Override
	public void command_cancelButton() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void command_encryptButton() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void command_saveButton() {
		int choice = JOptionPane.showConfirmDialog((KeyringView)this.view, "Do you really want to save?", "", JOptionPane.YES_NO_OPTION);
		if(choice == JOptionPane.YES_OPTION) {
			File saveFile = new File(KEYRING_FILE_PATH);
			try {
				this.model.saveData(new BufferedOutputStream(new FileOutputStream(saveFile)));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public TableModel tableBuilder(){
		DefaultTableModel tableModel = new DefaultTableModel( new Object[] { "Host", "Username", "Password" }, 0 ){
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
