package cryptography.algorithms;

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
	
	public byte[] fileAndProprietyToByte(File file){
		ByteArrayOutputStream baos = null;
		FileInputStream ios = null;
		int OFFSET = 4;
		int NAMEBYTELENGTH = file.getName().getBytes().length;
		int ARRAYLENGTH = (int)(file.length() + NAMEBYTELENGTH) + OFFSET;
		int FILELENGTH = (int)file.length();
		int nameByteCounter = 0;
		int fileByteCounter = 0;
		byte[] fileArray = new byte[ARRAYLENGTH];
		int FILEARRAYLENTGH = fileArray.length;
		byte[] nameByteArray = file.getName().getBytes();;
		byte[] nameLengthByte = intToByteArray(NAMEBYTELENGTH);
		
		try{
			baos = new ByteArrayOutputStream();
			ios = new FileInputStream(file);		
			int variable = 0;
			//write byte of image on byte[]
			while((variable = ios.read(fileArray)) != -1){
				baos.write(fileArray, 0, variable);
			}
			//Put the name of file (byte) in byte[] int the end of array
			for( fileByteCounter = FILELENGTH;  fileByteCounter< FILEARRAYLENTGH - 4; fileByteCounter++, nameByteCounter++){
				fileArray[fileByteCounter] = nameByteArray[nameByteCounter];
			}
			// add the length byte of name.
			for(int offsetNameLength = 0; offsetNameLength < 4 ; offsetNameLength++){
				fileArray[fileByteCounter+offsetNameLength] = nameLengthByte[offsetNameLength];
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
				try{
				if(baos != null)baos.close();
			}catch(IOException e ){}
				try{
					if(ios != null)ios.close();
				}catch(IOException e){}
		}
		return fileArray;
	}
	
	public File byteToFile(byte[] arrayFile){
		
		return null;
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
			// TODO Auto-generated catch block
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
	public String byteArrayToString(byte[] array, int offset){
		String name ="";
		int count = array.length - (offset+4);
		for(int i = count; i < array.length-4; i++){
			name += (char)array[i];
		}
		return name;
	}
}