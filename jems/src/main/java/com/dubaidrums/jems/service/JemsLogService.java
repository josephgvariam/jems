package com.dubaidrums.jems.service;

import java.util.List;

import com.dubaidrums.jems.domain.JemsLog;

public interface JemsLogService {
	public abstract long countAllJemsLogs();


	public abstract void deleteJemsLog(JemsLog jemsLog);


	public abstract JemsLog findJemsLog(Long id);


	public abstract List<JemsLog> findAllJemsLogs();


	public abstract List<JemsLog> findJemsLogEntries(int firstResult, int maxResults);


	public abstract void saveJemsLog(JemsLog jemsLog);


	public abstract JemsLog updateJemsLog(JemsLog jemsLog);


	public abstract List<JemsLog> findLatestLogs(int size);


	public abstract void newLog(String string);
}
