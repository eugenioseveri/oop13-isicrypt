package gui.controllers;
/**
 * @author Filippo Vimini
 *
 */
import static gui.models.OpenButtons.FileTypes.IMAGE;
import static gui.models.OpenButtons.FileTypes.TEXT;
import gui.models.OpenButtons;
import gui.views.AbstractGuiMethodSetter;
import gui.views.FileExchangeView;
import gui.views.StartScreenView;
import gui.views.SteganographyView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import org.apache.commons.lang3.StringUtils;

import algorithms.Steganography;
import algorithms.TypeConverter;

public class SteganographyController implements ISteganographyViewObserver, IGeneralViewObserver {

	private static SteganographyView view;
	//Contains the selected image. Used like ICON.
	private static File imageIcon = null; //Type File because steganography is ready for take another file over images
	//Contains the selected image.
	private static File imageChoosen = null;
	 //String that contains the extract text for image
	String textBorrowed;
	//String that contains the path of default icon;
	private static final String STEGANOGRAPHY_BACKGROUND = "./SteganographyDefaultIcon.jpg";
	private static final int iconHeigth = 240;
	private static final int iconWidth = 320;
	private static String textDefault = null;
	
	public void setView(SteganographyView view){
		SteganographyController.view = view;
		SteganographyController.view.attacSteganographyViewObserver(this);
	}
	
	@Override
	public void selectImage(){
		try {
			imageIcon = new OpenButtons().fileChooser(IMAGE);
			if(imageIcon != null){
				imageChoosen = imageIcon;
				((JLabel) SteganographyView.getIconLabel()).
					setIcon(AbstractGuiMethodSetter.iconOptimizer((JLabel)SteganographyView.getIconLabel(), ImageIO.read(imageIcon), iconHeigth, iconWidth));
				if(!StringUtils.isEmpty(SteganographyView.getTextArea().getText())){
					((JButton)SteganographyView.getStartButton()).setEnabled(true);
				}
				((JButton)SteganographyView.getClearSettingButton()).setEnabled(true);
			}
		} catch (IOException e) {
			// TODO JOptionPane
			System.out.println("image not select");
		}
	}
	@Override
	public void selectText(){
		new TypeConverter();
		try {
			textDefault = TypeConverter.fileToString(new OpenButtons().fileChooser(TEXT));
		} catch (FileNotFoundException e) {
			FileExchangeView.optionPanel(e);	
		}
			if(textDefault != null){
				SteganographyView.getTextArea().setText("");
				SteganographyView.getTextArea().append(textDefault);
				if(imageIcon != null){
					((JButton)SteganographyView.getStartButton()).setEnabled(true);
				}
				((JButton)SteganographyView.getClearSettingButton()).setEnabled(true);
				((JButton)SteganographyView.getInsertTextButton()).setEnabled(false);
			}
	}
	@Override
	public void start(){
		try {
			new Steganography().messageEncrypter(imageChoosen, "png", textDefault);
		} catch (IOException e) {
			FileExchangeView.optionPanel(e);	
		}
		JOptionPane.showMessageDialog(SteganographyView.getDialog(), "Image Steganografed");		
	}
	@Override
	public void findText(){
		try {
			File iconFinder = new OpenButtons().fileChooser(IMAGE);
			if(iconFinder != null){
				((JLabel) SteganographyView.getIconLabel()).
					setIcon(AbstractGuiMethodSetter.iconOptimizer((JLabel)SteganographyView.getIconLabel(), ImageIO.read(imageIcon), iconHeigth, iconWidth));
				textBorrowed = new Steganography().messageBorrower(iconFinder);
				if(SteganographyView.getTextArea() != null)SteganographyView.getTextArea().setText("");
				if(textBorrowed != null)SteganographyView.getTextArea().append(textBorrowed);
				else JOptionPane.showMessageDialog(SteganographyView.getDialog(), "Message not found");
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
			((JLabel) SteganographyView.getIconLabel()).
				setIcon(AbstractGuiMethodSetter.iconOptimizer((JLabel)SteganographyView.getIconLabel(), ImageIO.read(ClassLoader.getSystemResource(STEGANOGRAPHY_BACKGROUND)), iconHeigth, iconWidth));	
		} catch (IOException e) {
			e.printStackTrace();
		}
		SteganographyView.getTextArea().setText("");
		(SteganographyView.getStartButton()).setEnabled(false);
		(SteganographyView.getClearSettingButton()).setEnabled(false);
		(SteganographyView.getSelectImageButton()).setEnabled(true);
		(SteganographyView.getSelectTextButton()).setEnabled(true);
		(SteganographyView.getInsertTextButton()).setEnabled(true);
		imageChoosen = null;
		textDefault = null;
		textBorrowed = null;
		
	}
	
	@Override
	public void showStart() {
		StartScreenView.getFrame().setVisible(true);
		StartScreenView.redraw();
	}
	
	public void insertText(){
		if(StringUtils.isBlank(SteganographyView.getTextArea().
					getText()))JOptionPane.showMessageDialog(SteganographyView.getDialog(), "None text entered");
		else{ 
			textDefault = SteganographyView.getTextArea().getText();
			if(imageIcon != null){
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