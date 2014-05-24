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

/**
 * Class used to implement RSA algorithm.
 * @author Eugenio Severi
 */
public class RSA extends AbstractAsymmetricCryptography implements IAsymmetricCryptography {

	private final static String ALGORITHM = "RSA";
	
	public RSA() throws NoSuchAlgorithmException, NoSuchPaddingException {
		super.cryptoCipher = Cipher.getInstance(ALGORITHM);
	}
	
	@Override
	public byte[] encode(byte[] input) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
		// Aggiungere try-catch
		super.cryptoCipher.init(Cipher.ENCRYPT_MODE, super.keyPair.getPublic());
		return super.cryptoCipher.doFinal(input);
	}

	@Override
	public byte[] decode(byte[] input) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
		// Aggiungere try-catch
		super.cryptoCipher.init(Cipher.DECRYPT_MODE, super.keyPair.getPrivate());
		return super.cryptoCipher.doFinal(input);
	}

	@Override
	public void generateKeyPair(int keySize) throws InvalidKeyException, NoSuchAlgorithmException {
		if(!checkKeySize(keySize)) {
			throw new InvalidKeyException("Il valore di keySize deve essere una potenza di 2!");
		}
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);
		keyPairGenerator.initialize(keySize);
		super.keyPair = keyPairGenerator.generateKeyPair();
	}
	
	private boolean checkKeySize(int keySize) {
		// Controlla se la chiave è valida per RSA (deve essere una potenza di 2)
		double log2 = Math.log(keySize)/Math.log(2); // Proprietà del cambiamento di base dei logaritmi
		if(log2 == (int)log2) {
			return true;
		}
		return false;
	}
}
