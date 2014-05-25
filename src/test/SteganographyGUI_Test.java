package test;

import gui.controllers.SteganographyController;
import gui.views.SteganographyView;

public class SteganographyGUI_Test {

	public static void main(String[] args){
		SteganographyController controller = new SteganographyController(); 
		SteganographyView view = new SteganographyView();
		controller.setView(view);
	}
}
