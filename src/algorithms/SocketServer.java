package algorithms;
/**
 * @author Filippo Vimini
 * Created 05/05/2014
 */
import gui.controllers.IFileExchangeViewObserver;
import gui.views.OpenButtons;
import gui.views.OpenButtons.Theme;

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

	public void attachFileExchangeViewObserver(IFileExchangeViewObserver controller){
		this.controller = controller;
	}
	/**
	 * Start Server in a separate thread in concurrency with GUI and wait for client that send a file
	 */
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
		if("string".equals(fileName)) {
			this.stringChatDetector(fileArray);
		}
		else{
			controller.fileAppendServer(fileName, client);
			if( 0 == JOptionPane.showConfirmDialog(null, "Download the File?", "choose one", JOptionPane.YES_NO_OPTION)){
				//Select directory where save file
				final File directory = new OpenButtons().fileChooser(Theme.DIRECTORY);
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
		aesKeyEncryptor = new RSA();
		// Generate new 2048 k?bit key
		aesKeyEncryptor.generateKeyPair(2048);
		//New Buffer for byte[] that contain the key
		final ByteArrayInputStream byteArrayIn = new ByteArrayInputStream(aesKeyEncryptor.getPublicKey().getEncoded());
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
	}
	/**
	 * Receive AES key whereby the server will decrypt the byte[] sended by client
	 * 
	 * @throws IOException
	 * @throws GeneralSecurityException
	 */
	private void receiveAesKey() throws IOException, GeneralSecurityException{	
		connection = server.accept();
		//Initialize new Buffer out
		this.out = new ByteArrayOutputStream();
		//Buffer i connected with socket
		inStream = new BufferedInputStream(connection.getInputStream());
		int counter;
	    while ( (counter = inStream.read()) != -1) {
	           this.out.write(counter);
	       }
	    final byte[] aesKeyDecrypted = aesKeyEncryptor.decode(out.toByteArray());
	    aesEncryptor = new AES();
	    this.aesEncryptor.setSymmetricKeySpec(new SecretKeySpec(aesKeyDecrypted, "AES"));
	    //Close Buffer in/out connection
	    connection.close();
	}
	/**
	 * TODO EUGE
	 * @return byte[]		
	 * @throws IOException
	 * @throws InvalidKeyException
	 */
	private byte[] receiveSequence() throws IOException, InvalidKeyException{
		connection = server.accept();
		this.out = new ByteArrayOutputStream();
		inStream = new BufferedInputStream(connection.getInputStream());
		int counter;
	    while ( (counter = inStream.read()) != -1) {
	           this.out.write(counter);
	       }
	    //closeConnection();
	    final ByteArrayOutputStream byteArrayOutBuffer = new ByteArrayOutputStream();
	    //decode AES key 
	    //EUGE
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
	 * Return the server for closing it from control
	 * @return
	 */
	public static ServerSocket getSocket() {
		return server;
	}
	/**
	 * decompress the file and set right extension
	 * 
	 * @param fileName String that contain the name whit Gzip extension
	 * return String 	name of file without Gzip extension
	 */
	private String decompressFile(final String fileName){
		final ByteArrayInputStream arrayInStream = new ByteArrayInputStream(this.fileArray);
		final ByteArrayOutputStream arrayOutStream = new ByteArrayOutputStream();
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