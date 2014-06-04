package gui.models;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
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

	transient private final static String userHomePath = System.getProperty("user.home") + "\\isicrypt";
	transient private final static String settingsFilePath = userHomePath + "\\globalsettings.dat"; // TODO: Funziona multipiattaforma?
	private static final long serialVersionUID = -3375259654271655816L;
	private String defaultTheme = "NIGHTS_WATCH";
	
	public String getTheme() {
		return defaultTheme;
	}

	public void setTheme(String theme) {
		this.defaultTheme = theme;
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
			setTheme(readGlobalSettings.getTheme());
		} catch (FileNotFoundException e) {
			System.err.println(MISSING_SETTINGS_FILE);
			File userHome = new File(userHomePath);
			if(!userHome.exists()) {
				userHome.mkdir();
			}
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
