package oopProject.hashing;

public interface IHashing {
	/**
	 * Generate Hashing from a file
	 */
	public String generateHash();
	/**
	 * Compare a file or a hash whit another file or hash
	 */
	public boolean compare(String st1, String st2);
}
