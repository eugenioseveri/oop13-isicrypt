 package gui.views;

import gui.controllers.IStartScreenViewObserver;
import gui.controllers.StartScreenController;
import gui.controllers.ThemeChooser;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class StartScreenView extends AbstractGuiMethodSetter{
	private static final long serialVersionUID = 4934353268761561329L;
	private static final String APPLICATION_ICON = "isiCryptICON_MetroStyle.jpg";
	private static Font font;
	private static Color panelBackColor;
	private static Color buttonColor;
	private static Color foregroundColor;
	// Arrays that contains various dimension of insets
	private static final int insetsTop[] = { 0, 10, 20, 20 };
	private static final int insetsBotton[] = { 20, 0, 20, 20 };
	private static final int insetsCredit[] = { -20, -40, 10, 0 };
	private static final int noResizable  = 0;
	private static final int ipadDefaulty = 90;
	private static final int ipadDefaultx = 80;
	private static final int xPosition = 0;
	private static final int yPosition = 0;
	private static final int defaultCellArea = 1;
	private static GridBagConstraints limit;
	//GUI Component initialized columns x row order
	private static final JPanel container = new JPanel();
	private static final JFrame frame = new JFrame();
	private static final JButton theme = new JButton("theme");
	private static final JButton cryptography = new JButton("cryptography");
	private static final JButton steganography = new JButton("steganography");
	private static final JButton keyring = new JButton("keyring");
	private static final JButton fileExchange = new JButton("fileExchange");
	private static final JLabel authorName = new JLabel("<html>Filippo Vimini<br>........Eugenio Severi</html>");
	private static final JFrame dialog = new JFrame();

	//Initialize GUI view observer
	private IStartScreenViewObserver controller;
	
	public void attacStartScreenViewObserver(final IStartScreenViewObserver controller){
		this.controller = controller;
	}
	
	public StartScreenView(){
		super();
		final StartScreenController controller = new StartScreenController();
		controller.setView(this);
		buildLayout();
		componentSetting();
		setHandlers();
		setFrame();
	}
	
	private static void buildLayout() {
		//set all graphic parameters
		new ThemeChooser();
		dialog.getContentPane().setBackground(panelBackColor);
		buttonColor = ThemeChooser.getButtonColor();
		font = ThemeChooser.getFont();
		foregroundColor = ThemeChooser.getForegroundColor();
		panelBackColor = ThemeChooser.getPanelBackColor();
		final GridBagLayout layout = new GridBagLayout();
		limit = new GridBagConstraints();
		container.setLayout(layout);
		container.setBackground(panelBackColor);
	}
	
	private static void setFrame() {
		frame.getContentPane().removeAll();
		try {
			frame.setIconImage(ImageIO.read(ClassLoader.getSystemResourceAsStream(APPLICATION_ICON)));
		} catch (IOException e) {
			JOptionPane.showMessageDialog(dialog, e);
		}
		frame.setResizable(false);
		frame.setTitle("Start Screen");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(450, 350);
		frame.getContentPane().add(container);
		frame.setVisible(true);
	}
	
	private static void componentSetting(){
		//JButton CRYPTOGRAPHY
		setJButton(cryptography, buttonColor, foregroundColor, font, false, false);
		setLimit(limit, ipadDefaultx, ipadDefaulty, insetsTop, 
				GridBagConstraints.BOTH, GridBagConstraints.NORTH, container, cryptography);
		setGridposition(limit, xPosition, yPosition, defaultCellArea, defaultCellArea, 
				noResizable, noResizable, container, cryptography);
		if(CryptographyView.isOpen()){
			cryptography.setEnabled(false);
		}
		else {
			cryptography.setEnabled(true);
		}
		//JButton STEGANOGRAPHY
		setJButton(steganography, buttonColor, foregroundColor, font, false, false);
		setLimit(limit, ipadDefaultx, ipadDefaulty, insetsTop, 
				GridBagConstraints.BOTH, GridBagConstraints.NORTH, container, steganography);
		setGridposition(limit, xPosition+1, yPosition, defaultCellArea, defaultCellArea, 
				noResizable, noResizable, container, steganography);	
		if(SteganographyView.isOpen()){
			steganography.setEnabled(false);
		}
		else {
			steganography.setEnabled(true);
		}
		//JButton KEYRING
		setJButton(keyring, buttonColor, foregroundColor, font, false, false);
		setLimit(limit, ipadDefaultx, ipadDefaulty, insetsBotton, 
				GridBagConstraints.BOTH, GridBagConstraints.NORTH, container, keyring);
		setGridposition(limit, xPosition, yPosition+1, defaultCellArea, defaultCellArea, 
				noResizable, noResizable, container, keyring);	
		if(KeyringView.isOpen()){
			keyring.setEnabled(false);
		}
		else{
			keyring.setEnabled(true);
		}
		//JButton FILEEXCHANGE
		setJButton(fileExchange, buttonColor, foregroundColor, font, false, false);
		setLimit(limit, ipadDefaultx, ipadDefaulty, insetsBotton, 
				GridBagConstraints.BOTH, GridBagConstraints.NORTH, container, fileExchange);
		setGridposition(limit, xPosition+1, yPosition+1, defaultCellArea, defaultCellArea, 
				noResizable, noResizable, container, fileExchange);	
		if(FileExchangeView.isOpen()){
			fileExchange.setEnabled(false);
		}
		else{
			fileExchange.setEnabled(true);
		}
		//JLabel CREDITS
		authorName.setFont(new Font("Verdana",Font.BOLD, 12));
		authorName.setForeground(buttonColor);
		setLimit(limit, 0, 0, insetsCredit, 
				GridBagConstraints.SOUTH, GridBagConstraints.EAST, container, authorName);
		setGridposition(limit, xPosition+1, yPosition+2, defaultCellArea, defaultCellArea, 
				noResizable, noResizable, container, authorName);
		//JButton theme
		setJButton(theme, buttonColor, foregroundColor, font, false, false);
		setLimit(limit, ipadDefaultx-50, ipadDefaulty-90, insetsBotton,
				GridBagConstraints.CENTER, GridBagConstraints.WEST, container, theme);		
		setGridposition(limit, xPosition, yPosition+2, defaultCellArea, defaultCellArea, 
				noResizable, noResizable, container, theme);
		
	}
	
	private void setHandlers(){
		//select CRYPTOGRAPHY
		cryptography.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent arg0) {
				controller.selectCryptography();
			}
		});
		//select STEGANOGRAPHY
		steganography.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent arg0) {
				controller.selectSteganography();
			}
		});
		keyring.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent arg0) {
				controller.selectKeyring();
			}
		});
		fileExchange.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent arg0) {
				controller.selectFileExchange();
			}
		});
		theme.addActionListener(new ActionListener(	) {
			@Override
			public void actionPerformed(final ActionEvent arg0) {
				controller.selectTheme();
				
			}
		});
	}
	
	//initialize again many method for draw the start screen
	public static void redraw() {
		buildLayout();
		componentSetting();
		setFrame();
	}
	
	//GETTERS 
	public Font getFont() {
		return font;
	}

	public Color getPanelBakColor() {
		return panelBackColor;
	}

	public Color getButtonColor() {
		return buttonColor;
	}
	
	public Color getForegroundColor() {
		return foregroundColor;
	}
	
	public static JFrame getFrame() {
		return frame;
	}
	
	public static JFrame getDialog() {
		return dialog;
	}
}