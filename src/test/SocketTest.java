package test;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import gui.OpenButtons;
import gui.OpenButtons.FileTypes;
import cryptography.algorithms.ContactInfo;
import cryptography.algorithms.SocketClient;

public class SocketTest extends Thread {

	public static void main(String[] args) throws InterruptedException, IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, NoSuchProviderException, InvalidKeySpecException, IllegalBlockSizeException, BadPaddingException {
		ContactInfo Gianni = new ContactInfo("Gianni", "localhost");
	//	SocketClient client = new SocketClient(Gianni, new OpenButtons().FileChooser(FileTypes.GENERIC_FILE));
		SocketClient client = new SocketClient(Gianni, "Supporre che la cacca ");
		client.start();
	}

}
