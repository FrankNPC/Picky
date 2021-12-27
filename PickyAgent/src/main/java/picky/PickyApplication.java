package picky;

import java.io.IOException;

import picky.net.PickyServer;

public class PickyApplication {

	public static void main(String[] args) {
		try {
			try {
				if (args.length>0) {
					Configuration.loadConfiguration(args[0]);
				}else {
					Configuration.getConfiguration();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			PickyServer.getInstance().start();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}
