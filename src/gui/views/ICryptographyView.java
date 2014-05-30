package gui.views;

import algorithms.EnumAvailableCompressionAlgorithms;
import algorithms.EnumAvailableHashingAlgorithms;
import algorithms.EnumAvailableSymmetricAlgorithms;
import gui.controllers.ICryptographyViewObserver;

/**
 * @author Eugenio
 *
 */
public interface ICryptographyView {
	void attachViewObserver(ICryptographyViewObserver listener);
	void setText_textField_FileToEncrypt(String text);
	void setText_textField_OutputFileEncrypt(String text);
	void setText_textField_PublicKey(String text);
	void setText_textField_FileToDecrypt(String text);
	void setText_textField_OutputFileDecrypt(String text);
	void setText_textField_PrivateKey(String text);
	void addText_txtrLog(String text);
	EnumAvailableSymmetricAlgorithms get_SymmetricAlgorithm();
	EnumAvailableHashingAlgorithms get_HashingAlgorithm();
	EnumAvailableCompressionAlgorithms getCompressionAlgorithm();
	boolean chckbx_isWipingEnabled();
}
