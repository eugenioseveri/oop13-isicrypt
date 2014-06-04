package gui.views;
/**
 * @author Filippo Vimini
 * created 26/05/2014
 */
import gui.controllers.FileExchangeController;
import gui.controllers.IFileExchangeViewObserver;
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
import java.io.File;
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
import javax.swing.table.TableModel;

public class FileExchangeView extends AbstractGuiMethodSetter{
	
	private static final long serialVersionUID = -144598402405416549L;
	private Font font;
	private Color panelBackColor;
	private Color buttonColor;
	private Color foregroundColor;
	private static boolean isOpen;
	// Arrays that contains various dimension of insets
	private static final int insetsDefault[] = { 10, 10, 10, 10 };
	private static final int insetsChatArea[] = { 0, 0, 10, 10 };
	private static final int insetsSendButton[] = {0, 10, 10, 10};
	private static final int zeroInsets []= { 0, 0, 0, 0 };
	private static final int labelInsets [] = {10, 0, 10, 10};
	private static final int backInsets []= { 0, 0, 10, 10 };
	private static final int zeroIpad  = 0;
	private static final int noResizable  = 0;
	private static final int resizable  = 1;
	private static final int ipadDefaulty = 30;
	private static final int ipadDefaultx = 65;
	private static final int xPosition = 0;
	private static final int yPosition = 0;
	private static final int defaultCellArea = 1;
	private GridBagConstraints limit;
	//GUI Component initialized columns x row order
	private static final JButton backButton = new JButton("Show Start");
	private static final JPanel container = new JPanel();
	private final static JLabel datiLabel = new JLabel("Data");
	private final static JButton fileButton = new JButton("Choose file to send");
	private final static JButton stegaButton = new JButton("Send steganoghraphed image");
	private final static JButton zipButton = new JButton("Send compressed file");
	private final static JLabel contactLabel = new JLabel("Contact");
	private final static JButton addContactButton = new JButton("Add new Contact");
	private final static JButton closeContactButton = new JButton("Back to Start");
	private final static JButton changeContactButton = new JButton("Change contact");
	private final static JLabel sendStateLabel = new JLabel("Send state");
	private final static JProgressBar sendProgress = new JProgressBar();
	private final static JSeparator separator = new JSeparator(SwingConstants.VERTICAL);
//	private final static JTextField visualTextArea = new JTextField();
	private final static JTextArea visualTextArea = new JTextArea();
	private final static JScrollPane scrollPaneVisual = new JScrollPane(visualTextArea);
	private final static JTextArea chatTextArea = new JTextArea(10,10);
	private final static JScrollPane scrollPaneChat = new JScrollPane(chatTextArea);
	private final static JButton filler = new JButton("");
	private final static JButton sendButton = new JButton("Send");
	private static TableModel model = FileExchangeController.tableBuilder();
	private static JTable contactTable = new JTable(model);
	private final static JScrollPane scrollPaneTable = new JScrollPane(contactTable);
	private final static JFrame dialog = new JFrame();
	private static JFrame frame = new JFrame();

	//Initialize GUI view observer
	private IFileExchangeViewObserver controller;
	//Builder
	public FileExchangeView(){
		buildLayout();
		componentSetting();
		setHandlers();
		setFrame();
	}
	//View Observer attaccher
	public void attacFileExchangeViewObserve(IFileExchangeViewObserver controller){
		this.controller = controller;
	}
	//Build layout, same for all gui
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

	private void setFrame() {
		frame = new JFrame();
		try {
			frame.setIconImage(ImageIO.read(new File(
					"./res/isiCryptICON_MetroStyle.jpg")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		WindowAdapter listener = new WindowAdapter() {

	        @Override
	        public void windowOpened(WindowEvent e) {
	        	FileExchangeView.setOpen(true);	        }

	        @Override
	        public void windowClosing(WindowEvent e) {
	        	FileExchangeView.setOpen(false);
	        	//Try to close Server ad Server's thread
	        	FileExchangeController.closeThread();
	        	FileExchangeView.setOpen(false);
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
	private void componentSetting(){
		//JButton BACK TO START 
		setJButton(backButton, buttonColor, foregroundColor, font, false, false);
		setLimit(limit, ipadDefaultx-50, ipadDefaulty-10, backInsets,
				GridBagConstraints.CENTER, GridBagConstraints.WEST, container, backButton);
		setGridposition(limit, xPosition, yPosition , defaultCellArea, defaultCellArea,
				noResizable, noResizable, container, backButton);		
		//Jlabel data
		datiLabel.setForeground(Color.white);
		setLimit(limit, zeroIpad, zeroIpad, labelInsets, 
				GridBagConstraints.WEST, GridBagConstraints.WEST, container, datiLabel);
		setGridposition(limit, xPosition, yPosition, defaultCellArea, defaultCellArea,
				noResizable, noResizable, container, datiLabel);
		//JButton fileButton
		setJButton(fileButton, buttonColor, foregroundColor, font, false, false);
		setLimit(limit, ipadDefaultx, ipadDefaulty, insetsDefault,
				GridBagConstraints.BOTH, GridBagConstraints.CENTER, container, fileButton);
		setGridposition(limit, xPosition, yPosition+1, defaultCellArea, defaultCellArea, 
				noResizable, noResizable, container, fileButton);
		fileButton.setEnabled(false);
		//JButton stegaButton
		setJButton(stegaButton, buttonColor, foregroundColor, font, false, false);
		setLimit(limit, zeroIpad, ipadDefaulty, insetsDefault,
				GridBagConstraints.BOTH, GridBagConstraints.CENTER, container, stegaButton);
		setGridposition(limit, xPosition, yPosition+2, defaultCellArea, defaultCellArea, 
				noResizable, noResizable, container, stegaButton);
		stegaButton.setEnabled(false);
		//JButton zip Button
		setJButton(zipButton, buttonColor, foregroundColor, font, false, false);
		setLimit(limit, ipadDefaultx, ipadDefaulty, insetsDefault, 
				GridBagConstraints.BOTH, GridBagConstraints.CENTER, container, zipButton);
		setGridposition(limit, xPosition, yPosition+3, defaultCellArea, defaultCellArea,
				noResizable, noResizable, container, zipButton);
		zipButton.setEnabled(false);
		//JLabel contact
		contactLabel.setForeground(Color.white);
		setLimit(limit, zeroIpad, zeroIpad, labelInsets,
				GridBagConstraints.NONE, GridBagConstraints.WEST, container, contactLabel);
		setGridposition(limit, xPosition, yPosition+4, defaultCellArea, defaultCellArea,
				noResizable, noResizable, container, contactLabel);
		//JButton add new contact
		setJButton(addContactButton, buttonColor, foregroundColor, font, false, false);
		setLimit(limit, ipadDefaultx, ipadDefaulty, insetsDefault, 
				GridBagConstraints.BOTH, GridBagConstraints.CENTER, container, addContactButton);	
		setGridposition(limit, xPosition, yPosition+5, defaultCellArea, defaultCellArea,
				noResizable, noResizable, container, addContactButton);
		//JButton change contact
		setJButton(changeContactButton, buttonColor, foregroundColor, font, false, false);
		setLimit(limit, ipadDefaultx, ipadDefaulty, insetsDefault,
				GridBagConstraints.BOTH, GridBagConstraints.CENTER, container, changeContactButton);
		setGridposition(limit, xPosition, yPosition+6, defaultCellArea, defaultCellArea,
				noResizable, noResizable, container, changeContactButton);
		changeContactButton.setEnabled(false);
		//JButton close connection
		setJButton(closeContactButton, buttonColor, foregroundColor, font, false, false);
		setLimit(limit, ipadDefaultx, ipadDefaulty, insetsDefault,
				GridBagConstraints.BOTH, GridBagConstraints.CENTER, container, closeContactButton);
		setGridposition(limit, xPosition, yPosition+7, defaultCellArea, defaultCellArea,
				noResizable, noResizable, container, closeContactButton);
		closeContactButton.setEnabled(false);
		//filler
		setLimit(limit, zeroIpad, zeroIpad, zeroInsets, 
				GridBagConstraints.NONE, GridBagConstraints.SOUTH, container, filler);
		setGridposition(limit, xPosition, yPosition+8, defaultCellArea, defaultCellArea, 
				resizable, resizable, container, filler);
		filler.setVisible(false);
		//JLabel send state label
		sendStateLabel.setForeground(Color.white);
		setLimit(limit, zeroIpad, zeroIpad, labelInsets,
				GridBagConstraints.SOUTH, GridBagConstraints.WEST, container, sendStateLabel);
		setGridposition(limit, xPosition, yPosition+9, defaultCellArea, defaultCellArea,
				noResizable, noResizable, container, sendStateLabel);
		//JProgressbar Progress bar
		setLimit(limit, zeroIpad, zeroIpad, insetsDefault,
				GridBagConstraints.HORIZONTAL, GridBagConstraints.SOUTH, container, sendProgress);
		setGridposition(limit, xPosition, yPosition+10, defaultCellArea, defaultCellArea, 
				noResizable, noResizable, container, sendProgress);
		//JSeparator
		setLimit(limit, zeroIpad, zeroIpad, zeroInsets, 
				GridBagConstraints.BOTH, GridBagConstraints.SOUTH, container, separator);
		setGridposition(limit, xPosition+1, yPosition, defaultCellArea, defaultCellArea+8,
				noResizable, resizable, container, separator);
		//JTextPanel visual text area
		visualTextArea.setEditable(false);
		visualTextArea.setLineWrap(true);
		visualTextArea.setWrapStyleWord(true);
		visualTextArea.setFont(font);
		scrollPaneVisual.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPaneVisual.setVisible(false);
		setLimit(limit, zeroIpad, zeroIpad, insetsDefault, 
				GridBagConstraints.BOTH, GridBagConstraints.SOUTH, container, scrollPaneVisual);
		setGridposition(limit, xPosition+2, yPosition, defaultCellArea, defaultCellArea+8,
				resizable, resizable, container, scrollPaneVisual);
		//JTable
		contactTable.setFont(font);
		contactTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setLimit(limit, zeroIpad, zeroIpad, insetsDefault, 
				GridBagConstraints.BOTH, GridBagConstraints.SOUTH, container, scrollPaneTable);
		setGridposition(limit, xPosition+2, yPosition, defaultCellArea, defaultCellArea+8,
				resizable, resizable, container, scrollPaneTable);
		//JTextArea chat
		chatTextArea.setLineWrap(true);
		chatTextArea.setWrapStyleWord(true);
		chatTextArea.setFont(font);
		chatTextArea.setEnabled(false);
		scrollPaneChat.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		Dimension chatAreaDimensiono = new Dimension(0,ipadDefaulty);
		scrollPaneChat.setMinimumSize(chatAreaDimensiono);
		scrollPaneChat.setPreferredSize(chatAreaDimensiono);
		setLimit(limit, zeroIpad, ipadDefaulty, insetsChatArea, 
				GridBagConstraints.HORIZONTAL, GridBagConstraints.SOUTH, container, scrollPaneChat );
		setGridposition(limit, xPosition+2, yPosition+9, defaultCellArea+1, defaultCellArea,
				resizable, noResizable, container, scrollPaneChat);
		//JButton send text
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
				controller.selectCompress();
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
		closeContactButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.closeConnection();
			}
		});
		changeContactButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.changeContact();
			}
		});
		//Send chat text
		sendButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				controller.sendText();
			}
		});
		//JTable manage
		contactTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {	
			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				if(contactTable.getSelectedRow() >= 0){
					controller.selectContact();
					contactTable.clearSelection();
					contactTable.getSelectionModel().clearSelection();
				}
			}
		});
	}
	
	
	
	public static void optionPanel( Exception error ){
		JOptionPane.showMessageDialog(FileExchangeView.dialog, error);
	}
	
	public static void optionPanel( String error ){
		JOptionPane.showMessageDialog(FileExchangeView.dialog, error);
	}
	
	//Setter and Getter
	public void setFont(Font font) {
		this.font = font;
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
	
	//Component setter and getter
	//TODO: verificare cosa serve e cosa no
	public static JTextArea getVisualtextarea() {
		return visualTextArea;
	}
	
	public static JTextArea getChattextarea() {
		return chatTextArea;
	}
	
	public static JTable getContactTable() {
		return contactTable;
	}
	
	public static Component getScrollpanetable() {
		return scrollPaneTable;
	}

	public static JScrollPane getScrollpanevisual() {
		return (JScrollPane) scrollPaneVisual;
	}

	public static Component getFilebutton() {
		return fileButton;
	}
	public static Component getStegabutton() {
		return stegaButton;
	}
	public static Component getZipbutton() {
		return zipButton;
	}
	
	public static JFrame getFrame() {
		return frame;
	}

	public static void setContactTable(JTable contactTable) {
		FileExchangeView.contactTable = contactTable;
	}

	public static Component getClosecontactbutton() {
		return closeContactButton;
	}
	public static Component getChangecontactbutton() {
		return changeContactButton;
	}
	
	public static Component getAddcontactbutton() {
		return addContactButton;
	}
	
	public static Component getSendbutton() {
		return sendButton;
	}
	
	public static boolean isOpen() {
		return isOpen;
	}
	
	public static void setOpen(boolean isOpen) {
		FileExchangeView.isOpen = isOpen;
	}

 }
