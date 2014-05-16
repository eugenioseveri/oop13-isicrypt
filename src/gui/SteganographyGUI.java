package gui;
/**
 * @author Filippo Vimini
 * @data 25/04/2014
 * Graphic User Interface for Steganography class in Swing.
 * GridBagLayout used for make a precision and resizable GUI, for understand this class is necessary own the GridBagLayout knowledge.
 */

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import org.apache.commons.lang3.StringUtils;

import cryptography.algorithms.Steganography;
import cryptography.algorithms.TypeConverter;
import static gui.OpenButtons.FileTypes.*;

	public class SteganographyGUI {
		/*
		 * String variable that contains the text that will be selected by user.
		 */
		private static String textDefault = null;
		/*
		 * Contains the selected image. Used like ICON.
		 */
		private static File imageIcon = null; //Type File because steganography is ready for take another file over images
		/*
		 * Contains the selected image.
		 */
		private static File imageChoosen = null;
		/*
		 * String that contains the extract text for image
		 */
		String textBorrowed;
		/*
		 * String that contains the path of default icon;
		 */
		String pathDefault = "./res/SteganographyDefaultIcon.jpg";
		/*
		 * Default font for button
		 */
		private static final Font font = new Font("Verdana",Font.BOLD, 12);
		/*
		 * Default Background JPanel Color
		 */
		private static final Color panelBakColor = Color.DARK_GRAY;
		/*
		 * Default color of button
		 */
		private static final Color buttonColor = Color.black;
		/*
		 * Default foreground color of JButton
		 */
		private static final Color foregroundColor = Color.white;
		/*
		 * JFrame for message dialog
		 */
		JFrame dialog = new JFrame();
		/*
		 * Component type initialized immediately for simplify the programming, does not involve in wastage of resources
		 */
		Component selectImageButton = new JButton("Select image");
		String selectText = "Select text\nfrom File";	//Format text for JButton
		Component selectTextButton = new JButton("<html>" + selectText.replaceAll("\\n", "<br>") + "</html>");//html for new line in button
		Component separator = new JSeparator(SwingConstants.VERTICAL);
		Component iconLabel = new JLabel();
		Component encryptCheckbox = new JCheckBox("Encrypt");
		Component startButton = new JButton("START");
		JTextArea textArea = new JTextArea(10,10);
		Component scrollPane = new JScrollPane(textArea);
		Component findTextButton = new JButton("Find Text");
		Component clearSettingButton = new JButton("Clear Setting");
		Component insertTextButton = new JButton("Select enter text");
		Component filler = new JLabel();
		/**
		 * 
		 * @param contenitore	Container to change.
		 * @throws IOException
		 */
		public SteganographyGUI(Container contenitore) throws IOException{
			GridBagLayout layout = new GridBagLayout();
			GridBagConstraints limit = new GridBagConstraints();
			/*
			 * JPanel setting
			 */
			contenitore.setLayout(layout);
			contenitore.setBackground(panelBakColor);
			/*
			 * Arrays that contains various dimension of insets
			 */
			int insetsDefault[] = {10,10,10,10};
			int[] zeroInsets = {0,0,0,0};
			/*
			 * weight for ipadx and ipady
			 */
			int standardButtonIpady = 40;
			int standardButtonIpadx = -40;
			int buttonHeightDimension = 25;
			int buttonWidthDimension = 55;
			/*
			 * Component setting
			 */
			
			//JButton
			((JButton)selectImageButton).addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent arg0) {
					try {
						imageIcon = new OpenButtons().FileChooser(IMAGE);
						if(imageIcon != null){
							imageChoosen = imageIcon;
							((JLabel) iconLabel).setIcon(new GuiMethodSetter().iconOptimizer(((JLabel)iconLabel), ImageIO.read(imageIcon)));
							if(!StringUtils.isEmpty(textArea.getText())){
								((JButton)startButton).setEnabled(true);
							}
							((JButton)clearSettingButton).setEnabled(true);
						}
					} catch (IOException e) {
						System.out.println("image not select");
					}
				}
			});
			new GuiMethodSetter().setJButton(selectImageButton, buttonColor, foregroundColor, font, false, false);
			new GuiMethodSetter().setLimit(limit, 0, 0, 1, 1, 0, 0, buttonWidthDimension, standardButtonIpady, insetsDefault, GridBagConstraints.NONE, GridBagConstraints.WEST, contenitore, selectImageButton);
		
			//JButton
			((JButton)selectTextButton).setMinimumSize(((JButton)selectTextButton).getPreferredSize());
			((JButton)selectTextButton).addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					try {
						textDefault = new TypeConverter().fileToString(new OpenButtons().FileChooser(TEXT));
						if(textDefault != null){
							textArea.setText("");
							textArea.append(textDefault);
							if(imageIcon != null){
								((JButton)startButton).setEnabled(true);
							}
							((JButton)clearSettingButton).setEnabled(true);
							((JButton)insertTextButton).setEnabled(false);
						}
					} catch (FileNotFoundException e) {
						System.out.println("text not select");
					}
				}
			});
			new GuiMethodSetter().setJButton(selectTextButton, buttonColor, foregroundColor, font, false, false);
			new GuiMethodSetter().setLimit(limit, 1, 0, 1, 1, 0, 0, buttonWidthDimension + 23, buttonHeightDimension, insetsDefault, GridBagConstraints.NONE, GridBagConstraints.CENTER, contenitore, selectTextButton);
		
			//JSeparator 
			((JSeparator)separator).setBackground(Color.white);
			((JSeparator)separator).setMinimumSize(((JSeparator)separator).getPreferredSize());
			new GuiMethodSetter().setLimit(limit, 2, 0, 1, 6, 0, 1, 0, 0, zeroInsets, GridBagConstraints.BOTH, GridBagConstraints.CENTER, contenitore, separator);	
			
			//JLabel for icon
			BufferedImage defaultStartimage = ImageIO.read(new File(pathDefault));
			ImageIcon icon = new GuiMethodSetter().iconOptimizer(((JLabel) iconLabel), defaultStartimage);
			((JLabel) iconLabel).setIcon(icon);
			int insetsIcon[] = {20,10,10,10};
			new GuiMethodSetter().setLimit(limit, 3, 0, 1, 6, 0, 0, 0, 0,insetsIcon, GridBagConstraints.CENTER, GridBagConstraints.CENTER, contenitore, iconLabel);
			
			//JCheckBox
			((JCheckBox)encryptCheckbox).setBackground(Color.DARK_GRAY);
			((JCheckBox)encryptCheckbox).setForeground(Color.white);
			new GuiMethodSetter().setLimit(limit, 0, 1, 2, 1, 0, 0, 0, standardButtonIpady,insetsDefault, GridBagConstraints.NONE, GridBagConstraints.WEST, contenitore, encryptCheckbox);
			
			//JButton
			((JButton)startButton).addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent arg0) {
					try {
						new Steganography().messageEncrypter(imageChoosen, "png", textDefault);
						JOptionPane.showMessageDialog(dialog, "Image Steganografed");
					} catch (IOException e) {
						e.printStackTrace();
					}		
				}
			});
			((JButton)startButton).setEnabled(false);
			new GuiMethodSetter().setJButton(startButton, buttonColor, foregroundColor, font, false, false);
			new GuiMethodSetter().setLimit(limit, 0, 2, 2, 1, 0, 0, standardButtonIpadx, standardButtonIpady, insetsDefault, GridBagConstraints.BOTH, GridBagConstraints.CENTER, contenitore, startButton);
			
			//JScrollPane(JtextArea)
			scrollPane.setMinimumSize(scrollPane.getPreferredSize());
			/*
			 * To force JTextArea that set automatically a new line.
			 * The dimension of textArea is that because is prepared to host different type of information, like the info of the file ( different from image ) ecc..
			 */
			textArea.setLineWrap(true);
			textArea.setWrapStyleWord(true);
			((JScrollPane)scrollPane).setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			new GuiMethodSetter().setLimit(limit, 0, 7, 4, 1, 1, 0, 0, 0, insetsDefault, GridBagConstraints.BOTH, GridBagConstraints.WEST, contenitore, scrollPane);
			
			//JButton
			((JButton)findTextButton).addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent arg0) {
					try {
						File iconFinder = new OpenButtons().FileChooser(IMAGE);
						if(iconFinder != null){
							((JLabel) iconLabel).setIcon(new GuiMethodSetter().iconOptimizer(((JLabel)iconLabel), ImageIO.read(iconFinder)));
							textBorrowed = new Steganography().messageBorrower(iconFinder);
							if(textArea != null)textArea.setText("");
							if(textBorrowed != null)textArea.append(textBorrowed);
							else JOptionPane.showMessageDialog(dialog, "Message not found");
							((JButton)startButton).setEnabled(false);
							((JButton)selectImageButton).setEnabled(false);
							((JButton)selectTextButton).setEnabled(false);
							((JButton)clearSettingButton).setEnabled(true);
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
			new GuiMethodSetter().setJButton(findTextButton, buttonColor, foregroundColor, font, false, false);
			new GuiMethodSetter().setLimit(limit, 0, 3, 2, 1, 0, 0, standardButtonIpadx, standardButtonIpady, insetsDefault, GridBagConstraints.BOTH, GridBagConstraints.CENTER, contenitore, findTextButton);
			
			//JButton
			((JButton)clearSettingButton).addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent arg0) {
					try {
						((JLabel) iconLabel).setIcon(new GuiMethodSetter().iconOptimizer(((JLabel) iconLabel), ImageIO.read(new File(pathDefault))));
						textArea.setText("");
						((JButton)startButton).setEnabled(false);
						((JButton)clearSettingButton).setEnabled(false);
						((JButton)selectImageButton).setEnabled(true);
						((JButton)selectTextButton).setEnabled(true);
						((JButton)insertTextButton).setEnabled(true);
						imageChoosen = null;
						textDefault = null;
						textBorrowed = null;
						
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
			((JButton)clearSettingButton).setEnabled(false);
			new GuiMethodSetter().setJButton(clearSettingButton, buttonColor, foregroundColor, font, false, false);
			new GuiMethodSetter().setLimit(limit, 0, 4, 2, 1, 0, 0, standardButtonIpadx, standardButtonIpady, insetsDefault, GridBagConstraints.BOTH, GridBagConstraints.CENTER, contenitore, clearSettingButton);
			
			//JButton
			((JButton)insertTextButton).addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent arg0) {
					if(StringUtils.isBlank(textArea.getText()))JOptionPane.showMessageDialog(dialog, "None text entered");
					else{ 
						textDefault = textArea.getText();
						if(imageIcon != null){
							((JButton)startButton).addActionListener(new ActionListener() {
								
								public void actionPerformed(ActionEvent arg0) {
									try {
										new Steganography().messageEncrypter(imageIcon, "png", textDefault);
									} catch (IOException e) {
										e.printStackTrace();
										}
									}
								});
								((JButton)startButton).setEnabled(true);
								}
							((JButton)clearSettingButton).setEnabled(true);
							/*
							 * you can select test form File OR from textArea
							 */
							((JButton)selectTextButton).setEnabled(false);
						}
				}
				});
			int insetsTextAreaButton[] = {0,0,10,10};
			new GuiMethodSetter().setJButton(insertTextButton, buttonColor, foregroundColor, font, false, true);
			new GuiMethodSetter().setLimit(limit, 0, 6, 1, 1, 0, 0, 0, 0, insetsTextAreaButton, GridBagConstraints.NONE, GridBagConstraints.WEST, contenitore, insertTextButton);
			
			/*
			 * Filler for good buttons resizable setting
			 */
			new GuiMethodSetter().setLimit(limit, 0, 5, 2, 1, 0, 0, 0, 0, zeroInsets, GridBagConstraints.BOTH, GridBagConstraints.CENTER, contenitore, filler);
		}
}