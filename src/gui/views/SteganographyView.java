package gui.views;

/**
 * @author Filippo Vimini
 * @data 25/04/2014
 * Graphic User Interface for Steganography class in Swing.
 * GridBagLayout used for make a precision and resizable GUI, for understand this class is necessary to own the GridBagLayout knowledge.
 */

import gui.controllers.ISteganographyViewObserver;
import gui.models.GlobalSettings;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

public class SteganographyView extends AbstractGuiMethodSetter {
	//Color and Fond take from file
	private Font font;
	private Color panelBakColor;
	private Color buttonColor;
	private Color foregroundColor;
	// Arrays that contains various dimension of insets
	private static final int insetsDefault[] = { 10, 10, 10, 10 };
	private static final int insetsZero[] = { 0, 0, 0, 0 };
	private static final int insetsTextButton[] = { 0, 0, 10, 10 };
	//Position and dimension of element
	private static final int xPosition = 0;
	private static final int yPosition = 0;
	private static final int resizable = 1;
	private static final int noResizable = 0;
	private static final int defaultCellArea = 1;
	private static final int zeroIpad = 0;
	private static final int defaultIpadX = 10;
	private static final int defaultIpadY = 30;
	private static ImageIcon icon = null;
	private static final String pathDefault = "./res/SteganographyDefaultIcon.jpg";
	private static final int buttonFill = GridBagConstraints.BOTH;
	private static final int buttonAnchor = GridBagConstraints.CENTER;
	private static final int iconHeigth = 240;
	private static final int iconWidth = 320;
	GridBagConstraints limit;
	//Graphic Element initialization
	private final static Component selectImageButton = new JButton("Select image");
	private final static Component selectTextButton = new JButton("Text from File");
	private final static Component separator = new JSeparator(SwingConstants.VERTICAL);
	private final static Component iconLabel = new JLabel();
	private final static Component encryptCheckbox = new JCheckBox("Encrypt");
	private final static Component startButton = new JButton("START");
	private final static JTextArea textArea = new JTextArea(10, 10);
	private final static Component scrollPane = new JScrollPane(textArea);
	private final static Component findTextButton = new JButton("Find Text");
	private final static Component clearSettingButton = new JButton("Clear Setting");
	private final static Component insertTextButton = new JButton("Enter text");
	private final static Component filler = new JButton();
	private final static Component filler2 = new JButton();
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
		GlobalSettings set = null;
		try {
			set = new GlobalSettings();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.setButtonColor(set.getButtonColor());
		this.setFont(set.getFont());
		this.setForegroundColor(set.getForegroundColor());
		this.setPanelBakColor(set.getPanelBakColor());
		GridBagLayout layout = new GridBagLayout();
		limit = new GridBagConstraints();
		container.setLayout(layout);
		container.setBackground(panelBakColor);
	}
	
	private void setFrame() {
		JFrame frame = new JFrame();
		try {
			frame.setIconImage(ImageIO.read(new File("./res/isiCryptICON_MetroStyle.jpg")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		frame.setTitle("Steganography");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(960, 640);
		frame.getContentPane().add(container);
		frame.setVisible(true);
	}

	private void componentSettings() {
		// JButton select image
		setJButton(selectImageButton, buttonColor, foregroundColor, font, false, false);
		setLimit(limit, defaultIpadX, defaultIpadY, insetsDefault,
				buttonFill, GridBagConstraints.CENTER, container, selectImageButton);
		setGridposition(limit, xPosition, yPosition, defaultCellArea, defaultCellArea,
				noResizable, noResizable, container, selectImageButton);
		// JButton select text
		setJButton(selectTextButton, buttonColor, foregroundColor, font, false, false);
		setLimit(limit, defaultIpadX+5, defaultIpadY, insetsDefault,
				buttonFill, GridBagConstraints.CENTER, container, selectTextButton);
		setGridposition(limit, xPosition+1, yPosition, defaultCellArea, defaultCellArea,
				noResizable, noResizable, container, selectTextButton);
		// JSeparator
		((JSeparator) separator).setBackground(Color.white);
		((JSeparator) separator).setMinimumSize(((JSeparator)separator).getPreferredSize());
		setLimit(limit, zeroIpad, zeroIpad, insetsZero, 
				GridBagConstraints.BOTH, GridBagConstraints.CENTER, container, separator);
		setGridposition(limit, xPosition+2, yPosition, defaultCellArea, defaultCellArea+6,
				noResizable, resizable, container, separator);
		//filler2
		setJButton(filler2, panelBakColor, null, null, false, false);
		setLimit(limit, zeroIpad, zeroIpad, insetsZero,
				buttonFill, GridBagConstraints.CENTER, container, filler2);
		setGridposition(limit, xPosition+3, yPosition, defaultCellArea, defaultCellArea+6,
				resizable, resizable, container, filler2);	
		// JLabel for icon
		try {
			BufferedImage defaultStartimage = ImageIO.read(new File(pathDefault));
			icon = iconOptimizer(((JLabel) iconLabel), defaultStartimage, iconHeigth, iconWidth);
		} catch (IOException e) {
			e.printStackTrace();
		}
		((JLabel) iconLabel).setIcon(icon);
		int insetsIcon[] = { 20, 10, 10, 10 };
		((JLabel) iconLabel).setMinimumSize(((JLabel) iconLabel).getPreferredSize());
		setLimit(limit, zeroIpad, zeroIpad, insetsIcon, 
				GridBagConstraints.BOTH, buttonAnchor, container, iconLabel);
		setGridposition(limit, xPosition+4, yPosition, defaultCellArea, defaultCellArea+5, 
				resizable, resizable, container, iconLabel);
		// JCheckBox
		((JCheckBox) encryptCheckbox).setBackground(Color.DARK_GRAY);
		((JCheckBox) encryptCheckbox).setForeground(Color.white);
		setLimit(limit,zeroIpad, zeroIpad, insetsDefault, GridBagConstraints.NONE,
				GridBagConstraints.WEST, container, encryptCheckbox);
		setGridposition(limit, xPosition, yPosition+1, defaultCellArea+1, defaultCellArea,
				noResizable, noResizable, container, encryptCheckbox);
		// JButton start button
		((JButton) startButton).setEnabled(false);
		setJButton(startButton, buttonColor, foregroundColor, font, false, false);
		setLimit(limit, defaultIpadX, defaultIpadY, insetsDefault,
				buttonFill, buttonAnchor, container, startButton);	
		setGridposition(limit, xPosition, yPosition+2, defaultCellArea+1, defaultCellArea, 
				noResizable, noResizable, container, startButton);
		// JButton Find text
		setJButton(findTextButton, buttonColor, foregroundColor, font, false,false);
		setLimit(limit, defaultIpadX, defaultIpadY, insetsDefault, 
				buttonFill, buttonAnchor, container, findTextButton);
		setGridposition(limit, xPosition, yPosition+3, defaultCellArea+1, defaultCellArea,
				noResizable, noResizable, container, findTextButton);	
		// JButton clear setting
		((JButton) clearSettingButton).setEnabled(false);
		setJButton(clearSettingButton, buttonColor, foregroundColor, font, false, false);
		setLimit(limit, defaultIpadX, defaultIpadY, insetsDefault,
				buttonFill, buttonAnchor, container, clearSettingButton);
		setGridposition(limit, xPosition, yPosition+4, defaultCellArea+1, defaultCellArea, 
				noResizable, noResizable, container, clearSettingButton);
		// Filler for good buttons resizable setting
		((JButton)filler).setVisible(false);
		setLimit(limit, zeroIpad, zeroIpad, insetsZero,
				buttonFill, GridBagConstraints.CENTER, container, filler);
		setGridposition(limit, xPosition, yPosition+5, defaultCellArea, defaultCellArea,
				noResizable, resizable, container, filler);	
		// JButton insert text
		setJButton(insertTextButton, buttonColor, foregroundColor, font, false, true);
		setLimit(limit, defaultIpadX, zeroIpad, insetsTextButton,
				buttonFill, GridBagConstraints.WEST, container, insertTextButton);
		setGridposition(limit, xPosition, yPosition+6, defaultCellArea, defaultCellArea, 
				noResizable, noResizable, container, insertTextButton);
		// JScrollPane(JtextArea)
		scrollPane.setMinimumSize(scrollPane.getPreferredSize());
		textArea.setLineWrap(true);		//Format text on TextArea
		textArea.setWrapStyleWord(true);	//Format text on TextArea
		((JScrollPane) scrollPane).setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); 	//Only vertical scroll bar
		setLimit(limit, zeroIpad, zeroIpad, insetsDefault,
				GridBagConstraints.BOTH, GridBagConstraints.CENTER, container, scrollPane);
		setGridposition(limit, xPosition, yPosition+7, defaultCellArea+4, defaultCellArea,
				resizable, resizable, container, scrollPane);
	}

	private void setHandlers() {
		// Select image button handlers
		((JButton) selectImageButton).addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				controller.selectImage();
			}
		});
		// Select text button handlers
		((JButton) selectTextButton).addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				controller.selectText();
			}
		});
		// Start button handlers
		((JButton) startButton).addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				controller.start();
			}
		});
		// Find text button handlers
		((JButton) findTextButton).addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				controller.findText();
			}
		});
		// Clear setting button handlers
		((JButton) clearSettingButton).addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				controller.clearSetting();
			}
		});
		// Insert text button handlers
		((JButton) insertTextButton).addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				controller.insertText();
			}
		});
	}

	//Setter and Getter
	public static Component getIconLabel() {
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
	public void setPanelBakColor(Color panelBakColor) {
		this.panelBakColor = panelBakColor;
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


}