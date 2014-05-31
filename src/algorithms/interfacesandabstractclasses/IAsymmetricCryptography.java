package algorithms.interfacesandabstractclasses;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

import algorithms.EnumAsymmetricKeyTypes;


/**
 * Interface used to define mandatory methods of cryptography classes.
 * @author Eugenio Severi
 */
	public interface IAsymmetricCryptography {
		byte[] encode(byte[] input);
		byte[] decode(byte[] input);
		void generateKeyPair(int keySize) throws InvalidKeyException;
		void saveKeyToFile(EnumAsymmetricKeyTypes keyType, FileOutputStream output) throws IOException;
		void loadKeyFromFile(EnumAsymmetricKeyTypes keyType, FileInputStream input) throws IOException;
		PublicKey getPublicKey();
		PrivateKey getPrivateKey();
		void setKeyPair(KeyPair pair);
		void setKeyPair(PublicKey publicKey, PrivateKey privateKey);
	}
