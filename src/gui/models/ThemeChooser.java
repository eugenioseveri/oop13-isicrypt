package gui.models;
/**
 * @author Filippo Vimini
 * Created 30/05/2014
 * 
 * Class that read from file a text that contain a enum ( in String form ) and set the corresponding theme
 */
import java.awt.Color;
import java.awt.Font;

public class ThemeChooser {
	
	public static enum FileTypes {
		WINTER_IS_COMING, NIGHTS_WATCH, FIRE_AND_BLOOD, OURS_IS_THE_FURY
	}
	// Default font of buttons
		private static Font font;
		// Default background JPanel color
		private static Color panelBackColor ;
		// Default color of buttons
		private static Color buttonColor;
		// Default foreground color of JButton
		private static Color foregroundColor;
		
		//Switch implementation
		/**
		 * Select the correct enum from file 
		 */
		public ThemeChooser(){
			String name = new GlobalSettings().getTheme();
			if(name.equals(FileTypes.WINTER_IS_COMING.name())){
				setGraphic(FileTypes.WINTER_IS_COMING);
			}
			else if(name.equals(FileTypes.FIRE_AND_BLOOD.name())){
				setGraphic(FileTypes.FIRE_AND_BLOOD);
			}
			else if(name.equals(FileTypes.NIGHTS_WATCH.name())){
				setGraphic(FileTypes.NIGHTS_WATCH);
			}
			else if(name.equals(FileTypes.OURS_IS_THE_FURY.name())){
				setGraphic(FileTypes.OURS_IS_THE_FURY);
			}
			//Default graphic
			else setGraphic(FileTypes.NIGHTS_WATCH);;
		}
		/**
		 * Switch between enum for set the correct graphyc
		 * 
		 * @param theme enum that represent a String for choose the correspondent set of color and fort
		 */
	private void setGraphic(FileTypes theme){
		switch (theme){
		case NIGHTS_WATCH:
			setFont(new Font("Verdana",Font.BOLD, 12));
			setPanelBackColor(Color.DARK_GRAY);
			setButtonColor(Color.black);
			setForegroundColor(Color.white);
			break;
		case WINTER_IS_COMING:
			Color ice = new Color(55,124,217);
			setFont(new Font("Verdana",Font.BOLD, 12));
			setPanelBackColor(Color.WHITE);
			setButtonColor(ice);
			setForegroundColor(Color.black);
			break;
		case FIRE_AND_BLOOD:
			Color bloodback = new Color(138, 7, 7);
			Color blood = new Color(128, 10, 25);
			setFont(new Font("Verdana",Font.BOLD, 12));
			setPanelBackColor(bloodback);
			setButtonColor(Color.black);
			setForegroundColor(blood);
			break;
		case OURS_IS_THE_FURY:
			Color gold = new Color(207,181,59);
			Color royalGreen = new Color(29,91,6);
			setFont(new Font("Verdana",Font.BOLD, 12));
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

	public void setFont(Font font) {
		ThemeChooser.font = font;
	}

	public void setPanelBackColor(Color panelBackColor) {
		ThemeChooser.panelBackColor = panelBackColor;
	}

	public void setButtonColor(Color buttonColor) {
		ThemeChooser.buttonColor = buttonColor;
	}

	public void setForegroundColor(Color foregroundColor) {
		ThemeChooser.foregroundColor = foregroundColor;
	}

}
