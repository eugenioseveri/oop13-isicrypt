package algorithms;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.SecureRandom;

import algorithms.interfacesandabstractclasses.IWiping;

import static algorithms.ErrorMessages.*;

/**
 * Class used to implement wiping algorithms.
 * The Singleton pattern with lazy initialization has been used.
 * @author Eugenio Severi
 */
public final class Wiping implements IWiping {
	
	private static Wiping singleton;
	
	private Wiping() {
	}
	
	/**
	 * Returns an instance of this class
	 */
	public static Wiping getInstance() {
		if (singleton == null) {
			singleton = new Wiping();
		}
		return singleton;
	}

	@Override
	public void wipe(File fileToWipe, int numberOfPassages) throws IOException {
		if(fileToWipe.exists()) {
			SecureRandom rand = new SecureRandom();
			RandomAccessFile randomFile = null;
			try {
				randomFile = new RandomAccessFile(fileToWipe, "rw");
				FileChannel channel = randomFile.getChannel();
				MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, randomFile.length());
				while(numberOfPassages != 0) {
					byte[] randomBytes = new byte[1];
					while (buffer.hasRemaining()) { // Sovrascrittura del file con dati random
						rand.nextBytes(randomBytes);
						buffer.put(randomBytes[0]);
					}
					buffer.force();
					numberOfPassages--;
				}
			} catch (IOException e) {
				throw new IOException(IO_WRITING_ERROR + " File: " + fileToWipe.getAbsolutePath());
			} finally {
				if(randomFile != null) {
					randomFile.close();
					fileToWipe.delete();
				}
			}
		}
	}
}
