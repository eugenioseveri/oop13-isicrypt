package cryptography.algorithms;

import java.net.*;
import java.io.*;
public class SingleSocketServer extends Thread{
	public static void main(String[] args) throws IOException{
		new SingleSocketServer();
	}
	
	static ServerSocket socket1;
	protected static final int port = 19999;
	static Socket connection;
	
	static boolean first;
	static StringBuffer process;
	static String TimeStamp;
	public SingleSocketServer(){
		DataInputStream in = null;
		try{
			socket1 = new ServerSocket(port);
			System.out.println("SingleSocketServer initialized");
			connection =socket1.accept();
			in = new DataInputStream(new BufferedInputStream(connection.getInputStream()));
			byte[] lopo = new byte[4];
		    in.read(lopo);
		    int value = new TypeConverter().byteArrayToInt(lopo);
			byte[] bytes = new byte[value];
			in.read(bytes);
		   for(int count = 0; count < value; count ++){
				System.out.println((char)bytes[count]);
			}
		}
		catch (IOException e){}
		try{
			connection.close();
		}
		catch (IOException e){}
	}

}
