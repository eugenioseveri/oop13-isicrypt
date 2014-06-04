package gui.models;

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
		

		public ThemeChooser(){
			String name = new GlobalSettings().getTheme();
			if(name == null)System.out.println("nome thema == null");
			System.out.println("nome thema: "+name+"Controllo name() enum: "+FileTypes.NIGHTS_WATCH.name());
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
			else System.out.println("tema non trovato");
		}
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
			Color trashYellow = new Color(201,187,1);
			Color green = new Color(29,91,6);
			setFont(new Font("Verdana",Font.BOLD, 12));
			setPanelBackColor(trashYellow);
			setButtonColor(green);
			setForegroundColor(Color.yellow);
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
