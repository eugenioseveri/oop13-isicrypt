package algorithms.interfacesandabstractclasses;

import java.io.IOException;
import java.io.InputStream;

import algorithms.EnumAvailableHashingAlgorithms;
/**
 * Interface used to define mandatory methods of hashing classes.
 * @author Filippo Vimini
 * Created 08/03/2014
 */
public interface IHashing {

	/**
	 * Generate a checksum for the specified stream
	 * 
	 * @param hashingAlgorithm 	enum that represent the String of hashing algorithm 
	 * @param stream			InputStream for the file which will be calculated the algorithm
	 * @return The generated checksum
	 * @throws IOException		If an error occurs while reading the stream
	 */
	 String generateHash(EnumAvailableHashingAlgorithms hashingAlgorithm, InputStream stream) throws IOException;
	 
	 /**
	  * Compare two String
	  * 
	  * @param hashOne			The hash of the first stream
	  * @param hashTwo			The hash of the second stream
	  * @return If the hashes match
	  */
	 boolean compare(String hashOne, String hashTwo);
	
	 /**
	  * Compare two files with the same algorithm
	  * 
	  * @param hashingAlgorithm		enum that represent the String of hashing algorithm 
	  * @param streamOne			InputStream for the file which will be calculated the algorithm
	  * @param streamTwo			InputStream for the file which will be calculated the algorithm
	  * @return If the hashes match
	 * @throws IOException 			If an error occurs while reading a stream
	  */
	 boolean compare(EnumAvailableHashingAlgorithms hashingAlgorithm, InputStream streamOne, InputStream streamTwo) throws IOException;
	
	 /**
	  * Compare file hashing and string
	  * 
	  * @param hashingAlgorithm		enum that represent the String of hashing algorithm 
	  * @param streamOne			InputStream for the file which will be calculated the algorithm
	  * @param hashTwo				String that contain a hash
	  * @return If the hashes match
	 * @throws IOException 			If an error occurs while reading a stream
	  */
	 boolean compare(EnumAvailableHashingAlgorithms hashingAlgorithm, InputStream streamOne, String hashTwo) throws IOException;
	 
}
