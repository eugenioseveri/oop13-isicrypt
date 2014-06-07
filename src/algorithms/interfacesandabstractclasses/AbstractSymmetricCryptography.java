package algorithms.interfacesandabstractclasses;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * Abstract class used to implement getters/setters methods for symmetric algorithms classes.
 * @author Eugenio Severi
 */
public abstract class AbstractSymmetricCryptography implements ISymmetricCryptography {

	protected SecretKeySpec symmetricKeySpec;
	protected Cipher cryptoCipher;
	
	@Override
	public SecretKeySpec getSymmetricKeySpec() {
		return this.symmetricKeySpec;
	}

	@Override
	public void setSymmetricKeySpec(final SecretKeySpec symmetricKeySpec) {
		this.symmetricKeySpec = symmetricKeySpec;
	}
	
	protected boolean isSymmetricKeyInitialized() {
		return symmetricKeySpec != null;
	}
}
