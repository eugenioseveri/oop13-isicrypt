package test;

import gui.models.GlobalSettings;

import java.awt.Color;
import java.awt.Font;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.NoSuchPaddingException;


public class Test {
	
	public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, NullPointerException, IOException, ClassNotFoundException {
	/*	AES myaes = new AES();
		myaes.setSymmetricKeySpec(new SecretKeySpec(new byte[128/8], "AES"));
		byteArrayStamp(myaes.getSymmetricKeySpec().getEncoded());
		myaes.encode(new ByteArrayInputStream(new byte[10]), new ByteArrayOutputStream());
		//System.out.println(myaes.getSymmetricKeySpec().getEncoded().toString());*/
		GlobalSettings ciao = new GlobalSettings();
		ciao.setButtonColor(Color.black);
		ciao.setForegroundColor(Color.white);
		ciao.setPanelBakColor(Color.DARK_GRAY);
		ciao.setFont(new Font("Verdana", Font.BOLD, 12));
		ciao.storeSettings();
	}
	
	/*private static void byteArrayStamp(byte[] stamp){
		int i;
		System.out.println("byte[] length: " + stamp.length);
		for(i = 0; i < stamp.length; i++){
			System.out.print((char)stamp[i]);
		}
		System.out.println("");
	}*/
}
