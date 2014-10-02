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

import com.dubaidrums.jems.domain.JemsStaff;
import com.dubaidrums.jems.service.JemsEventService;
import com.dubaidrums.jems.service.JemsStaffService;
import com.dubaidrums.jems.service.JemsUserService;

@RequestMapping("/jemsstaffs")
@Controller
public class JemsStaffController {

	Logger log = LogManager.getLogger(JemsStaffController.class);

	@Autowired
	JemsStaffService jemsStaffService;

	@Autowired
	JemsEventService jemsEventService;

	@Autowired
	JemsUserService jemsUserService;

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
	public String create(@Valid JemsStaff jemsStaff,
			BindingResult bindingResult, Model uiModel,
			HttpServletRequest httpServletRequest, Principal principal) {
		if (bindingResult.hasErrors()) {
			populateEditForm(uiModel, jemsStaff);
			return "jemsstaffs/create";
		}
		uiModel.asMap().clear();
		jemsStaffService.saveJemsStaff(jemsStaff);
		// return "redirect:/jemsstaffs/" +
		// encodeUrlPathSegment(jemsStaff.getId().toString(),
		// httpServletRequest);
		log.info("user: " + principal.getName() + ", method: create, staffId: "
				+ jemsStaff.getId() + ", msg: new staff created");
		return "redirect:/jemsstaffs?msg=1";
	}

	@RequestMapping(params = "form", produces = "text/html")
	public String createForm(Model uiModel) {
		populateEditForm(uiModel, new JemsStaff());
		return "jemsstaffs/create";
	}

	// @RequestMapping(value = "/{id}", produces = "text/html")
	// public String show(@PathVariable("id") Long id, Model uiModel) {
	// uiModel.addAttribute("jemsstaff", jemsStaffService.findJemsStaff(id));
	// uiModel.addAttribute("itemId", id);
	// return "jemsstaffs/show";
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
			uiModel.addAttribute("jemsstaffs",
					jemsStaffService.findJemsStaffEntries(firstResult, sizeNo));
			float nrOfPages = (float) jemsStaffService.countAllJemsStaffs()
					/ sizeNo;
			uiModel.addAttribute(
					"maxPages",
					(int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1
							: nrOfPages));
		} else {
			if (msg != null && msg == 1) {
				uiModel.addAttribute("msg", "Staff sucessfully saved!");
			}
			uiModel.addAttribute("jemsstaffs",
					jemsStaffService.findAllJemsStaffs());
		}
		log.info("user: " + principal.getName() + ", method: list, page: "
				+ page + ", msg: staffs listed");
		return "jemsstaffs/list";
	}

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
	public String update(@Valid JemsStaff jemsStaff,
			BindingResult bindingResult, Model uiModel,
			HttpServletRequest httpServletRequest, Principal principal) {
		if (bindingResult.hasErrors()) {
			populateEditForm(uiModel, jemsStaff);
			return "jemsstaffs/update";
		}
		uiModel.asMap().clear();
		jemsStaffService.updateJemsStaff(jemsStaff);
		// return "redirect:/jemsstaffs/" +
		// encodeUrlPathSegment(jemsStaff.getId().toString(),
		// httpServletRequest);
		log.info("user: " + principal.getName() + ", method: update, staffId: "
				+ jemsStaff.getId() + ", msg: staff updated");
		return "redirect:/jemsstaffs?msg=1";
	}

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
	public String updateForm(@PathVariable("id") Long id, Model uiModel) {
		populateEditForm(uiModel, jemsStaffService.findJemsStaff(id));
		return "jemsstaffs/update";
	}

	// @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces
	// = "text/html")
	// public String delete(@PathVariable("id") Long id, @RequestParam(value =
	// "page", required = false) Integer page, @RequestParam(value = "size",
	// required = false) Integer size, Model uiModel) {
	// JemsStaff jemsStaff = jemsStaffService.findJemsStaff(id);
	// jemsStaffService.deleteJemsStaff(jemsStaff);
	// uiModel.asMap().clear();
	// uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
	// uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
	// return "redirect:/jemsstaffs";
	// }

	void populateEditForm(Model uiModel, JemsStaff jemsStaff) {
		uiModel.addAttribute("jemsStaff", jemsStaff);
	}

	// @PreAuthorize("hasRole('ROLE_ADMIN')")
	// @RequestMapping(method = RequestMethod.GET, value = "/fixstaff", produces
	// = "text/html")
	// public ResponseEntity<String> fixstaff(Principal principal) {
	// try{
	// log.info("Fixing Staff...");
	//
	// Map<String,JemsStaff> userStaffMap = new HashMap<String,JemsStaff>();
	//
	// List<JemsUser> users = jemsUserService.findAllJemsUsers();
	// for (JemsUser u : users) {
	// JemsStaff s = new JemsStaff();
	// s.setName(u.getFullName());
	// s.setMobile(u.getMobile());
	// s.setEmail(u.getEmail());
	// s.setActive(u.getEnabled());
	//
	// jemsStaffService.saveJemsStaff(s);
	//
	// userStaffMap.put(u.getUserName(), s);
	// log.info("New staff created: "+s.getName());
	// }
	//
	// List<JemsEvent> events = jemsEventService.findAllJemsEvents();
	// for (JemsEvent e : events) {
	// log.info("Processing event: "+e.getId()+" staffAssignedSize: "+e.getStaffAssigned().size());
	//
	//
	// if(e.getStaffAssigned().size()!=0){
	// for (JemsUser u : e.getStaffAssigned()) {
	// JemsStaff s = userStaffMap.get(u.getUserName());
	// log.info("Replacing user: "+u.getUserName()+" with staff: "+s.getName());
	// if(s!=null){
	// e.getStaffAssigned2().add(s);
	// s.getEvents().add(e);
	// jemsStaffService.updateJemsStaff(s);
	//
	// log.info("updated. staff: "+s.getName()+" event:"+e.getId());
	// }
	// }
	// jemsEventService.updateJemsEvent(e);
	// }
	// }
	//
	//
	// log.info("Staff fixed successfully...");
	// }catch(Exception ex){
	// log.error("Error FixStaff!",ex);
	// }
	//
	// HttpHeaders headers = new HttpHeaders();
	// headers.add("Content-Type", "application/json; charset=utf-8");
	// return new ResponseEntity<String>("Fix Staff", headers, HttpStatus.OK);
	// }

}
