package com.dubaidrums.jems.service.impl;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dubaidrums.jems.service.JemsCacheService;

import java.io.IOException;

import javax.servlet.ServletContext;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.ConnectionFactoryBuilder;
import net.spy.memcached.auth.PlainCallbackHandler;
import net.spy.memcached.auth.AuthDescriptor;

@Service
@Transactional
public class JemsCacheServiceImpl implements JemsCacheService {
	
	Logger log = LogManager.getLogger(JemsCacheServiceImpl.class);
	
	@Autowired
	private ServletContext servletContext;
	
	MemcachedClient mc;
	
	public void setup(){
		String servers = servletContext.getInitParameter("MEMCACHIER_SERVERS");
	    String username = servletContext.getInitParameter("MEMCACHIER_USERNAME");
	    String password = servletContext.getInitParameter("MEMCACHIER_PASSWORD");
	    	
		AuthDescriptor ad = new AuthDescriptor(new String[] { "PLAIN" }, new PlainCallbackHandler(username, password));

		try {			
			mc = new MemcachedClient(new ConnectionFactoryBuilder().setProtocol(ConnectionFactoryBuilder.Protocol.BINARY).setAuthDescriptor(ad).build(), AddrUtil.getAddresses(servers));
		}catch (Exception ioe) {
			log.error("Couldn't create a connection to MemCachier!",ioe);
		}
	}
	
	public void set(String key, String val){
		mc.set(key, 3600, val);
	}
	
	public String get(String key){
		return (String) mc.get(key);
	}
	
}
