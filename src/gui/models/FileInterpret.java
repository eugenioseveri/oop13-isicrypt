package gui.models;

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

import algorithms.EnumAvailableCompressionAlgorithms;
import algorithms.EnumAvailableHashingAlgorithms;
import algorithms.EnumAvailableSymmetricAlgorithms;
import algorithms.Hashing;

/**
 * This class interprets the application custom file format:
 * - Symmetric algorithm
 * - Encrypted symmetric key
 * - Hashing algorithm
 * - Hash of payload file (self-generated)
 * - Compression algorithm
 * - File name (self-generated)
 * - Payload (encrypted file)
 * @author Eugenio Severi
 */
public class FileInterpret implements Serializable, IFileInterpret {
	
	transient private static final int BYTE_CONVERSION_FACTOR = 1024;
	private static final long serialVersionUID = 7222536160521152258L;
	private EnumAvailableSymmetricAlgorithms symmetricAlgorithm;
	private byte[] encryptedSymmetricKey;
	private EnumAvailableHashingAlgorithms hashingAlgorithm;
	private String generatedHash;
	private EnumAvailableCompressionAlgorithms compressionAlgorithm;
	private String fileName;
	private byte[] payload;
	
	// Costruttore con parametri passati tramite argomenti del metodo. Da utilizzare per costruire da zero un FileInterpret.
	public FileInterpret(EnumAvailableSymmetricAlgorithms symmetricAlgorithm, byte[] encryptedSymmetricKey, EnumAvailableHashingAlgorithms hashingAlgorithm, EnumAvailableCompressionAlgorithms compressionAlgorithm, File payloadFile) throws FileNotFoundException, IOException {
		this.symmetricAlgorithm = symmetricAlgorithm;
		this.encryptedSymmetricKey = encryptedSymmetricKey;
		this.hashingAlgorithm = hashingAlgorithm;
		this.generatedHash = Hashing.getInstance().generateHash(hashingAlgorithm, new BufferedInputStream(new FileInputStream(payloadFile)));;
		this.compressionAlgorithm = compressionAlgorithm;
		this.fileName = payloadFile.getName();
		loadPayloadToRAM(payloadFile);
	}

	// Costruttore con parametri letti da file. Da utilizzare per leggere un FileInterpret precedentemente scritto su disco.
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
		// Sono necessari tutti questi close() o ne basta uno? (Idem nel metodo writeInterpretToFile() (sotto))
		objStream.close();
		/*buffStream.close();
		file.close();*/
	}
	
	@Override
	public EnumAvailableSymmetricAlgorithms getSymmetricAlgorithm() {
		return this.symmetricAlgorithm;
	}

	@Override
	public void setSymmetricAlgorithm(EnumAvailableSymmetricAlgorithms symmetricAlgorithm) {
		this.symmetricAlgorithm = symmetricAlgorithm;
	}

	public byte[] getEncryptedSymmetricKey() {
		return this.encryptedSymmetricKey;
	}

	@Override
	public void setEncryptedSymmetricKey(byte[] encryptedSymmetricKey) {
		this.encryptedSymmetricKey = encryptedSymmetricKey;
	}

	@Override
	public EnumAvailableHashingAlgorithms getHashingAlgorithm() {
		return this.hashingAlgorithm;
	}

	@Override
	public void setHashingAlgorithm(EnumAvailableHashingAlgorithms hashingAlgorithm) {
		this.hashingAlgorithm = hashingAlgorithm;
	}

	@Override
	public String getGeneratedHash() {
		return this.generatedHash;
	}

	@Override
	public void setGeneratedHash(String generatedHash) {
		this.generatedHash = generatedHash;
	}

	@Override
	public EnumAvailableCompressionAlgorithms getCompressionAlgorithm() {
		return this.compressionAlgorithm;
	}

	@Override
	public void setCompressionAlgorithm(EnumAvailableCompressionAlgorithms compressionAlgorithm) {
		this.compressionAlgorithm = compressionAlgorithm;
	}

	@Override
	public String getFileName() {
		return this.fileName;
	}
	
	@Override
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	@Override
	public byte[] getPayload() {
		return this.payload;
	}

	@Override
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
	
	@Override
	public void writeInterpretToFile(File outputFile) throws FileNotFoundException, IOException {
		FileOutputStream file = new FileOutputStream(outputFile);
		BufferedOutputStream buffStream = new BufferedOutputStream(file);
		ObjectOutputStream objStream = new ObjectOutputStream(buffStream);
		objStream.writeObject(this);
		objStream.close();
		/*buffStream.close();
		file.close();*/
	}
	
	// Estrae il payload da una classe e lo scrive su disco. Il file è temporaneo.
	@Override
	public void writePayloadToFile(File outputFile) throws FileNotFoundException, IOException {
		FileOutputStream fos = new FileOutputStream(outputFile);
		BufferedOutputStream buffPayload = new BufferedOutputStream(fos);
		try {
			for(byte currentByte: this.payload) {
				buffPayload.write(currentByte);
			}
		} catch (IOException e){
			// Gestire l'eccezione
		} finally {
			buffPayload.close(); // Throws IOException
		}
	}
	
	private void loadPayloadToRAM(File payload) throws FileNotFoundException, IOException {
		FileInputStream fis = new FileInputStream(payload);
		BufferedInputStream buffPayload = new BufferedInputStream(fis);
		int currentByte;
		try {
			for(int index = 0; (currentByte = buffPayload.read()) != -1; index++) {
			this.payload[index] = (byte)currentByte;
			}
		} catch (IOException e) {
			// Gestire eccezione
		} finally {
			buffPayload.close(); // Throws IOException
		}
	}
}
