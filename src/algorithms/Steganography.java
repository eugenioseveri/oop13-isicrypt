package algorithms;
/**
 * @author Filippo Vimini
 * @version 1.0
 * created 08/04/2014
 */
import gui.views.OpenButtons;
import gui.views.OpenButtons.FileTypes;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.StringUtils;

import algorithms.interfacesandabstractclasses.ISteganography;

public class Steganography implements ISteganography {

	private static BufferedImage image = null;

	@Override
	public void messageEncrypter(File rawImage, String extension, String text) throws IOException, IllegalArgumentException{
		//insert the file in buffer
		image = fileToBufferedImage(rawImage);
		//add the text in buffer
		try{
			image = messageAdder(image, text);
		}catch(IllegalArgumentException e ){
			throw e;
		}		//Choose the destination folder
		String fileName = new OpenButtons().fileChooser(FileTypes.DIRECTORY)+"/Stega_"+rawImage.getName();
		File savier = new File(fileName);
		ImageIO.write(image, extension, savier);
	}

	@Override
	public String messageFinder(File rawImage) throws IOException{
		byte[] hiddenMessage;
		//insert the file in buffer
		image = fileToBufferedImage(rawImage);
		//Convert image to raster
		image = imageAnalizer( image );
		//Convert raster to byte[] for search the message
		byte[] imageByte = bufferImageToByteArray(image);
		//try to find a message on image
		hiddenMessage = textFinder(imageByte);
		if(hiddenMessage == null || StringUtils.isBlank(new String(hiddenMessage)))return null;
		return new String(hiddenMessage);
	}

	@Override
	public File stegaForClient(File rawImage, String extension, String text) throws IOException, SecurityException, IllegalArgumentException{
		// convert File to buffer and add text
		image = fileToBufferedImage(rawImage);
		try{
			image = messageAdder(image, text);
		}catch(IllegalArgumentException e ){
			throw e;
		}
		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(rawImage));
		//Create temp file, because save file is not necessary
		File temp = TypeConverter.bufferedInputTOtempFile(bis, rawImage.getName(), ".png");
		return temp;
	}
	/**
	 * This method take in input a buffer that contains an image and a text, then convert in byte[] and join them, 
	 * giving back a new buffer with image with inside and hidden text
	 * 
	 * @param image				Buffer that contains image
	 * @param text				String that represent the text that will be hidden in the image
	 * @return	BufferImage		Buffer that contains the image with hidden text
	 */
	private static BufferedImage messageAdder( BufferedImage image, String text )throws IllegalArgumentException{
		//number that represent the star byte of the message
		int DIMENSIONSTART = 0;
		//number that represent the star byte of the message length
		int MESSAGESTART = 32; 
		// convert text, text length and image to byte[] then the text byte[] will put inside image byte[]
		byte[] message = text.getBytes();
		byte[] bitTextLength = TypeConverter.intToByteArray(text.length());
		byte[] imageData = bufferImageToByteArray(image);
		try{
			//Hide message offset
			textHiding(imageData, bitTextLength, DIMENSIONSTART);
			//Hide message
			textHiding(imageData, message, MESSAGESTART);
		}catch (IllegalArgumentException e) {
			throw e;
		}
		return image;
	}
	/**
	 * This method takes as input the image, the text converted to bytes,
	 * and the offset of the distance at which it begins to analyze the data.
	 * Returns an array containing the image data, hidden text and where to start looking data.
	 * 
	 * @param image		byte[] that contains the raster of image
	 * @param text		byte[] that contains the text in byte
	 * @param offset	Integer that represent the offset on message start
	 * @return	image	byte[] that contains the image with text hidden
	 */
	private static byte[] textHiding(byte[] image, byte[] text, int offset)throws IllegalArgumentException{
		if(text.length + offset> image.length){
			throw new IllegalArgumentException("The file is not longh enoght for the message!!");
		}
		//loop long all message, that it is the length of offset or the length of text
		for( int i=0; i < text.length; i++ ){
			int code = text[i];
			/*This "for" make the right shit of all bits and save int the less significant in the image,
			 * it's start from 7, not from because it will save a lot of operations.
			 */
			for( int bit = 7; bit >= 0; bit--, offset++ ){
				int positionBit = ( code >>> bit ) & 1;
				/*0xFE is equal at 11111110, doing the "AND"  with byte, leave unchanged all the bit except the last that will be
				 * change with the one of the text, doing this the less significant bit of the image will be the one of text based on the location of offset
				 */
				image[offset] = (byte)(image[offset] & 0xFE | positionBit );
			}
		}
		return image;
	}
	/**
	 * Search if a text is hidden in the byte[] image. Starting from first 32 byte, search first the dimension of the text, then the text cycling 
	 * the byte[] for all the text dimension length
	 * 
	 * @param image	
	 * @return
	 */
	private byte[] textFinder(byte[]image){
		int length = 0; 
		int offset = 32;
		//if the number is more than int there is an error
		int intMaxDimension = 2147483647;
		for( int i = 0; i < 32; i++){
			length = (length << 1) | (image[i] & 1 ); 
			if(length >= intMaxDimension || length < 0)return null;
		}
		byte[] decodeLength = new byte[length];
		//Take the less significant bit of byte[] for all the text length and then shift right TODO finisci di spiegare bene
		for(int a = 0; a < decodeLength.length; ++a){
			for(int i=0; i<8; i++, offset++){
				decodeLength[a] = (byte)((decodeLength[a] << 1) | (image[offset] & 1));
			}
		}
		return decodeLength;
	}
	/**
	 * Method that create a workspace for work with image, transforms an image to a numeric matrix
	 * 
	 * @param originalImage		BufferedImage that contain the image
	 * @return BufferedImage
	 */
	private BufferedImage imageAnalizer( BufferedImage originalImage){
		/*Create a new buffer with specific information and raster of the selected image, choosing TYPE_3BYTE_BRG for better 8bit
		 * shift operation in "encode" function*/
		BufferedImage rasterImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
		/*this function create graphic2d specific for this buffer for better graphics operations*/
		Graphics2D graphicalImage = rasterImage.createGraphics();
		//Draw the picture in the workspace
		graphicalImage.drawRenderedImage(originalImage, null);
		graphicalImage.dispose();
		return rasterImage;
	}
	/**
	 * Convert File to BufferedImage
	 * 
	 * @param rawImage	input file to convert
	 * @return BufferedImage
	 * @throws IOException 
	 */
	private static BufferedImage fileToBufferedImage( File rawImage ) throws IOException{
		BufferedImage imageBuffer = null;
		imageBuffer = ImageIO.read(rawImage);
		return imageBuffer;
	}
	/**
	 * Convert a BufferedImage to byte[]
	 * 
	 * @param picture 	input buffer to convert
	 * @return byte[]
	 */
	private static byte[] bufferImageToByteArray( BufferedImage picture ){
		WritableRaster raster   = picture.getRaster();
		DataBufferByte buffer = (DataBufferByte)raster.getDataBuffer();
		return buffer.getData();
	}
	
}