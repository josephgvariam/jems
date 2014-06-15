package com.dubaidrums.jems.domain;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class JemsViewRowMapper implements RowMapper<JemsView> {

	@Override
	public JemsView mapRow(ResultSet rs, int rowNum) throws SQLException {
		JemsView v = new JemsView();
        
        v.setChairRequired(rs.getString("chairs_required"));        
        v.setCompanyContactPerson(rs.getString("company_contact_person"));
        v.setCompanyContactNumber(rs.getString("company_contact_number"));
        v.setCompanyEmail(rs.getString("company_email"));
        v.setCompanyName(rs.getString("company_name"));
        v.setCountry(rs.getString("country"));
        v.setCurrency(rs.getString("currency"));
        v.setEventDate(rs.getString("event_date"));
        v.setEventDescription(rs.getString("event_description"));
        v.setEventLink(rs.getString("event_link"));
        v.setEventNumber(rs.getString("event_number"));
        v.setEventStatus(rs.getString("event_status"));
        v.setEventTitle(rs.getString("event_title"));
        v.setEventType(rs.getString("event_type"));        
        v.setGps(rs.getString("gps"));
        v.setInvoicedAmount(rs.getString("invoiced_amount"));
        v.setInvoiceDate(rs.getString("invoice_date"));
        v.setInvoiceLink(rs.getString("invoice_link"));
        v.setInvoiceNumber(rs.getString("invoice_number"));
        v.setLocation(rs.getString("location"));
        v.setNumberDrums(rs.getString("number_drums"));
        v.setNumberFacilitators(rs.getString("number_facilitators"));
        v.setNumberPeople(rs.getString("number_people"));
        v.setNumberSessions(rs.getString("number_sessions"));
        v.setOrganization(rs.getString("organization"));
        v.setPaidAmount(rs.getString("paid_amount"));
        v.setPaidStatus(rs.getString("paid_status"));
        v.setPaidDate(rs.getString("paid_date"));
        v.setQuotationDate(rs.getString("quotation_date"));
        v.setQuotationNumber(rs.getString("quotation_number"));
        v.setQuotationLink(rs.getString("quotation_link"));
        v.setQuotedAmount(rs.getString("quoted_amount"));
        v.setRecieptVoucher(rs.getString("receipt_voucher"));
        v.setRegion(rs.getString("region"));
        v.setStaffAssigned(rs.getString("staff_assigned"));
 
        return v;
	}

}
