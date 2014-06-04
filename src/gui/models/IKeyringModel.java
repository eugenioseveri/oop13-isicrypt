package gui.models;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidKeyException;

public interface IKeyringModel {
	void saveData(OutputStream outputFile, byte[] aesKey) throws IOException, InvalidKeyException;
	void loadData(InputStream inputFile, byte[] aesKey) throws IOException, InvalidKeyException, ClassNotFoundException;
	void addItem(String host, String username, String password);
	void removeItem(String host, String username, String password);
	java.util.Iterator<Triple<String, String, String>> iterator();
}
