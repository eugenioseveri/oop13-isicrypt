package gui.controllers;

import java.awt.ComponentOrientation;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JOptionPane;
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

public class FileExchangeController implements IFileExchangeViewObserve{
	//Initialize FileEchange Gui
	private static FileExchangeView view;
	private static String textTemp;
	SocketServer server = new SocketServer();
	
	public void setView(FileExchangeView view){
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			new SocketClient(FileExchangeModel.getContactInfo(), streamTemp, fileTemp.getName());
		}
		//TODO falla specifica
		else JOptionPane.showMessageDialog(FileExchangeView.getDialog(), "Select contact");
	}

	@Override
	public void stegaImage() {
		if(searchContact(FileExchangeModel.getContactInfo())!= null){		
			new TypeConverter();
			new Steganography().messageEncrypter(new OpenButtons().fileChooser(FileTypes.IMAGE),
					"png", 
					TypeConverter.fileToString(new OpenButtons().fileChooser(FileTypes.TEXT))) ;
		}
		//TODO falla specifica
		else JOptionPane.showMessageDialog(FileExchangeView.getDialog(), "Select contact");
	}

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
				e.printStackTrace();
			}
			new SocketClient(FileExchangeModel.getContactInfo(), buffer, tempFile.getName());
		}
		//TODO falla specifica
		else JOptionPane.showMessageDialog(FileExchangeView.getDialog(), "Select contact");
	}

	@Override
	public void selectContact() {
		ContactInfo contact = new ContactInfo(FileExchangeView.getContactTable()
				.getValueAt(FileExchangeView.getContactTable().getSelectedRow(), 1).toString(), FileExchangeView.getContactTable()
				.getValueAt(FileExchangeView.getContactTable().getSelectedRow(), 0).toString());
		FileExchangeModel.setContactInfo(contact);
		FileExchangeController.setEnableButton(true);
		FileExchangeView.getFrame().setVisible(true);
	}
	
	@Override
	public void addContact() {
		// TODO Crea JDialog con testo in input
		//Input dialog with a text field
		String name =  JOptionPane.showInputDialog("Name" 
		       ,"Enter in some text:");
		String host =  JOptionPane.showInputDialog("Host" 
			       ,"Enter in some text:");
		FileExchangeModel.setContactList(host, name);
		FileExchangeView.getContactTable().setModel(FileExchangeController.tableBuilder());
		FileExchangeController.setEnableButton(false);
	}
	
	@Override
	public void closeConnection() {
		FileExchangeModel.setContactInfo(null);
		//Ristampa la Select contact TODO
	}

	@Override
	public void changeContact() {
		FileExchangeModel.setContactInfo(null);
		FileExchangeController.setEnableButton(false);
		FileExchangeView.getFrame().setVisible(true);
	}

	@Override
	public void sendText() {
		if(StringUtils.isBlank(FileExchangeView.getChattextarea().
				getText()))JOptionPane.showMessageDialog(FileExchangeView.getDialog(), "None text entered");
		else textTemp = FileExchangeView.getChattextarea().getText();
		new SocketClient(FileExchangeModel.getContactInfo(), textTemp);
		FileExchangeView.getChattextarea().setText("");		
	}

	private ContactInfo searchContact(ContactInfo search){
		for(Entry<String, String> entry : FileExchangeModel.getContactList().entrySet()){
			if(search.getHost().equals(entry.getKey())){
				return search;
			}
		}
		return null;
	}

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
	
	public static String getChatAreaText(){
		while(StringUtils.isBlank(FileExchangeView.getChattextarea().getText())){
		}
		return FileExchangeView.getChattextarea().getText();
	}
	
	public static void textApendClient(String text){
		JTextArea area = FileExchangeView.getVisualtextarea();
		area.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		String append = "\nYOU\n			"+text;
		area.append(append);
		
	}
	
	public static void fileAppendClient(String text){
		JTextArea area = FileExchangeView.getVisualtextarea();
		area.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		String append = "\nYOU\n			SENDING: "+text;
		area.append(append);
	}
	
	public static void textAppendServer(String text, String name){
		JTextArea area = FileExchangeView.getVisualtextarea();
		area.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		String append = "\n"+name+"\n			"+text;
		area.append(append);
	}
	
	public static boolean fileAppendServer(String text, String name){
		JTextArea area = FileExchangeView.getVisualtextarea();
		area.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		String append = "\n"+name+"\n			DOWNLOADING FILE? (yes/no): "+text;
		area.append(append);
		return false;
	}
	
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
}
