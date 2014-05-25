package gui.controllers;
/**
 * @author Filippo Vimini
 *
 */
import static gui.models.OpenButtons.FileTypes.IMAGE;
import static gui.models.OpenButtons.FileTypes.TEXT;
import gui.models.OpenButtons;
import gui.views.AbstractGuiMethodSetter;
import gui.views.SteganographyView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import org.apache.commons.lang3.StringUtils;

import algorithms.Steganography;
import algorithms.TypeConverter;

public class SteganographyController extends AbstractGuiMethodSetter implements ISteganographyViewObserver {

	private SteganographyView view;
	//Contains the selected image. Used like ICON.
	private static File imageIcon = null; //Type File because steganography is ready for take another file over images
	//Contains the selected image.
	private static File imageChoosen = null;
	 //String that contains the extract text for image
	String textBorrowed;
	//String that contains the path of default icon;
	String pathDefault = "./res/SteganographyDefaultIcon.jpg";

	private static String textDefault = null;

	//JFrame for message dialog
	private static JFrame dialog = new JFrame();
	
	public void setView(SteganographyView view){
		this.view = view;
		this.view.attacSteganographyViewObserver(this);
	}
	@Override
	public void selectImage(){
		try {
			imageIcon = new OpenButtons().FileChooser(IMAGE);
			if(imageIcon != null){
				imageChoosen = imageIcon;
				((JLabel) SteganographyView.getIconLabel()).setIcon(iconOptimizer((JLabel)SteganographyView.getIconLabel(), ImageIO.read(imageIcon)));
				if(!StringUtils.isEmpty(SteganographyView.getTextArea().getText())){
					((JButton)SteganographyView.getStartButton()).setEnabled(true);
				}
				((JButton)SteganographyView.getClearSettingButton()).setEnabled(true);
			}
		} catch (IOException e) {
			System.out.println("image not select");
		}
	}
	@Override
	public void selectText(){
		try {
		textDefault = new TypeConverter().fileToString(new OpenButtons().FileChooser(TEXT));
			if(textDefault != null){
				SteganographyView.getTextArea().setText("");
				SteganographyView.getTextArea().append(textDefault);
				if(imageIcon != null){
					((JButton)SteganographyView.getStartButton()).setEnabled(true);
				}
				((JButton)SteganographyView.getClearSettingButton()).setEnabled(true);
				((JButton)SteganographyView.getInsertTextButton()).setEnabled(false);
			}
		} catch (FileNotFoundException e) {
			System.out.println("text not select");
		}
	}
	@Override
	public void start(){
		try {
			new Steganography().messageEncrypter(imageChoosen, "png", textDefault);
			JOptionPane.showMessageDialog(dialog, "Image Steganografed");
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	@Override
	public void findText(){
		try {
			File iconFinder = new OpenButtons().FileChooser(IMAGE);
			if(iconFinder != null){
				((JLabel) SteganographyView.getIconLabel()).setIcon(iconOptimizer((JLabel)SteganographyView.getIconLabel(), ImageIO.read(imageIcon)));
				textBorrowed = new Steganography().messageBorrower(iconFinder);
				if(SteganographyView.getTextArea() != null)SteganographyView.getTextArea().setText("");
				if(textBorrowed != null)SteganographyView.getTextArea().append(textBorrowed);
				else JOptionPane.showMessageDialog(dialog, "Message not found");
				(SteganographyView.getStartButton()).setEnabled(false);
				(SteganographyView.getSelectImageButton()).setEnabled(false);
				(SteganographyView.getSelectTextButton()).setEnabled(false);
				(SteganographyView.getClearSettingButton()).setEnabled(true);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void clearSetting(){
		try {
			((JLabel) SteganographyView.getIconLabel()).setIcon(iconOptimizer((JLabel)SteganographyView.getIconLabel(), ImageIO.read(new File(pathDefault))));
			SteganographyView.getTextArea().setText("");
			(SteganographyView.getStartButton()).setEnabled(false);
			(SteganographyView.getClearSettingButton()).setEnabled(false);
			(SteganographyView.getSelectImageButton()).setEnabled(true);
			(SteganographyView.getSelectTextButton()).setEnabled(true);
			(SteganographyView.getInsertTextButton()).setEnabled(true);
			imageChoosen = null;
			textDefault = null;
			textBorrowed = null;
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void insertText(){
		if(StringUtils.isBlank(SteganographyView.getTextArea().getText()))JOptionPane.showMessageDialog(dialog, "None text entered");
		else{ 
			textDefault = SteganographyView.getTextArea().getText();
			if(imageIcon != null){
				((JButton)SteganographyView.getStartButton()).addActionListener(new ActionListener() {
					
					public void actionPerformed(ActionEvent arg0) {
						try {
							new Steganography().messageEncrypter(imageIcon, "png", textDefault);
						} catch (IOException e) {
							e.printStackTrace();
							}
						}
					});
					((JButton)SteganographyView.getStartButton()).setEnabled(true);
					}
				((JButton)SteganographyView.getClearSettingButton()).setEnabled(true);
				/*
				 * you can select test form File OR from textArea
				 */
				((JButton)SteganographyView.getSelectTextButton()).setEnabled(false);
			}
	}
}
