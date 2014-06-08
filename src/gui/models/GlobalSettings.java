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

	transient private final static String USER_HOME_PATH = System.getProperty("user.home") + "/isicrypt";
	transient private final static String SETTINGS_FILE_PATH = USER_HOME_PATH + "/globalsettings.dat";
	private static final long serialVersionUID = -3375259654271655816L;
	private String defaultTheme = "NIGHTS_WATCH";
	
	public String getTheme() {
		return defaultTheme;
	}

	public void setTheme(final String theme) {
		this.defaultTheme = theme;
	}

	/**
	 * Automatically loads the global application settings from the settings file. If it does not exists, it will be created.
	 * @throws IOException If an error occurs wrilte reading or writing the settings file
	 */
	public GlobalSettings() throws IOException {
		FileInputStream file;
		BufferedInputStream buffFile;
		ObjectInputStream objFile = null;
		try {
			file = new FileInputStream(SETTINGS_FILE_PATH);
			buffFile = new BufferedInputStream(file);
			objFile = new ObjectInputStream(buffFile);
			final GlobalSettings readGlobalSettings = (GlobalSettings)objFile.readObject();
			setTheme(readGlobalSettings.getTheme());
			storeSettings();
		} catch (FileNotFoundException e) {
			//System.err.println(MISSING_SETTINGS_FILE);
			final File userHome = new File(USER_HOME_PATH);
			if(!userHome.exists()) {
				userHome.mkdir();
			}
		} catch (IOException e) {
			throw new IOException(IO_READING_ERROR);
		} catch (ClassNotFoundException e) {
			// This exception should not occur
		} finally {
			if(objFile != null) {
				objFile.close();
			}
		}
	}
	
	/**
	 * Stores the current global application settings to the settings file
	 * @throws IOException If an error occurs while writing the settings
	 */
	final public void storeSettings() throws IOException {
		FileOutputStream file;
		BufferedOutputStream buffFile;
		ObjectOutputStream objFile = null;
		try {
			file = new FileOutputStream(SETTINGS_FILE_PATH);
			buffFile = new BufferedOutputStream(file);
			objFile = new ObjectOutputStream(buffFile);
			objFile.writeObject(this);
		} catch (IOException e) {
			throw new IOException(IO_WRITING_ERROR);
		} finally {
			if(objFile != null) {
				objFile.close();
			}
		}
	}
	
}
