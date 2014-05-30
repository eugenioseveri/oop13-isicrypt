package gui.models;

import java.io.File;
import java.io.FileNotFoundException;
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
	void writeInterpretToFile(File outputFile);
	void writePayloadToFile(File outputFile);
}
