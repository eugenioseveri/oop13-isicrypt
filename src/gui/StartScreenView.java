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

public class StartScreenView extends JFrame {

	private JPanel contentPane;

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
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 399);
		contentPane = new JPanel();
		contentPane.setBackground(SystemColor.windowBorder);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnCrittografiaFile = new JButton("Crittografia File");
		btnCrittografiaFile.setBounds(20, 40, 153, 71);
		contentPane.add(btnCrittografiaFile);
		
		JButton btnSteganografia = new JButton("Steganografia");
		btnSteganografia.setBounds(258, 40, 153, 71);
		contentPane.add(btnSteganografia);
		
		JButton btnPasswordManager = new JButton("Password Manager");
		btnPasswordManager.setBackground(SystemColor.textInactiveText);
		btnPasswordManager.setBounds(20, 139, 153, 71);
		contentPane.add(btnPasswordManager);
		
		JButton btnScambioFile = new JButton("Scambio File");
		btnScambioFile.setBounds(258, 139, 153, 71);
		contentPane.add(btnScambioFile);
		
		JButton btnKeyManager = new JButton("key manager");
		btnKeyManager.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnKeyManager.setBounds(20, 239, 153, 71);
		contentPane.add(btnKeyManager);
		
		JButton btnInfo = new JButton("Info");
		btnInfo.setBounds(258, 239, 153, 71);
		contentPane.add(btnInfo);
	}
}
