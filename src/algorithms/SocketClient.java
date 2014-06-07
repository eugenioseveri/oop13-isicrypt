package algorithms;
/**
 * @author Filippo Vimini
 * Created 05/05/2014
 */
import gui.controllers.IFileExchangeViewObserver;
import gui.models.ContactInfo;

import java.net.*;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.io.*;

public class SocketClient {
	//initialization of class field
	private ByteArrayInputStream byteArrayIn = null;
	private BufferedOutputStream outStream = null;
	private AES aesEncryptor = null;
	private Socket client = null;
	private String host = null;
	private String clientName = "UserClientDefault";
	
	private IFileExchangeViewObserver controller;

	private void attacFileExchangeViewObserve(IFileExchangeViewObserver controller){
		this.controller = controller;
	}
	/**
	 * This constructor is used for send a file to a server.
	 * 
	 * @param contact						A ContactInfo object that contain the name and the address of the server
	 * @param fileStream					InputStream that contain the file that will be sent at the server
	 * @param name							The name of the file that will be sent at the server	
	 * @throws InvalidKeyException			Problem with the generation of AES key
	 * @throws NoSuchAlgorithmException		if no Provider supports a KeyFactorySpi implementation for the specified algorithm.
	 * @throws InvalidKeySpecException		if the given key specification is inappropriate for this key factory to produce a public key.
	 * @throws IOException					General I/O problem
	 */
	
	public SocketClient(ContactInfo contact, InputStream fileStream, String name, IFileExchangeViewObserver controllerObserver) throws InvalidKeyException, NoSuchAlgorithmException,
	InvalidKeySpecException, IOException {
		attacFileExchangeViewObserve(controllerObserver);
		byte[] clientNameByte;
		//set Host
		this.host = contact.getHost();
		//Put name of Client on byte[] for send through socket
		clientNameByte = this.clientName.getBytes();
		//Put name of file on byte[] for send through socket
		byte[] fileName = name.getBytes();       
		//InputStream to byte[]
		ByteArrayOutputStream bufferArray = new ByteArrayOutputStream();
		int i;
		while((i = fileStream.read())!=-1){
			bufferArray.write(i);
		}
		//Put file on byte[] for send through socket
		byte[] fileArray = bufferArray.toByteArray();
		//Method that send file to Server
		sendByteArray(clientNameByte, fileName, fileArray);
		controller.textApendClient(name);
		//Close existing connection
		client.close();
	
	}
	/**
	 * This constructor is used for send a text to a server.
	 * 
	 * @param contact						A ContactInfo object that contain the name and the address of the server
	 * @param text							String that will be encrypted and sent to the Server
	 * @throws InvalidKeyException			Problem with the generation of AES key
	 * @throws NoSuchAlgorithmException		if no Provider supports a KeyFactorySpi implementation for the specified algorithm.
	 * @throws InvalidKeySpecException		if the given key specification is inappropriate for this key factory to produce a public key.
	 * @throws IOException					General I/O problem
	 */
	public SocketClient(ContactInfo contact, String text, IFileExchangeViewObserver controllerObserver) throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, IOException{
		attacFileExchangeViewObserve(controllerObserver);
		byte[] clientNameByte;
		//initialize a string that will be used from server to check that the follow byte[] is a text message, not a file
		String isString = "string";
		//Put String on byte[] for send through socket
		byte[] stringCheck = isString.getBytes();
		//Get host
		this.host = contact.getHost();
		//Put name of Client on byte[] for send through socket
		clientNameByte = this.clientName.getBytes();
		//Put text message on byte[] for send through socket
		byte[] fileArray = text.getBytes();
		//Method that send text to Server
		sendByteArray(clientNameByte, stringCheck, fileArray);
		controller.textApendClient(text);
		//Close existing connection
		client.close();


	}
	/**
	 * Sequence that sent file and information at the server, first a byte[] that contain the name of client, 
	 * then a byte[] that contain the name of the file or String if ir's a 
	 * text and last the a byte[] that contain the file or text 	
	 * 
	 * @param clientName
	 * @param fileName
	 * @param fileArray
	 */
	private void sendByteArray( byte[] clientName, byte[] fileName, byte[] fileArray) throws IOException, InvalidKeyException,
																	NoSuchAlgorithmException, InvalidKeySpecException{
		//Receive the Public key from server and use it to encrypt byte[] to send
		sendAesKey(keyExchange());
		//Sequence that sent file and information at the server
    	sendSequence(clientName);	
		sendSequence(fileName);
		sendSequence(fileArray);
	}
	/**
	 * This method take a Public key from server and create a new AES key for encrypt the file.
	 * 
	 * @return aesKeyEncryted				 byte[] that contain the encryption key 
	 */
	private byte[] keyExchange() throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, IOException{
			//Get connection with server by creating new socket
			getConnection();
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
	}
	/**
	 * Send to the Server the AES key created from the public key of Server.
	 * 
	 * @param sendByte   byte[] that contain the AES key 
	 */
	//Send AES key, this method don't encrypt byte[], the key is just hidden by Diffie-Helman algorithm
	private void sendAesKey(byte[] sendByte) throws IOException{
		//Get connection with server by creating new socket
		getConnection();
		//Initialize buffer with byte[] to read and buffer with Socket
		byteArrayIn = new ByteArrayInputStream(sendByte);
		//Start write byte from buffer in to socket
		int transfertElement ;
		outStream = new BufferedOutputStream(client.getOutputStream());
		while((transfertElement = byteArrayIn.read()) != -1){
			outStream.write(transfertElement);
			outStream.flush();
		}
		//Close Buffer I/O
		closeBuffer();
	}
	/**
	 * Sent the byte[] to the server.
	 * 
	 * @param sendByte		byte[] that will be sent to the server
	 */
	private void sendSequence(byte[] sendByte) throws IOException{
		//Get connection with server by creating new socket
		getConnection();
		ByteArrayOutputStream encryptedByteArraybuffer = new ByteArrayOutputStream();
		ByteArrayOutputStream lopo = new ByteArrayOutputStream();
		byteArrayIn = new ByteArrayInputStream(sendByte);
		int transfertElement;
		//Crypting file with AES key
		this.aesEncryptor.encode(byteArrayIn, encryptedByteArraybuffer);
		byteArrayIn = new ByteArrayInputStream(encryptedByteArraybuffer.toByteArray());
		outStream = new BufferedOutputStream(client.getOutputStream());
		//Write byte[] to the buffer connected with server
		while((transfertElement = byteArrayIn.read()) != -1){
			outStream.write(transfertElement);
			lopo.write(transfertElement);
			outStream.flush();
		}
		//Close Buffer
		closeBuffer();
	}
	/**
	 * Initialize InetAddress and Socket.
	 * 
	 * @throws IOException    I/O problem when try to create a Socket or InetAddress
	 */
	private void getConnection() throws IOException{
		int port = 19999;
		//New InetAddress from host passed to client
		InetAddress address = InetAddress.getByName(host);
		//Initialize new Socket client
		client = new Socket(address, port);
	}
	/**
	 * Close buffer connection
	 * 
	 * @throws IOException		I/O problem when try to close buffer 
	 */
	private void closeBuffer() throws IOException{
		outStream.close();
		byteArrayIn.close();
	}
}
