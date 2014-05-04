/**
 * 
 */
package cryptography.algorithms;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;

/**
 * @author Filippo Vimini
 *
 */

/*Devo usare BufferedImage poichè preleva il file e lo inserisce in una griglia (raster), dove nel caso dell'immagine, corrisonde ai valori
 * della matrice che vengono usati per la bitmap. La griglia di BufferedImage ha infatti delle coordinate, che inziano con l'angolo altro sx (0,0)*/
public class Steganography {

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
		return rasterImage;
	}
	
	/*Questo metodo prende in ingresso l'immagine, il testo convertiti in byte e l'offset della distanza in cui incomincia il dato da 
	 * analizzare. restituisce un array contenente i dati dell'immagine, del testo nascosto e di dove iniziare a cercare i dati*/
	private byte[] textHiding(byte[] image, byte[] text, int offset){
		if(text.length + offset> image.length){
			throw new IllegalArgumentException("The file is not longh enoght for the message!!");
		}
		//loop lungo tutto il messaggio da criptare
		for( int i=0; i < text.length; i++ ){
			int code = text[i];
			/*Il tipo su internet inserisce nell'immagine l'intero int ricavato dal testo, validandone solo l'ultimo bit, come posizione 
			 * del numero ( 1 o 0 ) che dovrà stare nella posizione indicata dalla sua posizione totale dopo l'offset. Lo spazio veramente 
			 * utile è quindi dato solo dall'ultimo byte, CONTROLLA se con l'inserimento nell'array di byte il resto dei dati va perso
			 * se no scartalo e inserisci solo i byte validi per una migliore compressione anche in ambito advance */
			for( int bit = 7; bit > 0; bit--, offset++ ){
				int a = ( code >>> bit ) & 1;
				image[offset] = (byte)(image[offset] & 0xFE | a );
			}
		}
		return image;
	}
	
	/*Questo metodo prende in ingresso il buffer dell'immagine e il testo, li fa trasformare in byte e li unisce restituendo un nuovo buffer
	 * dell'immagine completo di test nascosto*/
	private BufferedImage messageAdder( BufferedImage image, String text ){
		int MESSAGESTART = 4; //number that represent the star byte of the message
		int LENGTHSTART = 0; //number that represent the star byte of the message length
		/*converto messaggio, lunghezza e immagine in byte per poterli manipolare*/
		byte[] message = text.getBytes();
		byte[] textLength = bitConversion(text.length());
		byte[] imageData = getImageData(image);
		try{
		textHiding(imageData, textLength, MESSAGESTART);
		textHiding(imageData, message, LENGTHSTART);
		}catch(Exception e){
			System.out.println("Can't hiding text: "+e);
		}
		return image;
	}
	
	private byte[] getImageData( BufferedImage picture ){
		WritableRaster raster   = picture.getRaster();
		DataBufferByte buffer = (DataBufferByte)raster.getDataBuffer();
		return buffer.getData();
	}
	
	private byte[] bitConversion(int i){
		return(new byte[]{0,0,0,(byte)( i & 0x000000FF )});
	}
	
	private byte[] textFinder(byte[]image){
		int length = 0; //si inizzializerà poi un parametro da passare per indicare il punto di dove inizia la lunghezza del messaggio
		int offset = 32; //Da decidere se farlo passare come paramero, poichè è possibile che non serva lavorare con tipi differenti dall'intero
		for( int i = 0; i < 32; i++){
			length = (length << 1) | (image[i] & 1 );
		}
		byte[] decodeLength = new byte[length];
		for(int a = 0; a < decodeLength.length; a++){
			for(int i=0; i<8; ++i, ++offset){
				//assign bit: [(new byte value) << 1] OR [(text byte) AND 1]
				decodeLength[a] = (byte)((decodeLength[a] << 1) | (image[offset] & 1));
			}
		}
		return decodeLength;
	}
	public String messageTaker(BufferedImage picture){
		byte[] decode;
		BufferedImage image = imageAnalizer( picture);
		decode = textFinder(getImageData(image));
		return new String(decode);
	}
	
}
