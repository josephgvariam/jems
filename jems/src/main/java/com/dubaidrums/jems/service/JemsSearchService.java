package com.dubaidrums.jems.service;

import java.util.Date;
import java.util.List;

import com.dubaidrums.jems.domain.JemsEvent;
import com.dubaidrums.jems.domain.JemsUser;

public interface JemsSearchService {
	public long getDocumentCount();

	public void setupSearch();

	public void createJemsEventDocument(JemsEvent je);

	public void deleteJemsEventDocument(JemsEvent je);

	public void updateJemsEventDocument(JemsEvent je);

	public void deleteIndex();

	public void createIndex(List<JemsEvent> events);

	public String query(String q, boolean chkdates, boolean chkpaid,
			boolean chktype, boolean chkeventstatus, Date startdatetime,
			Date enddatetime, boolean paidstatus, String eventtype,
			String eventstatus, JemsUser user);
}
