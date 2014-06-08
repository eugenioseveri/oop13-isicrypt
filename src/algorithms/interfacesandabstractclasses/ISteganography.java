package algorithms.interfacesandabstractclasses;

import java.io.File;
import java.io.IOException;

public interface ISteganography {

	/**
	 * Take in input an image and text then hide the text inside image
	 * 
	 * @param rawImage			File that contains the selected image.
	 * @param extension			String that contains the extension for the output image.
	 * @param text				String that contains the message to encrypt.
	 * @throws IOException		Generic I/O problem
	 */
	void messageEncrypter(File rawImage, String extension, String text) throws IOException;

	/**
	 * Search if in the image File there is hided a text
	 * 
	 * @param rawImage			Search a hidden message from this File
	 * @return					String that represent the hidden message
	 * @throws IOException 		Generic I/O problem
	 */
	String messageFinder(File rawImage) throws IOException;

	/**
	 * Take in input an image and text then hide the text inside image
	 *  like messageEncrypter but save the file in Temp
	 * folder because it will be send at Server and then saved on disk
	 * 
	 * @param rawImage //TODO commentare
	 * @param extension
	 * @param text
	 * @return
	 * @throws IOException 
	 */
	File stegaForClient(File rawImage, String extension, String text) throws IOException, SecurityException;
}