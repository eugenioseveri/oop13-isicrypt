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

import cryptography.interfacesandabstractclasses.ICryptography;

/**
 * FARE VERSIONE THREADED
 * Class used to implement AES algorithm.
 * @author Eugenio Severi
 */
public class AES implements ICryptography {
	
	private final static String ALGORITHM = "AES";
	private final static int AES_KEYSIZES[] = {128, 192, 256};
	private final static int BUFFER_SIZE = 1024;
	private Cipher aesCipher = null;
	private SecretKeySpec aeskeySpec;

	public AES() throws NoSuchAlgorithmException, NoSuchPaddingException {
		this.aesCipher = Cipher.getInstance(ALGORITHM);
	}
	
	public SecretKeySpec getAeskeySpec() {
		return this.aeskeySpec;
	}

	public void setAeskeySpec(SecretKeySpec aeskeySpec) {
		this.aeskeySpec = aeskeySpec;
	}
	
	@Override
	public void encode(InputStream input, OutputStream output) throws IOException, InvalidKeyException, NullPointerException {
		// Aggiungere try-catch
		int i;
		byte[] b = new byte[BUFFER_SIZE];
		try {
		this.aesCipher.init(Cipher.ENCRYPT_MODE, this.aeskeySpec); //java.security.InvalidKeyException: No installed provider supports this key: javax.crypto.spec.SecretKeySpec. Vedere https://www.google.it/?gfe_rd=cr&ei=281KU-qKGqWO8QfErIG4Aw#q=java+InvalidKeyException&safe=off
		} catch (Exception e) {
			System.out.println(e);
		}
		CipherOutputStream out = new CipherOutputStream(output, this.aesCipher);
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
		this.aesCipher.init(Cipher.DECRYPT_MODE, this.aeskeySpec);
		CipherInputStream in = new CipherInputStream(input, this.aesCipher);
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
		this.aeskeySpec = new SecretKeySpec(aesKey, ALGORITHM);
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