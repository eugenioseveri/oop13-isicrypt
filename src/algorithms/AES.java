package algorithms;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import algorithms.interfacesandabstractclasses.AbstractSymmetricCryptography;
import algorithms.interfacesandabstractclasses.ISymmetricCryptography;

import static algorithms.ErrorMessages.*;

/**
 * Class used to implement AES algorithm.
 * @author Eugenio Severi
 */
public class AES extends AbstractSymmetricCryptography implements ISymmetricCryptography {
	
	private final static String ALGORITHM = "AES";
	private final static int END_OF_FILE = -1;
	private final static int AES_KEYSIZES[] = {128/*, 192, 256*/};  //java.security.InvalidKeyException: No installed provider supports this key: javax.crypto.spec.SecretKeySpec. Vedere https://www.google.it/?gfe_rd=cr&ei=281KU-qKGqWO8QfErIG4Aw#q=java+InvalidKeyException&safe=off
	private final static int BUFFER_SIZE = 1024;

	/**
	 * This constructor instantiates a new AES cipher
	 */
	public AES() {
		super();
		try {
			super.cryptoCipher = Cipher.getInstance(ALGORITHM);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) { // Queste eccezioni non possono verificarsi (algorithm è costante)
			e.printStackTrace();
		}
	}
	
	/**
	 * This constructor allows to set an AES key from a byte array instead of creating a new SecretKeySpec manually.
	 * @param key AES key
	 * @throws InvalidKeyException If the key size is not valid.
	 */
	public void setSymmetricKeySpec(byte[] key) throws InvalidKeyException {
		if(!checkKeySize(key.length)) {
			throw new InvalidKeyException(WRONG_KEYSIZE_ERROR);
		}
		super.symmetricKeySpec = new SecretKeySpec(key, ALGORITHM);
	}
	
	@Override
	public void encode(InputStream input, OutputStream output) throws IOException {
		int i;
		byte[] b = new byte[BUFFER_SIZE];
		try {
			super.cryptoCipher.init(Cipher.ENCRYPT_MODE, super.symmetricKeySpec);
		} catch (InvalidKeyException e) {
			if(super.isSymmetricKeyInitialized()) {
				e.printStackTrace();
			} else {
				System.err.println(NOKEY_ERROR);
			}
		}
		CipherOutputStream out = new CipherOutputStream(output, super.cryptoCipher);
		try {
			while((i=input.read(b)) != END_OF_FILE) {
				out.write(b, 0, i);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(out != null) {
				// TODO: bisogna chiudere anche input? (idem in decode())
				out.close();
			}
		}
	}

	@Override
	public void decode(InputStream input, OutputStream output) throws IOException {
		int i;
		byte[] b = new byte[BUFFER_SIZE];
		try {
			super.cryptoCipher.init(Cipher.DECRYPT_MODE, super.symmetricKeySpec);
		} catch (InvalidKeyException e) {
			if(super.isSymmetricKeyInitialized()) {
				e.printStackTrace();
			} else {
				System.err.println(NOKEY_ERROR);
			}
		}
		CipherInputStream in = new CipherInputStream(input, super.cryptoCipher);
		try {
			while((i=in.read(b)) != END_OF_FILE) {
				output.write(b, 0, i);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(in != null) {
				in.close();
			}
		}
	}

	@Override
	public void generateKey(int keySize) throws InvalidKeyException {
		if(!checkKeySize(keySize)) {
			throw new InvalidKeyException(WRONG_KEYSIZE_ERROR);
		}
		// SecretKeyFactory potrebbe essere interessante per generare la chiave in maniera migliore...
		KeyGenerator keyGen = null;
		try {
			keyGen = KeyGenerator.getInstance(ALGORITHM);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} // Aggiungere keysize "AES(128)" e provider
		keyGen.init(keySize);
		SecretKey key = keyGen.generateKey();
		byte[] aesKey = key.getEncoded();
		super.symmetricKeySpec = new SecretKeySpec(aesKey, ALGORITHM);
	}
	
	/**
	 * This internal method checks if the key length is valid for AES.
	 * @param keySize The length (in bits) of the generated key
	 * @return If the key length matches with one of the supported values
	 */
	private boolean checkKeySize(int keySize) {
		for(int validKeyLength: AES_KEYSIZES) {
			if(validKeyLength == keySize) {
				return true;
			}
		}
		return false;
	}
}