package gui.controllers;

public interface ISteganographyViewObserver {
	void selectImage();
	void selectText();
	void start();
	void findText();
	void clearSetting();
	void insertText();
}
