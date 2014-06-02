package algorithms;

import java.security.InvalidKeyException;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import algorithms.interfacesandabstractclasses.AbstractAsymmetricCryptography;
import algorithms.interfacesandabstractclasses.IAsymmetricCryptography;

import static algorithms.ErrorMessages.*;

/**
 * Class used to implement RSA algorithm.
 * @author Eugenio Severi
 */
public class RSA extends AbstractAsymmetricCryptography implements IAsymmetricCryptography {

	private final static String ALGORITHM = "RSA";
	
	/**
	 * This constructor instantiates a new RSA cipher
	 */
	public RSA() {
		super();
		try {
			super.cryptoCipher = Cipher.getInstance(ALGORITHM);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) { // Queste eccezioni non possono verificarsi (algorithm è costante)
			e.printStackTrace();
		}
	}
	
	@Override
	public byte[] encode(byte[] input) {
		try {
			super.cryptoCipher.init(Cipher.ENCRYPT_MODE, super.keyPair.getPublic());
		} catch (InvalidKeyException e) {
			if(super.isKeyPairInitialized()) {
				e.printStackTrace();
			} else {
				System.err.println(NOKEY_ERROR);
			}
		}
		try {
			return super.cryptoCipher.doFinal(input);
		} catch (IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public byte[] decode(byte[] input) {
		try {
			super.cryptoCipher.init(Cipher.DECRYPT_MODE, super.keyPair.getPrivate());
		} catch (InvalidKeyException e) {
			if(super.isKeyPairInitialized()) {
				e.printStackTrace();
			} else {
				System.err.println(NOKEY_ERROR);
			}
		}
		try {
			return super.cryptoCipher.doFinal(input);
		} catch (IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void generateKeyPair(int keySize) throws InvalidKeyException {
		if(!checkKeySize(keySize)) {
			throw new InvalidKeyException("Il valore di keySize deve essere una potenza di 2!");
		}
		KeyPairGenerator keyPairGenerator = null;
		try {
			keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		keyPairGenerator.initialize(keySize);
		super.keyPair = keyPairGenerator.generateKeyPair();
	}
	
	/**
	 * This internal method checks if the key length is valid for RSA.
	 * @param keySize The length (in bits) of the generated key
	 * @return If the key length matches with one of the supported values
	 */
	private boolean checkKeySize(int keySize) {
		// Controlla se la chiave è valida per RSA (deve essere una potenza di 2)
		double log2 = Math.log(keySize)/Math.log(2); // Proprietà del cambiamento di base dei logaritmi
		if(log2 == (int)log2) {
			return true;
		}
		return false;
	}
}
