package oopProject.hashing;

public class TestClass {

	public static void main(String[] args) {

		String ciao = new Sha1hash("SHA1", new OpenButtons().filChooser()).generateHash();
		String ciao2 = new Sha1hash("MD5", new OpenButtons().filChooser()).generateHash();
		System.out.println("Sha1 algorithm: "+ciao);
		System.out.println("MD5 algorithm: "+ciao2);
		if(!new Sha1hash().compare(ciao, ciao2) ){
			System.out.println("The files don't match");
		}
		else System.out.println("The files match");
	}

}
