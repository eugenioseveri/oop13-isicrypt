package algorithms.interfacesandabstractclasses;

import java.io.File;
import java.io.IOException;

/**
 * Interface used to define mandatory methods for wiping classes.
 * @author Eugenio
 */
public interface IWiping {
	
	/**
	 * Securely deletes a file with random generated data in multiple passages
	 * @param fileToWipe The file you want to erase
	 * @param numberOfPassages The times you want to overwrite the file
	 * @throws IOException If an error occurs while wiping the file
	 */
	void wipe(File fileToWipe, int numberOfPassages) throws IOException;
}
