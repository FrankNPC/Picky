package picky;

import picky.net.PickyServer;

public class PickyApplication {

	public static void main(String[] args) {
		try {
			if (args.length>0) {
				Configuration.loadConfiguration(args[0]);
			}
			PickyServer.getInstance().start();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}
