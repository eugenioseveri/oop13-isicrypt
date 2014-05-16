package cryptography.algorithms;

import gui.OpenButtons;
import gui.OpenButtons.FileTypes;

import java.net.*;
import java.io.*;

import javafx.concurrent.Task;

import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;

public class SocketClient extends Thread{
	public static void main(String[] args){
		new SocketClient();
	}
	public SocketClient(){
		/*
		 * input and output buffer
		 */
		ByteArrayInputStream byteArrayIn = null;
		ByteArrayOutputStream byteArrayOut = null;
		OutputStream outStream = null;
		Socket connection = null;
		//Define a host server
		String host = "FiloPC_Desktop";
		//Define a port
		int port = 19999;
			try{
				//Obtain an address object of the server (IP)
				InetAddress address = InetAddress.getByName(host);
				connection = new Socket(address, port);
			//	SocketAddress socketAddress = new InetSocketAddress(address, port);
				System.out.println("Client initialized...:");
				//Establish a socket connection
				outStream = connection.getOutputStream();
				
				System.out.println("Choose a File to Send:");
				
				File file = new OpenButtons().FileChooser(FileTypes.GENERIC_FILE);
			//	File file = new File("./res/Test_Dragon_FULLHD.jpg");
			//	File file = new File("./res/imaTest.jpg");
				
				//Convert File and his name to unique byte array
				byte[] fileArray = new TypeConverter().fileAndProprietyToByte(file);
				//convert array length (int) to byte[]
				byte[] arrayLength = new TypeConverter().intToByteArray(fileArray.length);
				
				System.out.println("length: "+fileArray.length);
				// Send the array length to server for create a buffer array
				outStream.write(arrayLength);
				// Clean the buffer
				outStream.flush();
				
				//New byte[] buffer for fileArray
				
				byteArrayIn = new ByteArrayInputStream(fileArray);
				byteArrayOut = new ByteArrayOutputStream();
				
				int transfertElement ;
				long startTime = System.currentTimeMillis();

				while((transfertElement = byteArrayIn.read()) != -1){
					//output for socket
					outStream.write(transfertElement);
				}
				long seconds = (System.currentTimeMillis() - startTime) / 1000;
				outStream.flush();
				//Check what i've transferred
				byte[] byteCeck = byteArrayOut.toByteArray();
				//Control print of byteCheck
				/*for(int count3 = 0; count3 < byteCeck.length; count3 ++){
					System.out.print((char)byteCeck[count3]);
				}*/
				System.out.println("Tempo di trasferimento: " + seconds + "s");
				outStream.close();
			    byteArrayIn.close();
			    byteArrayOut.close();
				connection.close();
				System.out.println("File send. Connection close...:");
			}
			catch(IOException f){
				System.out.println("IOException " + f);
			}
			catch(Exception g){
				System.out.println("Exception " + g);
			}
	}
		
}
