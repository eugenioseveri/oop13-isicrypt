package gui.views;

import gui.controllers.FileExchangeController;
import gui.models.GlobalSettings;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableModel;

public class KeyringView extends AbstractGuiMethodSetter{
	private Font font;
	private Color panelBakColor;
	private Color buttonColor;
	private Color foregroundColor;
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
	ImageIcon icon = new ImageIcon("./res/isiCryptIIcon_Keyring.jpg");
	//GUI component declaration
	private static final Component backButton = new JButton("Show Start");
	private static final Component addButton = new JButton("Add account");
	private static final Component modifyButton = new JButton("Modify account");
	private static final Component cancelButton = new JButton("Cancel account");
	private static final Component encryptButton = new JButton("Encryption mode");
	private static final Component saveButton = new JButton("save settings");
	private static final Component iconLabel= new JLabel();
	private static TableModel model = FileExchangeController.tableBuilder();
	private static final Component table = new JTable(model);
	private static final Component scrollPane = new JScrollPane(table);
	private static final Component fillerOne = new JButton();
	private static final Component fillerTwo = new JButton();
	private static final Component fillerThree = new JButton();
	private static final Component fillerFour = new JButton();
	private static final Component fillerFive = new JButton();
	private static final Component fillerSix = new JButton();
	private static final Component separator = new JSeparator(SwingConstants.VERTICAL);

	//Frame and panel
	private static final JPanel container = new JPanel();
	private GridBagConstraints limit;
//	private final static JFrame dialog = new JFrame();
	private final static JFrame frame = new JFrame();
	
	public KeyringView(){
		buildLayout();
		componentSettings();
		setHandlers();
		setFrame();
	}
	
	//BuildLayout same for all view
	private void buildLayout() {
		GlobalSettings set = null;
		set = new GlobalSettings();
		this.setButtonColor(set.getButtonColor());
		this.setFont(set.getFont());
		this.setForegroundColor(set.getForegroundColor());
		this.setPanelBakColor(set.getPanelBackColor());
		GridBagLayout layout = new GridBagLayout();
		limit = new GridBagConstraints();
		container.setLayout(layout);
		container.setBackground(panelBakColor);
	}
	
	private void setFrame() {
		try {
			frame.setIconImage(ImageIO.read(new File(
					"./res/isiCryptICON_MetroStyle.jpg")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		frame.setTitle("Keyring");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(920, 640);
		frame.getContentPane().add(container);
		frame.setVisible(true);
	}
	
	//Graphic Draws
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
		((JButton)fillerOne).setEnabled(false);
		setJButton(fillerOne, panelBakColor, panelBakColor, null, false, false);
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
		((JButton)fillerTwo).setEnabled(false);
		setJButton(fillerTwo, panelBakColor, panelBakColor, null, false, false);
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
		((JButton)fillerThree).setEnabled(false);
		setJButton(fillerThree, panelBakColor, panelBakColor, null, false, false);
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
		((JButton)fillerFour).setEnabled(false);
		setJButton(fillerFour, panelBakColor, panelBakColor, null, false, false);
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
		((JButton)fillerFive).setEnabled(false);
		setJButton(fillerFive, panelBakColor, panelBakColor, null, false, false);
		setLimit(limit, ipadDefaultx, ipadDefaulty, zeroInsets, 
				GridBagConstraints.BOTH, GridBagConstraints.CENTER, container, fillerFive);
		setGridposition(limit, xPosition, yPosition+10, defaultCellArea, defaultCellArea,
				noResizable, resizable, container, fillerFive);		
		((JLabel)iconLabel).setIcon(icon);
		setLimit(limit, zeroIpad, zeroIpad, zeroInsets, 
				GridBagConstraints.CENTER, GridBagConstraints.CENTER, container, iconLabel);
		setGridposition(limit, xPosition, yPosition+11, defaultCellArea, defaultCellArea,
				noResizable, noResizable, container, iconLabel);
		//FILLER SIX
		((JButton)fillerSix).setEnabled(false);
		setJButton(fillerSix, panelBakColor, panelBakColor, null, false, false);
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
	
	private void setHandlers(){
		
	}
	
	//GETTERS and SETTERS
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
}