package cryptography.interfacesandabstractclasses;

import java.security.Key;

/**
 * Abstract class used to implement getters/setters methods for symmetric algorithms classes.
 * @author Eugenio Severi
 */
public abstract class AbstractSymmetricCryptography implements ICryptography {

	private Key symmetricKey;

	Key getKey() {
		return this.symmetricKey;
	}
	
	void setKey(Key newKey) {
		// Aggiungere controllo sull'input?
		this.symmetricKey = newKey;
	}
}
