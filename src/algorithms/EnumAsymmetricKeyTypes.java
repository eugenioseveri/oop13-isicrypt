package algorithms;

/**
 * @author Eugenio Severi
 */
public enum EnumAsymmetricKeyTypes {
	PRIVATE_KEY, PUBLIC_KEY;
	public boolean isPrivate() {
		return this == PRIVATE_KEY;
	}
}