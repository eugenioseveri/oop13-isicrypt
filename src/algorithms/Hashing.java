package algorithms;
/**
 * @author Filippo Vimini
 *Created 08/03/2014
 *
 *Class that generate hashing algorithm for check the correctness of file
 *implements singleton pattern
 */
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.IOException;
import java.io.InputStream;

import algorithms.interfacesandabstractclasses.IHashing;

public final class Hashing implements IHashing {
	
	private static final int DIM_BUFFER = 1024;
	private static final Hashing SINGLETON = new Hashing();
	
	private Hashing(){
	}
	
	public static Hashing getInstance(){
		return SINGLETON;
	}

	@Override
	public String generateHash(final EnumAvailableHashingAlgorithms hashingAlgorithm, final InputStream stream) throws IOException {
		final StringBuffer stringBuffer = new StringBuffer("");
		int nread;
		byte[] mdBytes = null;
		try {
			// Create MessageDigest object that implements the selected algorithm
			final MessageDigest md = MessageDigest.getInstance(hashingAlgorithm.name());
			// Select the key dimension 
			final byte[] dataBytes = new byte[DIM_BUFFER];
			nread = stream.read(dataBytes);
			// Calculating checksum with its class
			while(nread > 0){
				md.update(dataBytes, 0, nread);
				nread = stream.read(dataBytes);
			}	
			mdBytes = md.digest();
			
		} catch (IOException e) {
			throw e;
		} catch (NoSuchAlgorithmException e) {
			// This exceptions can not occur since hashingAlgorithm is choosen from an Enum of valid algorithms
		} finally {
			if (stream != null){
				stream.close();
			}
		}
		for(byte temp : mdBytes){
			//String format for messageDigest to convert byte to Hexadecimal, temp & 0xff: get the last byte
			stringBuffer.append(String.format("%02x", temp & 0xff));
		}		
		return stringBuffer.toString();
	}
	
	@Override
	public boolean compare(final String hashOne, final String hashTwo){
		//equalIgnoreCase = no case sensitive
		return hashOne.equalsIgnoreCase(hashTwo);
	}

	@Override
	public boolean compare(final EnumAvailableHashingAlgorithms hashingAlgorithm, final InputStream streamOne, final InputStream streamTwo) throws IOException{
		final String fileOne = generateHash(hashingAlgorithm, streamOne);
		final String fileTwo = generateHash(hashingAlgorithm, streamTwo);
		return fileOne.equals(fileTwo);
	}
	
	@Override
	public boolean compare(final EnumAvailableHashingAlgorithms hashingAlgorithm, final InputStream streamOne, final String hashTwo) throws IOException{
		final String compare = generateHash(hashingAlgorithm, streamOne);
		return compare.equals(hashTwo);
	}
	
}