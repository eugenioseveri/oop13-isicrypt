package gui.views;

/**
 * @author Filippo Vimini
 * @data 25/04/2014
 * Graphic User Interface for Steganography class in Swing.
 * GridBagLayout used for make a precision and resizable GUI, for understand this class is necessary to own the GridBagLayout knowledge.
 */

import gui.controllers.ISteganographyViewObserver;
import gui.controllers.SteganographyController;
import gui.controllers.ThemeChooser;

import java.awt.Color;
import java.awt.Dimension;
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
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

/**
 * Class used to implement the steganography function view.
 * @author Filippo Vimini
 */
public class SteganographyView extends AbstractGuiMethodSetter implements ISteganographyView{
	private static final long serialVersionUID = 1L;
	private static final String APPLICATION_ICON = "isiCryptICON_MetroStyle.jpg";
	//Color and Fond take from file
	private Font font;
	private Color buttonColor;
	private Color foregroundColor;
	//is used in StartScreenView for check if frame is open
	private static boolean isOpen; // Default=false
	// Arrays that contains various dimension of insets
	private final int insetsDefault[] = { 10, 10, 10, 10 };
	private final int insetsZero[] = { 0, 0, 0, 0 };
	private final int insetsTextButton[] = { 0, 0, 10, 10 };
	private final int backInsets []= { 10, 0, 10, 10 };
	//Position and dimension of element
	private static final int xPosition = 0;
	private static final int yPosition = 0;
	private static final int resizable = 1;
	private static final int noResizable = 0;
	private static final int defaultCellArea = 1;
	private static final int zeroIpad = 0;
	private static final int ipadDefaultx = 10;
	private static final int ipadDefaulty = 30;
	private static final String STEGANOGRAPHY_BACKGROUND = "SteganographyDefaultIcon.jpg";
	private final int buttonFill = GridBagConstraints.BOTH;
	private final int buttonAnchor = GridBagConstraints.CENTER;
	private static final int iconHeigth = 480;
	private static final int iconWidth = 640;
	private static final int textAreaYdimension = 100;
	private final Dimension dim = new Dimension(0,textAreaYdimension);
	private GridBagConstraints limit;
	//Graphic Element initialization
	private final JButton backButton = new JButton("Show Start");
	private final JButton selectImageButton = new JButton("Select image");
	private final JButton selectTextButton = new JButton("Text from File");
	private final JSeparator separator = new JSeparator(SwingConstants.VERTICAL);
	private final JLabel iconLabel = new JLabel();
/*	not implemented yet
	private final static JCheckBox encryptCheckbox = new JCheckBox("Encrypt");		*/
	private final JButton startButton = new JButton("START");
	private final JTextArea textArea = new JTextArea(10, 10);
	private final JScrollPane scrollPane = new JScrollPane(textArea);
	private final JButton findTextButton = new JButton("Find Text");
	private final JButton clearSettingsButton = new JButton("Clear Setting");
	private final JButton insertTextButton = new JButton("Enter text");
	private final JLabel filler = new JLabel();
	private final JLabel fillerSecond = new JLabel();
	private final JFrame dialog = new JFrame();
	private final JPanel container = new JPanel();

	private ISteganographyViewObserver controller;

	public SteganographyView() {
		super();
		buildLayout();
		componentSettings();
		setHandlers();
		setFrame();
	}

	@Override
	public void attackSteganographyViewObserver(final ISteganographyViewObserver controller) {
		this.controller = controller;
	}
	/**
	 * Set color and font from file, because there are many theme, and will be load the last used
	 * and set the layout
	 */
	private void buildLayout() {
		Color panelBackColor;
		buttonColor = ThemeChooser.getButtonColor();
		font = ThemeChooser.getFont();
		foregroundColor = ThemeChooser.getForegroundColor();
		panelBackColor = ThemeChooser.getPanelBackColor();
		//set layout
		final GridBagLayout layout = new GridBagLayout();
		limit = new GridBagConstraints();
		container.setLayout(layout);
		container.setBackground(panelBackColor);
	}
	/**
	 * Create a new Frame that contain the Component of the GUI and set closing and opening operations
	 */
	private void setFrame() {	
		//new frame created in this method because it will be re-draw every time a new frame is needed
		final JFrame frame = new JFrame();
		try {
			frame.setIconImage(ImageIO.read(ClassLoader.getSystemResourceAsStream(APPLICATION_ICON)));
		} catch (IOException e) {
			// This exception can not occur since APPLICATION_ICON is built-in
		}
		final WindowAdapter listener = new WindowAdapter() {

	        @Override
	        public void windowOpened(final WindowEvent e) {
	        	SteganographyView.setOpen(true);	        }

	        @Override
	        public void windowClosing(final WindowEvent e) {
	            SteganographyView.setOpen(false);
	            StartScreenView.redraw();
	        }
	    };
	    frame.addWindowListener(listener);
		frame.setTitle("Steganography");
		frame.setSize(960, 640);
		frame.getContentPane().add(container);
		frame.setVisible(true);
	}
	/**
	 * Method that create a Graphic, is optimized for GridBagLayout
	 */
	private void componentSettings() {
		// JButton BACK TO START 
		setJButton(backButton, buttonColor, foregroundColor, font, false, false);
		setLimit(limit, ipadDefaultx-5, ipadDefaulty-10, backInsets,
				GridBagConstraints.CENTER, GridBagConstraints.WEST, container, backButton);
		setGridposition(limit, xPosition, yPosition , defaultCellArea, defaultCellArea,
				noResizable, noResizable, container, backButton);		
		// JButton SELECT IMAGE
		setJButton(selectImageButton, buttonColor, foregroundColor, font, false, false);
		setLimit(limit, ipadDefaultx, ipadDefaulty, insetsDefault,
				buttonFill, GridBagConstraints.CENTER, container, selectImageButton);
		setGridposition(limit, xPosition, yPosition+1, defaultCellArea, defaultCellArea,
				noResizable, noResizable, container, selectImageButton);
		// JButton SELECT TEXT
		setJButton(selectTextButton, buttonColor, foregroundColor, font, false, false);
		setLimit(limit, ipadDefaultx+5, ipadDefaulty, insetsDefault,
				buttonFill, GridBagConstraints.CENTER, container, selectTextButton);
		setGridposition(limit, xPosition+1, yPosition+1, defaultCellArea, defaultCellArea,
				noResizable, noResizable, container, selectTextButton);
		// JSeparator SEPARATOR
		separator.setBackground(Color.white);
		separator.setMinimumSize(((JSeparator)separator).getPreferredSize());
		setLimit(limit, zeroIpad, zeroIpad, insetsZero, 
				GridBagConstraints.BOTH, GridBagConstraints.CENTER, container, separator);
		setGridposition(limit, xPosition+2, yPosition, defaultCellArea, defaultCellArea+6,
				noResizable, resizable, container, separator);
		// JLabel FILLER
		fillerSecond.setMaximumSize(fillerSecond.getPreferredSize());
		setLimit(limit, zeroIpad, zeroIpad, insetsZero,
				buttonFill, GridBagConstraints.CENTER, container, fillerSecond);
		setGridposition(limit, xPosition+3, yPosition, defaultCellArea, defaultCellArea+6,
				resizable, resizable, container, fillerSecond);	
		// JLabel ICON LABEL
		ImageIcon icon = null;
		try { 
			final BufferedImage defaultStartimage = ImageIO.read(ClassLoader.getSystemResourceAsStream(STEGANOGRAPHY_BACKGROUND));
			icon = iconOptimizer(iconLabel, defaultStartimage, iconHeigth, iconWidth);
		} catch (IOException e) {
			optionPane(e);
		}
		iconLabel.setIcon(icon);
		final int insetsIcon[] = { 20, 10, 10, 10 };
		iconLabel.setMinimumSize(iconLabel.getPreferredSize());
		setLimit(limit, zeroIpad, zeroIpad, insetsIcon, 
				GridBagConstraints.BOTH, GridBagConstraints.CENTER, container, iconLabel);
		setGridposition(limit, xPosition+4, yPosition, defaultCellArea, defaultCellArea+6, 
				resizable, resizable, container, iconLabel);
		// JCheckBox NOT IMPLEMETED YET
	/*	encryptCheckbox.setBackground(Color.DARK_GRAY);
		encryptCheckbox.setForeground(Color.white);
		setLimit(limit,zeroIpad, zeroIpad, insetsDefault, GridBagConstraints.NONE,
				GridBagConstraints.WEST, container, encryptCheckbox);
		setGridposition(limit, xPosition, yPosition+2, defaultCellArea+1, defaultCellArea,
				noResizable, noResizable, container, encryptCheckbox);							*/
		// JButton START BUTTON
		startButton.setEnabled(false);
		setJButton(startButton, buttonColor, foregroundColor, font, false, false);
		setLimit(limit, ipadDefaultx, ipadDefaulty, insetsDefault,
				buttonFill, buttonAnchor, container, startButton);	
		setGridposition(limit, xPosition, yPosition+3, defaultCellArea+1, defaultCellArea, 
				noResizable, noResizable, container, startButton);
		// JButton FIND TEXT
		setJButton(findTextButton, buttonColor, foregroundColor, font, false,false);
		setLimit(limit, ipadDefaultx, ipadDefaulty, insetsDefault, 
				buttonFill, buttonAnchor, container, findTextButton);
		setGridposition(limit, xPosition, yPosition+4, defaultCellArea+1, defaultCellArea,
				noResizable, noResizable, container, findTextButton);	
		// JButton CLEAR SETTINGS
		clearSettingsButton.setEnabled(false);
		setJButton(clearSettingsButton, buttonColor, foregroundColor, font, false, false);
		setLimit(limit, ipadDefaultx, ipadDefaulty, insetsDefault,
				buttonFill, buttonAnchor, container, clearSettingsButton);
		setGridposition(limit, xPosition, yPosition+5, defaultCellArea+1, defaultCellArea, 
				noResizable, noResizable, container, clearSettingsButton);
		// JLable FILLER
		filler.setVisible(false);
		setLimit(limit, zeroIpad, zeroIpad, insetsZero,
				buttonFill, GridBagConstraints.CENTER, container, filler);
		setGridposition(limit, xPosition, yPosition+6, defaultCellArea+1, defaultCellArea,
				noResizable, resizable, container, filler);	
		// JButton INSERT TEXT
		setJButton(insertTextButton, buttonColor, foregroundColor, font, false, true);
		setLimit(limit, ipadDefaultx, zeroIpad, insetsTextButton,
				buttonFill, GridBagConstraints.WEST, container, insertTextButton);
		setGridposition(limit, xPosition, yPosition+7, defaultCellArea, defaultCellArea, 
				noResizable, noResizable, container, insertTextButton);
		// JScrollPane(JtextArea) TEXT AREA
		scrollPane.setPreferredSize(dim);
		scrollPane.setMinimumSize(dim);
		//Format text on TextArea
		textArea.setLineWrap(true);		
		textArea.setWrapStyleWord(true);	
		//Only vertical scroll bar
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); 	
		setLimit(limit, zeroIpad, zeroIpad, insetsDefault,
				GridBagConstraints.BOTH, GridBagConstraints.CENTER, container, scrollPane);
		setGridposition(limit, xPosition, yPosition+8, defaultCellArea+4, defaultCellArea,
				resizable, noResizable, container, scrollPane);
	}

	private void setHandlers() {
		//back to Start View
		backButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(final ActionEvent arg0) {
				((SteganographyController) controller).showStart();
			}
		});
		// Select image button handlers
		selectImageButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent arg0) {
				controller.selectImage();
			}
		});
		// Select text button handlers
		selectTextButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent arg0) {
				controller.selectText();
			}
		});
		// Start button handlers
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent arg0) {
				controller.start();
			}
		});
		// Find text button handlers
		findTextButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent arg0) {
				controller.findText();
			}
		});
		// Clear setting button handlers
		clearSettingsButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent arg0) {
				controller.clearSetting();
			}
		});
		// Insert text button handlers
		insertTextButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent arg0) {
				controller.insertText();
			}
		});
	}

	//return the dialog for show the error 
	@Override
	public void optionPane(final Object error ){
		if(error instanceof Exception){
			JOptionPane.showMessageDialog(dialog, error);
		}
		if(error instanceof String){
			JOptionPane.showMessageDialog(dialog, error);
		}
	}
	
	//Setter and Getter
	@Override
	public JLabel getIconLabel() {
		return iconLabel;
	}
	@Override
	public JButton getSelectImageButton() {
		return selectImageButton;
	}
	@Override
	public JButton getSelectTextButton() {
		return selectTextButton;
	}
	@Override
	public JButton getStartButton() {
		return startButton;
	}
	@Override
	public JTextArea getTextArea() {
		return textArea;
	}
	@Override
	public JButton getFindTextButton() {
		return findTextButton;
	}
	@Override
	public JButton getClearSettingButton() {
		return clearSettingsButton;
	}
	@Override
	public JButton getInsertTextButton() {
		return insertTextButton;
	}
	@Override
	public JFrame getDialog() {
		return dialog;
	}
	private static void setOpen(final boolean isOpen) {
		SteganographyView.isOpen = isOpen;
	}
	
	public static boolean isOpen(){
		return SteganographyView.isOpen;
	}
}