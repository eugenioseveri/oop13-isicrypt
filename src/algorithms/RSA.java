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
		try {
			super.cryptoCipher = Cipher.getInstance(ALGORITHM);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) { // Queste eccezioni non possono verificarsi (algorithm è costante)
			e.printStackTrace();
		}
	}
	
	/**
	 * Encodes a byte array, using a previously set public key (via @link {@link #setKeyPair(java.security.KeyPair)} or @link {@link #setKeyPair(java.security.PublicKey, java.security.PrivateKey)})
	 * @param input The byte array you want to encode
	 * @return The encrypted byte array
	 */
	@Override
	public byte[] encode(byte[] input) {
		try {
			super.cryptoCipher.init(Cipher.ENCRYPT_MODE, super.keyPair.getPublic());
		} catch (InvalidKeyException e) {
			if(!super.isKeyPairInitialized()) {
				System.err.println(NOKEY_ERROR);
			} else {
				e.printStackTrace();
			}
		}
		try {
			return super.cryptoCipher.doFinal(input);
		} catch (IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Decodes a byte array, using a previously set private key (via @link {@link #setKeyPair(java.security.KeyPair)} or @link {@link #setKeyPair(java.security.PublicKey, java.security.PrivateKey)})
	 * @param input The byte array you want to decode
	 * @return The decrypted byte array
	 */
	@Override
	public byte[] decode(byte[] input) {
		try {
			super.cryptoCipher.init(Cipher.DECRYPT_MODE, super.keyPair.getPrivate());
		} catch (InvalidKeyException e) {
			if(!super.isKeyPairInitialized()) {
				System.err.println(NOKEY_ERROR);
			} else {
				e.printStackTrace();
			}
		}
		try {
			return super.cryptoCipher.doFinal(input);
		} catch (IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Generates a new RSA key pair of the specified length
	 * @param keySize The length (in bits) of the generated key
	 * @throws InvalidKeyException If the key size is not valid.
	 */
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
