package cryptography.interfacesandabstractclasses;

import java.io.InputStream;
import java.io.OutputStream;
import javax.crypto.SecretKey;

/**
 * DEPRECATED?
 * Abstract class used to implement getters/setters methods for symmetric algorithms classes.
 * @author Eugenio Severi
 */
public abstract class AbstractSymmetricCryptography implements ICryptography {

	private InputStream input;
	private OutputStream output;
	private SecretKey symmetricKey;
	
	/*public AbstractSymmetricCryptography(InputStream input, OutputStream output) {
		this.input = input;
		this.output = output;
	}*/
	public AbstractSymmetricCryptography() {
	}

	SecretKey getKey() {
		return this.symmetricKey;
	}
	
	void setKey(SecretKey newKey) {
		// Aggiungere controllo sull'input?
		this.symmetricKey = newKey;
	}
}
