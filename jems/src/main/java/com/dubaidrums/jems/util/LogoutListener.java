package com.dubaidrums.jems.util;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;


public class LogoutListener implements HttpSessionListener {
	
	Logger log = LogManager.getLogger(LogoutListener.class);
	 
    @Override
    public void sessionCreated(HttpSessionEvent arg0) {
    	
    }
 
    @Override
    public void sessionDestroyed(HttpSessionEvent arg0) {
    	try {
			SecurityContext sc = (SecurityContext) arg0.getSession().getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
			log.info(sc.getAuthentication().getName()+" logged off!");
		} catch (Exception e) {
		}
    }
 
}