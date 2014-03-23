package cryptography.interfacesandabstractclasses;

import java.io.InputStream;
import java.io.OutputStream;
import java.security.Key;

/**
 * Interface used to define mandatory methods of cryptography classes.
 * @author Eugenio Severi
 */
public interface ICryptography {
	boolean encode(InputStream input, OutputStream output, Key encodingKey);
	boolean decode(InputStream input, OutputStream output, Key decondingKey);
	void generateKey();
}
