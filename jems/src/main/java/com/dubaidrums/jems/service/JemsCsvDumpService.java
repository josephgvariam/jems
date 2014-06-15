package com.dubaidrums.jems.service;

import javax.servlet.http.HttpServletRequest;

public interface JemsCsvDumpService {
	public String dumpCsv(boolean searchDate, String startDate, String endDate, HttpServletRequest r);
}
