package gui.views;

import gui.controllers.IStartScreenViewObserver;

import java.awt.Color;
import java.awt.Font;
/**
 * 
 * @author Filippo Vimini
 *	Interface of the view in pattern MVC
 */
public interface IStartScreenView {
	/**
	 * link the controller at the view
	 */
	public abstract void attacStartScreenViewObserver(
			IStartScreenViewObserver controller);

	//getters for change the theme
	public abstract Font getFont();

	public abstract Color getPanelBakColor();

	public abstract Color getButtonColor();

	public abstract Color getForegroundColor();

}