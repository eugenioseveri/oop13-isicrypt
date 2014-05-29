package algorithms.interfacesandabstractclasses;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;


/**
 * Interface used to define mandatory methods of cryptography classes.
 * @author Eugenio Severi
 */
public interface ICryptography {
	void encode(InputStream input, OutputStream output) throws IOException;
	void decode(InputStream input, OutputStream output) throws IOException;
	void generateKey(int keySize) throws InvalidKeyException;
}
