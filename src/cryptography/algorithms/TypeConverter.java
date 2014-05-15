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
	
	public byte[] fileToByte(File file){
		ByteArrayOutputStream baos = null;
		FileInputStream ios = null;
		int OFFSET = 4;
		int NAMEBYTELENGTH = file.getName().getBytes().length;
		int ARRAYLENGTH = (int)(file.length() + NAMEBYTELENGTH) + OFFSET;
		int nameByteCounter = 0;
		int fileByteCounter = 0;
		byte[] fileArray = null;
		byte[] nameByteArray = file.getName().getBytes();;
		byte[] nameLengthByte = intToByteArray(NAMEBYTELENGTH);
		
		try{
			fileArray = new byte[ARRAYLENGTH];
			//System.out.println("fileBlock length " + fileArray.length);
			baos = new ByteArrayOutputStream();
			ios = new FileInputStream(file);		
			/*
			 * int for BufferedArrayOutputStream writer
			 */
			int variable = 0;
			/*
			 * write byte of image on byte[]
			 */
			while((variable = ios.read(fileArray)) != -1){
				baos.write(fileArray, 0, variable);
			}
			/*
			 * Put the name of file (byte) in byte[] int the end of array
			 */
			for( fileByteCounter = (int) file.length();  fileByteCounter< fileArray.length - 4; fileByteCounter++, nameByteCounter++){
				System.out.println("array length: " + fileArray.length);
				fileArray[fileByteCounter] = nameByteArray[nameByteCounter];
				System.out.println("position length " + fileByteCounter);
			}
			System.out.println("Last passage");
			/*
			 * add the length byte of name.
			 */
			for(int a = 0; a < 4 ; a++){
				fileArray[fileByteCounter+a] = nameLengthByte[a];
				System.out.println("array length: " + fileArray.length);
				System.out.println("position length " + (+a));
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
		public Image FileToImage( File rawImage ) throws IOException{
			return  ImageIO.read(rawImage);
		}
		/**
		 * Method that convert a File type to text
		 * @param file
		 * @return
		 * @throws FileNotFoundException
		 */
		public String textEncoder(File file) throws FileNotFoundException{
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
		
		public File tempFileFromInput(BufferedInputStream bis){
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
}
