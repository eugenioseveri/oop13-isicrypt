package gui;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.swing.JFileChooser;
import javax.swing.JPanel;	//need for the interface that select the file
import javax.swing.filechooser.FileNameExtensionFilter;


public class OpenButtons extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	JFileChooser fileChooser = new JFileChooser();
	File hashObj;
	BufferedInputStream stream;
	BufferedImage image;
	
	public BufferedInputStream streamChooser(){
		//this variable is used for make the execution of showOpenDialog bloker, and manage the exceptions
		int block = fileChooser.showOpenDialog(OpenButtons.this);
		hashObj = fileChooser.getSelectedFile();
		if( block == JFileChooser.CANCEL_OPTION || block == JFileChooser.ERROR_OPTION ){
			System.out.println("File non selezionato");
			/*Da Cambiare quando si inserisce nella GUI!!!!!*/
			System.exit(ABORT);
		}
		try {
			stream = new BufferedInputStream(new DataInputStream(new FileInputStream(hashObj)));
		} catch (FileNotFoundException e) {
			System.out.println("Can't stream the selected file: "+e);
		}
		return stream;		
	}
	
	public File FileChooser(){
		int controllor = fileChooser.showOpenDialog(getParent());
		File fileReturn = null;
		if(controllor == JFileChooser.APPROVE_OPTION){
			fileReturn = fileChooser.getSelectedFile();
		}
		return fileReturn;
	}
	public File imageChooser(){
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Image(*.png;*.jpg)","png","jpg"));
		int controllor = fileChooser.showOpenDialog(getParent());
		File fileReturn = null;
		if(controllor == JFileChooser.APPROVE_OPTION){
			fileReturn = fileChooser.getSelectedFile();
		}
		return fileReturn;
	}
	public File textChooser(){
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Text(*.txt)","txt"));
		int controllor = fileChooser.showOpenDialog(getParent());
		File fileReturn = null;
		if(controllor == JFileChooser.APPROVE_OPTION){
			fileReturn = fileChooser.getSelectedFile();
		}
		return fileReturn;
	}
	
	public File directoryChooser(){
		fileChooser.setCurrentDirectory(new java.io.File("."));
		fileChooser.setDialogTitle("select folder");
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fileChooser.setAcceptAllFileFilterUsed(false);
		int controllor = fileChooser.showOpenDialog(getParent());
		File fileReturn = null;
		if(controllor == JFileChooser.APPROVE_OPTION){
			fileReturn = fileChooser.getSelectedFile();
		}
		return fileReturn;
	}

}
