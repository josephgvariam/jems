package com.dubaidrums.jems.service;

import com.dubaidrums.jems.domain.JemsEvent;
import com.dubaidrums.jems.domain.JemsInvoice;
import com.dubaidrums.jems.domain.JemsOrganization;
import com.dubaidrums.jems.domain.JemsQuotation;

public interface JemsReportingService {
	public String generateQuotationPdf(JemsQuotation jq) throws Exception;
	public String generateInvoicePdf(JemsInvoice ji) throws Exception;
	public String generateJobsheetPdf(JemsEvent je) throws Exception;
	public String generateCostingsPdf(JemsEvent je) throws Exception;
}
