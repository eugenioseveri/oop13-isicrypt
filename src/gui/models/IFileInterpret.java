package gui.models;

import java.io.File;
import java.io.IOException;

import algorithms.EnumAvailableCompressionAlgorithms;
import algorithms.EnumAvailableHashingAlgorithms;
import algorithms.EnumAvailableSymmetricAlgorithms;

public interface IFileInterpret {
	
	EnumAvailableSymmetricAlgorithms getSymmetricAlgorithm();
	
	void setSymmetricAlgorithm(EnumAvailableSymmetricAlgorithms symmetricAlgorithm);
	
	byte[] getEncryptedSymmetricKey();
	
	void setEncryptedSymmetricKey(byte[] encryptedSymmetricKey);
	
	EnumAvailableHashingAlgorithms getHashingAlgorithm();
	
	void setHashingAlgorithm(EnumAvailableHashingAlgorithms hashingAlgorithm);
	
	String getGeneratedHash();
	
	void setGeneratedHash(String generatedHash);
	
	EnumAvailableCompressionAlgorithms getCompressionAlgorithm();
	
	void setCompressionAlgorithm(EnumAvailableCompressionAlgorithms compressionAlgorithm);
	
	String getFileName();
	
	void setFileName(String fileName);
	
	byte[] getPayload();
	
	void setPayload(byte[] payload);
	
	/**
	 * Writes this FileInterpret to a file
	 * @param outputFile The file you want to write the FileInterpret to
	 * @throws IOException If an error occurs while writing the payload to disk
	 */
	void writeInterpretToFile(File outputFile) throws IOException;
	
	/**
	 * Writes this payload to disk
	 * @param outputFile The file where you want to put the payload
	 * @throws CorruptedDataException If a file and his checksum don't match
	 * @throws IOException If an IO error occurs while writing the payload to disk
	 */
	void writePayloadToFile(File outputFile) throws CorruptedDataException, IOException;
}
