package gui.controllers;
/**
 * @author Filippo Vimini
 *
 */
import static gui.views.OpenButtons.FileTypes.IMAGE;
import static gui.views.OpenButtons.FileTypes.TEXT;
import gui.views.AbstractGuiMethodSetter;
import gui.views.ISteganographyView;
import gui.views.OpenButtons;
import gui.views.StartScreenView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.StringUtils;

import algorithms.Steganography;
import algorithms.TypeConverter;

public class SteganographyController implements ISteganographyViewObserver, IGeneralViewObserver {

	private static ISteganographyView view;
	//Contains the selected image. Used like ICON.
	private File imageIcon = null;
	//Contains the selected image.
	private File imageChoosen = null;
	 //String that contains the extract text for image
	String textBorrowed;
	//String that contains the path of default icon;
	private final String STEGANOGRAPHY_BACKGROUND = "SteganographyDefaultIcon.jpg";
	private final int iconHeigth = 240;
	private final int iconWidth = 320;
	private String textDefault = null;
	
	@Override
	public void setView(ISteganographyView view){
		SteganographyController.view = view;
		SteganographyController.view.attackSteganographyViewObserver(this);
	}
	
	@Override
	public void selectImage(){
		try {
			imageIcon = new OpenButtons().fileChooser(IMAGE);
			if(imageIcon != null){
				imageChoosen = imageIcon;
				view.getIconLabel().
					setIcon(AbstractGuiMethodSetter.iconOptimizer(view.getIconLabel(), ImageIO.read(imageIcon), iconHeigth, iconWidth));
				if(!StringUtils.isEmpty(view.getTextArea().getText())){
					view.getStartButton().setEnabled(true);
				}
				view.getClearSettingButton().setEnabled(true);
			}
		} catch (IOException e) {
			view.optionPane("image not select");
		}
	}
	@Override
	public void selectText(){
		try {
			textDefault = TypeConverter.fileToString(new OpenButtons().fileChooser(TEXT));
		} catch (FileNotFoundException e) {
			view.optionPane(e);	
		}
			if(textDefault != null){
				view.getTextArea().setText("");
				view.getTextArea().append(textDefault);
				if(imageIcon != null){
					view.getStartButton().setEnabled(true);
				}
				view.getClearSettingButton().setEnabled(true);
				view.getInsertTextButton().setEnabled(false);
			}
	}
	@Override
	public void start(){
		try {
			new Steganography().messageEncrypter(imageChoosen, "png", textDefault);
			view.optionPane("Image Steganografed");		
		} catch (IOException e) {
			view.optionPane("Error occured during the Steganography process");
		}catch (IllegalArgumentException e){
			view.optionPane("The text is too long for the image");
		}
	}
	@Override
	public void findText(){
		try {
			final File iconFinder = new OpenButtons().fileChooser(IMAGE);
			if(iconFinder != null){
				view.getIconLabel().
					setIcon(AbstractGuiMethodSetter.iconOptimizer(view.getIconLabel(), ImageIO.read(iconFinder), iconHeigth, iconWidth));
				textBorrowed = new Steganography().messageFinder(iconFinder);
				if(view.getTextArea() != null)view.getTextArea().setText("");
				if(textBorrowed != null)view.getTextArea().append(textBorrowed);
				else view.optionPane("Message not found");
				//Change button status
				(view.getStartButton()).setEnabled(false);
				(view.getSelectImageButton()).setEnabled(false);
				(view.getSelectTextButton()).setEnabled(false);
				(view.getClearSettingButton()).setEnabled(true);
			}
		} catch (IOException e) {
			view.optionPane(e);		
		}
	}
	@Override
	public void clearSetting(){
		try {
			(view.getIconLabel()).
				setIcon(AbstractGuiMethodSetter.iconOptimizer(view.getIconLabel(),
						ImageIO.read(ClassLoader.getSystemResourceAsStream(STEGANOGRAPHY_BACKGROUND)), iconHeigth, iconWidth));	
		} catch (IOException e) {
			view.optionPane( e);		
		}
		view.getTextArea().setText("");
		//Change button status
		(view.getStartButton()).setEnabled(false);
		(view.getClearSettingButton()).setEnabled(false);
		(view.getSelectImageButton()).setEnabled(true);
		(view.getSelectTextButton()).setEnabled(true);
		(view.getInsertTextButton()).setEnabled(true);
		imageChoosen = null;
		textDefault = null;
		textBorrowed = null;
	}
	
	@Override
	public void showStart() {
		StartScreenView.getFrame().setVisible(true);
		StartScreenView.redraw();
	}
	@Override
	public void insertText(){
		if(StringUtils.isBlank(view.getTextArea().
					getText()))view.optionPane("None text entered");
		else{ 
			textDefault = view.getTextArea().getText();
			if(imageIcon != null){
				view.getStartButton().setEnabled(true);
			}
				view.getClearSettingButton().setEnabled(true);
				//can select test form File OR from textArea
				view.getSelectTextButton().setEnabled(false);
			}
	}
}