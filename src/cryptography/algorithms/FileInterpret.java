package cryptography.algorithms;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
 * - Compression algorithm
 * - File name
 * - Payload (encrypted file)
 * Two inner static classes provide reading and writing methods.
 * @author Eugenio Severi
 */
public class FileInterpret implements Serializable {
	
	transient private static final int BYTE_CONVERSION_FACTOR = 1024;
	private static final long serialVersionUID = 7222536160521152258L;
	private String symmetricAlgorithm;
	private SecretKey encryptedSymmetricKey;
	private String hashingAlgorithm;
	private String generatedHash;
	private String compressionAlgorithm;
	private String fileName;
	private byte[] payload;
	
	// Costruttore con parametri passati tramite argomenti del metodo
	public FileInterpret(String symmetricAlgorithm, SecretKey encryptedSymmetricKey, String hashingAlgorithm, String generatedHash, String compressionAlgorithm, String fileName, byte[] payload) {
		this.symmetricAlgorithm = symmetricAlgorithm;
		this.encryptedSymmetricKey = encryptedSymmetricKey;
		this.hashingAlgorithm = hashingAlgorithm;
		this.generatedHash = generatedHash;
		this.compressionAlgorithm = compressionAlgorithm;
		this.fileName = fileName;
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
		this.fileName = readFile.getFileName();
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
		return this.symmetricAlgorithm;
	}

	public void setSymmetricAlgorithm(String symmetricAlgorithm) {
		this.symmetricAlgorithm = symmetricAlgorithm;
	}

	public SecretKey getEncryptedSymmetricKey() {
		return this.encryptedSymmetricKey;
	}

	public void setEncryptedSymmetricKey(SecretKey encryptedSymmetricKey) {
		this.encryptedSymmetricKey = encryptedSymmetricKey;
	}

	public String getHashingAlgorithm() {
		return this.hashingAlgorithm;
	}

	public void setHashingAlgorithm(String hashingAlgorithm) {
		this.hashingAlgorithm = hashingAlgorithm;
	}

	public String getGeneratedHash() {
		return this.generatedHash;
	}

	public void setGeneratedHash(String generatedHash) {
		this.generatedHash = generatedHash;
	}

	public String getCompressionAlgorithm() {
		return this.compressionAlgorithm;
	}

	public void setCompressionAlgorithm(String compressionAlgorithm) {
		this.compressionAlgorithm = compressionAlgorithm;
	}

	public String getFileName() {
		return this.fileName;
	}
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public byte[] getPayload() {
		return this.payload;
	}

	public void setPayload(byte[] payload) {
		this.payload = payload;
	}
	
	@Override
	public String toString() {
		return "FileInterpret [symmetricAlgorithm=" + symmetricAlgorithm
				+ ", encryptedSymmetricKey=" + encryptedSymmetricKey
				+ ", hashingAlgorithm=" + hashingAlgorithm
				+ ", generatedHash=" + generatedHash
				+ ", compressionEnabled=" + compressionAlgorithm
				+ ", payloadSize=" + payload.length/BYTE_CONVERSION_FACTOR + " KB]"; // Payload size in KB
	}
}
