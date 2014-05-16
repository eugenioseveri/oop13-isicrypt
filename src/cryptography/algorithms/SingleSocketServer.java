package cryptography.algorithms;

import gui.OpenButtons;
import gui.OpenButtons.FileTypes;

import java.net.*;
import java.io.*;

import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
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
	public SingleSocketServer() throws InterruptedException{
//	DataInputStream in = null;
	InputStream inStream = null;
	OutputStream outStream = null;
	ByteArrayOutputStream out = null;
	BufferedOutputStream byteToFile = null;
		try{
			socket1 = new ServerSocket(port);
			System.out.println("Server initialized...");
			connection =socket1.accept();
			//connection.connect(connection.getRemoteSocketAddress(), 10000);
			System.out.println("Start to accepting file...");
			inStream = connection.getInputStream();
			outStream = connection.getOutputStream();
 			byte[] lengthByte = new byte[4];
		    inStream.read(lengthByte);
		    int value = new TypeConverter().byteArrayToInt(lengthByte);
			System.out.println("passed value: " + value);
			out = new ByteArrayOutputStream(value);
			int i;
			//in.read(newByte);
            while ( (i = inStream.read()) != -1) {
                out.write(i);
            }
			byte[] newByte = out.toByteArray();//new byte[value];
			System.out.println("bytes[] length: " + newByte.length);
			System.out.println("File readed");
			System.out.println("Client file recived: ");
			int nameOffset = new TypeConverter().getOffset(newByte, newByte.length-1);
			System.out.println("name offset "+nameOffset);
			String fileName = new TypeConverter().byteArrayToString(newByte, nameOffset);
			System.out.println("file name "+fileName);
			byteToFile = new BufferedOutputStream(new FileOutputStream(new OpenButtons().FileChooser(FileTypes.DIRECTORY)+"/"+fileName));
			byteToFile.write(newByte, 0, newByte.length);
			/*byteToFile = new BufferedOutputStream(new FileOutputStream(""));
		   for(int count = 0; count < newByte.length; count ++){
				System.out.print((char)newByte[count]);
			}*/
		   out.close();
		   inStream.close();
		 //  connection.close();
		}
		catch (IOException e){}
		try{
			connection.close();
		}
		catch (IOException e){}
	}

}
