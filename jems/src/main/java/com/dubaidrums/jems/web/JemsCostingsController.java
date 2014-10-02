package com.dubaidrums.jems.web;

import java.security.Principal;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dubaidrums.jems.domain.JemsCostingCategory;
import com.dubaidrums.jems.domain.JemsCostingSubCategory;
import com.dubaidrums.jems.domain.JemsOrganization;
import com.dubaidrums.jems.service.JemsCostingCategoryService;
import com.dubaidrums.jems.service.JemsCostingSubCategoryService;
import com.dubaidrums.jems.service.JemsOrganizationService;
import com.dubaidrums.jems.util.transformer.CostingCategoryTransformer;
import com.dubaidrums.jems.util.transformer.OrgnaizationTransformer;

import flexjson.JSONSerializer;

@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping("/jemscostings")
@Controller
public class JemsCostingsController {

	Logger log = LogManager.getLogger(JemsCostingsController.class);

	@Autowired
	JemsCostingCategoryService jemsCostingCategoryService;

	@Autowired
	JemsCostingSubCategoryService jemsCostingSubCategoryService;

	@Autowired
	JemsOrganizationService jemsOrganizationService;

	@RequestMapping(produces = "text/html")
	public String list(Model uiModel, Principal principal) {
		// List<JemsCostingCategory> costingCategories =
		// jemsCostingCategoryService.findAllJemsCostingCategorys();
		// List<JemsCostingSubCategory> costingSubCategories =
		// jemsCostingSubCategoryService.findAllJemsCostingSubCategorys();
		//
		// List<Map<String, String>> categories = new ArrayList<Map<String,
		// String>>();
		// for (JemsCostingCategory c : costingCategories) {
		// Map<String, String> category = new HashMap<String, String>();
		// category.put("id", c.getId().toString());
		// category.put("name", c.getName());
		// category.put("orgId", c.getOrganization().getId().toString());
		//
		// categories.add(category);
		// }
		//
		// List<Map<String, String>> subCategories = new ArrayList<Map<String,
		// String>>();
		// for (JemsCostingSubCategory c : costingSubCategories) {
		// Map<String, String> category = new HashMap<String, String>();
		// category.put("id", c.getId().toString());
		// category.put("name", c.getName());
		// category.put("rate", c.getRate().toString());
		// category.put("catId", c.getCategory().getId().toString());
		//
		// subCategories.add(category);
		// }
		//
		// uiModel.addAttribute("costingCategoriesJson", new
		// JSONSerializer().exclude("*.class").serialize(categories));
		// uiModel.addAttribute("costingSubCategoriesJson", new
		// JSONSerializer().exclude("*.class").serialize(subCategories));

		uiModel.addAttribute("jemsorganizations",
				jemsOrganizationService.findAllJemsOrganizations());
		return "jemscostings/list";
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/categorys/{oid}", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> readCategories(@PathVariable("oid") Long oid) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");

		List<JemsCostingCategory> costingCategories = jemsCostingCategoryService
				.findAllJemsCostingCategorysByOrganizationId(oid);

		return new ResponseEntity<String>(new JSONSerializer()
				.exclude("*.class")
				.exclude("version")
				.transform(new OrgnaizationTransformer(),
						JemsOrganization.class).serialize(costingCategories),
				headers, HttpStatus.OK);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/categorys/update", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> updateCategory(
			@RequestParam(value = "id", required = true) Long id,
			@RequestParam(value = "name", required = true) String name,
			Principal principal) {
		JemsCostingCategory c = jemsCostingCategoryService
				.findJemsCostingCategory(id);
		c.setName(name);
		c = jemsCostingCategoryService.updateJemsCostingCategory(c);

		log.info("user: " + principal.getName()
				+ ", method: updateCategory, categoryId: " + id
				+ ", msg: category updated");

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		return new ResponseEntity<String>(new JSONSerializer()
				.exclude("*.class")
				.exclude("version")
				.transform(new OrgnaizationTransformer(),
						JemsOrganization.class).serialize(c), headers,
				HttpStatus.OK);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/categorys/create", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> createCategory(
			@RequestParam(value = "organization", required = true) Long organization,
			@RequestParam(value = "name", required = true) String name,
			Principal principal) {
		if (name == null || name.length() < 3) {
			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Type", "application/json; charset=utf-8");
			return new ResponseEntity<String>(headers, HttpStatus.OK);
		}

		JemsCostingCategory c = new JemsCostingCategory();
		c.setName(name);
		c.setOrganization(jemsOrganizationService
				.findJemsOrganization(organization));

		jemsCostingCategoryService.saveJemsCostingCategory(c);

		log.info("user: " + principal.getName()
				+ ", method: createCategory, categoryId: " + c.getId()
				+ ", msg: new category created");

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		return new ResponseEntity<String>(new JSONSerializer()
				.exclude("*.class")
				.exclude("version")
				.transform(new OrgnaizationTransformer(),
						JemsOrganization.class).serialize(c), headers,
				HttpStatus.OK);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/categorys/destroy", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> destroyCategory(
			@RequestParam(value = "id", required = true) Long id,
			Principal principal) {
		JemsCostingCategory c = jemsCostingCategoryService
				.findJemsCostingCategory(id);
		jemsCostingCategoryService.deleteJemsCostingCategory(c);

		log.info("user: " + principal.getName()
				+ ", method: destroyCategory, categoryId: " + id
				+ ", msg: category destroyed");

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		return new ResponseEntity<String>(headers, HttpStatus.OK);
	}

	// ///////////////////

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/subcategorys/{cid}", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> readSubCategories(
			@PathVariable("cid") Long cid) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");

		List<JemsCostingSubCategory> costingSubCategories = jemsCostingSubCategoryService
				.findAllJemsCostingSubCategorysByCategoryId(cid);

		return new ResponseEntity<String>(new JSONSerializer()
				.exclude("*.class")
				.exclude("version")
				.transform(new CostingCategoryTransformer(),
						JemsCostingCategory.class)
				.serialize(costingSubCategories), headers, HttpStatus.OK);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/subcategorys/create", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> createSubCategory(
			@RequestParam(value = "category", required = true) Long category,
			@RequestParam(value = "name", required = true) String name,
			@RequestParam(value = "rate", required = true) Double rate,
			Principal principal) {
		JemsCostingSubCategory c = new JemsCostingSubCategory();
		c.setName(name);
		c.setRate(rate);
		c.setCategory(jemsCostingCategoryService
				.findJemsCostingCategory(category));

		jemsCostingSubCategoryService.saveJemsCostingSubCategory(c);

		log.info("user: " + principal.getName()
				+ ", method: createSubCategory, subCategoryId: " + c.getId()
				+ ", msg: new subcategory created");

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		return new ResponseEntity<String>(new JSONSerializer()
				.exclude("*.class")
				.exclude("version")
				.transform(new CostingCategoryTransformer(),
						JemsCostingCategory.class).serialize(c), headers,
				HttpStatus.OK);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/subcategorys/update", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> updateSubCategory(
			@RequestParam(value = "id", required = true) Long id,
			@RequestParam(value = "name", required = true) String name,
			@RequestParam(value = "rate", required = true) Double rate,
			Principal principal) {
		JemsCostingSubCategory c = jemsCostingSubCategoryService
				.findJemsCostingSubCategory(id);
		c.setName(name);
		c.setRate(rate);
		c = jemsCostingSubCategoryService.updateJemsCostingSubCategory(c);

		log.info("user: " + principal.getName()
				+ ", method: updateSubCategory, subCategoryId: " + id
				+ ", msg: subcategory updated");

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		return new ResponseEntity<String>(new JSONSerializer()
				.exclude("*.class")
				.exclude("version")
				.transform(new CostingCategoryTransformer(),
						JemsCostingCategory.class).serialize(c), headers,
				HttpStatus.OK);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/subcategorys/destroy", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> destroySubCategory(
			@RequestParam(value = "id", required = true) Long id,
			Principal principal) {
		JemsCostingSubCategory c = jemsCostingSubCategoryService
				.findJemsCostingSubCategory(id);
		jemsCostingSubCategoryService.deleteJemsCostingSubCategory(c);

		log.info("user: " + principal.getName()
				+ ", method: destroySubCategory, subCategoryId: " + id
				+ ", msg: subcategory destroyed");

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		return new ResponseEntity<String>(headers, HttpStatus.OK);
	}

}
