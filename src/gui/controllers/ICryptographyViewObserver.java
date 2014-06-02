package gui.controllers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidKeyException;

/**
 * @author Eugenio Severi
 *
 */
public interface ICryptographyViewObserver {
	void command_SelectFileToEncrypt() throws FileNotFoundException;
	void command_SelectFileToDecrypt();
	void command_SelectOutputFileEncrypt(); // TODO: remove
	void command_SelectOutputFileDecrypt(); // TODO: remove
	void command_SelectPublicKeyFile();
	void command_SelectPrivateKeyFile();
	void command_Decrypt();
	void command_Encrypt() throws InstantiationException, IllegalAccessException, ClassNotFoundException, InvalidKeyException, IOException;
	void command_GenerateNewKeyPair();
}
