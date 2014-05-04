package cryptography.algorithms;

import java.io.*;
import java.security.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import cryptography.algorithms.FileInterpret.Reader;
import cryptography.algorithms.FileInterpret.Writer;
import test.OpenButtons;


public class TestClass {

	public static void main(String[] args) throws InvalidKeyException, FileNotFoundException, IOException, NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, ClassNotFoundException, IllegalBlockSizeException, BadPaddingException {
		//JFileChooser fileChooser = new JFileChooser();
		/*String ciao = new Hashing("SHA1", new OpenButtons().fileChooser()).generateHash();
		String ciao2 = new Hashing("MD5", new OpenButtons().fileChooser()).generateHash();
		System.out.println("Sha1 algorithm: "+ciao);
		System.out.println("MD5 algorithm: "+ciao2);*/
		/*AES newAES = new AES();
		newAES.generateKey(128);
		newAES.encode(new FileInputStream("aaa.txt"), new FileOutputStream("bbb.txt"));*/
		
		//newAES.encode(new TestClass().FileChooser(), "bbb.txt");
		//FileInterpret.FileInterpret.Reader aaa = new FileInterpret.FileInterpret.Reader(null);
		//FileInterpret.FileInterpret bbb = new FileInterpret.FileInterpret(null, null, null, null, true, null);
		//FileInterpretReader aaa = new FileInterpretReader(null);
		//FileInterpret ccc = aaa.getReader();
		/*FileInterpret.Reader newreader;
		//FileInterpret ddd = new FileInterpretReader(null).getReader();
		Reader eee = new FileInterpret.Reader(null);
		FileInterpret nuovo = new FileInterpret(null, null, null, null, false, null);
		Writer wri = new Writer(null);
		wri.writeInterpret(nuovo);*/
		/*GZip.getInstance().compress(new FileInputStream("aaa.txt"), new FileOutputStream("ccc.txt"));
		GZip.getInstance().decompress(new FileInputStream("ccc.txt"), new FileOutputStream("ddd.txt"));*/
		//System.out.println((Math.log(4096)/Math.log(2)));
		/*RSA newrsa = new RSA();
		newrsa.generateKeyPair(1024);
		byte[] bytearray = new byte[] {0,1,2,3};
		byte[] bytearrayencrypted = newrsa.encode(bytearray);
		System.out.println(bytearrayencrypted[0]);
		byte[] bytearraydecrypted = newrsa.decode(bytearrayencrypted);
		System.out.println(bytearraydecrypted[0]);*/
		File newFile = new File("eee.txt");
		Wiping.getInstance().wipe(newFile, 1);
		
		
		
	}	

	/*public File FileChooser(){
		int controllor = fileChooser.showOpenDialog(getParent());
		File fileReturn = fileChooser.getSelectedFile(); //Grezzata, cambiala se riesci
		if(controllor == JFileChooser.CANCEL_OPTION || controllor == JFileChooser.ERROR_OPTION){
			System.out.println("File non selezionato");
			//Da Cambiare quando si inserisce nella GUI!!!!!
			System.exit(ABORT);
		}
		return fileReturn;
	}*/
}
