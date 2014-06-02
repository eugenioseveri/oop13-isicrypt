package algorithms.interfacesandabstractclasses;

import java.io.InputStream;
import java.io.OutputStream;

import algorithms.GZip;

/**
 * Interface used to define mandatory methods for compression classes.
 * @author Eugenio Severi
 */
public interface ICompression {
	
	/**
	 * Reads an input stream and compresses it into another one using the selected algorithm.
	 * @param input The stream you want to compress
	 * @param output The stream you want the compressed stream goes to
	 */
	void compress(InputStream origin, OutputStream destination);
	
	/**
	 * Reads an input stream and decompresses it into another one using the selected algorithm.
	 * @param input The stream you want to decompress
	 * @param output The stream you want the decompressed stream goes to
	 */
	void decompress(InputStream origin, OutputStream destination);
}
