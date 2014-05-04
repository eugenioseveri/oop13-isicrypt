package cryptography.algorithms;

import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import cryptography.interfacesandabstractclasses.AbstractAsymmetricCryptography;
import cryptography.interfacesandabstractclasses.IAsymmetricCryptography;

/**
 * Class used to implement RSA algorithm.
 * @author Eugenio Severi
 */
public class RSA extends AbstractAsymmetricCryptography implements IAsymmetricCryptography {

	private final static String ALGORITHM = "RSA";
	private Cipher rsaCipher = null;
	private KeyPair keyPair;
	
	public RSA() throws NoSuchAlgorithmException, NoSuchPaddingException {
		this.rsaCipher = Cipher.getInstance(ALGORITHM);
	}
	
	@Override
	public byte[] encode(byte[] input) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
		// Aggiungere try-catch
		this.rsaCipher.init(Cipher.ENCRYPT_MODE, this.keyPair.getPublic());
		return this.rsaCipher.doFinal(input);
	}

	@Override
	public byte[] decode(byte[] input) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
		// Aggiungere try-catch
		this.rsaCipher.init(Cipher.DECRYPT_MODE, this.keyPair.getPrivate());
		return this.rsaCipher.doFinal(input);
	}

	@Override
	public void generateKeyPair(int keySize) throws InvalidKeyException, NoSuchAlgorithmException {
		if(!checkKeySize(keySize)) {
			throw new InvalidKeyException("Il valore di keySize deve essere una potenza di 2!");
		}
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);
		keyPairGenerator.initialize(keySize);
		this.keyPair = keyPairGenerator.generateKeyPair();
	}
	
	private boolean checkKeySize(int keySize) {
		// Controlla se la chiave è valida per RSA (deve essere una potenza di 2)
		double log2 = Math.log(keySize)/Math.log(2); // Proprietà del cambiamento di base dei logaritmi
		if(log2 == (int)log2) {
			return true;
		}
		return false;
	}

	
	/*@Override
	public void saveKeyToFile(EnumAsymmetricKeyTypes keyType, FileOutputStream output) throws IOException {
		
	}
	@Override
	public void loadKeyFromFile(EnumAsymmetricKeyTypes keyType, FileInputStream input) throws IOException, ClassNotFoundException {
		
	}*/
	/*@Override
	public void saveKeyToFile(EnumAsymmetricKeyTypes keyType, FileOutputStream output) throws IOException {
		ObjectOutputStream objOutStream = null;
		try {
			objOutStream = new ObjectOutputStream(new BufferedOutputStream(output));
			if(keyType.isPrivate()) {
				objOutStream.writeObject(this.privateKey);
			} else {
				objOutStream.writeObject(this.publicKey);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(objOutStream != null) {
				objOutStream.close();
				if(output != null) {
					output.close();
				}
			}
		}
	}

	@Override
	public void loadKeyFromFile(EnumAsymmetricKeyTypes keyType, FileInputStream input) throws IOException, ClassNotFoundException {
		ObjectInputStream objInStream = null;
		try {
			objInStream = new ObjectInputStream(new BufferedInputStream(input));
			if(keyType.isPrivate()) {
				this.privateKey = (PrivateKey)objInStream.readObject();
			} else {
				this.publicKey = (PublicKey)objInStream.readObject();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			if(objInStream != null) {
				objInStream.close();
				if(input != null) {
					input.close();
				}
			}
		}
	}*/
	
}
