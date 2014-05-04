package cryptography.algorithms;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import cryptography.interfacesandabstractclasses.ICompression;

/**
 * FARE VERSIONE THREADED
 * Class used to implement GZIP compression and decompression.
 * The Singleton pattern with lazy initialization has been used.
 * @author Eugenio Severi
 */
public class GZip implements ICompression {
	
	private final static int BUFFER_SIZE = 1024;
	private static GZip SINGLETON = null;
	
	private GZip() { // Costruttore privato
	}
	
	public static GZip getInstance() {
		if (SINGLETON == null) {
			SINGLETON = new GZip();
		}
		return SINGLETON;
	}

	@Override
	public void compress(InputStream origin, OutputStream destination) throws IOException {
		// Aggiungere try-catch
		GZIPOutputStream zip = new GZIPOutputStream(destination);
		int spaceLeft;
	    byte[] buffer = new byte[BUFFER_SIZE];
	    while ((spaceLeft = origin.read(buffer)) > 0) {
	    	zip.write(buffer, 0, spaceLeft);
	    }
	    zip.finish();
	    zip.close();
	    destination.close();
	    origin.close();
	}

	@Override
	public void decompress(InputStream origin, OutputStream destination) throws IOException {
		// Aggiungere try-catch
		GZIPInputStream zip = new GZIPInputStream(origin);
		int spaceLeft;
		byte[] buffer = new byte[BUFFER_SIZE];
		while ((spaceLeft = zip.read(buffer)) > 0) {
			destination.write(buffer, 0, spaceLeft);
		}
		zip.close();
		destination.close();
		origin.close();
	}
}