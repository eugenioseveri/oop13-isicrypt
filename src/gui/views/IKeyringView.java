package gui.views;

import javax.swing.JTable;

import gui.controllers.IKeyringViewObserver;

public interface IKeyringView {
	void attachViewObserver(IKeyringViewObserver listener);
	JTable getTable();
}
