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
	
	public FileInterpret(String symmetricAlgorithm, SecretKey encryptedSymmetricKey, String hashingAlgorithm, String generatedHash, String compressionEnabled, InputStream payload) {
		this.symmetricAlgorithm = symmetricAlgorithm;
		this.encryptedSymmetricKey = encryptedSymmetricKey;
		this.hashingAlgorithm = hashingAlgorithm;
		this.generatedHash = generatedHash;
		this.compressionAlgorithm = compressionEnabled;
		this.payload = payload;
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

	public String isCompressionEnabled() {
		return compressionAlgorithm;
	}

	public void setCompressionEnabled(String compressionEnabled) {
		this.compressionAlgorithm = compressionEnabled;
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
	
	public static class Reader{
		private FileInputStream file;
		private BufferedInputStream buffStream;
		private ObjectInputStream objStream;

		public Reader(File inputFile) throws FileNotFoundException, IOException {
			file = new FileInputStream(inputFile);
			buffStream = new BufferedInputStream(file);
			objStream = new ObjectInputStream(buffStream);
		}
		public FileInterpret getInterpret() throws ClassNotFoundException, IOException {
			return (FileInterpret)objStream.readObject();
		}
	}
	
	public static class Writer {
		private FileOutputStream file;
		private BufferedOutputStream buffStream;
		private ObjectOutputStream objStream;

		public Writer(File outputFile) throws FileNotFoundException, IOException {
			file = new FileOutputStream(outputFile);
			buffStream = new BufferedOutputStream(file);
			objStream = new ObjectOutputStream(buffStream);
		}
		public void writeInterpret(FileInterpret inter) throws ClassNotFoundException, IOException {
			objStream.writeObject(inter); //oppure anche this? (così non serve il parametro)
		}
	}
}
