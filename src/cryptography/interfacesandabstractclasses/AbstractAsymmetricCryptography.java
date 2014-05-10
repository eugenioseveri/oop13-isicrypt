package cryptography.interfacesandabstractclasses;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.Cipher;

import cryptography.algorithms.EnumAsymmetricKeyTypes;

/**
 * Abstract class used to implement getters/setters methods for asymmetric algorithms classes.
 * @author Eugenio Severi
 */
public abstract class AbstractAsymmetricCryptography implements IAsymmetricCryptography { // Ha senso mettere "implements" in una classe astratta?
	
	protected KeyPair keyPair;
	protected Cipher cryptoCipher;
	
	public PublicKey getPublicKey() {
		return this.keyPair.getPublic();
	}

	public PrivateKey getPrivateKey() {
		return this.keyPair.getPrivate();
	}

	public void setKeyPair(KeyPair pair) { // Due diversi modi di chiamare il metodo (vedi sotto)
		this.keyPair = pair;
	}
	
	public void setKeyPair(PublicKey publicKey, PrivateKey privateKey) {
		this.keyPair = new KeyPair(publicKey, privateKey);
	}
	
	@Override
	public void saveKeyToFile(EnumAsymmetricKeyTypes keyType, FileOutputStream output) throws IOException {
		ObjectOutputStream objOutStream = null;
		try {
			objOutStream = new ObjectOutputStream(new BufferedOutputStream(output));
			if(keyType.isPrivate()) {
				objOutStream.writeObject(getPrivateKey());
			} else {
				objOutStream.writeObject(getPublicKey());
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
			if(keyType.isPrivate()) { // Reimposta solo la chiave richiesta e conserva l'altra (se già presente).
				this.keyPair = new KeyPair(getPublicKey(), (PrivateKey)objInStream.readObject());
			} else {
				this.keyPair = new KeyPair((PublicKey)objInStream.readObject(), getPrivateKey());
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
	}

}
