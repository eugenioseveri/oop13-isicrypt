package gui.views;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.GridBagLayout;

import javax.swing.JLabel;

import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JComboBox;

import java.awt.Font;

import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JProgressBar;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.JTextArea;

import algorithms.EnumAvailableSymmetricAlgorithms;

import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CryptographyView extends JFrame {

	private static final long serialVersionUID = 8776796811456991741L;
	private JTextField textField_FileToEncrypt;
	private JButton button_SelectFileToEncrypt;
	private JTextField textField_FileToDecrypt;
	private JButton button_SelectFileToDecrypt;
	private JTextField textField_OutputFileEncrypt;
	private JButton button_SelectOutputFileEncrypt;
	private JTextField textField_OutputFileDecrypt;
	private JButton button_SelectOutputFileDecrypt;
	private JTextField textField_PublicKey;
	private JButton button_SelectPublicKeyFile;
	private JTextField textField_PrivateKey;
	private JButton button_SelectPrivateKey;
	private JComboBox<EnumAvailableSymmetricAlgorithms> comboBox_SymmetricAlgorithm;
	private JButton btnDecrypt;
	private JProgressBar progressBar_Decrypt;
	private JComboBox<EnumAvailableSymmetricAlgorithms> comboBox_HashingAlgorithm;
	private JTextArea txtrLog;
	private JComboBox<EnumAvailableSymmetricAlgorithms> comboBox_CompressionAlgorithm;
	private JButton btnEncrypt;
	private JProgressBar progressBar_Encrypt;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CryptographyView frame = new CryptographyView();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public CryptographyView() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Da modificare
		setBounds(100, 100, 600, 280);
		buildLayout();
		setHandlers();
	}
	
	private void buildLayout() {
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{0, 79, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_contentPane.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_contentPane.columnWeights = new double[]{1.0, 1.0, 0.0, 1.0, 0.0, 1.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		JLabel lblEncryption = new JLabel("Encryption");
		lblEncryption.setFont(new Font("Tahoma", Font.BOLD, 12));
		GridBagConstraints gbc_lblEncryption = new GridBagConstraints();
		gbc_lblEncryption.gridwidth = 4;
		gbc_lblEncryption.insets = new Insets(0, 0, 5, 5);
		gbc_lblEncryption.gridx = 0;
		gbc_lblEncryption.gridy = 0;
		contentPane.add(lblEncryption, gbc_lblEncryption);
		
		JLabel lblDecryption = new JLabel("Decryption");
		lblDecryption.setFont(new Font("Tahoma", Font.BOLD, 12));
		GridBagConstraints gbc_lblDecryption = new GridBagConstraints();
		gbc_lblDecryption.gridwidth = 4;
		gbc_lblDecryption.insets = new Insets(0, 0, 5, 0);
		gbc_lblDecryption.gridx = 5;
		gbc_lblDecryption.gridy = 0;
		contentPane.add(lblDecryption, gbc_lblDecryption);
		
		JLabel lblFileToEncrypt = new JLabel("File to encrypt:");
		GridBagConstraints gbc_lblFileToEncrypt = new GridBagConstraints();
		gbc_lblFileToEncrypt.anchor = GridBagConstraints.EAST;
		gbc_lblFileToEncrypt.insets = new Insets(0, 0, 5, 5);
		gbc_lblFileToEncrypt.gridx = 0;
		gbc_lblFileToEncrypt.gridy = 1;
		contentPane.add(lblFileToEncrypt, gbc_lblFileToEncrypt);
		
		textField_FileToEncrypt = new JTextField();
		GridBagConstraints gbc_textField_FileToEncrypt = new GridBagConstraints();
		gbc_textField_FileToEncrypt.gridwidth = 2;
		gbc_textField_FileToEncrypt.insets = new Insets(0, 0, 5, 5);
		gbc_textField_FileToEncrypt.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_FileToEncrypt.gridx = 1;
		gbc_textField_FileToEncrypt.gridy = 1;
		contentPane.add(textField_FileToEncrypt, gbc_textField_FileToEncrypt);
		textField_FileToEncrypt.setColumns(10);
		
		button_SelectFileToEncrypt = new JButton("...");
		GridBagConstraints gbc_button_SelectFileToEncrypt = new GridBagConstraints();
		gbc_button_SelectFileToEncrypt.insets = new Insets(0, 0, 5, 5);
		gbc_button_SelectFileToEncrypt.gridx = 3;
		gbc_button_SelectFileToEncrypt.gridy = 1;
		contentPane.add(button_SelectFileToEncrypt, gbc_button_SelectFileToEncrypt);
		
		JSeparator separator = new JSeparator();
		separator.setOrientation(SwingConstants.VERTICAL);
		GridBagConstraints gbc_separator = new GridBagConstraints();
		gbc_separator.fill = GridBagConstraints.VERTICAL;
		gbc_separator.gridheight = 8;
		gbc_separator.insets = new Insets(0, 0, 0, 0);
		gbc_separator.gridx = 4;
		gbc_separator.gridy = 0;
		gbc_separator.weightx = 1;
		gbc_separator.ipadx = 20;
		contentPane.add(separator, gbc_separator);
		
		JLabel lblFileToDecrypt = new JLabel("File to decrypt:");
		GridBagConstraints gbc_lblFileToDecrypt = new GridBagConstraints();
		gbc_lblFileToDecrypt.anchor = GridBagConstraints.EAST;
		gbc_lblFileToDecrypt.insets = new Insets(0, 0, 5, 5);
		gbc_lblFileToDecrypt.gridx = 5;
		gbc_lblFileToDecrypt.gridy = 1;
		contentPane.add(lblFileToDecrypt, gbc_lblFileToDecrypt);
		
		textField_FileToDecrypt = new JTextField();
		GridBagConstraints gbc_textField_FileToDecrypt = new GridBagConstraints();
		gbc_textField_FileToDecrypt.gridwidth = 2;
		gbc_textField_FileToDecrypt.insets = new Insets(0, 0, 5, 5);
		gbc_textField_FileToDecrypt.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_FileToDecrypt.gridx = 6;
		gbc_textField_FileToDecrypt.gridy = 1;
		contentPane.add(textField_FileToDecrypt, gbc_textField_FileToDecrypt);
		textField_FileToDecrypt.setColumns(10);
		
		button_SelectFileToDecrypt = new JButton("...");
		GridBagConstraints gbc_button_SelectFileToDecrypt = new GridBagConstraints();
		gbc_button_SelectFileToDecrypt.insets = new Insets(0, 0, 5, 0);
		gbc_button_SelectFileToDecrypt.gridx = 8;
		gbc_button_SelectFileToDecrypt.gridy = 1;
		contentPane.add(button_SelectFileToDecrypt, gbc_button_SelectFileToDecrypt);
		
		JLabel lblOutputFileEncrypt = new JLabel("Output file:");
		GridBagConstraints gbc_lblOutputFileEncrypt = new GridBagConstraints();
		gbc_lblOutputFileEncrypt.anchor = GridBagConstraints.EAST;
		gbc_lblOutputFileEncrypt.insets = new Insets(0, 0, 5, 5);
		gbc_lblOutputFileEncrypt.gridx = 0;
		gbc_lblOutputFileEncrypt.gridy = 2;
		contentPane.add(lblOutputFileEncrypt, gbc_lblOutputFileEncrypt);
		
		textField_OutputFileEncrypt = new JTextField();
		GridBagConstraints gbc_textField_OutputFileEncrypt = new GridBagConstraints();
		gbc_textField_OutputFileEncrypt.gridwidth = 2;
		gbc_textField_OutputFileEncrypt.insets = new Insets(0, 0, 5, 5);
		gbc_textField_OutputFileEncrypt.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_OutputFileEncrypt.gridx = 1;
		gbc_textField_OutputFileEncrypt.gridy = 2;
		contentPane.add(textField_OutputFileEncrypt, gbc_textField_OutputFileEncrypt);
		textField_OutputFileEncrypt.setColumns(10);
		
		button_SelectOutputFileEncrypt = new JButton("...");
		GridBagConstraints gbc_button_SelectOutputFileEncrypt = new GridBagConstraints();
		gbc_button_SelectOutputFileEncrypt.insets = new Insets(0, 0, 5, 5);
		gbc_button_SelectOutputFileEncrypt.gridx = 3;
		gbc_button_SelectOutputFileEncrypt.gridy = 2;
		contentPane.add(button_SelectOutputFileEncrypt, gbc_button_SelectOutputFileEncrypt);
		
		JLabel lblOutputFileDecrypt = new JLabel("Output file:");
		GridBagConstraints gbc_lblOutputFileDecrypt = new GridBagConstraints();
		gbc_lblOutputFileDecrypt.anchor = GridBagConstraints.EAST;
		gbc_lblOutputFileDecrypt.insets = new Insets(0, 0, 5, 5);
		gbc_lblOutputFileDecrypt.gridx = 5;
		gbc_lblOutputFileDecrypt.gridy = 2;
		contentPane.add(lblOutputFileDecrypt, gbc_lblOutputFileDecrypt);
		
		textField_OutputFileDecrypt = new JTextField();
		GridBagConstraints gbc_textField_OutputFileDecrypt = new GridBagConstraints();
		gbc_textField_OutputFileDecrypt.gridwidth = 2;
		gbc_textField_OutputFileDecrypt.insets = new Insets(0, 0, 5, 5);
		gbc_textField_OutputFileDecrypt.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_OutputFileDecrypt.gridx = 6;
		gbc_textField_OutputFileDecrypt.gridy = 2;
		contentPane.add(textField_OutputFileDecrypt, gbc_textField_OutputFileDecrypt);
		textField_OutputFileDecrypt.setColumns(10);
		
		button_SelectOutputFileDecrypt = new JButton("...");
		GridBagConstraints gbc_button_SelectOutputFileDecrypt = new GridBagConstraints();
		gbc_button_SelectOutputFileDecrypt.insets = new Insets(0, 0, 5, 0);
		gbc_button_SelectOutputFileDecrypt.gridx = 8;
		gbc_button_SelectOutputFileDecrypt.gridy = 2;
		contentPane.add(button_SelectOutputFileDecrypt, gbc_button_SelectOutputFileDecrypt);
		
		JLabel lblPublicKey = new JLabel("Public key:");
		GridBagConstraints gbc_lblPublicKey = new GridBagConstraints();
		gbc_lblPublicKey.anchor = GridBagConstraints.EAST;
		gbc_lblPublicKey.insets = new Insets(0, 0, 5, 5);
		gbc_lblPublicKey.gridx = 0;
		gbc_lblPublicKey.gridy = 3;
		contentPane.add(lblPublicKey, gbc_lblPublicKey);
		
		textField_PublicKey = new JTextField();
		GridBagConstraints gbc_textField_PublicKey = new GridBagConstraints();
		gbc_textField_PublicKey.gridwidth = 2;
		gbc_textField_PublicKey.insets = new Insets(0, 0, 5, 5);
		gbc_textField_PublicKey.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_PublicKey.gridx = 1;
		gbc_textField_PublicKey.gridy = 3;
		contentPane.add(textField_PublicKey, gbc_textField_PublicKey);
		textField_PublicKey.setColumns(10);
		
		button_SelectPublicKeyFile = new JButton("...");
		GridBagConstraints gbc_button_SelectPublicKeyFile = new GridBagConstraints();
		gbc_button_SelectPublicKeyFile.insets = new Insets(0, 0, 5, 5);
		gbc_button_SelectPublicKeyFile.gridx = 3;
		gbc_button_SelectPublicKeyFile.gridy = 3;
		contentPane.add(button_SelectPublicKeyFile, gbc_button_SelectPublicKeyFile);
		
		JLabel lblPrivateKey = new JLabel("Private Key:");
		GridBagConstraints gbc_lblPrivateKey = new GridBagConstraints();
		gbc_lblPrivateKey.anchor = GridBagConstraints.EAST;
		gbc_lblPrivateKey.insets = new Insets(0, 0, 5, 5);
		gbc_lblPrivateKey.gridx = 5;
		gbc_lblPrivateKey.gridy = 3;
		contentPane.add(lblPrivateKey, gbc_lblPrivateKey);
		
		textField_PrivateKey = new JTextField();
		GridBagConstraints gbc_textField_PrivateKey = new GridBagConstraints();
		gbc_textField_PrivateKey.gridwidth = 2;
		gbc_textField_PrivateKey.insets = new Insets(0, 0, 5, 5);
		gbc_textField_PrivateKey.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_PrivateKey.gridx = 6;
		gbc_textField_PrivateKey.gridy = 3;
		contentPane.add(textField_PrivateKey, gbc_textField_PrivateKey);
		textField_PrivateKey.setColumns(10);
		
		button_SelectPrivateKey = new JButton("...");
		GridBagConstraints gbc_button_SelectPrivateKey = new GridBagConstraints();
		gbc_button_SelectPrivateKey.insets = new Insets(0, 0, 5, 0);
		gbc_button_SelectPrivateKey.gridx = 8;
		gbc_button_SelectPrivateKey.gridy = 3;
		contentPane.add(button_SelectPrivateKey, gbc_button_SelectPrivateKey);
		
		JLabel lblSymmetricAlgorithm = new JLabel("Algorithm:");
		GridBagConstraints gbc_lblSymmetricAlgorithm = new GridBagConstraints();
		gbc_lblSymmetricAlgorithm.insets = new Insets(0, 0, 5, 5);
		gbc_lblSymmetricAlgorithm.gridx = 0;
		gbc_lblSymmetricAlgorithm.gridy = 4;
		contentPane.add(lblSymmetricAlgorithm, gbc_lblSymmetricAlgorithm);
		
		comboBox_SymmetricAlgorithm = new JComboBox<EnumAvailableSymmetricAlgorithms>();
		GridBagConstraints gbc_comboBox_SymmetricAlgorithm = new GridBagConstraints();
		gbc_comboBox_SymmetricAlgorithm.gridwidth = 3;
		gbc_comboBox_SymmetricAlgorithm.insets = new Insets(0, 0, 5, 5);
		gbc_comboBox_SymmetricAlgorithm.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox_SymmetricAlgorithm.gridx = 1;
		gbc_comboBox_SymmetricAlgorithm.gridy = 4;
		contentPane.add(comboBox_SymmetricAlgorithm, gbc_comboBox_SymmetricAlgorithm);
		
		btnDecrypt = new JButton("Decrypt!");
		btnDecrypt.setFont(new Font("Tahoma", Font.BOLD, 11));
		GridBagConstraints gbc_btnDecrypt = new GridBagConstraints();
		gbc_btnDecrypt.anchor = GridBagConstraints.SOUTHWEST;
		gbc_btnDecrypt.insets = new Insets(0, 0, 5, 5);
		gbc_btnDecrypt.gridx = 5;
		gbc_btnDecrypt.gridy = 4;
		gbc_btnDecrypt.fill = GridBagConstraints.SOUTHWEST;
		contentPane.add(btnDecrypt, gbc_btnDecrypt);
		
		progressBar_Decrypt = new JProgressBar();
		GridBagConstraints gbc_progressBar_Decrypt = new GridBagConstraints();
		gbc_progressBar_Decrypt.insets = new Insets(0, 0, 5, 0);
		gbc_progressBar_Decrypt.gridwidth = 3;
		gbc_progressBar_Decrypt.gridx = 6;
		gbc_progressBar_Decrypt.gridy = 4;
		contentPane.add(progressBar_Decrypt, gbc_progressBar_Decrypt);
		
		JLabel lblHashingAlgorithm = new JLabel("Hashing:");
		GridBagConstraints gbc_lblHashingAlgorithm = new GridBagConstraints();
		gbc_lblHashingAlgorithm.insets = new Insets(0, 0, 5, 5);
		gbc_lblHashingAlgorithm.gridx = 0;
		gbc_lblHashingAlgorithm.gridy = 5;
		contentPane.add(lblHashingAlgorithm, gbc_lblHashingAlgorithm);
		
		comboBox_HashingAlgorithm = new JComboBox<EnumAvailableSymmetricAlgorithms>();
		GridBagConstraints gbc_comboBox_HashingAlgorithm = new GridBagConstraints();
		gbc_comboBox_HashingAlgorithm.gridwidth = 3;
		gbc_comboBox_HashingAlgorithm.insets = new Insets(0, 0, 5, 5);
		gbc_comboBox_HashingAlgorithm.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox_HashingAlgorithm.gridx = 1;
		gbc_comboBox_HashingAlgorithm.gridy = 5;
		contentPane.add(comboBox_HashingAlgorithm, gbc_comboBox_HashingAlgorithm);
		
		txtrLog = new JTextArea();
		txtrLog.setText("Log:");
		txtrLog.setBackground(SystemColor.info);
		txtrLog.setEditable(false);
		GridBagConstraints gbc_txtrLog = new GridBagConstraints();
		gbc_txtrLog.gridheight = 3;
		gbc_txtrLog.gridwidth = 4;
		gbc_txtrLog.insets = new Insets(0, 0, 5, 0);
		gbc_txtrLog.fill = GridBagConstraints.BOTH;
		gbc_txtrLog.gridx = 5;
		gbc_txtrLog.gridy = 5;
		contentPane.add(txtrLog, gbc_txtrLog);
		
		JLabel lblCompressionAlgorithm = new JLabel("Compression:");
		GridBagConstraints gbc_lblCompressionAlgorithm = new GridBagConstraints();
		gbc_lblCompressionAlgorithm.insets = new Insets(0, 0, 5, 5);
		gbc_lblCompressionAlgorithm.gridx = 0;
		gbc_lblCompressionAlgorithm.gridy = 6;
		contentPane.add(lblCompressionAlgorithm, gbc_lblCompressionAlgorithm);
		
		comboBox_CompressionAlgorithm = new JComboBox<EnumAvailableSymmetricAlgorithms>();
		GridBagConstraints gbc_comboBox_CompressionAlgorithm = new GridBagConstraints();
		gbc_comboBox_CompressionAlgorithm.gridwidth = 3;
		gbc_comboBox_CompressionAlgorithm.insets = new Insets(0, 0, 5, 5);
		gbc_comboBox_CompressionAlgorithm.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox_CompressionAlgorithm.gridx = 1;
		gbc_comboBox_CompressionAlgorithm.gridy = 6;
		contentPane.add(comboBox_CompressionAlgorithm, gbc_comboBox_CompressionAlgorithm);
		
		btnEncrypt = new JButton("Encrypt!");
		btnEncrypt.setFont(new Font("Tahoma", Font.BOLD, 11));
		GridBagConstraints gbc_btnEncrypt = new GridBagConstraints();
		gbc_btnEncrypt.insets = new Insets(0, 0, 5, 5);
		gbc_btnEncrypt.gridx = 0;
		gbc_btnEncrypt.gridy = 7;
		contentPane.add(btnEncrypt, gbc_btnEncrypt);
		
		progressBar_Encrypt = new JProgressBar();
		GridBagConstraints gbc_progressBar_Encrypt = new GridBagConstraints();
		gbc_progressBar_Encrypt.gridwidth = 3;
		gbc_progressBar_Encrypt.insets = new Insets(0, 0, 5, 5);
		gbc_progressBar_Encrypt.gridx = 1;
		gbc_progressBar_Encrypt.gridy = 7;
		contentPane.add(progressBar_Encrypt, gbc_progressBar_Encrypt);
	}

	private void setHandlers() {
		this.button_SelectFileToEncrypt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		this.button_SelectFileToDecrypt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		this.button_SelectOutputFileEncrypt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		this.button_SelectOutputFileDecrypt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		this.button_SelectPublicKeyFile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		this.button_SelectPrivateKey.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		this.btnDecrypt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		this.btnEncrypt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}
}
