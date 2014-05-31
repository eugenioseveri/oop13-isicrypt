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
import java.security.PublicKey;

import algorithms.RSA;
import algorithms.Wiping;
import algorithms.interfacesandabstractclasses.IAsymmetricCryptography;
import algorithms.interfacesandabstractclasses.ISymmetricCryptography;
import gui.models.FileInterpret;
import gui.models.IFileInterpret;
import gui.models.OpenButtons;
import gui.models.OpenButtons.FileTypes;
import gui.views.CryptographyView;
import gui.views.ICryptographyView;

/**
 * @author Eugenio Severi
 *
 */
public class CryptographyController implements ICryptographyViewObserver {

	private final static String TEMP_FILE_NAME = "tempCipherFile.tmp";
	private File tempFileToEncrypt = null;
	private File tempOutputFileEncrypt = null;
	private File tempPublicKeyFile = null;
	private File tempPrivateKeyFile = null;
	private File tempFileToDecrypt = null;
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

	@Override
	// TODO: remove
	public void command_SelectOutputFileEncrypt() {
		File selectedFile = new OpenButtons().fileChooser(FileTypes.GENERIC_FILE);
		if(selectedFile != null) {
			this.view.setText_textField_OutputFileEncrypt(selectedFile.getAbsolutePath());
			this.tempOutputFileEncrypt = selectedFile;
		}
	}

	@Override
	// TODO: remove
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void command_Encrypt() throws InstantiationException, IllegalAccessException, ClassNotFoundException, InvalidKeyException, IOException {
		if(this.tempFileToEncrypt != null && this.tempOutputFileEncrypt != null && this.tempPublicKeyFile != null) {
			BufferedInputStream filetoencrypt = new BufferedInputStream(new FileInputStream(tempFileToEncrypt));
			BufferedOutputStream tempCipherFile = new BufferedOutputStream(new FileOutputStream(TEMP_FILE_NAME));
			BufferedOutputStream outputFile = new BufferedOutputStream(new FileOutputStream(tempOutputFileEncrypt));
			ISymmetricCryptography newSymmetricCipher = (ISymmetricCryptography)Class.forName(this.view.get_SymmetricAlgorithm().name()).newInstance(); // Reflection
			newSymmetricCipher.generateKey(128); // TODO: magic number
			newSymmetricCipher.encode(filetoencrypt, outputFile);
			IAsymmetricCryptography newRSA = new RSA(); // Unico algoritmo simmetrico supportato (per ora)
			ObjectInputStream newOis = new ObjectInputStream(new FileInputStream(tempPublicKeyFile));
			newOis.close();
			newRSA.setKeyPair((PublicKey)newOis.readObject(), null);
			byte[] tempEncryptedSymmetricKey = newRSA.encode(newSymmetricCipher.getSymmetricKeySpec().getEncoded());
			this.model = new FileInterpret(this.view.get_SymmetricAlgorithm(), tempEncryptedSymmetricKey, this.view.get_HashingAlgorithm(), this.view.getCompressionAlgorithm(), new File(TEMP_FILE_NAME));
			this.model.writeInterpretToFile(tempOutputFileEncrypt);
			filetoencrypt.close();
			tempCipherFile.close();
			new File(TEMP_FILE_NAME).delete();
			outputFile.close();
			if(this.view.chckbx_isWipingEnabled()) {
				Wiping.getInstance().wipe(tempFileToEncrypt, 1); // TODO: numero di passaggi specificato dall'utente
			}
			// TODO: mostrare qualcosa sulla gui
		} else {
			// TODO: Dialog
		}
	}

	@Override
	public void command_GenerateNewKeyPair() throws InvalidKeyException {
		RSA newRSA = new RSA();
		newRSA.generateKeyPair(2048);
		// TODO: newRSA.saveKeyToFile(keyType, output);
	}

}
