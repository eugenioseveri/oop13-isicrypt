package algorithms;
/**
 * @author Filippo Vimini
 * Created 05/05/2014
 */
import gui.models.OpenButtons;
import gui.models.OpenButtons.FileTypes;

import java.net.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.io.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class SocketServer extends Thread{
	public static void main(String[] args) throws IOException, InterruptedException{
		new SocketServer();
	}
	static Socket connection;	
	InputStream inStream = null;
	OutputStream outStream = null;
	ByteArrayOutputStream out = null;
	AES aesEncryptor = null;
	RSA aesKeyEncryptor = null;
	String client;
	//try
	ServerSocket server;
	
	
	public SocketServer() throws InterruptedException, IOException{
		final int port = 19999;
		server = new ServerSocket(port);
		System.out.println("Server initialized...\nStart to accepting connections...");
		//Start to accept connections.
		boolean onLine = true;
		while(onLine){
			connection = server.accept();
			this.receiveFile();
		}
		connection.close();
	}
	
	private void receiveFile(){
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
						BufferedOutputStream byteToFile = new BufferedOutputStream(new FileOutputStream(directory+"/"+fileName));
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
			aesKeyEncryptor = new RSA();
			// Generate new 2048 k?bit key
			aesKeyEncryptor.generateKeyPair(2048);
			//New Buffer for byte[] that contain the key
		//	System.out.println("Server's Public key: ");
		//	byteArrayStamp(aesKeyEncryptor.getPublicKey().getEncoded());
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
		//	System.out.println("KeyExchange complited...");
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
			connection = server.accept();
			//connection =socket1.accept();
			//Initialize new Buffer out
			this.out = new ByteArrayOutputStream();
			//Buffer i connected with socket
			inStream = new BufferedInputStream(connection.getInputStream());
	    	while ( (i = inStream.read()) != -1) {
	            this.out.write(i);
	        }
		//	System.out.println("AES key from client:");
	    	//byteArrayStamp(out.toByteArray());
	    	
	    	byte[] aesKeyDecrypted = aesKeyEncryptor.decode(out.toByteArray());
	    	//byteArrayStamp(aesKeyDecrypted);
	    	//Save decrypting key 
	    	aesEncryptor = new AES();
	    	this.aesEncryptor.setSymmetricKeySpec(new SecretKeySpec(aesKeyDecrypted, "AES"));
	    	//Close Buffer in/out connection
	    //	closeConnection();
	    	connection.close();
	    //	System.out.println("receiveAesKey complited...");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private byte[] receiveSequence(){
		int i;
		try {	
			connection = server.accept();
			this.out = new ByteArrayOutputStream();
			inStream = new BufferedInputStream(connection.getInputStream());
	    	while ( (i = inStream.read()) != -1) {
	            this.out.write(i);
	        }
	    	//closeConnection();
	    	ByteArrayOutputStream gincapa = new ByteArrayOutputStream();
	  //  	byteArrayStamp(out.toByteArray());
	    	aesEncryptor.decode(new ByteArrayInputStream(out.toByteArray()), gincapa);
	 //   	byteArrayStamp(gincapa.toByteArray());
	    	connection.close();
	    	return gincapa.toByteArray();	
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		}
		return out.toByteArray();
	}
	
/*	private void closeConnection(){
		try {
			out.close();
			inStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}	
		
	}	*/
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
	
/*	private void byteArrayStamp(byte[] stamp){
		int i;
		System.out.println("byte[] length: " + stamp.length);
		for(i = 0; i < stamp.length; i++){
			System.out.print((char)stamp[i]);
		}
		System.out.println("");
	}	*/
}
