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
import static algorithms.ErrorMessages.*;
import static algorithms.EnumAvailableCompressionAlgorithms.*;
import static algorithms.EnumAsymmetricKeyTypes.*;

/**
 * @author Eugenio Severi
 *
 */
public class CryptographyController implements ICryptographyViewObserver {

	private final static String TEMP_CIPHER_FILE_NAME = "tempCipherFile.tmp"; // TODO: metterlo in una cartella temporanea
	private final static String TEMP_COMPRESSION_FILE_NAME = "tempCompressionFile.tmp";
	private final static String TEMP_PAYLOAD_FILE_NAME = "tempPayloadExtracted.tmp";
	private File tempFileToEncrypt = null;
	private File tempOutputFileEncrypt = null;
	private File tempPublicKeyFile = null;
	private File tempFileToDecrypt = null;
	private File tempOutputFileDecrypt = null;
	private File tempPrivateKeyFile = null;
	private ICryptographyView view;
	private IFileInterpret model;
	
	public CryptographyController(CryptographyView view) {
		this.view = view;
		this.view.attachViewObserver(this);
	}

	/*public void setView(CryptographyView view) {
		this.view = view;
		this.view.attachViewObserver(this);
	}*/
	
	@Override
	public void command_SelectFileToEncrypt() throws FileNotFoundException {
		File selectedFile = new OpenButtons().fileChooser(FileTypes.GENERIC_FILE);
		if(selectedFile != null) {
			this.view.setText_textField_FileToEncrypt(selectedFile.getAbsolutePath());
			this.tempFileToEncrypt = selectedFile;
		}
	}

	@Override
	public void command_SelectFileToDecrypt() {
		File selectedFile = new OpenButtons().fileChooser(FileTypes.GENERIC_FILE);
		if(selectedFile != null) {
			this.view.setText_textField_FileToDecrypt(selectedFile.getAbsolutePath());
			this.tempFileToDecrypt = selectedFile;
		}
	}

	// TODO: remove
	@Override
	public void command_SelectOutputFileEncrypt() {
		File selectedFile = new OpenButtons().fileChooser(FileTypes.GENERIC_FILE);
		if(selectedFile != null) {
			this.view.setText_textField_OutputFileEncrypt(selectedFile.getAbsolutePath());
			this.tempOutputFileEncrypt = selectedFile;
		}
	}

	// TODO: remove
	@Override
	public void command_SelectOutputFileDecrypt() {
		
	}

	@Override
	public void command_SelectPublicKeyFile() {
		File selectedFile = new OpenButtons().fileChooser(FileTypes.GENERIC_FILE);
		if(selectedFile != null) {
			this.view.setText_textField_PublicKey(selectedFile.getAbsolutePath());
			this.tempPublicKeyFile = selectedFile;
		}
	}

	@Override
	public void command_SelectPrivateKeyFile() {
		File selectedFile = new OpenButtons().fileChooser(FileTypes.GENERIC_FILE);
		if(selectedFile != null) {
			this.view.setText_textField_PrivateKey(selectedFile.getAbsolutePath());
			this.tempPrivateKeyFile = selectedFile;
		}
		
	}

	@Override
	public void command_Decrypt() {
		if(this.tempFileToDecrypt != null && this.tempOutputFileDecrypt != null && this.tempPrivateKeyFile != null) {
			BufferedInputStream tempCipherFile = null;
			BufferedOutputStream outputFile = null;
			BufferedOutputStream tempCompressionFile = null;
			ISymmetricCryptography newSymmetricCipher = null;
			IAsymmetricCryptography newRSA = new RSA();
			ObjectInputStream streamPrivateKeyFile = null;
			try {
				tempCipherFile = new BufferedInputStream(new FileInputStream(TEMP_CIPHER_FILE_NAME));
				outputFile = new BufferedOutputStream(new FileOutputStream(tempOutputFileDecrypt));
				this.model = new FileInterpret(tempFileToDecrypt);
				streamPrivateKeyFile = new ObjectInputStream(new FileInputStream(tempPrivateKeyFile));
				newRSA.setKeyPair(null, (PrivateKey)streamPrivateKeyFile.readObject());
				byte[] tempDecryptedSymmetricKey = newRSA.decode(this.model.getEncryptedSymmetricKey());
				String reflectionSymmetricAlgorithm = this.model.getSymmetricAlgorithm().name();
				newSymmetricCipher = (ISymmetricCryptography)Class.forName(reflectionSymmetricAlgorithm).newInstance();
				newSymmetricCipher.setSymmetricKeySpec(new SecretKeySpec(tempDecryptedSymmetricKey, reflectionSymmetricAlgorithm));
				this.model.writePayloadToFile(new File(TEMP_PAYLOAD_FILE_NAME)); // TODO: magic strings
				String payloadFile = TEMP_PAYLOAD_FILE_NAME;
				if(this.model.getCompressionAlgorithm() != No_Compression) {
					tempCompressionFile = new BufferedOutputStream(new FileOutputStream(TEMP_COMPRESSION_FILE_NAME));
					algorithms.GZip.getInstance().decompress(tempCipherFile, tempCompressionFile);
					payloadFile = TEMP_COMPRESSION_FILE_NAME;
				}
				// TODO: chiedere all'utente la cartella in cui salvare il file (output file, usando il nome nel model)
				newSymmetricCipher.decode(new BufferedInputStream(new FileInputStream(payloadFile)), outputFile);
				
			} catch (FileNotFoundException e) {
				System.err.println(FILE_NOT_FOUND_GENERIC_ERROR);
				// TODO: message
			} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
				System.err.println(REFLECTION_ERROR);
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (CorruptedDataException e) {
				System.err.println(CORRUPTED_FILE_ERROR);
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
		} else {
			// TODO: dialog
		}
	}

	@Override
	public void command_Encrypt() {
		if(this.tempFileToEncrypt != null && this.tempOutputFileEncrypt != null && this.tempPublicKeyFile != null) {
			BufferedInputStream filetoencrypt = null;
			BufferedOutputStream tempCipherFile = null;
			BufferedOutputStream outputFile = null;
			BufferedOutputStream tempCompressionFile = null;
			ISymmetricCryptography newSymmetricCipher = null;
			IAsymmetricCryptography newRSA = new RSA(); // Unico algoritmo simmetrico supportato (per ora)
			ObjectInputStream streamPublicKeyFile = null;
			try {
				filetoencrypt = new BufferedInputStream(new FileInputStream(tempFileToEncrypt));
				tempCipherFile = new BufferedOutputStream(new FileOutputStream(TEMP_CIPHER_FILE_NAME));
				outputFile = new BufferedOutputStream(new FileOutputStream(tempOutputFileEncrypt));
				newSymmetricCipher = (ISymmetricCryptography)Class.forName(this.view.get_SymmetricAlgorithm().name()).newInstance(); // Reflection
				newSymmetricCipher.generateKey(128); // TODO: magic number
				newSymmetricCipher.encode(filetoencrypt, outputFile);
				streamPublicKeyFile = new ObjectInputStream(new FileInputStream(tempPublicKeyFile));
				newRSA.setKeyPair((PublicKey)streamPublicKeyFile.readObject(), null);
				byte[] tempEncryptedSymmetricKey = newRSA.encode(newSymmetricCipher.getSymmetricKeySpec().getEncoded());
				String payloadFile = TEMP_CIPHER_FILE_NAME;
				if(this.view.getCompressionAlgorithm() != No_Compression) {
					tempCompressionFile = new BufferedOutputStream(new FileOutputStream(TEMP_COMPRESSION_FILE_NAME));
					algorithms.GZip.getInstance().compress(new BufferedInputStream(new FileInputStream(TEMP_CIPHER_FILE_NAME)), tempCompressionFile); // C'è modo di usare la reflection? Al momento funziona solo con GZip
					payloadFile = TEMP_COMPRESSION_FILE_NAME;
				}
				this.model = new FileInterpret(this.view.get_SymmetricAlgorithm(), tempEncryptedSymmetricKey, this.view.get_HashingAlgorithm(), this.view.getCompressionAlgorithm(), new File(payloadFile));
				this.model.writeInterpretToFile(tempOutputFileEncrypt);
				
			} catch (FileNotFoundException e) {
				System.err.println(FILE_NOT_FOUND_GENERIC_ERROR);
				// TODO Message
			} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
				System.err.println(REFLECTION_ERROR);
				e.printStackTrace();
			}  catch (InvalidKeyException e) {
				System.err.println(WRONG_KEYSIZE_ERROR);
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
					if(outputFile != null) {
						outputFile.close();
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
			if(this.view.chckbx_isWipingEnabled()) {
				Wiping.getInstance().wipe(tempFileToEncrypt, 1); // TODO: numero di passaggi specificato dall'utente
			}
			// TODO: mostrare qualcosa sulla gui
		} else {
			// TODO: Dialog
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
				System.err.println(IO_WRITING_ERROR);
				e.printStackTrace();
			}
		} else {
			// TODO: dialog
		}
	}

}
