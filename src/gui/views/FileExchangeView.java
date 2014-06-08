package gui.views;
/**
 * @author Filippo Vimini
 * created 26/05/2014
 */
import gui.controllers.FileExchangeController;
import gui.controllers.IFileExchangeViewObserver;
import gui.controllers.ThemeChooser;

import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class FileExchangeView extends AbstractGuiMethodSetter implements IFileExchangeView{
	
	private static final long serialVersionUID = -1686107590453441615L;
	//Path of resource 
	private final String APPLICATION_ICON = "isiCryptICON_MetroStyle.jpg";
	private Font font;
	private Color panelBackColor;
	private Color buttonColor;
	private Color foregroundColor;
	private static boolean isOpen;
	// Arrays that contains various dimension of insets
	private final int insetsDefault[] = { 10, 10, 10, 10 };
	private final int insetsChatArea[] = { 0, 0, 10, 10 };
	private final int insetsSendButton[] = {0, 10, 10, 10};
	private final int zeroInsets []= { 0, 0, 0, 0 };
	private final int labelInsets [] = {10, 0, 10, 10};
	private final int backInsets []= { 0, 0, 10, 10 };
	private final int zeroIpad  = 0;
	private final int noResizable  = 0;
	private final int resizable  = 1;
	private final int ipadDefaulty = 30;
	private final int ipadDefaultx = 65;
	private final int xPosition = 0;
	private final int yPosition = 0;
	private final int defaultCellArea = 1;
	private GridBagConstraints limit;
	//GUI Component initialized columns x row order
	private final JButton backButton = new JButton("Show Start");
	private final JPanel container = new JPanel();
	private final JLabel datiLabel = new JLabel("Data");
	private final JButton fileButton = new JButton("Choose file to send");
	private final JButton stegaButton = new JButton("Send steganoghraphed image");
	private final JButton zipButton = new JButton("Send compressed file");
	private final JLabel contactLabel = new JLabel("Contact");
	private final JButton addContactButton = new JButton("Add new Contact");
	private final JButton deleteContactButton = new JButton("Delete contact");
	private final JButton changeContactButton = new JButton("Change contact");
	private final JLabel sendStateLabel = new JLabel("Send state");
	private final JProgressBar sendProgress = new JProgressBar();
	private final JSeparator separator = new JSeparator(SwingConstants.VERTICAL);
	private final JTextArea visualTextArea = new JTextArea();
	private final JScrollPane scrollPaneVisual = new JScrollPane(visualTextArea);
	private final JTextArea chatTextArea = new JTextArea(10,10);
	private final JScrollPane scrollPaneChat = new JScrollPane(chatTextArea);
	private final JButton filler = new JButton("");
	private final JButton sendButton = new JButton("Send");
	private TableModel model = new DefaultTableModel();
	private final JTable contactTable = new JTable(model);
	private final JScrollPane scrollPaneTable = new JScrollPane(contactTable);
	private final JFrame dialog = new JFrame();
	private static JFrame frame = new JFrame();
	//Initialize GUI view observer
	private IFileExchangeViewObserver controller;
	
	//View Observer attacher
	@Override
	public void attachFileExchangeViewObserve(final IFileExchangeViewObserver controller){
		this.controller = controller;
	}
	
	//Builder
	public FileExchangeView(){
		buildLayout();
		componentSetting();
		setHandlers();
		setFrame();
	}
	/**
	 * Set color and font from file, because there are many theme,
	 *  and will be load the last used and set the layout
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
	 * Create a new Frame that contain the Component of the GUI
	 *  and set closing and opening operations
	 */
	private void setFrame() {
		frame = new JFrame();
		try {
			frame.setIconImage(ImageIO.read(ClassLoader.getSystemResourceAsStream(APPLICATION_ICON)));
		} catch (IOException e) {
			optionPanel(e);
		}
		//Set action when frame is open and close
		final WindowAdapter listener = new WindowAdapter() {

	        @Override
	        public void windowOpened(WindowEvent e) {
	        	setOpen(true);	        }

	        @Override
	        public void windowClosing(WindowEvent e) {
	        	setOpen(false);
	    		controller.setEnableButton(false);
	        	//Try to close Server ad Server's thread
	        	controller.closeThread();
	        	//repaint the start screen
	        	StartScreenView.redraw();
	        }
	    };
		frame.setTitle("FileExchange");
		frame.setSize(920, 640);
		frame.getContentPane().add(container);
		frame.setVisible(true);
	    frame.addWindowListener(listener);
	}
	/**
	 * Method that create a Graphic, is optimized for GridBagLayout
	 */
	private void componentSetting(){
		//JBUTTON BACK TO START 
		setJButton(backButton, buttonColor, foregroundColor, font, false, false);
		setLimit(limit, ipadDefaultx-50, ipadDefaulty-10, backInsets,
				GridBagConstraints.CENTER, GridBagConstraints.WEST, container, backButton);
		setGridposition(limit, xPosition, yPosition , defaultCellArea, defaultCellArea,
				noResizable, noResizable, container, backButton);		
		//JLABEL DATA
		datiLabel.setForeground(Color.white);
		setLimit(limit, zeroIpad, zeroIpad, labelInsets, 
				GridBagConstraints.WEST, GridBagConstraints.WEST, container, datiLabel);
		setGridposition(limit, xPosition, yPosition, defaultCellArea, defaultCellArea,
				noResizable, noResizable, container, datiLabel);
		//JBUTTON SELECT FILE BUTTON
		setJButton(fileButton, buttonColor, foregroundColor, font, false, false);
		setLimit(limit, ipadDefaultx, ipadDefaulty, insetsDefault,
				GridBagConstraints.BOTH, GridBagConstraints.CENTER, container, fileButton);
		setGridposition(limit, xPosition, yPosition+1, defaultCellArea, defaultCellArea, 
				noResizable, noResizable, container, fileButton);
		fileButton.setEnabled(false);
		//JBUTTON SELECT STEGANOGRAPHED IMAGE BUTTON
		setJButton(stegaButton, buttonColor, foregroundColor, font, false, false);
		setLimit(limit, zeroIpad, ipadDefaulty, insetsDefault,
				GridBagConstraints.BOTH, GridBagConstraints.CENTER, container, stegaButton);
		setGridposition(limit, xPosition, yPosition+2, defaultCellArea, defaultCellArea, 
				noResizable, noResizable, container, stegaButton);
		stegaButton.setEnabled(false);
		//JBUTTON SEND ZIPPED FILE BUTTON
		setJButton(zipButton, buttonColor, foregroundColor, font, false, false);
		setLimit(limit, ipadDefaultx, ipadDefaulty, insetsDefault, 
				GridBagConstraints.BOTH, GridBagConstraints.CENTER, container, zipButton);
		setGridposition(limit, xPosition, yPosition+3, defaultCellArea, defaultCellArea,
				noResizable, noResizable, container, zipButton);
		zipButton.setEnabled(false);
		//JLABEL CONTACT 
		contactLabel.setForeground(buttonColor);
		setLimit(limit, zeroIpad, zeroIpad, labelInsets,
				GridBagConstraints.NONE, GridBagConstraints.WEST, container, contactLabel);
		setGridposition(limit, xPosition, yPosition+4, defaultCellArea, defaultCellArea,
				noResizable, noResizable, container, contactLabel);
		//JBUTTON ADD NEW CONTACT
		setJButton(addContactButton, buttonColor, foregroundColor, font, false, false);
		setLimit(limit, ipadDefaultx, ipadDefaulty, insetsDefault, 
				GridBagConstraints.BOTH, GridBagConstraints.CENTER, container, addContactButton);	
		setGridposition(limit, xPosition, yPosition+5, defaultCellArea, defaultCellArea,
				noResizable, noResizable, container, addContactButton);
		//JBUTTON CHANGE CONTACT
		setJButton(changeContactButton, buttonColor, foregroundColor, font, false, false);
		setLimit(limit, ipadDefaultx, ipadDefaulty, insetsDefault,
				GridBagConstraints.BOTH, GridBagConstraints.CENTER, container, changeContactButton);
		setGridposition(limit, xPosition, yPosition+6, defaultCellArea, defaultCellArea,
				noResizable, noResizable, container, changeContactButton);
		changeContactButton.setEnabled(false);
		//JBUTTON DELETE CONTACT
		setJButton(deleteContactButton, buttonColor, foregroundColor, font, false, false);
		setLimit(limit, ipadDefaultx, ipadDefaulty, insetsDefault,
				GridBagConstraints.BOTH, GridBagConstraints.CENTER, container, deleteContactButton);
		setGridposition(limit, xPosition, yPosition+7, defaultCellArea, defaultCellArea,
				noResizable, noResizable, container, deleteContactButton);
		deleteContactButton.setEnabled(true);
		//FILLER
		setLimit(limit, zeroIpad, zeroIpad, zeroInsets, 
				GridBagConstraints.NONE, GridBagConstraints.SOUTH, container, filler);
		setGridposition(limit, xPosition, yPosition+8, defaultCellArea, defaultCellArea, 
				resizable, resizable, container, filler);
		filler.setVisible(false);
		//JLABEL SEND STATE LABEL
		sendStateLabel.setForeground(Color.white);
		setLimit(limit, zeroIpad, zeroIpad, labelInsets,
				GridBagConstraints.SOUTH, GridBagConstraints.WEST, container, sendStateLabel);
		setGridposition(limit, xPosition, yPosition+9, defaultCellArea, defaultCellArea,
				noResizable, noResizable, container, sendStateLabel);
		//JPROGRESSBAR SEND STATUS PROGRESSBAR
		setLimit(limit, zeroIpad, zeroIpad, insetsDefault,
				GridBagConstraints.HORIZONTAL, GridBagConstraints.SOUTH, container, sendProgress);
		setGridposition(limit, xPosition, yPosition+10, defaultCellArea, defaultCellArea, 
				noResizable, noResizable, container, sendProgress);
		//JSEPARATOR for divide button from table/text area
		setLimit(limit, zeroIpad, zeroIpad, zeroInsets, 
				GridBagConstraints.BOTH, GridBagConstraints.SOUTH, container, separator);
		setGridposition(limit, xPosition+1, yPosition, defaultCellArea, defaultCellArea+8,
				noResizable, resizable, container, separator);
		//JTEXTAREA/SCROLLPANE TEXT AREA AND SCROLLPANE stamp text send/received during the conversation
		visualTextArea.setEditable(false);
		visualTextArea.setLineWrap(true);
		visualTextArea.setWrapStyleWord(true);
		visualTextArea.setFont(font);
		visualTextArea.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		scrollPaneVisual.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPaneVisual.setVisible(false);
		setLimit(limit, zeroIpad, zeroIpad, insetsDefault, 
				GridBagConstraints.BOTH, GridBagConstraints.SOUTH, container, scrollPaneVisual);
		setGridposition(limit, xPosition+2, yPosition, defaultCellArea, defaultCellArea+8,
				resizable, resizable, container, scrollPaneVisual);
		//JTABLE TABLE that contain and show the HashMap with user 
		contactTable.setFont(font);
		contactTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setLimit(limit, zeroIpad, zeroIpad, insetsDefault, 
				GridBagConstraints.BOTH, GridBagConstraints.SOUTH, container, scrollPaneTable);
		setGridposition(limit, xPosition+2, yPosition, defaultCellArea, defaultCellArea+8,
				resizable, resizable, container, scrollPaneTable);
		//JTEXTAREA TEXTAREA for write text
		chatTextArea.setLineWrap(true);
		chatTextArea.setWrapStyleWord(true);
		chatTextArea.setFont(font);
		//Set disable because first it must be visible the table
		chatTextArea.setEnabled(false);
		scrollPaneChat.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		final Dimension chatAreaDimensiono = new Dimension(0,ipadDefaulty);
		scrollPaneChat.setMinimumSize(chatAreaDimensiono);
		scrollPaneChat.setPreferredSize(chatAreaDimensiono);
		setLimit(limit, zeroIpad, ipadDefaulty, insetsChatArea, 
				GridBagConstraints.HORIZONTAL, GridBagConstraints.SOUTH, container, scrollPaneChat );
		setGridposition(limit, xPosition+2, yPosition+9, defaultCellArea+1, defaultCellArea,
				resizable, noResizable, container, scrollPaneChat);
		//JBUTTON SEND TEXT write on chat area
		setJButton(sendButton, buttonColor, foregroundColor, font, false, false);
		setLimit(limit, ipadDefaultx, ipadDefaulty-20, insetsSendButton, 
				GridBagConstraints.WEST, GridBagConstraints.SOUTHEAST, container, sendButton);
		setGridposition(limit, xPosition+2, yPosition+10, defaultCellArea, defaultCellArea, 
				noResizable, noResizable, container, sendButton);
		sendButton.setEnabled(false);
	}
	
	private void setHandlers(){
		//back to Start View
		backButton.addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent arg0) {
				((FileExchangeController) controller).showStart();
			}
		});
		//Select File to Send
		fileButton.addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.selectFile();			
			}
		});
		//Select and Stenograph the image
		stegaButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.stegaImage();
			}
		});
		//Send Gzipped file
		zipButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.selectCompressedFile();
			}
		});
		//Add new contact to HashSet in Model
		addContactButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.addContact();
			}
		});
		//Close current connection with abstract server
		deleteContactButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.deleteContact();
			}
		});
		//Change current connection and show Jtable for choose new contact
		changeContactButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.changeContact();
			}
		});
		//Send chat text
		sendButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.sendText();
			}
		});
		//JTable manage
		contactTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {	
			@Override
			public void valueChanged(ListSelectionEvent arg0) {
					controller.selectContact();
			}
		});
	}
	
	//the dialog for show the error 
	@Override
	public void optionPanel(final Object error ){
		if(error instanceof Exception)JOptionPane.showMessageDialog(this.dialog, error);
		if(error instanceof String)JOptionPane.showMessageDialog(this.dialog, error);
	}
	
	//Component setter and getter
	@Override
	public JTextArea getVisualtextarea() {
		return visualTextArea;
	}
	@Override
	public JTextArea getChattextarea() {
		return chatTextArea;
	}
	@Override
	public JTable getContactTable() {
		return contactTable;
	}
	@Override
	public Component getScrollpanetable() {
		return scrollPaneTable;
	}
	@Override
	public JScrollPane getScrollpanevisual() {
		return (JScrollPane) scrollPaneVisual;
	}
	@Override
	public JButton getFilebutton() {
		return fileButton;
	}
	@Override
	public JButton getStegabutton() {
		return stegaButton;
	}
	@Override
	public JButton getZipbutton() {
		return zipButton;
	}
	@Override
	public JFrame getFrame() {
		return frame;
	}
	@Override
	public JButton getDeleteContactButton() {
		return deleteContactButton;
	}
	@Override
	public JButton getChangecontactbutton() {
		return changeContactButton;
	}
	@Override
	public JButton getAddcontactbutton() {
		return addContactButton;
	}
	@Override
	public JButton getSendbutton() {
		return sendButton;
	}
	@Override
	public TableModel getModel() {
		return model;
	}
	@Override
	public void setSendprogress(int value) {
		sendProgress.getModel().setValue(value);
	}
	//boolean for control if "JFrame" is close or open
	public static boolean isOpen() {
		return isOpen;
	}

	private static void setOpen(boolean isOpen) {
		FileExchangeView.isOpen = isOpen;
	}
	@Override
	public String setOptionPane(final String name, final String text){
		final String back = JOptionPane.showInputDialog(name, text);
		return back;
	}
 }