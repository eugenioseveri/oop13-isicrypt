package gui.views;

import gui.controllers.ICryptographyViewObserver;
import gui.models.GlobalSettings;
import gui.models.ThemeChooser;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class CryptographyView_Filo extends AbstractGuiMethodSetter{
	private Font font;
	private Color panelBakColor;
	private Color buttonColor;
	private Color foregroundColor;
	// Arrays that contains various dimension of insets
	private static final int insetsDefault[] = { 10, 10, 10, 10 };
	private static final int insetsBigButton[] = { 10, 10, 70, 70 };
	private static final int zeroInsets []= { 0, 0, 0, 0 };
	private static final int backInsets []= { 0, 0, 10, 10 };
	private static final int labelInsets []= { 10, -10, 0, 0 };
	private static final int zeroIpad  = 0;
	private static final int noResizable  = 0;
	private static final int resizable  = 1;
	private static final int ipadDefaulty = 30;
	private static final int ipadDefaultx = 65;
	private static final int xPosition = 0;
	private static final int yPosition = 0;
	private static final int defaultCellArea = 1;
	//GUI component declaration
	//Encryption part
	private static final Component backButton = new JButton("Show Start");
	private static final Component westEncryptBorderFiller = new JButton();
	private static final Component encryptionLabel = new JLabel("Encryption");
	private static final Component encryptButton = new JButton("encrypt file");
	private static final Component encryptTextField = new JTextField();
	private static final Component wipeSource = new JCheckBox("wipe source file");
	private static final Component publicKey = new JButton("Public key");
	private static final Component publicKeyTextField = new JTextField();
	private static final Component newKeyPair = new JButton("New key pair");
	private static final Component fillerOne = new JButton();
	private static final Component algorithmLabel = new JLabel("Algorithm");
	private static final Component algorithmTextField = new JTextField();
	private static final Component hashingLabel = new JLabel("hashing");
	private static final Component hashingTextField = new JTextField();
	private static final Component compressionLabel = new JLabel("Compression");
	private static final Component compressionTextField = new JTextField();
	private static final Component fillerTwo = new JButton();
	private static final Component startEncryption = new JButton("Start Encryption");
	private static final Component statusLabel = new JLabel("Status: ");
	private static final Component progressBar = new JProgressBar();
	private static final Component statusLabel2 = new JLabel("Status: ");
	private static final Component progressBar2 = new JProgressBar();
	private static final Component eastEncryptBorderFiller = new JButton();
	//Decryption part
	private static final Component separator = new JSeparator(SwingConstants.VERTICAL);
	private static final Component westDecryptBorderFiller = new JButton();
	private static final Component decryptionLabel = new JLabel("Decryption");
	private static final Component decryptButton = new JButton("decrypt file");
	private static final Component decryptTextField = new JTextField();
	private static final Component privateKey = new JButton("Private key");
	private static final Component privateKeyTextField = new JTextField();
	private static final Component fillerThree = new JButton();
	private static final Component startDecryption = new JButton("Start Decryption");
	private static final Component fillerFour= new JButton();
	private static final Component logTextArea = new JTextArea(20,20);
	private final static Component scrollPane = new JScrollPane(logTextArea);
	private static final Component eastDecryptBorderFiller = new JButton();
	//Frame and panel
	private static final JPanel container = new JPanel();
	private GridBagConstraints limit;
	private final static JFrame dialog = new JFrame();
	private final static JFrame frame = new JFrame();
	//Observer
	private ICryptographyViewObserver controller;
	
	public void attacICryptographyViewObserver(ICryptographyViewObserver controller){
		this.controller = controller;
	}
	
	public CryptographyView_Filo(){
		buildLayout();
		componentSetting();
		setFrame();
	}
	//BuildLayout same for all view
	private void buildLayout() {
		GlobalSettings set = null;
	//	set = new GlobalSettings();
		this.setButtonColor(ThemeChooser.getButtonColor());
		this.setFont(ThemeChooser.getFont());
		this.setForegroundColor(ThemeChooser.getForegroundColor());
		this.setPanelBakColor(ThemeChooser.getPanelBackColor());
		GridBagLayout layout = new GridBagLayout();
		limit = new GridBagConstraints();
		container.setLayout(layout);
		container.setBackground(panelBakColor);
	}
	
	//Graphic draw
	private void componentSetting(){
		//JLabel ENCRYPTION LABEL
		((JLabel)encryptionLabel).setForeground(Color.WHITE);
		setLimit(limit, zeroIpad, zeroIpad, labelInsets,
				GridBagConstraints.CENTER, GridBagConstraints.CENTER, container, encryptionLabel);
		setGridposition(limit, xPosition+1, yPosition, defaultCellArea+1, defaultCellArea,
				noResizable, noResizable, container, encryptionLabel);
		//JButton BACK TO START 
		setJButton(backButton, buttonColor, foregroundColor, font, false, false);
		setLimit(limit, ipadDefaultx-50, ipadDefaulty-10, backInsets,
				GridBagConstraints.CENTER, GridBagConstraints.WEST, container, backButton);
		setGridposition(limit, xPosition+1, yPosition	, defaultCellArea, defaultCellArea,
				noResizable, noResizable, container, backButton);
		//FILLER
		((JButton)fillerThree).setEnabled(false);
		setJButton(fillerThree, panelBakColor, panelBakColor, null, false, false);
		setLimit(limit, zeroIpad, zeroIpad, zeroInsets, 
				GridBagConstraints.BOTH, GridBagConstraints.CENTER, container, fillerThree);
		setGridposition(limit, xPosition+1, yPosition+1, defaultCellArea+1, defaultCellArea,
				resizable, resizable, container, fillerThree);
		//JButton ENCRYPT BUTTON
		setJButton(encryptButton, buttonColor, foregroundColor, font, false, false);
		setLimit(limit, ipadDefaultx, ipadDefaulty, insetsDefault,
				GridBagConstraints.BOTH, GridBagConstraints.CENTER, container, encryptButton);
		setGridposition(limit, xPosition+1, yPosition+2	, defaultCellArea, defaultCellArea,
				noResizable, noResizable, container, encryptButton);
		//TextField ENCRYPT TEXTFIELD
		setLimit(limit, zeroIpad, zeroIpad, insetsDefault, 
				GridBagConstraints.HORIZONTAL, GridBagConstraints.SOUTH, container, encryptTextField);
		setGridposition(limit, xPosition+2, yPosition+2, defaultCellArea, defaultCellArea,
				resizable, noResizable, container, encryptTextField);
		//checkbox WIPE SOURCE CHECKBOX
		setLimit(limit, zeroIpad, zeroIpad, zeroInsets,
				GridBagConstraints.CENTER, GridBagConstraints.WEST, container, wipeSource);
		setGridposition(limit, xPosition+1, yPosition+4, defaultCellArea, defaultCellArea,
				noResizable, noResizable, container, wipeSource);
		//JButton PUBLIC KEY BUTTON
		setJButton(publicKey, buttonColor, foregroundColor, font, false, false);
		setLimit(limit, ipadDefaultx, ipadDefaulty, insetsDefault,
				GridBagConstraints.BOTH, GridBagConstraints.CENTER, container, publicKey);
		setGridposition(limit, xPosition+1, yPosition+5, defaultCellArea, defaultCellArea,
				noResizable, noResizable, container, publicKey);
		//TextField PUBLIC KEY TEXT FIELD
		setLimit(limit, zeroIpad, zeroIpad, insetsDefault,
				GridBagConstraints.HORIZONTAL, GridBagConstraints.SOUTH, container, publicKeyTextField);
		setGridposition(limit, xPosition+2, yPosition+5, defaultCellArea, defaultCellArea,
				resizable, noResizable, container, publicKeyTextField);
		//JButton NEW KEY PAIR BUTTON 
		setJButton(newKeyPair, buttonColor, foregroundColor, font, false, false);
		setLimit(limit, ipadDefaultx, ipadDefaulty, insetsBigButton,
				GridBagConstraints.BOTH, GridBagConstraints.CENTER, container, newKeyPair);
		setGridposition(limit, xPosition+1, yPosition+7, defaultCellArea+1, defaultCellArea,
				resizable, noResizable, container, newKeyPair);
		//Filler 
		((JButton)fillerOne).setEnabled(false);
	//	((JButton)fillerOne).setVisible(false);
		setJButton(fillerOne, panelBakColor, panelBakColor, null, false, false);
		setLimit(limit, zeroIpad, zeroIpad, zeroInsets, 
				GridBagConstraints.BOTH, GridBagConstraints.CENTER, container, fillerOne);
		setGridposition(limit, xPosition+1, yPosition+8, defaultCellArea+1, defaultCellArea,
				resizable, resizable, container, fillerOne);
		//JLabel ALGORITHM LABLE
		((JLabel)algorithmLabel).setForeground(Color.WHITE);
		setLimit(limit, zeroIpad, zeroIpad, insetsDefault,
				GridBagConstraints.CENTER, GridBagConstraints.EAST, container, algorithmLabel);
		setGridposition(limit, xPosition+1, yPosition+9, defaultCellArea, defaultCellArea,
				noResizable, noResizable, container, algorithmLabel);
		//textFielf ALGORITHM TEXT FIELD
		setLimit(limit, zeroIpad, zeroIpad, insetsDefault,
				GridBagConstraints.HORIZONTAL, GridBagConstraints.EAST, container, algorithmTextField);
		setGridposition(limit, xPosition+2, yPosition+9, defaultCellArea, defaultCellArea,
				resizable, noResizable, container, algorithmTextField);
		//JLabel HASHING LABEL
		((JLabel)hashingLabel).setForeground(Color.WHITE);
		setLimit(limit, zeroIpad, zeroIpad, insetsDefault,
				GridBagConstraints.CENTER, GridBagConstraints.EAST, container, hashingLabel );
		setGridposition(limit, xPosition+1, yPosition+11, defaultCellArea, defaultCellArea,
				noResizable, noResizable, container, hashingLabel);
		//TextField HASHING TEXT FIELD
		setLimit(limit, zeroIpad, zeroIpad, insetsDefault,
				GridBagConstraints.HORIZONTAL, GridBagConstraints.EAST, container, hashingTextField);
		setGridposition(limit, xPosition+2, yPosition+11, defaultCellArea, defaultCellArea,
				resizable, noResizable, container, hashingTextField);
		//JLabel COMPRESSION LABEL
		((JLabel)compressionLabel).setForeground(Color.WHITE);
		setLimit(limit, zeroIpad, zeroIpad, insetsDefault,
				GridBagConstraints.CENTER, GridBagConstraints.EAST, container, compressionLabel);
		setGridposition(limit, xPosition+1, yPosition+12, defaultCellArea, defaultCellArea,
				noResizable, noResizable, container, compressionLabel);
		//TextField COMPRESSION TEXT FIELD
		setLimit(limit, zeroIpad, zeroIpad, insetsDefault,
				GridBagConstraints.HORIZONTAL, GridBagConstraints.EAST, container, compressionTextField);
		setGridposition(limit, xPosition+2, yPosition+12, defaultCellArea, defaultCellArea,
				resizable, noResizable, container, compressionTextField);
		//filler
		((JButton)fillerTwo).setEnabled(false);
		setJButton(fillerTwo, panelBakColor, panelBakColor, null, false, false);
		setLimit(limit, zeroIpad, zeroIpad, zeroInsets,
				GridBagConstraints.BOTH, GridBagConstraints.CENTER, container, fillerTwo);
		setGridposition(limit, xPosition+1, yPosition+13, defaultCellArea+1, defaultCellArea,
				resizable, resizable, container, fillerTwo);
		//JButton START ENCRYPTION
		setJButton(startEncryption, buttonColor, foregroundColor, font, false, false);
		setLimit(limit, ipadDefaultx, ipadDefaulty, insetsBigButton, 
				GridBagConstraints.BOTH, GridBagConstraints.EAST, container, startEncryption);
		setGridposition(limit, xPosition+1, yPosition+14, defaultCellArea+1, defaultCellArea,
				resizable, noResizable, container, startEncryption);
		//Status LABEL
		((JLabel)statusLabel).setForeground(Color.WHITE);
		setLimit(limit, zeroIpad, zeroIpad, insetsDefault,
				GridBagConstraints.CENTER, GridBagConstraints.CENTER, container, statusLabel);
		setGridposition(limit, xPosition+1, yPosition+15, defaultCellArea, defaultCellArea, 
				noResizable, noResizable, container, statusLabel);
		//encryption PROGRESS BAR
		setLimit(limit, zeroIpad, zeroIpad, insetsDefault,
				GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER, container, progressBar);
		setGridposition(limit, xPosition+2, yPosition+15, defaultCellArea, defaultCellArea,
				resizable, noResizable, container, progressBar);
		
		//DECRYPTION
		
		//SEPARATOR
		setLimit(limit, zeroIpad, zeroIpad, zeroInsets, 
				GridBagConstraints.BOTH, GridBagConstraints.CENTER, container, separator);
		setGridposition(limit, xPosition+3, yPosition, defaultCellArea, defaultCellArea+15,
				noResizable, resizable, container, separator);
		//decryption LABEL
		((JLabel)decryptionLabel).setForeground(Color.WHITE);
		setLimit(limit, zeroIpad, zeroIpad, labelInsets,
				GridBagConstraints.CENTER, GridBagConstraints.CENTER, container, decryptionLabel);
		setGridposition(limit, xPosition+4, yPosition, defaultCellArea+2, defaultCellArea,
				noResizable, noResizable, container, decryptionLabel);
		//JButton select FILE DECRYPT
		setJButton(decryptButton, buttonColor, foregroundColor, font, false, false);
		setLimit(limit, ipadDefaultx, ipadDefaulty, insetsDefault,
				GridBagConstraints.BOTH, GridBagConstraints.CENTER, container, decryptButton);
		setGridposition(limit, xPosition+4, yPosition+2	, defaultCellArea, defaultCellArea,
				noResizable, noResizable, container, decryptButton);
		//TextField  DECRYPT FIELD
		setLimit(limit, zeroIpad, zeroIpad, insetsDefault, 
				GridBagConstraints.HORIZONTAL, GridBagConstraints.SOUTH, container, decryptTextField);
		setGridposition(limit, xPosition+5, yPosition+2, defaultCellArea+1, defaultCellArea,
				noResizable, noResizable, container, decryptTextField);
		//JButton PRIVATE KEY
		setJButton(privateKey, buttonColor, foregroundColor, font, false, false);
		setLimit(limit, ipadDefaultx, ipadDefaulty, insetsDefault,
				GridBagConstraints.BOTH, GridBagConstraints.CENTER, container, privateKey);
		setGridposition(limit, xPosition+4, yPosition+5, defaultCellArea, defaultCellArea,
				noResizable, noResizable, container, privateKey);
		//textField PRIVATE KEY FIELD
		setLimit(limit, zeroIpad, zeroIpad, insetsDefault,
				GridBagConstraints.HORIZONTAL, GridBagConstraints.SOUTH, container, privateKeyTextField);
		setGridposition(limit, xPosition+5, yPosition+5, defaultCellArea+1, defaultCellArea,
				resizable, noResizable, container, privateKeyTextField);
		//Jbutton STAR DECRYPTION
		setJButton(startDecryption, buttonColor, foregroundColor, font, false, false);
		setLimit(limit, ipadDefaultx, ipadDefaulty, insetsBigButton,
				GridBagConstraints.BOTH, GridBagConstraints.CENTER, container, startDecryption);
		setGridposition(limit, xPosition+4, yPosition+7, defaultCellArea+2, defaultCellArea,
				noResizable, noResizable, container, startDecryption);
		//Status LABEL
		((JLabel)statusLabel2).setForeground(Color.WHITE);
		setLimit(limit, zeroIpad, zeroIpad, insetsDefault,
				GridBagConstraints.CENTER, GridBagConstraints.NORTH, container, statusLabel2);
		setGridposition(limit, xPosition+4, yPosition+8, defaultCellArea, defaultCellArea, 
				noResizable, noResizable, container, statusLabel2);
		//PROGRESS BAR
		setLimit(limit, zeroIpad, zeroIpad, insetsDefault,
				GridBagConstraints.HORIZONTAL, GridBagConstraints.NORTH, container, progressBar2);
		setGridposition(limit, xPosition+5, yPosition+8, defaultCellArea, defaultCellArea,
				resizable, noResizable, container, progressBar2);
		//TextArea LOG TEXT AREA
		((JTextArea) logTextArea).setLineWrap(true);//Format text on TextArea
		((JTextArea) logTextArea).setWrapStyleWord(true);	//Format text on TextArea
		scrollPane.setPreferredSize(((JTextArea) logTextArea).getSize());
		((JScrollPane) scrollPane).setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); 	//Only vertical scroll bar
		setLimit(limit, zeroIpad, zeroIpad, insetsDefault,
				GridBagConstraints.BOTH, GridBagConstraints.CENTER, container, scrollPane);
		setGridposition(limit, xPosition+4, yPosition+9, defaultCellArea+1, defaultCellArea+6,
				resizable, resizable, container, scrollPane);
		
	}
	
	private void setFrame() {
		try {
			frame.setIconImage(ImageIO.read(new File(
					"./res/isiCryptICON_MetroStyle.jpg")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		frame.setTitle("Cryptography");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(920, 640);
		frame.getContentPane().add(container);
		frame.setVisible(true);
	}
	
	//Getters and setters
	private void setPanelBakColor(Color panelBakColor) {
		this.panelBakColor = panelBakColor;
	}

	private void setForegroundColor(Color foregroundColor) {
		this.foregroundColor = foregroundColor;
	}

	private void setFont(Font font) {
		this.font = font;
	}

	private void setButtonColor(Color buttonColor) {
		this.buttonColor = buttonColor;
	}

	public static Component getWestencryptborderfiller() {
		return westEncryptBorderFiller;
	}

	public static Component getEncryptionlabel() {
		return encryptionLabel;
	}

	public static Component getEncryptbutton() {
		return encryptButton;
	}

	public static Component getEncrypttextfield() {
		return encryptTextField;
	}

	public static Component getWipesource() {
		return wipeSource;
	}

	public static Component getPublickey() {
		return publicKey;
	}

	public static Component getPublickeytextfield() {
		return publicKeyTextField;
	}

	public static Component getNewkeypair() {
		return newKeyPair;
	}

	public static Component getFilleone() {
		return fillerOne;
	}

	public static Component getAlgorithmlabel() {
		return algorithmLabel;
	}

	public static Component getAlgorithmtextfield() {
		return algorithmTextField;
	}

	public static Component getHashingtextfield() {
		return hashingTextField;
	}

	public static Component getCompressionlabel() {
		return compressionLabel;
	}

	public static Component getCompressiontextfield() {
		return compressionTextField;
	}

	public static Component getFilletwo() {
		return fillerTwo;
	}

	public static Component getStartencryption() {
		return startEncryption;
	}

	public static Component getStatuslabel() {
		return statusLabel;
	}

	public static Component getProgressbar() {
		return progressBar;
	}

	public static Component getEastencryptborderfiller() {
		return eastEncryptBorderFiller;
	}

	public static Component getSeparator() {
		return separator;
	}

	public static Component getWestdecryptborderfiller() {
		return westDecryptBorderFiller;
	}

	public static Component getDecryptionlabel() {
		return decryptionLabel;
	}

	public static Component getDecryptbutton() {
		return decryptButton;
	}

	public static Component getDecrypttextfield() {
		return decryptTextField;
	}

	public static Component getPrivatekey() {
		return privateKey;
	}

	public static Component getPrivatekeytextfield() {
		return privateKeyTextField;
	}

	public static Component getFillethree() {
		return fillerThree;
	}

	public static Component getStartdecryption() {
		return startDecryption;
	}

	public static Component getFillefour() {
		return fillerFour;
	}

	public static Component getLogtextarea() {
		return logTextArea;
	}

	public static Component getEastdecryptborderfiller() {
		return eastDecryptBorderFiller;
	}

	public static JFrame getDialog() {
		return dialog;
	}
}
