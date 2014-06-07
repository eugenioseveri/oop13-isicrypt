package algorithms.interfacesandabstractclasses;

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

import algorithms.EnumAsymmetricKeyTypes;

/**
 * Abstract class used to implement getters/setters methods for asymmetric algorithms classes.
 * @author Eugenio Severi
 */
public abstract class AbstractAsymmetricCryptography implements IAsymmetricCryptography { // Ha senso mettere "implements" in una classe astratta?
	
	protected KeyPair keyPair;
	protected Cipher cryptoCipher;
	
	@Override
	public PublicKey getPublicKey() {
		return this.keyPair.getPublic();
	}

	@Override
	public PrivateKey getPrivateKey() {
		return this.keyPair.getPrivate();
	}

	@Override
	public void setKeyPair(final KeyPair pair) {
		this.keyPair = pair;
	}


	@Override
	public void setKeyPair(final PublicKey publicKey, final PrivateKey privateKey) {
		this.keyPair = new KeyPair(publicKey, privateKey);
	}

	protected boolean isKeyPairInitialized() {
		return this.keyPair != null;
	}
	
	@Override
	public void saveKeyToFile(final EnumAsymmetricKeyTypes keyType, final FileOutputStream output) throws IOException {
		ObjectOutputStream objOutStream = null;
		try {
			objOutStream = new ObjectOutputStream(new BufferedOutputStream(output));
			if(keyType.isPrivate()) {
				objOutStream.writeObject(getPrivateKey());
			} else {
				objOutStream.writeObject(getPublicKey());
			}
		} catch (IOException e) {
			throw e;
		} finally {
			if(objOutStream != null) {
				objOutStream.close();
			}
		}
	}
	
	@Override
	public void loadKeyFromFile(final EnumAsymmetricKeyTypes keyType, final FileInputStream input) throws IOException {
		ObjectInputStream objInStream = null;
		try {
			objInStream = new ObjectInputStream(new BufferedInputStream(input));
			if(keyType.isPrivate()) { // Reimposta solo la chiave richiesta e conserva l'altra (se già presente).
				this.keyPair = new KeyPair(getPublicKey(), (PrivateKey)objInStream.readObject());
			} else {
				this.keyPair = new KeyPair((PublicKey)objInStream.readObject(), getPrivateKey());
			}
		} catch (IOException e) {
			throw e;
		} catch (ClassNotFoundException e) {
			// This exception can not occur
		} finally {
			if(objInStream != null) {
				objInStream.close();
			}
		}
	}

}
