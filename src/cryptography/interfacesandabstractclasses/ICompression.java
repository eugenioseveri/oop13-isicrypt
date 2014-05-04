package cryptography.interfacesandabstractclasses;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Interface used to define mandatory methods for compression classes.
 * @author Eugenio Severi
 */
public interface ICompression {
	void compress(InputStream origin, OutputStream destination) throws IOException;
	void decompress(InputStream origin, OutputStream destination) throws IOException;
}
