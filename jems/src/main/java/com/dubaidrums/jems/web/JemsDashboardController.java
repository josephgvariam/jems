package com.dubaidrums.jems.web;

import java.security.Principal;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dubaidrums.jems.service.JemsEventService;
import com.dubaidrums.jems.service.JemsOrganizationService;


@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER')")
@RequestMapping("/dashboard")
@Controller
public class JemsDashboardController {

	Logger log = LogManager.getLogger(JemsCurrencyController.class);
	
	@Autowired
    JemsOrganizationService jemsOrganizationService;
	
	@Autowired
    JemsEventService jemsEventService;
	
	@RequestMapping(produces = "text/html")
    public String dashboard(Model uiModel, Principal principal) {
        uiModel.addAttribute("jemsorganizations", jemsOrganizationService.findActiveJemsOrganizations());
        
        log.info("user: "+principal.getName()+", method: dashboard, msg: view dashboard");
        
        return "dashboard";
    }

}
