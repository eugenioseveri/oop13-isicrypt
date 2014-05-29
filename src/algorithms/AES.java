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
import algorithms.interfacesandabstractclasses.ICryptography;

/**
 * FARE VERSIONE THREADED
 * Class used to implement AES algorithm.
 * @author Eugenio Severi
 */
public class AES extends AbstractSymmetricCryptography implements ICryptography {
	
	private final static String ALGORITHM = "AES";
	private final static int AES_KEYSIZES[] = {128/*, 192, 256*/};  //java.security.InvalidKeyException: No installed provider supports this key: javax.crypto.spec.SecretKeySpec. Vedere https://www.google.it/?gfe_rd=cr&ei=281KU-qKGqWO8QfErIG4Aw#q=java+InvalidKeyException&safe=off
	private final static int BUFFER_SIZE = 1024;
	private final static String NOKEY_ERROR = "Non è stata impostata una chiave di cifratura!";
	private final static String WRONG_KEYSIZE_ERROR = "Il valore di keySize non è valido per AES!";

	public AES() { // Queste eccezioni non possono verificarsi (algorithm è costante)
		try {
			super.cryptoCipher = Cipher.getInstance(ALGORITHM);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void encode(InputStream input, OutputStream output) throws IOException {
		int i;
		byte[] b = new byte[BUFFER_SIZE];
		try {
				super.cryptoCipher.init(Cipher.ENCRYPT_MODE, super.symmetricKeySpec);
			} catch (InvalidKeyException e) {
				if(super.symmetricKeySpec == null) {
					System.out.println(NOKEY_ERROR);
				} else {
					e.printStackTrace();
				}
			}
		CipherOutputStream out = new CipherOutputStream(output, super.cryptoCipher);
		try {
			while((i=input.read(b)) != -1) {
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
			if(super.symmetricKeySpec == null) {
				System.out.println(NOKEY_ERROR);
			} else {
				e.printStackTrace();
			}
		}
		CipherInputStream in = new CipherInputStream(input, super.cryptoCipher);
		try {
			while((i=in.read(b)) != -1) {
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
	
	private boolean checkKeySize(int keySize) {
		for(int validKeyLength: AES_KEYSIZES) {
			if(validKeyLength == keySize) {
				return true;
			}
		}
		return false;
	}
}