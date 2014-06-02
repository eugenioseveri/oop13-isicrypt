package gui.controllers;


import gui.views.CryptographyView_Filo;
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
		CryptographyView_Filo frame = new CryptographyView_Filo();		
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
		StartScreenView.getFrame().setVisible(false);
	}

	@Override
	public void selectFileExchange() {
		FileExchangeController controller = new FileExchangeController();
		FileExchangeView view = new FileExchangeView();
		controller.setView(view);		
		StartScreenView.getFrame().setVisible(false);
	}
}