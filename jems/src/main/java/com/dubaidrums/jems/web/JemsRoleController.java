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

import com.dubaidrums.jems.domain.JemsRole;
import com.dubaidrums.jems.service.JemsRoleService;

@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping("/jemsroles")
@Controller
public class JemsRoleController {

	@RequestMapping(value = "/{id}", headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> showJson(@PathVariable("id") Long id) {
		JemsRole jemsRole = jemsRoleService.findJemsRole(id);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		if (jemsRole == null) {
			return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<String>(jemsRole.toJson(), headers,
				HttpStatus.OK);
	}

	@RequestMapping(headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> listJson() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		List<JemsRole> result = jemsRoleService.findAllJemsRoles();
		return new ResponseEntity<String>(JemsRole.toJsonArray(result),
				headers, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<String> createFromJson(@RequestBody String json) {
		JemsRole jemsRole = JemsRole.fromJsonToJemsRole(json);
		jemsRoleService.saveJemsRole(jemsRole);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<String> createFromJsonArray(@RequestBody String json) {
		for (JemsRole jemsRole : JemsRole.fromJsonArrayToJemsRoles(json)) {
			jemsRoleService.saveJemsRole(jemsRole);
		}
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}

	@RequestMapping(method = RequestMethod.PUT, headers = "Accept=application/json")
	public ResponseEntity<String> updateFromJson(@RequestBody String json) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		JemsRole jemsRole = JemsRole.fromJsonToJemsRole(json);
		if (jemsRoleService.updateJemsRole(jemsRole) == null) {
			return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<String>(headers, HttpStatus.OK);
	}

	@RequestMapping(value = "/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
	public ResponseEntity<String> updateFromJsonArray(@RequestBody String json) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		for (JemsRole jemsRole : JemsRole.fromJsonArrayToJemsRoles(json)) {
			if (jemsRoleService.updateJemsRole(jemsRole) == null) {
				return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
			}
		}
		return new ResponseEntity<String>(headers, HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
	public ResponseEntity<String> deleteFromJson(@PathVariable("id") Long id) {
		JemsRole jemsRole = jemsRoleService.findJemsRole(id);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		if (jemsRole == null) {
			return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}
		jemsRoleService.deleteJemsRole(jemsRole);
		return new ResponseEntity<String>(headers, HttpStatus.OK);
	}

	@Autowired
	JemsRoleService jemsRoleService;

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
	public String create(@Valid JemsRole jemsRole, BindingResult bindingResult,
			Model uiModel, HttpServletRequest httpServletRequest) {
		if (bindingResult.hasErrors()) {
			populateEditForm(uiModel, jemsRole);
			return "jemsroles/create";
		}
		uiModel.asMap().clear();
		jemsRoleService.saveJemsRole(jemsRole);
		return "redirect:/jemsroles/"
				+ encodeUrlPathSegment(jemsRole.getId().toString(),
						httpServletRequest);
	}

	@RequestMapping(params = "form", produces = "text/html")
	public String createForm(Model uiModel) {
		populateEditForm(uiModel, new JemsRole());
		return "jemsroles/create";
	}

	@RequestMapping(value = "/{id}", produces = "text/html")
	public String show(@PathVariable("id") Long id, Model uiModel) {
		uiModel.addAttribute("jemsrole", jemsRoleService.findJemsRole(id));
		uiModel.addAttribute("itemId", id);
		return "jemsroles/show";
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
			uiModel.addAttribute("jemsroles",
					jemsRoleService.findJemsRoleEntries(firstResult, sizeNo));
			float nrOfPages = (float) jemsRoleService.countAllJemsRoles()
					/ sizeNo;
			uiModel.addAttribute(
					"maxPages",
					(int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1
							: nrOfPages));
		} else {
			uiModel.addAttribute("jemsroles",
					jemsRoleService.findAllJemsRoles());
		}
		return "jemsroles/list";
	}

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
	public String update(@Valid JemsRole jemsRole, BindingResult bindingResult,
			Model uiModel, HttpServletRequest httpServletRequest) {
		if (bindingResult.hasErrors()) {
			populateEditForm(uiModel, jemsRole);
			return "jemsroles/update";
		}
		uiModel.asMap().clear();
		jemsRoleService.updateJemsRole(jemsRole);
		return "redirect:/jemsroles/"
				+ encodeUrlPathSegment(jemsRole.getId().toString(),
						httpServletRequest);
	}

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
	public String updateForm(@PathVariable("id") Long id, Model uiModel) {
		populateEditForm(uiModel, jemsRoleService.findJemsRole(id));
		return "jemsroles/update";
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
	public String delete(@PathVariable("id") Long id,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size,
			Model uiModel) {
		JemsRole jemsRole = jemsRoleService.findJemsRole(id);
		jemsRoleService.deleteJemsRole(jemsRole);
		uiModel.asMap().clear();
		uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
		uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
		return "redirect:/jemsroles";
	}

	void populateEditForm(Model uiModel, JemsRole jemsRole) {
		uiModel.addAttribute("jemsRole", jemsRole);
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
}
