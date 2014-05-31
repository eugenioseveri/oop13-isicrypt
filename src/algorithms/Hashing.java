package algorithms;
/**
 * @author Filippo Vimini
 *Created 08/03/2014
 */
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.IOException;
import java.io.InputStream;

import algorithms.interfacesandabstractclasses.IHashing;



public class Hashing implements IHashing {

	private static final int DIM_BUFFER = 1024;
	private static final Hashing SINGLETON = new Hashing();
	
	private Hashing(){
	}
	
	public static Hashing getInstance(){
		return SINGLETON;
	}
	//Non bufferizzo l'input perchè se viene passato un buffer ribufferizza
	@Override
	public String generateHash(EnumAvailableHashingAlgorithms hashingAlgorithm, InputStream stream) {
		//create the interface for the selection of the file
		StringBuffer sb = new StringBuffer("");
		//BufferedInputStream bufferStrem = new BufferedInputStream(stream);
		int nread;
		byte[] mdBytes = null;
		try {
			//Create MessageDigest and select the sha-1 algorithm
			MessageDigest md = MessageDigest.getInstance(hashingAlgorithm.name());
			//Select the key dimension i think (MUST SEARCH!!!)
			byte[] dataBytes = new byte[DIM_BUFFER];
			nread = stream.read(dataBytes);
			//Calculating sha1 with is class
			while(nread > 0){
				md.update(dataBytes, 0, nread);
				nread = stream.read(dataBytes);
			}	
			mdBytes = md.digest();
		} catch (IOException e) {
			System.out.println("Stream.read error, can't read byte array: "+e);
			return null;
		} catch (NoSuchAlgorithmException e) {
			System.out.println("Can't select sha1 algorithm: "+e);
			return null;
		} finally {
			if (stream != null){
		        try {
		            stream.close();
		        } catch (IOException e) {
		        	System.out.println("Stream close error occured: "+e);
		        	return null;
		        }
			}
		}
		//TODO Studia come funziona e al max modificala
	    for (int i = 0; i < mdBytes.length; i++) {
	    	sb.append(Integer.toString((mdBytes[i] & 0xff) + 0x100, 16).substring(1));
	    }
		return sb.toString();
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