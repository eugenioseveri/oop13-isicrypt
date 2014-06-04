package algorithms;
/**
 * @author Filippo Vimini
 * Created 05/05/2014
 */
import gui.controllers.FileExchangeController;
import gui.models.OpenButtons;
import gui.models.OpenButtons.FileTypes;

import java.net.*;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.io.*;

import javax.crypto.spec.SecretKeySpec;
import javax.swing.JOptionPane;

public class SocketServer extends Thread{
	
	/*public static void main(String[] args){
		new SocketServer();
	}*/
	
	static Socket connection;	
	InputStream inStream = null;
	OutputStream outStream = null;
	ByteArrayOutputStream out = null;
	AES aesEncryptor = null;
	RSA aesKeyEncryptor = null;
	String client;
	static boolean onLine = true;

	private static ServerSocket  server;


	//Come lanciare le eccezioni a questo punto?
	public void run(){
		final int port = 19999;
		try {
			server = new ServerSocket(port);
			System.out.println("Server initialized...\nStart to accepting connections...");
			//Start to accept connections.
			while(onLine){
				connection = server.accept();
				receiveFile();
			}
			connection.close();
			System.out.println("The server if fucking closed!!!");
		} catch (IOException e) {
			//Error catched when you close FileExchange Frame because it force close the connection, but it does't give problem
			System.out.println("Force close");
		} catch (GeneralSecurityException e) {
			System.out.println("Key exchange or I/O errors occured " + e);
		}
	}

	/**
	 * 
	 * @throws IOException
	 * @throws GeneralSecurityException
	 */
	private void receiveFile() throws IOException, GeneralSecurityException{
	    try {
	    	//Send Public key to client for crypt data.
	    	keyExchange();
	    	//Receive AES key for decrypt file
	    	receiveAesKey();
			//Start to receive the file
	    	byte[] clientName = receiveSequence();
			byte[] nameFile = receiveSequence();
			byte[] fileArray = receiveSequence();			
			//Update Client name
			this.client = TypeConverter.byteArrayToString(clientName);
			System.out.println("\nConnection with: " + this.client+":");
			new TypeConverter();
			//Get name of file for check the type.
			String fileName = TypeConverter.byteArrayToString(nameFile);
			//Text or file control
			if(fileName.equals("string")) {
				this.stringChatDetector(fileArray);
			}
			else{
				System.out.println("Client file recived... \nfile name: "+fileName+"\nDownload?(yes|no)");
				FileExchangeController.fileAppendServer(fileName, client);
				if(JOptionPane.showConfirmDialog(null, "Download the File?", "choose one", JOptionPane.YES_NO_OPTION) == 0){
					//Select directory where save file
					File directory = new OpenButtons().fileChooser(FileTypes.DIRECTORY);
					if(directory!=null){
						BufferedOutputStream byteToFile = new BufferedOutputStream
								(new FileOutputStream(directory+"/"+fileName));
						byteToFile.write(fileArray, 0, fileArray.length);
						byteToFile.close();
						System.out.println("file "+fileName+" saved...");
						FileExchangeController.textAppendServer("File Downloaded", client);
					}
					else FileExchangeController.textAppendServer("File Discarded", client);
				}
				else FileExchangeController.textAppendServer("File Discarded", client);
			}
		} catch (IOException e) {
			System.out.println("an I/O error occured: " + e);
			throw e;
		}
	}
	
	/**
	 * 
	 * @throws GeneralSecurityException
	 * @throws IOException
	 */
	private void keyExchange() throws GeneralSecurityException, IOException{
		try {
			//connection =socket1.accept();
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
			System.out.println("RSA encryption error occured: " + e);
			throw e;
		} catch (IOException e) {
			System.out.println("an I/O error occured: " + e);
			throw e;
		}
	}
	
	/**
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
			System.out.println("an I/O error occured: " + e);
			throw e;
		}
	}
	
	/**
	 * 
	 * @return
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
	    	aesEncryptor.decode(new ByteArrayInputStream(out.toByteArray()), byteArrayOutBuffer);
	    	connection.close();
	    	return byteArrayOutBuffer.toByteArray();	
		} catch (SocketTimeoutException e){
			System.out.println("Socket Timeout error occured: " + e);
			throw e;	
		} catch (IOException e) {
			System.out.println("an I/O error occured: " + e);
			throw e;
		}
	}
	
	/**
	 * 
	 * @param stringByte
	 */
	private void stringChatDetector(byte[] stringByte){
		int count;
		System.out.print("	");
		for(count = 0; count < stringByte.length; count++){
			System.out.print((char)stringByte[count]);
		}
		String append = TypeConverter.byteArrayToString(stringByte);
		FileExchangeController.textAppendServer(append, client);
	}
	
	public static void closeSocket() {
		 try {
			server.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//SETTERS
	public static void setOnLine(boolean onLine) {
		SocketServer.onLine = onLine;
	}
}