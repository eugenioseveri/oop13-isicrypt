package cryptography.algorithms;

import gui.OpenButtons;
import gui.OpenButtons.FileTypes;

import java.net.*;
import java.io.*;

public class SocketClient extends Thread{
	public static void main(String[] args){
		new SocketClient();
	}
	public SocketClient(){
		/*
		 * input and output buffer
		 */
		DataInputStream in = null;
		DataOutputStream out = null;
		//Define a host server
		String host = "localhost";
		//Define a port
		int port = 19999;
			try{
				//Obtain an address object of the server (IP)
				InetAddress address = InetAddress.getByName(host);
				//Establish a socket connection
				Socket connection = new Socket(address, port);
				in = new DataInputStream(new BufferedInputStream(connection.getInputStream()));
				out = new DataOutputStream(new BufferedOutputStream(connection.getOutputStream()));
				File file = new OpenButtons().FileChooser(FileTypes.GENERIC_FILE);
				byte[] fileArray = new TypeConverter().fileToByte(file);
				byte[] arrayLength = new byte[4];
				arrayLength = new TypeConverter().intToByteArray(fileArray.length);
				System.out.println("length: "+(byte)fileArray.length);
				out.write(arrayLength);
				out.flush();
				out.write(fileArray);
				out.close();
			    in.close();
				connection.close();
			}
			catch(IOException f){
				System.out.println("IOException " + f);
			}
			catch(Exception g){
				System.out.println("Exception " + g);
			}
	}
}
