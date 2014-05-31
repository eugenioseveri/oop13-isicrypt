package gui.controllers;
/**
 * @author Filippo Vimini
 * Created 24/05/2014
 */
import java.awt.ComponentOrientation;
import java.awt.HeadlessException;
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

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
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

public class FileExchangeController implements IFileExchangeViewObserver{
	//Initialize FileEchange Gui
	private static FileExchangeView view;
	private static String textTemp;
	SocketServer server = new SocketServer();
	/**
	 * 
	 * @param view
	 */
	public void setView(FileExchangeView view){
		FileExchangeController.view = view;
		FileExchangeController.view.attacFileExchangeViewObserve(this);
		server.start();
	}
	/**
	 * 
	 */
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
	/**
	 * 
	 */
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
	/**
	 * 
	 */
	@Override
	public void selectCompress() {
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
	/**
	 * 
	 */
	@Override
	public void selectContact() {
		JTable table = FileExchangeView.getContactTable();
		String host = table.getValueAt(table.getSelectedRow(), 1).toString();
		String name = table.getValueAt(table.getSelectedRow(), 0).toString(); 
		ContactInfo contact = new ContactInfo(host, name);
		FileExchangeModel.setContactInfo(contact);
		FileExchangeView.getVisualtextarea().setText("");
		FileExchangeController.setEnableButton(true);
		FileExchangeView.getFrame().setVisible(true);
	}
	/**
	 * 
	 */
	@Override
	public void addContact() {
		//Input dialog with a text field
		String name =  JOptionPane.showInputDialog("Name" 
		       ,"Enter in some text:");
		String host =  JOptionPane.showInputDialog("Host" 
			       ,"Enter in some text:");
		FileExchangeModel.setContactList(host, name);
		FileExchangeView.getContactTable().setModel(FileExchangeController.tableBuilder());
		FileExchangeController.setEnableButton(false);
	}
	/**
	 * 
	 */
	@Override
	public void closeConnection() {
		FileExchangeModel.setContactInfo(null);
		//Ristampa la Select contact TODO
	}
	/**
	 * 
	 */
	@Override
	public void changeContact() {
		FileExchangeModel.setContactInfo(null);
		FileExchangeController.setEnableButton(false);
		FileExchangeView.getFrame().setVisible(true);
	}
	/**
	 * 
	 */
	@Override
	public void sendText() {
		if(StringUtils.isBlank(FileExchangeView.getChattextarea().getText()))
			FileExchangeView.optionPanel("select contact");
		else {
			textTemp = FileExchangeView.getChattextarea().getText();
			socketClient(FileExchangeModel.getContactInfo(), null, textTemp);
			FileExchangeView.getChattextarea().setText("");
		}		
	}
	/**
	 * 
	 * @param search
	 * @return
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
	 * 
	 * @return
	 */
	public static TableModel tableBuilder(){
		DefaultTableModel model = new DefaultTableModel( new Object[] { "Host", "Name", "Status" }, 0 ){
			/**
			 * 
			 */
			private static final long serialVersionUID = 737530902377505148L;

			public boolean isCellEditable(int row, int column){
				return false;
			}
		};
		FileExchangeModel.setContactList("localhost", "FiloNotebook");
		FileExchangeModel.setContactList("Gincapa", "FiloGincapa");

		for (Map.Entry<String,String> entry : FileExchangeModel.getContactList().entrySet()) {
		        model.addRow(new Object[] { entry.getKey(), entry.getValue() });
		    }
		
		    return model;
	}
	/**
	 * 
	 * @return
	 */
	public static String getChatAreaText(){
		while(StringUtils.isBlank(FileExchangeView.getChattextarea().getText())){
		}
		return FileExchangeView.getChattextarea().getText();
	}
	/**
	 * 
	 * @param text
	 */
	public static void textApendClient(String text){
		JTextArea area = FileExchangeView.getVisualtextarea();
		area.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		String append = "\nYOU\n			"+text;
		area.append(append);
		
	}
	/**
	 * 
	 * @param text
	 */
	public static void fileAppendClient(String text){
		JTextArea area = FileExchangeView.getVisualtextarea();
		area.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		String append = "\nYOU\n			SENDING: "+text;
		area.append(append);
	}
	/**
	 * 
	 * @param text
	 * @param name
	 */
	public static void textAppendServer(String text, String name){
		JTextArea area = FileExchangeView.getVisualtextarea();
		area.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		String append = "\n"+name+"\n			"+text;
		area.append(append);
	}
	/**
	 * 
	 * @param text
	 * @param name
	 * @return
	 */
	public static boolean fileAppendServer(String text, String name){
		JTextArea area = FileExchangeView.getVisualtextarea();
		area.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		String append = "\n"+name+"\n			DOWNLOADING FILE? (yes/no): "+text;
		area.append(append);
		return false;
	}
	/**
	 * 
	 * @param state
	 */
	private static void setEnableButton(boolean state){
		FileExchangeView.getScrollpanetable().setVisible(!state);
		FileExchangeView.getScrollpanetable().setEnabled(!state);
		FileExchangeView.getScrollpanevisual().setVisible(state);
		FileExchangeView.getFilebutton().setEnabled(state);
		FileExchangeView.getStegabutton().setEnabled(state);
		FileExchangeView.getZipbutton().setEnabled(state);
		FileExchangeView.getAddcontactbutton().setEnabled(!state);
		FileExchangeView.getClosecontactbutton().setEnabled(state);
		FileExchangeView.getChangecontactbutton().setEnabled(state);
		FileExchangeView.getSendbutton().setEnabled(state);
		FileExchangeView.getChattextarea().setEnabled(state);
	}
	/**
	 * 
	 * @param contact
	 * @param buffer
	 * @param name
	 */
	private static void socketClient(ContactInfo contact, InputStream buffer, String name){
		try {
			if(buffer == null)new SocketClient(contact, name);
			else new SocketClient(contact, buffer,name);
		} catch (InvalidKeyException e) {
			FileExchangeView.optionPanel(e);	
		} catch (HeadlessException e) {
			FileExchangeView.optionPanel(e);	
		} catch (NoSuchAlgorithmException e) {
			FileExchangeView.optionPanel(e);	
		} catch (BadPaddingException e) {
			FileExchangeView.optionPanel(e);	
		} catch (NoSuchPaddingException e) {
			FileExchangeView.optionPanel(e);	
		} catch (IllegalBlockSizeException e) {
			FileExchangeView.optionPanel(e);	
		} catch (InvalidKeySpecException e) {
			FileExchangeView.optionPanel(e);	
		} catch (IOException e) {
			FileExchangeView.optionPanel("Server doesn't connect or portfowarding problem");	
		}
	}
}