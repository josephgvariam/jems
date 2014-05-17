package com.dubaidrums.jems.web;

import com.dubaidrums.jems.domain.JemsUser;
import com.dubaidrums.jems.service.JemsOrganizationService;
import com.dubaidrums.jems.service.JemsRoleService;
import com.dubaidrums.jems.service.JemsUserService;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.List;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping("/jemsusers")
@Controller
public class JemsUserController {
	
	Logger log = LogManager.getLogger(JemsUserController.class);

	@Autowired
    JemsUserService jemsUserService;

	@Autowired
    JemsOrganizationService jemsOrganizationService;

	@Autowired
    JemsRoleService jemsRoleService;

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid JemsUser jemsUser, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest, Principal principal) {
		if(jemsUserService.findJemsUserByUserNameIgnoreCase(jemsUser.getUserName())!=null){
			bindingResult.rejectValue("userName", "user.exists");
		}
		
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, jemsUser);
            return "jemsusers/create";
        }
        uiModel.asMap().clear();
        jemsUser.setPassword(jemsUserService.encrypt(jemsUser.getPassword()));
        jemsUserService.saveJemsUser(jemsUser);
        //return "redirect:/jemsusers/" + encodeUrlPathSegment(jemsUser.getId().toString(), httpServletRequest);
        log.info("user: "+principal.getName()+", method: create, userId: "+jemsUser.getId()+", msg: new user created");
        return "redirect:/jemsusers?msg=1";
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new JemsUser());
        return "jemsusers/create";
    }

//	@RequestMapping(value = "/{id}", produces = "text/html")
//    public String show(@PathVariable("id") Long id, Model uiModel) {
//        uiModel.addAttribute("jemsuser", jemsUserService.findJemsUser(id));
//        uiModel.addAttribute("itemId", id);
//        return "jemsusers/show";
//    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "msg", required = false) Integer msg, Model uiModel, Principal principal) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("jemsusers", jemsUserService.findJemsUserEntries(firstResult, sizeNo));
            float nrOfPages = (float) jemsUserService.countAllJemsUsers() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
        	if(msg!=null && msg==1){
            	uiModel.addAttribute("msg", "User sucessfully saved!");
            }
            uiModel.addAttribute("jemsusers", jemsUserService.findAllJemsUsers());
        }
        log.info("user: "+principal.getName()+", method: list, page: "+page+", msg: users listed");
        return "jemsusers/list";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid JemsUser jemsUser, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest, Principal principal) {
		JemsUser u = jemsUserService.findJemsUserByUserNameIgnoreCase(jemsUser.getUserName());
		if(u!=null && !u.getId().equals(jemsUser.getId())){
			bindingResult.rejectValue("userName", "user.exists");
		}
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, jemsUser);
            return "jemsusers/update";
        }
        uiModel.asMap().clear();
        
        String oldEncryptedPasswd = jemsUserService.findJemsUser(jemsUser.getId()).getPassword();
    	String newEncryptedPasswd = jemsUser.getPassword();

    	if(!oldEncryptedPasswd.equals(newEncryptedPasswd)){
    		jemsUser.setPassword(jemsUserService.encrypt(jemsUser.getPassword()));
    	}
    		
        jemsUserService.updateJemsUser(jemsUser);
        //return "redirect:/jemsusers/" + encodeUrlPathSegment(jemsUser.getId().toString(), httpServletRequest);
        log.info("user: "+principal.getName()+", method: update, userId: "+jemsUser.getId()+", msg: user updated");
        return "redirect:/jemsusers?msg=1";
    }

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, jemsUserService.findJemsUser(id));
        return "jemsusers/update";
    }

//	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
//    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
//        JemsUser jemsUser = jemsUserService.findJemsUser(id);
//        jemsUserService.deleteJemsUser(jemsUser);
//        uiModel.asMap().clear();
//        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
//        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
//        return "redirect:/jemsusers";
//    }

	void populateEditForm(Model uiModel, JemsUser jemsUser) {
        uiModel.addAttribute("jemsUser", jemsUser);
        uiModel.addAttribute("jemsorganizations", jemsOrganizationService.findAllJemsOrganizations());
        uiModel.addAttribute("jemsroles", jemsRoleService.findAllJemsRoles());
    }

//	String encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
//        String enc = httpServletRequest.getCharacterEncoding();
//        if (enc == null) {
//            enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
//        }
//        try {
//            pathSegment = UriUtils.encodePathSegment(pathSegment, enc);
//        } catch (UnsupportedEncodingException uee) {}
//        return pathSegment;
//    }
//	
//	@RequestMapping(value = "/{id}", headers = "Accept=application/json")
//    @ResponseBody
//    public ResponseEntity<String> showJson(@PathVariable("id") Long id) {
//        JemsUser jemsUser = jemsUserService.findJemsUser(id);
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json; charset=utf-8");
//        if (jemsUser == null) {
//            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
//        }
//        return new ResponseEntity<String>(jemsUser.toJson(), headers, HttpStatus.OK);
//    }
//
//	@RequestMapping(headers = "Accept=application/json")
//    @ResponseBody
//    public ResponseEntity<String> listJson() {
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json; charset=utf-8");
//        List<JemsUser> result = jemsUserService.findAllJemsUsers();
//        return new ResponseEntity<String>(JemsUser.toJsonArray(result), headers, HttpStatus.OK);
//    }
//
//	@RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
//    public ResponseEntity<String> createFromJson(@RequestBody String json) {
//        JemsUser jemsUser = JemsUser.fromJsonToJemsUser(json);
//        jemsUserService.saveJemsUser(jemsUser);
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json");
//        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
//    }
//
//	@RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
//    public ResponseEntity<String> createFromJsonArray(@RequestBody String json) {
//        for (JemsUser jemsUser: JemsUser.fromJsonArrayToJemsUsers(json)) {
//            jemsUserService.saveJemsUser(jemsUser);
//        }
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json");
//        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
//    }
//
//	@RequestMapping(method = RequestMethod.PUT, headers = "Accept=application/json")
//    public ResponseEntity<String> updateFromJson(@RequestBody String json) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json");
//        JemsUser jemsUser = JemsUser.fromJsonToJemsUser(json);
//        if (jemsUserService.updateJemsUser(jemsUser) == null) {
//            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
//        }
//        return new ResponseEntity<String>(headers, HttpStatus.OK);
//    }
//
//	@RequestMapping(value = "/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
//    public ResponseEntity<String> updateFromJsonArray(@RequestBody String json) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json");
//        for (JemsUser jemsUser: JemsUser.fromJsonArrayToJemsUsers(json)) {
//            if (jemsUserService.updateJemsUser(jemsUser) == null) {
//                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
//            }
//        }
//        return new ResponseEntity<String>(headers, HttpStatus.OK);
//    }
//
//	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
//    public ResponseEntity<String> deleteFromJson(@PathVariable("id") Long id) {
//        JemsUser jemsUser = jemsUserService.findJemsUser(id);
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json");
//        if (jemsUser == null) {
//            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
//        }
//        jemsUserService.deleteJemsUser(jemsUser);
//        return new ResponseEntity<String>(headers, HttpStatus.OK);
//    }	
}
