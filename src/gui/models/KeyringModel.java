package gui.models;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.security.InvalidKeyException;
import java.util.HashSet;

import algorithms.AES;

/**
 * Model for the keyring function. It stores triples of Host-Username-Password into a HashSet.
 * @author Eugenio Severi
 */
public class KeyringModel extends HashSet<Triple<String, String, String>> implements IKeyringModel, Serializable {

	private static final long serialVersionUID = -4180040339126413292L;
	
	/*public KeyringModel() {
		super();
	}*/

	@Override
	public void saveData(final OutputStream outputFile, final byte[] aesKey) throws IOException, InvalidKeyException {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(this); // TODO: crash se l'hashset non è vuoto
		oos.flush();
		oos.close();
		final ByteArrayInputStream is = new ByteArrayInputStream(baos.toByteArray());
		final AES encryptor = new AES();
		encryptor.setSymmetricKeySpec(aesKey);
		encryptor.encode(is, outputFile);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public void loadData(final InputStream inputFile, final byte[] aesKey) throws IOException, InvalidKeyException, ClassNotFoundException {
		final ByteArrayOutputStream bois = new ByteArrayOutputStream();
		final AES decryptor = new AES();
		decryptor.setSymmetricKeySpec(aesKey);
		decryptor.decode(inputFile, bois);
		final ByteArrayInputStream bais = new ByteArrayInputStream(bois.toByteArray());
		final ObjectInputStream ois = new ObjectInputStream(bais);
		final HashSet<Triple<String, String, String>> readData = (HashSet<Triple<String, String, String>>) ois.readObject();
		this.clear();
		this.addAll(readData);
	}
	
	@Override
	public void addItem(final String host, final String username, final String password) {
		this.add(new Triple<String, String, String>(host, username, password));
	}
	
	@Override
	public void removeItem(final String host, final String username, final String password) {
		this.remove(new Triple<String, String, String>(host, username, password));
	}
	
}
