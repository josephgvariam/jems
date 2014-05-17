package com.dubaidrums.jems.web;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dubaidrums.jems.domain.JemsEvent;
import com.dubaidrums.jems.domain.JemsInvoice;
import com.dubaidrums.jems.domain.JemsQuotation;
import com.dubaidrums.jems.domain.JemsStaff;
import com.dubaidrums.jems.service.JemsInvoiceService;
import com.dubaidrums.jems.service.JemsQuotationService;

@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_ACCOUNTS')")
@RequestMapping("/jemscsvdumps")
@Controller
public class JemsCSVDumpsController {

	Logger log = LogManager.getLogger(JemsCSVDumpsController.class);

	@Autowired
	JemsQuotationService jemsQuotationService;
	
	@Autowired
	JemsInvoiceService jemsInvoiceService;
	
	@RequestMapping(produces = "text/html")
    public String showForm() {
        return "jemscsvdumps/show";
    }

	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
	@RequestMapping(value = "/quotations", method = RequestMethod.POST, produces = "text/csv")
	@ResponseBody
	public ResponseEntity<String> getQuotationCSVDump(@RequestParam(value = "searchdate", required = true) boolean searchDate, @RequestParam(value = "startDate", required = true) String startDate, @RequestParam(value = "endDate", required = true) String endDate, Principal principal,HttpServletRequest r) {
		try{
			
			
			Date sd = sdf.parse(startDate);
			Calendar cal = Calendar.getInstance();
			cal.setTime(sd);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			sd = cal.getTime();
			
			Date ed = sdf.parse(endDate);			
			cal.setTime(ed);
			cal.set(Calendar.HOUR_OF_DAY, 23);
			cal.set(Calendar.MINUTE, 59);
			cal.set(Calendar.SECOND, 59);
			ed = cal.getTime();
		
			
			List<LineItem> result = new ArrayList<LineItem>();			
			
			List<JemsQuotation> quotations = jemsQuotationService.findAllJemsQuotations();
			List<JemsInvoice> invoices = jemsInvoiceService.findAllJemsInvoices();
			
			for (Iterator iterator = invoices.iterator(); iterator.hasNext();) {
				JemsInvoice i = (JemsInvoice) iterator.next();				
				if(i.getJemsQuotation()==null && i.isActive()){
					JemsEvent e = i.getJemsEvent();
					if(e!=null && e.getActive()){
						Date checkDate;
						if(searchDate){
							checkDate = e.getStartDateTime();
						}else{
							checkDate = i.getIDate();
						}
						if(checkDate!=null && checkDate.before(ed) && checkDate.after(sd)){
							String lis = getLineItemString(e, null, i,r);
							if(!isEmptyString(lis)){
								result.add(new LineItem(checkDate, lis));
							}
						}
					}
				}
			}
			
			for (Iterator iterator = quotations.iterator(); iterator.hasNext();) {
				JemsQuotation q = (JemsQuotation) iterator.next();
				JemsEvent e = q.getJemsEvent();
				JemsInvoice i = q.getJemsInvoice();
				if(q!=null && q.isActive()){
					if(e!=null && e.getActive()){
						Date checkDate;
						if(searchDate){
							checkDate = e.getStartDateTime();
						}else{
							checkDate = q.getQDate();
						}
						if(checkDate!=null && checkDate.before(ed) && checkDate.after(sd)){
							String lis = getLineItemString(e, q, i,r);
							if(!isEmptyString(lis)){
								result.add(new LineItem(checkDate, lis));
							}
						}
					}
				}
			}
			
			//StringBuilder sb = new StringBuilder("Quotation Date,Event Date,Quote No,Organisation,Event Type,Event Description,No of Facilitators,No of People,Company Name,Contact Person,Contact Number,Email ID,Status,Invoice No,Quoted Amt,Invoiced Amt,Paid Status\n");
			StringBuilder sb = new StringBuilder(getHeaderString(r));
			Collections.sort(result);
			for (LineItem l : result) {
				sb.append(l.getLine());
			}
						
			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Type", "text/csv; charset=utf-8");
			log.info("user: "+principal.getName()+", method: getQuotationCSVDump, msg: csv dumped!");
			return new ResponseEntity<String>(sb.toString(), headers, HttpStatus.OK);
		}catch(Exception ex){
			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Type", "text/html; charset=utf-8");
			log.error("Could not dump csv!", ex);
			return new ResponseEntity<String>(ex.getMessage(), headers, HttpStatus.OK);
		}
	}
	
	private boolean isEmptyString(String s) {
		s = s.trim();
		boolean res = StringUtils.countOccurrencesOf(s, ",") == s.length();
		//log.info("Checking: "+s+", result: "+res);
		return res;
	}

	private boolean checkOption(String s, HttpServletRequest request){
		return request.getParameter(s)!=null;
	}
	
	private String getHeaderString(HttpServletRequest r){
		StringBuilder s = new StringBuilder();
		if(checkOption("edate",r)){
			s.append("Event Date,");
		}
		if(checkOption("enumber",r)){
			s.append("Event Number,");
		}
		if(checkOption("org",r)){
			s.append("Organization,");
		}
		if(checkOption("type",r)){
			s.append("Event Type,");
		}
		if(checkOption("status",r)){
			s.append("Event Status,");
		}
		if(checkOption("title",r)){
			s.append("Event Title,");
		}
		if(checkOption("description",r)){
			s.append("Event Description,");
		}
		if(checkOption("numfacilitators",r)){
			s.append("Number of Facilitators,");			
		}
		if(checkOption("numPeople",r)){
			s.append("Number of People,");
		}
		if(checkOption("companyname",r)){
			s.append("Company Name,");						
		}
		if(checkOption("contactperson",r)){
			s.append("Company Contact Person,");
		}
		if(checkOption("contactnumber",r)){
			s.append("Company Contact Number,");
		}
		if(checkOption("email",r)){
			s.append("Company Email,");
		}
		
		if(checkOption("currency",r)){
			s.append("Currency,");
		}
		if(checkOption("location",r)){
			s.append("Location,");
		}
		if(checkOption("gps",r)){
			s.append("GPS Coordinates,");
		}
		if(checkOption("country",r)){
			s.append("Country,");
		}
		if(checkOption("Region",r)){
			s.append("Region,");
		}
		if(checkOption("numDrums",r)){
			s.append("Number of Drums,");
		}
		if(checkOption("numSessions",r)){
			s.append("Number of Sessions,");
		}
		if(checkOption("chairs",r)){
			s.append("Chairs Required,");
		}
		if(checkOption("staff",r)){
			s.append("Staff Assigned,");
		}
		if(checkOption("receipt",r)){
			s.append("Receipt Voucher,");
		}
		
		if(checkOption("qdate",r)){				
			s.append("Quotation Date,");
		}
		if(checkOption("qnum",r)){
			s.append("Quotation Number,");
		}
		if(checkOption("idate",r)){
			s.append("Invoice Date,");
		}
		if(checkOption("inum",r)){
			s.append("Invoice Number,");
		}
		if(checkOption("qamount",r)){
			s.append("Quoted Amount,");
		}
		if(checkOption("iamount",r)){
			s.append("Invoiced Amount,");
		}
		if(checkOption("paidstatus",r)){
			s.append("Paid Status,");
		}
		if(checkOption("paidamount",r)){
			s.append("Paid Amount,");				
		}
		if(checkOption("paiddate",r)){
			s.append("Paid Date,");			
		}
		if(checkOption("elink",r)){
			s.append("Event Link,");
		}
		if(checkOption("qlink",r)){
			s.append("Quotation Link,");
		}
		if(checkOption("ilink",r)){
			s.append("Invoice Link");
		}
		
		
		s.append("\n");
		
		return s.toString();
		
	}
	
	private String getLineItemString(JemsEvent e, JemsQuotation q, JemsInvoice i,HttpServletRequest r){
			StringBuilder s = new StringBuilder();
			
			if(checkOption("edate",r)){
				s.append(sdf.format(e.getStartDateTime())).append(",");
			}
			if(checkOption("enumber",r)){
				s.append(e.getId()).append(",");
			}
			if(checkOption("org",r)){
				s.append("\""+e.getOrganization().getName()+"\"").append(",");
			}
			if(checkOption("type",r)){
				s.append(e.getType().name()).append(",");
			}
			if(checkOption("status",r)){
				s.append(e.getStatus().name()).append(",");
			}
			if(checkOption("title",r)){
				s.append("\""+e.getTitle()+"\"").append(",");
			}
			if(checkOption("description",r)){
				s.append("\""+e.getDescription()+"\"").append(",");
			}
			if(checkOption("numfacilitators",r)){
				if(e.getNumberDrummers()==null){
					s.append(",");
				}else{
					s.append(e.getNumberDrummers()).append(",");
				}				
			}
			if(checkOption("numPeople",r)){
				if(e.getNumberPeople()==null){
					s.append(",");
				}else{
					s.append(e.getNumberPeople()).append(",");
				}
			}
			if(checkOption("companyname",r)){
				if(e.getUseInDocs()==0){
					s.append("\""+e.getClientCompany()+"\"").append(",");
				}else{
					s.append("\""+e.getHiringAgentCompany()+"\"").append(",");
				}				
				
			}
			if(checkOption("contactperson",r)){
				if(e.getUseInDocs()==0){
					s.append("\""+e.getClientContactPerson()+"\"").append(",");
				}else{
					s.append("\""+e.getHiringAgentContactPerson()+"\"").append(",");
				}
			}
			if(checkOption("contactnumber",r)){
				if(e.getUseInDocs()==0){
					s.append("\""+e.getClientPhone()+"\"").append(",");
				}else{
					s.append("\""+e.getHiringAgentPhone()+"\"").append(",");
				}
			}
			if(checkOption("email",r)){
				if(e.getUseInDocs()==0){
					s.append("\""+e.getClientEmail()+"\"").append(",");
				}else{
					s.append("\""+e.getHiringAgentEmail()+"\"").append(",");
				}
			}
			
			
			
			if(checkOption("currency",r)){
				s.append(e.getCurrency().getIsoCode()).append(",");
			}
			if(checkOption("location",r)){
				s.append("\""+e.getLocation().replaceAll("[\\n\\r]+", " ")+"\"").append(",");
			}
			if(checkOption("gps",r)){
				s.append("\""+e.getLocationLatLong()+"\"").append(",");
			}
			if(checkOption("country",r)){
				s.append(e.getCountry().getName()).append(",");
			}
			if(checkOption("Region",r)){
				s.append(e.getRegion().getName()).append(",");
			}
			if(checkOption("numDrums",r)){
				if(e.getNumberDrums()==null){
					s.append(",");
				}else{
					s.append(e.getNumberDrums()).append(",");
				}
			}
			if(checkOption("numSessions",r)){
				if(e.getNumberSessions()==null){
					s.append(",");
				}else{
					s.append(e.getNumberSessions()).append(",");
				}
			}
			if(checkOption("chairs",r)){
				s.append(e.getChairsRequired().toString()).append(",");
			}
			if(checkOption("staff",r)){
				s.append("\""+getStaffList(e.getStaffAssigned2())+"\"").append(",");
			}
			if(checkOption("receipt",r)){
				s.append("\""+e.getReceiptVoucherNumber()+"\"").append(",");
			}			
			
			
			
			if(checkOption("qdate",r)){				
				if(q!=null && q.isActive() && q.getQDate()!=null){
					s.append(sdf.format(q.getQDate())).append(",");
				}else{
					s.append(",");
				}
			}
			if(checkOption("qnum",r)){
				if(q!=null && q.isActive()){
					s.append(q.getQNumber()).append(",");
				}else{
					s.append(",");
				}
			}
			if(checkOption("idate",r)){
				if(i!=null && i.isActive() && i.getIDate()!=null ){
					s.append(sdf.format(i.getIDate())).append(",");
				}else{
					s.append(",");
				}
			}
			if(checkOption("inum",r)){
				if(i!=null && i.isActive()){
					s.append(i.getINumber()).append(",");
				}else{
					s.append(",");
				}
			}
			if(checkOption("qamount",r)){
				if(q!=null && q.isActive()){
					s.append(q.getTotalAmount()).append(",");
				}else{
					s.append(",");
				}
			}
			if(checkOption("iamount",r)){
				if(i!=null && i.isActive()){
					s.append(i.getTotalAmount()).append(",");
				}else{
					s.append(",");
				}
			}
			if(checkOption("paidstatus",r)){
				s.append(e.getPaid().toString()).append(",");
			}
			if(checkOption("paidamount",r)){
				if(e.getPaidAmount()==null){
					s.append(",");
				}else{
					s.append(e.getPaidAmount().toString()).append(",");
				}				
			}
			if(checkOption("paiddate",r)){
				if(e.getPaidDate()==null){
					s.append(",");
				}else{
					s.append(sdf.format(e.getPaidDate())).append(",");
				}				
			}
			if(checkOption("elink",r)){
				s.append("http://jems.dubaidrums.com/jemsevents/"+e.getId()).append(",");
			}
			if(checkOption("qlink",r)){
				if(q!=null && q.isActive()){
					s.append("http://jems.dubaidrums.com/jemsquotations/"+q.getId()).append(",");
				}else{
					s.append(",");
				}
			}
			if(checkOption("ilink",r)){
				if(i!=null && i.isActive()){
					s.append("http://jems.dubaidrums.com/jemsinvoices/"+i.getId()).append(",");
				}else{
					s.append(",");
				}
			}
			
			
			s.append("\n");
			
			return s.toString();
		}		

	
	private String getStaffList(Set<JemsStaff> staffAssigned2) {
		StringBuilder sb = new StringBuilder();
		for (JemsStaff s : staffAssigned2) {
			sb.append(s.getName()).append(", ");
		}
		String res = sb.toString();
		if(res.endsWith(", ")){
			res = res.substring(0, res.length()-2);
		}
		return res;
	}


	private class LineItem implements Comparable<LineItem>{
		Date d;
		String line;
		
		public LineItem(Date d, String line){
			this.d = d;
			this.line = line;
		}
		
		public Date getD() {
			return d;
		}
		public void setD(Date d) {
			this.d = d;
		}
		public String getLine() {
			return line;
		}
		public void setLine(String line) {
			this.line = line;
		}

		public int compareTo(LineItem o) {
			return this.d.compareTo(o.getD());
		}
	}

}
