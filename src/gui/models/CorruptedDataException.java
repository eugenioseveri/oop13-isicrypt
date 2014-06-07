package gui.models;

/**
 * This exception occurs when some data and its checksum don't match
 * @author Eugenio Severi
 */
public class CorruptedDataException extends Exception {

	private static final long serialVersionUID = -6065089324864770515L;
	
	public CorruptedDataException() {
		super();
	}

	public CorruptedDataException(final String message) {
		super(message);
	}
}
