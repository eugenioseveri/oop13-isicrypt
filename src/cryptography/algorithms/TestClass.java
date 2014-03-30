package cryptography.algorithms;

import test.OpenButtons;


public class TestClass {

	public static void main(String[] args) {

		String ciao = new Hashing("SHA1", new OpenButtons().fileChooser()).generateHash();
		String ciao2 = new Hashing("MD5", new OpenButtons().fileChooser()).generateHash();
		System.out.println("Sha1 algorithm: "+ciao);
		System.out.println("MD5 algorithm: "+ciao2);
	}	
}
