package gui.views;

import gui.controllers.IKeyringViewObserver;
import gui.controllers.KeyringController;
import gui.controllers.ThemeChooser;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 * Class used to implement the keyring function view.
 * @author Eugenio Severi
 */
public class KeyringView extends AbstractGuiMethodSetter implements IKeyringView {

	private static final long serialVersionUID = -4534574271536073257L;
	private static final String APPLICATION_ICON = "isiCryptICON_MetroStyle.jpg";
	private static final String KEYRING_ICON = "isiCryptIIcon_Keyring.jpg";
	private Font font;
	private Color panelBackColor;
	private Color buttonColor;
	private Color foregroundColor;
	private static boolean isOpen; // Default=false
	private static final int ICON_HEIGTH = 100;
	private static final int ICON_WIDTH = 100;
	// Arrays that contains various dimension of insets
	private static final int insetsDefault[] = { 10, 10, 10, 10 };
	private static final int zeroInsets []= { 0, 0, 0, 0 };
	private static final int backInsets []= { 0, 0, 10, 10 };
	private static final int zeroIpad  = 0;
	private static final int noResizable  = 0;
	private static final int resizable  = 1;
	private static final int ipadDefaulty = 30;
	private static final int ipadDefaultx = 65;
	private static final int xPosition = 0;
	private static final int yPosition = 0;
	private static final int defaultCellArea = 1;
	//GUI component declaration
	private static final JButton backButton = new JButton("Show Start");
	private static final JButton addButton = new JButton("Add account");
	private static final JButton modifyButton = new JButton("Modify account");
	private static final JButton cancelButton = new JButton("Delete account");
	private static final JButton encryptButton = new JButton("Encryption key");
	private static final JButton saveButton = new JButton("Save settings");
	private static final JLabel iconLabel= new JLabel();
	private static ImageIcon icon;
	private static final TableModel tableModel = new DefaultTableModel();
	private static final JTable table = new JTable(tableModel);
	private static final JScrollPane scrollPane = new JScrollPane(table);
	private static final JButton fillerOne = new JButton();
	private static final JButton fillerTwo = new JButton();
	private static final JButton fillerThree = new JButton();
	private static final JButton fillerFour = new JButton();
	private static final JButton fillerFive = new JButton();
	private static final JButton fillerSix = new JButton();
	private static final JSeparator separator = new JSeparator(SwingConstants.VERTICAL);
	
	private IKeyringViewObserver controller;
	
	@Override
	public void attachViewObserver(final IKeyringViewObserver listener){
		this.controller = listener;
	}

	//Frame and panel
	private static final JPanel container = new JPanel();
	private GridBagConstraints limit;
//	private final static JFrame dialog = new JFrame();
	
	/**
	 * Creates a new cryptography function view.
	 */
	public KeyringView(){
		super();
		buildLayout();
		componentSettings();
		setHandlers();
		setFrame();
	}
	
	/**
	 * Creates the frame layout (same for all the views)
	 */
	private void buildLayout() {
		buttonColor = ThemeChooser.getButtonColor();
		font = ThemeChooser.getFont();
		foregroundColor = ThemeChooser.getForegroundColor();
		panelBackColor = ThemeChooser.getPanelBackColor();
		final GridBagLayout layout = new GridBagLayout();
		limit = new GridBagConstraints();
		container.setLayout(layout);
		container.setBackground(panelBackColor);
	}
	
	/**
	 * Sets the properties for the frame.
	 */
	private void setFrame() {
		final JFrame frame = new JFrame();
		try {
			frame.setIconImage(ImageIO.read(ClassLoader.getSystemResourceAsStream(APPLICATION_ICON)));
		} catch (IOException e) {
			// This exception can not occur since the resource is built-in
		}
		final WindowAdapter listener = new WindowAdapter() {

	        @Override
	        public void windowOpened(final WindowEvent e) {
	        	KeyringView.setOpen(true);	        }

	        @Override
	        public void windowClosing(final WindowEvent e) {
	        	KeyringView.setOpen(false);
	            StartScreenView.redraw();
	        }
	    };
	    frame.addWindowListener(listener);
		frame.setTitle("Keyring");
		frame.setSize(920, 640);
		frame.getContentPane().add(container);
		frame.setVisible(true);
	}
	
	/**
	 * Sets the properties for all the components.
	 */
	private void componentSettings(){
		//JButton BACK TO START 
		setJButton(backButton, buttonColor, foregroundColor, font, false, false);
		setLimit(limit, ipadDefaultx-50, ipadDefaulty-10, backInsets,
				GridBagConstraints.CENTER, GridBagConstraints.WEST, container, backButton);
		setGridposition(limit, xPosition, yPosition , defaultCellArea, defaultCellArea,
				noResizable, noResizable, container, backButton);		
		//JButton ADD
		setJButton(addButton, buttonColor, foregroundColor, font, false, false);
		setLimit(limit, ipadDefaultx, ipadDefaulty, insetsDefault, 
				GridBagConstraints.BOTH, GridBagConstraints.CENTER, container, addButton);
		setGridposition(limit, xPosition, yPosition+1, defaultCellArea, defaultCellArea,
				noResizable, noResizable, container, addButton);
		//FILLER ONE
		fillerOne.setEnabled(false);
		setJButton(fillerOne, panelBackColor, panelBackColor, null, false, false);
		setLimit(limit, ipadDefaultx, ipadDefaulty, zeroInsets, 
				GridBagConstraints.BOTH, GridBagConstraints.CENTER, container, fillerOne);
		setGridposition(limit, xPosition, yPosition+2, defaultCellArea, defaultCellArea,
				noResizable, resizable, container, fillerOne);
		//JButton MODIFY
		setJButton(modifyButton, buttonColor, foregroundColor, font, false, false);
		setLimit(limit, ipadDefaultx, ipadDefaulty, insetsDefault, 
				GridBagConstraints.BOTH, GridBagConstraints.CENTER, container, modifyButton);
		setGridposition(limit, xPosition, yPosition+3, defaultCellArea, defaultCellArea,
				noResizable, noResizable, container, modifyButton);
		//FILLER TWO
		fillerTwo.setEnabled(false);
		setJButton(fillerTwo, panelBackColor, panelBackColor, null, false, false);
		setLimit(limit, ipadDefaultx, ipadDefaulty, zeroInsets, 
				GridBagConstraints.BOTH, GridBagConstraints.CENTER, container, fillerTwo);
		setGridposition(limit, xPosition, yPosition+4, defaultCellArea, defaultCellArea,
				noResizable, resizable, container, fillerTwo);
		//JButton CANCEL
		setJButton(cancelButton, buttonColor, foregroundColor, font, false, false);
		setLimit(limit, ipadDefaultx, ipadDefaulty, insetsDefault, 
				GridBagConstraints.BOTH, GridBagConstraints.CENTER, container, cancelButton);
		setGridposition(limit, xPosition, yPosition+5, defaultCellArea, defaultCellArea,
				noResizable, noResizable, container, cancelButton);		
		//FILLER THREE
		fillerThree.setEnabled(false);
		setJButton(fillerThree, panelBackColor, panelBackColor, null, false, false);
		setLimit(limit, ipadDefaultx, ipadDefaulty, zeroInsets, 
				GridBagConstraints.BOTH, GridBagConstraints.CENTER, container, fillerThree);
		setGridposition(limit, xPosition, yPosition+6, defaultCellArea, defaultCellArea,
				noResizable, resizable, container, fillerThree);
		//JButton ENCYPTION MODE
		setJButton(encryptButton, buttonColor, foregroundColor, font, false, false);
		setLimit(limit, ipadDefaultx, ipadDefaulty, insetsDefault, 
				GridBagConstraints.BOTH, GridBagConstraints.CENTER, container, encryptButton);
		setGridposition(limit, xPosition, yPosition+7, defaultCellArea, defaultCellArea,
				noResizable, noResizable, container, encryptButton);	
		//FILLER FOUR
		fillerFour.setEnabled(false);
		setJButton(fillerFour, panelBackColor, panelBackColor, null, false, false);
		setLimit(limit, ipadDefaultx, ipadDefaulty, zeroInsets, 
				GridBagConstraints.BOTH, GridBagConstraints.CENTER, container, fillerFour);
		setGridposition(limit, xPosition, yPosition+8, defaultCellArea, defaultCellArea,
				noResizable, resizable, container, fillerFour);		
		//JButton SAVE
		setJButton(saveButton, buttonColor, foregroundColor, font, false, false);
		setLimit(limit, ipadDefaultx, ipadDefaulty, insetsDefault, 
				GridBagConstraints.BOTH, GridBagConstraints.CENTER, container, saveButton);
		setGridposition(limit, xPosition, yPosition+9, defaultCellArea, defaultCellArea,
				noResizable, noResizable, container, saveButton);
		//FILLER FIVE
		fillerFive.setEnabled(false);
		setJButton(fillerFive, panelBackColor, panelBackColor, null, false, false);
		setLimit(limit, ipadDefaultx, ipadDefaulty, zeroInsets, 
				GridBagConstraints.BOTH, GridBagConstraints.CENTER, container, fillerFive);
		setGridposition(limit, xPosition, yPosition+10, defaultCellArea, defaultCellArea,
				noResizable, resizable, container, fillerFive);		
		try {
			final BufferedImage imageIcon = ImageIO.read(ClassLoader.getSystemResourceAsStream(KEYRING_ICON));
			icon = iconOptimizer(iconLabel, imageIcon, ICON_HEIGTH, ICON_WIDTH);
		} catch (IOException e) {
			//This exception can not occur since the resource is built-in
		}
		iconLabel.setIcon(icon);
		setLimit(limit, zeroIpad, zeroIpad, zeroInsets, 
				GridBagConstraints.CENTER, GridBagConstraints.CENTER, container, iconLabel);
		setGridposition(limit, xPosition, yPosition+11, defaultCellArea, defaultCellArea,
				noResizable, noResizable, container, iconLabel);
		//FILLER SIX
		fillerSix.setEnabled(false);
		setJButton(fillerSix, panelBackColor, panelBackColor, null, false, false);
		setLimit(limit, ipadDefaultx, ipadDefaulty, zeroInsets, 
				GridBagConstraints.BOTH, GridBagConstraints.CENTER, container, fillerSix);
		setGridposition(limit, xPosition, yPosition+12, defaultCellArea, defaultCellArea,
				noResizable, resizable, container, fillerSix);	
		//JSeparator SEPARATOR
		setLimit(limit, zeroIpad, zeroIpad, zeroInsets,
				GridBagConstraints.BOTH, GridBagConstraints.CENTER, container, separator);
		setGridposition(limit, xPosition+1, yPosition, defaultCellArea, defaultCellArea+11,
				noResizable, resizable, container, separator);
		//JTable TABLE
		setLimit(limit, zeroIpad, zeroIpad, insetsDefault, 
				GridBagConstraints.BOTH, GridBagConstraints.CENTER, container, scrollPane);
		setGridposition(limit, xPosition+2, yPosition, defaultCellArea, defaultCellArea+11,
				resizable, resizable, container, scrollPane);
	}
	
	/**
	 * Sets the handlers for the components that require it.
	 */
	private void setHandlers(){
		KeyringView.backButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent arg0) {
				((KeyringController)controller).showStart();
			}
		});
		KeyringView.addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent arg0) {
				controller.command_addButton();
			}
		});
		KeyringView.modifyButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent arg0) {
				controller.command_modifyButton();
			}
		});
		KeyringView.cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent arg0) {
				controller.command_cancelButton();
			}
		});
		KeyringView.encryptButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent arg0) {
				controller.command_encryptButton();
			}
		});
		KeyringView.saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent arg0) {
				controller.command_saveButton();
			}
		});
	}
	
	//Getters and setters
	@Override
	public JTable getTable() {
		return (JTable) table;
	}

	@Override
	public void showMessageDialog(final String message) {
		JOptionPane.showMessageDialog(this, message);
	}

	@Override
	public boolean showYesNoOptionPane(final String message) {
		final int choice = JOptionPane.showConfirmDialog(this, message, "", JOptionPane.YES_NO_OPTION);
		if(choice == JOptionPane.YES_OPTION) {
			return true;
		}
		return false;
	}

	@Override
	public String showInputDialog(final String title, final String editableText) {
		return JOptionPane.showInputDialog(title, editableText);
	}
	
	public static void setOpen(final boolean isOpen) {
		KeyringView.isOpen = isOpen;
	}
	
	public static boolean isOpen(){
		return KeyringView.isOpen;
	}
	
}