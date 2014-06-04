package gui.controllers;

/**
 * Interface for the cryptography function controller.
 * @author Eugenio Severi
 */
public interface ICryptographyViewObserver {
	
	// ENCRYPTION methods
	/**
	 * Asks the user for a file to encrypt (source).
	 */
	void command_SelectFileToEncrypt();
	
	/**
	 * Asks the user for a public key file to encrypt the symmetric key.
	 */
	void command_SelectPublicKeyFile();
	
	/**
	 * Generates a new RSA key pair to encrypt the symmetric key.
	 */
	void command_GenerateNewKeyPair();
	
	/**
	 * Encrypts the source to the specified destination file.
	 */
	void command_Encrypt();
	
	// DECRIPTION methods
	/**
	 * Asks the user for a file to decrypt (source).
	 */
	void command_SelectFileToDecrypt();

	/**
	 * Asks the user for a private key file to decrypt the symmetric key.
	 */
	void command_SelectPrivateKeyFile();
	
	/**
	 * Decrypts the source to the specified file.
	 */
	void command_Decrypt();
}
