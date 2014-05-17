package com.dubaidrums.jems.web;

import com.dubaidrums.jems.domain.JemsEvent;
import com.dubaidrums.jems.domain.JemsQuotation;
import com.dubaidrums.jems.domain.JemsUser;
import com.dubaidrums.jems.service.JemsEmailService;
import com.dubaidrums.jems.service.JemsEventService;
import com.dubaidrums.jems.service.JemsLogService;
import com.dubaidrums.jems.service.JemsQuotationService;
import com.dubaidrums.jems.service.JemsReportingService;
import com.dubaidrums.jems.service.JemsUserService;
import com.dubaidrums.jems.service.JemsUtilService;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.FileSystemResource;
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
@RequestMapping("/jemsquotations")
@Controller
public class JemsQuotationController {
	
	Logger log = LogManager.getLogger(JemsQuotationController.class);
	
	@Autowired
    JemsEventService jemsEventService;

	@Autowired
    JemsQuotationService jemsQuotationService;
	
	@Autowired
    JemsUtilService jemsUtilService;	
	
	@Autowired
	JemsReportingService jemsReportingService;	
	
	@Autowired
    JemsLogService jemsLogService;
	
	@Autowired
    JemsEmailService jemsEmailService;	
	
	@Autowired
    JemsUserService jemsUserService;
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER','ROLE_OPERATOR')")
	@RequestMapping(value = "/{id}/pdf", method = RequestMethod.GET, produces = "application/pdf")
	public String generatePdf(@PathVariable("id") Long id, HttpServletRequest httpServletRequest, Principal principal){
		JemsQuotation jq = jemsQuotationService.findJemsQuotation(id);
		try {			
			log.info("user: "+principal.getName()+", method: generatePdf, quotationId: "+id+", msg: generating quotation pdf");
			String fileName = jemsReportingService.generateQuotationPdf(jq);
			
			jemsLogService.newLog(principal.getName()+" generated a PDF quotation: <a href='/jemsquotations/"+id+"'>"+jq.getQNumber()+"</a> for event: <a href='/jemsevents/"+jq.getJemsEvent().getId()+"'>"+jq.getJemsEvent().getTitle()+"</a>");
			jemsEmailService.sendEmail("PDF Quotation Generated: "+jq.getJemsEvent().getTitle(), principal.getName()+" generated a PDF quotation: <a href='/jemsquotations/"+id+"'>"+jq.getQNumber()+"</a> for event: <a href='/jemsevents/"+jq.getJemsEvent().getId()+"'>"+jq.getJemsEvent().getTitle()+"</a>", fileName);
			
			return "redirect:/jemsquotations/pdf/" + encodeUrlPathSegment(fileName, httpServletRequest);
		} catch (Exception e) {
			log.error("Unable to generate PDF!", e);
			throw new RuntimeException("Unable to generate PDF!", e);
		}
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER','ROLE_OPERATOR')")
	@RequestMapping(value = "/pdf/{fileName}", method = RequestMethod.GET, produces = "application/pdf")
	@ResponseBody
	public FileSystemResource getPdfFile(@PathVariable("fileName") String fileName){	
		return new FileSystemResource(System.getProperty("java.io.tmpdir")+"/"+fileName+".pdf");		
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER','ROLE_OPERATOR')")
	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid JemsQuotation jemsQuotation, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest, Principal principal) {
		if(jemsQuotation.getQDate()==null){
			bindingResult.rejectValue("qDate", "date.invalid");
		}
		
        if (bindingResult.hasErrors()) {
        	log.info(bindingResult);
            populateEditForm(uiModel, jemsQuotation);
            return "jemsquotations/create";
        }
        uiModel.asMap().clear();
                
        JemsUser user = jemsUserService.findJemsUserByUserName(principal.getName());
        
        jemsQuotation.setActive(true);
        Date nd = new Date();
        jemsQuotation.setCreatedDate(nd);
        jemsQuotation.setCreatedUser(user);
        jemsQuotation.setModifiedDate(nd);
        jemsQuotation.setModifiedUser(user);
        jemsQuotationService.saveNewJemsQuotation(jemsQuotation);
        
        
        JemsEvent je = jemsQuotation.getJemsEvent();
        jemsLogService.newLog(principal.getName()+" created a new quotation: <a href='/jemsquotations/"+jemsQuotation.getId()+"'>"+jemsQuotation.getQNumber()+"</a> for event: <a href='/jemsevents/"+je.getId()+"'>"+je.getTitle()+"</a>");
        jemsEmailService.sendEmail("Quotation Created: "+je.getTitle(), principal.getName()+" created a new quotation: <a href='/jemsquotations/"+jemsQuotation.getId()+"'>"+jemsQuotation.getQNumber()+"</a> for event: <a href='/jemsevents/"+je.getId()+"'>"+je.getTitle()+"</a>", null);
        log.info("user: "+principal.getName()+", method: create, eventId: "+je.getId()+", quotationId: "+jemsQuotation.getId()+", quotationNumber: "+jemsQuotation.getQNumber()+", msg: new quotation created");
        
        return "redirect:/jemsquotations/" + encodeUrlPathSegment(jemsQuotation.getId().toString(), httpServletRequest)+"?msg=1";
    }

	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER','ROLE_OPERATOR')")
	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(@RequestParam(value="jemsEventId", required = true) Long jemsEventId, Model uiModel) {
		JemsEvent je = jemsEventService.findJemsEvent(jemsEventId);
		JemsQuotation jq = new JemsQuotation();
		jq.setJemsEvent(je);
		jq.setQNumber(1);
		jq.setQDate(jemsUtilService.getCurrentDate());
		jq.setBillTo(jemsEventService.getBillTo(je));
		jq.setEventDetails(jemsEventService.getEventDetails(je));
		jq.setPaymentTerms("Full Amount on day of event.\nCancellation 3 days prior to event 100% payment required.\nIn the event of cancellation 50% due if 1 week before the event. 25% if 2 weeks prior.\nNo charge for cancellation over two weeks prior to the event.");
		
        populateEditForm(uiModel, jq);
        //uiModel.addAttribute("jemsEventId", jemsEventId);
        
        return "jemsquotations/create";
    }

	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER','ROLE_OPERATOR')")
	@RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, @RequestParam(value = "msg", required = false) Integer msg, Model uiModel, Principal principal) {
		JemsQuotation jq = jemsQuotationService.findJemsQuotation(id);
        uiModel.addAttribute("jemsquotation", jq);
        //uiModel.addAttribute("jemsEventId", jemsEventId);
        
        if(jq.isActive()){
        	uiModel.addAttribute("previousJemsQuotations", jemsQuotationService.findByActiveAndJemsEvent(false, jq.getJemsEvent()));
        }
        
        if(msg!=null && msg==1){
        	uiModel.addAttribute("msg", "Quotation sucessfully saved!");
        }
        
        log.info("user: "+principal.getName()+", method: show, eventId: "+jq.getJemsEvent().getId()+", quotationId: "+id+", quotationNumber: "+jq.getQNumber()+", msg: quotation showed");
        
        return "jemsquotations/show";
    }

//	@RequestMapping(produces = "text/html")
//    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
//        if (page != null || size != null) {
//            int sizeNo = size == null ? 10 : size.intValue();
//            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
//            uiModel.addAttribute("jemsquotations", jemsQuotationService.findJemsQuotationEntries(firstResult, sizeNo));
//            float nrOfPages = (float) jemsQuotationService.countAllJemsQuotations() / sizeNo;
//            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
//        } else {
//            uiModel.addAttribute("jemsquotations", jemsQuotationService.findAllJemsQuotations());
//        }
//        //addDateTimeFormatPatterns(uiModel);
//        return "jemsquotations/list";
//    }

	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER','ROLE_OPERATOR')")
	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid JemsQuotation jemsQuotation, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest, Principal principal) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, jemsQuotation);
            return "jemsquotations/update";
        }
        uiModel.asMap().clear();
        jemsQuotation.setModifiedUser(jemsUserService.findJemsUserByUserName(principal.getName()));
        jemsQuotation = jemsQuotationService.updateJemsQuotation(jemsQuotation);
        
        JemsEvent je = jemsQuotation.getJemsEvent();
        jemsLogService.newLog(principal.getName()+" updated a quotation: <a href='/jemsquotations/"+jemsQuotation.getId()+"'>"+jemsQuotation.getQNumber()+"</a> for event: <a href='/jemsevents/"+je.getId()+"'>"+je.getTitle()+"</a>");
        jemsEmailService.sendEmail("Quotation Updated: "+je.getTitle(), principal.getName()+" updated a quotation: <a href='/jemsquotations/"+jemsQuotation.getId()+"?jemsEventId="+je.getId()+"'>"+jemsQuotation.getQNumber()+"</a> for event: <a href='/jemsevents/"+je.getId()+"'>"+je.getTitle()+"</a>", null);
        log.info("user: "+principal.getName()+", method: update, eventId: "+je.getId()+", quotationId: "+jemsQuotation.getId()+", quotationNumber: "+jemsQuotation.getQNumber()+", msg: quotation updated");
        
        
        uiModel.addAttribute("msg", 1);
        return "redirect:/jemsquotations/" + encodeUrlPathSegment(jemsQuotation.getId().toString(), httpServletRequest);
    }

	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER','ROLE_OPERATOR')")
	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, jemsQuotationService.findJemsQuotation(id));
        //uiModel.addAttribute("jemsEventId", jemsEventId);
        return "jemsquotations/update";
    }

//	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
//    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
//        JemsQuotation jemsQuotation = jemsQuotationService.findJemsQuotation(id);
//        jemsQuotationService.deleteJemsQuotation(jemsQuotation);
//        uiModel.asMap().clear();
//        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
//        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
//        return "redirect:/jemsquotations";
//    }


	void populateEditForm(Model uiModel, JemsQuotation jemsQuotation) {
        uiModel.addAttribute("jemsQuotation", jemsQuotation);
    }

	String encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
        String enc = httpServletRequest.getCharacterEncoding();
        if (enc == null) {
            enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
        }
        try {
            pathSegment = UriUtils.encodePathSegment(pathSegment, enc);
        } catch (UnsupportedEncodingException uee) {}
        return pathSegment;
    }

//	@RequestMapping(value = "/{id}", headers = "Accept=application/json")
//    @ResponseBody
//    public ResponseEntity<String> showJson(@PathVariable("id") Long id) {
//        JemsQuotation jemsQuotation = jemsQuotationService.findJemsQuotation(id);
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json; charset=utf-8");
//        if (jemsQuotation == null) {
//            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
//        }
//        return new ResponseEntity<String>(jemsQuotation.toJson(), headers, HttpStatus.OK);
//    }
//
//	@RequestMapping(headers = "Accept=application/json")
//    @ResponseBody
//    public ResponseEntity<String> listJson() {
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json; charset=utf-8");
//        List<JemsQuotation> result = jemsQuotationService.findAllJemsQuotations();
//        return new ResponseEntity<String>(JemsQuotation.toJsonArray(result), headers, HttpStatus.OK);
//    }
//
//	@RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
//    public ResponseEntity<String> createFromJson(@RequestBody String json) {
//        JemsQuotation jemsQuotation = JemsQuotation.fromJsonToJemsQuotation(json);
//        jemsQuotationService.saveJemsQuotation(jemsQuotation);
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json");
//        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
//    }
//
//	@RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
//    public ResponseEntity<String> createFromJsonArray(@RequestBody String json) {
//        for (JemsQuotation jemsQuotation: JemsQuotation.fromJsonArrayToJemsQuotations(json)) {
//            jemsQuotationService.saveJemsQuotation(jemsQuotation);
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
//        JemsQuotation jemsQuotation = JemsQuotation.fromJsonToJemsQuotation(json);
//        if (jemsQuotationService.updateJemsQuotation(jemsQuotation) == null) {
//            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
//        }
//        return new ResponseEntity<String>(headers, HttpStatus.OK);
//    }
//
//	@RequestMapping(value = "/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
//    public ResponseEntity<String> updateFromJsonArray(@RequestBody String json) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json");
//        for (JemsQuotation jemsQuotation: JemsQuotation.fromJsonArrayToJemsQuotations(json)) {
//            if (jemsQuotationService.updateJemsQuotation(jemsQuotation) == null) {
//                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
//            }
//        }
//        return new ResponseEntity<String>(headers, HttpStatus.OK);
//    }
//
//	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
//    public ResponseEntity<String> deleteFromJson(@PathVariable("id") Long id) {
//        JemsQuotation jemsQuotation = jemsQuotationService.findJemsQuotation(id);
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json");
//        if (jemsQuotation == null) {
//            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
//        }
//        jemsQuotationService.deleteJemsQuotation(jemsQuotation);
//        return new ResponseEntity<String>(headers, HttpStatus.OK);
//    }
	

}
