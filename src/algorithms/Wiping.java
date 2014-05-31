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
public class Wiping implements IWiping {
	
	private static Wiping SINGLETON = null;
	
	private Wiping() {
	}
	
	/**
	 * Returns an instance of this class
	 */
	public static Wiping getInstance() {
		if (SINGLETON == null) {
			SINGLETON = new Wiping();
		}
		return SINGLETON;
	}

	/**
	 * Securely deletes a file with random generated data in multiple passages
	 * @param fileToWipe The file you want to erase
	 * @param numberOfPassages The times you want to overwrite the file
	 */
	@Override
	public void wipe(File fileToWipe, int numberOfPassages) {
		// Gestire l'eccezione e l'input dei parametri
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
				System.err.println(IO_WRITING_ERROR + " File: " + fileToWipe.getAbsolutePath());
				e.printStackTrace();
			} finally {
				try {
					randomFile.close();
					fileToWipe.delete(); // Al momento il file non � cancellato(?)
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
		}
	}
}
