package algorithms;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import algorithms.interfacesandabstractclasses.ICompression;

import static algorithms.ErrorMessages.*;

/**
 * Class used to implement GZIP compression and decompression.
 * The Singleton pattern with lazy initialization has been used.
 * @author Eugenio Severi
 */
public class GZip implements ICompression {
	
	private final static int BUFFER_SIZE = 1024;
	private static GZip SINGLETON = null;
	
	private GZip() { // Costruttore privato
	}
	
	/**
	 * Returns an instance of this class
	 */
	public static GZip getInstance() {
		if (SINGLETON == null) {
			SINGLETON = new GZip();
		}
		return SINGLETON;
	}

	/**
	 * Reads an input stream and compresses it into another one using GZIP algorithm.
	 * @param input The stream you want to compress
	 * @param output The stream you want the compressed stream goes to
	 */
	@Override
	public void compress(InputStream origin, OutputStream destination) {
		GZIPOutputStream zip = null;
		int spaceLeft;
	    byte[] buffer = new byte[BUFFER_SIZE];
		try {
			zip = new GZIPOutputStream(destination);
			while ((spaceLeft = origin.read(buffer)) > 0) {
		    	zip.write(buffer, 0, spaceLeft);
		    }
		    zip.finish();
		} catch (IOException e) {
			System.err.println(IO_WRITING_ERROR);
			e.printStackTrace();
		} finally {
			try {
				zip.close();
				origin.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Reads an input stream and decompresses it into another one using GZIP algorithm.
	 * @param input The stream you want to decompress
	 * @param output The stream you want the decompressed stream goes to
	 */
	@Override
	public void decompress(InputStream origin, OutputStream destination) {
		// Aggiungere try-catch
		GZIPInputStream zip = null;
		int spaceLeft;
		byte[] buffer = new byte[BUFFER_SIZE];
		try {
			zip = new GZIPInputStream(origin);
			while ((spaceLeft = zip.read(buffer)) > 0) {
				destination.write(buffer, 0, spaceLeft);
			}
		} catch (IOException e) {
			System.err.println(IO_WRITING_ERROR);
			e.printStackTrace();
		} finally {
			try {
				zip.close();
				destination.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}