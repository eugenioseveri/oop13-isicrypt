package cryptography.interfacesandabstractclasses;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * Abstract class used to implement getters/setters methods for asymmetric algorithms classes.
 * @author Eugenio Severi
 */
public abstract class AbstractAsymmetricCryptography implements ICryptography {
	
	private KeyPair asymmetricKeys;
	
	PrivateKey getPrivateKey(){
		return this.asymmetricKeys.getPrivate();
	}
	
	PublicKey getPublicKey() {
		return this.asymmetricKeys.getPublic();
	}
	
	void setKeyPair(PublicKey publicKey, PrivateKey privateKey) {
		// Aggiungere controllo sull'input?
		this.asymmetricKeys = new KeyPair(publicKey, privateKey);
	}

}
