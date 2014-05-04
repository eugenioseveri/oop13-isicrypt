package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.FlowLayout;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.SystemColor;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import java.awt.Font;
import javax.swing.SwingConstants;
import java.awt.Window.Type;
import javax.swing.DropMode;
import java.awt.Toolkit;

public class StartScreenView extends JFrame {

	private JPanel contentPane;
	private JTextField textField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					StartScreenView frame = new StartScreenView();
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
	public StartScreenView() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(StartScreenView.class.getResource("/gui/IsiCryptIcon.jpg")));
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 451, 448);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(51, 0, 102));
		contentPane.setBorder(null);
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnCrittografiaFile = new JButton("Crittografia File");
		btnCrittografiaFile.setBackground(new Color(240, 240, 240));
		btnCrittografiaFile.setBounds(20, 69, 180, 100);
		contentPane.add(btnCrittografiaFile);
		
		JButton btnSteganografia = new JButton("Steganografia");
		btnSteganografia.setBackground(SystemColor.menu);
		btnSteganografia.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnSteganografia.setBounds(244, 69, 180, 100);
		contentPane.add(btnSteganografia);
		
		JButton btnPasswordManager = new JButton("Password Manager");
		btnPasswordManager.setBackground(SystemColor.menu);
		btnPasswordManager.setBounds(20, 182, 180, 100);
		contentPane.add(btnPasswordManager);
		
		JButton btnScambioFile = new JButton("Scambio File");
		btnScambioFile.setBackground(SystemColor.menu);
		btnScambioFile.setBounds(244, 182, 180, 100);
		contentPane.add(btnScambioFile);
		
		JButton btnKeyManager = new JButton("key manager");
		btnKeyManager.setBackground(SystemColor.menu);
		btnKeyManager.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnKeyManager.setBounds(20, 296, 180, 100);
		contentPane.add(btnKeyManager);
		
		JButton btnInfo = new JButton("Info");
		btnInfo.setBackground(SystemColor.menu);
		btnInfo.setBounds(244, 296, 180, 100);
		contentPane.add(btnInfo);
		
		textField = new JTextField("IsiCrypt");
		textField.setDropMode(DropMode.INSERT);
		textField.setForeground(SystemColor.menu);
		textField.setHorizontalAlignment(SwingConstants.CENTER);
		textField.setEditable(false);
		textField.setFont(new Font("Tahoma", Font.BOLD, 23));
		textField.setBackground(new Color(51, 0, 102));
		textField.setBounds(166, 11, 113, 47);
		contentPane.add(textField);
		textField.setColumns(10);
	}
}
