package algorithms.interfacesandabstractclasses;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * Abstract class used to implement getters/setters methods for symmetric algorithms classes.
 * @author Eugenio Severi
 */
public abstract class AbstractSymmetricCryptography implements ICryptography {

	// TODO: codice duplicato per la crittografia assimmetrica?
	protected final static String NOKEY_ERROR = "Non è stata impostata una chiave di cifratura!";
	protected final static String WRONG_KEYSIZE_ERROR = "Il valore di keySize non è valido!";
	protected SecretKeySpec symmetricKeySpec;
	protected Cipher cryptoCipher;
	
	public SecretKeySpec getSymmetricKeySpec() {
		return this.symmetricKeySpec;
	}

	public void setSymmetricKeySpec(SecretKeySpec symmetricKeySpec) {
		this.symmetricKeySpec = symmetricKeySpec;
	}
	
	protected boolean isSymmetricKeyInitialized() {
		return symmetricKeySpec != null;
	}
}
