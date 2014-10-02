package com.dubaidrums.jems.util;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.dubaidrums.jems.service.JemsSearchService;

@Component
public class Bootstrap implements ApplicationListener<ContextRefreshedEvent> {

	Logger log = LogManager.getLogger(Bootstrap.class);

	@Autowired
	JemsSearchService jemsSearchService;

	@Transactional
	public void onApplicationEvent(ContextRefreshedEvent event) {

		if (event.getApplicationContext().getParent() == null) {
			log.info("####################");
			log.info("JEMS BootStrapping...");

			setupSearch();
		}
	}

	private void setupSearch() {
		log.info("Setting Up Search...");
		jemsSearchService.setupSearch();
	}
}