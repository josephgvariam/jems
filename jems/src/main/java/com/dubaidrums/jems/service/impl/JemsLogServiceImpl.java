package com.dubaidrums.jems.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dubaidrums.jems.domain.JemsLog;
import com.dubaidrums.jems.repository.JemsLogRepository;
import com.dubaidrums.jems.service.JemsLogService;
import com.dubaidrums.jems.service.JemsUtilService;

@Service
@Transactional
public class JemsLogServiceImpl implements JemsLogService {

	@Autowired
	JemsLogRepository jemsLogRepository;

	@Autowired
	JemsUtilService jemsUtilService;

	public long countAllJemsLogs() {
		return jemsLogRepository.count();
	}

	public void deleteJemsLog(JemsLog jemsLog) {
		jemsLogRepository.delete(jemsLog);
	}

	public JemsLog findJemsLog(Long id) {
		return jemsLogRepository.findOne(id);
	}

	public List<JemsLog> findAllJemsLogs() {
		return jemsLogRepository.findAll();
	}

	public List<JemsLog> findJemsLogEntries(int firstResult, int maxResults) {
		return jemsLogRepository.findAll(
				new org.springframework.data.domain.PageRequest(firstResult
						/ maxResults, maxResults)).getContent();
	}

	public void saveJemsLog(JemsLog jemsLog) {
		jemsLogRepository.save(jemsLog);
	}

	public JemsLog updateJemsLog(JemsLog jemsLog) {
		return jemsLogRepository.save(jemsLog);
	}

	public List<JemsLog> findLatestLogs(int size) {
		return jemsLogRepository.findAll(
				new org.springframework.data.domain.PageRequest(0, size,
						new Sort(Sort.Direction.DESC, "logDateTime")))
				.getContent();
	}

	@Override
	public void newLog(String msg) {
		JemsLog l = new JemsLog();
		l.setMsg(msg);
		l.setLogDateTime(jemsUtilService.getCurrentDate());

		saveJemsLog(l);
	}
}
