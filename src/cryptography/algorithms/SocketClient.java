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
	ByteArrayOutputStream out = null;;
	BufferedOutputStream outStream = null;
	BufferedInputStream inStream = null;
	AES aesEncryptor = null;
	Socket connection = null;
	//Define a port
	int port = 19999;
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
		connection.close();
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
		connection.close();
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
	
	@SuppressWarnings("finally")
	private byte[] keyExchange(){
		
		try {
			//Get connection with server by creating new socket
			getConnection();
			System.out.println("Get connection with server...");
			//Initialize new Buffer out for write
			this.out  = new ByteArrayOutputStream();
	//		inStream = new BufferedInputStream(connection.getInputStream());
			 DataInputStream in = new DataInputStream(connection.getInputStream());
			//Start to receive public key from server
	/*		int i;
	    	while ( (i = inStream.read()) != -1) {
	            this.out.write(i);
	        }		*/
			byte[] keyBytes = new byte[in.readInt()];
			in.readFully(keyBytes);
			System.out.println("Server's Public key received");
	    	//byte[] bytePublicServerKey = out.toByteArray();
	    	//Save Server's public key
	//    	PublicKey publicServerKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(out.toByteArray()));
	    	PublicKey publicServerKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(keyBytes));
			//new RSA for encrypt file
	    	RSA aesKeyEncryptor = new RSA();
	    	//Set Server's public key for encrypt
			aesKeyEncryptor.setKeyPair(publicServerKey,null);
			//initialize new AES
			aesEncryptor = new AES();
			//Generate 128k?bit key
			aesEncryptor.generateKey(128);	
			//Create new key from Public Server's key
			byte[] key = aesEncryptor.getSymmetricKeySpec().getEncoded();
			//Create new byte[] with decode key for send it on server
			byte[] aesKeyEncryted = aesKeyEncryptor.encode(key);
			//Close buffer I/O even with server
			this.closeBuffer();
			//return byte[] with hidden key
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
		}finally{
			return null;
		}
	}
	
	//Send AES key, this method don't crypt byte[], the key is just hidden by Diffie-Helman algorithm
	private void sendAesKey(byte[] sendByte) throws IOException{
		//Get connection with server by creating new socket
		getConnection();
		//Initialize buffer with byte[] to read and buffer with Socket
		byteArrayIn = new ByteArrayInputStream(sendByte);
		outStream = new BufferedOutputStream(connection.getOutputStream());
		//Start write byte from buffer in to socket
		int transfertElement ;
		try {
			while((transfertElement = byteArrayIn.read()) != -1){
				outStream.write(transfertElement);
				outStream.flush();
			}
			System.out.println("AES key sent to Server...");
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
		outStream = new BufferedOutputStream(connection.getOutputStream());
		ByteArrayOutputStream encryptedByteArraybuffer = new ByteArrayOutputStream();
		//Crypting file with AES key
		this.aesEncryptor.encode(byteArrayIn, encryptedByteArraybuffer);
		//Put Encrypted data on buffer In for send to Server
		byteArrayIn.read(encryptedByteArraybuffer.toByteArray());
		int transfertElement;
		try {
			while((transfertElement = byteArrayIn.read()) != -1){
				outStream.write(transfertElement);
				outStream.flush();
			}
			//Close Buffer
			closeBuffer();
		} catch (IOException e) {
				e.printStackTrace();
		}
	}
	
	private void getConnection(){
		try {
			//New InetAddress from host passed to client
			InetAddress address = InetAddress.getByName(host);
			//Initialize new Socket
			connection = new Socket(address, port);
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
}
