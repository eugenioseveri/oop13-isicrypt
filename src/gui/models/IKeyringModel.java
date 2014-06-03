package gui.models;

import java.io.InputStream;
import java.io.OutputStream;

public interface IKeyringModel {
	void saveData(OutputStream outputFile);
	void loadData(InputStream inputFile);
	void addItem(String host, String username, String password);
	void removeItem(String host, String username, String password);
	java.util.Iterator<Triple<String, String, String>> iterator();
}
