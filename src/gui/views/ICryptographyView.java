package gui.views;

import javax.swing.JTextArea;

import algorithms.EnumAvailableCompressionAlgorithms;
import algorithms.EnumAvailableHashingAlgorithms;
import algorithms.EnumAvailableSymmetricAlgorithms;
import gui.controllers.ICryptographyViewObserver;

/**
 * 
 * @author Eugenio Severi
 */
public interface ICryptographyView {
	void attachViewObserver(ICryptographyViewObserver listener);
	void setText_encryptTextField(String text);
	void setText_publicKeyTextField(String text);
	void setText_decryptTextField(String text);
	void setText_privateKeyTextField(String text);
	//void addText_logTextArea(String text);
	JTextArea getTextArea();
	EnumAvailableSymmetricAlgorithms getSymmetricAlgorithm();
	EnumAvailableHashingAlgorithms getHashingAlgorithm();
	EnumAvailableCompressionAlgorithms getCompressionAlgorithm();
	int getNumberOfWipingPassages();
	void setValue_progressBarEncryption(int value);
	void setValue_progressBarDecryption(int value);
	void showMessageDialog(String message);
}
