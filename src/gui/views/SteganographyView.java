package gui.views;

/**
 * @author Filippo Vimini
 * @data 25/04/2014
 * Graphic User Interface for Steganography class in Swing.
 * GridBagLayout used for make a precision and resizable GUI, for understand this class is necessary own the GridBagLayout knowledge.
 */

import gui.controllers.ISteganographyViewObserver;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
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
	// Default font for button
	private static final Font font = new Font("Verdana", Font.BOLD, 12);
	// Default Background JPanel Color
	private static final Color panelBakColor = Color.DARK_GRAY;
	// Default color of button
	private static final Color buttonColor = Color.black;
	// Default foreground color of JButton
	private static final Color foregroundColor = Color.white;
	// Arrays that contains various dimension of insets
	private static final int insetsDefault[] = { 10, 10, 10, 10 };
	private static final int[] zeroInsets = { 0, 0, 0, 0 };
	// weight for ipadx and ipady
	private static final int standardButtonIpady = 40;
	private static final int standardButtonIpadx = -40;
	private static final int buttonHeightDimension = 25;
	private static final int buttonWidthDimension = 55;

	private static final JPanel contenitore = new JPanel();
	GridBagConstraints limit;
	// Costante duplicata
	String pathDefault = "./res/SteganographyDefaultIcon.jpg";

	private final static Component selectImageButton = new JButton("Select image");
	private final static String selectText = "Select text\nfrom File"; 
	private final static Component selectTextButton = 
			new JButton("<html>" + selectText.replaceAll("\\n", "<br>") + "</html>");
	private final static Component separator = new JSeparator(SwingConstants.VERTICAL);
	private final static Component iconLabel = new JLabel();
	private final static Component encryptCheckbox = new JCheckBox("Encrypt");
	private final static Component startButton = new JButton("START");
	private final static JTextArea textArea = new JTextArea(10, 10);
	private final static Component scrollPane = new JScrollPane(textArea);
	private final static Component findTextButton = new JButton("Find Text");
	private final static Component clearSettingButton = new JButton(
			"Clear Setting");
	private final static Component insertTextButton = new JButton(
			"Select enter text");
	private final static Component filler = new JLabel();

	private ISteganographyViewObserver controller;

	public SteganographyView() {
		buildLayout();
		componentSettings();
		setHandlers();
		setFrame();
	}

	public void attacSteganographyViewObserver(
			ISteganographyViewObserver controller) {
		this.controller = controller;
	}

	private void buildLayout() {
		GridBagLayout layout = new GridBagLayout();
		limit = new GridBagConstraints();
		contenitore.setLayout(layout);
		contenitore.setBackground(panelBakColor);
	}

	private void componentSettings() {
		// JButton select image
		setJButton(selectImageButton, buttonColor, foregroundColor, font, false, false);
		setLimit(limit, 0, 0, 1, 1, 0, 0, buttonWidthDimension,
				standardButtonIpady, insetsDefault, GridBagConstraints.NONE,
				GridBagConstraints.WEST, contenitore, selectImageButton);
		// JButton select text
		((JButton) selectTextButton).setMinimumSize(((JButton) selectTextButton).getPreferredSize());
		setJButton(selectTextButton, buttonColor, foregroundColor, font, false, false);
		setLimit(limit, 1, 0, 1, 1, 0, 0, buttonWidthDimension + 23,
				buttonHeightDimension, insetsDefault, GridBagConstraints.NONE,
				GridBagConstraints.CENTER, contenitore, selectTextButton);
		// JSeparator
		((JSeparator) separator).setBackground(Color.white);
		((JSeparator) separator).setMinimumSize(((JSeparator) separator).getPreferredSize());
		setLimit(limit, 2, 0, 1, 6, 0, 1, 0, 0, zeroInsets,
				GridBagConstraints.BOTH, GridBagConstraints.CENTER,
				contenitore, separator);
		// JLabel for icon
		ImageIcon icon = null;
		try {
			BufferedImage defaultStartimage = ImageIO.read(new File(pathDefault));
			icon = iconOptimizer(((JLabel) iconLabel), defaultStartimage);
		} catch (IOException e) {
			e.printStackTrace();
		}
		((JLabel) iconLabel).setIcon(icon);
		int insetsIcon[] = { 20, 10, 10, 10 };
		setLimit(limit, 3, 0, 1, 6, 0, 0, 0, 0, insetsIcon,
				GridBagConstraints.CENTER, GridBagConstraints.CENTER,
				contenitore, iconLabel);
		// JCheckBox
		((JCheckBox) encryptCheckbox).setBackground(Color.DARK_GRAY);
		((JCheckBox) encryptCheckbox).setForeground(Color.white);
		setLimit(limit, 0, 1, 2, 1, 0, 0, 0, standardButtonIpady,
				insetsDefault, GridBagConstraints.NONE,
				GridBagConstraints.WEST, contenitore, encryptCheckbox);

		// JButton start button
		((JButton) startButton).setEnabled(false);
		setJButton(startButton, buttonColor, foregroundColor, font, false, false);
		setLimit(limit, 0, 2, 2, 1, 0, 0, standardButtonIpadx,
				standardButtonIpady, insetsDefault, GridBagConstraints.BOTH,
				GridBagConstraints.CENTER, contenitore, startButton);
		// JScrollPane(JtextArea)
		scrollPane.setMinimumSize(scrollPane.getPreferredSize());
		/*
		 * To force JTextArea that set automatically a new line. The dimension
		 * of textArea is that because is prepared to host different type of
		 * information, like the info of the file ( different from image ) ecc..
		 */
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		((JScrollPane) scrollPane).setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		setLimit(limit, 0, 7, 4, 1, 1, 0, 0, 0, insetsDefault,
				GridBagConstraints.BOTH, GridBagConstraints.WEST, contenitore,
				scrollPane);
		// JButton Find text
		setJButton(findTextButton, buttonColor, foregroundColor, font, false,false);
		setLimit(limit, 0, 3, 2, 1, 0, 0, standardButtonIpadx,
				standardButtonIpady, insetsDefault, GridBagConstraints.BOTH,
				GridBagConstraints.CENTER, contenitore, findTextButton);
		// JButton clear setting
		((JButton) clearSettingButton).setEnabled(false);
		setJButton(clearSettingButton, buttonColor, foregroundColor, font, false, false);
		setLimit(limit, 0, 4, 2, 1, 0, 0, standardButtonIpadx,
				standardButtonIpady, insetsDefault, GridBagConstraints.BOTH,
				GridBagConstraints.CENTER, contenitore, clearSettingButton);
		// JButton insert text
		int insetsTextAreaButton[] = { 0, 0, 10, 10 };
		setJButton(insertTextButton, buttonColor, foregroundColor, font, false, true);
		setLimit(limit, 0, 6, 1, 1, 0, 0, 0, 0, insetsTextAreaButton,
				GridBagConstraints.NONE, GridBagConstraints.WEST, contenitore, insertTextButton);
		// Filler for good buttons resizable setting
		setLimit(limit, 0, 5, 2, 1, 0, 0, 0, 0, zeroInsets,
				GridBagConstraints.BOTH, GridBagConstraints.CENTER,
				contenitore, filler);
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

	private void setFrame() {
		JFrame frame = new JFrame();
		try {
			frame.setIconImage(ImageIO.read(new File(
					"./res/isiCryptICON_MetroStyle.jpg")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		frame.setTitle("Steganography");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1280, 720);
		frame.getContentPane().add(contenitore);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setVisible(true);
	}

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
}