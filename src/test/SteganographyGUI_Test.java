package test;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import gui.SteganographyGUI;

public class SteganographyGUI_Test {

	public static void main(String[] args) throws IOException {
		JFrame frame = new JFrame();
		frame.setIconImage(ImageIO.read(new File("./res/isiCryptICON_MetroStyle.jpg")));
		frame.setTitle("GridBagLayout-Test");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1280,720);//Posso mettere quello che voglio, tanto i componenti si adattano! CHE SBORATA!!!
		JPanel contenitore = new JPanel();
		new SteganographyGUI(contenitore);
		frame.getContentPane().add(contenitore);
		frame.setVisible(true);
	}
}
