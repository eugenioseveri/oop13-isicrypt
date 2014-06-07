package gui.controllers;
/**
 * @author Filippo Vimini
 * Created 24/05/2014
 */
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



//Used for set the JTable
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.apache.commons.lang3.StringUtils;

import algorithms.GZip;
import algorithms.SocketClient;
import algorithms.SocketServer;
import algorithms.Steganography;
import algorithms.TypeConverter;
import gui.models.ContactInfo;
import gui.models.IFileExchangeModel;
import gui.views.IFileExchangeView;
import gui.views.OpenButtons;
import gui.views.StartScreenView;
import gui.views.OpenButtons.FileTypes;

public class FileExchangeController implements IFileExchangeViewObserver, IGeneralViewObserver{
	//Initialize FileEchange Gui
	private IFileExchangeView view;
	private IFileExchangeModel model;
	private static String textTemp;
	private final static String userHomePath = System.getProperty("user.home") + "/isicrypt";
	private final static String fileExchangeSettings = userHomePath + "/accountContacts.dat";

	@Override
	public void  setViewAndModel(IFileExchangeView view, IFileExchangeModel model){
		SocketServer server = new SocketServer();
		server.attacFileExchangeViewObserve(this);
		this.model = model;
		this.view = view;
		this.view.getContactTable().setModel(this.tableBuilder());
		this.view.attachFileExchangeViewObserve(this);
		server.start();
	}

	@Override
	public void selectFile() {
		if(searchContact(model.getContactInfo())!= null){	
			File fileTemp = new OpenButtons().fileChooser(FileTypes.GENERIC_FILE);
			FileInputStream streamTemp = null;
			try {
				streamTemp = new FileInputStream(fileTemp);
			} catch (FileNotFoundException e) {
				view.optionPanel(e);	
			}
			socketClientSend(model.getContactInfo(), streamTemp, fileTemp.getName());
		}
		else view.optionPanel("select contact");
	}

	@Override
	public void stegaImage() {
		if(searchContact(model.getContactInfo())!= null){
			File imageTemp = new OpenButtons().fileChooser(FileTypes.IMAGE);
			File textTemp = new OpenButtons().fileChooser(FileTypes.TEXT);
			if(imageTemp!= null && textTemp != null){
			new Steganography();
			File imageToSend = null;
			BufferedInputStream bis = null;
			try {
				imageToSend = new Steganography().stegaForClient(imageTemp,"png", TypeConverter.fileToString(textTemp));
				bis = new BufferedInputStream(new FileInputStream(imageToSend));
			} catch (FileNotFoundException e) {
				view.optionPanel(e);	
			} catch (IOException e) {
				view.optionPanel(e);	
			}
			if(imageToSend != null)
				socketClientSend(model.getContactInfo(), bis, imageToSend.getName());
			}
		}
		else view.optionPanel("select contact");
	}

	@Override
	public void selectCompressedFile() {
		if(searchContact(model.getContactInfo())!= null){
			File tempFile = new OpenButtons().fileChooser(FileTypes.GENERIC_FILE);
			ByteArrayOutputStream outByteBuffer = new ByteArrayOutputStream();
			ByteArrayInputStream inByteBuffer = null;
			BufferedInputStream bufferForZip = null;
			try {
				bufferForZip = new BufferedInputStream(new FileInputStream(tempFile));
				//Compress Input file
				GZip.getInstance().compress(bufferForZip, outByteBuffer);
				//Put in InputStream for send with client
				inByteBuffer = new ByteArrayInputStream(outByteBuffer.toByteArray());
			}catch(FileNotFoundException e){
				view.optionPanel("File not found");
			} catch (IOException e) {
				view.optionPanel("Error occured during compression");	
			}
			socketClientSend(model.getContactInfo(), inByteBuffer, tempFile.getName()+".Gzip");
		}
		else view.optionPanel("select contact");
	}

	@Override
	public void selectContact() {
		view.getContactTable().addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
			      if (e.getClickCount() == 2){
				    	Point p = e.getPoint();
				    	int row = view.getContactTable().rowAtPoint(p);
				    	String host = view.getContactTable().getValueAt(row, 1).toString();
				  		String name = view.getContactTable().getValueAt(row, 0).toString(); 
				  		if(host != null && name != null){
				  			ContactInfo contact = new ContactInfo(host, name);
				  			model.setContactInfo(contact);
					  		view.getVisualtextarea().setText("");
					  		setEnableButton(true);
					  		view.getFrame().setVisible(true);
				  		}
			      }
			}
		});
	}

	@Override
	public void addContact() {
		//Input dialog with a text field
		String name =  view.setOptionPane("Name", "Enter in some text:");
		String host =  view.setOptionPane("Host", "Enter in some text:");
		model.setContactList(host, name);
		try {
			model.saveContacts(new File(fileExchangeSettings));
		} catch (FileNotFoundException e) {
			view.optionPanel("Contact file not found");
		} catch (IOException e) {
			view.optionPanel("i/o error occured");
		}
		setEnableButton(false);
		view.getContactTable().setModel(tableBuilder());
		
	}

	@Override
	public void deleteContact() {
		int selectedRow = view.getContactTable().getSelectedRow();
		if(selectedRow != -1) {
			TableModel getTableModel = tableBuilder();
			String host = (String) getTableModel.getValueAt(selectedRow, 0);
			String name = (String)getTableModel.getValueAt(selectedRow, 1);
			model.getContactList().remove(host, name);
			try {
				model.saveContacts(new File(fileExchangeSettings));
			} catch (FileNotFoundException e) {
				view.optionPanel("Contact file not found");
			} catch (IOException e) {
				view.optionPanel("i/o error occured");
			}
			view.getContactTable().setModel(tableBuilder());
		}
	}

	@Override
	public void changeContact() {
		model.setContactInfo(null);
		setEnableButton(false);
		view.getFrame().setVisible(true);
	}

	@Override
	public void sendText() {
		if(StringUtils.isBlank(view.getChattextarea().getText()))
			view.optionPanel("none text entered");
		else {
			textTemp = view.getChattextarea().getText();
			socketClientSend(model.getContactInfo(), null, textTemp);
			view.getChattextarea().setText("");
		}		
	}
	
	@Override
	public void showStart() {
		StartScreenView.getFrame().setVisible(true);
		StartScreenView.redraw();	
	}

	@Override
	public void closeThread(){
		try {
			SocketServer.getSocket().close();
		} catch (IOException e) {
			view.optionPanel(e);		}
	}

	@Override
	public TableModel tableBuilder(){
		DefaultTableModel tableModel = new DefaultTableModel( new Object[] { "Host", "Name" }, 0 ){
			private static final long serialVersionUID = 737530902377505148L;
			//set the cell non editable, read only table
			public boolean isCellEditable(int row, int column){ 
				return false;
			}
		};
		try {
			model.loadContacts(new File(fileExchangeSettings));
		}
		//Not visible to the user
		catch (FileNotFoundException e) {} 
		  catch (ClassNotFoundException e) {
			view.optionPanel(e);
		} catch (IOException e) {
			view.optionPanel(e);
		}
		for (Map.Entry<String,String> entry : model.getContactList().entrySet()) {
		        tableModel.addRow(new Object[] { entry.getKey(), entry.getValue() });
		    }
		    return tableModel;
	}

	@Override
	public void textApendClient(String text){
	//	JTextArea area = view.getVisualtextarea();
		String append = "\nYOU\n			"+text;
		view.getVisualtextarea().append(append);
	}

	@Override
	public void textAppendServer(String text, String name){
	//	JTextArea area = view.getVisualtextarea();
		String append = "\n"+name+"\n			"+text;
		view.getVisualtextarea().append(append);
	}

	@Override
	public boolean fileAppendServer(String text, String name){
	//	JTextArea area = view.getVisualtextarea();
		String append = "\n"+name+"\n			DOWNLOADING FILE? (yes/no): "+text;
		view.getVisualtextarea().append(append);
		return false;
	}

	//Public because is used even when the FileExchange is closed (bugFix for closure after contact selection)
	@Override
	public void setEnableButton(boolean state){
		view.getScrollpanetable().setVisible(!state);
		view.getScrollpanetable().setEnabled(!state);
		view.getScrollpanevisual().setVisible(state);
		view.getFilebutton().setEnabled(state);
		view.getStegabutton().setEnabled(state);
		view.getZipbutton().setEnabled(state);
		view.getAddcontactbutton().setEnabled(!state);
		view.getDeleteContactButton().setEnabled(!state);
		view.getChangecontactbutton().setEnabled(state);
		view.getSendbutton().setEnabled(state);
		view.getChattextarea().setEnabled(state);
	}
	
	@Override
	public void setProgressbar(int value){
		view.setSendprogress(value);
	}
	/**
	 * Search a ContactInfo from HashMap "ContactList"
	 * 
	 * @param search		ContactInfo to search
	 * @return ContactInfo
	 */
	private ContactInfo searchContact(ContactInfo search){
		for(Entry<String, String> entry : model.getContactList().entrySet()){
			if(search.getHost().equals(entry.getKey())){
				return search;
			}
		}
		return null;
	}
	
	@Override
	public void threadErrorThrow(Exception error){
		view.optionPanel(error);
	}
	/**
	 * Create a socket for client and set a series of exception, for better class formatting.
	 * 
	 * @param contact		info for the server.
	 * @param buffer		represent the File.
	 * @param name			For first constructor, represent the text that will be sent
	 * 						for the second, represent the name of file.
	 */
	private void socketClientSend(ContactInfo contact, InputStream buffer, String name){
		try {
			//the buffer represent the file
			if(buffer == null){
				new SocketClient(contact, name, this);
			}
			else new SocketClient(contact, buffer, name, this);
		} catch (InvalidKeyException e) {
			view.optionPanel("Problem with key exchange");		
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			view.optionPanel("Problem to generate RSA public key");		
		} catch (IOException e) {
			view.optionPanel("socket is closed or port fowarding problem");	
		}
	}
}