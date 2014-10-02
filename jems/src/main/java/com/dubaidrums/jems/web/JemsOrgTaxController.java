package com.dubaidrums.jems.web;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.dubaidrums.jems.domain.JemsOrgTax;
import com.dubaidrums.jems.service.JemsOrgTaxService;
import com.dubaidrums.jems.service.JemsOrganizationService;

@RequestMapping("/jemsorgtaxes")
@Controller
public class JemsOrgTaxController {

	Logger log = LogManager.getLogger(JemsOrgTaxController.class);

	@Autowired
	JemsOrgTaxService jemsOrgTaxService;

	@Autowired
	JemsOrganizationService jemsOrganizationService;

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
	public String create(@Valid JemsOrgTax jemsOrgTax,
			BindingResult bindingResult, Model uiModel,
			HttpServletRequest httpServletRequest, Principal principal) {
		if (bindingResult.hasErrors()) {
			populateEditForm(uiModel, jemsOrgTax);
			return "jemsorgtaxes/create";
		}
		uiModel.asMap().clear();
		jemsOrgTaxService.saveJemsOrgTax(jemsOrgTax);
		// return "redirect:/jemsorgtaxes/" +
		// encodeUrlPathSegment(jemsOrgTax.getId().toString(),
		// httpServletRequest);
		log.info("user: " + principal.getName() + ", method: create, taxId: "
				+ jemsOrgTax.getId() + ", msg: new tax created");
		return "redirect:/jemsorgtaxes?msg=1";
	}

	@RequestMapping(params = "form", produces = "text/html")
	public String createForm(Model uiModel) {
		populateEditForm(uiModel, new JemsOrgTax());
		return "jemsorgtaxes/create";
	}

	// @RequestMapping(value = "/{id}", produces = "text/html")
	// public String show(@PathVariable("id") Long id, Model uiModel) {
	// uiModel.addAttribute("jemstax", jemsOrgTaxService.findOne(id));
	// uiModel.addAttribute("itemId", id);
	// return "jemsorgtaxes/show";
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
			uiModel.addAttribute("jemsorgtaxes", jemsOrgTaxService
					.findJemsOrgTaxEntries(firstResult, sizeNo));
			float nrOfPages = (float) jemsOrgTaxService.countAllJemsOrgTaxes()
					/ sizeNo;
			uiModel.addAttribute(
					"maxPages",
					(int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1
							: nrOfPages));
		} else {
			if (msg != null && msg == 1) {
				uiModel.addAttribute("msg", "Tax sucessfully saved!");
			}
			uiModel.addAttribute("jemsorgtaxes",
					jemsOrgTaxService.findAllJemsOrgTaxes());
		}
		log.info("user: " + principal.getName() + ", method: list, page: "
				+ page + ", msg: taxes listed");
		return "jemsorgtaxes/list";
	}

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
	public String update(@Valid JemsOrgTax jemsOrgTax,
			BindingResult bindingResult, Model uiModel,
			HttpServletRequest httpServletRequest, Principal principal) {
		if (bindingResult.hasErrors()) {
			populateEditForm(uiModel, jemsOrgTax);
			return "jemsorgtaxes/update";
		}
		uiModel.asMap().clear();
		jemsOrgTaxService.saveJemsOrgTax(jemsOrgTax);
		// return "redirect:/jemsorgtaxes/" +
		// encodeUrlPathSegment(jemsOrgTax.getId().toString(),
		// httpServletRequest);
		log.info("user: " + principal.getName() + ", method: update, taxId: "
				+ jemsOrgTax.getId() + ", msg: tax updated");
		return "redirect:/jemsorgtaxes?msg=1";
	}

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
	public String updateForm(@PathVariable("id") Long id, Model uiModel) {
		populateEditForm(uiModel, jemsOrgTaxService.findJemsOrgTax(id));
		return "jemsorgtaxes/update";
	}

	// @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces
	// = "text/html")
	// public String delete(@PathVariable("id") Long id, @RequestParam(value =
	// "page", required = false) Integer page, @RequestParam(value = "size",
	// required = false) Integer size, Model uiModel) {
	// JemsOrgTax jemsOrgTax = jemsOrgTaxService.findOne(id);
	// jemsOrgTaxService.delete(jemsOrgTax);
	// uiModel.asMap().clear();
	// uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
	// uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
	// return "redirect:/jemsorgtaxes";
	// }

	void populateEditForm(Model uiModel, JemsOrgTax jemsOrgTax) {
		uiModel.addAttribute("jemsorganizations",
				jemsOrganizationService.findAllJemsOrganizations());
		uiModel.addAttribute("jemsOrgTax", jemsOrgTax);
	}
}
