package gui.views;

import gui.controllers.IStartScreenViewObserver;

import java.awt.Color;
import java.awt.Font;

/**
 * Interface of the view in pattern MVC
 * @author Filippo Vimini
 */
public interface IStartScreenView {
	/**
	 * @param controller 	link the controller at the view
	 */
	public abstract void attacStartScreenViewObserver(IStartScreenViewObserver controller);

	//getters for change the theme
	public abstract Font getFont();

	public abstract Color getPanelBakColor();

	public abstract Color getButtonColor();

	public abstract Color getForegroundColor();

}