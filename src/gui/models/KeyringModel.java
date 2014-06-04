package gui.models;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.util.HashSet;

import algorithms.AES;

public class KeyringModel extends HashSet<Triple<String, String, String>> implements IKeyringModel {

	private static final long serialVersionUID = -4180040339126413292L;
	
	public KeyringModel() {
		super();
	}
	
	@Override
	public void saveData(OutputStream outputFile, byte[] aesKey) throws IOException, InvalidKeyException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(this); // TODO: crash se l'hashset non è vuoto
		oos.flush();
		oos.close();
		ByteArrayInputStream is = new ByteArrayInputStream(baos.toByteArray());
		AES encryptor = new AES();
		encryptor.setSymmetricKeySpec(aesKey);
		encryptor.encode(is, outputFile);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public void loadData(InputStream inputFile, byte[] aesKey) throws IOException, InvalidKeyException, ClassNotFoundException {
		ByteArrayOutputStream bois = new ByteArrayOutputStream();
		AES decryptor = new AES();
		decryptor.setSymmetricKeySpec(aesKey);
		decryptor.decode(inputFile, bois);
		ByteArrayInputStream bais = new ByteArrayInputStream(bois.toByteArray());
		ObjectInputStream ois = new ObjectInputStream(bais);
		HashSet<Triple<String, String, String>> readData = (HashSet<Triple<String, String, String>>) ois.readObject();
		this.clear();
		this.addAll(readData);
	}
	
	@Override
	public void addItem(String host, String username, String password) {
		this.add(new Triple<String, String, String>(host, username, password));
	}
	
	@Override
	public void removeItem(String host, String username, String password) {
		this.remove(new Triple<String, String, String>(host, username, password));
	}
	
}
