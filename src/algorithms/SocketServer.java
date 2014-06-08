package algorithms;
/**
 * @author Filippo Vimini
 * Created 05/05/2014
 */
import gui.controllers.IFileExchangeViewObserver;
import gui.views.OpenButtons;
import gui.views.OpenButtons.FyleTypes;

import java.net.*;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.io.*;

import javax.crypto.spec.SecretKeySpec;
import javax.swing.JOptionPane;

import org.apache.commons.io.FilenameUtils;

public class SocketServer extends Thread{
	// Fields initialization
	private final static int PORT = 19999;
	private final static int DEFAULT_RSA_KEY_SIZE = 2048;
	private Socket connection;	
	private InputStream inStream;
	private OutputStream outStream;
	private ByteArrayOutputStream out;
	private AES aesEncryptor;
	private RSA aesKeyEncryptor;
	private String client;
	private static boolean onLine = true;
	private byte[] fileArray;
	// Server creation
	private static ServerSocket  server;

	private IFileExchangeViewObserver controller;

	public void attachFileExchangeViewObserver(final IFileExchangeViewObserver controller){
		this.controller = controller;
	}
	/**
	 * Start Server in a separate thread in concurrency with GUI and wait for client that send a file
	 */
	public void run(){
		try {
			server = new ServerSocket(PORT);
		} catch(IOException e){
			controller.threadErrorThrow(e);
		}
		try {
			// Start to accept connections.
			while(onLine) {
				connection = server.accept();
				receiveFile();
			}
		} catch (IOException e) {
			// Mask error when close the frame
		}
		try {
			connection.close();
		} 
		// Error catch when you close FileExchange Frame because connection clising is forced, but it's not a problem
		catch (IOException e){
			controller.threadErrorThrow(e);
		} 	
	}
	/**
	 * Series of send ad receive for receive the file from client, check if the received byte[] is
	 * a File or text and save or append the byte[]
	 * 
	 * @throws IOException
	 * @throws GeneralSecurityException
	 */
	private void receiveFile(){
	    // Send Public key to client to encrypt data.
		byte[] clientName = null;
		byte[] nameFile = null;
	    try {
			keyExchange();
			// Receive AES key for decrypt file
		   	receiveAesKey();
		   	controller.setProgressbar(10);
			// Start to receive the file
		   	clientName = receiveSequence();
			nameFile = receiveSequence();
		   	controller.setProgressbar(20);
			fileArray = receiveSequence();
		   	controller.setProgressbar(60);
			// Send Exception to the controller because i must hide IOExeption in the run method
		} catch (GeneralSecurityException e) {
			controller.threadErrorThrow(e);
		} catch (IOException e) {
			controller.threadErrorThrow(e);
		}
	    controller.setProgressbar(70);
		// Update Client name
		this.client = TypeConverter.byteArrayToString(clientName);
		new TypeConverter();
		// Get name of file for check the type.
		String fileName = TypeConverter.byteArrayToString(nameFile);
	    controller.setProgressbar(100);
		// "Text" or "file" check
		if("string".equals(fileName)) {
			this.stringChatDetector(fileArray);
		}
		else{
			controller.fileAppendServer(fileName, client);
			if( 0 == JOptionPane.showConfirmDialog(null, "Download the File?", "choose one", JOptionPane.YES_NO_OPTION)){
				// Select directory where to save file
				final File directory = new OpenButtons().fileChooser(FyleTypes.DIRECTORY);
				if(directory == null){
					controller.textAppendServer("File Discarded", client);
				} else {
					// Control if the file is compressed
					if(FilenameUtils.getExtension(fileName).equalsIgnoreCase("Gzip")){
						fileName = decompressFile(fileName);
					}
					BufferedOutputStream byteToFile = null;
					try {
						byteToFile = new BufferedOutputStream(new FileOutputStream(directory+"/"+fileName));
						byteToFile.write(fileArray, 0, fileArray.length);
						byteToFile.close();
					} catch (IOException e) {
						controller.threadErrorThrow(e);
					}
					controller.textAppendServer("File Downloaded", client);
				}
			} else {
				controller.textAppendServer("File Discarded", client);
			}
		}
	}	
	/**
	 * Negotiates a new RSA key pair with the client.
	 * @throws IOException If an error occurs writing the stream
	 */
	private void keyExchange() throws IOException{
		aesKeyEncryptor = new RSA();
		// Generate a new DEFAULT_RSA_KEY_SIZE bits long key
		try {
			aesKeyEncryptor.generateKeyPair(DEFAULT_RSA_KEY_SIZE);
		} catch (InvalidKeyException e) {
			// This exception can not occur since DEFAULT_RSA_KEY_SIZE is built-in
		}
		// New Buffer for byte[] that contains the key
		final ByteArrayInputStream byteArrayIn = new ByteArrayInputStream(aesKeyEncryptor.getPublicKey().getEncoded());
		// Create a buffer for send the public key through Socket
		outStream = new BufferedOutputStream(connection.getOutputStream());
		// Read key from Buffer in and write to buffer on Socket
		int transfertElement ;
		while((transfertElement = byteArrayIn.read()) != -1){
			outStream.write(transfertElement);
			outStream.flush();
		}
		byteArrayIn.close();
		connection.close();
	}
	/**
	 * Receive AES key whereby the server will decrypt the byte[] sent by client
	 * 
	 * @throws IOException
	 * @throws GeneralSecurityException
	 */
	private void receiveAesKey() throws IOException, GeneralSecurityException{	
		connection = server.accept();
		// Initialize new Buffer out
		this.out = new ByteArrayOutputStream();
		// Buffer i connected with socket
		inStream = new BufferedInputStream(connection.getInputStream());
		int counter;
	    while ((counter = inStream.read()) != -1) {
	    	this.out.write(counter);
	    }
	    final byte[] aesKeyDecrypted = aesKeyEncryptor.decode(out.toByteArray());
	    aesEncryptor = new AES();
	    this.aesEncryptor.setSymmetricKeySpec(new SecretKeySpec(aesKeyDecrypted, "AES"));
	    // Close Buffer in/out connection
	    connection.close();
	}
	/**
	 * Receives a message sent by a client and decrypts it.
	 * @return byte[] The received data
	 * @throws IOException If an error occurs while reading data from network or while decrypting the data
	 * @throws InvalidKeyException If the set key is not valid for AES
	 */
	private byte[] receiveSequence() throws IOException, InvalidKeyException{
		connection = server.accept();
		this.out = new ByteArrayOutputStream();
		inStream = new BufferedInputStream(connection.getInputStream());
		int counter;
	    while ((counter = inStream.read()) != -1) {
	           this.out.write(counter);
	       }
	    final ByteArrayOutputStream byteArrayOutBuffer = new ByteArrayOutputStream();
	    // Decode data using AES
	    aesEncryptor.decode(new ByteArrayInputStream(out.toByteArray()), byteArrayOutBuffer);
	    connection.close();
	    return byteArrayOutBuffer.toByteArray();	
	}
	/**
	 * Convert input byte[] in String, and append text on JTextArea
	 * 
	 * @param stringByte		byte[] to be converted
	 */
	private void stringChatDetector(final byte[] stringByte){
		final String append = TypeConverter.byteArrayToString(stringByte);
		controller.textAppendServer(append, client);
	}	
	/**
	 * @return the server for closing it from control
	 */
	public static ServerSocket getSocket() {
		return server;
	}
	/**
	 * decompress the file and set right extension
	 * 
	 * @param fileName String that contain the name whit Gzip extension
	 * return name of file without Gzip extension
	 */
	private String decompressFile(final String fileName){
		final ByteArrayInputStream arrayInStream = new ByteArrayInputStream(this.fileArray);
		final ByteArrayOutputStream arrayOutStream = new ByteArrayOutputStream();
		// Decompress file
		try{
			GZip.getInstance().decompress(arrayInStream, arrayOutStream);
		}catch(IOException e){
			controller.threadErrorThrow(e);
		}
		this.fileArray = arrayOutStream.toByteArray();
		// Update the name without the zip extension
		return FilenameUtils.removeExtension(fileName);
	}
}