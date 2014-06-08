package algorithms;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import algorithms.interfacesandabstractclasses.ICompression;

/**
 * Class used to implement GZIP compression and decompression.
 * The Singleton pattern with lazy initialization has been used.
 * @author Eugenio Severi
 */
public final class GZip implements ICompression {
	
	private final static int BUFFER_SIZE = 1024;
	private static GZip singleton;
	
	private GZip() {
	}
	
	/**
	 * @return An instance of this class
	 */
	public static GZip getInstance() {
		if (singleton == null) {
			singleton = new GZip();
		}
		return singleton;
	}

	@Override
	public void compress(final InputStream origin, final OutputStream destination) throws IOException {
		GZIPOutputStream zip = null;
		int spaceLeft;
	    final byte[] buffer = new byte[BUFFER_SIZE];
		try {
			zip = new GZIPOutputStream(destination);
			while ((spaceLeft = origin.read(buffer)) > 0) {
		    	zip.write(buffer, 0, spaceLeft);
		    }
		    zip.finish();
		} catch (IOException e) {
			throw e;
		} finally {
			if(zip != null) {
				zip.close();
			}
			if(origin != null) {
				origin.close();
			}
		}
	}
	
	@Override
	public void decompress(final InputStream origin, final OutputStream destination) throws IOException {
		GZIPInputStream zip = null;
		int spaceLeft;
		final byte[] buffer = new byte[BUFFER_SIZE];
		try {
			zip = new GZIPInputStream(origin);
			while ((spaceLeft = zip.read(buffer)) > 0) {
				destination.write(buffer, 0, spaceLeft);
			}
		} catch (IOException e) {
			throw e;
		} finally {
			if(zip != null) {
				zip.close();
			}
			if(destination != null) {
				destination.close();
			}
		}
	}
}