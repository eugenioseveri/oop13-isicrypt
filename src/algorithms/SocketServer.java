package algorithms;
/**
 * @author Filippo Vimini
 * Created 05/05/2014
 */
import gui.controllers.IFileExchangeViewObserver;
import gui.views.OpenButtons;
import gui.views.OpenButtons.FileTypes;

import java.net.*;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.io.*;

import javax.crypto.spec.SecretKeySpec;
import javax.swing.JOptionPane;

import org.apache.commons.io.FilenameUtils;

public class SocketServer extends Thread{
	//Class's field initialization
	private Socket connection;	
	private InputStream inStream = null;
	private OutputStream outStream = null;
	private ByteArrayOutputStream out = null;
	private AES aesEncryptor = null;
	private RSA aesKeyEncryptor = null;
	private String client = null;
	private static boolean onLine = true;
	private byte[] clientName = null;
	private byte[] nameFile = null;
	private byte[] fileArray = null;
	//Server creation
	private static ServerSocket  server;

	private IFileExchangeViewObserver controller;

	public void attacFileExchangeViewObserve(IFileExchangeViewObserver controller){
		this.controller = controller;
	}
	
	public void run(){
		final int port = 19999;
		try {
			server = new ServerSocket(port);
		}catch(IOException e){
			controller.threadErrorThrow(e);
		}
		try{
			//Start to accept connections.
			while(onLine){
				connection = server.accept();
				receiveFile();
			}
			//Mask error when close the frame
		}catch(IOException e){}
		try{
			//close connection 
			connection.close();
		} 
		//Error catch when you close FileExchange Frame because it force close the connection, but it does't give problem
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
	    	//Send Public key to client for crypt data.
	    try {
			keyExchange();
			//Receive AES key for decrypt file
		   	receiveAesKey();
		   	//update progressBar
		   	controller.setProgressbar(10);
			//Start to receive the file
		   	clientName = receiveSequence();
			nameFile = receiveSequence();
		   	controller.setProgressbar(20);
			fileArray = receiveSequence();
		   	controller.setProgressbar(60);
			//Send Exception to the controller because i must hide IOExeption in the run method
		} catch (GeneralSecurityException e) {
			controller.threadErrorThrow(e);
		} catch (IOException e) {
			controller.threadErrorThrow(e);
		}
	    controller.setProgressbar(70);
		//Update Client name
		this.client = TypeConverter.byteArrayToString(clientName);
		new TypeConverter();
		//Get name of file for check the type.
		String fileName = TypeConverter.byteArrayToString(nameFile);
	    controller.setProgressbar(100);
		//Text or file control
		if(fileName.equals("string")) {
			this.stringChatDetector(fileArray);
		}
		else{
			controller.fileAppendServer(fileName, client);
			if(JOptionPane.showConfirmDialog(null, "Download the File?", "choose one", JOptionPane.YES_NO_OPTION) == 0){
				//Select directory where save file
				File directory = new OpenButtons().fileChooser(FileTypes.DIRECTORY);
				if(directory!=null){
					//Control if the file is compressed
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
				else controller.textAppendServer("File Discarded", client);
			}
			else controller.textAppendServer("File Discarded", client);
		}
	}	
	/**
	 * TODO EUGE
	 * @throws GeneralSecurityException
	 * @throws IOException
	 */
	private void keyExchange() throws GeneralSecurityException, IOException{
		try {
			aesKeyEncryptor = new RSA();
			// Generate new 2048 k?bit key
			aesKeyEncryptor.generateKeyPair(2048);
			//New Buffer for byte[] that contain the key
			ByteArrayInputStream byteArrayIn = new ByteArrayInputStream(aesKeyEncryptor.getPublicKey().getEncoded());
			//Create a buffer for send the public key through Socket
			outStream = new BufferedOutputStream(connection.getOutputStream());
			//Read key from Buffer in and write to buffer on Socket
			int transfertElement ;
			while((transfertElement = byteArrayIn.read()) != -1){
				outStream.write(transfertElement);
				outStream.flush();
			}
			//Closing Buffer in
			byteArrayIn.close();
			connection.close();
		} catch (InvalidKeyException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		}
	}
	
	/**
	 * Receive AES key whereby the server will decrypt the byte[] sended by client
	 * 
	 * @throws IOException
	 * @throws GeneralSecurityException
	 */
	private void receiveAesKey() throws IOException, GeneralSecurityException{
		int i;
		try {	
			connection = server.accept();
			//Initialize new Buffer out
			this.out = new ByteArrayOutputStream();
			//Buffer i connected with socket
			inStream = new BufferedInputStream(connection.getInputStream());
	    	while ( (i = inStream.read()) != -1) {
	            this.out.write(i);
	        }
	    	byte[] aesKeyDecrypted = aesKeyEncryptor.decode(out.toByteArray());
	    	aesEncryptor = new AES();
	    	this.aesEncryptor.setSymmetricKeySpec(new SecretKeySpec(aesKeyDecrypted, "AES"));
	    	//Close Buffer in/out connection
	    	connection.close();
		} catch (IOException e) {
			throw e;
		}
	}
	
	/**
	 * TODO EUGE
	 * @return byte[]		
	 * @throws IOException
	 * @throws InvalidKeyException
	 */
	private byte[] receiveSequence() throws IOException, InvalidKeyException{
		int i;
		try {	
			connection = server.accept();
			this.out = new ByteArrayOutputStream();
			inStream = new BufferedInputStream(connection.getInputStream());
	    	while ( (i = inStream.read()) != -1) {
	            this.out.write(i);
	        }
	    	//closeConnection();
	    	ByteArrayOutputStream byteArrayOutBuffer = new ByteArrayOutputStream();
	    	//decode AES key 
	    	//EUGE
	    	aesEncryptor.decode(new ByteArrayInputStream(out.toByteArray()), byteArrayOutBuffer);
	    	connection.close();
	    	return byteArrayOutBuffer.toByteArray();	
		} catch (SocketTimeoutException e){
			throw e;	
		} catch (IOException e) {
			throw e;
		}
	}
	
	/**
	 * Convert input byte[] in String, and append text on JTextArea
	 * 
	 * @param stringByte		byte[] to be converted
	 */
	private void stringChatDetector(byte[] stringByte){
		String append = TypeConverter.byteArrayToString(stringByte);
		controller.textAppendServer(append, client);
	}
	
	//Getter for close server from control
	public static ServerSocket getSocket() {
		return server;
	}
	/**
	 * decompress the file and set right extension
	 * 
	 * @param fileName String that contain the name whit Gzip extension
	 * return String 	name of file without Gzip extension
	 */
	private String decompressFile(String fileName){
		ByteArrayInputStream arrayInStream = new ByteArrayInputStream(this.fileArray);
		ByteArrayOutputStream arrayOutStream = new ByteArrayOutputStream();
		//decompress file
		try{
			GZip.getInstance().decompress(arrayInStream, arrayOutStream);
		}catch(IOException e){
			controller.threadErrorThrow(e);
		}
		this.fileArray = arrayOutStream.toByteArray();
		//update the name without the zip extension
		return FilenameUtils.removeExtension(fileName);
	}
}