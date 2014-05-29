package algorithms.interfacesandabstractclasses;

import java.io.InputStream;

import algorithms.EnumAvailableHashingAlgorithms;
/**
 * Interface used to define mandatory methods of hashing classes.
 * @author Filippo Vimini
 */
public interface IHashing {

	 String generateHash(EnumAvailableHashingAlgorithms hashingAlgorithm, InputStream stream);
	 boolean compare(String hashOne, String hashTwo);
	 boolean compare(EnumAvailableHashingAlgorithms hashingAlgorithm, InputStream streamOne, InputStream streamTwo);
	 boolean compare(EnumAvailableHashingAlgorithms hashingAlgorithm, InputStream streamOne, String hashTwo);
	 
}
