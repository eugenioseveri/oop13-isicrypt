package algorithms.interfacesandabstractclasses;

import algorithms.Hashing;
/**
 * Interface used to define mandatory methods of hashing classes.
 * @author Filippo Vimini
 */
public interface IHashing {
	/**
	 * Generate Hashing from a file
	 */
	public String generateHash();
	/**
	 * Compare a file whit hash
	 */
	public boolean compare(String st1);
	/**
	 * Compare two files hashes
	 */
	public boolean compare(Hashing first);
}
