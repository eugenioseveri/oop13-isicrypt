package cryptography.algorithms;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JPanel;	//need for the interface that select the file

import cryptography.interfacesandabstractclasses.IHashing;

/**
 * 
 * @author Filippo Vimini
 *
 */

public class Hashing extends JPanel implements IHashing {


	private static final long serialVersionUID = -5571957322765098395L;

	String hashingAlgorithm;
	InputStream stream;
	final private static int DIM_BUFFER = 1024;
	
	public Hashing(String hashingAlgorithm, InputStream inputData){
		this.hashingAlgorithm = hashingAlgorithm;
		this.stream = inputData;
	}
	public Hashing(){
	}
	@Override
	public String generateHash() {
		//create the interface for the selection of the file
		StringBuffer sb = new StringBuffer("");
		try {
			int nread;
			//Create MessageDigest and select the sha-1 algorithm
			MessageDigest md = MessageDigest.getInstance(this.hashingAlgorithm);
			//Select the key dimension i think (MUST SEARCH!!!)
			byte[] dataBytes = new byte[DIM_BUFFER];
			nread = this.stream.read(dataBytes);
			//Calculating sha1 with is class
			while(nread > 0){
				md.update(dataBytes, 0, nread);
				nread = this.stream.read(dataBytes);
			}	
			byte[] mdBytes = md.digest();
		    for (int i = 0; i < mdBytes.length; i++) {
		    	sb.append(Integer.toString((mdBytes[i] & 0xff) + 0x100, 16).substring(1));
		    }
		} catch (FileNotFoundException e) {
			System.out.println("Can't find file Error: "+e);
		} catch (IOException e) {
			System.out.println("Stream.read error, can't read byte array");
		} catch (NoSuchAlgorithmException e) {
			System.out.println("Can't select sha1 algorithm: "+e);
		} catch (Exception e) {  //catch other exceptions
			throw e;
		} finally {
			if (this.stream != null){
		        try {
		            this.stream.close();
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
			}
		}
		return sb.toString();
	}

	@Override
	public boolean compare(String st1) {
		return st1.equalsIgnoreCase(this.generateHash());	//NO casesensitive!
	}
	@Override
	public boolean compare( Hashing first ){
		return first.generateHash().equalsIgnoreCase(this.generateHash());
	}
}
