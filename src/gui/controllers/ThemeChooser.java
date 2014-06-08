package gui.controllers;

import gui.models.GlobalSettings;

import java.awt.Color;
import java.awt.Font;
import java.io.IOException;

/**
 * @author Filippo Vimini
 * Created 30/05/2014
 * 
 * Class that read from file a text that contain a enum ( in String form ) and set the corresponding theme
 */
public class ThemeChooser {

	// Default font of buttons
	private static Font font;
	// Default background JPanel color
	private static Color panelBackColor ;
	// Default color of buttons
	private static Color buttonColor;
	// Default foreground color of JButton
	private static Color foregroundColor;
	
	public static enum Theme {
		WINTER_IS_COMING, NIGHTS_WATCH, FIRE_AND_BLOOD, OURS_IS_THE_FURY
	}
	
	//Switch implementation
	/**
	 * Select the correct enum from file 
	 */
	public ThemeChooser(){
		String name = null;
		try {
			name = new GlobalSettings().getTheme();
		} catch (IOException e) {
			// Do nothing
		}
		if(name.equals(Theme.WINTER_IS_COMING.name())){
			setGraphic(Theme.WINTER_IS_COMING);
		}
		else if(name.equals(Theme.FIRE_AND_BLOOD.name())){
			setGraphic(Theme.FIRE_AND_BLOOD);
		}
		else if(name.equals(Theme.NIGHTS_WATCH.name())){
			setGraphic(Theme.NIGHTS_WATCH);
		}
		else if(name.equals(Theme.OURS_IS_THE_FURY.name())){
			setGraphic(Theme.OURS_IS_THE_FURY);
		}
		//Default graphic
		else{
			setGraphic(Theme.NIGHTS_WATCH);
		}
	}
	/**
	 * Switch between enum for set the correct graphic
	 * 
	 * @param theme enum that represent a String 
	 * for choose the correspondent set of color and fort
	 */
	private void setGraphic(final Theme theme){
		setFont(new Font("Verdana",Font.BOLD, 12));
		switch (theme){
		case NIGHTS_WATCH:
			setPanelBackColor(Color.DARK_GRAY);
			setButtonColor(Color.black);
			setForegroundColor(Color.white);
			break;
		case WINTER_IS_COMING:
			final Color ice = new Color(55,124,217);
			setPanelBackColor(Color.WHITE);
			setButtonColor(ice);
			setForegroundColor(Color.black);
			break;
		case FIRE_AND_BLOOD:
			final Color bloodback = new Color(138, 7, 7);
			final Color blood = new Color(128, 10, 25);
			setPanelBackColor(bloodback);
			setButtonColor(Color.black);
			setForegroundColor(blood);
			break;
		case OURS_IS_THE_FURY:
			final Color gold = new Color(207,181,59);
			final Color royalGreen = new Color(29,91,6);
			setPanelBackColor(gold);
			setButtonColor(royalGreen);
			setForegroundColor(gold);
			break;
		}	
	}
	
	//GETTERS and SETTERS
	public static Font getFont() {
		return font;
	}

	public static Color getPanelBackColor() {
		return panelBackColor;
	}

	public static Color getButtonColor() {
		return buttonColor;
	}

	public static Color getForegroundColor() {
		return foregroundColor;
	}

	public void setFont(final Font font) {
		ThemeChooser.font = font;
	}

	public void setPanelBackColor(final Color panelBackColor) {
		ThemeChooser.panelBackColor = panelBackColor;
	}

	public void setButtonColor(final Color buttonColor) {
		ThemeChooser.buttonColor = buttonColor;
	}

	public void setForegroundColor(final Color foregroundColor) {
		ThemeChooser.foregroundColor = foregroundColor;
	}
}