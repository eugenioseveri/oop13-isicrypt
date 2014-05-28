package test;

import java.io.FileNotFoundException;
import java.io.IOException;

import gui.controllers.SteganographyController;
import gui.views.SteganographyView;

public class SteganographyView_Test {

	public static void main(String[] args) throws FileNotFoundException, ClassNotFoundException, IOException{
		SteganographyController controller = new SteganographyController(); 
		SteganographyView view = new SteganographyView();
		controller.setView(view);
	}
}
