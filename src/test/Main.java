package test;

import it.sauronsoftware.junique.AlreadyLockedException;
import it.sauronsoftware.junique.JUnique;
import gui.views.StartScreenView;

public class Main {

	private static final String appId = "isiCrypt07062014";
	
	public static void main(String[] args) {
		
		boolean alreadyRunning;
		try {
			JUnique.acquireLock(appId);
			alreadyRunning = false;
		} catch (AlreadyLockedException e) {
			alreadyRunning = true;
		}
		if (!alreadyRunning) {
			new StartScreenView();
		}
	}

}
