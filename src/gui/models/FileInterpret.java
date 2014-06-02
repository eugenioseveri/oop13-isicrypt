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
import java.util.Arrays;

import algorithms.EnumAvailableCompressionAlgorithms;
import algorithms.EnumAvailableHashingAlgorithms;
import algorithms.EnumAvailableSymmetricAlgorithms;
import algorithms.Hashing;
import static algorithms.ErrorMessages.*;

/**
 * This class is a wrapper for an encrypted and compressed file. It interprets the application custom file format:
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
	
	/**
	 * This constructor is meant to be used while creating a new FileInterpret from raw data
	 * @param symmetricAlgorithm The symmetric cryptography algorithm you want to use. Must be one of @link {@link EnumAvailableSymmetricAlgorithms}
	 * @param encryptedSymmetricKey The previously encrypted symmetric key using RSA
	 * @param hashingAlgorithm The hashing algorithm you want to use to ensure the payload's integrity. Must be one of @link {@link EnumAvailableHashingAlgorithms}
	 * @param compressionAlgorithm The compression algorithm you want to use. Must be one of @link {@link EnumAvailableCompressionAlgorithms}
	 * @param payloadFile The file you want to put inside this FileInterpret wrapper
	 */
	public FileInterpret(EnumAvailableSymmetricAlgorithms symmetricAlgorithm, byte[] encryptedSymmetricKey, EnumAvailableHashingAlgorithms hashingAlgorithm, EnumAvailableCompressionAlgorithms compressionAlgorithm, String originalFileName, File payloadFile) {
		this.symmetricAlgorithm = symmetricAlgorithm;
		this.encryptedSymmetricKey = encryptedSymmetricKey;
		this.hashingAlgorithm = hashingAlgorithm;
		try {
			this.generatedHash = Hashing.getInstance().generateHash(hashingAlgorithm, new BufferedInputStream(new FileInputStream(payloadFile)));
		} catch (FileNotFoundException e) {
			System.err.println(FILE_NOT_FOUND_ERROR + payloadFile.getAbsolutePath());
			e.printStackTrace();
		};
		this.compressionAlgorithm = compressionAlgorithm;
		this.fileName = originalFileName;
		loadPayloadToRAM(payloadFile);
	}

	/**
	 * This constructor is meant to be used to read a previously created FileInterpret file
	 * @param inputFile The FileInterpret file you want to read
	 */
	public FileInterpret(File inputFile) {
		FileInputStream file;
		BufferedInputStream buffStream;
		ObjectInputStream objStream = null;
		FileInterpret readFile = null;
		try {
			file = new FileInputStream(inputFile);
			buffStream = new BufferedInputStream(file);
			objStream = new ObjectInputStream(buffStream);
			// È proprio necessario duplicare l'oggetto? Non c'è modo di assegnare direttamente a "this" l'output di readObject()?
			readFile = (FileInterpret)objStream.readObject();
		} catch (IOException e) {
			System.err.println(IO_READING_ERROR);
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				objStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		this.symmetricAlgorithm = readFile.getSymmetricAlgorithm();
		this.encryptedSymmetricKey = readFile.getEncryptedSymmetricKey();
		this.hashingAlgorithm = readFile.getHashingAlgorithm();
		this.generatedHash = readFile.getGeneratedHash();
		this.compressionAlgorithm = readFile.getCompressionAlgorithm();
		this.fileName = readFile.getFileName();
		this.payload = readFile.getPayload();
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
				+ ", encryptedSymmetricKeySize=" + encryptedSymmetricKey.length
				+ ", hashingAlgorithm=" + hashingAlgorithm
				+ ", generatedHash=" + generatedHash
				+ ", compressionAlgorithm=" + compressionAlgorithm
				+ ", payloadSize=" + payload.length/BYTE_CONVERSION_FACTOR + " KB]"; // Payload size in KB
	}
	
	@Override
	public void writeInterpretToFile(File outputFile) {
		FileOutputStream file;
		BufferedOutputStream buffStream;
		ObjectOutputStream objStream = null;
		try {
			file = new FileOutputStream(outputFile);
			buffStream = new BufferedOutputStream(file);
			objStream = new ObjectOutputStream(buffStream);
			objStream.writeObject(this);
			
		} catch (IOException e) {
			System.err.println(IO_WRITING_ERROR);
			e.printStackTrace();
		} finally {
			try {
				objStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	// Estrae il payload da una classe e lo scrive su disco. Il file è temporaneo.
	@Override
	public void writePayloadToFile(File outputFile) throws CorruptedDataException {
		FileOutputStream fos;
		BufferedOutputStream buffPayload = null;
		try {
			fos = new FileOutputStream(outputFile);
			buffPayload = new BufferedOutputStream(fos);
			for(byte currentByte: this.payload) {
				buffPayload.write(currentByte);
			}
		} catch (IOException e) {
			System.err.println(IO_WRITING_ERROR);
			e.printStackTrace();
		} finally {
			try {
				buffPayload.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if(!checkPayloadChecksum(outputFile)) {
			throw new CorruptedDataException(CORRUPTED_FILE_ERROR);
		}
	}
	
	/**
	 * Internal method to read a file and put its content in this payload. It is called by one constructor
	 * @param payload The file you want to load
	 */
	private void loadPayloadToRAM(File payload) {
		FileInputStream fis;
		BufferedInputStream buffPayload = null;
		int currentByte;
		try {
			fis = new FileInputStream(payload);
			buffPayload = new BufferedInputStream(fis);
			this.payload = new byte[(int) payload.length()];
			for(int index = 0; (currentByte = buffPayload.read()) != -1; index++) {
				this.payload[index] = (byte)currentByte;
			}	
		} catch (FileNotFoundException e1) {
			System.err.println(FILE_NOT_FOUND_ERROR + payload.getAbsolutePath());
			e1.printStackTrace();
		} catch (IOException e) {
			System.err.println(IO_READING_ERROR);
			e.printStackTrace();
		} finally {
			try {
				buffPayload.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Check if an extracted file is corrupted
	 * @param diskPayload
	 * @return If the stored checksum matches with the generated one
	 */
	private boolean checkPayloadChecksum(File diskPayload) {
		BufferedInputStream buffDiskPayload = null;
		try {
			buffDiskPayload = new BufferedInputStream(new FileInputStream(diskPayload));
		} catch (FileNotFoundException e) {
			System.err.println(FILE_NOT_FOUND_ERROR + diskPayload.getAbsolutePath());
			e.printStackTrace();
		}
		return Hashing.getInstance().compare(this.hashingAlgorithm, buffDiskPayload, this.generatedHash);
	}
}
