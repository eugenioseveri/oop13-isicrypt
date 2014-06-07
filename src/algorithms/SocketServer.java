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
	//Class's field initialization
	private Socket connection;	
	private InputStream inStream = null;
	private OutputStream outStream = null;
	private ByteArrayOutputStream out = null;
	private AES aesEncryptor = null;
	private RSA aesKeyEncryptor = null;
	private String client = null;
	static boolean onLine = true;
	//Server creation
	private static ServerSocket  server;

	public void run(){
		final int port = 19999;
		try {
			server = new ServerSocket(port);
			//Start to accept connections.
			while(onLine){
				connection = server.accept();
				receiveFile();
			}
			//close connection 
			connection.close();
		} 
		//Error catch when you close FileExchange Frame because it force close the connection, but it does't give problem
		catch (IOException e){} 
		catch (GeneralSecurityException e){}
	}

	/**
	 * Series of send ad receive for receive the file from client, check if the received byte[] is
	 * a File or text and save or append the byte[]
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
	    	FileExchangeController.setProgressbar(10);
			//Start to receive the file
	    	byte[] clientName = receiveSequence();
	    	FileExchangeController.setProgressbar(20);
			byte[] nameFile = receiveSequence();
	    	FileExchangeController.setProgressbar(40);
			byte[] fileArray = receiveSequence();	
	    	FileExchangeController.setProgressbar(70);
			//Update Client name
			this.client = TypeConverter.byteArrayToString(clientName);
			new TypeConverter();
			//Get name of file for check the type.
			String fileName = TypeConverter.byteArrayToString(nameFile);
	    	FileExchangeController.setProgressbar(100);
			//Text or file control
			if(fileName.equals("string")) {
				this.stringChatDetector(fileArray);
			}
			else{
				FileExchangeController.fileAppendServer(fileName, client);
				if(JOptionPane.showConfirmDialog(null, "Download the File?", "choose one", JOptionPane.YES_NO_OPTION) == 0){
					//Select directory where save file
					File directory = new OpenButtons().fileChooser(FileTypes.DIRECTORY);
					if(directory!=null){
						BufferedOutputStream byteToFile = new BufferedOutputStream
								(new FileOutputStream(directory+"/"+fileName));
						byteToFile.write(fileArray, 0, fileArray.length);
						byteToFile.close();
						FileExchangeController.textAppendServer("File Downloaded", client);
					}
					else FileExchangeController.textAppendServer("File Discarded", client);
				}
				else FileExchangeController.textAppendServer("File Discarded", client);
			}
		} catch (IOException e) {
			throw e;
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
		FileExchangeController.textAppendServer(append, client);
	}
	
	//Getter
	public static ServerSocket getSocket() {
		return server;
	}
	
	//Setter
	public static void setOnLine(boolean onLine) {
		SocketServer.onLine = onLine;
	}
}