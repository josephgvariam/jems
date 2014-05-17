package com.dubaidrums.jems.messagelisteners;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class IndexMessageListener {
	Logger log = LogManager.getLogger(IndexMessageListener.class);
	
	public void onMessage(String message) {
		log.info("RECIEVED IndexMessageListener: "+message);
	}
}
