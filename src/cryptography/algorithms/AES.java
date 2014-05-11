package cryptography.algorithms;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import cryptography.interfacesandabstractclasses.AbstractSymmetricCryptography;
import cryptography.interfacesandabstractclasses.ICryptography;

/**
 * FARE VERSIONE THREADED
 * Class used to implement AES algorithm.
 * @author Eugenio Severi
 */
public class AES extends AbstractSymmetricCryptography implements ICryptography {
	
	private final static String ALGORITHM = "AES";
	private final static int AES_KEYSIZES[] = {128, 192, 256};
	private final static int BUFFER_SIZE = 1024;

	public AES() throws NoSuchAlgorithmException, NoSuchPaddingException {
		super.cryptoCipher = Cipher.getInstance(ALGORITHM);
	}
	
	@Override
	public void encode(InputStream input, OutputStream output) throws IOException, InvalidKeyException, NullPointerException {
		// Aggiungere try-catch
		int i;
		byte[] b = new byte[BUFFER_SIZE];
		try {
			super.cryptoCipher.init(Cipher.ENCRYPT_MODE, super.symmetricKeySpec); //java.security.InvalidKeyException: No installed provider supports this key: javax.crypto.spec.SecretKeySpec. Vedere https://www.google.it/?gfe_rd=cr&ei=281KU-qKGqWO8QfErIG4Aw#q=java+InvalidKeyException&safe=off
		} catch (Exception e) {
			System.out.println(e);
		}
		CipherOutputStream out = new CipherOutputStream(output, super.cryptoCipher);
		while((i=input.read(b)) != -1) {
			out.write(b, 0, i);
		}
		out.close();
	}

	@Override
	public void decode(InputStream input, OutputStream output) throws IOException, InvalidKeyException {
		// Aggiungere try-catch
		int i;
		byte[] b = new byte[BUFFER_SIZE];
		super.cryptoCipher.init(Cipher.DECRYPT_MODE, super.symmetricKeySpec);
		CipherInputStream in = new CipherInputStream(input, super.cryptoCipher);
		while((i=in.read(b)) != -1) {
			output.write(b, 0, i);
		}
		in.close();
		output.close();
	}

	@Override
	public void generateKey(int keySize) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchProviderException {
		if(!checkKeySize(keySize)) {
			throw new InvalidKeyException("Il valore di keySize deve essere uno tra 128,192,256!");
		}
		// SecretKeyFactory potrebbe essere interessante per generare la chiave in maniera migliore...
		KeyGenerator keyGen = KeyGenerator.getInstance(ALGORITHM); // Aggiungere keysize "AES(128)" e provider
		keyGen.init(keySize);
		SecretKey key = keyGen.generateKey();
		byte[] aesKey = key.getEncoded();
		super.symmetricKeySpec = new SecretKeySpec(aesKey, ALGORITHM);
	}
	
	private boolean checkKeySize(int keySize) {
		for(int validKeyLength: AES_KEYSIZES) {
			if(validKeyLength == keySize) {
				return true;
			}
		}
		return false;
	}
}