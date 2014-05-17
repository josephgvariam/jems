package com.dubaidrums.jems.service.impl;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dubaidrums.jems.service.JemsMessageService;

@Service
@Transactional
public class JemsMessageServiceImpl implements JemsMessageService {

	Logger log = LogManager.getLogger(JemsMessageServiceImpl.class);

//	@Autowired
//    private AmqpTemplate template;
	
	public void sendMessage(String m){
//		try{
//			template.convertAndSend(m);
//		}catch(Exception e){
//			log.error("Error occured in sending message!", e);
//		}
	}
}
