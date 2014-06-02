package gui.models;

import java.awt.Color;
import java.awt.Font;

public class ThemeChooser {
	
	public static enum FileTypes {
		WINTER_IS_COMING, NIGHT_WATCH, FIRE_AND_BLOOD, OURS_IS_THE_FURY
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
			System.out.println("nome thema: "+name+"Controllo name() enum: "+FileTypes.NIGHT_WATCH.name());
			if(name.equals(FileTypes.WINTER_IS_COMING.name())){
				setGraphic(FileTypes.WINTER_IS_COMING);
			}
			else if(name.equals(FileTypes.FIRE_AND_BLOOD.name())){
				setGraphic(FileTypes.FIRE_AND_BLOOD);
			}
			else if(name.equals(FileTypes.NIGHT_WATCH.name())){
				setGraphic(FileTypes.NIGHT_WATCH);
			}
			else if(name.equals(FileTypes.OURS_IS_THE_FURY.name())){
				setGraphic(FileTypes.OURS_IS_THE_FURY);
			}
			else System.out.println("tema non trovato");
		}
	private void setGraphic(FileTypes theme){
		switch (theme){
		case NIGHT_WATCH:
			setFont(new Font("Verdana",Font.BOLD, 12));
			setPanelBackColor(Color.DARK_GRAY);
			setButtonColor(Color.black);
			setForegroundColor(Color.white);
			break;
		case WINTER_IS_COMING:
			setFont(new Font("Verdana",Font.BOLD, 12));
			setPanelBackColor(Color.WHITE);
			setButtonColor(Color.blue);
			setForegroundColor(Color.white);
			break;
		case FIRE_AND_BLOOD:
			Color blood = new Color(128, 3, 15);
			setFont(new Font("Verdana",Font.BOLD, 12));
			setPanelBackColor(blood);
			setButtonColor(Color.black);
			setForegroundColor(blood);
			break;
		case OURS_IS_THE_FURY:
			setFont(new Font("Verdana",Font.BOLD, 12));
			setPanelBackColor(Color.yellow);
			setButtonColor(Color.black);
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
