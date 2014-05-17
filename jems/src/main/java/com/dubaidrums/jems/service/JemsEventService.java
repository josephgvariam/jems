package com.dubaidrums.jems.service;

import com.dubaidrums.jems.domain.JemsEvent;
import com.dubaidrums.jems.domain.JemsOrganization;
import com.dubaidrums.jems.domain.JemsUser;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.data.domain.Page;

public interface JemsEventService {

	public abstract long countAllJemsEvents();

	public abstract void deleteJemsEvent(JemsEvent jemsEvent);


	public abstract JemsEvent findJemsEvent(Long id);


	public abstract List<JemsEvent> findAllJemsEvents();


	//public abstract List<JemsEvent> findJemsEventEntries(int firstResult, int maxResults);
	public abstract Page<JemsEvent> findJemsEventEntries(JemsUser user, int firstResult, int maxResults);


	public abstract void saveJemsEvent(JemsEvent jemsEvent);


	public abstract JemsEvent updateJemsEvent(JemsEvent jemsEvent);


	public abstract List<JemsEvent> getCalendarEventsList(String selectedDate, JemsUser user);
	
	public String getEventDetails(JemsEvent je);
	
	public String getBillTo(JemsEvent je);

	public abstract List<JemsEvent> findByStartDateTimeBetweenAndOrganizationIn(Date start, Date end, Set<JemsOrganization> orgs);
	public abstract List<JemsEvent> findByStartDateTimeBetweenAndOrganizationId(Date start, Date end, Long orgId);
	
	
	public void generateRepeatEvents(String params, JemsEvent jemsEvent);
	
	public abstract Map<String, List> getReportData(String startDate, String endDate, String type, Long org, JemsUser user);

	
	public JemsEvent buildCopy(JemsEvent j) ;
}
