package com.dubaidrums.jems.web;

import com.dubaidrums.jems.domain.JemsCurrency;
import com.dubaidrums.jems.service.JemsCurrencyService;
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
@RequestMapping("/jemscurrencys")
@Controller
public class JemsCurrencyController {
	
	Logger log = LogManager.getLogger(JemsCurrencyController.class);

	@Autowired
    JemsCurrencyService jemsCurrencyService;

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid JemsCurrency jemsCurrency, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest, Principal principal) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, jemsCurrency);
            return "jemscurrencys/create";
        }
        uiModel.asMap().clear();
        jemsCurrencyService.saveJemsCurrency(jemsCurrency);
        //return "redirect:/jemscurrencys/" + encodeUrlPathSegment(jemsCurrency.getId().toString(), httpServletRequest);
        log.info("user: "+principal.getName()+", method: create, currencyId: "+jemsCurrency.getId()+", msg: new currency created");
        return "redirect:/jemscurrencys?msg=1";
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new JemsCurrency());
        return "jemscurrencys/create";
    }

//	@RequestMapping(value = "/{id}", produces = "text/html")
//    public String show(@PathVariable("id") Long id, Model uiModel) {
//        uiModel.addAttribute("jemscurrency", jemsCurrencyService.findJemsCurrency(id));
//        uiModel.addAttribute("itemId", id);
//        return "jemscurrencys/show";
//    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "msg", required = false) Integer msg, Model uiModel, Principal principal) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("jemscurrencys", jemsCurrencyService.findJemsCurrencyEntries(firstResult, sizeNo));
            float nrOfPages = (float) jemsCurrencyService.countAllJemsCurrencys() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
        	if(msg!=null && msg==1){
            	uiModel.addAttribute("msg", "Currency sucessfully saved!");
            }
            uiModel.addAttribute("jemscurrencys", jemsCurrencyService.findAllJemsCurrencys());
        }
        log.info("user: "+principal.getName()+", method: list, page: "+page+", msg: currencies listed");
        return "jemscurrencys/list";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid JemsCurrency jemsCurrency, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest, Principal principal) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, jemsCurrency);
            return "jemscurrencys/update";
        }
        uiModel.asMap().clear();
        jemsCurrencyService.updateJemsCurrency(jemsCurrency);
        //return "redirect:/jemscurrencys/" + encodeUrlPathSegment(jemsCurrency.getId().toString(), httpServletRequest);
        log.info("user: "+principal.getName()+", method: update, currencyId: "+jemsCurrency.getId()+", msg: currency updated");
        return "redirect:/jemscurrencys?msg=1";
    }

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, jemsCurrencyService.findJemsCurrency(id));
        return "jemscurrencys/update";
    }

//	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
//    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
//        JemsCurrency jemsCurrency = jemsCurrencyService.findJemsCurrency(id);
//        jemsCurrencyService.deleteJemsCurrency(jemsCurrency);
//        uiModel.asMap().clear();
//        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
//        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
//        return "redirect:/jemscurrencys";
//    }

	void populateEditForm(Model uiModel, JemsCurrency jemsCurrency) {
        uiModel.addAttribute("jemsCurrency", jemsCurrency);
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
//        JemsCurrency jemsCurrency = jemsCurrencyService.findJemsCurrency(id);
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json; charset=utf-8");
//        if (jemsCurrency == null) {
//            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
//        }
//        return new ResponseEntity<String>(jemsCurrency.toJson(), headers, HttpStatus.OK);
//    }
//
//	@RequestMapping(headers = "Accept=application/json")
//    @ResponseBody
//    public ResponseEntity<String> listJson() {
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json; charset=utf-8");
//        List<JemsCurrency> result = jemsCurrencyService.findAllJemsCurrencys();
//        return new ResponseEntity<String>(JemsCurrency.toJsonArray(result), headers, HttpStatus.OK);
//    }
//
//	@RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
//    public ResponseEntity<String> createFromJson(@RequestBody String json) {
//        JemsCurrency jemsCurrency = JemsCurrency.fromJsonToJemsCurrency(json);
//        jemsCurrencyService.saveJemsCurrency(jemsCurrency);
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json");
//        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
//    }
//
//	@RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
//    public ResponseEntity<String> createFromJsonArray(@RequestBody String json) {
//        for (JemsCurrency jemsCurrency: JemsCurrency.fromJsonArrayToJemsCurrencys(json)) {
//            jemsCurrencyService.saveJemsCurrency(jemsCurrency);
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
//        JemsCurrency jemsCurrency = JemsCurrency.fromJsonToJemsCurrency(json);
//        if (jemsCurrencyService.updateJemsCurrency(jemsCurrency) == null) {
//            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
//        }
//        return new ResponseEntity<String>(headers, HttpStatus.OK);
//    }
//
//	@RequestMapping(value = "/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
//    public ResponseEntity<String> updateFromJsonArray(@RequestBody String json) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json");
//        for (JemsCurrency jemsCurrency: JemsCurrency.fromJsonArrayToJemsCurrencys(json)) {
//            if (jemsCurrencyService.updateJemsCurrency(jemsCurrency) == null) {
//                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
//            }
//        }
//        return new ResponseEntity<String>(headers, HttpStatus.OK);
//    }
//
//	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
//    public ResponseEntity<String> deleteFromJson(@PathVariable("id") Long id) {
//        JemsCurrency jemsCurrency = jemsCurrencyService.findJemsCurrency(id);
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json");
//        if (jemsCurrency == null) {
//            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
//        }
//        jemsCurrencyService.deleteJemsCurrency(jemsCurrency);
//        return new ResponseEntity<String>(headers, HttpStatus.OK);
//    }	
}
