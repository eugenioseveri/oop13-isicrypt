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
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			// This exceptions can not occur since ALGORITHM is built-in
		}
	}
	
	/**
	 * This constructor allows to set an AES key from a byte array instead of creating a new SecretKeySpec manually.
	 * @param key AES key
	 * @throws InvalidKeyException If the key size is not valid.
	 */
	public void setSymmetricKeySpec(final byte[] key) throws InvalidKeyException {
		if(!checkKeySize(key.length * 8)) {
			throw new InvalidKeyException(WRONG_KEYSIZE_ERROR);
		}
		super.symmetricKeySpec = new SecretKeySpec(key, ALGORITHM);
	}
	
	@Override
	public void encode(final InputStream input, final OutputStream output) throws IOException, InvalidKeyException {
		int i;
		final byte[] b = new byte[BUFFER_SIZE];
		try {
			super.cryptoCipher.init(Cipher.ENCRYPT_MODE, super.symmetricKeySpec);
		} catch (InvalidKeyException e) {
			if(super.isSymmetricKeyInitialized()) {
				throw e;
			} else {
				throw new InvalidKeyException(NOKEY_ERROR);
			}
		}
		final CipherOutputStream out = new CipherOutputStream(output, super.cryptoCipher);
		try {
			while((i=input.read(b)) != END_OF_FILE) {
				out.write(b, 0, i);
			}
		} catch (IOException e) {
			throw e;
		} finally {
			if(out != null) {
				out.close();
			}
		}
	}

	@Override
	public void decode(final InputStream input, final OutputStream output) throws IOException, InvalidKeyException {
		int i;
		final byte[] b = new byte[BUFFER_SIZE];
		try {
			super.cryptoCipher.init(Cipher.DECRYPT_MODE, super.symmetricKeySpec);
		} catch (InvalidKeyException e) {
			if(super.isSymmetricKeyInitialized()) {
				throw e;
			} else {
				throw new InvalidKeyException(NOKEY_ERROR);
			}
		}
		final CipherInputStream in = new CipherInputStream(input, super.cryptoCipher);
		try {
			while((i=in.read(b)) != END_OF_FILE) {
				output.write(b, 0, i);
			}
		} catch (IOException e) {
			throw e;
		} finally {
			if(in != null) {
				in.close();
			}
		}
	}

	@Override
	public void generateKey(final int keySize) throws InvalidKeyException {
		if(!checkKeySize(keySize)) {
			throw new InvalidKeyException(WRONG_KEYSIZE_ERROR);
		}
		KeyGenerator keyGen = null;
		try {
			keyGen = KeyGenerator.getInstance(ALGORITHM);
		} catch (NoSuchAlgorithmException e) {
			// This exception can not occur since ALGORITHM is built-in
		}
		keyGen.init(keySize);
		final SecretKey key = keyGen.generateKey();
		final byte[] aesKey = key.getEncoded();
		super.symmetricKeySpec = new SecretKeySpec(aesKey, ALGORITHM);
	}
	
	/**
	 * This internal method checks if the key length is valid for AES.
	 * @param keySize The length (in bits) of the generated key
	 * @return If the key length matches with one of the supported values
	 */
	private boolean checkKeySize(final int keySize) {
		for(int validKeyLength: AES_KEYSIZES) {
			if(validKeyLength == keySize) {
				return true;
			}
		}
		return false;
	}
}