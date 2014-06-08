package test;

import it.sauronsoftware.junique.AlreadyLockedException;
import it.sauronsoftware.junique.JUnique;
import gui.views.StartScreenView;

public class Main {

	public static void main(String[] args) {
		final String appId = "isiCrypt07062014";
		boolean alreadyRunning;
		try {
			JUnique.acquireLock(appId);
			alreadyRunning = false;
		} catch (AlreadyLockedException e) {
			alreadyRunning = true;
		}
		if (!alreadyRunning) {
			@SuppressWarnings("unused")
			StartScreenView view = new StartScreenView();
		}
	}

}
