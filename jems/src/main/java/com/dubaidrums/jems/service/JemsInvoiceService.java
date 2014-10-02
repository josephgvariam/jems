package com.dubaidrums.jems.service;

import java.util.Date;
import java.util.List;

import com.dubaidrums.jems.domain.JemsEvent;
import com.dubaidrums.jems.domain.JemsInvoice;

public interface JemsInvoiceService {

	public abstract long countAllJemsInvoices();

	public abstract void deleteJemsInvoice(JemsInvoice jemsInvoice);

	public abstract JemsInvoice findJemsInvoice(Long id);

	public abstract List<JemsInvoice> findAllJemsInvoices();

	public abstract List<JemsInvoice> findJemsInvoiceEntries(int firstResult,
			int maxResults);

	// public abstract void saveJemsInvoice(JemsInvoice jemsInvoice, Long
	// jemsEventId);

	public abstract JemsInvoice updateJemsInvoice(JemsInvoice jemsInvoice);

	public abstract void saveNewJemsInvoice(JemsInvoice jemsInvoice);

	public abstract List<JemsInvoice> findByIDateBetween(Date start, Date end);

	public List<JemsInvoice> findByActiveAndJemsEvent(boolean active,
			JemsEvent e);

}
