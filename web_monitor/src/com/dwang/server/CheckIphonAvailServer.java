package com.dwang.server;

import com.dwang.IphoneAvailSelenium;

public class CheckIphonAvailServer {
	
	public static void main(String[] args) {

	     Runnable r = new Runnable() {
	         public void run() {
	             runBackgroundTask();
	         }
	     };

	     new Thread(r).start();
	     //this line will execute immediately, not waiting for your task to complete
	}

	protected static void runBackgroundTask() {
		IphoneAvailSelenium iphoneAvail = new IphoneAvailSelenium();
		
		while (iphoneAvail.getWebsiteAvailability()) {
			iphoneAvail.checkAll();
			try {
			    Thread.sleep(15000);                 //1000 milliseconds is one second.
			} catch(InterruptedException ex) {
			    Thread.currentThread().interrupt();
			}
		}
		
	}

}
