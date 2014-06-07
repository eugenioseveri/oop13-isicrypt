package gui.controllers;
/**
 * @author Filippo Vimini
 * Created 24/05/2014
 */
import java.awt.HeadlessException;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.apache.commons.lang3.StringUtils;

import algorithms.GZip;
import algorithms.SocketClient;
import algorithms.SocketServer;
import algorithms.Steganography;
import algorithms.TypeConverter;
import gui.models.ContactInfo;
import gui.models.FileExchangeModel;
import gui.models.OpenButtons;
import gui.models.OpenButtons.FileTypes;
import gui.views.FileExchangeView;
import gui.views.StartScreenView;

public class FileExchangeController implements IFileExchangeViewObserver, IGeneralViewObserver{
	//Initialize FileEchange Gui
	private static FileExchangeView view;
	private static String textTemp;
	private final static String userHomePath = System.getProperty("user.home") + "/isicrypt";
	private final static String fileExchangeSettings = userHomePath + "/accountContacts.dat";

	public void  setView(FileExchangeView view){
		SocketServer server = new SocketServer();
		FileExchangeController.view = view;
		FileExchangeController.view.attacFileExchangeViewObserve(this);
		server.start();
	}

	@Override
	public void selectFile() {
		
		if(searchContact(FileExchangeModel.getContactInfo())!= null){	
			File fileTemp = new OpenButtons().fileChooser(FileTypes.GENERIC_FILE);
			FileInputStream streamTemp = null;
			try {
				streamTemp = new FileInputStream(fileTemp);
			} catch (FileNotFoundException e) {
				FileExchangeView.optionPanel(e);	
			}
			socketClient(FileExchangeModel.getContactInfo(), streamTemp, fileTemp.getName());
		}
		else FileExchangeView.optionPanel("select contact");
	}

	@Override
	public void stegaImage() {
		if(searchContact(FileExchangeModel.getContactInfo())!= null){
			File imageTemp = new OpenButtons().fileChooser(FileTypes.IMAGE);
			File textTemp = new OpenButtons().fileChooser(FileTypes.TEXT);
			if(imageTemp!= null && textTemp != null){
			new Steganography();
			File imageToSend = null;
			BufferedInputStream bis = null;
			try {
				imageToSend = Steganography.stegaForClient(imageTemp,"png", TypeConverter.fileToString(textTemp));
				bis = new BufferedInputStream(new FileInputStream(imageToSend));
			} catch (FileNotFoundException e) {
				FileExchangeView.optionPanel(e);	
			} catch (IOException e) {
				FileExchangeView.optionPanel(e);	
			}
			if(imageToSend != null)
				socketClient(FileExchangeModel.getContactInfo(), bis, imageToSend.getName());
			}
		}
		else FileExchangeView.optionPanel("select contact");
	}

	@Override
	public void selectCompressedFile() {
		if(searchContact(FileExchangeModel.getContactInfo())!= null){
			BufferedInputStream buffer = null;
			ByteArrayOutputStream outBuffer = null;
			File tempFile = new OpenButtons().fileChooser(FileTypes.GENERIC_FILE);
			try {
				buffer = new BufferedInputStream(new FileInputStream(tempFile)) ;
				outBuffer = new ByteArrayOutputStream(); 
				buffer = new BufferedInputStream(new ByteArrayInputStream(outBuffer.toByteArray()));
				//Compress Input file
				GZip.getInstance().compress(buffer, outBuffer);
				
			} catch (IOException e) {
				FileExchangeView.optionPanel(e);	
			}
			socketClient(FileExchangeModel.getContactInfo(), buffer, tempFile.getName());
		}
		else FileExchangeView.optionPanel("select contact");
	}

	@Override
	public void selectContact() {
		final JTable table = FileExchangeView.getContactTable();
		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
			      if (e.getClickCount() == 2){
				    	Point p = e.getPoint();
				    	int row = table.rowAtPoint(p);
				    	String host = table.getValueAt(row, 1).toString();
				  		String name = table.getValueAt(row, 0).toString(); 
				  		if(host != null && name != null){
				  			ContactInfo contact = new ContactInfo(host, name);
				  			FileExchangeModel.setContactInfo(contact);
					  		FileExchangeView.getVisualtextarea().setText("");
					  		FileExchangeController.setEnableButton(true);
					  		FileExchangeView.getFrame().setVisible(true);
				  		}
			      }
			}
		});
	}

	@Override
	public void addContact() {
		//Input dialog with a text field
		String name =  JOptionPane.showInputDialog("Name", "Enter in some text:");
		String host =  JOptionPane.showInputDialog("Host", "Enter in some text:");
		FileExchangeModel.setContactList(host, name);
		try {
			FileExchangeModel.saveContacts(new File(fileExchangeSettings));
		} catch (FileNotFoundException e) {
			FileExchangeView.optionPanel("Contact file not found");
		} catch (IOException e) {
			FileExchangeView.optionPanel("i/o error occured");
		}
		FileExchangeController.setEnableButton(false);
		FileExchangeView.getContactTable().setModel(tableBuilder());
		
	}

	@Override
	public void deleteContact() {
		JTable table = FileExchangeView.getContactTable();
		int selectedRow = table.getSelectedRow();
		if(selectedRow != -1) {
			TableModel getTableModel = tableBuilder();
			String host = (String) getTableModel.getValueAt(selectedRow, 0);
			String name = (String)getTableModel.getValueAt(selectedRow, 1);
			FileExchangeModel.getContactList().remove(host, name);
			try {
				FileExchangeModel.saveContacts(new File(fileExchangeSettings));
			} catch (FileNotFoundException e) {
				FileExchangeView.optionPanel("Contact file not found");
			} catch (IOException e) {
				FileExchangeView.optionPanel("i/o error occured");
			}
			FileExchangeView.getContactTable().setModel(tableBuilder());
		}
	}
		
	@Override
	public void changeContact() {
		FileExchangeModel.setContactInfo(null);
		FileExchangeController.setEnableButton(false);
		FileExchangeView.getFrame().setVisible(true);
	}

	@Override
	public void sendText() {
		if(StringUtils.isBlank(FileExchangeView.getChattextarea().getText()))
			FileExchangeView.optionPanel("none text entered");
		else {
			textTemp = FileExchangeView.getChattextarea().getText();
			socketClient(FileExchangeModel.getContactInfo(), null, textTemp);
			FileExchangeView.getChattextarea().setText("");
		}		
	}
	@Override
	public void showStart() {
		StartScreenView.getFrame().setVisible(true);
		StartScreenView.redraw();	
	}
	/**
	 * close the server connection, used like bridge for view from model
	 */
	public static void closeThread(){
		try {
			SocketServer.getSocket().close();
		} catch (IOException e) {
			FileExchangeView.optionPanel(e);		}
	}
	/**
	 * Search a ContactInfo from HashMap "ContactList"
	 * 
	 * @param search		ContactInfo to search
	 * @return ContactInfo
	 */
	private ContactInfo searchContact(ContactInfo search){
		for(Entry<String, String> entry : FileExchangeModel.getContactList().entrySet()){
			if(search.getHost().equals(entry.getKey())){
				return search;
			}
		}
		return null;
	}
	/**
	 * Creae a TableModel with two columns that represents the host and the name of the reachable server.
	 * Set the cell of the table no editable and load from File the list of contact.
	 * 
	 * @return TableModel
	 */
	public static TableModel tableBuilder(){
		DefaultTableModel model = new DefaultTableModel( new Object[] { "Host", "Name" }, 0 ){
			private static final long serialVersionUID = 737530902377505148L;
			//set the cell non editable, read only table
			public boolean isCellEditable(int row, int column){ 
				return false;
			}
		};
		try {
			FileExchangeModel.loadContacts(new File(fileExchangeSettings));
		}
		//Not visible to the user
		catch (FileNotFoundException e) {} 
		  catch (ClassNotFoundException e) {
			FileExchangeView.optionPanel(e);
		} catch (IOException e) {
			FileExchangeView.optionPanel(e);
		}
		for (Map.Entry<String,String> entry : FileExchangeModel.getContactList().entrySet()) {
		        model.addRow(new Object[] { entry.getKey(), entry.getValue() });
		    }
		    return model;
	}

	/**
	 * Append  text on JTextArea, with the formatting of text for the Client.
	 * 
	 * @param text		text that will be append.
	 */
	public static void textApendClient(String text){
		JTextArea area = FileExchangeView.getVisualtextarea();
		String append = "\nYOU\n			"+text;
		area.append(append);
	}
	/**
	 *Append  text on JTextArea, with the formatting of text for the server.
	 * 
	 * @param text		text that will be append.
	 * @param name		name of the client.
	 */
	public static void textAppendServer(String text, String name){
		JTextArea area = FileExchangeView.getVisualtextarea();
		String append = "\n"+name+"\n			"+text;
		area.append(append);
	}
	/**
	 *Append  text on JTextArea, with the formatting for the server.
	 *
	 * @param text		name of file.
	 * @param name		name of client.
	 * @return boolean
	 */
	public static boolean fileAppendServer(String text, String name){
		JTextArea area = FileExchangeView.getVisualtextarea();
		String append = "\n"+name+"\n			DOWNLOADING FILE? (yes/no): "+text;
		area.append(append);
		return false;
	}
	/**
	 * Set JButton enable or disabled, created for better class formatting.
	 * 
	 * @param state		boolean that set the button enable or disable.
	 */
	//Public because is used even when the FileExchange is closed (bugFix for closure after contact selection)
	public static void setEnableButton(boolean state){
		FileExchangeView.getScrollpanetable().setVisible(!state);
		FileExchangeView.getScrollpanetable().setEnabled(!state);
		FileExchangeView.getScrollpanevisual().setVisible(state);
		FileExchangeView.getFilebutton().setEnabled(state);
		FileExchangeView.getStegabutton().setEnabled(state);
		FileExchangeView.getZipbutton().setEnabled(state);
		FileExchangeView.getAddcontactbutton().setEnabled(!state);
		FileExchangeView.getDeleteContactButton().setEnabled(!state);
		FileExchangeView.getChangecontactbutton().setEnabled(state);
		FileExchangeView.getSendbutton().setEnabled(state);
		FileExchangeView.getChattextarea().setEnabled(state);
	}
	/**
	 * Create a socket for client and set a series of exception, for better class formatting.
	 * 
	 * @param contact		info for the server.
	 * @param buffer		represent the File.
	 * @param name			For first constructor, represent the text that will be sent
	 * 						for the second, represent the name of file.
	 */
	private static void socketClient(ContactInfo contact, InputStream buffer, String name){
		try {
			//the buffer represent the file
			if(buffer == null)new SocketClient(contact, name);
			else new SocketClient(contact, buffer, name);
		} catch (InvalidKeyException e) {
			FileExchangeView.optionPanel(e);	
		} catch (HeadlessException e) {
			FileExchangeView.optionPanel(e);	
		} catch (NoSuchAlgorithmException e) {
			FileExchangeView.optionPanel(e);	
		} catch (InvalidKeySpecException e) {
			FileExchangeView.optionPanel(e);	
		} catch (IOException e) {
			FileExchangeView.optionPanel("Server doesn't connect or portfowarding problem");	
		}
	}
	
	public static void setProgressbar(int value){
		FileExchangeView.setSendprogress(value);
	}
}