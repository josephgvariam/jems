package com.dubaidrums.jems.web;

import java.security.Principal;
import java.util.Set;

import org.apache.commons.lang3.text.WordUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dubaidrums.jems.domain.JemsOrganization;
import com.dubaidrums.jems.domain.JemsUser;
import com.dubaidrums.jems.service.JemsOrganizationService;
import com.dubaidrums.jems.service.JemsUserService;

import flexjson.JSONSerializer;

@PreAuthorize("isAuthenticated()")
@RequestMapping("/calendar")
@Controller
public class JemsCalendarController {

	Logger log = LogManager.getLogger(JemsCalendarController.class);
	
	@Autowired
    JemsUserService jemsUserService;	
	
	@RequestMapping(produces = "text/html")
	public String showCalendar(Model uiModel, Principal principal) {
		JemsUser user = jemsUserService.findJemsUserByUserName(principal.getName());
		
		Set<JemsOrganization> orgs = user.getOrganizations();
		for (JemsOrganization jo : orgs) {
			jo.setName(getShortOrgName(jo));
		}
		String json = new JSONSerializer().exclude("*.class").exclude("active").exclude("adminEmail").exclude("country").exclude("currency").exclude("defaultGps").exclude("region").exclude("version").serialize(orgs);
		
        uiModel.addAttribute("orgsjson", json);        
        log.info("user: "+principal.getName()+", method: showCalendar");
        
        return "calendar";
    }
	
	private String getShortOrgName(JemsOrganization o){
        if (o.getId() == 1) {
            return "JE";
        }
        if (o.getId() == 3) {
            return "UT ";
        }
        if (o.getId() == 4) {
            return "FMD";
        }
        return WordUtils.initials(o.getName());
	}

}
