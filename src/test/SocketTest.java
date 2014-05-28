package test;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import algorithms.SocketClient;
import gui.models.ContactInfo;


public class SocketTest extends Thread {

	public static void main(String[] args) throws InterruptedException, IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, NoSuchProviderException, InvalidKeySpecException, IllegalBlockSizeException, BadPaddingException {
		ContactInfo Gianni = new ContactInfo("Gianni", "localhost");
		SocketClient client = new SocketClient(Gianni, "Supporre che la cacca ");
		client.start();
	}

}
