package gui.views;
/**
 * @author Filippo Vimini
 * created 26/05/2014
 */
import gui.controllers.FileExchangeController;
import gui.controllers.IFileExchangeViewObserve;
import gui.models.GlobalSettings;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
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
import javax.swing.JFrame;
import javax.swing.JLabel;
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
	private Font font;
	private Color panelBakColor;
	private Color buttonColor;
	private Color foregroundColor;
	// Arrays that contains various dimension of insets
	private static final int insetsDefault[] = { 10, 10, 10, 10 };
	private static final int insetsChatArea[] = { 0, 0, 10, 10 };
	private static final int insetsSendButton[] = {0, 10, 10, 10};
	private static final int zeroInsets []= { 0, 0, 0, 0 };
	private static final int labelInsets [] = {10, 0, 10, 10};
	private static final JPanel container = new JPanel();
	private static final int zeroIpad  = 0;
	private static final int noResizable  = 0;
	private static final int resizable  = 1;
	private static final int ipadDefaulty = 30;
	private static final int ipadDefaultx = 65;
	private static final int xPosition = 0;
	private static final int yPosition = 0;
	private static final int defaultCellArea = 1;
	private static TableModel model = FileExchangeController.tableBuilder();
	GridBagConstraints limit;
	//GUI Component initialized columns x row order
	private final static Component datiLabel = new JLabel("Data");
	private final static Component fileButton = new JButton("Choose file to send");
	private final static Component stegaButton = new JButton("Send steganoghraphed image");
	private final static Component zipButton = new JButton("Send compressed file");
	private final static Component contactLabel = new JLabel("Contact");
	private final static Component addContactButton = new JButton("Add new Contact");
	private final static Component closeContactButton = new JButton("Back to Start");
	private final static Component changeContactButton = new JButton("Change contact");
	private final static Component sendStateLabel = new JLabel("Send state");
	private final static Component sendProgress = new JProgressBar();
	private final static Component separator = new JSeparator(SwingConstants.VERTICAL);
//	private final static JTextField visualTextArea = new JTextField();
	private final static JTextArea visualTextArea = new JTextArea();
	private final static Component scrollPaneVisual = new JScrollPane(visualTextArea);
	private final static JTextArea chatTextArea = new JTextArea(10,10);
	private final static Component scrollPaneChat = new JScrollPane(chatTextArea);
	private final static Component filler = new JButton("");
	private final static Component sendButton = new JButton("Send");
	private static JTable contactTable = new JTable(model);
	private final static Component scrollPaneTable = new JScrollPane(contactTable);
	private final static JFrame dialog = new JFrame();
	private final static JFrame frame = new JFrame();

	//Initialize GUI view observer
	private IFileExchangeViewObserve controller;
	//Builder
	public FileExchangeView(){
		buildLayout();
		componentSetting();
		setHandlers();
		setFrame();
	}
	//View Observer attaccher
	public void attacFileExchangeViewObserve(IFileExchangeViewObserve controller){
		this.controller = controller;
	}
	//Build layout, same for all gui
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

	private void componentSetting(){
		//Jlabel data
		((JLabel)datiLabel).setForeground(Color.white);
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
		((JButton)fileButton).setEnabled(false);
		//JButton stegaButton
		setJButton(stegaButton, buttonColor, foregroundColor, font, false, false);
		setLimit(limit, zeroIpad, ipadDefaulty, insetsDefault,
				GridBagConstraints.BOTH, GridBagConstraints.CENTER, container, stegaButton);
		setGridposition(limit, xPosition, yPosition+2, defaultCellArea, defaultCellArea, 
				noResizable, noResizable, container, stegaButton);
		((JButton)stegaButton).setEnabled(false);
		//JButton zip Button
		setJButton(zipButton, buttonColor, foregroundColor, font, false, false);
		setLimit(limit, ipadDefaultx, ipadDefaulty, insetsDefault, 
				GridBagConstraints.BOTH, GridBagConstraints.CENTER, container, zipButton);
		setGridposition(limit, xPosition, yPosition+3, defaultCellArea, defaultCellArea,
				noResizable, noResizable, container, zipButton);
		((JButton)zipButton).setEnabled(false);
		//JLabel contact
		((JLabel)contactLabel).setForeground(Color.white);
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
		((JButton)changeContactButton).setEnabled(false);
		//JButton close connection
		setJButton(closeContactButton, buttonColor, foregroundColor, font, false, false);
		setLimit(limit, ipadDefaultx, ipadDefaulty, insetsDefault,
				GridBagConstraints.BOTH, GridBagConstraints.CENTER, container, closeContactButton);
		setGridposition(limit, xPosition, yPosition+7, defaultCellArea, defaultCellArea,
				noResizable, noResizable, container, closeContactButton);
		((JButton)closeContactButton).setEnabled(false);
		//filler
		((JButton)filler).setVisible(false);
		setLimit(limit, zeroIpad, zeroIpad, zeroInsets, 
				GridBagConstraints.NONE, GridBagConstraints.SOUTH, container, filler);
		setGridposition(limit, xPosition, yPosition+8, defaultCellArea, defaultCellArea, 
				resizable, resizable, container, filler);
		//JLabel send state label
		((JLabel)sendStateLabel).setForeground(Color.white);
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
		((JScrollPane)scrollPaneVisual).setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
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
		((JScrollPane)scrollPaneChat).setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		Dimension chatAreaDimensiono = new Dimension(0,ipadDefaulty);
		((JScrollPane)scrollPaneChat).setMinimumSize(chatAreaDimensiono);
		((JScrollPane)scrollPaneChat).setPreferredSize(chatAreaDimensiono);
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
		((JButton)sendButton).setEnabled(false);
			
	}
	
	private void setHandlers(){
		//Select File to Send
		((JButton)fileButton).addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.selectFile();			
			}
		});
		//Select and Stenograph the image
		((JButton)stegaButton).addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.stegaImage();
			}
		});
		//Send Gzipped file
		((JButton)zipButton).addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.selectCompress();
			}
		});
		//Add new contact to HashSet in Model
		((JButton)addContactButton).addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.addContact();
			}
		});
		//Close current connection with abstract server
		((JButton)closeContactButton).addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.closeConnection();
			}
		});
		((JButton)changeContactButton).addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.changeContact();
			}
		});
		//Send chat text
		((JButton)sendButton).addActionListener(new ActionListener() {
			
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
	
	private void setFrame() {
		try {
			frame.setIconImage(ImageIO.read(new File(
					"./res/isiCryptICON_MetroStyle.jpg")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		frame.setTitle("FileExchange");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(920, 640);
		frame.getContentPane().add(container);
		//frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setVisible(true);
	}
	
	//Setter and Getter
	public void setFont(Font font) {
		this.font = font;
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
	
	//Component setter and getter
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
	
	public static JFrame getDialog() {
		return dialog;
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
 }
