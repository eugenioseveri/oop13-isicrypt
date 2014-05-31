package algorithms;
/**
 * @author Filippo Vimini
 * Created 12/03/2014
 */
import gui.views.FileExchangeView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.util.Scanner;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;

public class TypeConverter {
	/**
	 * 
	 * @param file
	 * @return
	 * @throws IOException 
	 */
	public static byte[] fileToByte(File file) throws IOException{
		//Create Buffer in/out for read and write the file
		ByteArrayOutputStream baos = null;
		FileInputStream filebufferInput = null;
		byte[] fileArray = null;
		int variable = 0;
		try{
			//initialize buffer
			baos = new ByteArrayOutputStream();
			filebufferInput = new FileInputStream(file);		
			//write byte after byte of file on byte[]
			while((variable = filebufferInput.read()) != -1){
				baos.write(variable);
			}
			//Write byte[] of buffer on normal byte[]
			fileArray = baos.toByteArray();
			return fileArray;
		} catch (FileNotFoundException e) {
			System.out.println("File not found: " + e);
			throw e;
		} catch (IOException e) {
			System.out.println("an I/O error occured: " + e);
			throw e;
		}finally{
				try{
				if(baos != null)baos.close();
			}catch(IOException e ){
				System.out.println("an I/O error occured: " + e);
				throw e;
			}
				try{
					if(filebufferInput != null)filebufferInput.close();
				}catch(IOException e){
					System.out.println("an I/O error occured: " + e);
					throw e;
				}
		}
	}
	/**
	 * Method that convert a File type to text
	 * @param file
	 * @return
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
			// TODO Auto-generated catch block
			System.out.println("File not found: " + e);
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
	 * 
	 * @param rawImage
	 * @return
	 * @throws IOException 
	 */
	public static BufferedImage fileToBufferedImage( File rawImage ) throws IOException{
		BufferedImage imageBuffer = null;
		try {
			imageBuffer = ImageIO.read(rawImage);
		} catch (IOException e) {
			System.out.println("Image writing error occured during message encryprion on image: " + e);
			FileExchangeView.optionPanel("Image writing error occured during message encryprion on image: ");	
			throw e;
		}
		System.out.println("bufferCreator fatto!");
		return imageBuffer;
	}
	/**
	 * 
	 * @param value
	 * @return
	 */
	public static byte[] intToByteArray(int value){
		return new byte[]{
				(byte)(value >>> 24),
	            (byte)(value >>> 16),
	            (byte)(value >>> 8),
	            (byte)value};
	}
	/**
	 * 
	 * @param b
	 * @return
	 */
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
	 * 
	 * @param text
	 * @return
	 */
	public  static String byteArrayToString(byte[] text){			
		StringBuilder sb = new StringBuilder(new String(text));
		return sb.toString();
	}
	/**
	 * 
	 * @param bis
	 * @param name
	 * @return
	 * @throws IOException 
	 */
	public static  File bufferedInputTOtempFile(BufferedInputStream bis, String name) throws IOException{
		String nomeTemp = name;
		String tempExtension =".png";
		 File tempFile;
		try {
			tempFile = File.createTempFile(nomeTemp, tempExtension);
			tempFile.deleteOnExit();
	        FileOutputStream out = new FileOutputStream(tempFile);
	        IOUtils.copy(bis, out);
		    return tempFile;
		} catch (IOException e) {
			System.out.println("an I/O error occured: " + e);
			throw e;
		}

	}
	/**
	 * 
	 * @param picture
	 * @return
	 */
	public static byte[] bufferImageToByteArray( BufferedImage picture ){
		WritableRaster raster   = picture.getRaster();
		DataBufferByte buffer = (DataBufferByte)raster.getDataBuffer();
		System.out.println("getImageData done");
		return buffer.getData();
	}
}