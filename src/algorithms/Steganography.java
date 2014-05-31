package algorithms;
/**
 * @author Filippo Vimini
 * @version 1.0
 * created 08/04/2014
 */
import gui.models.OpenButtons;
import gui.models.OpenButtons.FileTypes;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.StringUtils;

public class Steganography {

	private static BufferedImage image;
	/**
	 * Take in input an image and text then hide the text inside image
	 * 
	 * @param rawImage			File that contains the selected image.
	 * @param extension			String that contains the extension for the output image.
	 * @param text				String that contains the message to encrypt.
	 * @return boolean					confirm the image saving.
	 * @throws IOException
	 */
	public boolean messageEncrypter(File rawImage, String extension, String text) throws IOException{
		try {
			image = TypeConverter.fileToBufferedImage(rawImage);
			image = messageAdder(image, text);
			String fileName = new OpenButtons().fileChooser(FileTypes.DIRECTORY)+"/Stega_"+rawImage.getName();
			File savier = new File(fileName);
			System.out.println("messageEncrypter done!"); //Check print
			return ImageIO.write(image, extension, savier);
		} catch (IOException e) {
			System.out.println("Image writing error occured during message encryprion on image: " + e);
			throw e;
		} catch (IllegalArgumentException e){
			System.out.println("Null pointer during ImageIO writing: " + e);
			throw e;
		}
	}
	/**
	 * Search if in the image File is hided a text
	 * 
	 * @param rawImage		Search a hidden message from this File
	 * @return	String			String that represent the hidden message
	 * @throws IOException 
	 */
	public String messageBorrower(File rawImage) throws IOException{
		byte[] decode;
		
		image = TypeConverter.fileToBufferedImage(rawImage);
		image = imageAnalizer( image );
		byte[] imageByte = TypeConverter.bufferImageToByteArray(image);
		decode = textFinder(imageByte);
		if(decode == null || StringUtils.isBlank(new String(decode)))return null;
		System.out.println("messageBorrower fatto!");
		return new String(decode);
	}
	/**
	 * Take in input an image and text then hide the text inside image like messageEncrypter but save the file in Temp
	 * folder because it will be send at Server and then saved on disk
	 * 
	 * @param rawImage
	 * @param extension
	 * @param text
	 * @return
	 * @throws IOException 
	 */
	public static File stegaForClient(File rawImage, String extension, String text) throws IOException{
		BufferedImage image;
		try {
			image = TypeConverter.fileToBufferedImage(rawImage);
			image = messageAdder(image, text);
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(rawImage));
			File temp = TypeConverter.bufferedInputTOtempFile(bis, rawImage.getName());
			System.out.println("messageEncrypter done!"); //Check print
			return temp;
		} catch (FileNotFoundException e) {
			System.out.println("File doesn't exist: " + e);
			throw e;
		} catch (SecurityException e){
			System.out.println("Security error, can't read the file: " + e);
			throw e;
		} catch (IOException e) {
			System.out.println("an I/O error occured: " + e);
			throw e;
		}
	}
	/**
	 * This method take in input a buffer that contains an image and a text, then convert in byte[] and join them, 
	 * giving back a new buffer with image with inside and hidden text
	 * 
	 * @param image				Buffer that contains image
	 * @param text				String that represent the text that will be hidden in the image
	 * @return	BufferImage		Buffer that contains the image with hidden text
	 */
	private static BufferedImage messageAdder( BufferedImage image, String text ){
		//number that represent the star byte of the message
		int DIMENSIONSTART = 0;
		//number that represent the star byte of the message length
		int MESSAGESTART = 32; 
		// convert text, text length and image to byte[] then the text byte[] will put inside image byte[]
		byte[] message = text.getBytes();
		byte[] bitTextLength = TypeConverter.intToByteArray(text.length());
		byte[] imageData = TypeConverter.bufferImageToByteArray(image);
		//Hide message offset
		textHiding(imageData, bitTextLength, DIMENSIONSTART);
		//Hide message
		textHiding(imageData, message, MESSAGESTART);
		System.out.println("messageAdder done");
		return image;
	}
	/**
	 * 
	 * 
	 * @param image		byte[] that contains the raster of image
	 * @param text		byte[] that contains the text in byte
	 * @param offset	Integer that represent the offset on message start
	 * @return	image	byte[] that contains the image with text hidden
	 */
	/*Questo metodo prende in ingresso l'immagine, il testo convertiti in byte e l'offset della distanza in cui incomincia il dato da 
	 * analizzare. restituisce un array contenente i dati dell'immagine, del testo nascosto e di dove iniziare a cercare i dati*/
	private static byte[] textHiding(byte[] image, byte[] text, int offset){
		if(text.length + offset> image.length){
			throw new IllegalArgumentException("The file is not longh enoght for the message!!");
		}
		//loop lungo tutto il messaggio, che sia esso la lunghezza dell'offset (4) o la lunghezza del testo da criptare (ND).
		for( int i=0; i < text.length; i++ ){
			int code = text[i];
			/*
			 * Questo for serve per shiftare tutti i bit di code verso destra e salvarne il meno significativo nell'immagine,
			 * parto da 7 poichè in un byte ci sono 8 bit e se partissi da 8 molte operazioni sarebbero inutili.
			 * Aumento l'offset ad ogni ciclo cosi da scorrere i byte dell'immagine.
			 */
			for( int bit = 7; bit >= 0; bit--, offset++ ){
				int positionBit = ( code >>> bit ) & 1;
				/*0xFE equivale in binario a: 11111110, facendo l'AND con il byte lascio invariati tutti i bit eccetto l'ultimo
				 *che andò a sostituire con quello di  positionBit, cosi facendo salverò come bit meno significativo quello di 
				 *code in base alla posizione dello shiftR partendo dall'offset.
				 */
				image[offset] = (byte)(image[offset] & 0xFE | positionBit );
			}
		}
		System.out.println("textHiding done");
		return image;
	}
	
	private byte[] textFinder(byte[]image){
		int length = 0; //si inizzializerà poi un parametro da passare per indicare il punto di dove inizia la lunghezza del messaggio
		int offset = 32; //Da decidere se farlo passare come paramero, poichè è possibile che non serva lavorare con tipi differenti dall'intero
		int intMaxDimension = 2147483647;
		for( int i = 0; i < 32; i++){
			length = (length << 1) | (image[i] & 1 ); 
			if(length >= intMaxDimension || length < 0)return null;
		}
		byte[] decodeLength = new byte[length];
		for(int a = 0; a < decodeLength.length; ++a){
			for(int i=0; i<8; i++, offset++){
				decodeLength[a] = (byte)((decodeLength[a] << 1) | (image[offset] & 1));
			}
		}
		System.out.println("textFinder done");
		return decodeLength;
	}

	/*Metodo che serve per creare l'ambiente di lavoro con l'immagine selezionata, trasforma cioè un immagine in una griglia di numeri per poterli manipolare a piacimento*/
	private BufferedImage imageAnalizer( BufferedImage originalImage){
		/*Create a new buffer with specific information and raster of the selected image, i've choose TYPE_3BYTE_BRG for better 8bit
		 * shift operation in "encode" function*/
		BufferedImage rasterImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
		/*this function create graphic2d specific for this buffer for better graphics operations*/
		Graphics2D graphicalImage = rasterImage.createGraphics();
		/*Le funzioni precedenti servivano per creare l'ambiete di lavoro cucito per l'immagine selezionata, con questa invece disegno l'immagine 
		 * originale nel suo ambiente di lavoro cosicché acquisisca le funzionalità per essere modificata a piacimento e si possano manipolare
		 * i suoi dati*/
		graphicalImage.drawRenderedImage(originalImage, null);
		/*Fin'ora ho sempre l'avorato con finzioni che modificavan il buffer, cosi non mi reasta che restituirlo modificato*/
		graphicalImage.dispose(); //Finito di lavorare con l'immagine preparo l'ambiente di lavoro per l'eliminazione
		System.out.println("imageAnalizer done");//print usato per debuggare, old is the best way!
		return rasterImage;
	}
	
}