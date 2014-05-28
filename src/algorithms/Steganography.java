/**
 * 
 */
package algorithms;

import gui.models.OpenButtons;
import gui.models.OpenButtons.FileTypes;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Filippo Vimini
 * @version 1.0
 * created 03/03/2014
 */

/*Devo usare BufferedImage poichè preleva il file e lo inserisce in una griglia (raster), dove nel caso dell'immagine, corrisonde ai valori
 * della matrice che vengono usati per la bitmap. La griglia di BufferedImage ha infatti delle coordinate, che inziano con l'angolo altro sx (0,0)*/
public class Steganography {

	public Image icon; //Used for icon on StegaGUI
	/**
	 * 
	 * @param rawImage			File that contains the selected image.
	 * @param extension			String that contains the extension for the output image.
	 * @param text				String that contains the message to encrypt.
	 * @return					boolean which confirm the image saving.
	 * @throws IOException
	 */
	public boolean messageEncrypter(File rawImage, String extension, String text){
		new TypeConverter();
		//File rawImage = new FileTypeFinder().tempFileFromInput(rawImageBuffer);
		this.icon = TypeConverter.fileToImage(rawImage);
		BufferedImage image;
		try {
			image = bufferCreator(rawImage);
			image = messageAdder(image, text);
			String fileName = new OpenButtons().fileChooser(FileTypes.DIRECTORY)+"/Stega_"+rawImage.getName();
			File savier = new File(fileName);
			System.out.println("messageEncrypter done!"); //Check print
			return ImageIO.write(image, extension, savier);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public String messageBorrower(File rawPicture) throws IOException{
		byte[] decode;
		BufferedImage image = bufferCreator(rawPicture);
		image = imageAnalizer( image );
		decode = textFinder(getImageData(image));
		if(decode == null || StringUtils.isBlank(new String(decode)))return null;
		System.out.println("messageBorrower fatto!");
		return new String(decode);
	}
	/*Questo metodo prende in ingresso il buffer dell'immagine e il testo, li fa trasformare in byte e li unisce restituendo un nuovo buffer
	 * dell'immagine completo di test nascosto*/
	private BufferedImage messageAdder( BufferedImage image, String text ){
		int DIMENSIONSTART = 0; //number that represent the star byte of the message
		int MESSAGESTART = 32; //number that represent the star byte of the message length
		/*converto messaggio, lunghezza e immagine in byte per poterli manipolare*/
		byte[] message = text.getBytes();
		byte[] bitTextLength = bitConversion(text.length()); //un messaggio può essere al max di 255 per ora
		byte[] imageData = getImageData(image);
		try{
		textHiding(imageData, bitTextLength, DIMENSIONSTART);
		textHiding(imageData, message, MESSAGESTART);
		}catch(Exception e){
			System.out.println("Can't hiding text: "+e);
		}
		System.out.println("messageAdder done");//print usato per debuggare, old is the best way!
		return image;
	}
	
	/*Questo metodo prende in ingresso l'immagine, il testo convertiti in byte e l'offset della distanza in cui incomincia il dato da 
	 * analizzare. restituisce un array contenente i dati dell'immagine, del testo nascosto e di dove iniziare a cercare i dati*/
	private byte[] textHiding(byte[] image, byte[] text, int offset){
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
		System.out.println("textHiding done");//print usato per debuggare, old is the best way!
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
		System.out.println("textFinder done");//print usato per debuggare, old is the best way!
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
	
	private byte[] getImageData( BufferedImage picture ){
		WritableRaster raster   = picture.getRaster();
		DataBufferByte buffer = (DataBufferByte)raster.getDataBuffer();
		System.out.println("getImageData done"); //print usato per debuggare, old is the best way!
		return buffer.getData();
	}
	
	private byte[] bitConversion(int i){
		System.out.println("bitConversion done");//print usato per debuggare, old is the best way!
		//FF=255
		byte[] array = new byte[4];
		array[3] = (byte)(i & 0x000000FF);
		array[2] = (byte)((i & 0x0000FF00) >>> 8);
		array[1] = (byte)((i & 0x00FF0000) >>> 16);
		array[0] = (byte)((i & 0xFF000000) >>> 24);
		return array;
	}
	
	/*metodo creato cosicchè si possa prendere in ingresso un tipo File e risalire quindi al nome originale del file selezionato e settarlo anche per il salvataggio*/
	private BufferedImage bufferCreator( File rawImage ) throws IOException{
		BufferedImage imageBuffer = ImageIO.read(rawImage);
		System.out.println("bufferCreator fatto!");
		return imageBuffer;
	}
}
