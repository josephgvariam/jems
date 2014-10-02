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

import com.dubaidrums.jems.domain.JemsCostingCategory;
import com.dubaidrums.jems.service.JemsCostingCategoryService;
import com.dubaidrums.jems.service.JemsCostingSubCategoryService;
import com.dubaidrums.jems.service.JemsOrganizationService;

@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping("/jemscostingcategorys")
@Controller
public class JemsCostingCategoryController {

	@Autowired
	JemsCostingCategoryService jemsCostingCategoryService;

	@Autowired
	JemsCostingSubCategoryService jemsCostingSubCategoryService;

	@Autowired
	JemsOrganizationService jemsOrganizationService;

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
	public String create(@Valid JemsCostingCategory jemsCostingCategory,
			BindingResult bindingResult, Model uiModel,
			HttpServletRequest httpServletRequest) {
		if (bindingResult.hasErrors()) {
			populateEditForm(uiModel, jemsCostingCategory);
			return "jemscostingcategorys/create";
		}
		uiModel.asMap().clear();
		jemsCostingCategoryService.saveJemsCostingCategory(jemsCostingCategory);
		return "redirect:/jemscostingcategorys/"
				+ encodeUrlPathSegment(jemsCostingCategory.getId().toString(),
						httpServletRequest);
	}

	@RequestMapping(params = "form", produces = "text/html")
	public String createForm(Model uiModel) {
		populateEditForm(uiModel, new JemsCostingCategory());
		return "jemscostingcategorys/create";
	}

	@RequestMapping(value = "/{id}", produces = "text/html")
	public String show(@PathVariable("id") Long id, Model uiModel) {
		uiModel.addAttribute("jemscostingcategory",
				jemsCostingCategoryService.findJemsCostingCategory(id));
		uiModel.addAttribute("itemId", id);
		return "jemscostingcategorys/show";
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
			uiModel.addAttribute("jemscostingcategorys",
					jemsCostingCategoryService.findJemsCostingCategoryEntries(
							firstResult, sizeNo));
			float nrOfPages = (float) jemsCostingCategoryService
					.countAllJemsCostingCategorys() / sizeNo;
			uiModel.addAttribute(
					"maxPages",
					(int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1
							: nrOfPages));
		} else {
			uiModel.addAttribute("jemscostingcategorys",
					jemsCostingCategoryService.findAllJemsCostingCategorys());
		}
		return "jemscostingcategorys/list";
	}

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
	public String update(@Valid JemsCostingCategory jemsCostingCategory,
			BindingResult bindingResult, Model uiModel,
			HttpServletRequest httpServletRequest) {
		if (bindingResult.hasErrors()) {
			populateEditForm(uiModel, jemsCostingCategory);
			return "jemscostingcategorys/update";
		}
		uiModel.asMap().clear();
		jemsCostingCategoryService
				.updateJemsCostingCategory(jemsCostingCategory);
		return "redirect:/jemscostingcategorys/"
				+ encodeUrlPathSegment(jemsCostingCategory.getId().toString(),
						httpServletRequest);
	}

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
	public String updateForm(@PathVariable("id") Long id, Model uiModel) {
		populateEditForm(uiModel,
				jemsCostingCategoryService.findJemsCostingCategory(id));
		return "jemscostingcategorys/update";
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
	public String delete(@PathVariable("id") Long id,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size,
			Model uiModel) {
		JemsCostingCategory jemsCostingCategory = jemsCostingCategoryService
				.findJemsCostingCategory(id);
		jemsCostingCategoryService
				.deleteJemsCostingCategory(jemsCostingCategory);
		uiModel.asMap().clear();
		uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
		uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
		return "redirect:/jemscostingcategorys";
	}

	void populateEditForm(Model uiModel, JemsCostingCategory jemsCostingCategory) {
		uiModel.addAttribute("jemsCostingCategory", jemsCostingCategory);
		uiModel.addAttribute("jemscostingsubcategorys",
				jemsCostingSubCategoryService.findAllJemsCostingSubCategorys());
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
		JemsCostingCategory jemsCostingCategory = jemsCostingCategoryService
				.findJemsCostingCategory(id);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		if (jemsCostingCategory == null) {
			return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<String>(jemsCostingCategory.toJson(),
				headers, HttpStatus.OK);
	}

	@RequestMapping(headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> listJson() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		List<JemsCostingCategory> result = jemsCostingCategoryService
				.findAllJemsCostingCategorys();
		return new ResponseEntity<String>(
				JemsCostingCategory.toJsonArray(result), headers, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<String> createFromJson(@RequestBody String json) {
		JemsCostingCategory jemsCostingCategory = JemsCostingCategory
				.fromJsonToJemsCostingCategory(json);
		jemsCostingCategoryService.saveJemsCostingCategory(jemsCostingCategory);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<String> createFromJsonArray(@RequestBody String json) {
		for (JemsCostingCategory jemsCostingCategory : JemsCostingCategory
				.fromJsonArrayToJemsCostingCategorys(json)) {
			jemsCostingCategoryService
					.saveJemsCostingCategory(jemsCostingCategory);
		}
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}

	@RequestMapping(method = RequestMethod.PUT, headers = "Accept=application/json")
	public ResponseEntity<String> updateFromJson(@RequestBody String json) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		JemsCostingCategory jemsCostingCategory = JemsCostingCategory
				.fromJsonToJemsCostingCategory(json);
		if (jemsCostingCategoryService
				.updateJemsCostingCategory(jemsCostingCategory) == null) {
			return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<String>(headers, HttpStatus.OK);
	}

	@RequestMapping(value = "/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
	public ResponseEntity<String> updateFromJsonArray(@RequestBody String json) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		for (JemsCostingCategory jemsCostingCategory : JemsCostingCategory
				.fromJsonArrayToJemsCostingCategorys(json)) {
			if (jemsCostingCategoryService
					.updateJemsCostingCategory(jemsCostingCategory) == null) {
				return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
			}
		}
		return new ResponseEntity<String>(headers, HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
	public ResponseEntity<String> deleteFromJson(@PathVariable("id") Long id) {
		JemsCostingCategory jemsCostingCategory = jemsCostingCategoryService
				.findJemsCostingCategory(id);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		if (jemsCostingCategory == null) {
			return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}
		jemsCostingCategoryService
				.deleteJemsCostingCategory(jemsCostingCategory);
		return new ResponseEntity<String>(headers, HttpStatus.OK);
	}
}
