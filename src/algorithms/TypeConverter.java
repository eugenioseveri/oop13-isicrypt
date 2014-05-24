package algorithms;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.awt.Image;
import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.util.Scanner;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;

public class TypeConverter {
	
	public byte[] fileToByte(File file){
		//Create Buffer in/out for read and write the file
		ByteArrayOutputStream baos = null;
		FileInputStream filebufferInput = null;
		byte[] fileArray = null;
		try{
			//initialize buffer
			baos = new ByteArrayOutputStream();
			filebufferInput = new FileInputStream(file);		
			int variable = 0;
			//write byte after byte of file on byte[]
			while((variable = filebufferInput.read()) != -1){
				baos.write(variable);
			}
			//Write byte[] of buffer on normal byte[]
			fileArray = baos.toByteArray();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
				try{
				if(baos != null)baos.close();
			}catch(IOException e ){}
				try{
					if(filebufferInput != null)filebufferInput.close();
				}catch(IOException e){}
		}
		return fileArray;
	}
	
	public byte[] intToByteArray(int value){
		return new byte[]{
				(byte)(value >>> 24),
	            (byte)(value >>> 16),
	            (byte)(value >>> 8),
	            (byte)value};
	}
	
	public int byteArrayToInt(byte[] b){
	    int MASK = 0xFF;
	    int result = 0;   
	        result = b[3] & MASK;
	        result = result + ((b[2] & MASK) << 8);
	        result = result + ((b[1] & MASK) << 16);
	        result = result + ((b[0] & MASK) << 24);            
	    return result; 
	}
	
	/**
	 * Method that convert a File type to image
	 * @param rawImage			
	 * @return
	 * @throws IOException
	 */
	public Image fileToImage( File rawImage ) throws IOException{
		return  ImageIO.read(rawImage);
	}
	/**
	 * Method that convert a File type to text
	 * @param file
	 * @return
	 * @throws FileNotFoundException
	 */
	public String fileToString(File file) throws FileNotFoundException{
		Scanner myScanner = null;
		String contents = null;
		try
		{
			if(file != null){
				myScanner = new Scanner(file);
				contents = myScanner.useDelimiter("\\Z").next(); 
			}
		}
		finally
		{
		    if(myScanner != null)
		    {
		        myScanner.close(); 
		    }
		}
		return contents;
	}
	
	public File bufferedInputTOtempFile(BufferedInputStream bis){
		String nomeTemp = "streamTempFile";
		String tempExtension =".tmp";
		 File tempFile;
		try {
			tempFile = File.createTempFile(nomeTemp, tempExtension);
			tempFile.deleteOnExit();
	        try (FileOutputStream out = new FileOutputStream(tempFile)) {
	            IOUtils.copy(bis, out);
	        }
	        return tempFile;
		} catch (IOException e) {
			e.printStackTrace();
		}
	        return null;
	}
	public int getOffset(byte[] b, int end){
		int MASK = 0xFF;
	    int result = 0;   
	        result = b[end] & MASK;
	        result = result + ((b[end-1] & MASK) << 8);
	        result = result + ((b[end-2] & MASK) << 16);
	        result = result + ((b[end-3] & MASK) << 24);            
	    return result; 
	}
	public String fileByteArrayToString(byte[] array, int offset){
		String name ="";
		int count = array.length - (offset+4);
		for(int i = count; i < array.length-4; i++){
			name += (char)array[i];
		}
		return name;
	}
	
	public String byteArrayToString(byte[] text){			
		StringBuilder sb = new StringBuilder(new String(text));
		return sb.toString();
	}
	
	public byte[] stringAndProprietyToByte(String text){
		String name = "string";
		byte[] extesion = name.getBytes();
		byte[] stringArray = text.getBytes();
		byte[] returnByte = new byte[(extesion.length + stringArray.length)+4];
		for(int i = 0; i < stringArray.length; i++){
			returnByte[i] = stringArray[i];
		}
		int o = 0;
		for(int i = stringArray.length; i < (stringArray.length + extesion.length); i++){
			returnByte[i] = extesion[o];
			o++;
		}
		int m = 0;
		byte[] extLength = new TypeConverter().intToByteArray(6);
		
		for(int l = (extesion.length + stringArray.length); m < 4; l++){
			returnByte[l] = extLength[m];
			m++;
		}
		
		return returnByte;
	}
}