package test;

import gui.controllers.CryptographyController;
import gui.views.CryptographyView;

public class CryptographyGUITest {
	
	public static void main(String[] args){
		
		CryptographyView frame = new CryptographyView();
		CryptographyController ccc = new CryptographyController(frame);
		//frame.setVisible(true);
		
	}
}
