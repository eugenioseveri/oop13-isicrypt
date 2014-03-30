package oopProject.hashing;

import java.io.DataInputStream;

public class TestClass {

	public static void main(String[] args) {

		String ciao = new Sha1hash("SHA1", new OpenButtons().fileChooser()).generateHash();
		String ciao2 = new Sha1hash("MD5", new OpenButtons().fileChooser()).generateHash();
		System.out.println("Sha1 algorithm: "+ciao);
		System.out.println("MD5 algorithm: "+ciao2);
		if(!new Sha1hash().compare(ciao, ciao2) ){
			System.out.println("The files don't match");
		}
		else System.out.println("The files match");
		
		//Test Sha1 e MD5 insieme.
		/*DataInputStream lopo = new OpenButtons().fileChooser();
		DataInputStream lopo2 = lopo;
		String Gincapa = new Sha1hash("SHA1", lopo).generateHash();
		String Gincapa2 = new Sha1hash("MD5", lopo2).generateHash();
		System.out.println("SHA1: "+Gincapa+"\nMD5: "+Gincapa2);*/
	}	
}
