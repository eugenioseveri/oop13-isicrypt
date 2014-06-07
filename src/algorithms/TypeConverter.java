package algorithms;
/**
 * @author Filippo Vimini
 * Created 12/03/2014
 * 
 * class that implement some method for convert object
 */
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.util.Scanner;

import org.apache.commons.io.IOUtils;

public class TypeConverter {
	/**
	 * Convert a File to byte[]
	 * 
	 * @param file	input file to convert
	 * @return	byte[]
	 * @throws IOException 
	 */
	public static byte[] fileToByte(File file) throws IOException, FileNotFoundException{
		//Create Buffer in/out for read and write the file
		ByteArrayOutputStream baos = null;
		FileInputStream filebufferInput = null;
		byte[] fileArray = null;
		int variable = 0;
		//initialize buffer
		baos = new ByteArrayOutputStream();
		filebufferInput = new FileInputStream(file);		
		//write byte after byte of file on byte[]
		while((variable = filebufferInput.read()) != -1){
			baos.write(variable);
		}
		//Write byte[] of buffer on normal byte[]
		fileArray = baos.toByteArray();
		if(baos != null)baos.close();
		if(filebufferInput != null)filebufferInput.close();
		return fileArray;
	}
	/**
	 * Method that convert a File to String
	 * 
	 * @param file	input file to convert
	 * @return String
	 * @throws FileNotFoundException
	 */
	public  static String fileToString(File file) throws FileNotFoundException{
		Scanner myScanner = null;
		String contents = null;
		try
		{
			if(file != null){
				myScanner = new Scanner(file);
				contents = myScanner.useDelimiter("\\Z").next(); 
			}
			return contents;
		} catch (FileNotFoundException e) {
			throw e;
		}
		finally
		{
		    if(myScanner != null)
		    {
		        myScanner.close(); 
		    }
		}
	}

	/**
	 * Convert Integer to byte[]
	 * 
	 * @param value int value to convert
	 * @return byte[]
	 */
	public static byte[] intToByteArray(int value){
		return new byte[]{
				(byte)(value >>> 24),
	            (byte)(value >>> 16),
	            (byte)(value >>> 8),
	            (byte)value};
	}
	/**
	 * Convert byte[] to Integer
	 * 
	 * @param byteArray		byte[] to convert
	 * @return int
	 */
	public int byteArrayToInt(byte[] byteArray){
	    int MASK = 0xFF;
	    int result = 0;   
	        result = byteArray[3] & MASK;
	        result = result + ((byteArray[2] & MASK) << 8);
	        result = result + ((byteArray[1] & MASK) << 16);
	        result = result + ((byteArray[0] & MASK) << 24);            
	    return result; 
	}
	/**
	 * Convert a byte[] to String
	 * 
	 * @param byteArray		byte[] to convert
	 * @return	String
	 */
	public static String byteArrayToString(byte[] byteArray){			
		StringBuilder sb = new StringBuilder(new String(byteArray));
		return sb.toString();
	}
	/**
	 * Convert a BufferedInputStream to temporary file
	 * 
	 * @param bis		InputStream to convert
	 * @param name		name of file
	 * @returnFile
	 * @throws IOException 		General I/O problem
	 */
	public static File bufferedInputTOtempFile(InputStream inputStream, String name, String extension) throws IOException{
		BufferedInputStream bis = new BufferedInputStream(inputStream);
		String nomeTemp = name;
		String tempExtension = extension;
		File tempFile;
		tempFile = File.createTempFile(nomeTemp, tempExtension);
		tempFile.deleteOnExit();
        FileOutputStream out = new FileOutputStream(tempFile);
        IOUtils.copy(bis, out);
	    return tempFile;
	}
}