package com.dubaidrums.jems.util;

import java.net.MalformedURLException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.dubaidrums.jems.domain.JemsCurrency;
import com.dubaidrums.jems.domain.JemsRole;
import com.dubaidrums.jems.domain.JemsUser;
import com.dubaidrums.jems.service.JemsCurrencyService;
import com.dubaidrums.jems.service.JemsOrganizationService;
import com.dubaidrums.jems.service.JemsRegionService;
import com.dubaidrums.jems.service.JemsRoleService;
import com.dubaidrums.jems.service.JemsSearchService;
import com.dubaidrums.jems.service.JemsUserService;
import com.dubaidrums.jems.service.JemsUtilService;

@Component
public class Bootstrap implements ApplicationListener<ContextRefreshedEvent> {

	Logger log = LogManager.getLogger(Bootstrap.class);
	
	@Autowired
    JemsUserService jemsUserService;
	
	@Autowired
    JemsRoleService jemsRoleService;
	
	@Autowired
    JemsCurrencyService jemsCurrencyService;
	
	@Autowired
    JemsOrganizationService jemsOrganizationService;
	
	@Autowired
    JemsRegionService jemsRegionService;
	
	@Autowired
    JemsSearchService jemsSearchService;	
	
	@Autowired
    JemsUtilService jemsUtilService;
	
	@Transactional
	public void onApplicationEvent(ContextRefreshedEvent event) {
		
		if (event.getApplicationContext().getParent() == null) {
			log.info("####################");
			log.info("JEMS BootStrapping...");		
			log.info("Enviornment: "+System.getProperty("buildenv"));
			log.info("isDev: "+jemsUtilService.isDev());
			
			
			setupSearch();
			
			
//			if(jemsRoleService.countAllJemsRoles()==0){
//				jemsRoleService.saveJemsRole(new JemsRole("ROLE_ADMIN"));
//				jemsRoleService.saveJemsRole(new JemsRole("ROLE_OPERATOR"));
//				jemsRoleService.saveJemsRole(new JemsRole("ROLE_VIEWER"));
//			}
//			
//			if(jemsUserService.countAllJemsUsers()==0){
//				Set<JemsRole> roles;
//				
//				JemsUser u1 = new JemsUser();
//				u1.setUserName("admin");
//				u1.setFullName("Admin");
//				u1.setMobile("123");
//				u1.setEmail("admin@dubaidrums.com");				
//				u1.setPassword(jemsUserService.encrypt("admin"));
//				u1.setEnabled(true);				
//				roles = new HashSet<JemsRole>();			
//				roles.add(jemsRoleService.findByRoleName("ROLE_ADMIN"));
//				u1.setRoles(roles);
//				jemsUserService.saveJemsUser(u1);
//				
//				JemsUser u2 = new JemsUser();
//				u2.setUserName("operator");
//				u2.setFullName("Operator");
//				u2.setMobile("123");
//				u2.setEmail("operator@dubaidrums.com");				
//				u2.setPassword(jemsUserService.encrypt("operator"));
//				u2.setEnabled(true);				
//				roles = new HashSet<JemsRole>();			
//				roles.add(jemsRoleService.findByRoleName("ROLE_OPERATOR"));
//				u2.setRoles(roles);
//				jemsUserService.saveJemsUser(u2);
//				
//				JemsUser u3 = new JemsUser();
//				u3.setUserName("viewer");
//				u3.setFullName("Viewer");
//				u3.setMobile("123");
//				u3.setEmail("viewer@dubaidrums.com");				
//				u3.setPassword(jemsUserService.encrypt("viewer"));
//				u3.setEnabled(true);				
//				roles = new HashSet<JemsRole>();			
//				roles.add(jemsRoleService.findByRoleName("ROLE_VIEWER"));
//				u3.setRoles(roles);
//				jemsUserService.saveJemsUser(u3);				
//			}
			

		}
	}


	private void setupSearch() {
		log.info("Setting Up Search...");
		
		jemsSearchService.setupSearch();
		//long docCount = jemsSearchService.getDocumentCount();
		
		//log.info("Number of documents in search index: "+docCount);
	}
}