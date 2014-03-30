package test;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javax.swing.JFileChooser;
import javax.swing.JPanel;	//need for the interface that select the file


public class OpenButtons extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	JFileChooser fileChooser = new JFileChooser();
	File hashObj;
	BufferedInputStream stream;
	/*Gestire l'annullamento dell'aperturra file*/
	public BufferedInputStream fileChooser(){
		//this variable is used for make the execution of showOpenDialog bloker, and manage the exceprions
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

}
