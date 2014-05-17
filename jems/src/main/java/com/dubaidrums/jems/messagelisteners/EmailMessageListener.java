package com.dubaidrums.jems.messagelisteners;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class EmailMessageListener {
	Logger log = LogManager.getLogger(EmailMessageListener.class);
	
	public void onMessage(String message) {
		log.info("RECIEVED EmailMessageListener: "+message);
	}
}
