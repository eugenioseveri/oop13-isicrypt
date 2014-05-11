package cryptography.interfacesandabstractclasses;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * Abstract class used to implement getters/setters methods for symmetric algorithms classes.
 * @author Eugenio Severi
 */
public abstract class AbstractSymmetricCryptography implements ICryptography {

	protected SecretKeySpec symmetricKeySpec;
	protected Cipher cryptoCipher;
	
	public SecretKeySpec getSymmetricKeySpec() {
		return this.symmetricKeySpec;
	}

	public void setSymmetricKeySpec(SecretKeySpec symmetricKeySpec) {
		this.symmetricKeySpec = symmetricKeySpec;
	}
}
