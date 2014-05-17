package com.dubaidrums.jems.service;

import com.dubaidrums.jems.domain.JemsCounters;
import com.dubaidrums.jems.domain.JemsOrganization;

import java.util.List;

public interface JemsCountersService {

	public abstract long countAllJemsCounterses();


	public abstract void deleteJemsCounters(JemsCounters jemsCounters);


	public abstract JemsCounters findJemsCounters(Long id);


	public abstract List<JemsCounters> findAllJemsCounterses();


	public abstract List<JemsCounters> findJemsCountersEntries(int firstResult, int maxResults);


	public abstract void saveJemsCounters(JemsCounters jemsCounters);


	public abstract JemsCounters updateJemsCounters(JemsCounters jemsCounters);
	
	public abstract Integer nextQuotaionNumber(JemsOrganization organization);
	public abstract Integer nextInvoiceNumber(JemsOrganization organization);

}
