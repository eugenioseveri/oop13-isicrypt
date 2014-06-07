package gui.views;

import gui.controllers.CryptographyController;
import gui.controllers.ICryptographyViewObserver;
import gui.models.ThemeChooser;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
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

/**
 * Class used to implement the cryptography function view.
 * @author Eugenio Severi
 */
public class CryptographyView extends AbstractGuiMethodSetter implements ICryptographyView { // TODO: si può non estendere JFrame?

	private static final long serialVersionUID = -162452746296023405L;
	private static final String APPLICATION_ICON = "isiCryptICON_MetroStyle.jpg";
	private Font font;
	private Color panelBackColor;
	private Color buttonColor;
	private Color foregroundColor;
	private static boolean isOpen = false;
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
	private static final JButton backButton = new JButton("Show Start");
	//private static final Component westEncryptBorderFiller = new JButton();
	private static final JLabel encryptionLabel = new JLabel("Encryption");
	private static final JButton fileToEncryptButton = new JButton("File to encrypt:");
	private static final JTextField encryptTextField = new JTextField();
	private static final JLabel wipeSource = new JLabel("Source file wiping passages:");
	private static final JComboBox<Integer> wipingComboBox = new JComboBox<Integer>();
	private static final JButton publicKeyButton = new JButton("Public key:");
	private static final JTextField publicKeyTextField = new JTextField();
	private static final JButton newKeyPairButton = new JButton("Generate new key pair");
	private static final JButton fillerOne = new JButton();
	private static final JLabel algorithmLabel = new JLabel("Algorithm:");
	private static final JComboBox<EnumAvailableSymmetricAlgorithms> algorithmComboBox = new JComboBox<EnumAvailableSymmetricAlgorithms>();
	private static final JLabel hashingLabel = new JLabel("Hashing:");
	private static final JComboBox<EnumAvailableHashingAlgorithms> hashingComboBox = new JComboBox<EnumAvailableHashingAlgorithms>();
	private static final JLabel compressionLabel = new JLabel("Compression:");
	private static final JComboBox<EnumAvailableCompressionAlgorithms> compressionComboBox = new JComboBox<EnumAvailableCompressionAlgorithms>();
	private static final JButton fillerTwo = new JButton();
	private static final JButton startEncryptionButton = new JButton("Start Encryption");
	private static final JLabel statusLabelEncryption = new JLabel("Status: ");
	private static final JProgressBar progressBarEncryption = new JProgressBar();
	private static final JLabel statusLabelDecryption = new JLabel("Status: ");
	private static final JProgressBar progressBarDecryption = new JProgressBar();
	//private static final Component eastEncryptBorderFiller = new JButton();
	//Decryption part
	private static final JSeparator separator = new JSeparator(SwingConstants.VERTICAL);
	//private static final Component westDecryptBorderFiller = new JButton();
	private static final JLabel decryptionLabel = new JLabel("Decryption");
	private static final JButton fileToDecryptButton = new JButton("File to decrypt:");
	private static final JTextField decryptTextField = new JTextField();
	private static final JButton privateKeyButton = new JButton("Private key:");
	private static final JTextField privateKeyTextField = new JTextField();
	private static final JButton fillerThree = new JButton();
	private static final JButton startDecryptionButton = new JButton("Start Decryption");
	//private static final Component fillerFour= new JButton();
	private static final JTextArea logTextArea = new JTextArea(20,20);
	private final static JScrollPane scrollPane = new JScrollPane(logTextArea);
	//private static final Component eastDecryptBorderFiller = new JButton();
	//Frame and panel
	private static final JPanel container = new JPanel();
	private GridBagConstraints limit;
	//private final static JFrame dialog = new JFrame();
	//Observer
	private ICryptographyViewObserver controller;
	
	@Override
	public void attachViewObserver(ICryptographyViewObserver listener){
		this.controller = listener;
	}
	
	/**
	 * Creates a new cryptography function view.
	 */
	public CryptographyView(){
		buildLayout();
		componentSetting();
		setFrame();
		setHandlers();
	}
	
	/**
	 * Creates the frame layout (same for all the views)
	 */
	private void buildLayout() {
		this.setButtonColor(ThemeChooser.getButtonColor());
		this.setFont(ThemeChooser.getFont());
		this.setForegroundColor(ThemeChooser.getForegroundColor());
		this.setPanelBackColor(ThemeChooser.getPanelBackColor());
		GridBagLayout layout = new GridBagLayout();
		limit = new GridBagConstraints();
		container.setLayout(layout);
		container.setBackground(panelBackColor);
	}
	
	/**
	 * Sets the properties for the frame.
	 */
	private void setFrame() {
		JFrame frame = new JFrame();
		try {
			frame.setIconImage(ImageIO.read(ClassLoader.getSystemResource(APPLICATION_ICON)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		WindowAdapter listener = new WindowAdapter() {

	        @Override
	        public void windowOpened(WindowEvent e) {
	        	CryptographyView.setOpen(true);	        }

	        @Override
	        public void windowClosing(WindowEvent e) {
	            CryptographyView.setOpen(false);
	            StartScreenView.redraw();
	        }
	    };
	    frame.addWindowListener(listener);
		frame.setTitle("Cryptography");
		frame.setSize(920, 640);
		frame.getContentPane().add(container);
		frame.setVisible(true);
	}
	
	/**
	 * Sets the properties for all the components.
	 */
	private void componentSetting(){
		//JLabel ENCRYPTION LABEL
		encryptionLabel.setForeground(Color.WHITE);
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
		fillerThree.setEnabled(false);
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
		encryptTextField.setEditable(false);
		setLimit(limit, zeroIpad, zeroIpad, insetsDefault, 
				GridBagConstraints.HORIZONTAL, GridBagConstraints.SOUTH, container, encryptTextField);
		setGridposition(limit, xPosition+2, yPosition+2, defaultCellArea, defaultCellArea,
				resizable, noResizable, container, encryptTextField);
		//checkbox WIPE SOURCE CHECKBOX
		wipeSource.setForeground(Color.WHITE);
		setLimit(limit, zeroIpad, zeroIpad, zeroInsets,
				GridBagConstraints.CENTER, GridBagConstraints.CENTER, container, wipeSource);
		setGridposition(limit, xPosition+1, yPosition+4, defaultCellArea, defaultCellArea,
				noResizable, noResizable, container, wipeSource);
		// JCombobox SELECT WIPING TYPE
		wipingComboBox.addItem(0);
		wipingComboBox.addItem(1);
		wipingComboBox.addItem(2);
		wipingComboBox.addItem(7);
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
		publicKeyTextField.setEditable(false);
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
		fillerOne.setEnabled(false);
	//	((JButton)fillerOne).setVisible(false);
		setJButton(fillerOne, panelBackColor, panelBackColor, null, false, false);
		setLimit(limit, zeroIpad, zeroIpad, zeroInsets, 
				GridBagConstraints.BOTH, GridBagConstraints.CENTER, container, fillerOne);
		setGridposition(limit, xPosition+1, yPosition+8, defaultCellArea+1, defaultCellArea,
				resizable, resizable, container, fillerOne);
		//JLabel ALGORITHM LABLE
		algorithmLabel.setForeground(Color.WHITE);
		setLimit(limit, zeroIpad, zeroIpad, insetsDefault,
				GridBagConstraints.CENTER, GridBagConstraints.EAST, container, algorithmLabel);
		setGridposition(limit, xPosition+1, yPosition+9, defaultCellArea, defaultCellArea,
				noResizable, noResizable, container, algorithmLabel);
		//JCombobox ALGORITHM TEXT FIELD
		for(EnumAvailableSymmetricAlgorithms item: EnumAvailableSymmetricAlgorithms.values()) {
			algorithmComboBox.addItem(item);
		}
		setLimit(limit, zeroIpad, zeroIpad, insetsDefault,
				GridBagConstraints.HORIZONTAL, GridBagConstraints.EAST, container, algorithmComboBox);
		setGridposition(limit, xPosition+2, yPosition+9, defaultCellArea, defaultCellArea,
				resizable, noResizable, container, algorithmComboBox);
		//JLabel HASHING LABEL
		hashingLabel.setForeground(Color.WHITE);
		setLimit(limit, zeroIpad, zeroIpad, insetsDefault,
				GridBagConstraints.CENTER, GridBagConstraints.EAST, container, hashingLabel );
		setGridposition(limit, xPosition+1, yPosition+11, defaultCellArea, defaultCellArea,
				noResizable, noResizable, container, hashingLabel);
		//JCombobox HASHING TEXT FIELD
		for(EnumAvailableHashingAlgorithms item: EnumAvailableHashingAlgorithms.values()) {
			hashingComboBox.addItem(item);
		}
		setLimit(limit, zeroIpad, zeroIpad, insetsDefault,
				GridBagConstraints.HORIZONTAL, GridBagConstraints.EAST, container, hashingComboBox);
		setGridposition(limit, xPosition+2, yPosition+11, defaultCellArea, defaultCellArea,
				resizable, noResizable, container, hashingComboBox);
		//JLabel COMPRESSION LABEL
		compressionLabel.setForeground(Color.WHITE);
		setLimit(limit, zeroIpad, zeroIpad, insetsDefault,
				GridBagConstraints.CENTER, GridBagConstraints.EAST, container, compressionLabel);
		setGridposition(limit, xPosition+1, yPosition+12, defaultCellArea, defaultCellArea,
				noResizable, noResizable, container, compressionLabel);
		//JCombobox COMPRESSION TEXT FIELD
		for(EnumAvailableCompressionAlgorithms item: EnumAvailableCompressionAlgorithms.values()) {
			compressionComboBox.addItem(item);
		}
		setLimit(limit, zeroIpad, zeroIpad, insetsDefault,
				GridBagConstraints.HORIZONTAL, GridBagConstraints.EAST, container, compressionComboBox);
		setGridposition(limit, xPosition+2, yPosition+12, defaultCellArea, defaultCellArea,
				resizable, noResizable, container, compressionComboBox);
		//filler
		fillerTwo.setEnabled(false);
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
		statusLabelEncryption.setForeground(Color.WHITE);
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
		decryptionLabel.setForeground(Color.WHITE);
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
		decryptTextField.setEditable(false);
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
		privateKeyTextField.setEditable(false);
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
		statusLabelDecryption.setForeground(Color.WHITE);
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
		logTextArea.setEditable(false);
		logTextArea.setLineWrap(true);//Format text on TextArea
		logTextArea.setWrapStyleWord(true);	//Format text on TextArea
		scrollPane.setPreferredSize(((JTextArea) logTextArea).getSize());
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); 	//Only vertical scroll bar
		setLimit(limit, zeroIpad, zeroIpad, insetsDefault,
				GridBagConstraints.BOTH, GridBagConstraints.CENTER, container, scrollPane);
		setGridposition(limit, xPosition+4, yPosition+9, defaultCellArea+1, defaultCellArea+6,
				resizable, resizable, container, scrollPane);
		
	}
	
	/**
	 * Sets the handlers for the components that require it.
	 */
	private void setHandlers() {
		CryptographyView.fileToEncryptButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.command_SelectFileToEncrypt();
			}
		});
		CryptographyView.fileToDecryptButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.command_SelectFileToDecrypt();				
			}
		});
		CryptographyView.newKeyPairButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.command_GenerateNewKeyPair();
			}
		});
		CryptographyView.publicKeyButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.command_SelectPublicKeyFile();				
			}
		});
		CryptographyView.privateKeyButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.command_SelectPrivateKeyFile();
			}
		});
		CryptographyView.startDecryptionButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.command_Decrypt();
			}
		});
		CryptographyView.startEncryptionButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.command_Encrypt();
			}
		});
		CryptographyView.backButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				((CryptographyController)controller).showStart();
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

	@Override // TODO: ha senso ridefinirlo?
	public void setFont(Font font) {
		this.font = font;
	}

	private void setButtonColor(Color buttonColor) {
		this.buttonColor = buttonColor;
	}

	@Override
	public void setText_encryptTextField(String text) {
		CryptographyView.encryptTextField.setText(text);
	}

	@Override
	public void setText_publicKeyTextField(String text) {
		CryptographyView.publicKeyTextField.setText(text);
	}

	@Override
	public void setText_decryptTextField(String text) {
		CryptographyView.decryptTextField.setText(text);
	}

	@Override
	public void setText_privateKeyTextField(String text) {
		CryptographyView.privateKeyTextField.setText(text);
	}
	
	@Override
	public JTextArea getTextArea(){
		return CryptographyView.logTextArea;
	}
	
	@Override
	public EnumAvailableSymmetricAlgorithms getSymmetricAlgorithm() {
		return (EnumAvailableSymmetricAlgorithms)CryptographyView.algorithmComboBox.getSelectedItem();
	}

	@Override
	public EnumAvailableHashingAlgorithms getHashingAlgorithm() {
		return (EnumAvailableHashingAlgorithms)CryptographyView.hashingComboBox.getSelectedItem();
	}

	@Override
	public EnumAvailableCompressionAlgorithms getCompressionAlgorithm() {
		return (EnumAvailableCompressionAlgorithms)CryptographyView.compressionComboBox.getSelectedItem();
	}

	@Override
	public int getNumberOfWipingPassages() {
		return (int)CryptographyView.wipingComboBox.getSelectedItem();
	}

	@Override
	public void setValue_progressBarEncryption(int value) {
		progressBarEncryption.getModel().setValue(value);
	}

	@Override
	public void setValue_progressBarDecryption(int value) {
		progressBarDecryption.getModel().setValue(value);
	}

	@Override
	public void showMessageDialog(String message) {
		JOptionPane.showMessageDialog(this, message);
	}
	
	public static boolean isOpen(){
		return isOpen;
	}

	public static void setOpen(boolean isOpen) {
		CryptographyView.isOpen = isOpen;
	}

}
