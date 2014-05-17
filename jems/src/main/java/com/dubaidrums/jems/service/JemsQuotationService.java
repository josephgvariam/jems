package com.dubaidrums.jems.service;

import com.dubaidrums.jems.domain.JemsEvent;
import com.dubaidrums.jems.domain.JemsQuotation;

import java.util.Date;
import java.util.List;

public interface JemsQuotationService {

	public abstract long countAllJemsQuotations();


	public abstract void deleteJemsQuotation(JemsQuotation jemsQuotation);


	public abstract JemsQuotation findJemsQuotation(Long id);


	public abstract List<JemsQuotation> findAllJemsQuotations();


	public abstract List<JemsQuotation> findJemsQuotationEntries(int firstResult, int maxResults);


	//public abstract void saveJemsQuotation(JemsQuotation jemsQuotation, Long jemsEventId);


	public abstract JemsQuotation updateJemsQuotation(JemsQuotation jemsQuotation);


	public abstract void saveNewJemsQuotation(JemsQuotation jemsQuotation);


	public abstract List<JemsQuotation> findByQDateBetween(Date start, Date end);
	
	public List<JemsQuotation> findByActiveAndJemsEvent(boolean active, JemsEvent e);

}
