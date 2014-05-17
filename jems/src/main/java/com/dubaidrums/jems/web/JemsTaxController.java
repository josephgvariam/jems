package com.dubaidrums.jems.web;

import com.dubaidrums.jems.domain.JemsTax;
import com.dubaidrums.jems.repository.JemsTaxRepository;
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
@RequestMapping("/jemstaxes")
@Controller
public class JemsTaxController {
	
	Logger log = LogManager.getLogger(JemsTaxController.class);

	@Autowired
    JemsTaxRepository jemsTaxRepository;

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid JemsTax jemsTax, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest, Principal principal) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, jemsTax);
            return "jemstaxes/create";
        }
        uiModel.asMap().clear();
        jemsTaxRepository.save(jemsTax);
        //return "redirect:/jemstaxes/" + encodeUrlPathSegment(jemsTax.getId().toString(), httpServletRequest);
        log.info("user: "+principal.getName()+", method: create, taxId: "+jemsTax.getId()+", msg: new tax created");
        return "redirect:/jemstaxes?msg=1";
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new JemsTax());
        return "jemstaxes/create";
    }

//	@RequestMapping(value = "/{id}", produces = "text/html")
//    public String show(@PathVariable("id") Long id, Model uiModel) {
//        uiModel.addAttribute("jemstax", jemsTaxRepository.findOne(id));
//        uiModel.addAttribute("itemId", id);
//        return "jemstaxes/show";
//    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "msg", required = false) Integer msg, Model uiModel, Principal principal) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("jemstaxes", jemsTaxRepository.findAll(new org.springframework.data.domain.PageRequest(firstResult / sizeNo, sizeNo)).getContent());
            float nrOfPages = (float) jemsTaxRepository.count() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
        	if(msg!=null && msg==1){
            	uiModel.addAttribute("msg", "Tax sucessfully saved!");
            }
            uiModel.addAttribute("jemstaxes", jemsTaxRepository.findAll());
        }
        log.info("user: "+principal.getName()+", method: list, page: "+page+", msg: taxes listed");
        return "jemstaxes/list";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid JemsTax jemsTax, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest, Principal principal) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, jemsTax);
            return "jemstaxes/update";
        }
        uiModel.asMap().clear();
        jemsTaxRepository.save(jemsTax);
        //return "redirect:/jemstaxes/" + encodeUrlPathSegment(jemsTax.getId().toString(), httpServletRequest);
        log.info("user: "+principal.getName()+", method: update, taxId: "+jemsTax.getId()+", msg: tax updated");
        return "redirect:/jemstaxes?msg=1";
    }

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, jemsTaxRepository.findOne(id));
        return "jemstaxes/update";
    }

//	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
//    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
//        JemsTax jemsTax = jemsTaxRepository.findOne(id);
//        jemsTaxRepository.delete(jemsTax);
//        uiModel.asMap().clear();
//        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
//        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
//        return "redirect:/jemstaxes";
//    }

	void populateEditForm(Model uiModel, JemsTax jemsTax) {
        uiModel.addAttribute("jemsTax", jemsTax);
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
//        JemsTax jemsTax = jemsTaxRepository.findOne(id);
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json; charset=utf-8");
//        if (jemsTax == null) {
//            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
//        }
//        return new ResponseEntity<String>(jemsTax.toJson(), headers, HttpStatus.OK);
//    }
//
//	@RequestMapping(headers = "Accept=application/json")
//    @ResponseBody
//    public ResponseEntity<String> listJson() {
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json; charset=utf-8");
//        List<JemsTax> result = jemsTaxRepository.findAll();
//        return new ResponseEntity<String>(JemsTax.toJsonArray(result), headers, HttpStatus.OK);
//    }
//
//	@RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
//    public ResponseEntity<String> createFromJson(@RequestBody String json) {
//        JemsTax jemsTax = JemsTax.fromJsonToJemsTax(json);
//        jemsTaxRepository.save(jemsTax);
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json");
//        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
//    }
//
//	@RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
//    public ResponseEntity<String> createFromJsonArray(@RequestBody String json) {
//        for (JemsTax jemsTax: JemsTax.fromJsonArrayToJemsTaxes(json)) {
//            jemsTaxRepository.save(jemsTax);
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
//        JemsTax jemsTax = JemsTax.fromJsonToJemsTax(json);
//        if (jemsTaxRepository.save(jemsTax) == null) {
//            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
//        }
//        return new ResponseEntity<String>(headers, HttpStatus.OK);
//    }
//
//	@RequestMapping(value = "/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
//    public ResponseEntity<String> updateFromJsonArray(@RequestBody String json) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json");
//        for (JemsTax jemsTax: JemsTax.fromJsonArrayToJemsTaxes(json)) {
//            if (jemsTaxRepository.save(jemsTax) == null) {
//                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
//            }
//        }
//        return new ResponseEntity<String>(headers, HttpStatus.OK);
//    }
//
//	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
//    public ResponseEntity<String> deleteFromJson(@PathVariable("id") Long id) {
//        JemsTax jemsTax = jemsTaxRepository.findOne(id);
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json");
//        if (jemsTax == null) {
//            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
//        }
//        jemsTaxRepository.delete(jemsTax);
//        return new ResponseEntity<String>(headers, HttpStatus.OK);
//    }	
}
