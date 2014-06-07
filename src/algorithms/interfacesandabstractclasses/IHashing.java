package algorithms.interfacesandabstractclasses;

import java.io.InputStream;

import algorithms.EnumAvailableHashingAlgorithms;
/**
 * Interface used to define mandatory methods of hashing classes.
 * @author Filippo Vimini
 * Created 08/03/2014
 */
public interface IHashing {

	/**
	 * Generate Hashing algorithm
	 * 
	 * @param hashingAlgorithm 	enum that represent the String of hashing algorithm 
	 * @param stream			InputStream for the file which will be calculated the algorithm
	 * @return String					
	 */
	 String generateHash(EnumAvailableHashingAlgorithms hashingAlgorithm, InputStream stream);
	 /**
	  * Compare two String
	  * 
	  * @param hashOne				
	  * @param hashTwo
	  * @return boolean
	  */
	 boolean compare(String hashOne, String hashTwo);
	 /**
	  * Compare two file with the same algorithm
	  * @param hashingAlgorithm		enum that represent the String of hashing algorithm 
	  * @param streamOne			InputStream for the file which will be calculated the algorithm
	  * @param streamTwo			InputStream for the file which will be calculated the algorithm
	  * @return boolean
	  */
	 boolean compare(EnumAvailableHashingAlgorithms hashingAlgorithm, InputStream streamOne, InputStream streamTwo);
	 /**
	  * Compare file hashing and string
	  * 
	  * @param hashingAlgorithm		enum that represent the String of hashing algorithm 
	  * @param streamOne			InputStream for the file which will be calculated the algorithm
	  * @param hashTwo				String that contain a hash
	  * @return
	  */
	 boolean compare(EnumAvailableHashingAlgorithms hashingAlgorithm, InputStream streamOne, String hashTwo);
	 
}
