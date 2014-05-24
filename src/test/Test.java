package test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import algorithms.AES;

public class Test {
	
	public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, NullPointerException, IOException {
		AES myaes = new AES();
		myaes.setSymmetricKeySpec(new SecretKeySpec(new byte[128/8], "AES"));
		byteArrayStamp(myaes.getSymmetricKeySpec().getEncoded());
		myaes.encode(new ByteArrayInputStream(new byte[10]), new ByteArrayOutputStream());
		//System.out.println(myaes.getSymmetricKeySpec().getEncoded().toString());
	}
	
	private static void byteArrayStamp(byte[] stamp){
		int i;
		System.out.println("byte[] length: " + stamp.length);
		for(i = 0; i < stamp.length; i++){
			System.out.print((char)stamp[i]);
		}
		System.out.println("");
	}
}
