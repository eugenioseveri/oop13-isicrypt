package cryptography.algorithms;
/**
 * @author Filippo Vimini
 * Created 05/05/2014
 */
import gui.OpenButtons;
import gui.OpenButtons.FileTypes;

import java.net.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.io.*;

import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class SingleSocketServer extends Thread{
	public static void main(String[] args) throws IOException, InterruptedException{
		new SingleSocketServer();
	}
	
	static ServerSocket socket1;
	protected static final int port = 19999;
	static Socket connection;
	
	static boolean first;
	static StringBuffer process;
	static String TimeStamp;
	InputStream inStream = null;
	OutputStream outStream = null;
	ByteArrayOutputStream out = null;
	BufferedOutputStream byteToFile = null;
	AES aesEncryptor = null;
	String client;
	
	public SingleSocketServer() throws InterruptedException, IOException{
		
		socket1 = new ServerSocket(port);
		System.out.println("Server initialized...");
		//Start to accept connections.
		connection =socket1.accept();
		System.out.println("Start to accepting connections...");
		boolean onLine = true;
		while(onLine){
			this.receiveFile();
			onLine = false;
		}
		connection.close();
	}
	
	private void receiveFile(){
	    try {
	    	//Send Public key to client for crypt data.
			System.out.println("Sending Public key...\n");
	    	this.keyExchange();
	    	//Receive AES key for decrypt file
	    	this.receiveAesKey();
			System.out.println("AES key received...\n Start to accepting files...");
			//Start to receive the file
	    	byte[] clientName = receiveSequence();
			byte[] nameFile = receiveSequence();
			byte[] fileArray = receiveSequence();
			
			//Update Client name
			this.client = new TypeConverter().byteArrayToString(clientName);
			System.out.println("\nConnection with: " + this.client+":");
			//Get name of file for check the type.
			String fileName = new TypeConverter().byteArrayToString(nameFile);
			//Text or file control
			if(fileName.equals("string")) this.stringChatDetector(fileArray);
			else{
				System.out.println("Client file recived... \nfile name: "+fileName+"\nDownload?(yes|no)");
				//Keyboard choice
				String riga = this.textInput();
				if(riga.equals("yes")||riga.equals("YES")){
					//Select directory where save file
					File directory = new OpenButtons().FileChooser(FileTypes.DIRECTORY);
					if(directory!=null){
						byteToFile = new BufferedOutputStream(new FileOutputStream(directory+"/"+fileName));
						byteToFile.write(fileArray, 0, fileArray.length);
						byteToFile.close();
						System.out.println("file "+fileName+" saved...");
					}
					else System.out.println("file discarded..");
				}
				else System.out.println("file discarded..");
			}
			System.out.println("");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void keyExchange(){
		try {
			//connection =socket1.accept();
			RSA aesKeyEncryptor = new RSA();
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
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void receiveAesKey(){
		int i;
		try {	
			//connection =socket1.accept();
			//Initialize new Buffer out
			this.out = new ByteArrayOutputStream();
			//Buffer i connected with socket
			inStream = new BufferedInputStream(connection.getInputStream());
	    	while ( (i = inStream.read()) != -1) {
	            this.out.write(i);
	        }
	    	//Save decrypting key 
	    	this.aesEncryptor.setSymmetricKeySpec(new SecretKeySpec(out.toByteArray(), "AES"));
	    	//Close Buffer in/out connection
	    	this.closeConnection();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private byte[] receiveSequence(){
		int i;
		try {	
			this.out = new ByteArrayOutputStream();
			inStream = new BufferedInputStream(connection.getInputStream());
	    	while ( (i = inStream.read()) != -1) {
	            this.out.write(i);
	        }
	    	this.closeConnection();
	    	ByteArrayOutputStream gincapa = new ByteArrayOutputStream();
	    	aesEncryptor.decode(new ByteArrayInputStream(out.toByteArray()), gincapa);
	    	return gincapa.toByteArray();	
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		}
		return out.toByteArray();
	}
	
	private void closeConnection(){
		try {
			out.close();
			inStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	private String textInput(){
		  InputStreamReader reader = new InputStreamReader (System.in);
          BufferedReader input = new BufferedReader (reader);
          String str= new String();
          try {
			str = input.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
          return str;
	}
	
	
	
	private void stringChatDetector(byte[] stringByte){
		int count;
		System.out.print("	");
		for(count = 0; count < stringByte.length; count++){
			System.out.print((char)stringByte[count]);
		}
	}
}
