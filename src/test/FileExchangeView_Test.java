package test;

import gui.controllers.FileExchangeController;
import gui.views.FileExchangeView;

public class FileExchangeView_Test {

	public static void main(String[] args) {
		FileExchangeController controller = new FileExchangeController();
		FileExchangeView view = new FileExchangeView();
		controller.setView(view);
	}

}
