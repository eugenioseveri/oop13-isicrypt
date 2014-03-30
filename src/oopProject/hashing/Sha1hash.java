package oopProject.hashing;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.swing.JPanel;	//need for the interface that select the file

public class Sha1hash extends JPanel implements IHashing {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int nread;
	StringBuffer sb = new StringBuffer("");
	String lopo;
	DataInputStream stream;
	
	public Sha1hash(String str, DataInputStream stm){
		this.lopo = str;
		this.stream = stm;
	}
	public Sha1hash(){
	}
	/*Eventualmente creare generatehash che calcola sia sha1 che md5 direttamente,
	 *  in modo tale che si possa avere tutt le info senza passare il file piu volte.
	 *  Una soluzione sarebbe riuscire a non chiudere lo stream nel finally in modo tale da creare un'oggetto stream
	 *  e passarlo piu volte a generateHash. ( con un while != true o con OpenButtons in ascolto )*/
	/*Gestire l'annullamento dell'aperturra file*/
	@Override
	public String generateHash() {
		//create the interface for the selection of the file
		try {
			//Create MessageDigest and select the sha-1 algorithm
			MessageDigest md = MessageDigest.getInstance(lopo);
			//Select the key dimension i think (MUST SEARCH!!!)
			byte[] dataBytes = new byte[1024];
			nread = stream.read(dataBytes);
			//Calculating sha1 with is class
			while(nread > 0){
				md.update(dataBytes, 0, nread);
				nread = stream.read(dataBytes);
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
			if (stream != null){
		        try {
		            stream.close();
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
			}
		}
		return sb.toString();
	}

	@Override
	public boolean compare(String st1, String st2) {
		return st1.equalsIgnoreCase(st2);	//NO casesensitive!
	}

}
