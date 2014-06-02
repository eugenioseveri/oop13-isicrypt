package gui.views;

import gui.controllers.ICryptographyViewObserver;
import gui.models.ThemeChooser;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import algorithms.EnumAvailableCompressionAlgorithms;
import algorithms.EnumAvailableHashingAlgorithms;
import algorithms.EnumAvailableSymmetricAlgorithms;

public class CryptographyView extends AbstractGuiMethodSetter implements ICryptographyView { // TODO: si può non estendere JFrame?
	private Font font;
	private Color panelBackColor;
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
	//private static final Component westEncryptBorderFiller = new JButton();
	private static final Component encryptionLabel = new JLabel("Encryption");
	private static final Component fileToEncryptButton = new JButton("File to encrypt:");
	private static final Component encryptTextField = new JTextField();
	private static final Component wipeSource = new JLabel("Source file wiping passages:");
	private static final Component wipingComboBox = new JComboBox<Integer>();
	private static final Component publicKeyButton = new JButton("Public key:");
	private static final Component publicKeyTextField = new JTextField();
	private static final Component newKeyPairButton = new JButton("Generate new key pair");
	private static final Component fillerOne = new JButton();
	private static final Component algorithmLabel = new JLabel("Algorithm:");
	private static final Component algorithmComboBox = new JComboBox<EnumAvailableSymmetricAlgorithms>();
	private static final Component hashingLabel = new JLabel("Hashing:");
	private static final Component hashingComboBox = new JComboBox<EnumAvailableHashingAlgorithms>();
	private static final Component compressionLabel = new JLabel("Compression:");
	private static final Component compressionComboBox = new JComboBox<EnumAvailableCompressionAlgorithms>();
	private static final Component fillerTwo = new JButton();
	private static final Component startEncryptionButton = new JButton("Start Encryption");
	private static final Component statusLabelEncryption = new JLabel("Status: ");
	private static final Component progressBarEncryption = new JProgressBar();
	private static final Component statusLabelDecryption = new JLabel("Status: ");
	private static final Component progressBarDecryption = new JProgressBar();
	//private static final Component eastEncryptBorderFiller = new JButton();
	//Decryption part
	private static final Component separator = new JSeparator(SwingConstants.VERTICAL);
	//private static final Component westDecryptBorderFiller = new JButton();
	private static final Component decryptionLabel = new JLabel("Decryption");
	private static final Component fileToDecryptButton = new JButton("File to decrypt:");
	private static final Component decryptTextField = new JTextField();
	private static final Component privateKeyButton = new JButton("Private key:");
	private static final Component privateKeyTextField = new JTextField();
	private static final Component fillerThree = new JButton();
	private static final Component startDecryptionButton = new JButton("Start Decryption");
	//private static final Component fillerFour= new JButton();
	private static final Component logTextArea = new JTextArea(20,20);
	private final static Component scrollPane = new JScrollPane(logTextArea);
	//private static final Component eastDecryptBorderFiller = new JButton();
	//Frame and panel
	private static final JPanel container = new JPanel();
	private GridBagConstraints limit;
	//private final static JFrame dialog = new JFrame();
	private final static JFrame frame = new JFrame();
	//Observer
	private ICryptographyViewObserver controller;
	
	public void attachViewObserver(ICryptographyViewObserver listener){
		this.controller = listener;
	}
	
	public CryptographyView(){
		buildLayout();
		componentSetting();
		setFrame();
		setHandlers();
	}
	//BuildLayout same for all view
	private void buildLayout() {
		//GlobalSettings set = null;
	//	set = new GlobalSettings();
		this.setButtonColor(ThemeChooser.getButtonColor());
		this.setFont(ThemeChooser.getFont());
		this.setForegroundColor(ThemeChooser.getForegroundColor());
		this.setPanelBackColor(ThemeChooser.getPanelBackColor());
		GridBagLayout layout = new GridBagLayout();
		limit = new GridBagConstraints();
		container.setLayout(layout);
		container.setBackground(panelBackColor);
	}
	
	//Graphic draw
	@SuppressWarnings("unchecked") // Attenzione! (ma non dovrebbero verificarsi errori) (anche sotto)
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
		setJButton(fillerThree, panelBackColor, panelBackColor, null, false, false);
		setLimit(limit, zeroIpad, zeroIpad, zeroInsets, 
				GridBagConstraints.BOTH, GridBagConstraints.CENTER, container, fillerThree);
		setGridposition(limit, xPosition+1, yPosition+1, defaultCellArea+1, defaultCellArea,
				resizable, resizable, container, fillerThree);
		//JButton ENCRYPT BUTTON
		setJButton(fileToEncryptButton, buttonColor, foregroundColor, font, false, false);
		setLimit(limit, ipadDefaultx, ipadDefaulty, insetsDefault,
				GridBagConstraints.BOTH, GridBagConstraints.CENTER, container, fileToEncryptButton);
		setGridposition(limit, xPosition+1, yPosition+2	, defaultCellArea, defaultCellArea,
				noResizable, noResizable, container, fileToEncryptButton);
		//TextField ENCRYPT TEXTFIELD
		((JTextField)encryptTextField).setEditable(false);
		setLimit(limit, zeroIpad, zeroIpad, insetsDefault, 
				GridBagConstraints.HORIZONTAL, GridBagConstraints.SOUTH, container, encryptTextField);
		setGridposition(limit, xPosition+2, yPosition+2, defaultCellArea, defaultCellArea,
				resizable, noResizable, container, encryptTextField);
		//checkbox WIPE SOURCE CHECKBOX
		((JLabel)wipeSource).setForeground(Color.WHITE);
		setLimit(limit, zeroIpad, zeroIpad, zeroInsets,
				GridBagConstraints.CENTER, GridBagConstraints.CENTER, container, wipeSource);
		setGridposition(limit, xPosition+1, yPosition+4, defaultCellArea, defaultCellArea,
				noResizable, noResizable, container, wipeSource);
		// JCombobox SELECT WIPING TYPE
		((JComboBox<Integer>)wipingComboBox).addItem(0);
		((JComboBox<Integer>)wipingComboBox).addItem(1);
		((JComboBox<Integer>)wipingComboBox).addItem(2);
		((JComboBox<Integer>)wipingComboBox).addItem(7);
		setLimit(limit, zeroIpad, zeroIpad, insetsDefault, 
				GridBagConstraints.HORIZONTAL, GridBagConstraints.SOUTH, container, wipingComboBox);
		setGridposition(limit, xPosition+2, yPosition+4, defaultCellArea, defaultCellArea,
				resizable, noResizable, container, wipingComboBox);
		//JButton PUBLIC KEY BUTTON
		setJButton(publicKeyButton, buttonColor, foregroundColor, font, false, false);
		setLimit(limit, ipadDefaultx, ipadDefaulty, insetsDefault,
				GridBagConstraints.BOTH, GridBagConstraints.CENTER, container, publicKeyButton);
		setGridposition(limit, xPosition+1, yPosition+5, defaultCellArea, defaultCellArea,
				noResizable, noResizable, container, publicKeyButton);
		//TextField PUBLIC KEY TEXT FIELD
		((JTextField)publicKeyTextField).setEditable(false);
		setLimit(limit, zeroIpad, zeroIpad, insetsDefault,
				GridBagConstraints.HORIZONTAL, GridBagConstraints.SOUTH, container, publicKeyTextField);
		setGridposition(limit, xPosition+2, yPosition+5, defaultCellArea, defaultCellArea,
				resizable, noResizable, container, publicKeyTextField);
		//JButton NEW KEY PAIR BUTTON 
		setJButton(newKeyPairButton, buttonColor, foregroundColor, font, false, false);
		setLimit(limit, ipadDefaultx, ipadDefaulty, insetsBigButton,
				GridBagConstraints.BOTH, GridBagConstraints.CENTER, container, newKeyPairButton);
		setGridposition(limit, xPosition+1, yPosition+7, defaultCellArea+1, defaultCellArea,
				resizable, noResizable, container, newKeyPairButton);
		//Filler 
		((JButton)fillerOne).setEnabled(false);
	//	((JButton)fillerOne).setVisible(false);
		setJButton(fillerOne, panelBackColor, panelBackColor, null, false, false);
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
		//JCombobox ALGORITHM TEXT FIELD
		for(EnumAvailableSymmetricAlgorithms item: EnumAvailableSymmetricAlgorithms.values()) {
			((JComboBox<EnumAvailableSymmetricAlgorithms>)algorithmComboBox).addItem(item);
		}
		setLimit(limit, zeroIpad, zeroIpad, insetsDefault,
				GridBagConstraints.HORIZONTAL, GridBagConstraints.EAST, container, algorithmComboBox);
		setGridposition(limit, xPosition+2, yPosition+9, defaultCellArea, defaultCellArea,
				resizable, noResizable, container, algorithmComboBox);
		//JLabel HASHING LABEL
		((JLabel)hashingLabel).setForeground(Color.WHITE);
		setLimit(limit, zeroIpad, zeroIpad, insetsDefault,
				GridBagConstraints.CENTER, GridBagConstraints.EAST, container, hashingLabel );
		setGridposition(limit, xPosition+1, yPosition+11, defaultCellArea, defaultCellArea,
				noResizable, noResizable, container, hashingLabel);
		//JCombobox HASHING TEXT FIELD
		for(EnumAvailableHashingAlgorithms item: EnumAvailableHashingAlgorithms.values()) {
			((JComboBox<EnumAvailableHashingAlgorithms>)hashingComboBox).addItem(item);
		}
		setLimit(limit, zeroIpad, zeroIpad, insetsDefault,
				GridBagConstraints.HORIZONTAL, GridBagConstraints.EAST, container, hashingComboBox);
		setGridposition(limit, xPosition+2, yPosition+11, defaultCellArea, defaultCellArea,
				resizable, noResizable, container, hashingComboBox);
		//JLabel COMPRESSION LABEL
		((JLabel)compressionLabel).setForeground(Color.WHITE);
		setLimit(limit, zeroIpad, zeroIpad, insetsDefault,
				GridBagConstraints.CENTER, GridBagConstraints.EAST, container, compressionLabel);
		setGridposition(limit, xPosition+1, yPosition+12, defaultCellArea, defaultCellArea,
				noResizable, noResizable, container, compressionLabel);
		//JCombobox COMPRESSION TEXT FIELD
		for(EnumAvailableCompressionAlgorithms item: EnumAvailableCompressionAlgorithms.values()) {
			((JComboBox<EnumAvailableCompressionAlgorithms>)compressionComboBox).addItem(item);
		}
		setLimit(limit, zeroIpad, zeroIpad, insetsDefault,
				GridBagConstraints.HORIZONTAL, GridBagConstraints.EAST, container, compressionComboBox);
		setGridposition(limit, xPosition+2, yPosition+12, defaultCellArea, defaultCellArea,
				resizable, noResizable, container, compressionComboBox);
		//filler
		((JButton)fillerTwo).setEnabled(false);
		setJButton(fillerTwo, panelBackColor, panelBackColor, null, false, false);
		setLimit(limit, zeroIpad, zeroIpad, zeroInsets,
				GridBagConstraints.BOTH, GridBagConstraints.CENTER, container, fillerTwo);
		setGridposition(limit, xPosition+1, yPosition+13, defaultCellArea+1, defaultCellArea,
				resizable, resizable, container, fillerTwo);
		//JButton START ENCRYPTION
		setJButton(startEncryptionButton, buttonColor, foregroundColor, font, false, false);
		setLimit(limit, ipadDefaultx, ipadDefaulty, insetsBigButton, 
				GridBagConstraints.BOTH, GridBagConstraints.EAST, container, startEncryptionButton);
		setGridposition(limit, xPosition+1, yPosition+14, defaultCellArea+1, defaultCellArea,
				resizable, noResizable, container, startEncryptionButton);
		//Status LABEL
		((JLabel)statusLabelEncryption).setForeground(Color.WHITE);
		setLimit(limit, zeroIpad, zeroIpad, insetsDefault,
				GridBagConstraints.CENTER, GridBagConstraints.CENTER, container, statusLabelEncryption);
		setGridposition(limit, xPosition+1, yPosition+15, defaultCellArea, defaultCellArea, 
				noResizable, noResizable, container, statusLabelEncryption);
		//encryption PROGRESS BAR
		setLimit(limit, zeroIpad, zeroIpad, insetsDefault,
				GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER, container, progressBarEncryption);
		setGridposition(limit, xPosition+2, yPosition+15, defaultCellArea, defaultCellArea,
				resizable, noResizable, container, progressBarEncryption);
		
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
		setJButton(fileToDecryptButton, buttonColor, foregroundColor, font, false, false);
		setLimit(limit, ipadDefaultx, ipadDefaulty, insetsDefault,
				GridBagConstraints.BOTH, GridBagConstraints.CENTER, container, fileToDecryptButton);
		setGridposition(limit, xPosition+4, yPosition+2	, defaultCellArea, defaultCellArea,
				noResizable, noResizable, container, fileToDecryptButton);
		//TextField  DECRYPT FIELD
		((JTextField)decryptTextField).setEditable(false);
		setLimit(limit, zeroIpad, zeroIpad, insetsDefault, 
				GridBagConstraints.HORIZONTAL, GridBagConstraints.SOUTH, container, decryptTextField);
		setGridposition(limit, xPosition+5, yPosition+2, defaultCellArea+1, defaultCellArea,
				noResizable, noResizable, container, decryptTextField);
		//JButton PRIVATE KEY
		setJButton(privateKeyButton, buttonColor, foregroundColor, font, false, false);
		setLimit(limit, ipadDefaultx, ipadDefaulty, insetsDefault,
				GridBagConstraints.BOTH, GridBagConstraints.CENTER, container, privateKeyButton);
		setGridposition(limit, xPosition+4, yPosition+5, defaultCellArea, defaultCellArea,
				noResizable, noResizable, container, privateKeyButton);
		//textField PRIVATE KEY FIELD
		((JTextField)privateKeyTextField).setEditable(false);
		setLimit(limit, zeroIpad, zeroIpad, insetsDefault,
				GridBagConstraints.HORIZONTAL, GridBagConstraints.SOUTH, container, privateKeyTextField);
		setGridposition(limit, xPosition+5, yPosition+5, defaultCellArea+1, defaultCellArea,
				resizable, noResizable, container, privateKeyTextField);
		//Jbutton STAR DECRYPTION
		setJButton(startDecryptionButton, buttonColor, foregroundColor, font, false, false);
		setLimit(limit, ipadDefaultx, ipadDefaulty, insetsBigButton,
				GridBagConstraints.BOTH, GridBagConstraints.CENTER, container, startDecryptionButton);
		setGridposition(limit, xPosition+4, yPosition+7, defaultCellArea+2, defaultCellArea,
				noResizable, noResizable, container, startDecryptionButton);
		//Status LABEL
		((JLabel)statusLabelDecryption).setForeground(Color.WHITE);
		setLimit(limit, zeroIpad, zeroIpad, insetsDefault,
				GridBagConstraints.CENTER, GridBagConstraints.NORTH, container, statusLabelDecryption);
		setGridposition(limit, xPosition+4, yPosition+8, defaultCellArea, defaultCellArea, 
				noResizable, noResizable, container, statusLabelDecryption);
		//PROGRESS BAR
		setLimit(limit, zeroIpad, zeroIpad, insetsDefault,
				GridBagConstraints.HORIZONTAL, GridBagConstraints.NORTH, container, progressBarDecryption);
		setGridposition(limit, xPosition+5, yPosition+8, defaultCellArea, defaultCellArea,
				resizable, noResizable, container, progressBarDecryption);
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
	
	private void setHandlers() {
		((JButton)CryptographyView.fileToEncryptButton).addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try { //  TODO: remove
					controller.command_SelectFileToEncrypt();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		});
		((JButton)CryptographyView.fileToDecryptButton).addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.command_SelectFileToDecrypt();				
			}
		});
		((JButton)CryptographyView.newKeyPairButton).addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.command_GenerateNewKeyPair();
			}
		});
		((JButton)CryptographyView.publicKeyButton).addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.command_SelectPublicKeyFile();				
			}
		});
		((JButton)CryptographyView.privateKeyButton).addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.command_SelectPrivateKeyFile();
			}
		});
		((JButton)CryptographyView.startDecryptionButton).addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.command_Decrypt();
			}
		});
		((JButton)CryptographyView.startEncryptionButton).addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try { // TODO: remove
					controller.command_Encrypt();
				} catch (NullPointerException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	//Getters and setters
	private void setPanelBackColor(Color panelBackColor) {
		this.panelBackColor = panelBackColor;
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

	@Override
	public void setText_encryptTextField(String text) {
		((JTextField)CryptographyView.encryptTextField).setText(text);
	}

	@Override
	public void setText_publicKeyTextField(String text) {
		((JTextField)CryptographyView.publicKeyTextField).setText(text);
	}

	@Override
	public void setText_decryptTextField(String text) {
		((JTextField)CryptographyView.decryptTextField).setText(text);
	}

	@Override
	public void setText_privateKeyTextField(String text) {
		((JTextField)CryptographyView.privateKeyTextField).setText(text);
	}

	@Override
	public void addText_logTextArea(String text) {
		((JTextArea)CryptographyView.logTextArea).append("\n" + text);
		//logTextArea.repaint();
	}

	@SuppressWarnings("unchecked")
	@Override
	public EnumAvailableSymmetricAlgorithms getSymmetricAlgorithm() {
		return (EnumAvailableSymmetricAlgorithms)((JComboBox<EnumAvailableSymmetricAlgorithms>) CryptographyView.algorithmComboBox).getSelectedItem();
	}

	@SuppressWarnings("unchecked")
	@Override
	public EnumAvailableHashingAlgorithms getHashingAlgorithm() {
		return (EnumAvailableHashingAlgorithms)((JComboBox<EnumAvailableHashingAlgorithms>) CryptographyView.hashingComboBox).getSelectedItem();
	}

	@SuppressWarnings("unchecked")
	@Override
	public EnumAvailableCompressionAlgorithms getCompressionAlgorithm() {
		return (EnumAvailableCompressionAlgorithms)((JComboBox<Integer>) CryptographyView.compressionComboBox).getSelectedItem();
	}

	@SuppressWarnings("unchecked")
	@Override
	public int getNumberOfWipingPassages() {
		return (int)((JComboBox<Integer>)CryptographyView.wipingComboBox).getSelectedItem();
	}

	@Override
	public void setValue_progressBarEncryption(int value) {
		((JProgressBar)progressBarEncryption).getModel().setValue(value);
	}

	@Override
	public void setValue_progressBarDecryption(int value) {
		((JProgressBar)progressBarDecryption).getModel().setValue(value);
	}


	
}
