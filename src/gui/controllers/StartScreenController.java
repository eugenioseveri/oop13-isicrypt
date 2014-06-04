package gui.controllers;


import javax.swing.JOptionPane;

import gui.models.GlobalSettings;
import gui.models.KeyringModel;
import gui.views.CryptographyView;
import gui.views.FileExchangeView;
import gui.views.KeyringView;
import gui.views.StartScreenView;
import gui.views.SteganographyView;

public class StartScreenController implements IStartScreenViewObserver{
	
	private static StartScreenView view;
	
	public void setView(StartScreenView view){
		StartScreenController.view = view;
		StartScreenController.view.attacStartScreenViewObserver(this);
	}
	
	@Override
	public void selectCryptography() {
		CryptographyView frame = new CryptographyView();
		new CryptographyController(frame);
		StartScreenView.getFrame().setVisible(false);
	}

	@Override
	public void selectSteganography() {
		SteganographyController controller = new SteganographyController(); 
		SteganographyView view = new SteganographyView();
		controller.setView(view);	
		StartScreenView.getFrame().setVisible(false);
	}

	@Override
	public void selectKeyring() {
		KeyringView frame = new KeyringView();
		KeyringModel model = new KeyringModel();
		new KeyringController(frame, model);
		StartScreenView.getFrame().setVisible(false);
	}

	@Override
	public void selectFileExchange() {
		FileExchangeController controller = new FileExchangeController();
		FileExchangeView view = new FileExchangeView();
		controller.setView(view);		
		StartScreenView.getFrame().setVisible(false);
	}
	
	@Override
	public void selectTheme(){
		Object[] possibilities = {"WINTER_IS_COMING", "NIGHTS_WATCH", "FIRE_AND_BLOOD", "OURS_IS_THE_FURY"};
		String theme = (String)JOptionPane.showInputDialog(StartScreenView.getDialog(),"Choose a theme:\n","Customized Dialog",JOptionPane.PLAIN_MESSAGE,null ,possibilities,null);
		if(theme != null){
			GlobalSettings settings = new GlobalSettings();
			settings.setTheme(theme);
			settings.storeSettings();
			StartScreenView.redraw();
			/*StartScreenController reloadController = new StartScreenController();
			StartScreenView reloadView = new StartScreenView();
			reloadController.setView(reloadView);*/
		}
	}
}