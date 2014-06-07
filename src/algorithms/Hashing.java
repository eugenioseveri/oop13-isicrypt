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

public class Hashing implements IHashing {
	
	private final int DIM_BUFFER = 1024;
	private static final Hashing SINGLETON = new Hashing();
	
	private Hashing(){
	}
	
	public static Hashing getInstance(){
		return SINGLETON;
	}

	@Override
	public String generateHash(EnumAvailableHashingAlgorithms hashingAlgorithm, InputStream stream) {
		StringBuffer stringBuffer = new StringBuffer("");
		int nread;
		byte[] mdBytes = null;
		try {
			//Create MessageDigest object that implement the selected algorithm
			MessageDigest md = MessageDigest.getInstance(hashingAlgorithm.name());
			//Select the key dimension 
			byte[] dataBytes = new byte[DIM_BUFFER];
			nread = stream.read(dataBytes);
			//Calculating sha1 with is class
			while(nread > 0){
				md.update(dataBytes, 0, nread);
				nread = stream.read(dataBytes);
			}	
			mdBytes = md.digest();
			
		} catch (IOException e) {
			return null;
		} catch (NoSuchAlgorithmException e) {
			return null;
		} finally {
			if (stream != null){
		        try {
		            stream.close();
		        } catch (IOException e) {
		        	return null;
		        }
			}
		}
		for(byte temp : mdBytes){
			//String format for messageDigest to convert byte to Hexadecimal, temp & 0xff: get the last byte
			stringBuffer.append(String.format("%02x", temp & 0xff));
		}		
		
		return stringBuffer.toString();
	}
	//Commentare nella java dc che l'utente deve usare lo stesso algorithm
	@Override
	public boolean compare(String hashOne, String hashTwo){
		//equalIgnoreCase = no case sensitive
		return hashOne.equalsIgnoreCase(hashTwo);
	}

	@Override
	public boolean compare(EnumAvailableHashingAlgorithms hashingAlgorithm, InputStream streamOne, InputStream streamTwo){
		String fileOne = generateHash(hashingAlgorithm, streamOne);
		String fileTwo = generateHash(hashingAlgorithm, streamTwo);
		return fileOne.equals(fileTwo);
	}
	
	@Override
	public boolean compare(EnumAvailableHashingAlgorithms hashingAlgorithm, InputStream streamOne, String hashTwo){
		String compare = generateHash(hashingAlgorithm, streamOne);
		return compare.equals(hashTwo);
	}
	
}