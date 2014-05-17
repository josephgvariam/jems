package com.dubaidrums.jems.web;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


import com.dubaidrums.jems.domain.JemsClient;
import com.dubaidrums.jems.domain.JemsEvent;
import com.dubaidrums.jems.service.JemsClientService;
import com.dubaidrums.jems.service.JemsEventService;

import flexjson.JSONSerializer;

@RequestMapping("/jemsclients")
@Controller
public class JemsClientController {
	Logger log = LogManager.getLogger(JemsClientController.class);
	
	@Autowired
    JemsClientService jemsClientService;
	
	@Autowired
    JemsEventService jemsEventService;
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid JemsClient jemsClient, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest, Principal principal) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, jemsClient);
            return "jemsclients/create";
        }
        uiModel.asMap().clear();
        jemsClientService.saveJemsClient(jemsClient);
        //return "redirect:/jemsclients/" + encodeUrlPathSegment(jemsClient.getId().toString(), httpServletRequest);
        log.info("user: "+principal.getName()+", method: create, clientId: "+jemsClient.getId()+", msg: new client created");
        return "redirect:/jemsclients?msg=1";
    }

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new JemsClient());
        return "jemsclients/create";
    }

//	@RequestMapping(value = "/{id}", produces = "text/html")
//    public String show(@PathVariable("id") Long id, Model uiModel) {
//        uiModel.addAttribute("jemsclient", jemsClientService.findJemsClient(id));
//        uiModel.addAttribute("itemId", id);
//        return "jemsclients/show";
//    }

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "msg", required = false) Integer msg, Model uiModel, Principal principal) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("jemsclients", jemsClientService.findJemsClientEntries(firstResult, sizeNo));
            float nrOfPages = (float) jemsClientService.countAllJemsClients() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
        	if(msg!=null && msg==1){
            	uiModel.addAttribute("msg", "Client sucessfully saved!");
            }
            uiModel.addAttribute("jemsclients", jemsClientService.findAllJemsClients());
        }
        log.info("user: "+principal.getName()+", method: list, page: "+page+", msg: clients listed");
        return "jemsclients/list";
    }

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid JemsClient jemsClient, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest, Principal principal) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, jemsClient);
            return "jemsclients/update";
        }
        uiModel.asMap().clear();
        jemsClientService.updateJemsClient(jemsClient);
        //return "redirect:/jemsclients/" + encodeUrlPathSegment(jemsClient.getId().toString(), httpServletRequest);
        log.info("user: "+principal.getName()+", method: update, clientId: "+jemsClient.getId()+", msg: client updated");
        return "redirect:/jemsclients?msg=1";
    }

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, jemsClientService.findJemsClient(id));
        return "jemsclients/update";
    }

//	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
//    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
//        JemsClient jemsClient = jemsClientService.findJemsClient(id);
//        jemsClientService.deleteJemsClient(jemsClient);
//        uiModel.asMap().clear();
//        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
//        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
//        return "redirect:/jemsclients";
//    }

	void populateEditForm(Model uiModel, JemsClient jemsClient) {
        uiModel.addAttribute("jemsClient", jemsClient);        
    }	
	
	
//	@PreAuthorize("hasRole('ROLE_ADMIN')")
//	@RequestMapping(method = RequestMethod.GET, value = "/fixstaff", produces = "text/html")
//    public ResponseEntity<String> fixstaff(Principal principal) {
//		log.info("Fixing Clients...");
//		
//		List<JemsEvent> events = jemsEventService.findAllJemsEvents();
//		
//		for (JemsEvent e : events) {
//		}
//		
//		log.info("Clients fixed successfully...");
//        
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json; charset=utf-8");
//        return new ResponseEntity<String>("Fix Staff", headers, HttpStatus.OK);
//    }		
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(method = RequestMethod.GET, value = "/jems-clients", produces = "text/csv")
    public ResponseEntity<String> downloadcsv(Principal principal) {		
		
		List<JemsClient> clients = jemsClientService.findAllJemsClients();
		
		StringBuilder export = new StringBuilder("Company,Contact Person,Phone,Email,Address,Events\n");
		for (JemsClient c : clients) {
			export
			.append("\""+c.getCompany()+"\"")
			.append(",")
			.append("\""+c.getContactPerson()+"\"")
			.append(",")
			.append("\""+c.getPhone()+"\"")
			.append(",")
			.append("\""+c.getEmail()+"\"")
			.append(",")
			.append("\""+c.getAddress()+"\"")
			.append(",")
			.append("\""+c.getEvents().size()+"\"")
			.append("\n")
			;
		}
		
		log.info("Downloading Clients as CSV! csv-length: "+export.length());
        
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "text/csv; charset=utf-8");
        return new ResponseEntity<String>(export.toString(), headers, HttpStatus.OK);
    }	
	
	@RequestMapping(value = "listjson")
	@ResponseBody
	public ResponseEntity<String> listJson(@RequestParam(value = "q", required = false) String q, Principal principal) {
		//log.info("user: " + principal.getName()+ ", method: listJson, query: "+q);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		List<JemsClient> clients = jemsClientService.findAllJemsClients();
		
		List<Map> results = new ArrayList<Map>();
		
		if(q!=null && q.length()!=0){
			for (JemsClient c : clients) {			
				if(c.getCompany().toLowerCase().indexOf(q.toLowerCase())>-1){
					Map<String,Object> m = new HashMap<String,Object>();
					m.put("value", c.getCompany());
					m.put("clientId", c.getId().toString());
					m.put("address", c.getAddress());
					m.put("contactPerson", c.getContactPerson());
					m.put("email", c.getEmail());
					m.put("phone", c.getPhone());				
					m.put("tokens", new String[]{c.getCompany()});				
					results.add(m);
				}
			}
		}else{
			for (JemsClient c : clients) {				
				Map<String,Object> m = new HashMap<String,Object>();
				m.put("value", c.getCompany());
				m.put("clientId", c.getId().toString());
				m.put("address", c.getAddress());
				m.put("contactPerson", c.getContactPerson());
				m.put("email", c.getEmail());
				m.put("phone", c.getPhone());				
				m.put("tokens", new String[]{c.getCompany()});				
				results.add(m);
			}			
		}
		

		
		return new ResponseEntity<String>(new JSONSerializer().exclude("*.class").serialize(results), headers, HttpStatus.OK);
	}

}
