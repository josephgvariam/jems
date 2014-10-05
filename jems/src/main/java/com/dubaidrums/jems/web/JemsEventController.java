package com.dubaidrums.jems.web;

import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Page;
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

import com.dubaidrums.jems.domain.JemsCostingItem;
import com.dubaidrums.jems.domain.JemsCountry;
import com.dubaidrums.jems.domain.JemsCurrency;
import com.dubaidrums.jems.domain.JemsEvent;
import com.dubaidrums.jems.domain.JemsEventStatus;
import com.dubaidrums.jems.domain.JemsEventType;
import com.dubaidrums.jems.domain.JemsInvoice;
import com.dubaidrums.jems.domain.JemsLog;
import com.dubaidrums.jems.domain.JemsOrganization;
import com.dubaidrums.jems.domain.JemsPaymentMethod;
import com.dubaidrums.jems.domain.JemsQuotation;
import com.dubaidrums.jems.domain.JemsRegion;
import com.dubaidrums.jems.domain.JemsUser;
import com.dubaidrums.jems.repository.JemsInvoiceRepository;
import com.dubaidrums.jems.repository.JemsQuotationRepository;
import com.dubaidrums.jems.service.JemsClientService;
import com.dubaidrums.jems.service.JemsCostingCategoryService;
import com.dubaidrums.jems.service.JemsCostingItemService;
import com.dubaidrums.jems.service.JemsCostingSubCategoryService;
import com.dubaidrums.jems.service.JemsCountryService;
import com.dubaidrums.jems.service.JemsCurrencyService;
import com.dubaidrums.jems.service.JemsEmailService;
import com.dubaidrums.jems.service.JemsEventService;
import com.dubaidrums.jems.service.JemsInvoiceService;
import com.dubaidrums.jems.service.JemsLogService;
import com.dubaidrums.jems.service.JemsOrganizationService;
import com.dubaidrums.jems.service.JemsQuotationService;
import com.dubaidrums.jems.service.JemsRegionService;
import com.dubaidrums.jems.service.JemsReportingService;
import com.dubaidrums.jems.service.JemsStaffService;
import com.dubaidrums.jems.service.JemsUserService;
import com.dubaidrums.jems.service.JemsUtilService;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@PreAuthorize("isAuthenticated()")
@RequestMapping("/jemsevents")
@Controller
public class JemsEventController {
	Logger log = LogManager.getLogger(JemsEventController.class);

	@Autowired
	JemsEventService jemsEventService;

	@Autowired
	JemsCountryService jemsCountryService;

	@Autowired
	JemsCurrencyService jemsCurrencyService;

	@Autowired
	JemsInvoiceService jemsInvoiceService;

	@Autowired
	JemsOrganizationService jemsOrganizationService;

	@Autowired
	JemsQuotationService jemsQuotationService;

	@Autowired
	JemsRegionService jemsRegionService;

	@Autowired
	JemsUserService jemsUserService;

	@Autowired
	JemsStaffService jemsStaffService;

	@Autowired
	JemsClientService jemsClientService;

	@Autowired
	JemsUtilService jemsUtilService;

	@Autowired
	JemsLogService jemsLogService;

	@Autowired
	JemsCostingItemService jemsCostingItemService;

	@Autowired
	JemsCostingCategoryService jemsCostingCategoryService;

	@Autowired
	JemsCostingSubCategoryService jemsCostingSubCategoryService;

	@Autowired
	JemsEmailService jemsEmailService;

	@Autowired
	JemsReportingService jemsReportingService;

	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER','ROLE_OPERATOR')")
	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
	public String create(@Valid JemsEvent jemsEvent,
			BindingResult bindingResult, Model uiModel, Principal principal,
			HttpServletRequest httpServletRequest) {
		JemsUser user = jemsUserService.findJemsUserByUserName(principal
				.getName());

		String recurringeventparams = httpServletRequest
				.getParameter("recurringeventparams");
		String recurringeventsummary = httpServletRequest
				.getParameter("recurringeventsummary");

		if (jemsEvent.getStartDateTime() == null) {
			bindingResult.rejectValue("startDateTime", "date.invalid");
		}

		if (jemsEvent.getEndDateTime() == null) {
			bindingResult.rejectValue("endDateTime", "date.invalid");
		}

		if (jemsEvent.getEndDateTime().before(jemsEvent.getStartDateTime())
				|| jemsEvent.getEndDateTime().equals(
						jemsEvent.getStartDateTime())) {
			bindingResult.rejectValue("endDateTime", "date.invalid");
		}

		if (recurringeventparams == null || recurringeventparams.length() == 0) {
			if (jemsEvent.getEndDateTime() != null
					&& jemsEvent.getStartDateTime() != null
					&& jemsEvent.getEndDateTime().before(
							jemsEvent.getStartDateTime())) {
				bindingResult.rejectValue("endDateTime",
						"jemsevent.enddate.invalid");
			}
		}

		if (bindingResult.hasErrors()) {
			populateEditForm(uiModel, jemsEvent, user);
			return "jemsevents/create";
		}
		uiModel.asMap().clear();
		Date nd = new Date();
		jemsEvent.setCreatedDate(nd);
		jemsEvent.setCreatedUser(user);
		jemsEvent.setModifiedDateTime(nd);
		jemsEvent.setModifiedUser(user);
		jemsEvent.setActive(true);

		// RECURRING EVENTS
		if (recurringeventparams != null && recurringeventparams.length() > 0) {
			log.info("user: "
					+ user.getUserName()
					+ ", method: create, msg: creating recurring event, recurringEventParams: "
					+ recurringeventparams + ", recurringEventSummary: "
					+ recurringeventsummary);
			jemsEventService.generateRepeatEvents(recurringeventparams,
					jemsEvent);

			jemsLogService.newLog(user.getUserName()
					+ " created a new recurring event: " + jemsEvent.getTitle()
					+ " - " + recurringeventsummary);
			jemsEmailService.sendEmail(
					"Repeat Event Created: " + jemsEvent.getTitle(),
					user.getUserName() + " created a new recurring event: "
							+ jemsEvent.getTitle() + " - "
							+ recurringeventsummary, null);

			return "redirect:/jemsevents";
		}

		jemsEventService.saveJemsEvent(jemsEvent);

		jemsLogService.newLog(user.getUserName()
				+ " created a new event: <a href='/jemsevents/"
				+ jemsEvent.getId() + "'>" + jemsEvent.getTitle() + "</a>");
		jemsEmailService.sendEmail("Event Created: " + jemsEvent.getTitle(),
				user.getUserName()
						+ " created a new event: <a href='/jemsevents/"
						+ jemsEvent.getId() + "'>" + jemsEvent.getTitle()
						+ "</a>", null);
		log.info("user: " + user.getUserName() + ", method: create, eventId: "
				+ jemsEvent.getId() + ", msg: new event created");

		return "redirect:/jemsevents/"
				+ encodeUrlPathSegment(jemsEvent.getId().toString(),
						httpServletRequest) + "?msg=1";
	}

	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER','ROLE_OPERATOR')")
	@RequestMapping(params = "form", produces = "text/html")
	public String createForm(
			@RequestParam(value = "startDateTime", required = false) Date startDateTime,
			@RequestParam(value = "endDateTime", required = false) Date endDateTime,
			Model uiModel, Principal principal) {
		if (startDateTime == null)
			startDateTime = jemsUtilService.getCurrentDate();// new Date();
		if (endDateTime == null)
			endDateTime = jemsUtilService.getCurrentDate();// new Date();
		Calendar endCal = Calendar.getInstance();
		endCal.setTime(endDateTime);
		endCal.add(Calendar.MINUTE, endCal.get(Calendar.MINUTE) + 30);
		endDateTime = endCal.getTime();

		JemsEvent je = new JemsEvent();
		JemsUser user = jemsUserService.findJemsUserByUserName(principal
				.getName());

		Set<JemsOrganization> orgs = user.getOrganizations();
		JemsOrganization org = jemsOrganizationService
				.findJemsOrganization((long) 1);
		if (!orgs.contains(org)) {
			org = orgs.iterator().next();
		}
		je.setOrganization(org);

		je.setCurrency(org.getCurrency());
		je.setCountry(org.getCountry());
		je.setRegion(org.getRegion());
		je.setLocationLatLong(org.getDefaultGps());
		je.setChairsRequired(false);
		je.setUseInDocs(0);
		je.setPaid(false);
		je.setNumberDrums(0);
		je.setStartDateTime(startDateTime);
		je.setEndDateTime(endDateTime);
		je.setActive(true);

		populateEditForm(uiModel, je, user);
		return "jemsevents/create";
	}

	@RequestMapping(value = "/{id}", produces = "text/html")
	public String show(@PathVariable("id") Long id,
			@RequestParam(value = "msg", required = false) Integer msg,
			Model uiModel, Principal principal) {
		uiModel.addAttribute("jemsevent", jemsEventService.findJemsEvent(id));
		if (msg != null && msg == 1) {
			uiModel.addAttribute("msg", "Event sucessfully saved!");
		}

		log.info("user: " + principal.getName() + ", method: show, eventId: "
				+ id + ", msg: event showed");

		// log.info("JQs: "+jemsEventService.findJemsEvent(id).getJemsQuotations());
		// log.info("JIs: "+jemsEventService.findJemsEvent(id).getJemsInvoices());

		return "jemsevents/show";
	}

	@RequestMapping(value = "/{id}", params = "duplicate", produces = "text/html")
	public String duplicate(@PathVariable("id") Long id, Model uiModel,
			Principal principal) {
		JemsEvent je = jemsEventService.findJemsEvent(id);

		log.info("user: " + principal.getName()
				+ ", method: duplicate, eventId: " + id
				+ ", msg: event duplicated");

		populateEditForm(uiModel, jemsEventService.buildCopy(je),
				jemsUserService.findJemsUserByUserName(principal.getName()));
		return "jemsevents/create";
	}

	@RequestMapping(value = "/{id}/jobsheet", produces = "text/html")
	public String jobsheet(@PathVariable("id") Long id, Model uiModel,
			Principal principal) {
		uiModel.addAttribute("jemsevent", jemsEventService.findJemsEvent(id));

		List<JemsCostingItem> jcostings = jemsCostingItemService
				.findByJemsEventId(id);
		List<JemsCostingItem> costings = new ArrayList<JemsCostingItem>();

		for (JemsCostingItem ci : jcostings) {
			if (ci.getQuantity() != null & ci.getQuantity() != 0.0) {
				costings.add(ci);
			}
		}
		uiModel.addAttribute(
				"jemscostings",
				new JSONSerializer().exclude("*.class").exclude("jemsEvent")
						.exclude("organization").exclude("rate")
						.exclude("version").serialize(costings));

		log.info("user: " + principal.getName()
				+ ", method: jobsheet, eventId: " + id
				+ ", msg: jobsheet showed");

		return "jemsevents/jobsheet";
	}

	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/{id}/jobsheetpdf", method = RequestMethod.GET, produces = "application/pdf")
	public String generateJobsheetPdf(@PathVariable("id") Long id,
			Model uiModel, Principal principal,
			HttpServletRequest httpServletRequest) {
		JemsEvent je = jemsEventService.findJemsEvent(id);
		try {
			log.info("user: " + principal.getName()
					+ ", method: generateJobsheetPdf, eventId: " + id
					+ ", msg: generating jobsheet pdf");
			String fileName = jemsReportingService.generateJobsheetPdf(je);

			return "redirect:/jemsevents/jobsheetpdf/"
					+ encodeUrlPathSegment(fileName, httpServletRequest);
		} catch (Exception e) {
			log.error("Unable to generate PDF!", e);
			throw new RuntimeException("Unable to generate PDF!", e);
		}
	}

	@RequestMapping(value = "/jobsheetpdf/{fileName}", method = RequestMethod.GET, produces = "application/pdf")
	@ResponseBody
	public FileSystemResource getJobsheetPdfFile(
			@PathVariable("fileName") String fileName) {
		return new FileSystemResource(System.getProperty("java.io.tmpdir")
				+ "/" + fileName + ".pdf");
	}

	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/{id}/costingspdf", method = RequestMethod.GET, produces = "application/pdf")
	public String generateCostingsPdf(@PathVariable("id") Long id,
			Model uiModel, Principal principal,
			HttpServletRequest httpServletRequest) {
		JemsEvent je = jemsEventService.findJemsEvent(id);
		try {
			log.info("user: " + principal.getName()
					+ ", method: generateCostingsPdf, eventId: " + id
					+ ", msg: generating costings pdf");
			String fileName = jemsReportingService.generateCostingsPdf(je);

			return "redirect:/jemsevents/costingspdf/"
					+ encodeUrlPathSegment(fileName, httpServletRequest);
		} catch (Exception e) {
			log.error("Unable to generate PDF!", e);
			throw new RuntimeException("Unable to generate PDF!", e);
		}
	}

	@RequestMapping(value = "/costingspdf/{fileName}", method = RequestMethod.GET, produces = "application/pdf")
	@ResponseBody
	public FileSystemResource getCostingsPdfFile(
			@PathVariable("fileName") String fileName) {
		return new FileSystemResource(System.getProperty("java.io.tmpdir")
				+ "/" + fileName + ".pdf");
	}

	@RequestMapping(produces = "text/html")
	public String list(
			@RequestParam(value = "page", required = false) Integer page,
			Model uiModel, Principal principal) {
		JemsUser user = jemsUserService.findJemsUserByUserName(principal
				.getName());

		if (page == null) {
			page = 1;
		}

		final int sizeNo = 100;
		final int firstResult = (page.intValue() - 1) * sizeNo;

		Page<JemsEvent> p = jemsEventService.findJemsEventEntries(user,
				firstResult, sizeNo);

		float nrOfPages = (float) p.getTotalElements() / sizeNo;
		int maxPages = (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1
				: nrOfPages);

		int begin, end;
		//
		// if(maxPages <= 25){
		// begin = 1;
		// end = maxPages;
		// }else{
		// if()
		// }

		begin = 1;
		end = maxPages;

		uiModel.addAttribute("maxPages", maxPages);
		uiModel.addAttribute("page", page);
		uiModel.addAttribute("begin", begin);
		uiModel.addAttribute("end", end);
		uiModel.addAttribute("jemsevents", p.getContent());

		log.info("user: " + principal.getName() + ", method: list, page: "
				+ page + ", msg: events listed");

		return "jemsevents/list";
	}

	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER','ROLE_OPERATOR')")
	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
	public String update(@Valid JemsEvent jemsEvent,
			BindingResult bindingResult, Model uiModel, Principal principal,
			HttpServletRequest httpServletRequest) {
		JemsUser user = jemsUserService.findJemsUserByUserName(principal
				.getName());

		if (jemsEvent.getStartDateTime() == null) {
			bindingResult.rejectValue("startDateTime", "date.invalid");
		}

		if (jemsEvent.getEndDateTime() == null) {
			bindingResult.rejectValue("endDateTime", "date.invalid");
		}

		if (jemsEvent.getEndDateTime().before(jemsEvent.getStartDateTime())
				|| jemsEvent.getEndDateTime().equals(
						jemsEvent.getStartDateTime())) {
			bindingResult.rejectValue("endDateTime", "date.invalid");
		}

		if (bindingResult.hasErrors()) {
			populateEditForm(uiModel, jemsEvent, user);
			return "jemsevents/update";
		}
		uiModel.asMap().clear();
		jemsEvent.setModifiedDateTime(new Date());
		jemsEvent.setModifiedUser(user);
		jemsEvent.setActive(true);

		// JemsEvent oldJemsEvent =
		// jemsEventService.findJemsEvent(jemsEvent.getId());
		// jemsEvent.setJemsQuotation(oldJemsEvent.getJemsQuotation());
		// jemsEvent.setJemsInvoice(oldJemsEvent.getJemsInvoice());

		// log.info("Event updated!! quotation: "+jemsEvent.getJemsQuotation());

		jemsEventService.updateJemsEvent(jemsEvent);

		jemsLogService.newLog(user.getUserName()
				+ " updated an event: <a href='/jemsevents/"
				+ jemsEvent.getId() + "'>" + jemsEvent.getTitle() + "</a>");
		jemsEmailService.sendEmail("Event Updated: " + jemsEvent.getTitle(),
				user.getUserName() + " updated an event: <a href='/jemsevents/"
						+ jemsEvent.getId() + "'>" + jemsEvent.getTitle()
						+ "</a>", null);
		log.info("user: " + principal.getName() + ", method: update, eventId: "
				+ jemsEvent.getId() + ", msg: event updated");

		return "redirect:/jemsevents/"
				+ encodeUrlPathSegment(jemsEvent.getId().toString(),
						httpServletRequest) + "?msg=1";
	}

	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER','ROLE_OPERATOR')")
	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
	public String updateForm(@PathVariable("id") Long id, Model uiModel,
			Principal principal) {
		JemsUser user = jemsUserService.findJemsUserByUserName(principal
				.getName());
		JemsEvent je = jemsEventService.findJemsEvent(id);

		// log.info("Event update! quotation: "+je.getJemsQuotation());

		populateEditForm(uiModel, je, user);
		return "jemsevents/update";
	}

	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER','ROLE_OPERATOR')")
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
	public String delete(@PathVariable("id") Long id, Model uiModel,
			Principal principal) {
		JemsEvent jemsEvent = jemsEventService.findJemsEvent(id);
		jemsEventService.deleteJemsEvent(jemsEvent);
		uiModel.asMap().clear();

		jemsLogService.newLog(principal.getName() + " deleted an event: "
				+ jemsEvent.getTitle());
		jemsEmailService.sendEmail(
				"Event Deleted: " + jemsEvent.getTitle(),
				principal.getName() + " deleted an event: "
						+ jemsEvent.getTitle(), null);
		log.info("user: " + principal.getName() + ", method: delete, eventId: "
				+ jemsEvent.getId() + ", msg: event deleted");

		return "redirect:/";
	}

	void populateEditForm(Model uiModel, JemsEvent jemsEvent, JemsUser user) {
		uiModel.addAttribute("jemsEvent", jemsEvent);

		Set<JemsOrganization> jemsOrganizations = user.getOrganizations();
		uiModel.addAttribute("jemsorganizations", jemsOrganizations);

		List<JemsCountry> jemsCountrys = jemsCountryService
				.findActiveJemsCountrys();
		List<JemsCurrency> jemsCurrencys = jemsCurrencyService
				.findActiveJemsCurrencys();
		List<JemsRegion> jemsRegions = jemsRegionService
				.findActiveJemsRegions();
		uiModel.addAttribute("jemscountrys", jemsCountrys);
		uiModel.addAttribute("jemscurrencys", jemsCurrencys);
		uiModel.addAttribute("jemsusers",
				jemsUserService.findEnabledJemsUsers());
		uiModel.addAttribute("jemsstaff",
				jemsStaffService.findActiveJemsStaffs());
		uiModel.addAttribute("jemspaymentmethods",
				Arrays.asList(JemsPaymentMethod.values()));
		uiModel.addAttribute("jemseventstatuses",
				Arrays.asList(JemsEventStatus.values()));
		uiModel.addAttribute("jemseventtypes",
				Arrays.asList(JemsEventType.values()));

		JemsOrganization org = jemsOrganizations.iterator().next();
		uiModel.addAttribute("jemsregions",
				jemsRegionService.findByCountry(org.getCountry()));

		uiModel.addAttribute("jemsOrganizationsJSON", new JSONSerializer()
				.exclude("*.class").serialize(jemsOrganizations));
		// uiModel.addAttribute("jemsCountrysJSON", new
		// JSONSerializer().exclude("*.class").serialize(jemsCountrys));
		uiModel.addAttribute("jemsRegionsJSON",
				new JSONSerializer().exclude("*.class").serialize(jemsRegions));

		// addDateTimeFormatPatterns(uiModel);
		// uiModel.addAttribute("jemsinvoices",
		// jemsInvoiceService.findAllJemsInvoices());
		// uiModel.addAttribute("jemsquotations",
		// jemsQuotationService.findAllJemsQuotations());
	}

	String encodeUrlPathSegment(String pathSegment,
			HttpServletRequest httpServletRequest) {
		String enc = httpServletRequest.getCharacterEncoding();
		if (enc == null) {
			enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
		}
		try {
			pathSegment = UriUtils.encodePathSegment(pathSegment, enc);
		} catch (UnsupportedEncodingException uee) {
		}
		return pathSegment;
	}

	// @RequestMapping(value = "/{id}", headers = "Accept=application/json")
	// @ResponseBody
	// public ResponseEntity<String> showJson(@PathVariable("id") Long id) {
	// JemsEvent jemsEvent = jemsEventService.findJemsEvent(id);
	// HttpHeaders headers = new HttpHeaders();
	// headers.add("Content-Type", "application/json; charset=utf-8");
	// if (jemsEvent == null) {
	// return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
	// }
	// return new ResponseEntity<String>(jemsEvent.toJson(), headers,
	// HttpStatus.OK);
	// }
	//
	// @RequestMapping(headers = "Accept=application/json")
	// @ResponseBody
	// public ResponseEntity<String> listJson() {
	// HttpHeaders headers = new HttpHeaders();
	// headers.add("Content-Type", "application/json; charset=utf-8");
	// List<JemsEvent> result = jemsEventService.findAllJemsEvents();
	// return new ResponseEntity<String>(JemsEvent.toJsonArray(result), headers,
	// HttpStatus.OK);
	// }
	//
	// @RequestMapping(method = RequestMethod.POST, headers =
	// "Accept=application/json")
	// public ResponseEntity<String> createFromJson(@RequestBody String json) {
	// JemsEvent jemsEvent = JemsEvent.fromJsonToJemsEvent(json);
	// jemsEventService.saveJemsEvent(jemsEvent);
	// HttpHeaders headers = new HttpHeaders();
	// headers.add("Content-Type", "application/json");
	// return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	// }
	//
	// @RequestMapping(value = "/jsonArray", method = RequestMethod.POST,
	// headers = "Accept=application/json")
	// public ResponseEntity<String> createFromJsonArray(@RequestBody String
	// json) {
	// for (JemsEvent jemsEvent: JemsEvent.fromJsonArrayToJemsEvents(json)) {
	// jemsEventService.saveJemsEvent(jemsEvent);
	// }
	// HttpHeaders headers = new HttpHeaders();
	// headers.add("Content-Type", "application/json");
	// return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	// }
	//
	// @RequestMapping(method = RequestMethod.PUT, headers =
	// "Accept=application/json")
	// public ResponseEntity<String> updateFromJson(@RequestBody String json) {
	// HttpHeaders headers = new HttpHeaders();
	// headers.add("Content-Type", "application/json");
	// JemsEvent jemsEvent = JemsEvent.fromJsonToJemsEvent(json);
	// if (jemsEventService.updateJemsEvent(jemsEvent) == null) {
	// return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
	// }
	// return new ResponseEntity<String>(headers, HttpStatus.OK);
	// }
	//
	// @RequestMapping(value = "/jsonArray", method = RequestMethod.PUT, headers
	// = "Accept=application/json")
	// public ResponseEntity<String> updateFromJsonArray(@RequestBody String
	// json) {
	// HttpHeaders headers = new HttpHeaders();
	// headers.add("Content-Type", "application/json");
	// for (JemsEvent jemsEvent: JemsEvent.fromJsonArrayToJemsEvents(json)) {
	// if (jemsEventService.updateJemsEvent(jemsEvent) == null) {
	// return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
	// }
	// }
	// return new ResponseEntity<String>(headers, HttpStatus.OK);
	// }
	//
	// @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers =
	// "Accept=application/json")
	// public ResponseEntity<String> deleteFromJson(@PathVariable("id") Long id)
	// {
	// JemsEvent jemsEvent = jemsEventService.findJemsEvent(id);
	// HttpHeaders headers = new HttpHeaders();
	// headers.add("Content-Type", "application/json");
	// if (jemsEvent == null) {
	// return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
	// }
	// jemsEventService.deleteJemsEvent(jemsEvent);
	// return new ResponseEntity<String>(headers, HttpStatus.OK);
	// }
	//
	// @InitBinder
	// public void initBinder(WebDataBinder binder) {
	// //binder.getConversionService().
	// }

	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/calendar/events/{selecteddate}", headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> getCalendarEventsListJson(
			@PathVariable("selecteddate") String selectedDate,
			Principal principal) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");

		JemsUser user = jemsUserService.findJemsUserByUserName(principal
				.getName());
		List<JemsEvent> result = jemsEventService.getCalendarEventsList(
				selectedDate, user);

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm");

		List<Map<String, String>> events = new ArrayList<Map<String, String>>();
		for (JemsEvent je : result) {
			Map<String, String> event = new HashMap<String, String>();

			if (je.getEndDateTime().before(je.getStartDateTime())
					|| je.getEndDateTime().equals(je.getStartDateTime())) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(je.getStartDateTime());
				cal.add(Calendar.HOUR_OF_DAY, 1);
				je.setEndDateTime(cal.getTime());
			}

			event.put("id", je.getId().toString());
			event.put("subject", je.getTitleWithOrg());
			event.put("description", je.getDescription());
			event.put("color", je.getCalendarStatusColor());
			event.put("start", df.format(je.getStartDateTime()));
			event.put("end", df.format(je.getEndDateTime()));
			event.put("orgid", je.getOrganization().getId().toString());
			// event.put("alLDay", "true");
			// event.put("calendar", "Default");

			events.add(event);
		}

		log.info("user: " + principal.getName()
				+ ", method: getCalendarEventsListJson, selectedDate: "
				+ selectedDate + ", msg: returned " + events.size() + " events");

		return new ResponseEntity<String>(new JSONSerializer().exclude(
				"*.class").serialize(events), headers, HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER')")
	@RequestMapping(value = "/reports", headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> getReportData(
			@RequestParam(value = "startDate", required = true) String startDate,
			@RequestParam(value = "endDate", required = true) String endDate,
			@RequestParam(value = "type", required = true) String type,
			@RequestParam(value = "org", required = true) Long org,
			Principal principal) {
		JemsUser user = jemsUserService.findJemsUserByUserName(principal
				.getName());
		
		long startTime = System.currentTimeMillis();
		Map<String, List> data = jemsEventService.getReportData(startDate,
				endDate, type, org, user);
		;
		long endTime = System.currentTimeMillis();
		long diff = endTime - startTime;				

		log.info("user: " + principal.getName()
				+ ", method: getReportData, msg: got report data for type: "
				+ type + ", org: " + org);
		
		log.info("getReportData took "+diff+" milliseconds.");

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		return new ResponseEntity<String>(new JSONSerializer().exclude(
				"*.class").deepSerialize(data), headers, HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER','ROLE_OPERATOR')")
	@RequestMapping(value = "/{id}/costings", produces = "text/html")
	public String getCostings(@PathVariable("id") Long id,
			@RequestParam(value = "msg", required = false) Integer msg,
			Model uiModel, Principal principal) {
		List<JemsCostingItem> costings = jemsCostingItemService
				.findByJemsEventId(id);

		uiModel.addAttribute("jemsEventId", id);
		uiModel.addAttribute("costings", new JSONSerializer()
				.exclude("*.class").exclude("jemsEvent")
				.exclude("organization").serialize(costings));

		log.info("user: " + principal.getName()
				+ ", method: getCostings, eventId: " + id
				+ ", msg: costings showed");

		if (msg != null && msg == 1) {
			uiModel.addAttribute("msg", "Costings sucessfully saved!");
		}

		return "jemsevents/costings";
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(method = RequestMethod.POST, value = "/{id}/costings", produces = "text/html")
	public ResponseEntity<String> saveCostings(@PathVariable("id") Long id,
			@RequestBody String json, Principal principal) {
		JemsEvent jemsEvent = jemsEventService.findJemsEvent(id);
		List<JemsCostingItem> costingItems = new JSONDeserializer<List<JemsCostingItem>>()
				.use(null, ArrayList.class)
				.use("values", JemsCostingItem.class).deserialize(json);

		for (JemsCostingItem jemsCostingItem : costingItems) {
			// log.info("CI: "+jemsCostingItem);
			jemsCostingItem.setOrganization(jemsEvent.getOrganization());
			jemsCostingItem.setJemsEvent(jemsEvent);
			jemsCostingItemService.saveJemsCostingItem(jemsCostingItem);
		}

		jemsLogService.newLog(principal.getName()
				+ " updated Costings for event: <a href='/jemsevents/"
				+ jemsEvent.getId() + "'>" + jemsEvent.getTitle() + "</a>");
		log.info("user: " + principal.getName()
				+ ", method: saveCostings, eventId: " + id
				+ ", msg: costings saved");

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		return new ResponseEntity<String>(headers, HttpStatus.OK);
	}

	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/logs", produces = "text/html")
	public ResponseEntity<String> getLogs(Principal principal) {
		List<JemsLog> logs = jemsLogService.findLatestLogs(420);

		log.info("user: " + principal.getName()
				+ ", method: getLogs, msg: logs showed");

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		return new ResponseEntity<String>(new JSONSerializer().exclude(
				"*.class").serialize(logs), headers, HttpStatus.OK);
	}

	// @PreAuthorize("hasRole('ROLE_ADMIN')")
	// @RequestMapping(method = RequestMethod.GET, value = "/copycostings",
	// produces = "text/html")
	// public ResponseEntity<String> copyCostings(Principal principal) {
	// List<JemsCostingCategory> categories =
	// jemsCostingCategoryService.findAllJemsCostingCategorysByOrganizationId((long)
	// 1);
	// List<JemsOrganization> orgs =
	// jemsOrganizationService.findAllJemsOrganizations();
	//
	// for (JemsOrganization o : orgs) {
	// if(o.getId()==3 || o.getId()==4){
	// for (JemsCostingCategory c : categories) {
	// JemsCostingCategory c1 = new JemsCostingCategory();
	// c1.setName(c.getName());
	// c1.setOrganization(o);
	// jemsCostingCategoryService.saveJemsCostingCategory(c1);
	//
	// for(JemsCostingSubCategory sc: c.getSubcategories()){
	// JemsCostingSubCategory sc1 = new JemsCostingSubCategory();
	// sc1.setCategory(c1);
	// sc1.setName(sc.getName());
	// sc1.setRate(sc.getRate());
	//
	// jemsCostingSubCategoryService.saveJemsCostingSubCategory(sc1);
	// c1.getSubcategories().add(sc1);
	// }
	//
	// jemsCostingCategoryService.saveJemsCostingCategory(c1);
	// }
	// }
	// }
	//
	//
	//
	// HttpHeaders headers = new HttpHeaders();
	// headers.add("Content-Type", "application/json; charset=utf-8");
	// return new ResponseEntity<String>("Copy Costings",
	// headers,HttpStatus.OK);
	// }

	// @PreAuthorize("hasRole('ROLE_ADMIN')")
	// @RequestMapping(method = RequestMethod.GET, value = "/fixgps", produces =
	// "text/html")
	// public ResponseEntity<String> fixgps(Principal principal) {
	//
	// List<JemsEvent> events = jemsEventService.findAllJemsEvents();
	//
	// String str = "";
	// for (JemsEvent e : events) {
	// if(e.getLocationLatLong().toLowerCase().indexOf("map")>-1 ||
	// e.getLocationLatLong().toLowerCase().indexOf("hybrid")>-1 ||
	// e.getLocationLatLong().toLowerCase().indexOf("satellite")>-1){
	// str = e.getLocationLatLong();
	// int end = str.indexOf(",", str.indexOf(",")+1);
	// str = str.substring(0, end);
	// e.setLocationLatLong(str);
	// jemsEventService.updateJemsEvent(e);
	// //System.out.println(e.getLocationLatLong()+" -> "+str);
	// }
	// }
	//
	//
	// HttpHeaders headers = new HttpHeaders();
	// headers.add("Content-Type", "application/json; charset=utf-8");
	// return new ResponseEntity<String>("Fix GPS", headers, HttpStatus.OK);
	// }

	// private Map<String, JemsClient> clientsMap = new HashMap<String,
	// JemsClient>();
	//
	// @PreAuthorize("hasRole('ROLE_ADMIN')")
	// @RequestMapping(method = RequestMethod.GET, value = "/fixclient",
	// produces = "text/html")
	// public ResponseEntity<String> fixclient(Principal principal) {
	//
	// List<JemsEvent> events = jemsEventService.findAllJemsEvents();
	//
	// for (JemsEvent e : events) {
	// if(e.getClientCompany()!=null && e.getClientCompany().length()!=0){
	// JemsClient c = new JemsClient();
	// c.setActive(true);
	// c.setAddress(e.getClientAddress());
	// c.setCompany(e.getClientCompany());
	// c.setContactPerson(e.getClientContactPerson());
	// c.setEmail(e.getClientEmail());
	// c.setPhone(e.getClientPhone());
	// c.getEvents().add(e);
	// addClient(c,e);
	// }
	//
	// if(e.getHiringAgentCompany()!=null &&
	// e.getHiringAgentCompany().length()!=0){
	// JemsClient c = new JemsClient();
	// c.setActive(true);
	// c.setAddress(e.getHiringAgentAddress());
	// c.setCompany(e.getHiringAgentCompany());
	// c.setContactPerson(e.getHiringAgentContactPerson());
	// c.setEmail(e.getHiringAgentEmail());
	// c.setPhone(e.getHiringAgentPhone());
	// c.getEvents().add(e);
	// addClient(c, e);
	// }
	// }
	//
	//
	// for(JemsClient c : clientsMap.values()){
	// jemsClientService.saveJemsClient(c);
	// }
	//
	// HttpHeaders headers = new HttpHeaders();
	// headers.add("Content-Type", "application/json; charset=utf-8");
	// return new ResponseEntity<String>("Fix Client: "+clientsMap.size(),
	// headers, HttpStatus.OK);
	// }
	//
	// private void addClient(JemsClient newc,JemsEvent e) {
	// newc.setCompany(newc.getCompany().trim());
	// JemsClient oldc = clientsMap.get(newc.getCompany().toLowerCase());
	// if(oldc!=null){
	// if(oldc.getCompany().trim().equalsIgnoreCase(newc.getCompany().trim().toLowerCase())){
	// oldc.setCompany(oldc.getCompany().trim());
	// oldc.getEvents().add(e);
	//
	// if(newc.getContactPerson().length() > oldc.getContactPerson().length())
	// oldc.setContactPerson(newc.getContactPerson());
	//
	// if(newc.getAddress().length() > oldc.getAddress().length())
	// oldc.setContactPerson(newc.getContactPerson());
	//
	// if(newc.getEmail().length() > oldc.getEmail().length())
	// oldc.setContactPerson(newc.getContactPerson());
	//
	// if(newc.getPhone().length() > oldc.getPhone().length())
	// oldc.setContactPerson(newc.getContactPerson());
	//
	//
	// }
	// else{
	// clientsMap.put(newc.getCompany().trim().toLowerCase(), newc);
	// }
	// }else{
	// clientsMap.put(newc.getCompany().trim().toLowerCase(), newc);
	// }
	// }

	// List<JemsEvent> events;
	// List<JemsQuotation> quotations;
	// List<JemsInvoice> invoices;
	// Map<JemsQuotation,JemsEvent> quotation_event;
	// Map<JemsInvoice,JemsEvent> invoice_event;
	//
	// @RequestMapping(method = RequestMethod.GET, value = "/fixalldocs",
	// produces = "text/html")
	// public ResponseEntity<String> fixalldocs(Principal principal) {
	// events = jemsEventService.findAllJemsEvents();
	// quotation_event = new HashMap<JemsQuotation,JemsEvent>();
	// invoice_event = new HashMap<JemsInvoice,JemsEvent>();
	//
	//
	// for (JemsEvent e : events) {
	// if(e.getJemsQuotation()!=null){
	// quotation_event.put(e.getJemsQuotation(), e);
	// }
	//
	// if(e.getJemsInvoice()!=null){
	// invoice_event.put(e.getJemsInvoice(), e);
	// }
	// }
	//
	// for(JemsQuotation q: quotation_event.keySet()){
	// System.out.println("update jems_quotation set jems_event="+quotation_event.get(q).getId()+" where id="+q.getId()+";");
	// }
	//
	// for(JemsInvoice i: invoice_event.keySet()){
	// System.out.println("update jems_invoice set jems_event="+invoice_event.get(i).getId()+" where id="+i.getId()+";");
	// }
	//
	// HttpHeaders headers = new HttpHeaders();
	// headers.add("Content-Type", "application/json; charset=utf-8");
	// System.out.println("========================== DONE ==================================");
	// return new ResponseEntity<String>("Fix  Docs", headers, HttpStatus.OK);
	// }

	//
	// @RequestMapping(method = RequestMethod.GET, value = "/fixmissingdocs",
	// produces = "text/html")
	// public ResponseEntity<String> fixmissingdocs(Principal principal) {
	// events = jemsEventService.findAllJemsEvents();
	// quotations = jemsQuotationService.findAllJemsQuotations();
	// invoices = jemsInvoiceService.findAllJemsInvoices();
	// quotation_event = new HashMap<JemsQuotation,JemsEvent>();
	// invoice_event = new HashMap<JemsInvoice,JemsEvent>();
	//
	// int missing_q=0, missing_i=0;
	//
	// for (JemsEvent e : events) {
	// if(e.getJemsQuotation()!=null){
	// quotation_event.put(e.getJemsQuotation(), e);
	// }
	//
	// if(e.getJemsInvoice()!=null){
	// invoice_event.put(e.getJemsInvoice(), e);
	// }
	// }
	//
	// Map<Long,Long> matched_eq = new HashMap<Long,Long>();
	// Map<Long,Long> matched_ei = new HashMap<Long,Long>();
	// Map<Long,List<Long>> duplicate_eq = new HashMap<Long,List<Long>>();
	// Map<Long,List<Long>> duplicate_ei = new HashMap<Long,List<Long>>();
	// Map<Long,Long> existing_eq = new HashMap<Long,Long>();
	// Map<Long,Long> existing_ei = new HashMap<Long,Long>();
	//
	//
	// //QUOTATIONS
	// try {
	// for (JemsQuotation q : quotations) {
	// JemsEvent e = quotation_event.get(q);
	// if(e==null){
	// missing_q++;
	//
	// List<Long> foundEvents = findEvent(q);
	// if(foundEvents.size()==1){
	// if(matched_eq.get(foundEvents.get(0))!=null){
	// List<Long> duplicates = duplicate_eq.get(foundEvents.get(0));
	// if(duplicates==null){
	// duplicates = new ArrayList<Long>();
	// duplicates.add(matched_eq.get(foundEvents.get(0)));
	// }
	//
	// duplicates.add(q.getId());
	//
	// duplicate_eq.put(foundEvents.get(0),duplicates);
	// }else{
	// matched_eq.put(foundEvents.get(0), q.getId());
	// }
	//
	// }else if(foundEvents.size()>1){
	// System.out.println("Multiple Matches: q: "+q.getId()+" e: "+foundEvents+"\n");
	// }else if(foundEvents.size()==0){
	// System.out.println("No Match: q: "+q.getId()+", qDate="+q.getQDate());
	// System.out.println("billTo:\n"+q.getBillTo().trim()+"\neventDetails:\n"+q.getEventDetails().trim()+"\n");
	// }
	// }else{
	// existing_eq.put(e.getId(),q.getId());
	// }
	// }
	//
	// System.out.println("\n\nDuplicate event-quotation: "+duplicate_eq);
	// System.out.println("\n\nExisting event-quotation: "+existing_eq);
	//
	// List<Long> yo = new ArrayList(matched_eq.keySet());
	// Collections.sort(yo);
	// yo.removeAll(duplicate_eq.keySet());
	// System.out.println("\n");
	// for(Long eid: yo){
	// System.out.println("update jems_event set jems_quotation="+matched_eq.get(eid)+" where id="+eid);
	// }
	//
	//
	// //INVOICES
	// System.out.println("\n====================INVOICES=========================\n");
	//
	// for (JemsInvoice i : invoices) {
	// JemsEvent e = invoice_event.get(i);
	// if(e==null){
	// missing_i++;
	//
	// List<Long> foundEvents = findEvent(i);
	// if(foundEvents.size()==1){
	// if(matched_ei.get(foundEvents.get(0))!=null){
	// List<Long> duplicates = duplicate_ei.get(foundEvents.get(0));
	// if(duplicates==null){
	// duplicates = new ArrayList<Long>();
	// duplicates.add(matched_ei.get(foundEvents.get(0)));
	// }
	//
	// duplicates.add(i.getId());
	//
	// duplicate_ei.put(foundEvents.get(0),duplicates);
	// }else{
	// matched_ei.put(foundEvents.get(0), i.getId());
	// }
	//
	// }else if(foundEvents.size()>1){
	// System.out.println("Multiple Matches: i: "+i.getId()+" e: "+foundEvents+"\n");
	// }else if(foundEvents.size()==0){
	// System.out.println("No Match: i: "+i.getId()+", iDate="+i.getIDate());
	// System.out.println("billTo:\n"+i.getBillTo().trim()+"\neventDetails:\n"+i.getEventDetails().trim()+"\n");
	// }
	// }else{
	// existing_ei.put(e.getId(),i.getId());
	// }
	// }
	//
	// System.out.println("\n\nDuplicate event-invoice: "+duplicate_ei);
	// System.out.println("\n\nExisting event-invoice: "+existing_ei);
	//
	// List<Long> yo2 = new ArrayList(matched_ei.keySet());
	// Collections.sort(yo2);
	// yo2.removeAll(duplicate_ei.keySet());
	// System.out.println("\n");
	// for(Long eid: yo2){
	// System.out.println("update jems_event set jems_invoice="+matched_ei.get(eid)+" where id="+eid);
	// }
	//
	//
	//
	// // for (JemsInvoice i : invoices) {
	// // JemsEvent e = invoice_event.get(i);
	// // if(e==null){
	// // missing_i++;
	// // //System.out.println("Missing Invoice. iid: "+i.getId());
	// //
	// //// JemsEvent foundEvent = findEvent(i, events);
	// //// if(foundEvent !=null)
	// ////
	// System.out.println("Match. i: "+i.getId()+" e: "+foundEvent.getId());
	// //// else
	// //// System.out.println("NO Match. i: "+i.getId());
	// // }
	// // }
	// } catch (Exception e1) {
	// // TODO Auto-generated catch block
	// e1.printStackTrace();
	// }
	//
	// HttpHeaders headers = new HttpHeaders();
	// headers.add("Content-Type", "application/json; charset=utf-8");
	// System.out.println("========================== DONE ==================================");
	// return new
	// ResponseEntity<String>("Fix Missing Docs. Quotations: "+missing_q+", Invoices: "+missing_i,
	// headers, HttpStatus.OK);
	// }
	//
	// private List<Long> findEvent(JemsInvoice i) {
	// List<Long> result = new ArrayList<Long>();
	// for (JemsEvent e : events) {
	// //if(jemsEventService.getBillTo(e).trim().equals(q.getBillTo().trim()) ||
	// jemsEventService.getEventDetails(e).trim().equals(q.getEventDetails().trim())){
	// if(compare(i,e)){
	// //System.out.println("Found Event: "+e.getId());
	// result.add(e.getId());
	// }
	// }
	// return result;
	// }
	//
	// private List<Long> findEvent(JemsQuotation q) {
	// //System.out.println("Finding Events for q: "+q.getId());
	// List<Long> result = new ArrayList<Long>();
	// for (JemsEvent e : events) {
	// //if(jemsEventService.getBillTo(e).trim().equals(q.getBillTo().trim()) ||
	// jemsEventService.getEventDetails(e).trim().equals(q.getEventDetails().trim())){
	// if(compare(q,e)){
	// //System.out.println("Found Event: "+e.getId());
	// result.add(e.getId());
	// }
	// }
	// return result;
	// }
	// private DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	//
	// private boolean compare(JemsQuotation q, JemsEvent e) {
	// String[] s1 = q.getBillTo().trim().split("\n");
	// String contactPerson="", company="";
	//
	// try{contactPerson = s1[0].trim();}catch(Exception x){}
	// try{company = s1[1].trim();}catch(Exception x){}
	//
	// boolean result = false;
	//
	// if(e.getUseInDocs() !=null ){
	// if(e.getUseInDocs()==0){
	// if(contactPerson.equals(e.getClientContactPerson().trim()) &&
	// company.equals(e.getClientCompany().trim())){
	// //System.out.println("q: "+q.getEventDetails().trim()+" e: "+jemsEventService.getEventDetails(e).trim());
	// return true;
	// }
	// else{
	// result = false;
	// }
	// }else{
	// if(contactPerson.equals(e.getHiringAgentContactPerson().trim()) &&
	// company.equals(e.getHiringAgentCompany().trim())){
	// //System.out.println("q: "+q.getEventDetails().trim()+" e: "+jemsEventService.getEventDetails(e).trim());
	// return true;
	// }else {
	// result = false;
	// }
	// }
	// }
	//
	// if(!result){
	// String[] s2 = q.getEventDetails().trim().split("\n");
	// String startDate="",endDate="",numDrums="",type="";
	//
	// try{startDate = s2[0];}catch(Exception x){}
	// try{endDate = s2[1];}catch(Exception x){}
	// try{numDrums = s2[2];}catch(Exception x){}
	// try{type = s2[3];}catch(Exception x){}
	//
	// String startDate2 =
	// "Start Date: "+dateFormat.format(e.getStartDateTime());
	// String endDate2 = "End Date: "+dateFormat.format(e.getEndDateTime());
	//
	// int numAttendees = 0;
	// if(e.getNumberPeople()==null || e.getNumberPeople()==0){
	// numAttendees = e.getNumberDrums();
	// }else{
	// numAttendees = e.getNumberPeople();
	// }
	//
	// String numDrums2 = "Number Of Attendees: "+numAttendees;
	// String type2 = "Event Type: "+e.getType().toString().replaceAll("_",
	// " ");
	//
	// if(startDate.equals(startDate2) && endDate.equals(endDate2) &&
	// numDrums.equals(numDrums2) && type.equals(type2)){
	// return true;
	// }
	//
	// // if( c(startDate,startDate2) && c(endDate,endDate2) &&
	// c(numDrums,numDrums2) && c(type,type2)){
	// // return true;
	// // }
	//
	// }
	//
	// return result;
	// }
	//
	// private boolean compare(JemsInvoice q, JemsEvent e) {
	// String[] s1 = q.getBillTo().trim().split("\n");
	// String contactPerson="", company="";
	//
	// try{contactPerson = s1[0].trim();}catch(Exception x){}
	// try{company = s1[1].trim();}catch(Exception x){}
	//
	// boolean result = false;
	//
	// if(e.getUseInDocs() !=null ){
	// if(e.getUseInDocs()==0){
	// if(contactPerson.equals(e.getClientContactPerson().trim()) &&
	// company.equals(e.getClientCompany().trim())){
	// //System.out.println("q: "+q.getEventDetails().trim()+" e: "+jemsEventService.getEventDetails(e).trim());
	// return true;
	// }
	// else{
	// result = false;
	// }
	// }else{
	// if(contactPerson.equals(e.getHiringAgentContactPerson().trim()) &&
	// company.equals(e.getHiringAgentCompany().trim())){
	// //System.out.println("q: "+q.getEventDetails().trim()+" e: "+jemsEventService.getEventDetails(e).trim());
	// return true;
	// }else {
	// result = false;
	// }
	// }
	// }
	//
	// if(!result){
	// String[] s2 = q.getEventDetails().trim().split("\n");
	// String startDate="",endDate="",numDrums="",type="";
	//
	// try{startDate = s2[0];}catch(Exception x){}
	// try{endDate = s2[1];}catch(Exception x){}
	// try{numDrums = s2[2];}catch(Exception x){}
	// try{type = s2[3];}catch(Exception x){}
	//
	// String startDate2 =
	// "Start Date: "+dateFormat.format(e.getStartDateTime());
	// String endDate2 = "End Date: "+dateFormat.format(e.getEndDateTime());
	//
	// int numAttendees = 0;
	// if(e.getNumberPeople()==null || e.getNumberPeople()==0){
	// numAttendees = e.getNumberDrums();
	// }else{
	// numAttendees = e.getNumberPeople();
	// }
	//
	// String numDrums2 = "Number Of Attendees: "+numAttendees;
	// String type2 = "Event Type: "+e.getType().toString().replaceAll("_",
	// " ");
	//
	// if(startDate.equals(startDate2) && endDate.equals(endDate2) &&
	// numDrums.equals(numDrums2) && type.equals(type2)){
	// return true;
	// }
	//
	// // if( c(startDate,startDate2) && c(endDate,endDate2) &&
	// c(numDrums,numDrums2) && c(type,type2)){
	// // return true;
	// // }
	//
	// }
	//
	// return result;
	// }
	//
	// //closestring
	// private boolean c(String s1, String s2){
	// s1 = s1.trim();
	// s2 = s2.trim();
	// boolean result = StringUtils.getLevenshteinDistance(s1,s2,2)!=-1;
	// if(result)
	// System.out.println("CHECKING Levenshtein s1: "+s1+", s2: "+s2+", result="+result);
	// return result;
	// }

	@Autowired
	JemsQuotationRepository jemsQuotationRepository;

	@Autowired
	JemsInvoiceRepository jemsInvoiceRepository;

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(method = RequestMethod.GET, value = "/fixqi", produces = "text/html")
	public ResponseEntity<String> fixqi(Principal principal) {

		List<JemsEvent> events = jemsEventService.findAllJemsEvents();

		for (JemsEvent e : events) {
			JemsQuotation q = e.getJemsQuotation();
			JemsInvoice i = e.getJemsInvoice();
			Set<JemsQuotation> quotations = e.getJemsQuotations();
			Set<JemsInvoice> invoices = e.getJemsInvoices();

			int qcount = 0;
			for (JemsQuotation jq : quotations) {
				if (jq.isActive()) {
					qcount++;
				}
			}
			if (qcount > 1) {
				log.info("event: " + e.getId() + " multiple active quotations.");
			}

			int icount = 0;
			for (JemsInvoice ji : invoices) {
				if (ji.isActive()) {
					icount++;
				}
				if (ji.getJemsQuotation() == null
						&& e.getJemsQuotations().size() != 0) {
					log.info("event: " + e.getId() + ", Invoive id: "
							+ ji.getId() + ", iN: " + ji.getINumber()
							+ " No parent quotation.");
				}
			}
			if (icount > 1) {
				log.info("event: " + e.getId() + " multiple active invoices.");
			}

			if (q != null && i != null) {
				q.setJemsInvoice(i);
				i.setJemsQuotation(q);

				jemsQuotationRepository.save(q);
				jemsInvoiceRepository.save(i);

			}
		}

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		return new ResponseEntity<String>("Fix QI: OK", headers, HttpStatus.OK);
	}

}
