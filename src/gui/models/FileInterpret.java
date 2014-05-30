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
	public FileInterpret(EnumAvailableSymmetricAlgorithms symmetricAlgorithm, byte[] encryptedSymmetricKey, EnumAvailableHashingAlgorithms hashingAlgorithm, EnumAvailableCompressionAlgorithms compressionAlgorithm, File payloadFile) {
		this.symmetricAlgorithm = symmetricAlgorithm;
		this.encryptedSymmetricKey = encryptedSymmetricKey;
		this.hashingAlgorithm = hashingAlgorithm;
		try {
			this.generatedHash = Hashing.getInstance().generateHash(hashingAlgorithm, new BufferedInputStream(new FileInputStream(payloadFile)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		};
		this.compressionAlgorithm = compressionAlgorithm;
		this.fileName = payloadFile.getName();
		loadPayloadToRAM(payloadFile);
	}

	// Costruttore con parametri letti da file. Da utilizzare per leggere un FileInterpret precedentemente scritto su disco.
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
				+ ", encryptedSymmetricKey=" + encryptedSymmetricKey
				+ ", hashingAlgorithm=" + hashingAlgorithm
				+ ", generatedHash=" + generatedHash
				+ ", compressionEnabled=" + compressionAlgorithm
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
	public void writePayloadToFile(File outputFile) {
		FileOutputStream fos;
		BufferedOutputStream buffPayload = null;
		try {
			fos = new FileOutputStream(outputFile);
			buffPayload = new BufferedOutputStream(fos);
			for(byte currentByte: this.payload) {
				buffPayload.write(currentByte);
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally {
			try {
				buffPayload.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void loadPayloadToRAM(File payload) {
		FileInputStream fis;
		BufferedInputStream buffPayload = null;
		int currentByte;
		try {
			fis = new FileInputStream(payload);
			buffPayload = new BufferedInputStream(fis);
			for(int index = 0; (currentByte = buffPayload.read()) != -1; index++) {
				this.payload[index] = (byte)currentByte;
			}	
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				buffPayload.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
