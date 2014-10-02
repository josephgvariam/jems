package com.dubaidrums.jems.web;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

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

import com.dubaidrums.jems.domain.JemsCostingItem;
import com.dubaidrums.jems.service.JemsCostingItemService;
import com.dubaidrums.jems.service.JemsOrganizationService;

@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping("/jemscostingitems")
@Controller
public class JemsCostingItemController {

	@Autowired
	JemsCostingItemService jemsCostingItemService;

	@Autowired
	JemsOrganizationService jemsOrganizationService;

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
	public String create(@Valid JemsCostingItem jemsCostingItem,
			BindingResult bindingResult, Model uiModel,
			HttpServletRequest httpServletRequest) {
		if (bindingResult.hasErrors()) {
			populateEditForm(uiModel, jemsCostingItem);
			return "jemscostingitems/create";
		}
		uiModel.asMap().clear();
		jemsCostingItemService.saveJemsCostingItem(jemsCostingItem);
		return "redirect:/jemscostingitems/"
				+ encodeUrlPathSegment(jemsCostingItem.getId().toString(),
						httpServletRequest);
	}

	@RequestMapping(params = "form", produces = "text/html")
	public String createForm(Model uiModel) {
		populateEditForm(uiModel, new JemsCostingItem());
		return "jemscostingitems/create";
	}

	@RequestMapping(value = "/{id}", produces = "text/html")
	public String show(@PathVariable("id") Long id, Model uiModel) {
		uiModel.addAttribute("jemscostingitem",
				jemsCostingItemService.findJemsCostingItem(id));
		uiModel.addAttribute("itemId", id);
		return "jemscostingitems/show";
	}

	@RequestMapping(produces = "text/html")
	public String list(
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size,
			Model uiModel) {
		if (page != null || size != null) {
			int sizeNo = size == null ? 10 : size.intValue();
			final int firstResult = page == null ? 0 : (page.intValue() - 1)
					* sizeNo;
			uiModel.addAttribute("jemscostingitems", jemsCostingItemService
					.findJemsCostingItemEntries(firstResult, sizeNo));
			float nrOfPages = (float) jemsCostingItemService
					.countAllJemsCostingItems() / sizeNo;
			uiModel.addAttribute(
					"maxPages",
					(int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1
							: nrOfPages));
		} else {
			uiModel.addAttribute("jemscostingitems",
					jemsCostingItemService.findAllJemsCostingItems());
		}
		return "jemscostingitems/list";
	}

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
	public String update(@Valid JemsCostingItem jemsCostingItem,
			BindingResult bindingResult, Model uiModel,
			HttpServletRequest httpServletRequest) {
		if (bindingResult.hasErrors()) {
			populateEditForm(uiModel, jemsCostingItem);
			return "jemscostingitems/update";
		}
		uiModel.asMap().clear();
		jemsCostingItemService.updateJemsCostingItem(jemsCostingItem);
		return "redirect:/jemscostingitems/"
				+ encodeUrlPathSegment(jemsCostingItem.getId().toString(),
						httpServletRequest);
	}

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
	public String updateForm(@PathVariable("id") Long id, Model uiModel) {
		populateEditForm(uiModel,
				jemsCostingItemService.findJemsCostingItem(id));
		return "jemscostingitems/update";
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
	public String delete(@PathVariable("id") Long id,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size,
			Model uiModel) {
		JemsCostingItem jemsCostingItem = jemsCostingItemService
				.findJemsCostingItem(id);
		jemsCostingItemService.deleteJemsCostingItem(jemsCostingItem);
		uiModel.asMap().clear();
		uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
		uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
		return "redirect:/jemscostingitems";
	}

	void populateEditForm(Model uiModel, JemsCostingItem jemsCostingItem) {
		uiModel.addAttribute("jemsCostingItem", jemsCostingItem);
		uiModel.addAttribute("jemsorganizations",
				jemsOrganizationService.findAllJemsOrganizations());
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

	@RequestMapping(value = "/{id}", headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> showJson(@PathVariable("id") Long id) {
		JemsCostingItem jemsCostingItem = jemsCostingItemService
				.findJemsCostingItem(id);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		if (jemsCostingItem == null) {
			return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<String>(jemsCostingItem.toJson(), headers,
				HttpStatus.OK);
	}

	@RequestMapping(headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> listJson() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		List<JemsCostingItem> result = jemsCostingItemService
				.findAllJemsCostingItems();
		return new ResponseEntity<String>(JemsCostingItem.toJsonArray(result),
				headers, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<String> createFromJson(@RequestBody String json) {
		JemsCostingItem jemsCostingItem = JemsCostingItem
				.fromJsonToJemsCostingItem(json);
		jemsCostingItemService.saveJemsCostingItem(jemsCostingItem);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<String> createFromJsonArray(@RequestBody String json) {
		for (JemsCostingItem jemsCostingItem : JemsCostingItem
				.fromJsonArrayToJemsCostingItems(json)) {
			jemsCostingItemService.saveJemsCostingItem(jemsCostingItem);
		}
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}

	@RequestMapping(method = RequestMethod.PUT, headers = "Accept=application/json")
	public ResponseEntity<String> updateFromJson(@RequestBody String json) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		JemsCostingItem jemsCostingItem = JemsCostingItem
				.fromJsonToJemsCostingItem(json);
		if (jemsCostingItemService.updateJemsCostingItem(jemsCostingItem) == null) {
			return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<String>(headers, HttpStatus.OK);
	}

	@RequestMapping(value = "/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
	public ResponseEntity<String> updateFromJsonArray(@RequestBody String json) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		for (JemsCostingItem jemsCostingItem : JemsCostingItem
				.fromJsonArrayToJemsCostingItems(json)) {
			if (jemsCostingItemService.updateJemsCostingItem(jemsCostingItem) == null) {
				return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
			}
		}
		return new ResponseEntity<String>(headers, HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
	public ResponseEntity<String> deleteFromJson(@PathVariable("id") Long id) {
		JemsCostingItem jemsCostingItem = jemsCostingItemService
				.findJemsCostingItem(id);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		if (jemsCostingItem == null) {
			return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}
		jemsCostingItemService.deleteJemsCostingItem(jemsCostingItem);
		return new ResponseEntity<String>(headers, HttpStatus.OK);
	}
}
