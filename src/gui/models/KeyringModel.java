package gui.models;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.HashSet;

public class KeyringModel extends HashSet<Triple<String, String, String>> implements IKeyringModel {

	private static final long serialVersionUID = -4180040339126413292L;
	
	public KeyringModel() {
		super();
	}
	
	@Override
	public void saveData(OutputStream outputFile) {
		try {
			new ObjectOutputStream(outputFile).writeObject(this);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public void loadData(InputStream inputFile) {
		HashSet<Triple<String, String, String>> readData = null;
		try {
			readData = (HashSet<Triple<String, String, String>>) new ObjectInputStream(inputFile).readObject();
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
