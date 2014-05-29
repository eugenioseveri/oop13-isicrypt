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
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import algorithms.AES;
import algorithms.RSA;
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

	//private String tempsymmetricAlgorithm = null;
	//private SecretKey tempencryptedSymmetricKey = null;
	//private String temphashingAlgorithm = null;
	//private String tempcompressionAlgorithm = null;
	private File tempFileToEncrypt = null;
	private File tempOutputFileEncrypt = null;
	private File tempPublicKey = null;
	private ICryptographyView view;
	private IFileInterpret model;
	
	public CryptographyController(CryptographyView view) {
		//this.view = new CryptographyView();
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void command_SelectOutputFileEncrypt() {
		File selectedFile = new OpenButtons().fileChooser(FileTypes.GENERIC_FILE);
		if(selectedFile != null) {
			this.view.setText_textField_OutputFileEncrypt(selectedFile.getAbsolutePath());
			this.tempOutputFileEncrypt = selectedFile;
		}
	}

	@Override
	public void command_SelectOutputFileDecrypt() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void command_SelectPublicKeyFile() {
		File selectedFile = new OpenButtons().fileChooser(FileTypes.GENERIC_FILE);
		if(selectedFile != null) {
			this.view.setText_textField_PublicKey(selectedFile.getAbsolutePath());
			this.tempPublicKey = selectedFile;
		}
		
	}

	@Override
	public void command_SelectPrivateKey() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void command_Decrypt() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void command_Encrypt() throws IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchProviderException, ClassNotFoundException, IllegalBlockSizeException, BadPaddingException, NullPointerException, NoSuchPaddingException {
		if(this.tempFileToEncrypt != null && this.tempOutputFileEncrypt != null && this.tempPublicKey != null) {
			BufferedInputStream filetoencrypt = new BufferedInputStream(new FileInputStream(tempFileToEncrypt));
			BufferedOutputStream tempCipherFile = new BufferedOutputStream(new FileOutputStream("tempCipherFile.tmp"));
			BufferedOutputStream outputFile = new BufferedOutputStream(new FileOutputStream(tempOutputFileEncrypt));
			AES newSymmetricCipher = new AES(); // TODO: reflection con AES per supportare algoritmi diversi
			newSymmetricCipher.generateKey(128);
			newSymmetricCipher.encode(filetoencrypt, outputFile);
			RSA newRSA = new RSA();
			ObjectInputStream newOis = new ObjectInputStream(new FileInputStream(tempPublicKey));
			newOis.close();
			newRSA.setKeyPair((PublicKey)newOis.readObject(), null);
			byte[] tempEncryptedSymmetricKey = newRSA.encode(newSymmetricCipher.getSymmetricKeySpec().getEncoded());
			this.model = new FileInterpret(this.view.get_SymmetricAlgorithm(), tempEncryptedSymmetricKey, this.view.get_HashingAlgorithm(), this.view.getCompressionAlgorithm(), new File("tempCipherFile.tmp"));
			this.model.writeInterpretToFile(tempOutputFileEncrypt);
			filetoencrypt.close();
			tempCipherFile.close();
			outputFile.close();
			// TODO: mostrare qualcosa sulla gui
		} else {
			// TODO: Dialog
		}
	}

	@Override
	public void command_GenerateNewKeyPair() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
		RSA newRSA = new RSA();
		newRSA.generateKeyPair(2048);
		// TODO: newRSA.saveKeyToFile(keyType, output);
	}

}
