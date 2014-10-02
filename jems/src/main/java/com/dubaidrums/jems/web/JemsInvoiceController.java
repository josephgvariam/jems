package com.dubaidrums.jems.web;

import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

import com.dubaidrums.jems.domain.JemsEvent;
import com.dubaidrums.jems.domain.JemsInvoice;
import com.dubaidrums.jems.domain.JemsQuotation;
import com.dubaidrums.jems.domain.JemsUser;
import com.dubaidrums.jems.service.JemsEmailService;
import com.dubaidrums.jems.service.JemsEventService;
import com.dubaidrums.jems.service.JemsInvoiceService;
import com.dubaidrums.jems.service.JemsLogService;
import com.dubaidrums.jems.service.JemsQuotationService;
import com.dubaidrums.jems.service.JemsReportingService;
import com.dubaidrums.jems.service.JemsUserService;
import com.dubaidrums.jems.service.JemsUtilService;

@RequestMapping("/jemsinvoices")
@Controller
public class JemsInvoiceController {

	Logger log = LogManager.getLogger(JemsInvoiceController.class);

	@Autowired
	JemsInvoiceService jemsInvoiceService;

	@Autowired
	JemsEventService jemsEventService;

	@Autowired
	JemsUtilService jemsUtilService;

	@Autowired
	JemsReportingService jemsReportingService;

	@Autowired
	JemsLogService jemsLogService;

	@Autowired
	JemsEmailService jemsEmailService;

	@Autowired
	JemsQuotationService jemsQuotationService;

	@Autowired
	JemsUserService jemsUserService;

	private DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER','ROLE_OPERATOR')")
	@RequestMapping(value = "/{id}/pdf", method = RequestMethod.GET, produces = "application/pdf")
	public String generatePdf(@PathVariable("id") Long id,
			HttpServletRequest httpServletRequest, Principal principal) {
		JemsInvoice ji = jemsInvoiceService.findJemsInvoice(id);
		try {
			log.info("user: " + principal.getName()
					+ ", method: generatePdf, eventId: "
					+ ji.getJemsEvent().getId()
					+ ", msg: generating invoice pdf");
			String fileName = jemsReportingService.generateInvoicePdf(ji);

			jemsLogService.newLog(principal.getName()
					+ " generated a PDF invoice: <a href='/jemsinvoices/" + id
					+ "'>" + ji.getINumber()
					+ "</a> for event: <a href='/jemsevents/"
					+ ji.getJemsEvent().getId() + "'>"
					+ ji.getJemsEvent().getTitle() + "</a>");
			jemsEmailService.sendEmail("PDF Invoice Generated: "
					+ ji.getJemsEvent().getTitle(), principal.getName()
					+ " generated a PDF invoice: <a href='/jemsinvoices/" + id
					+ "'>" + ji.getINumber()
					+ "</a> for event: <a href='/jemsevents/"
					+ ji.getJemsEvent().getId() + "'>"
					+ ji.getJemsEvent().getTitle() + "</a>", fileName);

			return "redirect:/jemsinvoices/pdf/"
					+ encodeUrlPathSegment(fileName, httpServletRequest);
		} catch (Exception e) {
			log.error("Unable to generate PDF!", e);
			throw new RuntimeException("Unable to generate PDF!", e);
		}
	}

	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER','ROLE_OPERATOR')")
	@RequestMapping(value = "/pdf/{fileName}", method = RequestMethod.GET, produces = "application/pdf")
	@ResponseBody
	public FileSystemResource getPdfFile(
			@PathVariable("fileName") String fileName) {
		return new FileSystemResource(System.getProperty("java.io.tmpdir")
				+ "/" + fileName + ".pdf");
	}

	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER','ROLE_OPERATOR')")
	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
	public String create(@Valid JemsInvoice jemsInvoice,
			BindingResult bindingResult, Model uiModel,
			HttpServletRequest httpServletRequest, Principal principal) {
		if (jemsInvoice.getIDate() == null) {
			bindingResult.rejectValue("iDate", "date.invalid");
		}

		if (bindingResult.hasErrors()) {
			populateEditForm(uiModel, jemsInvoice);
			return "jemsinvoices/create";
		}
		uiModel.asMap().clear();

		JemsUser user = jemsUserService.findJemsUserByUserName(principal
				.getName());
		jemsInvoice.setActive(true);
		Date nd = new Date();
		jemsInvoice.setCreatedDate(nd);
		jemsInvoice.setCreatedUser(user);
		jemsInvoice.setModifiedDate(nd);
		jemsInvoice.setModifiedUser(user);

		jemsInvoiceService.saveNewJemsInvoice(jemsInvoice);

		jemsLogService.newLog(principal.getName()
				+ " created a new invoice: <a href='/jemsinvoices/"
				+ jemsInvoice.getId() + "'>" + jemsInvoice.getINumber()
				+ "</a> for event: <a href='/jemsevents/"
				+ jemsInvoice.getJemsEvent().getId() + "'>"
				+ jemsInvoice.getJemsEvent().getTitle() + "</a>");
		jemsEmailService.sendEmail("Invoice Created: "
				+ jemsInvoice.getJemsEvent().getTitle(), principal.getName()
				+ " created a new invoice: <a href='/jemsinvoices/"
				+ jemsInvoice.getId() + "'>" + jemsInvoice.getINumber()
				+ "</a> for event: <a href='/jemsevents/"
				+ jemsInvoice.getJemsEvent().getId() + "'>"
				+ jemsInvoice.getJemsEvent().getTitle() + "</a>", null);
		log.info("user: " + principal.getName() + ", method: create, eventId: "
				+ jemsInvoice.getJemsEvent().getId() + ", invoiceId: "
				+ jemsInvoice.getId() + ", invoiceNumber: "
				+ jemsInvoice.getINumber() + ", msg: new invoice created");

		return "redirect:/jemsinvoices/"
				+ encodeUrlPathSegment(jemsInvoice.getId().toString(),
						httpServletRequest) + "?msg=1";
	}

	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER','ROLE_OPERATOR')")
	@RequestMapping(params = "form", produces = "text/html")
	public String createForm(
			@RequestParam(value = "jemsEventId", required = true) Long jemsEventId,
			@RequestParam(value = "jemsQuotationId", required = false) Long jemsQuotationId,
			Model uiModel) {
		JemsEvent je = jemsEventService.findJemsEvent(jemsEventId);
		JemsQuotation jq = null;

		if (jemsQuotationId != null) {
			jq = jemsQuotationService.findJemsQuotation(jemsQuotationId);
		}
		JemsInvoice ji = new JemsInvoice();

		ji.setINumber(1);
		ji.setIDate(jemsUtilService.getCurrentDate());
		ji.setJemsEvent(je);
		if (jq != null) {
			jq.setJemsInvoice(ji);
			ji.setJemsQuotation(jq);
		}

		if (jq != null) {
			ji.setBillTo(jq.getBillTo());
			ji.setEventDetails(jq.getEventDetails());
			ji.setPaymentTerms(jq.getPaymentTerms());
			ji.setAmount1(jq.getAmount1());
			ji.setAmount2(jq.getAmount2());
			ji.setAmount3(jq.getAmount3());
			ji.setAmount4(jq.getAmount4());
			ji.setAmount5(jq.getAmount5());
			ji.setDescription1(jq.getDescription1());
			ji.setDescription2(jq.getDescription2());
			ji.setDescription3(jq.getDescription3());
			ji.setDescription4(jq.getDescription4());
			ji.setDescription5(jq.getDescription5());

			ji.setTaxes(jq.getTaxes());
		} else {
			ji.setBillTo(jemsEventService.getBillTo(je));
			ji.setEventDetails(jemsEventService.getEventDetails(je));
			ji.setPaymentTerms("Full Amount on day of event.\nCancellation 3 days prior to event 100% payment required.\nIn the event of cancellation 50% due if 1 week before the event. 25% if 2 weeks prior.\nNo charge for cancellation over two weeks prior to the event.");

			ji.setTaxes(new HashSet());
		}

		populateEditForm(uiModel, ji);
		// uiModel.addAttribute("jemsEventId", jemsEventId);

		return "jemsinvoices/create";
	}

	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER','ROLE_OPERATOR')")
	@RequestMapping(value = "/{id}", produces = "text/html")
	public String show(@PathVariable("id") Long id,
			@RequestParam(value = "msg", required = false) Integer msg,
			Model uiModel, Principal principal) {
		JemsInvoice ji = jemsInvoiceService.findJemsInvoice(id);
		uiModel.addAttribute("jemsinvoice", ji);
		// uiModel.addAttribute("jemsEventId", jemsEventId);

		if (ji.isActive()) {
			uiModel.addAttribute(
					"previousJemsInvoices",
					jemsInvoiceService.findByActiveAndJemsEvent(false,
							ji.getJemsEvent()));
		}

		if (msg != null && msg == 1) {
			uiModel.addAttribute("msg", "Invoice sucessfully saved!");
		}

		log.info("user: " + principal.getName() + ", method: show, eventId: "
				+ ji.getJemsEvent().getId() + ", invoiceId: " + id
				+ ", invoiceNumber: " + ji.getINumber()
				+ ", msg: invoice showed");

		return "jemsinvoices/show";
	}

	// @RequestMapping(produces = "text/html")
	// public String list(@RequestParam(value = "page", required = false)
	// Integer page, @RequestParam(value = "size", required = false) Integer
	// size, Model uiModel) {
	// if (page != null || size != null) {
	// int sizeNo = size == null ? 10 : size.intValue();
	// final int firstResult = page == null ? 0 : (page.intValue() - 1) *
	// sizeNo;
	// uiModel.addAttribute("jemsinvoices",
	// jemsInvoiceService.findJemsInvoiceEntries(firstResult, sizeNo));
	// float nrOfPages = (float) jemsInvoiceService.countAllJemsInvoices() /
	// sizeNo;
	// uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages ||
	// nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
	// } else {
	// uiModel.addAttribute("jemsinvoices",
	// jemsInvoiceService.findAllJemsInvoices());
	// }
	// //addDateTimeFormatPatterns(uiModel);
	// return "jemsinvoices/list";
	// }

	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER','ROLE_OPERATOR')")
	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
	public String update(@Valid JemsInvoice jemsInvoice,
			BindingResult bindingResult, Model uiModel,
			HttpServletRequest httpServletRequest, Principal principal) {
		if (bindingResult.hasErrors()) {
			populateEditForm(uiModel, jemsInvoice);
			return "jemsinvoices/update";
		}
		uiModel.asMap().clear();
		jemsInvoice.setModifiedUser(jemsUserService
				.findJemsUserByUserName(principal.getName()));
		jemsInvoice = jemsInvoiceService.updateJemsInvoice(jemsInvoice);

		jemsLogService.newLog(principal.getName()
				+ " updated an invoice: <a href='/jemsinvoices/"
				+ jemsInvoice.getId() + "'>" + jemsInvoice.getINumber()
				+ "</a> for event: <a href='/jemsevents/"
				+ jemsInvoice.getJemsEvent().getId() + "'>"
				+ jemsInvoice.getJemsEvent().getTitle() + "</a>");
		jemsEmailService.sendEmail("Invoice Updated: "
				+ jemsInvoice.getJemsEvent().getTitle(),
				principal.getName()
						+ " updated an invoice: <a href='/jemsinvoices/"
						+ jemsInvoice.getId() + "'>" + jemsInvoice.getINumber()
						+ "</a> for event: <a href='/jemsevents/"
						+ jemsInvoice.getJemsEvent().getId() + "'>"
						+ jemsInvoice.getJemsEvent().getTitle() + "</a>", null);
		log.info("user: " + principal.getName() + ", method: update, eventId: "
				+ jemsInvoice.getJemsEvent().getId() + ", invoiceId: "
				+ jemsInvoice.getId() + ", invoiceNumber: "
				+ jemsInvoice.getINumber() + ", msg: invoice updated");

		// uiModel.addAttribute("jemsEventId", jemsEventId);
		uiModel.addAttribute("msg", 1);
		return "redirect:/jemsinvoices/"
				+ encodeUrlPathSegment(jemsInvoice.getId().toString(),
						httpServletRequest);
	}

	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER','ROLE_OPERATOR')")
	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
	public String updateForm(@PathVariable("id") Long id, Model uiModel) {
		populateEditForm(uiModel, jemsInvoiceService.findJemsInvoice(id));
		// uiModel.addAttribute("jemsEventId", jemsEventId);
		return "jemsinvoices/update";
	}

	// @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces
	// = "text/html")
	// public String delete(@PathVariable("id") Long id, @RequestParam(value =
	// "page", required = false) Integer page, @RequestParam(value = "size",
	// required = false) Integer size, Model uiModel) {
	// JemsInvoice jemsInvoice = jemsInvoiceService.findJemsInvoice(id);
	// jemsInvoiceService.deleteJemsInvoice(jemsInvoice);
	// uiModel.asMap().clear();
	// uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
	// uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
	// return "redirect:/jemsinvoices";
	// }

	void populateEditForm(Model uiModel, JemsInvoice jemsInvoice) {
		uiModel.addAttribute("jemsInvoice", jemsInvoice);
		// uiModel.addAttribute("jemstaxes", jemsTaxRepository.findAll());
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
	// JemsInvoice jemsInvoice = jemsInvoiceService.findJemsInvoice(id);
	// HttpHeaders headers = new HttpHeaders();
	// headers.add("Content-Type", "application/json; charset=utf-8");
	// if (jemsInvoice == null) {
	// return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
	// }
	// return new ResponseEntity<String>(jemsInvoice.toJson(), headers,
	// HttpStatus.OK);
	// }
	//
	// @RequestMapping(headers = "Accept=application/json")
	// @ResponseBody
	// public ResponseEntity<String> listJson() {
	// HttpHeaders headers = new HttpHeaders();
	// headers.add("Content-Type", "application/json; charset=utf-8");
	// List<JemsInvoice> result = jemsInvoiceService.findAllJemsInvoices();
	// return new ResponseEntity<String>(JemsInvoice.toJsonArray(result),
	// headers, HttpStatus.OK);
	// }
	//
	// @RequestMapping(method = RequestMethod.POST, headers =
	// "Accept=application/json")
	// public ResponseEntity<String> createFromJson(@RequestBody String json) {
	// JemsInvoice jemsInvoice = JemsInvoice.fromJsonToJemsInvoice(json);
	// jemsInvoiceService.saveJemsInvoice(jemsInvoice);
	// HttpHeaders headers = new HttpHeaders();
	// headers.add("Content-Type", "application/json");
	// return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	// }
	//
	// @RequestMapping(value = "/jsonArray", method = RequestMethod.POST,
	// headers = "Accept=application/json")
	// public ResponseEntity<String> createFromJsonArray(@RequestBody String
	// json) {
	// for (JemsInvoice jemsInvoice:
	// JemsInvoice.fromJsonArrayToJemsInvoices(json)) {
	// jemsInvoiceService.saveJemsInvoice(jemsInvoice);
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
	// JemsInvoice jemsInvoice = JemsInvoice.fromJsonToJemsInvoice(json);
	// if (jemsInvoiceService.updateJemsInvoice(jemsInvoice) == null) {
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
	// for (JemsInvoice jemsInvoice:
	// JemsInvoice.fromJsonArrayToJemsInvoices(json)) {
	// if (jemsInvoiceService.updateJemsInvoice(jemsInvoice) == null) {
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
	// JemsInvoice jemsInvoice = jemsInvoiceService.findJemsInvoice(id);
	// HttpHeaders headers = new HttpHeaders();
	// headers.add("Content-Type", "application/json");
	// if (jemsInvoice == null) {
	// return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
	// }
	// jemsInvoiceService.deleteJemsInvoice(jemsInvoice);
	// return new ResponseEntity<String>(headers, HttpStatus.OK);
	// }

}
