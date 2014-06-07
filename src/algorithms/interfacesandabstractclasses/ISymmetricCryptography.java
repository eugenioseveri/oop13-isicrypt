package algorithms.interfacesandabstractclasses;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import javax.crypto.spec.SecretKeySpec;


/**
 * Interface used to define mandatory methods of cryptography classes.
 * @author Eugenio Severi
 */
public interface ISymmetricCryptography {
	
	/**
	 * Encode a stream into another one, using a previously set key (via @link {@link #generateKey(int)} or @link {@link #setSymmetricKeySpec(SecretKeySpec)})
	 * @param input The stream you want to encrypt
	 * @param output The stream you want the encrypted stream goes to
	 * @throws IOException If an error occurs while writing the output stream
	 * @throws InvalidKeyException If a key has not been set or is not valid
	 */
	void encode(InputStream input, OutputStream output) throws IOException, InvalidKeyException;
	
	/**
	 * Decode a stream into another one, using a previously set key (via @link {@link #generateKey(int)} or @link {@link #setSymmetricKeySpec(SecretKeySpec)})
	 * @param input The stream you want to encrypt
	 * @param output The stream you want the encrypted stream goes to
	 * @throws IOException If an error occurs while writing the output stream
	 * @throws InvalidKeyException If a key has not been set or is not valid
	 */
	void decode(InputStream input, OutputStream output) throws IOException, InvalidKeyException;
	
	/**
	 * Generates a new symmetric key of the specified size
	 * @param keySize The length (in bits) of the generated key
	 * @throws InvalidKeyException If the key size is not valid.
	 */
	void generateKey(int keySize) throws InvalidKeyException;
	
	SecretKeySpec getSymmetricKeySpec();
	
	void setSymmetricKeySpec(SecretKeySpec symmetricKeySpec);
}
