package gui.models;
/**
 * @author Filippo Vimini 
 */
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;


import javax.swing.JFileChooser;
import javax.swing.JPanel;	//need for the interface that select the file
import javax.swing.filechooser.FileNameExtensionFilter;


public class OpenButtons extends JPanel{
	
	/**
	 * 
	 */
	public static enum FileTypes {
		GENERIC_FILE, IMAGE, TEXT, DIRECTORY;
	}
	private static final long serialVersionUID = 1L;
	
	JFileChooser fileChooser = new JFileChooser();
	File hashObj;
	BufferedInputStream stream;
	BufferedImage image;
	
	public File FileChooser(FileTypes fileType) {
			fileChooser.setAcceptAllFileFilterUsed(false);			
		switch( fileType ){
		case IMAGE: 	
			fileChooser.setDialogTitle("select Image");
			fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Image(*.png;*.jpg)","png","jpg"));
			break;
		case TEXT:
			fileChooser.setDialogTitle("select Text");
			fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Text(*.txt)","txt"));
			break;
		case DIRECTORY: 
			fileChooser.setDialogTitle("select Folder");
			fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		case GENERIC_FILE:
			fileChooser.setAcceptAllFileFilterUsed(true);		
			break;
		}
		int controller = fileChooser.showOpenDialog(getParent());
		File fileReturn = null;
		if(controller == JFileChooser.APPROVE_OPTION){
			fileReturn = fileChooser.getSelectedFile();
			return fileReturn;
		}
		return null;
	}	
}
