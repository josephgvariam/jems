package com.dubaidrums.jems.web;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.dubaidrums.jems.domain.JemsOrganization;
import com.dubaidrums.jems.service.JemsCountryService;
import com.dubaidrums.jems.service.JemsCurrencyService;
import com.dubaidrums.jems.service.JemsOrganizationService;
import com.dubaidrums.jems.service.JemsRegionService;

import flexjson.JSONSerializer;

@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping("/jemsorganizations")
@Controller
public class JemsOrganizationController {

	Logger log = LogManager.getLogger(JemsOrganizationController.class);

	@Autowired
	JemsOrganizationService jemsOrganizationService;

	@Autowired
	JemsCountryService jemsCountryService;

	@Autowired
	JemsCurrencyService jemsCurrencyService;

	@Autowired
	JemsRegionService jemsRegionService;

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
	public String create(@Valid JemsOrganization jemsOrganization,
			BindingResult bindingResult, Model uiModel,
			HttpServletRequest httpServletRequest, Principal principal) {
		if (bindingResult.hasErrors()) {
			populateEditForm(uiModel, jemsOrganization);
			return "jemsorganizations/create";
		}
		uiModel.asMap().clear();
		jemsOrganizationService.saveJemsOrganization(jemsOrganization);
		// return "redirect:/jemsorganizations/" +
		// encodeUrlPathSegment(jemsOrganization.getId().toString(),
		// httpServletRequest);
		log.info("user: " + principal.getName()
				+ ", method: create, organizationId: "
				+ jemsOrganization.getId() + ", msg: new organization created");
		return "redirect:/jemsorganizations?msg=1";
	}

	@RequestMapping(params = "form", produces = "text/html")
	public String createForm(Model uiModel) {
		populateEditForm(uiModel, new JemsOrganization());
		return "jemsorganizations/create";
	}

	// @RequestMapping(value = "/{id}", produces = "text/html")
	// public String show(@PathVariable("id") Long id, Model uiModel) {
	// uiModel.addAttribute("jemsorganization",
	// jemsOrganizationService.findJemsOrganization(id));
	// uiModel.addAttribute("itemId", id);
	// return "jemsorganizations/show";
	// }

	@RequestMapping(produces = "text/html")
	public String list(
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size,
			@RequestParam(value = "msg", required = false) Integer msg,
			Model uiModel, Principal principal) {
		if (page != null || size != null) {
			int sizeNo = size == null ? 10 : size.intValue();
			final int firstResult = page == null ? 0 : (page.intValue() - 1)
					* sizeNo;
			uiModel.addAttribute("jemsorganizations", jemsOrganizationService
					.findJemsOrganizationEntries(firstResult, sizeNo));
			float nrOfPages = (float) jemsOrganizationService
					.countAllJemsOrganizations() / sizeNo;
			uiModel.addAttribute(
					"maxPages",
					(int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1
							: nrOfPages));
		} else {
			if (msg != null && msg == 1) {
				uiModel.addAttribute("msg", "Organization sucessfully saved!");
			}
			uiModel.addAttribute("jemsorganizations",
					jemsOrganizationService.findAllJemsOrganizations());
		}
		log.info("user: " + principal.getName() + ", method: list, page: "
				+ page + ", msg: organizations listed");
		return "jemsorganizations/list";
	}

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
	public String update(@Valid JemsOrganization jemsOrganization,
			BindingResult bindingResult, Model uiModel,
			HttpServletRequest httpServletRequest, Principal principal) {
		if (bindingResult.hasErrors()) {
			populateEditForm(uiModel, jemsOrganization);
			return "jemsorganizations/update";
		}
		uiModel.asMap().clear();
		jemsOrganizationService.updateJemsOrganization(jemsOrganization);
		// return "redirect:/jemsorganizations/" +
		// encodeUrlPathSegment(jemsOrganization.getId().toString(),
		// httpServletRequest);
		log.info("user: " + principal.getName()
				+ ", method: update, organizationId: "
				+ jemsOrganization.getId() + ", msg: organization updated");
		return "redirect:/jemsorganizations?msg=1";
	}

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
	public String updateForm(@PathVariable("id") Long id, Model uiModel) {
		populateEditForm(uiModel,
				jemsOrganizationService.findJemsOrganization(id));
		return "jemsorganizations/update";
	}

	// @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces
	// = "text/html")
	// public String delete(@PathVariable("id") Long id, @RequestParam(value =
	// "page", required = false) Integer page, @RequestParam(value = "size",
	// required = false) Integer size, Model uiModel) {
	// JemsOrganization jemsOrganization =
	// jemsOrganizationService.findJemsOrganization(id);
	// jemsOrganizationService.deleteJemsOrganization(jemsOrganization);
	// uiModel.asMap().clear();
	// uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
	// uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
	// return "redirect:/jemsorganizations";
	// }

	void populateEditForm(Model uiModel, JemsOrganization jemsOrganization) {
		uiModel.addAttribute("jemsOrganization", jemsOrganization);
		// uiModel.addAttribute("jemscountrys",
		// jemsCountryService.findAllJemsCountrys());
		// uiModel.addAttribute("jemscurrencys",
		// jemsCurrencyService.findAllJemsCurrencys());
		// uiModel.addAttribute("jemsregions",
		// jemsRegionService.findAllJemsRegions());

		uiModel.addAttribute("jemscountrys",
				jemsCountryService.findActiveJemsCountrys());
		uiModel.addAttribute("jemscurrencys",
				jemsCurrencyService.findActiveJemsCurrencys());
		uiModel.addAttribute("jemsregions",
				jemsRegionService.findActiveJemsRegions());
		uiModel.addAttribute(
				"jemsRegionsJSON",
				new JSONSerializer().exclude("*.class").serialize(
						jemsRegionService.findActiveJemsRegions()));
	}

	// String encodeUrlPathSegment(String pathSegment, HttpServletRequest
	// httpServletRequest) {
	// String enc = httpServletRequest.getCharacterEncoding();
	// if (enc == null) {
	// enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
	// }
	// try {
	// pathSegment = UriUtils.encodePathSegment(pathSegment, enc);
	// } catch (UnsupportedEncodingException uee) {}
	// return pathSegment;
	// }
	//
	// @RequestMapping(value = "/{id}", headers = "Accept=application/json")
	// @ResponseBody
	// public ResponseEntity<String> showJson(@PathVariable("id") Long id) {
	// JemsOrganization jemsOrganization =
	// jemsOrganizationService.findJemsOrganization(id);
	// HttpHeaders headers = new HttpHeaders();
	// headers.add("Content-Type", "application/json; charset=utf-8");
	// if (jemsOrganization == null) {
	// return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
	// }
	// return new ResponseEntity<String>(jemsOrganization.toJson(), headers,
	// HttpStatus.OK);
	// }
	//
	// @RequestMapping(headers = "Accept=application/json")
	// @ResponseBody
	// public ResponseEntity<String> listJson() {
	// HttpHeaders headers = new HttpHeaders();
	// headers.add("Content-Type", "application/json; charset=utf-8");
	// List<JemsOrganization> result =
	// jemsOrganizationService.findAllJemsOrganizations();
	// return new ResponseEntity<String>(JemsOrganization.toJsonArray(result),
	// headers, HttpStatus.OK);
	// }
	//
	// @RequestMapping(method = RequestMethod.POST, headers =
	// "Accept=application/json")
	// public ResponseEntity<String> createFromJson(@RequestBody String json) {
	// JemsOrganization jemsOrganization =
	// JemsOrganization.fromJsonToJemsOrganization(json);
	// jemsOrganizationService.saveJemsOrganization(jemsOrganization);
	// HttpHeaders headers = new HttpHeaders();
	// headers.add("Content-Type", "application/json");
	// return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	// }
	//
	// @RequestMapping(value = "/jsonArray", method = RequestMethod.POST,
	// headers = "Accept=application/json")
	// public ResponseEntity<String> createFromJsonArray(@RequestBody String
	// json) {
	// for (JemsOrganization jemsOrganization:
	// JemsOrganization.fromJsonArrayToJemsOrganizations(json)) {
	// jemsOrganizationService.saveJemsOrganization(jemsOrganization);
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
	// JemsOrganization jemsOrganization =
	// JemsOrganization.fromJsonToJemsOrganization(json);
	// if (jemsOrganizationService.updateJemsOrganization(jemsOrganization) ==
	// null) {
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
	// for (JemsOrganization jemsOrganization:
	// JemsOrganization.fromJsonArrayToJemsOrganizations(json)) {
	// if (jemsOrganizationService.updateJemsOrganization(jemsOrganization) ==
	// null) {
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
	// JemsOrganization jemsOrganization =
	// jemsOrganizationService.findJemsOrganization(id);
	// HttpHeaders headers = new HttpHeaders();
	// headers.add("Content-Type", "application/json");
	// if (jemsOrganization == null) {
	// return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
	// }
	// jemsOrganizationService.deleteJemsOrganization(jemsOrganization);
	// return new ResponseEntity<String>(headers, HttpStatus.OK);
	// }
}
