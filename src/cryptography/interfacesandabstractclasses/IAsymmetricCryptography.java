package cryptography.interfacesandabstractclasses;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

import cryptography.algorithms.EnumAsymmetricKeyTypes;


/**
 * Interface used to define mandatory methods of cryptography classes.
 * @author Eugenio Severi
 */
	public interface IAsymmetricCryptography {
		byte[] encode(byte[] input) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException;
		byte[] decode(byte[] input) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException;
		void generateKeyPair(int keySize) throws InvalidKeyException, NoSuchAlgorithmException;
		void saveKeyToFile(EnumAsymmetricKeyTypes keyType, FileOutputStream output) throws IOException;
		void loadKeyFromFile(EnumAsymmetricKeyTypes keyType, FileInputStream input) throws IOException, ClassNotFoundException;
	}
