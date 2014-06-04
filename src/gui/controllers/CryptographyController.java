package gui.controllers;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.security.InvalidKeyException;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.spec.SecretKeySpec;

import algorithms.RSA;
import algorithms.Wiping;
import algorithms.interfacesandabstractclasses.IAsymmetricCryptography;
import algorithms.interfacesandabstractclasses.ISymmetricCryptography;
import gui.models.CorruptedDataException;
import gui.models.FileInterpret;
import gui.models.IFileInterpret;
import gui.models.OpenButtons;
import gui.models.OpenButtons.FileTypes;
import gui.views.CryptographyView;
import gui.views.ICryptographyView;
import gui.views.StartScreenView;
import static algorithms.ErrorMessages.*;
import static algorithms.EnumAvailableCompressionAlgorithms.*;
import static algorithms.EnumAsymmetricKeyTypes.*;

/**
 * Class used to implement the cryptography function controller.
 * @author Eugenio Severi
 */
public class CryptographyController implements ICryptographyViewObserver, IGeneralViewObserver {

	private final static String TEMP_CIPHER_FILE_NAME = "tempCipherFile.tmp"; // TODO: metterlo in una cartella temporanea
	private final static String TEMP_COMPRESSION_FILE_NAME = "tempCompressionFile.tmp";
	private final static String TEMP_PAYLOAD_FILE_NAME = "tempPayloadExtracted.tmp";
	private File tempFileToEncrypt = null;
	private File tempPublicKeyFile = null;
	private File tempFileToDecrypt = null;
	private File tempPrivateKeyFile = null;
	private ICryptographyView view;
	private IFileInterpret model;
	
	/**
	 * Creates a new controller for the cryptography function. No model is required.
	 * @param view
	 */
	public CryptographyController(CryptographyView view) {
		this.view = view;
		this.view.attachViewObserver(this);
	}
	
	@Override
	public void command_SelectFileToEncrypt() {
		File selectedFile = new OpenButtons().fileChooser(FileTypes.GENERIC_FILE);
		if(selectedFile != null) {
			this.view.setText_encryptTextField(selectedFile.getAbsolutePath());
			this.tempFileToEncrypt = selectedFile;
		}
	}
	
	@Override
	public void command_SelectPublicKeyFile() {
		File selectedFile = new OpenButtons().fileChooser(FileTypes.GENERIC_FILE);
		if(selectedFile != null) {
			this.view.setText_publicKeyTextField(selectedFile.getAbsolutePath());
			this.tempPublicKeyFile = selectedFile;
		}
	}
	
	@Override
	public void command_GenerateNewKeyPair() {
		RSA newRSA = new RSA();
		try {
			newRSA.generateKeyPair(2048); // TODO: magic number
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		}
		File selectedFile = new OpenButtons().fileChooser(FileTypes.GENERIC_FILE);
		if(selectedFile != null) {
			try {
				newRSA.saveKeyToFile(PUBLIC_KEY, new FileOutputStream(selectedFile.getAbsolutePath() + ".pub"));
				newRSA.saveKeyToFile(PRIVATE_KEY, new FileOutputStream(selectedFile.getAbsolutePath() + ".pvk"));
			} catch (IOException e) {
				this.view.showMessageDialog(IO_WRITING_ERROR);
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void command_Encrypt() {
		File tempOutputFileEncrypt = new OpenButtons().fileChooser(FileTypes.GENERIC_FILE);
		if(this.tempFileToEncrypt != null && tempOutputFileEncrypt != null && this.tempPublicKeyFile != null) {
			BufferedInputStream filetoencrypt = null;
			BufferedOutputStream tempCipherFile = null;
			BufferedOutputStream tempCompressionFile = null;
			ISymmetricCryptography newSymmetricCipher = null;
			IAsymmetricCryptography newRSA = new RSA(); // Unico algoritmo simmetrico supportato (per ora)
			ObjectInputStream streamPublicKeyFile = null;
			try {
				filetoencrypt = new BufferedInputStream(new FileInputStream(tempFileToEncrypt));
				tempCipherFile = new BufferedOutputStream(new FileOutputStream(TEMP_CIPHER_FILE_NAME));
				this.view.setValue_progressBarEncryption(5);
				newSymmetricCipher = (ISymmetricCryptography)Class.forName("algorithms." + this.view.getSymmetricAlgorithm().name()).newInstance(); // Reflection
				newSymmetricCipher.generateKey(128); // TODO: magic number
				newSymmetricCipher.encode(filetoencrypt, tempCipherFile);
				this.view.setValue_progressBarEncryption(30);
				streamPublicKeyFile = new ObjectInputStream(new FileInputStream(tempPublicKeyFile));
				newRSA.setKeyPair((PublicKey)streamPublicKeyFile.readObject(), null);
				byte[] tempEncryptedSymmetricKey = newRSA.encode(newSymmetricCipher.getSymmetricKeySpec().getEncoded());
				this.view.setValue_progressBarEncryption(40);
				String payloadFile = TEMP_CIPHER_FILE_NAME;
				if(this.view.getCompressionAlgorithm() != No_Compression) {
					tempCompressionFile = new BufferedOutputStream(new FileOutputStream(TEMP_COMPRESSION_FILE_NAME));
					algorithms.GZip.getInstance().compress(new BufferedInputStream(new FileInputStream(TEMP_CIPHER_FILE_NAME)), tempCompressionFile); // C'� modo di usare la reflection? Al momento funziona solo con GZip
					payloadFile = TEMP_COMPRESSION_FILE_NAME;
				}
				this.view.setValue_progressBarEncryption(60);
				this.model = new FileInterpret(this.view.getSymmetricAlgorithm(), tempEncryptedSymmetricKey, this.view.getHashingAlgorithm(), this.view.getCompressionAlgorithm(), this.tempFileToEncrypt.getName(),new File(payloadFile));
				this.model.writeInterpretToFile(tempOutputFileEncrypt);
				this.view.setValue_progressBarEncryption(80);
			} catch (FileNotFoundException e) {
				this.view.showMessageDialog(FILE_NOT_FOUND_GENERIC_ERROR);
			} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
				this.view.showMessageDialog(REFLECTION_ERROR);
				e.printStackTrace();
			}  catch (InvalidKeyException e) {
				this.view.showMessageDialog(WRONG_KEYSIZE_ERROR);
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if(filetoencrypt != null) {
						filetoencrypt.close();
					}
					if(tempCipherFile != null) {
						tempCipherFile.close();
						new File(TEMP_CIPHER_FILE_NAME).delete();
					}
					if(streamPublicKeyFile != null) {
						streamPublicKeyFile.close();
					}
					if(tempCompressionFile != null) {
						tempCompressionFile.close();
						new File(TEMP_COMPRESSION_FILE_NAME).delete();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(this.view.getNumberOfWipingPassages() > 0) {
				Wiping.getInstance().wipe(tempFileToEncrypt, this.view.getNumberOfWipingPassages());
			}
			this.view.setValue_progressBarEncryption(100);
			// TODO: mostrare qualcosa sulla gui
		} else {
			this.view.showMessageDialog(FORM_NOT_COMPILED_ERROR);
		}
		this.view.addText_logTextArea("Process completed!");
	}

	@Override
	public void command_SelectFileToDecrypt() {
		File selectedFile = new OpenButtons().fileChooser(FileTypes.GENERIC_FILE);
		if(selectedFile != null) {
			this.view.setText_decryptTextField(selectedFile.getAbsolutePath());
			this.tempFileToDecrypt = selectedFile;
		}
	}
	
	@Override
	public void command_SelectPrivateKeyFile() {
		File selectedFile = new OpenButtons().fileChooser(FileTypes.GENERIC_FILE);
		if(selectedFile != null) {
			this.view.setText_privateKeyTextField(selectedFile.getAbsolutePath());
			this.tempPrivateKeyFile = selectedFile;
		}
		
	}

	@Override
	public void command_Decrypt() {
		File tempOutputFileDecrypt = new OpenButtons().fileChooser(FileTypes.DIRECTORY);
		if(this.tempFileToDecrypt != null && tempOutputFileDecrypt != null && this.tempPrivateKeyFile != null) {
			BufferedInputStream tempCipherFile = null;
			BufferedOutputStream outputFile = null;
			BufferedOutputStream tempCompressionFile = null;
			ISymmetricCryptography newSymmetricCipher = null;
			IAsymmetricCryptography newRSA = new RSA();
			ObjectInputStream streamPrivateKeyFile = null;
			try {
				this.view.setValue_progressBarDecryption(5);
				this.view.addText_logTextArea("Objects initializazion completed.");
				this.model = new FileInterpret(tempFileToDecrypt);
				this.view.setValue_progressBarDecryption(25);
				this.view.addText_logTextArea("The selected file has been loaded.");
				outputFile = new BufferedOutputStream(new FileOutputStream(tempOutputFileDecrypt.getAbsolutePath() + "\\" + this.model.getFileName())); // Platform-independent?
				streamPrivateKeyFile = new ObjectInputStream(new FileInputStream(tempPrivateKeyFile));
				newRSA.setKeyPair(null, (PrivateKey)streamPrivateKeyFile.readObject()); // controllare che la chiave sia giusta
				this.view.setValue_progressBarDecryption(35);
				this.view.addText_logTextArea("The private key has been loaded.");
				byte[] tempDecryptedSymmetricKey = newRSA.decode(this.model.getEncryptedSymmetricKey());
				String reflectionSymmetricAlgorithm = this.model.getSymmetricAlgorithm().name();
				this.view.setValue_progressBarDecryption(45);
				this.view.addText_logTextArea("The symmetric key has been decrypted.");
				this.view.addText_logTextArea("Algorithm: " + reflectionSymmetricAlgorithm + ", key size:" + tempDecryptedSymmetricKey.length);
				newSymmetricCipher = (ISymmetricCryptography)Class.forName("algorithms." + reflectionSymmetricAlgorithm).newInstance();
				newSymmetricCipher.setSymmetricKeySpec(new SecretKeySpec(tempDecryptedSymmetricKey, reflectionSymmetricAlgorithm));
				this.view.setValue_progressBarDecryption(55);
				this.view.addText_logTextArea("The symmetric algorithm has been instantiated and the key set.");
				this.model.writePayloadToFile(new File(TEMP_PAYLOAD_FILE_NAME));
				this.view.setValue_progressBarDecryption(70);
				this.view.addText_logTextArea("The payload has been written to disk.");
				String payloadFile = TEMP_PAYLOAD_FILE_NAME;
				if(this.model.getCompressionAlgorithm() != No_Compression) {
					this.view.addText_logTextArea("GZip compression found: the file is going to be decompressed.");
					tempCompressionFile = new BufferedOutputStream(new FileOutputStream(TEMP_COMPRESSION_FILE_NAME));
					algorithms.GZip.getInstance().decompress(tempCipherFile, tempCompressionFile);
					payloadFile = TEMP_COMPRESSION_FILE_NAME;
					this.view.addText_logTextArea("The file has been decompressed.");
				} else {
					this.view.addText_logTextArea("The file is not compressed.");
				}
				this.view.setValue_progressBarDecryption(80);
				newSymmetricCipher.decode(new BufferedInputStream(new FileInputStream(payloadFile)), outputFile);
				this.view.setValue_progressBarDecryption(90);
				this.view.addText_logTextArea("The file has been decrypted.");
			} catch (FileNotFoundException e) {
				this.view.showMessageDialog(FILE_NOT_FOUND_GENERIC_ERROR);
			} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
				this.view.showMessageDialog(REFLECTION_ERROR);
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (CorruptedDataException e) {
				this.view.showMessageDialog(CORRUPTED_FILE_ERROR);
				e.printStackTrace();
			} finally {
				try {
					if(tempCipherFile != null) {
						tempCipherFile.close();
						new File(TEMP_CIPHER_FILE_NAME).delete();
					}
					if(outputFile != null) {
						outputFile.close();
					}
					if(tempCompressionFile != null) {
						tempCompressionFile.close();
						new File(TEMP_COMPRESSION_FILE_NAME).delete();
					}
					if(streamPrivateKeyFile != null) {
						streamPrivateKeyFile.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			this.view.setValue_progressBarDecryption(100);
			this.view.addText_logTextArea("Temporary files have been cleaned.");
		} else {
			this.view.showMessageDialog(FORM_NOT_COMPILED_ERROR);
		}
		this.view.addText_logTextArea("Process completed!");
	}

	@Override
	public void showStart() {
		StartScreenView.getFrame().setVisible(true);
		StartScreenView.redraw();		
	}

	
}
