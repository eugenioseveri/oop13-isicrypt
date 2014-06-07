package gui.views;

/**
 * @author Filippo Vimini
 * @data 25/04/2014
 * Graphic User Interface for Steganography class in Swing.
 * GridBagLayout used for make a precision and resizable GUI, for understand this class is necessary to own the GridBagLayout knowledge.
 */

import gui.controllers.ISteganographyViewObserver;
import gui.controllers.SteganographyController;
import gui.models.ThemeChooser;

import java.awt.Color;
import java.awt.Component;
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

public class SteganographyView extends AbstractGuiMethodSetter {
	private static final long serialVersionUID = 1L;
	private static final String APPLICATION_ICON = "isiCryptICON_MetroStyle.jpg";
	//Color and Fond take from file
	private Font font;
	private Color panelBackColor;
	private Color buttonColor;
	private Color foregroundColor;
	private static boolean isOpen = false;
	// Arrays that contains various dimension of insets
	private static final int insetsDefault[] = { 10, 10, 10, 10 };
	private static final int insetsZero[] = { 0, 0, 0, 0 };
	private static final int insetsTextButton[] = { 0, 0, 10, 10 };
	private static final int backInsets []= { 0, 0, 10, 10 };
	//Position and dimension of element
	private static final int xPosition = 0;
	private static final int yPosition = 0;
	private static final int resizable = 1;
	private static final int noResizable = 0;
	private static final int defaultCellArea = 1;
	private static final int zeroIpad = 0;
	private static final int ipadDefaultx = 10;
	private static final int ipadDefaulty = 30;
	private static ImageIcon icon = null;
	private static final String STEGANOGRAPHY_BACKGROUND = "SteganographyDefaultIcon.jpg";
	private static final int buttonFill = GridBagConstraints.BOTH;
	private static final int buttonAnchor = GridBagConstraints.CENTER;
	private static final int iconHeigth = 240;
	private static final int iconWidth = 320;
	private static final int textAreaYdimension = 100;
	private static final Dimension dim = new Dimension(0,textAreaYdimension);
	GridBagConstraints limit;
	//Graphic Element initialization
	private static final JButton backButton = new JButton("Show Start");
	private final static JButton selectImageButton = new JButton("Select image");
	private final static JButton selectTextButton = new JButton("Text from File");
	private final static JSeparator separator = new JSeparator(SwingConstants.VERTICAL);
	private final static JLabel iconLabel = new JLabel();
/*	not implemented yet
	private final static JCheckBox encryptCheckbox = new JCheckBox("Encrypt");		*/
	private final static JButton startButton = new JButton("START");
	private final static JTextArea textArea = new JTextArea(10, 10);
	private final static JScrollPane scrollPane = new JScrollPane(textArea);
	private final static JButton findTextButton = new JButton("Find Text");
	private final static JButton clearSettingButton = new JButton("Clear Setting");
	private final static JButton insertTextButton = new JButton("Enter text");
	private final static JButton filler = new JButton();
	private final static JButton filler2 = new JButton();
	private final static JFrame dialog = new JFrame();
	private static final JPanel container = new JPanel();

	private ISteganographyViewObserver controller;

	public SteganographyView() {
		buildLayout();
		componentSettings();
		setHandlers();
		setFrame();
	}

	public void attacSteganographyViewObserver(ISteganographyViewObserver controller) {
		this.controller = controller;
	}

	private void buildLayout() {
		//Set color and font from file, because there are many theme, and will be load the last used
		this.setButtonColor(ThemeChooser.getButtonColor());
		this.setFont(ThemeChooser.getFont());
		this.setForegroundColor(ThemeChooser.getForegroundColor());
		this.setPanelBackColor(ThemeChooser.getPanelBackColor());
		//set layout
		GridBagLayout layout = new GridBagLayout();
		limit = new GridBagConstraints();
		container.setLayout(layout);
		container.setBackground(panelBackColor);
	}
	
	private void setFrame() {	
		//new frame created in this method because it will be re-draw every time a new frame is needed
		JFrame frame = new JFrame();
		try {
			frame.setIconImage(ImageIO.read(ClassLoader.getSystemResourceAsStream(APPLICATION_ICON)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		WindowAdapter listener = new WindowAdapter() {

	        @Override
	        public void windowOpened(WindowEvent e) {
	        	SteganographyView.setOpen(true);	        }

	        @Override
	        public void windowClosing(WindowEvent e) {
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

	private void componentSettings() {
		//JButton BACK TO START 
		setJButton(backButton, buttonColor, foregroundColor, font, false, false);
		setLimit(limit, ipadDefaultx-5, ipadDefaulty-10, backInsets,
				GridBagConstraints.CENTER, GridBagConstraints.WEST, container, backButton);
		setGridposition(limit, xPosition, yPosition , defaultCellArea, defaultCellArea,
				noResizable, noResizable, container, backButton);		
		// JButton select image
		setJButton(selectImageButton, buttonColor, foregroundColor, font, false, false);
		setLimit(limit, ipadDefaultx, ipadDefaulty, insetsDefault,
				buttonFill, GridBagConstraints.CENTER, container, selectImageButton);
		setGridposition(limit, xPosition, yPosition+1, defaultCellArea, defaultCellArea,
				noResizable, noResizable, container, selectImageButton);
		// JButton select text
		setJButton(selectTextButton, buttonColor, foregroundColor, font, false, false);
		setLimit(limit, ipadDefaultx+5, ipadDefaulty, insetsDefault,
				buttonFill, GridBagConstraints.CENTER, container, selectTextButton);
		setGridposition(limit, xPosition+1, yPosition+1, defaultCellArea, defaultCellArea,
				noResizable, noResizable, container, selectTextButton);
		// JSeparator
		separator.setBackground(Color.white);
		separator.setMinimumSize(((JSeparator)separator).getPreferredSize());
		setLimit(limit, zeroIpad, zeroIpad, insetsZero, 
				GridBagConstraints.BOTH, GridBagConstraints.CENTER, container, separator);
		setGridposition(limit, xPosition+2, yPosition, defaultCellArea, defaultCellArea+6,
				noResizable, resizable, container, separator);
		//filler2
		filler2.setMaximumSize(((JButton)filler2).getPreferredSize());
		setJButton(filler2, panelBackColor, null, null, false, false);
		setLimit(limit, zeroIpad, zeroIpad, insetsZero,
				buttonFill, GridBagConstraints.CENTER, container, filler2);
		setGridposition(limit, xPosition+3, yPosition, defaultCellArea, defaultCellArea+6,
				resizable, resizable, container, filler2);	
		// JLabel for icon
		try { 
			BufferedImage defaultStartimage = ImageIO.read(ClassLoader.getSystemResourceAsStream(STEGANOGRAPHY_BACKGROUND));
			icon = iconOptimizer(iconLabel, defaultStartimage, iconHeigth, iconWidth);
		} catch (IOException e) {
		}
		iconLabel.setIcon(icon);
		int insetsIcon[] = { 20, 10, 10, 10 };
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
		// JButton start button
		startButton.setEnabled(false);
		setJButton(startButton, buttonColor, foregroundColor, font, false, false);
		setLimit(limit, ipadDefaultx, ipadDefaulty, insetsDefault,
				buttonFill, buttonAnchor, container, startButton);	
		setGridposition(limit, xPosition, yPosition+3, defaultCellArea+1, defaultCellArea, 
				noResizable, noResizable, container, startButton);
		// JButton Find text
		setJButton(findTextButton, buttonColor, foregroundColor, font, false,false);
		setLimit(limit, ipadDefaultx, ipadDefaulty, insetsDefault, 
				buttonFill, buttonAnchor, container, findTextButton);
		setGridposition(limit, xPosition, yPosition+4, defaultCellArea+1, defaultCellArea,
				noResizable, noResizable, container, findTextButton);	
		// JButton clear setting
		clearSettingButton.setEnabled(false);
		setJButton(clearSettingButton, buttonColor, foregroundColor, font, false, false);
		setLimit(limit, ipadDefaultx, ipadDefaulty, insetsDefault,
				buttonFill, buttonAnchor, container, clearSettingButton);
		setGridposition(limit, xPosition, yPosition+5, defaultCellArea+1, defaultCellArea, 
				noResizable, noResizable, container, clearSettingButton);
		// Filler for good buttons resizable setting
		filler.setVisible(false);
		setLimit(limit, zeroIpad, zeroIpad, insetsZero,
				buttonFill, GridBagConstraints.CENTER, container, filler);
		setGridposition(limit, xPosition, yPosition+6, defaultCellArea+1, defaultCellArea,
				noResizable, resizable, container, filler);	
		// JButton insert text
		setJButton(insertTextButton, buttonColor, foregroundColor, font, false, true);
		setLimit(limit, ipadDefaultx, zeroIpad, insetsTextButton,
				buttonFill, GridBagConstraints.WEST, container, insertTextButton);
		setGridposition(limit, xPosition, yPosition+7, defaultCellArea, defaultCellArea, 
				noResizable, noResizable, container, insertTextButton);
		// JScrollPane(JtextArea)
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
			public void actionPerformed(ActionEvent arg0) {
				((SteganographyController) controller).showStart();
			}
		});
		// Select image button handlers
		selectImageButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				controller.selectImage();
			}
		});
		// Select text button handlers
		selectTextButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				controller.selectText();
			}
		});
		// Start button handlers
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				controller.start();
			}
		});
		// Find text button handlers
		findTextButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				controller.findText();
			}
		});
		// Clear setting button handlers
		clearSettingButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				controller.clearSetting();
			}
		});
		// Insert text button handlers
		insertTextButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				controller.insertText();
			}
		});
	}

	//return the dialog for show the error 
	public static void optionPanel( Object error ){
		if(error instanceof Exception)JOptionPane.showMessageDialog(dialog, error);
		if(error instanceof String)JOptionPane.showMessageDialog(dialog, error);
	}
	
	//Setter and Getter
	public static JLabel getIconLabel() {
		return iconLabel;
	}

	public static Component getSelectImageButton() {
		return selectImageButton;
	}

	public static Component getSelectTextButton() {
		return selectTextButton;
	}

	public static Component getStartButton() {
		return startButton;
	}

	public static JTextArea getTextArea() {
		return textArea;
	}

	public static Component getFindTextButton() {
		return findTextButton;
	}

	public static Component getClearSettingButton() {
		return clearSettingButton;
	}

	public static Component getInsertTextButton() {
		return insertTextButton;
	}
	public void setPanelBackColor(Color panelBackColor) {
		this.panelBackColor = panelBackColor;
	}

	public void setButtonColor(Color buttonColor) {
		this.buttonColor = buttonColor;
	}

	public void setForegroundColor(Color foregroundColor) {
		this.foregroundColor = foregroundColor;
	}
	public void setFont(Font font) {
		this.font = font;
	}

	public static JFrame getDialog() {
		return dialog;
	}
	public static void setOpen(boolean isOpen) {
		SteganographyView.isOpen = isOpen;
	}
	
	public static boolean isOpen(){
		return SteganographyView.isOpen;
	}
}