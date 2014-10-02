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

import com.dubaidrums.jems.domain.JemsCostingSubCategory;
import com.dubaidrums.jems.service.JemsCostingCategoryService;
import com.dubaidrums.jems.service.JemsCostingSubCategoryService;

@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping("/jemscostingsubcategorys")
@Controller
public class JemsCostingSubCategoryController {

	@Autowired
	JemsCostingSubCategoryService jemsCostingSubCategoryService;

	@Autowired
	JemsCostingCategoryService jemsCostingCategoryService;

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
	public String create(@Valid JemsCostingSubCategory jemsCostingSubCategory,
			BindingResult bindingResult, Model uiModel,
			HttpServletRequest httpServletRequest) {
		if (bindingResult.hasErrors()) {
			populateEditForm(uiModel, jemsCostingSubCategory);
			return "jemscostingsubcategorys/create";
		}
		uiModel.asMap().clear();
		jemsCostingSubCategoryService
				.saveJemsCostingSubCategory(jemsCostingSubCategory);
		return "redirect:/jemscostingsubcategorys/"
				+ encodeUrlPathSegment(jemsCostingSubCategory.getId()
						.toString(), httpServletRequest);
	}

	@RequestMapping(params = "form", produces = "text/html")
	public String createForm(Model uiModel) {
		populateEditForm(uiModel, new JemsCostingSubCategory());
		return "jemscostingsubcategorys/create";
	}

	@RequestMapping(value = "/{id}", produces = "text/html")
	public String show(@PathVariable("id") Long id, Model uiModel) {
		uiModel.addAttribute("jemscostingsubcategory",
				jemsCostingSubCategoryService.findJemsCostingSubCategory(id));
		uiModel.addAttribute("itemId", id);
		return "jemscostingsubcategorys/show";
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
			uiModel.addAttribute("jemscostingsubcategorys",
					jemsCostingSubCategoryService
							.findJemsCostingSubCategoryEntries(firstResult,
									sizeNo));
			float nrOfPages = (float) jemsCostingSubCategoryService
					.countAllJemsCostingSubCategorys() / sizeNo;
			uiModel.addAttribute(
					"maxPages",
					(int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1
							: nrOfPages));
		} else {
			uiModel.addAttribute("jemscostingsubcategorys",
					jemsCostingSubCategoryService
							.findAllJemsCostingSubCategorys());
		}
		return "jemscostingsubcategorys/list";
	}

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
	public String update(@Valid JemsCostingSubCategory jemsCostingSubCategory,
			BindingResult bindingResult, Model uiModel,
			HttpServletRequest httpServletRequest) {
		if (bindingResult.hasErrors()) {
			populateEditForm(uiModel, jemsCostingSubCategory);
			return "jemscostingsubcategorys/update";
		}
		uiModel.asMap().clear();
		jemsCostingSubCategoryService
				.updateJemsCostingSubCategory(jemsCostingSubCategory);
		return "redirect:/jemscostingsubcategorys/"
				+ encodeUrlPathSegment(jemsCostingSubCategory.getId()
						.toString(), httpServletRequest);
	}

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
	public String updateForm(@PathVariable("id") Long id, Model uiModel) {
		populateEditForm(uiModel,
				jemsCostingSubCategoryService.findJemsCostingSubCategory(id));
		return "jemscostingsubcategorys/update";
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
	public String delete(@PathVariable("id") Long id,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size,
			Model uiModel) {
		JemsCostingSubCategory jemsCostingSubCategory = jemsCostingSubCategoryService
				.findJemsCostingSubCategory(id);
		jemsCostingSubCategoryService
				.deleteJemsCostingSubCategory(jemsCostingSubCategory);
		uiModel.asMap().clear();
		uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
		uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
		return "redirect:/jemscostingsubcategorys";
	}

	void populateEditForm(Model uiModel,
			JemsCostingSubCategory jemsCostingSubCategory) {
		uiModel.addAttribute("jemsCostingSubCategory", jemsCostingSubCategory);
		uiModel.addAttribute("jemscostingcategorys",
				jemsCostingCategoryService.findAllJemsCostingCategorys());
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
		JemsCostingSubCategory jemsCostingSubCategory = jemsCostingSubCategoryService
				.findJemsCostingSubCategory(id);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		if (jemsCostingSubCategory == null) {
			return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<String>(jemsCostingSubCategory.toJson(),
				headers, HttpStatus.OK);
	}

	@RequestMapping(headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> listJson() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		List<JemsCostingSubCategory> result = jemsCostingSubCategoryService
				.findAllJemsCostingSubCategorys();
		return new ResponseEntity<String>(
				JemsCostingSubCategory.toJsonArray(result), headers,
				HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<String> createFromJson(@RequestBody String json) {
		JemsCostingSubCategory jemsCostingSubCategory = JemsCostingSubCategory
				.fromJsonToJemsCostingSubCategory(json);
		jemsCostingSubCategoryService
				.saveJemsCostingSubCategory(jemsCostingSubCategory);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<String> createFromJsonArray(@RequestBody String json) {
		for (JemsCostingSubCategory jemsCostingSubCategory : JemsCostingSubCategory
				.fromJsonArrayToJemsCostingSubCategorys(json)) {
			jemsCostingSubCategoryService
					.saveJemsCostingSubCategory(jemsCostingSubCategory);
		}
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}

	@RequestMapping(method = RequestMethod.PUT, headers = "Accept=application/json")
	public ResponseEntity<String> updateFromJson(@RequestBody String json) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		JemsCostingSubCategory jemsCostingSubCategory = JemsCostingSubCategory
				.fromJsonToJemsCostingSubCategory(json);
		if (jemsCostingSubCategoryService
				.updateJemsCostingSubCategory(jemsCostingSubCategory) == null) {
			return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<String>(headers, HttpStatus.OK);
	}

	@RequestMapping(value = "/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
	public ResponseEntity<String> updateFromJsonArray(@RequestBody String json) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		for (JemsCostingSubCategory jemsCostingSubCategory : JemsCostingSubCategory
				.fromJsonArrayToJemsCostingSubCategorys(json)) {
			if (jemsCostingSubCategoryService
					.updateJemsCostingSubCategory(jemsCostingSubCategory) == null) {
				return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
			}
		}
		return new ResponseEntity<String>(headers, HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
	public ResponseEntity<String> deleteFromJson(@PathVariable("id") Long id) {
		JemsCostingSubCategory jemsCostingSubCategory = jemsCostingSubCategoryService
				.findJemsCostingSubCategory(id);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		if (jemsCostingSubCategory == null) {
			return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}
		jemsCostingSubCategoryService
				.deleteJemsCostingSubCategory(jemsCostingSubCategory);
		return new ResponseEntity<String>(headers, HttpStatus.OK);
	}
}
