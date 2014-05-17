package com.dubaidrums.jems.web;

import com.dubaidrums.jems.domain.JemsRegion;
import com.dubaidrums.jems.service.JemsCountryService;
import com.dubaidrums.jems.service.JemsRegionService;
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
@RequestMapping("/jemsregions")
@Controller
public class JemsRegionController {
	
	Logger log = LogManager.getLogger(JemsRegionController.class);

	@Autowired
    JemsRegionService jemsRegionService;

	@Autowired
    JemsCountryService jemsCountryService;

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid JemsRegion jemsRegion, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest, Principal principal) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, jemsRegion);
            return "jemsregions/create";
        }
        uiModel.asMap().clear();
        jemsRegionService.saveJemsRegion(jemsRegion);        
        
        //return "redirect:/jemsregions/" + encodeUrlPathSegment(jemsRegion.getId().toString(), httpServletRequest);
        log.info("user: "+principal.getName()+", method: create, regionId: "+jemsRegion.getId()+", msg: new region created");
        return "redirect:/jemsregions?msg=1";
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new JemsRegion());
        return "jemsregions/create";
    }

//	@RequestMapping(value = "/{id}", produces = "text/html")
//    public String show(@PathVariable("id") Long id, Model uiModel) {
//        uiModel.addAttribute("jemsregion", jemsRegionService.findJemsRegion(id));
//        uiModel.addAttribute("itemId", id);
//        return "jemsregions/show";
//    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "msg", required = false) Integer msg, Model uiModel, Principal principal) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("jemsregions", jemsRegionService.findJemsRegionEntries(firstResult, sizeNo));
            float nrOfPages = (float) jemsRegionService.countAllJemsRegions() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            if(msg!=null && msg==1){
            	uiModel.addAttribute("msg", "Region sucessfully saved!");
            }
            uiModel.addAttribute("jemsregions", jemsRegionService.findAllJemsRegions());
        }
        
        log.info("user: "+principal.getName()+", method: list, page: "+page+", msg: regions listed");
        
        return "jemsregions/list";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid JemsRegion jemsRegion, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest, Principal principal) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, jemsRegion);
            return "jemsregions/update";
        }
        uiModel.asMap().clear();
        jemsRegionService.updateJemsRegion(jemsRegion);        
        
        //return "redirect:/jemsregions/" + encodeUrlPathSegment(jemsRegion.getId().toString(), httpServletRequest);
        log.info("user: "+principal.getName()+", method: update, regionId: "+jemsRegion.getId()+", msg: region updated");
        return "redirect:/jemsregions?msg=1";
    }

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, jemsRegionService.findJemsRegion(id));
        return "jemsregions/update";
    }

//	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
//    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
//        JemsRegion jemsRegion = jemsRegionService.findJemsRegion(id);
//        jemsRegionService.deleteJemsRegion(jemsRegion);
//        uiModel.asMap().clear();
//        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
//        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
//        return "redirect:/jemsregions";
//    }

	void populateEditForm(Model uiModel, JemsRegion jemsRegion) {
        uiModel.addAttribute("jemsRegion", jemsRegion);
        uiModel.addAttribute("jemscountrys", jemsCountryService.findAllJemsCountrys());
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
//        JemsRegion jemsRegion = jemsRegionService.findJemsRegion(id);
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json; charset=utf-8");
//        if (jemsRegion == null) {
//            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
//        }
//        return new ResponseEntity<String>(jemsRegion.toJson(), headers, HttpStatus.OK);
//    }
//
//	@RequestMapping(headers = "Accept=application/json")
//    @ResponseBody
//    public ResponseEntity<String> listJson() {
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json; charset=utf-8");
//        List<JemsRegion> result = jemsRegionService.findAllJemsRegions();
//        return new ResponseEntity<String>(JemsRegion.toJsonArray(result), headers, HttpStatus.OK);
//    }
//
//	@RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
//    public ResponseEntity<String> createFromJson(@RequestBody String json) {
//        JemsRegion jemsRegion = JemsRegion.fromJsonToJemsRegion(json);
//        jemsRegionService.saveJemsRegion(jemsRegion);
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json");
//        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
//    }
//
//	@RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
//    public ResponseEntity<String> createFromJsonArray(@RequestBody String json) {
//        for (JemsRegion jemsRegion: JemsRegion.fromJsonArrayToJemsRegions(json)) {
//            jemsRegionService.saveJemsRegion(jemsRegion);
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
//        JemsRegion jemsRegion = JemsRegion.fromJsonToJemsRegion(json);
//        if (jemsRegionService.updateJemsRegion(jemsRegion) == null) {
//            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
//        }
//        return new ResponseEntity<String>(headers, HttpStatus.OK);
//    }
//
//	@RequestMapping(value = "/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
//    public ResponseEntity<String> updateFromJsonArray(@RequestBody String json) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json");
//        for (JemsRegion jemsRegion: JemsRegion.fromJsonArrayToJemsRegions(json)) {
//            if (jemsRegionService.updateJemsRegion(jemsRegion) == null) {
//                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
//            }
//        }
//        return new ResponseEntity<String>(headers, HttpStatus.OK);
//    }
//
//	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
//    public ResponseEntity<String> deleteFromJson(@PathVariable("id") Long id) {
//        JemsRegion jemsRegion = jemsRegionService.findJemsRegion(id);
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json");
//        if (jemsRegion == null) {
//            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
//        }
//        jemsRegionService.deleteJemsRegion(jemsRegion);
//        return new ResponseEntity<String>(headers, HttpStatus.OK);
//    }
}
