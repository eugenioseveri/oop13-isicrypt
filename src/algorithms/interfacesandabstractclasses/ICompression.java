package algorithms.interfacesandabstractclasses;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Interface used to define mandatory methods for compression classes.
 * @author Eugenio Severi
 */
public interface ICompression {
	
	/**
	 * Reads an input stream and compresses it into another one using the selected algorithm.
	 * @param input The stream you want to compress
	 * @param output The stream you want the compressed stream goes to
	 * @throws IOException If an error occurs during compression
	 */
	void compress(InputStream origin, OutputStream destination) throws IOException;
	
	/**
	 * Reads an input stream and decompresses it into another one using the selected algorithm.
	 * @param input The stream you want to decompress
	 * @param output The stream you want the decompressed stream goes to
	 * @throws IOException If an error occurs during decompression
	 */
	void decompress(InputStream origin, OutputStream destination) throws IOException;
}
