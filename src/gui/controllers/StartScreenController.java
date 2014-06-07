package gui.controllers;

import java.io.IOException;

import javax.swing.JOptionPane;

import gui.models.FileExchangeModel;
import gui.models.GlobalSettings;
import gui.models.IFileExchangeModel;
import gui.models.KeyringModel;
import gui.views.CryptographyView;
import gui.views.FileExchangeView;
import gui.views.IFileExchangeView;
import gui.views.ISteganographyView;
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
		ISteganographyView view = new SteganographyView();
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
		IFileExchangeView view = new FileExchangeView();
		IFileExchangeModel model = new FileExchangeModel();
		controller.setViewAndModel(view, model);		
		StartScreenView.getFrame().setVisible(false);
	}
	
	@Override
	public void selectTheme(){
		Object[] possibilities = {"WINTER_IS_COMING", "NIGHTS_WATCH", "FIRE_AND_BLOOD", "OURS_IS_THE_FURY"};
		String theme = (String)JOptionPane.showInputDialog(StartScreenView.getDialog(),"Choose a theme:\n","Customized Dialog",JOptionPane.PLAIN_MESSAGE,null ,possibilities,null);
		if(theme != null){
			GlobalSettings settings;
			try {
				settings = new GlobalSettings();
				settings.setTheme(theme);
				settings.storeSettings();
			} catch (IOException e) {
				// Do nothing
			}
			StartScreenView.redraw();
		}
	}
}