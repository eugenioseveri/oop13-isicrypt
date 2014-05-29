package gui.controllers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * @author Eugenio Severi
 *
 */
public interface ICryptographyViewObserver {
	void command_SelectFileToEncrypt() throws FileNotFoundException;
	void command_SelectFileToDecrypt();
	void command_SelectOutputFileEncrypt();
	void command_SelectOutputFileDecrypt();
	void command_SelectPublicKeyFile();
	void command_SelectPrivateKey();
	void command_Decrypt();
	void command_Encrypt() throws FileNotFoundException, IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchProviderException, ClassNotFoundException, IllegalBlockSizeException, BadPaddingException, NullPointerException, NoSuchPaddingException;
	void command_GenerateNewKeyPair() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException;
}
