package gui.models;

/**
 * This exception occurs when some data and its checksum don't match
 */
public class CorruptedDataException extends Exception {

	private static final long serialVersionUID = -6065089324864770515L;
	
	public CorruptedDataException() {
		super();
	}

	public CorruptedDataException(String message) {
		super(message);
	}
}
