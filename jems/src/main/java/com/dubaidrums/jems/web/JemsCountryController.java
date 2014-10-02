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

import com.dubaidrums.jems.domain.JemsCountry;
import com.dubaidrums.jems.service.JemsCountryService;
import com.dubaidrums.jems.service.JemsCurrencyService;

@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping("/jemscountrys")
@Controller
public class JemsCountryController {

	Logger log = LogManager.getLogger(JemsCountryController.class);

	@Autowired
	JemsCountryService jemsCountryService;

	@Autowired
	JemsCurrencyService jemsCurrencyService;

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
	public String create(@Valid JemsCountry jemsCountry,
			BindingResult bindingResult, Model uiModel,
			HttpServletRequest httpServletRequest, Principal principal) {
		if (bindingResult.hasErrors()) {
			populateEditForm(uiModel, jemsCountry);
			return "jemscountrys/create";
		}
		uiModel.asMap().clear();
		jemsCountryService.saveJemsCountry(jemsCountry);
		// return "redirect:/jemscountrys/" +
		// encodeUrlPathSegment(jemsCountry.getId().toString(),
		// httpServletRequest);
		log.info("user: " + principal.getName()
				+ ", method: create, countryId: " + jemsCountry.getId()
				+ ", msg: new country created");
		return "redirect:/jemscountrys?msg=1";
	}

	@RequestMapping(params = "form", produces = "text/html")
	public String createForm(Model uiModel) {
		populateEditForm(uiModel, new JemsCountry());
		return "jemscountrys/create";
	}

	// @RequestMapping(value = "/{id}", produces = "text/html")
	// public String show(@PathVariable("id") Long id, Model uiModel) {
	// uiModel.addAttribute("jemscountry",
	// jemsCountryService.findJemsCountry(id));
	// uiModel.addAttribute("itemId", id);
	// return "jemscountrys/show";
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
			uiModel.addAttribute("jemscountrys", jemsCountryService
					.findJemsCountryEntries(firstResult, sizeNo));
			float nrOfPages = (float) jemsCountryService.countAllJemsCountrys()
					/ sizeNo;
			uiModel.addAttribute(
					"maxPages",
					(int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1
							: nrOfPages));
		} else {
			if (msg != null && msg == 1) {
				uiModel.addAttribute("msg", "Country sucessfully saved!");
			}
			uiModel.addAttribute("jemscountrys",
					jemsCountryService.findAllJemsCountrys());
		}
		log.info("user: " + principal.getName() + ", method: list, page: "
				+ page + ", msg: countries listed");
		return "jemscountrys/list";
	}

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
	public String update(@Valid JemsCountry jemsCountry,
			BindingResult bindingResult, Model uiModel,
			HttpServletRequest httpServletRequest, Principal principal) {
		if (bindingResult.hasErrors()) {
			populateEditForm(uiModel, jemsCountry);
			return "jemscountrys/update";
		}
		uiModel.asMap().clear();
		jemsCountryService.updateJemsCountry(jemsCountry);
		// return "redirect:/jemscountrys/" +
		// encodeUrlPathSegment(jemsCountry.getId().toString(),
		// httpServletRequest);
		log.info("user: " + principal.getName()
				+ ", method: update, countryId: " + jemsCountry.getId()
				+ ", msg: country updated");
		return "redirect:/jemscountrys?msg=1";
	}

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
	public String updateForm(@PathVariable("id") Long id, Model uiModel) {
		populateEditForm(uiModel, jemsCountryService.findJemsCountry(id));
		return "jemscountrys/update";
	}

	// @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces
	// = "text/html")
	// public String delete(@PathVariable("id") Long id, @RequestParam(value =
	// "page", required = false) Integer page, @RequestParam(value = "size",
	// required = false) Integer size, Model uiModel) {
	// JemsCountry jemsCountry = jemsCountryService.findJemsCountry(id);
	// jemsCountryService.deleteJemsCountry(jemsCountry);
	// uiModel.asMap().clear();
	// uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
	// uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
	// return "redirect:/jemscountrys";
	// }

	void populateEditForm(Model uiModel, JemsCountry jemsCountry) {
		uiModel.addAttribute("jemsCountry", jemsCountry);
		uiModel.addAttribute("jemscurrencys",
				jemsCurrencyService.findAllJemsCurrencys());
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
	// JemsCountry jemsCountry = jemsCountryService.findJemsCountry(id);
	// HttpHeaders headers = new HttpHeaders();
	// headers.add("Content-Type", "application/json; charset=utf-8");
	// if (jemsCountry == null) {
	// return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
	// }
	// return new ResponseEntity<String>(jemsCountry.toJson(), headers,
	// HttpStatus.OK);
	// }
	//
	// @RequestMapping(headers = "Accept=application/json")
	// @ResponseBody
	// public ResponseEntity<String> listJson() {
	// HttpHeaders headers = new HttpHeaders();
	// headers.add("Content-Type", "application/json; charset=utf-8");
	// List<JemsCountry> result = jemsCountryService.findAllJemsCountrys();
	// return new ResponseEntity<String>(JemsCountry.toJsonArray(result),
	// headers, HttpStatus.OK);
	// }
	//
	// @RequestMapping(method = RequestMethod.POST, headers =
	// "Accept=application/json")
	// public ResponseEntity<String> createFromJson(@RequestBody String json) {
	// JemsCountry jemsCountry = JemsCountry.fromJsonToJemsCountry(json);
	// jemsCountryService.saveJemsCountry(jemsCountry);
	// HttpHeaders headers = new HttpHeaders();
	// headers.add("Content-Type", "application/json");
	// return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	// }
	//
	// @RequestMapping(value = "/jsonArray", method = RequestMethod.POST,
	// headers = "Accept=application/json")
	// public ResponseEntity<String> createFromJsonArray(@RequestBody String
	// json) {
	// for (JemsCountry jemsCountry:
	// JemsCountry.fromJsonArrayToJemsCountrys(json)) {
	// jemsCountryService.saveJemsCountry(jemsCountry);
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
	// JemsCountry jemsCountry = JemsCountry.fromJsonToJemsCountry(json);
	// if (jemsCountryService.updateJemsCountry(jemsCountry) == null) {
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
	// for (JemsCountry jemsCountry:
	// JemsCountry.fromJsonArrayToJemsCountrys(json)) {
	// if (jemsCountryService.updateJemsCountry(jemsCountry) == null) {
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
	// JemsCountry jemsCountry = jemsCountryService.findJemsCountry(id);
	// HttpHeaders headers = new HttpHeaders();
	// headers.add("Content-Type", "application/json");
	// if (jemsCountry == null) {
	// return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
	// }
	// jemsCountryService.deleteJemsCountry(jemsCountry);
	// return new ResponseEntity<String>(headers, HttpStatus.OK);
	// }
}
