package algorithms.interfacesandabstractclasses;

import java.io.File;
import java.io.IOException;

/**
 * Interface used to define mandatory methods for wiping classes.
 * @author Eugenio
 */
public interface IWiping {
	void wipe(File fileToWipe, int numberOfPassages);
}
