package cryptography.algorithms;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javax.crypto.SecretKey;

/**
 * This class interprets the application custom file format:
 * - Symmetric algorithm
 * - Encrypted symmetric key
 * - Hashing algorithm
 * - Hash of unencrypted file
 * - Compression enabled (boolean)
 * - Payload (encrypted file)
 * Two inner static classes provide reading and writing methods.
 * @author Eugenio Severi
 */
public class FileInterpret implements Serializable {
	
	private static final long serialVersionUID = 7222536160521152258L;
	private String symmetricAlgorithm;
	private SecretKey encryptedSymmetricKey;
	private String hashingAlgorithm;
	private String generatedHash;
	private String compressionAlgorithm;
	private InputStream payload;
	
	// Costruttore con parametri passati tramite argomenti del metodo
	public FileInterpret(String symmetricAlgorithm, SecretKey encryptedSymmetricKey, String hashingAlgorithm, String generatedHash, String compressionAlgorithm, InputStream payload) {
		this.symmetricAlgorithm = symmetricAlgorithm;
		this.encryptedSymmetricKey = encryptedSymmetricKey;
		this.hashingAlgorithm = hashingAlgorithm;
		this.generatedHash = generatedHash;
		this.compressionAlgorithm = compressionAlgorithm;
		this.payload = payload;
	}

	// Costruttore con parametri letti da file
	public FileInterpret(File inputFile) throws ClassNotFoundException, FileNotFoundException, IOException {
		FileInputStream file = new FileInputStream(inputFile);
		BufferedInputStream buffStream = new BufferedInputStream(file);
		ObjectInputStream objStream = new ObjectInputStream(buffStream);
		// È proprio necessario duplicare l'oggetto? Non c'è modo di assegnare direttamente a "this" l'output di readObject()?
		FileInterpret readFile = (FileInterpret)objStream.readObject();
		this.symmetricAlgorithm = readFile.getSymmetricAlgorithm();
		this.encryptedSymmetricKey = readFile.getEncryptedSymmetricKey();
		this.hashingAlgorithm = readFile.getHashingAlgorithm();
		this.generatedHash = readFile.getGeneratedHash();
		this.compressionAlgorithm = readFile.getCompressionAlgorithm();
		this.payload = readFile.getPayload();
		// Sono necessari tutti questi close() o ne basta uno? (Idem nel metodo sotto)
		objStream.close();
		/*buffStream.close();
		file.close();*/
	}
	
	public void writeInterpretToFile(File outputFile) throws FileNotFoundException, IOException {
		FileOutputStream file = new FileOutputStream(outputFile);
		BufferedOutputStream buffStream = new BufferedOutputStream(file);
		ObjectOutputStream objStream = new ObjectOutputStream(buffStream);
		objStream.writeObject(this);
		objStream.close();
		/*buffStream.close();
		file.close();*/
	}

	public String getSymmetricAlgorithm() {
		return symmetricAlgorithm;
	}

	public void setSymmetricAlgorithm(String symmetricAlgorithm) {
		this.symmetricAlgorithm = symmetricAlgorithm;
	}

	public SecretKey getEncryptedSymmetricKey() {
		return encryptedSymmetricKey;
	}

	public void setEncryptedSymmetricKey(SecretKey encryptedSymmetricKey) {
		this.encryptedSymmetricKey = encryptedSymmetricKey;
	}

	public String getHashingAlgorithm() {
		return hashingAlgorithm;
	}

	public void setHashingAlgorithm(String hashingAlgorithm) {
		this.hashingAlgorithm = hashingAlgorithm;
	}

	public String getGeneratedHash() {
		return generatedHash;
	}

	public void setGeneratedHash(String generatedHash) {
		this.generatedHash = generatedHash;
	}

	public String getCompressionAlgorithm() {
		return compressionAlgorithm;
	}

	public void setCompressionAlgorithm(String compressionAlgorithm) {
		this.compressionAlgorithm = compressionAlgorithm;
	}

	public InputStream getPayload() {
		return payload;
	}

	public void setPayload(InputStream payload) {
		this.payload = payload;
	}
	
	@Override
	public String toString() {
		return "FileInterpret [symmetricAlgorithm=" + symmetricAlgorithm
				+ ", encryptedSymmetricKey=" + encryptedSymmetricKey
				+ ", hashingAlgorithm=" + hashingAlgorithm
				+ ", generatedHash=" + generatedHash
				+ ", compressionEnabled=" + compressionAlgorithm
				+ ", payload=" + payload + "]"; // Modificare con payload size (e non direttamente il payload)
	}
}
