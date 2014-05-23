package cryptography.algorithms;
/**
 * @author Filippo Vimini
 * Created 05/05/2014
 */
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

public class SocketClient extends Thread{

	ByteArrayInputStream byteArrayIn = null;
	BufferedOutputStream outStream = null;
	AES aesEncryptor = null;
	Socket client = null;
	//Define a port
	byte[] clientNameByte;
	ContactInfo contact;
	//Define a host server
	String host;
	String clientName = "UserClientDefault";

	
	public SocketClient(ContactInfo contact, File file) throws InterruptedException, IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, NoSuchProviderException, InvalidKeySpecException, IllegalBlockSizeException, BadPaddingException{
		//Update contact info for text area
		this.contact = contact;
		//Get host
		this.host = contact.getHost();
		//Put name of Client on byte[] for send through socket
		this.clientNameByte = this.clientName.getBytes();
		//Put name of file on byte[] for send through socket
		byte[] fileName = file.getName().getBytes();       
		//Put file on byte[] for send through socket
		byte[] fileArray = new TypeConverter().fileToByte(file);
		//Method that send file to Server
		this.sendByteArray(clientNameByte, fileName, fileArray);
		//this.closeConnection();				
		//try
		//Close existing connection
		client.close();
	}
	
	public SocketClient(ContactInfo contact, String text) throws InterruptedException, IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, NoSuchProviderException, InvalidKeySpecException, IllegalBlockSizeException, BadPaddingException{
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
		this.sendByteArray(clientNameByte, stringCheck, fileArray);
		//this.closeConnection();				
		//Try
		//Close existing connection
		client.close();
	}
	
	private void sendByteArray( byte[] clientName, byte[] fileName, byte[] fileArray) throws InterruptedException, IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, NoSuchProviderException, InvalidKeySpecException, IllegalBlockSizeException, BadPaddingException{
		//Start counting of time to leave package
		long startTime = System.currentTimeMillis();
		//Client start to send package
		System.out.println("Client initialized...:");
		//Receive the Public key from server and use it to encrypt byte[] to send
		sendAesKey(keyExchange()); //Rivoluzione AppleSucks!
    	sendSequence(clientName);
		sendSequence(fileName);
		sendSequence(fileArray);
		//sendSequence(encryptor.encode(new ByteArraI, output););
		long seconds = (System.currentTimeMillis() - startTime) / 1000;
		System.out.println("File Send to: " + contact.getName() + "\nTransfert time: " + seconds + "s");
		System.out.println("Connection close...:");
	}
	
	private byte[] keyExchange(){
		
		try {
			//Get connection with server by creating new socket
			getConnection();
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
	private void sendAesKey(byte[] sendByte) throws IOException{
		//Get connection with server by creating new socket
		getConnection();
//		System.out.println("AES key to server: ");
		//byteArrayStamp(sendByte);
		//Initialize buffer with byte[] to read and buffer with Socket
		byteArrayIn = new ByteArrayInputStream(sendByte);
		outStream = new BufferedOutputStream(client.getOutputStream());
		//Start write byte from buffer in to socket
		int transfertElement ;
		try {
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
	}
	
	private void sendSequence(byte[] sendByte) throws IOException, InvalidKeyException, NullPointerException{
		//Get connection with server by creating new socket
		getConnection();
		//byteArrayIn = new ByteArrayInputStream(sendByte);
		//Initialize Buffer Out with socket
		outStream = new BufferedOutputStream(client.getOutputStream());
		ByteArrayOutputStream encryptedByteArraybuffer = new ByteArrayOutputStream();
		ByteArrayOutputStream lopo = new ByteArrayOutputStream();
		byteArrayIn = new ByteArrayInputStream(sendByte);
		//Crypting file with AES key
		this.aesEncryptor.encode(byteArrayIn, encryptedByteArraybuffer);
		byteArrayIn = null;
		byteArrayIn = new ByteArrayInputStream(encryptedByteArraybuffer.toByteArray());
		//Put Encrypted data on buffer In for send to Server
//		byteArrayIn.read(encryptedByteArraybuffer.toByteArray());
		int transfertElement;
		try {
			while((transfertElement = byteArrayIn.read()) != -1){
				outStream.write(transfertElement);
				lopo.write(transfertElement);
				outStream.flush();
			}
	//		byteArrayStamp(lopo.toByteArray());
			//Close Buffer
			closeBuffer();
		} catch (IOException e) {
				e.printStackTrace();
		}
	}
	
	private void getConnection(){
		try {
			int port = 19999;
			//New InetAddress from host passed to client
			InetAddress address = InetAddress.getByName(host);
			//Initialize new Socket client
			client = new Socket(address, port);
			//client.connect(arg0);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
			
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
