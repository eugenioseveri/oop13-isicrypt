package algorithms;
/**
 * @author Filippo Vimini
 * Created 05/05/2014
 */
import gui.controllers.FileExchangeController;
import gui.models.ContactInfo;

import java.net.*;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.awt.HeadlessException;
import java.io.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

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
	/**
	 * 
	 * @param contact
	 * @param fileStream
	 * @param name
	 * @throws InvalidKeyException
	 * @throws HeadlessException
	 * @throws NoSuchAlgorithmException
	 * @throws BadPaddingException
	 * @throws NoSuchPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws InvalidKeySpecException
	 * @throws IOException
	 */
	public SocketClient(ContactInfo contact, InputStream fileStream, String name) throws InvalidKeyException, HeadlessException, NoSuchAlgorithmException, BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException, InvalidKeySpecException, IOException {
		//Update contact info for text area
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
		} catch (IOException e) {
			System.out.println("an I/O error occured: " + e);
			throw e;
		}
		//Put file on byte[] for send through socket
		byte[] fileArray = bufferArray.toByteArray();
		//Method that send file to Server
		sendByteArray(clientNameByte, fileName, fileArray);
		FileExchangeController.textApendClient(name);
		//Close existing connection
		try {
			client.close();
		} catch (IOException e) {
			System.out.println("an I/O error occured: " + e);
			throw e;
		}
	}
	/**
	 * 
	 * @param contact
	 * @param text
	 * @throws InvalidKeyException
	 * @throws HeadlessException
	 * @throws NoSuchAlgorithmException
	 * @throws BadPaddingException
	 * @throws NoSuchPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws InvalidKeySpecException
	 * @throws IOException
	 */
	public SocketClient(ContactInfo contact, String text) throws InvalidKeyException, HeadlessException, NoSuchAlgorithmException, BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException, InvalidKeySpecException, IOException{
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
		sendByteArray(clientNameByte, stringCheck, fileArray);
		FileExchangeController.textApendClient(text);
		//Close existing connection
		try {
			client.close();
		} catch (IOException e) {
			System.out.println("an I/O error occured: " + e);
			throw e;
		}
	}
	/**
	 * 
	 * @param clientName
	 * @param fileName
	 * @param fileArray
	 * @return
	 * @throws IOException
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws BadPaddingException
	 * @throws NoSuchPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws InvalidKeySpecException
	 */
	private void sendByteArray( byte[] clientName, byte[] fileName, byte[] fileArray) throws IOException, InvalidKeyException, NoSuchAlgorithmException, BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException, InvalidKeySpecException{
		//Start counting of time to leave package
		long startTime = System.currentTimeMillis();
		//Client start to send package
		System.out.println("Client initialized...:");
		//Receive the Public key from server and use it to encrypt byte[] to send
		sendAesKey(keyExchange());
    	sendSequence(clientName);
		sendSequence(fileName);
		sendSequence(fileArray);
		//sendSequence(encryptor.encode(new ByteArraI, output););
		long seconds = (System.currentTimeMillis() - startTime) / 1000;
		System.out.println("File Send to: " + contact.getName() + "\nTransfert time: " + seconds + "s");
		System.out.println("Connection close...:");
	}
	/**
	 * 
	 * @return
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws BadPaddingException
	 * @throws NoSuchPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws InvalidKeySpecException
	 * @throws IOException
	 */
	private byte[] keyExchange() 
			throws InvalidKeyException, NoSuchAlgorithmException, BadPaddingException, 
			NoSuchPaddingException, IllegalBlockSizeException, InvalidKeySpecException, IOException{
		try {
			//Get connection with server by creating new socket
			getConnection();
			System.out.println("Get connection with server...");
			//Initialize new Buffer out for write
			ByteArrayOutputStream out  = new ByteArrayOutputStream();
			BufferedInputStream inStream = new BufferedInputStream(client.getInputStream());		
			//Start to receive public key from server
			int i;
	    	while ( (i = inStream.read()) != -1) {
	            out.write(i);
	        }		
	    	//Save Server's public key
	    	PublicKey publicServerKey = KeyFactory.getInstance("RSA").
	    			generatePublic(new X509EncodedKeySpec(out.toByteArray()));
			//new RSA for encrypt AES key
	    	RSA aesKeyEncryptor = new RSA();
	    	//Set Server's public key for encrypt
			aesKeyEncryptor.setKeyPair(publicServerKey,null);
			//initialize new AES
			aesEncryptor = new AES();
			//Generate 128bit key
			aesEncryptor.generateKey(128);
			//Create new key from Public Server's key
			byte[] key = aesEncryptor.getSymmetricKeySpec().getEncoded();
			//Create new byte[] with decode key for send it on server
			byte[] aesKeyEncryted = aesKeyEncryptor.encode(key);
			return aesKeyEncryted;
			
		} catch (InvalidKeyException e) {
			System.out.println("Byte[] encryption error occured: " + e);
			throw e;
		} catch (IOException e) {
			System.out.println("an I/O error occured: " + e);
			throw e;
		} catch (InvalidKeySpecException e) {
			System.out.println("Generate public key error occured: " + e);
			throw e;
		} catch (NoSuchAlgorithmException e) {
			System.out.println("RSA initializzation error occured: " + e);
			throw e;
		}
	}
	/**
	 * 
	 * @param sendByte
	 * @return
	 * @throws IOException
	 */
	//Send AES key, this method don't crypt byte[], the key is just hidden by Diffie-Helman algorithm
	private boolean sendAesKey(byte[] sendByte) throws IOException{
		//Get connection with server by creating new socket
		if(sendByte == null)return false;
		getConnection();
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
			//Close Buffer I/O
			closeBuffer();
		} catch (IOException e) {
			System.out.println("an I/O error occured: " + e);
			throw e;
		}
		return true;
	}
	/**
	 * 
	 * @param sendByte
	 * @throws IOException
	 */
	private void sendSequence(byte[] sendByte) throws IOException{
		//Get connection with server by creating new socket
		getConnection();
		ByteArrayOutputStream encryptedByteArraybuffer = new ByteArrayOutputStream();
		ByteArrayOutputStream lopo = new ByteArrayOutputStream();
		byteArrayIn = new ByteArrayInputStream(sendByte);
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
			//Close Buffer
			closeBuffer();
		} catch (IOException e) {
			System.out.println("an I/O error occured: " + e);
			throw e;
		}
	}
	/**
	 * 
	 * @throws IOException
	 */
	private void getConnection() throws IOException{
		try {
			int port = 19999;
			//New InetAddress from host passed to client
			InetAddress address = InetAddress.getByName(host);
			//Initialize new Socket client
			client = new Socket(address, port);
		} catch (UnknownHostException e) {
			System.out.println(" no IP address for the host could be found: " + e);
			throw e;
		} catch (IOException e) {
			System.out.println("an I/O error occured: " + e);
			throw e;
		}
	}
	/**
	 * 
	 * @throws IOException
	 */
	private void closeBuffer() throws IOException{
		try {
			outStream.close();
			byteArrayIn.close();
		//	connection.close();
		} catch (IOException e) {
			System.out.println("an I/O error occured: " + e);
			throw e;		
		}
	}
	/**
	 * 
	 * @param name
	 */
	public void setClientName(String name){
		if(name.length() < 17)this.clientName = name;
	}
}
