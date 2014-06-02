package algorithms.interfacesandabstractclasses;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

import algorithms.EnumAsymmetricKeyTypes;


/**
 * Interface used to define mandatory methods of cryptography classes.
 * @author Eugenio Severi
 */
	public interface IAsymmetricCryptography {
		
		/**
		 * Encodes a byte array, using a previously set public key (via @link {@link #setKeyPair(java.security.KeyPair)} or @link {@link #setKeyPair(java.security.PublicKey, java.security.PrivateKey)})
		 * @param input The byte array you want to encode
		 * @return The encrypted byte array
		 */
		byte[] encode(byte[] input);
		
		/**
		 * Decodes a byte array, using a previously set private key (via @link {@link #setKeyPair(java.security.KeyPair)} or @link {@link #setKeyPair(java.security.PublicKey, java.security.PrivateKey)})
		 * @param input The byte array you want to decode
		 * @return The decrypted byte array
		 */
		byte[] decode(byte[] input);
		
		/**
		 * Generates a new asymmetric key pair of the specified length
		 * @param keySize The length (in bits) of the generated key
		 * @throws InvalidKeyException If the key size is not valid.
		 */
		void generateKeyPair(int keySize) throws InvalidKeyException;
		
		/**
		 * Saves an asymmetric key (public or private) to the specified output stream
		 * @param keyType Public or private key
		 * @param output The output stream you want to save the key to
		 * @throws IOException If an error occurs while writing the key
		 */
		void saveKeyToFile(EnumAsymmetricKeyTypes keyType, FileOutputStream output) throws IOException;
		
		/**
		 * Loads an asymmetric key (public or private) from the specified file
		 * @param keyType Public or private key
		 * @param input The input stream you want to read the key from
		 * @throws IOException If an error occurs while reading the key
		 */
		void loadKeyFromFile(EnumAsymmetricKeyTypes keyType, FileInputStream input) throws IOException;
		
		PublicKey getPublicKey();
		
		PrivateKey getPrivateKey();
		
		void setKeyPair(KeyPair pair);
		
		/**
		 * Set a new asymmetric key pair specifying manually the public and private key. Also only one key can be set (if you only want to encrypt or decrypt), leaving the other to null
		 */
		void setKeyPair(PublicKey publicKey, PrivateKey privateKey);
	}
