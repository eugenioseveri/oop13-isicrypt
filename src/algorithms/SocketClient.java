package algorithms;
/**
 * @author Filippo Vimini
 * Created 05/05/2014
 */
import gui.controllers.FileExchangeController;
import gui.models.ContactInfo;
import gui.views.FileExchangeView;

import java.net.*;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.io.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.JOptionPane;

public class SocketClient extends Thread{

	ByteArrayInputStream byteArrayIn = null;
	BufferedOutputStream outStream = null;
	AES aesEncryptor = null;
	//Client used only for send name at Server
	Socket client = null;
	//Define a port
	byte[] clientNameByte;
	ContactInfo contact;
	//Define a host server
	String host;
	String clientName = "UserClientDefault";

	public SocketClient(ContactInfo contact, InputStream fileStream, String name) {
		//Update contact info for text area
	//	this.contact = contact;
		//Get host
		this.contact = contact;
		//set Host
		this.host = contact.getHost();
		//Put name of Client on byte[] for send through socket
		this.clientNameByte = this.clientName.getBytes();
		//Put name of file on byte[] for send through socket
		byte[] fileName = name.getBytes();       
		//InputStream to byte[]
		ByteArrayOutputStream bufferArray = new ByteArrayOutputStream();
		int i;
		try {
			while((i = fileStream.read())!=-1){
				bufferArray.write(i);
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		//Put file on byte[] for send through socket
		byte[] fileArray = bufferArray.toByteArray();
		//Method that send file to Server
		if(!sendByteArray(clientNameByte, fileName, fileArray))
			JOptionPane.showMessageDialog(FileExchangeView.getDialog(), "Contact is not Online");
		else {
			FileExchangeController.textApendClient(name);
			//Close existing connection
			try {
				client.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public SocketClient(ContactInfo contact, String text){
		//initialize a string that will be used from server to check that the follow byte[] is a text message, not a file
		String lopo = "string";
		//Put String on byte[] for send through socket
		byte[] stringCheck = lopo.getBytes();
		//Update contact info for text area
		this.contact = contact;
		//Get host
		this.host = contact.getHost();
		//Put name of Client on byte[] for send through socket
		this.clientNameByte = this.clientName.getBytes();
		//Put text message on byte[] for send through socket
		byte[] fileArray = text.getBytes();
		//Method that send text to Server
		if(!sendByteArray(clientNameByte, stringCheck, fileArray))
			JOptionPane.showMessageDialog(FileExchangeView.getDialog(), "Contact is not Online");
		else {
			FileExchangeController.textApendClient(text);
			//Close existing connection
			try {
				client.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private boolean sendByteArray( byte[] clientName, byte[] fileName, byte[] fileArray){
		//Start counting of time to leave package
		long startTime = System.currentTimeMillis();
		//Client start to send package
		System.out.println("Client initialized...:");
		//Receive the Public key from server and use it to encrypt byte[] to send
		if(!sendAesKey(keyExchange()))return false;
    	sendSequence(clientName);
		sendSequence(fileName);
		sendSequence(fileArray);
		//sendSequence(encryptor.encode(new ByteArraI, output););
		long seconds = (System.currentTimeMillis() - startTime) / 1000;
		System.out.println("File Send to: " + contact.getName() + "\nTransfert time: " + seconds + "s");
		System.out.println("Connection close...:");
		return true;
	}
	
	private byte[] keyExchange(){
		
		try {
			//Get connection with server by creating new socket
			if(!getConnection())return null;
			System.out.println("Get connection with server...");
			//Initialize new Buffer out for write
			ByteArrayOutputStream out  = new ByteArrayOutputStream();
			BufferedInputStream inStream = new BufferedInputStream(client.getInputStream());		
	//		 DataInputStream in = new DataInputStream(connection.getInputStream());
			//Start to receive public key from server
			int i;
	    	while ( (i = inStream.read()) != -1) {
	            out.write(i);
	        }		
	    	//TRY
	/*		byte[] keyBytes = new byte[in.readInt()];
			in.readFully(keyBytes);*/
	//		System.out.println("Server's Public key received");
	    	//byte[] bytePublicServerKey = out.toByteArray();
	    	//Save Server's public key
	//		System.out.print("Server's public key: ");
	//		byteArrayStamp(out.toByteArray());
	    	PublicKey publicServerKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(out.toByteArray()));
	//    	PublicKey publicServerKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(keyBytes));
			//new RSA for encrypt AES key
	    	RSA aesKeyEncryptor = new RSA();
	    	//Set Server's public key for encrypt
			aesKeyEncryptor.setKeyPair(publicServerKey,null);
			//initialize new AES
			aesEncryptor = new AES();
			//Generate 128bit key
			aesEncryptor.generateKey(128);
	//		System.out.println("generateKey.length: "+aesEncryptor.getSymmetricKeySpec().getEncoded().length);
			//Create new key from Public Server's key
			byte[] key = aesEncryptor.getSymmetricKeySpec().getEncoded();
	//		byteArrayStamp(key);
			//Create new byte[] with decode key for send it on server
			byte[] aesKeyEncryted = aesKeyEncryptor.encode(key);
	//		byteArrayStamp(aesKeyEncryted);
			//Close buffer I/O even with server
			//return byte[] with hidden key
	//		System.out.println("keyExchange completed");
			//closeBuffer();
			return aesKeyEncryted;
			
		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchProviderException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//Send AES key, this method don't crypt byte[], the key is just hidden by Diffie-Helman algorithm
	private boolean sendAesKey(byte[] sendByte){
		//Get connection with server by creating new socket
		if(sendByte == null)return false;
		getConnection();
//		System.out.println("AES key to server: ");
		//byteArrayStamp(sendByte);
		//Initialize buffer with byte[] to read and buffer with Socket
		byteArrayIn = new ByteArrayInputStream(sendByte);
		//Start write byte from buffer in to socket
		int transfertElement ;
		try {
			outStream = new BufferedOutputStream(client.getOutputStream());
			while((transfertElement = byteArrayIn.read()) != -1){
				outStream.write(transfertElement);
				outStream.flush();
			}
//			System.out.println("AES key sent to Server...");
			//Close Buffer I/O
			closeBuffer();
		} catch (IOException e) {
				e.printStackTrace();
		}
		return true;
	}
	
	private void sendSequence(byte[] sendByte){
		//Get connection with server by creating new socket
		getConnection();
		//byteArrayIn = new ByteArrayInputStream(sendByte);
		//Initialize Buffer Out with socket
		ByteArrayOutputStream encryptedByteArraybuffer = new ByteArrayOutputStream();
		ByteArrayOutputStream lopo = new ByteArrayOutputStream();
		byteArrayIn = new ByteArrayInputStream(sendByte);
		
		//Put Encrypted data on buffer In for send to Server
//		byteArrayIn.read(encryptedByteArraybuffer.toByteArray());
		int transfertElement;
		try {
			//Crypting file with AES key
			this.aesEncryptor.encode(byteArrayIn, encryptedByteArraybuffer);
			byteArrayIn = new ByteArrayInputStream(encryptedByteArraybuffer.toByteArray());
			outStream = new BufferedOutputStream(client.getOutputStream());
			while((transfertElement = byteArrayIn.read()) != -1){
				outStream.write(transfertElement);
				lopo.write(transfertElement);
				outStream.flush();
			}
	//		byteArrayStamp(lopo.toByteArray());
			//Close Buffer
			closeBuffer();
		} catch (IOException | InvalidKeyException | NullPointerException e) {
				e.printStackTrace();
		}
	}
	
	private boolean getConnection(){
		try {
			int port = 19999;
			//New InetAddress from host passed to client
			InetAddress address = InetAddress.getByName(host);
			//Initialize new Socket client
			client = new Socket(address, port);
			//client.connect(arg0);
		} catch (UnknownHostException e) {
			return false;
		} catch (IOException e) {
			e.printStackTrace();
		}
			return true;
	}
	
	private void closeBuffer(){
		try {
			outStream.close();
			byteArrayIn.close();
		//	connection.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setClientName(String name){
		if(name.length() < 17)this.clientName = name;
	}
	
/*	private void byteArrayStamp(byte[] stamp){
		int i;
		System.out.println("byte[] length: " + stamp.length);
		for(i = 0; i < stamp.length; i++){
			System.out.print((char)stamp[i]);
		}
		System.out.println("");
	}	*/
}
