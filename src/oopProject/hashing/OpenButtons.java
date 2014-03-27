package oopProject.hashing;

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
	DataInputStream stream;
	
	public DataInputStream filChooser(){
		@SuppressWarnings("unused")
		//this variable is used for make the execution of showOpenDialog bloker
		int block = fileChooser.showOpenDialog(OpenButtons.this);
		hashObj = fileChooser.getSelectedFile();
		try {
			stream = new DataInputStream(new FileInputStream(hashObj));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return stream;		
	}

}
