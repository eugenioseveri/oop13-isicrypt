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

/**
 * This class loads and stores the application's common settings (settings used by multiple GUIs).
 * @author Eugenio Severi
 *
 */
public class GlobalSettings implements Serializable {

	transient private final static String settingsFilePath = "./globalsettings.dat";
	private static final long serialVersionUID = -3375259654271655816L;
	// Default font for button
	private Font font = new Font("Verdana",Font.BOLD, 12);
	// Default Background JPanel Color
	private Color panelBakColor = Color.DARK_GRAY;
	// Default color of button
	private Color buttonColor = Color.black;
	// Default foreground color of JButton
	private Color foregroundColor = Color.white;
	
	public Font getFont() {
		return font;
	}

	public void setFont(Font font) {
		this.font = font;
	}

	public Color getPanelBakColor() {
		return panelBakColor;
	}

	public void setPanelBakColor(Color panelBakColor) {
		this.panelBakColor = panelBakColor;
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
	
	// The constructor loads the settings from the file automatically
	public GlobalSettings() throws FileNotFoundException, IOException, ClassNotFoundException {
		FileInputStream file = new FileInputStream(settingsFilePath);
		BufferedInputStream buffFile = new BufferedInputStream(file);
		ObjectInputStream objFile = new ObjectInputStream(buffFile);
		GlobalSettings readGlobalSettings = (GlobalSettings)objFile.readObject();
		setFont(readGlobalSettings.getFont());
		setPanelBakColor(readGlobalSettings.getPanelBakColor());
		setButtonColor(readGlobalSettings.getButtonColor());
		setForegroundColor(readGlobalSettings.getForegroundColor());
		objFile.close();
	}
	
	// Questo metodo è molto simile a quello in FileInterpret...
	public void storeSettings() throws FileNotFoundException, IOException {
		FileOutputStream file = new FileOutputStream(settingsFilePath);
		BufferedOutputStream buffFile = new BufferedOutputStream(file);
		ObjectOutputStream objFile = new ObjectOutputStream(buffFile);
		objFile.writeObject(this);
		objFile.close();
	}
}
