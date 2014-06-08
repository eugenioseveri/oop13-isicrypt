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
import gui.views.IStartScreenView;
import gui.views.ISteganographyView;
import gui.views.KeyringView;
import gui.views.StartScreenView;
import gui.views.SteganographyView;
/**
 * 
 * @author Filippo Vimini
 *Create controller for standard pattern MVC
 */
public class StartScreenController implements IStartScreenViewObserver{
	
	private static IStartScreenView view;
	
	public void setView(final IStartScreenView view){
		StartScreenController.view = view;
		StartScreenController.view.attacStartScreenViewObserver(this);
	}
	
	@Override
	public void selectCryptography() {
		final CryptographyView frame = new CryptographyView();
		new CryptographyController(frame);
		StartScreenView.getFrame().setVisible(false);
	}

	@Override
	public void selectSteganography() {
		final SteganographyController controller = new SteganographyController(); 
		final ISteganographyView view = new SteganographyView();
		controller.setView(view);	
		StartScreenView.getFrame().setVisible(false);
	}

	@Override
	public void selectKeyring() {
		final KeyringView frame = new KeyringView();
		final KeyringModel model = new KeyringModel();
		new KeyringController(frame, model);
		StartScreenView.getFrame().setVisible(false);
	}

	@Override
	public void selectFileExchange() {
		final FileExchangeController controller = new FileExchangeController();
		final IFileExchangeView view = new FileExchangeView();
		final IFileExchangeModel model = new FileExchangeModel();
		controller.setViewAndModel(view, model);		
		StartScreenView.getFrame().setVisible(false);
	}
	
	@Override
	public void selectTheme(){
		final Object[] possibilities = {"WINTER_IS_COMING", "NIGHTS_WATCH", "FIRE_AND_BLOOD", "OURS_IS_THE_FURY"};
		final String theme = (String)JOptionPane.showInputDialog(StartScreenView.getDialog(),"Choose a theme:\n","Customized Dialog",JOptionPane.PLAIN_MESSAGE,null ,possibilities,null);
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