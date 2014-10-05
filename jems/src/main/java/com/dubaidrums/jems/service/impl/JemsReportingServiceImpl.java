package com.dubaidrums.jems.service.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dubaidrums.jems.domain.JemsCostingItem;
import com.dubaidrums.jems.domain.JemsEvent;
import com.dubaidrums.jems.domain.JemsInvoice;
import com.dubaidrums.jems.domain.JemsOrganization;
import com.dubaidrums.jems.domain.JemsQuotation;
import com.dubaidrums.jems.domain.JemsStaff;
import com.dubaidrums.jems.domain.JemsTax;
import com.dubaidrums.jems.service.JemsCostingItemService;
import com.dubaidrums.jems.service.JemsReportingService;

@Service
@Transactional
public class JemsReportingServiceImpl implements JemsReportingService {

	Logger log = LogManager.getLogger(JemsReportingServiceImpl.class);

	@Autowired
	private ServletContext servletContext;

	@Autowired
	JemsCostingItemService jemsCostingItemService;

	private static SimpleDateFormat dateFormat = new SimpleDateFormat(
			"dd/MM/yyyy");
	private static SimpleDateFormat dateTimeFormat = new SimpleDateFormat(
			"dd/MM/yyyy HH:mm");
	private static NumberFormat numFormat = new DecimalFormat("#,###,###.##");
	
	private static final BigDecimal hundred = new BigDecimal(100);

	public String generateQuotationPdf(JemsQuotation jq) throws Exception {
		JemsOrganization org = jq.getJemsEvent().getOrganization();

		Map parameters = new HashMap();
		parameters.put("qNumber", jq.getQNumber().toString());
		parameters.put("qDate", formatDate(jq.getQDate()));
		parameters.put("billTo", jq.getBillTo());
		parameters.put("eventDetails", jq.getEventDetails());
		parameters.put("paymentTerms", jq.getPaymentTerms());
		parameters.put("description1", jq.getDescription1());
		parameters.put("amount1", formatNumber(jq.getAmount1()));
		parameters.put("description2", jq.getDescription2());
		parameters.put("amount2", formatNumber(jq.getAmount2()));
		parameters.put("description3", jq.getDescription3());
		parameters.put("amount3", formatNumber(jq.getAmount3()));
		parameters.put("description4", jq.getDescription4());
		parameters.put("amount4", formatNumber(jq.getAmount4()));
		parameters.put("description5", jq.getDescription5());
		parameters.put("amount5", formatNumber(jq.getAmount5()));
		parameters.put("totalAmount", formatNumber(jq.getTotalAmount()));

		if (jq.getTaxes() != null && jq.getTaxes().size() != 0) {
			JemsTax[] taxes_ = jq.getTaxes().toArray(new JemsTax[0]);
			BigDecimal subtotal = jq.getSubTotalAmount();
			parameters.put("subtotalAmount", formatNumber(subtotal));
			parameters.put("tax1", getTax(taxes_, 0));
			parameters.put("tax1Amount", getTaxAmount(taxes_, 0, subtotal));
			parameters.put("tax2", getTax(taxes_, 1));
			parameters.put("tax2Amount", getTaxAmount(taxes_, 1, subtotal));
			parameters.put("tax3", getTax(taxes_, 2));
			parameters.put("tax3Amount", getTaxAmount(taxes_, 2, subtotal));
		} else {
			parameters.put("subtotalAmount", null);
			parameters.put("tax1", null);
			parameters.put("tax1Amount", null);
			parameters.put("tax2", null);
			parameters.put("tax2Amount", null);
			parameters.put("tax3", null);
			parameters.put("tax3Amount", null);
		}

		String fileName = "JemsQuotation_" + jq.getQNumber() + ".pdf";
		String reportPath = System.getProperty("java.io.tmpdir") + "/"
				+ fileName;

		JasperReport jr = getReport("WEB-INF/reports/" + org.getId()
				+ "/jemsQuotation.jasper");
		JasperPrint print = JasperFillManager.fillReport(jr, parameters,
				new JREmptyDataSource());
		JasperExportManager.exportReportToPdfFile(print, reportPath);

		log.info("method: generateQuotationPdf, msg: generated quotation pdf at "
				+ reportPath);

		return fileName;
	}

	@Cacheable("reports")
	private JasperReport getReport(String path) throws Exception {
		log.info("method: getReport, msg: loading JasperReport from " + path);
		return (JasperReport) JRLoader.loadObjectFromFile(servletContext
				.getRealPath("/") + path);
	}

	public String generateInvoicePdf(JemsInvoice jq) throws Exception {
		JemsOrganization org = jq.getJemsEvent().getOrganization();

		Map parameters = new HashMap();
		parameters.put("iNumber", jq.getINumber().toString());
		parameters.put("iDate", formatDate(jq.getIDate()));
		parameters.put("billTo", jq.getBillTo());
		parameters.put("eventDetails", jq.getEventDetails());
		parameters.put("clientRef", jq.getClientRef());
		parameters.put("paymentTerms", jq.getPaymentTerms());
		parameters.put("description1", jq.getDescription1());
		parameters.put("amount1", formatNumber(jq.getAmount1()));
		parameters.put("description2", jq.getDescription2());
		parameters.put("amount2", formatNumber(jq.getAmount2()));
		parameters.put("description3", jq.getDescription3());
		parameters.put("amount3", formatNumber(jq.getAmount3()));
		parameters.put("description4", jq.getDescription4());
		parameters.put("amount4", formatNumber(jq.getAmount4()));
		parameters.put("description5", jq.getDescription5());
		parameters.put("amount5", formatNumber(jq.getAmount5()));
		parameters.put("totalAmount", formatNumber(jq.getTotalAmount()));

		if (jq.getTaxes() != null && jq.getTaxes().size() != 0) {
			JemsTax[] taxes_ = jq.getTaxes().toArray(new JemsTax[0]);
			BigDecimal subtotal = jq.getSubTotalAmount();
			parameters.put("subtotalAmount", formatNumber(subtotal));
			parameters.put("tax1", getTax(taxes_, 0));
			parameters.put("tax1Amount", getTaxAmount(taxes_, 0, subtotal));
			parameters.put("tax2", getTax(taxes_, 1));
			parameters.put("tax2Amount", getTaxAmount(taxes_, 1, subtotal));
			parameters.put("tax3", getTax(taxes_, 2));
			parameters.put("tax3Amount", getTaxAmount(taxes_, 2, subtotal));
		} else {
			parameters.put("subtotalAmount", null);
			parameters.put("tax1", null);
			parameters.put("tax1Amount", null);
			parameters.put("tax2", null);
			parameters.put("tax2Amount", null);
			parameters.put("tax3", null);
			parameters.put("tax3Amount", null);
		}

		String fileName = "JemsInvoice_" + jq.getINumber() + ".pdf";
		String reportPath = System.getProperty("java.io.tmpdir") + "/"
				+ fileName;

		JasperReport jr = getReport("WEB-INF/reports/" + org.getId()
				+ "/jemsInvoice.jasper");
		JasperPrint print = JasperFillManager.fillReport(jr, parameters,
				new JREmptyDataSource());
		JasperExportManager.exportReportToPdfFile(print, reportPath);

		log.info("method: generateInvoicePdf, msg: generated invoice pdf at "
				+ reportPath);

		return fileName;
	}

	private static String getTaxAmount(JemsTax[] taxes_, int i, BigDecimal subtotal) {
		if (i < taxes_.length) {			
			return formatNumber((taxes_[i].getRatePercent().multiply(subtotal).divide(hundred,2,BigDecimal.ROUND_HALF_UP)));
		} else
			return null;
	}

	private static String getTax(JemsTax[] taxes_, int i) {
		if (i < taxes_.length) {
			return taxes_[i].getName() + " ("
					+ formatNumber(taxes_[i].getRatePercent()) + "%)";
		} else
			return null;
	}

	private static String formatNumber(Double amt) {
		if (amt == null)
			return "";
		return numFormat.format(amt);
	}

	private static String formatNumber(BigDecimal amt) {
		if (amt == null)
			return "";
		return numFormat.format(amt.doubleValue());
	}
	
	private static String formatDate(Date d) {
		if (d == null)
			d = new Date();
		return dateFormat.format(d);
	}

	private static String formatDateTime(Date d) {
		if (d == null)
			d = new Date();
		return dateTimeFormat.format(d);
	}

	private static String fix(String s) {
		if (s == null)
			return "";
		return s;
	}

	private static String fix(Integer i) {
		if (i == null)
			return "";
		return i.toString();
	}

	public String generateCostingsPdf(JemsEvent je) throws Exception {
		Map p = new HashMap();
		p.put("eventID", je.getId().toString());
		p.put("title", je.getTitle());
		p.put("organization", je.getOrganization().getName());

		List<JemsCostingItem> jcostings = jemsCostingItemService
				.findByJemsEventId(je.getId());
		List<JemsCostingItem> costings = new ArrayList<JemsCostingItem>();
		// double total = 0.0;
		double amount = 0.0;

		for (JemsCostingItem ci : jcostings) {
			if (ci.getQuantity() != null) {
				costings.add(ci);
				amount += ci.getAmount();
			}
		}

		p.put("costings", costings);
		p.put("amount", amount);

		String fileName = "JemsCostings_" + je.getId() + ".pdf";
		String reportPath = System.getProperty("java.io.tmpdir") + "/"
				+ fileName;

		JasperReport jr = getReport("WEB-INF/reports/"
				+ je.getOrganization().getId() + "/jemsCostings.jasper");
		JasperPrint print = JasperFillManager.fillReport(jr, p,
				new JREmptyDataSource());
		JasperExportManager.exportReportToPdfFile(print, reportPath);

		log.info("method: generateCostingsPdf, msg: generated costings pdf at "
				+ reportPath);

		return fileName;
	}

	@Override
	public String generateJobsheetPdf(JemsEvent je) throws Exception {
		Map p = new HashMap();

		p.put("eventID", je.getId().toString());
		p.put("title", je.getTitle());
		p.put("description", je.getDescription());
		p.put("organization", je.getOrganization().getName());
		p.put("type", je.getType().name());
		p.put("status", je.getStatus().name());
		p.put("startDateTime", formatDateTime(je.getStartDateTime()));
		p.put("endDateTime", formatDateTime(je.getEndDateTime()));
		p.put("location", je.getLocation());
		p.put("numberDrums", je.getNumberDrums().toString());
		p.put("numberPeople", je.getNumberPeople().toString());
		p.put("numberDrummers", je.getNumberDrummers().toString());
		p.put("numberSessions", je.getNumberSessions().toString());
		p.put("sessionTime", je.getSessionTime());
		p.put("chairsRequired", je.getChairsRequired().toString());
		p.put("staffAssigned2", getNiceStaff(je.getStaffAssigned2()));

		if (je.getUseInDocs() == 0) {
			p.put("company", je.getClientCompany());
			p.put("contactPerson", je.getClientContactPerson());
			p.put("phone", je.getClientPhone());
		} else {
			p.put("company", je.getHiringAgentCompany());
			p.put("contactPerson", je.getHiringAgentContactPerson());
			p.put("phone", je.getHiringAgentPhone());
		}

		p.put("notes", je.getNotes());

		// if(je.getJemsQuotation()!=null){
		// p.put("qNumber", je.getJemsQuotation().getQNumber().toString());
		// }else{
		// p.put("qNumber", "");
		// }
		//
		// if(je.getJemsInvoice()!=null){
		// p.put("iNumber", je.getJemsInvoice().getINumber().toString());
		// }else{
		// p.put("iNumber", "");
		// }

		// new qi model
		p.put("qNumber", "");
		p.put("iNumber", "");

		p.put("locationLatLong", je.getLocationLatLong());

		List<JemsCostingItem> jcostings = jemsCostingItemService
				.findByJemsEventId(je.getId());
		List<JemsCostingItem> costings = new ArrayList<JemsCostingItem>();

		for (JemsCostingItem ci : jcostings) {
			if (ci.getQuantity() != null & ci.getQuantity() != 0.0) {
				costings.add(ci);
			}
		}

		p.put("costings", costings);

		String fileName = "JemsJobSheet_" + je.getId() + ".pdf";
		String reportPath = System.getProperty("java.io.tmpdir") + "/"
				+ fileName;

		JasperReport jr = getReport("WEB-INF/reports/"
				+ je.getOrganization().getId() + "/jemsJobsheet2.jasper");
		JasperPrint print = JasperFillManager.fillReport(jr, p,
				new JREmptyDataSource());
		JasperExportManager.exportReportToPdfFile(print, reportPath);

		log.info("method: generateJobsheetPdf, msg: generated jobsheet pdf at "
				+ reportPath);

		return fileName;
	}

	private String getNiceStaff(Set<JemsStaff> staff) {
		StringBuilder sb = new StringBuilder();
		boolean comma = false;
		for (JemsStaff s : staff) {
			sb.append(s.getName()).append(", ");
			comma = true;
		}

		if (comma) {
			return sb.substring(0, sb.length() - 2);
		} else {
			return sb.toString();
		}
	}

}
