package test;

import gui.controllers.StartScreenController;
import gui.views.StartScreenView;

public class StartScreen_Test {

	public static void main(String[] args) {
		StartScreenController controller = new StartScreenController();
		StartScreenView view = new StartScreenView();
		controller.setView(view);
	}

}
