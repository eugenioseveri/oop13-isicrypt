package gui.models;

import java.awt.Color;
import java.awt.Font;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import static algorithms.ErrorMessages.*;

/**
 * This class loads and stores the application's common settings (settings used by multiple GUIs).
 * @author Eugenio Severi
 *
 */
public class GlobalSettings implements Serializable {

	transient private final static String settingsFilePath = System.getProperty("user.home") + "\\.isicrypt\\globalsettings.dat"; // TODO: Funziona multipiattaforma?
	private static final long serialVersionUID = -3375259654271655816L;
	// Default font of buttons
	private Font font = new Font("Verdana",Font.BOLD, 12);
	// Default background JPanel color
	private Color panelBackColor = Color.DARK_GRAY;
	// Default color of buttons
	private Color buttonColor = Color.black;
	// Default foreground color of JButton
	private Color foregroundColor = Color.white;
	
	public Font getFont() {
		return font;
	}

	public void setFont(Font font) {
		this.font = font;
	}

	public Color getPanelBakColor() { //TODO: rinominare in "back"
		return panelBackColor;
	}

	public void setPanelBakColor(Color panelBakColor) { //TODO: rinominare in "back"
		this.panelBackColor = panelBakColor;
	}

	public Color getButtonColor() {
		return buttonColor;
	}

	public void setButtonColor(Color buttonColor) {
		this.buttonColor = buttonColor;
	}

	public Color getForegroundColor() {
		return foregroundColor;
	}

	public void setForegroundColor(Color foregroundColor) {
		this.foregroundColor = foregroundColor;
	}
	
	/**
	 * Automatically loads the global application settings from the settings file
	 */
	public GlobalSettings() {
		FileInputStream file;
		BufferedInputStream buffFile;
		ObjectInputStream objFile = null;
		try {
			file = new FileInputStream(settingsFilePath);
			buffFile = new BufferedInputStream(file);
			objFile = new ObjectInputStream(buffFile);
			GlobalSettings readGlobalSettings = (GlobalSettings)objFile.readObject();
			setFont(readGlobalSettings.getFont());
			setPanelBakColor(readGlobalSettings.getPanelBakColor());
			setButtonColor(readGlobalSettings.getButtonColor());
			setForegroundColor(readGlobalSettings.getForegroundColor());
		} catch (FileNotFoundException e) {
			System.err.println(MISSING_SETTINGS_FILE);
			storeSettings();
		} catch (IOException e) {
			System.err.println(IO_READING_ERROR);
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			if(objFile != null) {
				try {
					objFile.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Stores the current global application settings to the settings file
	 */
	// Questo metodo è molto simile a quello in FileInterpret...
	public void storeSettings() {
		FileOutputStream file;
		BufferedOutputStream buffFile;
		ObjectOutputStream objFile = null;
		try {
			file = new FileOutputStream(settingsFilePath);
			buffFile = new BufferedOutputStream(file);
			objFile = new ObjectOutputStream(buffFile);
			objFile.writeObject(this);
		} catch (IOException e) {
			System.err.println(IO_WRITING_ERROR);
			e.printStackTrace();
		} finally {
			try {
				objFile.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
