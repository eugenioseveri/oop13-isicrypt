package gui.views;
/**
 * @author Filippo Vimini 
 */
import java.io.File;


import javax.swing.JFileChooser;
import javax.swing.JPanel;	//need for the interface that select the file
import javax.swing.filechooser.FileNameExtensionFilter;


public class OpenButtons extends JPanel{
	/**
	 * 
	 * @author Filippo
	 *Enum that represent the theme that the user can set
	 */
	public static enum Theme {
		GENERIC_FILE, IMAGE, TEXT, DIRECTORY;
	}
	private static final long serialVersionUID = 1L;
	
	JFileChooser selectFile = new JFileChooser();
	/**
	 * Switch from enum for choose the correct file to open from file system
	 *  and return the selected file on file system 
	 * 
	 * @param themes 		enum to be checked
	 * @return File			selected file
	 */
	public File fileChooser(Theme themes) {
			selectFile.setAcceptAllFileFilterUsed(false);			
		switch( themes ){
		case IMAGE: 	
			selectFile.setDialogTitle("select Image");
			selectFile.addChoosableFileFilter(new FileNameExtensionFilter("Image(*.png;*.jpg)","png","jpg"));
			break;
		case TEXT:
			selectFile.setDialogTitle("select Text");
			selectFile.addChoosableFileFilter(new FileNameExtensionFilter("Text(*.txt)","txt"));
			break;
		case DIRECTORY: 
			selectFile.setDialogTitle("select Folder");
			selectFile.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		case GENERIC_FILE:
			selectFile.setAcceptAllFileFilterUsed(true);		
			break;
		}
		final int controller = selectFile.showOpenDialog(getParent());
		File fileReturn = null;
		if(controller == JFileChooser.APPROVE_OPTION){
			fileReturn = selectFile.getSelectedFile();
			return fileReturn;
		}
		return null;
	}	
}
