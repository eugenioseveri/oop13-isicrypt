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
	private AES aesEncryptor = null;
	private Socket client = null;
	private String host = null;
	private String clientName = "UserClientDefault";
	
	private IFileExchangeViewObserver controller;

	private void attacFileExchangeViewObserve(final IFileExchangeViewObserver controller){
		this.controller = controller;
	}
	/**
	 * This constructor is used for send a file to a server.
	 * 
	 * @param contact						A ContactInfo object that contain 
	 * 										the name and the address of the server
	 * @param fileStream					InputStream that contain the file 
	 * 										that will be sent at the server
	 * @param name							The name of the file that will be sent at the server	
	 * @throws InvalidKeyException			Problem with the generation of AES key
	 * @throws NoSuchAlgorithmException		if no Provider supports a KeyFactorySpi
	 * 										 implementation for the specified algorithm.
	 * @throws InvalidKeySpecException		if the given key specification is  
	 * 										inappropriate for this key factory to produce a public key.
	 * @throws IOException					General I/O problem
	 */
	
	public SocketClient(final ContactInfo contact, final InputStream fileStream, final String name, final IFileExchangeViewObserver controllerObserver) throws InvalidKeyException, NoSuchAlgorithmException,
	InvalidKeySpecException, IOException {
		attacFileExchangeViewObserve(controllerObserver);
		byte[] clientNameByte;
		//set Host
		this.host = contact.getHost();
		//Put name of Client on byte[] for send through socket
		clientNameByte = this.clientName.getBytes();
		//Put name of file on byte[] for send through socket
		final byte[] fileName = name.getBytes();       
		//InputStream to byte[]
		final ByteArrayOutputStream bufferArray = new ByteArrayOutputStream();
		int counter;
		while((counter = fileStream.read())!=-1){
			bufferArray.write(counter);
		}
		//Put file on byte[] for send through socket
		final byte[] fileArray = bufferArray.toByteArray();
		//Method that send file to Server
		sendByteArray(clientNameByte, fileName, fileArray);
		controller.textApendClient(name);
		//Close existing connection
		client.close();
	
	}
	/**
	 * This constructor is used for send a text to a server.
	 * 
	 * @param contact						A ContactInfo object that contain the name and the 
	 * 										address of the server
	 * @param text							String that will be encrypted and sent to the Server
	 * @throws InvalidKeyException			Problem with the generation of AES key
	 * @throws NoSuchAlgorithmException		if no Provider supports a KeyFactorySpi
	 * 										implementation for the specified algorithm.
	 * @throws InvalidKeySpecException		if the given key specification is  
	 * 										inappropriate for this key factory 
	 * 										to produce a public key.
	 * @throws IOException					General I/O problem
	 */
	public SocketClient(final ContactInfo contact, final String text, final IFileExchangeViewObserver controllerObserver) throws InvalidKeyException, 
	NoSuchAlgorithmException, InvalidKeySpecException, IOException{
		attacFileExchangeViewObserve(controllerObserver);
		byte[] clientNameByte;
		/*initialize a string that will be used from server
		to check that the follow byte[] is a text message, not a file*/
		final String isString = "string";
		//Put String on byte[] for send through socket
		final byte[] stringCheck = isString.getBytes();
		//Get host
		this.host = contact.getHost();
		//Put name of Client on byte[] for send through socket
		clientNameByte = this.clientName.getBytes();
		//Put text message on byte[] for send through socket
		final byte[] fileArray = text.getBytes();
		//Method that send text to Server
		sendByteArray(clientNameByte, stringCheck, fileArray);
		controller.textApendClient(text);
		//Close existing connection
		try{
			client.close();
		}catch( IOException e){
			//Override original trace
			throw new IOException("an I/O error occurs when closing this socket");
		}
	}
	/**
	 * Sequence that sent file and information at the server, 
	 * first a byte[] that contain the name of client, 
	 * then a byte[] that contain the name of the file or String if ir's a 
	 * text and last the a byte[] that contain the file or text 	
	 * 
	 * @param clientName
	 * @param fileName
	 * @param fileArray
	 */
	private void sendByteArray( final byte[] clientName, final byte[] fileName, final byte[] fileArray) throws IOException, InvalidKeyException,
																	NoSuchAlgorithmException, InvalidKeySpecException{
		//Receive the Public key from server and use it to encrypt byte[] to send
		sendAesKey(keyExchange());
		//Sequence that sent file and information at the server
    	sendSequence(clientName);	
		sendSequence(fileName);
		sendSequence(fileArray);
	}
	/**
	 * This method take a Public key from server and 
	 * create a new AES key for encrypt the file.
	 * 
	 * @return aesKeyEncryted				 byte[] that contain the encryption key 
	 * @throws IOException 					if there is an error when try to 
	 * create the socket or the input stream,a specific Exception is catch and throw
	 */
	private byte[] keyExchange() throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, IOException{
			//Get connection with server by creating new socket
			getConnection();
			//Initialize new Buffer out for write
			BufferedInputStream inStream;
			try {
				inStream = new BufferedInputStream(client.getInputStream());
				//Override original track
			} catch (IOException e) {
				throw new IOException(e);
			}		
			//Start to receive public key from server
			final ByteArrayOutputStream out  = new ByteArrayOutputStream();
			int counter;
	    	try {
				while ( (counter = inStream.read()) != -1) {
				    out.write(counter);
				}
			} catch (IOException e) {
				throw new IOException(e);
			}		
			//new RSA for encrypt AES key
	    	final RSA aesKeyEncryptor = new RSA();
	    	//Save Server's public key
	    	try{
	    		final PublicKey publicServerKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(out.toByteArray()));
		    	//Set Server's public key for encrypt
				aesKeyEncryptor.setKeyPair(publicServerKey,null);
	    	}catch(InvalidKeySpecException e){
	    		//Override original track
	    		throw new InvalidKeySpecException("given key specification is inappropriate for this key factory");
	    	}catch(NoSuchAlgorithmException e){
	    		//Override original track
	    		throw new NoSuchAlgorithmException("no Provider supports a KeyFactorySpi implementation for the specified algorithm");
	    	}
			//initialize new AES
			aesEncryptor = new AES();
			//Generate 128bit key
			try{
				aesEncryptor.generateKey(128);
				//Create new key from Public Server's key
				final byte[] key = aesEncryptor.getSymmetricKeySpec().getEncoded();
				//Create new byte[] with decode key for send it on server
				final byte[] aesKeyEncryted = aesKeyEncryptor.encode(key);
				return aesKeyEncryted;
			}catch(InvalidKeyException e ){
	    		//Override original track
				throw new InvalidKeyException("invalid key settings");
			}
	}
	/**
	 * Send to the Server the AES key created from the public key of Server.
	 * 
	 * @param sendByte   byte[] that contain the AES key 
	 */
	/*Send AES key, this method don't encrypt byte[],
	 *  the key is just hidden by Diffie-Helman algorithm*/
	private void sendAesKey(final byte[] sendByte) throws IOException{
		//Get connection with server by creating new socket
		getConnection();
		//Initialize buffer with byte[] to read and buffer with Socket
		final ByteArrayInputStream byteArrayIn = new ByteArrayInputStream(sendByte);
		//Start write byte from buffer in to socket
		BufferedOutputStream outStream = null;
		int transfertElement ;
		try{
			outStream = new BufferedOutputStream(client.getOutputStream());
			while((transfertElement = byteArrayIn.read()) != -1){
				outStream.write(transfertElement);
				outStream.flush();
			}
		}catch(IOException e){
    		//Override original track
			throw new IOException("an I/O error occurs when creating the output stream or if the socket is not connected");
		}
		//Close Buffer I/O
		outStream.close();
		byteArrayIn.close();
	}
	/**
	 * Sent the byte[] to the server.
	 * 
	 * @param sendByte		byte[] that will be sent to the server
	 * @throws IOException  
	 * @throws InvalidKeyException  If a key has not been set or is not valid
	 */
	private void sendSequence(final byte[] sendByte) throws IOException, InvalidKeyException{
		//Get connection with server by creating new socket
		getConnection();
		final ByteArrayOutputStream encryptedByteArraybuffer = new ByteArrayOutputStream();
		final ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
		ByteArrayInputStream byteArrayIn = new ByteArrayInputStream(sendByte);
		//Crypting file with AES key
		try{
			this.aesEncryptor.encode(byteArrayIn, encryptedByteArraybuffer);

		}catch(InvalidKeyException e){
			throw new InvalidKeyException("key has not been set or is not valid");
		}
		byteArrayIn = new ByteArrayInputStream(encryptedByteArraybuffer.toByteArray());
		BufferedOutputStream outStream = null;
		try{
			outStream = new BufferedOutputStream(client.getOutputStream());
			//Write byte[] to the buffer connected with server
			int transfertElement;
			while((transfertElement = byteArrayIn.read()) != -1){
				outStream.write(transfertElement);
				byteArrayOut.write(transfertElement);
				outStream.flush();
			}
		}catch(IOException e){
			//Override original track
			throw new IOException("an I/O error occurs when creating the output stream or if the socket is not connected");
		}
		//Close Buffer
		outStream.close();
		byteArrayIn.close();
	}
	/**
	 * Initialize InetAddress and Socket.
	 * 
	 * @throws IOException    I/O problem when try to create a Socket or InetAddress
	 */
	private void getConnection() throws IOException{
		final int port = 19999;
		//New InetAddress from host passed to client
		try {
			final InetAddress address = InetAddress.getByName(host);
			client = new Socket(address, port);
		} catch (UnknownHostException e){
			//Override original track
			throw new UnknownHostException("no IP address for the host could be found");
		} catch (IOException e) {
			//Override original track
			throw new IOException("an I/O error occurs when creating the socket");
		}
	}
}
