package com.dubaidrums.jems.web;

import java.security.Principal;
import java.util.Arrays;
import java.util.Date;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dubaidrums.jems.domain.JemsEventStatus;
import com.dubaidrums.jems.domain.JemsEventType;
import com.dubaidrums.jems.domain.JemsUser;
import com.dubaidrums.jems.service.JemsEventService;
import com.dubaidrums.jems.service.JemsSearchService;
import com.dubaidrums.jems.service.JemsUserService;

@RequestMapping("/search")
@Controller
public class JemsSearchController {

	Logger log = LogManager.getLogger(JemsSearchController.class);

	@Autowired
	JemsSearchService jemsSearchService;

	@Autowired
	JemsUserService jemsUserService;

	@Autowired
	JemsEventService jemsEventService;

	@RequestMapping(produces = "text/html")
	public String search(Model uiModel) {
		uiModel.addAttribute("jemseventstatuses",
				Arrays.asList(JemsEventStatus.values()));
		uiModel.addAttribute("jemseventtypes",
				Arrays.asList(JemsEventType.values()));
		return "search";
	}

	@RequestMapping(value = "/query", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> query(
			@RequestParam(value = "q", required = true) String q,
			@RequestParam(value = "chkdates", required = false) boolean chkdates,
			@RequestParam(value = "chkpaid", required = false) boolean chkpaid,
			@RequestParam(value = "chktype", required = false) boolean chktype,
			@RequestParam(value = "chkeventstatus", required = false) boolean chkeventstatus,

			@RequestParam(value = "startdatetime", required = false) Date startdatetime,
			@RequestParam(value = "enddatetime", required = false) Date enddatetime,
			@RequestParam(value = "paidstatus", required = false) boolean paidstatus,
			@RequestParam(value = "eventtype", required = false) String eventtype,
			@RequestParam(value = "eventstatus", required = false) String eventstatus,
			Principal principal) {

		// System.out.println("chkdates:"+chkdates);
		// System.out.println("chkpaid:"+chkpaid);
		// System.out.println("chktype:"+chktype);
		// System.out.println("chkeventstatus:"+chkeventstatus);
		// System.out.println("startdatetime:"+startdatetime);
		// System.out.println("enddatetime:"+enddatetime);
		// System.out.println("paidstatus:"+paidstatus);
		// System.out.println("eventtype:"+eventtype);
		// System.out.println("eventstatus:"+eventstatus);

		log.info("user: " + principal.getName() + ", method: query, q: " + q);

		JemsUser user = jemsUserService.findJemsUserByUserName(principal
				.getName());

		String results = jemsSearchService.query(q, chkdates, chkpaid, chktype,
				chkeventstatus, startdatetime, enddatetime, paidstatus,
				eventtype, eventstatus, user);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		return new ResponseEntity<String>(results, headers, HttpStatus.OK);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/createindex", produces = "text/html")
	public ResponseEntity<String> createIndex(Principal principal) {
		log.info("user: " + principal.getName()
				+ ", method: createIndex, msg: creating search index");

		jemsSearchService.createIndex(jemsEventService.findAllJemsEvents());

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		return new ResponseEntity<String>("Create Index", headers,
				HttpStatus.OK);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/deleteindex", produces = "text/html")
	public ResponseEntity<String> deleteIndex(Principal principal) {
		log.info("user: " + principal.getName()
				+ ", method: deleteIndex, msg: deleting search index");

		jemsSearchService.deleteIndex();

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		return new ResponseEntity<String>("Delete Index", headers,
				HttpStatus.OK);
	}

}
