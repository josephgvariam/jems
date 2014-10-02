package com.dubaidrums.jems.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dubaidrums.jems.domain.JemsCounters;
import com.dubaidrums.jems.domain.JemsOrganization;
import com.dubaidrums.jems.repository.JemsCountersRepository;
import com.dubaidrums.jems.service.JemsCountersService;

@Service
@Transactional
public class JemsCountersServiceImpl implements JemsCountersService {

	@Autowired
	JemsCountersRepository jemsCountersRepository;

	public long countAllJemsCounterses() {
		return jemsCountersRepository.count();
	}

	public void deleteJemsCounters(JemsCounters jemsCounters) {
		jemsCountersRepository.delete(jemsCounters);
	}

	public JemsCounters findJemsCounters(Long id) {
		return jemsCountersRepository.findOne(id);
	}

	public List<JemsCounters> findAllJemsCounterses() {
		return jemsCountersRepository.findAll();
	}

	public List<JemsCounters> findJemsCountersEntries(int firstResult,
			int maxResults) {
		return jemsCountersRepository.findAll(
				new org.springframework.data.domain.PageRequest(firstResult
						/ maxResults, maxResults)).getContent();
	}

	public void saveJemsCounters(JemsCounters jemsCounters) {
		jemsCountersRepository.save(jemsCounters);
	}

	public JemsCounters updateJemsCounters(JemsCounters jemsCounters) {
		return jemsCountersRepository.save(jemsCounters);
	}

	public Integer nextQuotaionNumber(JemsOrganization organization) {
		JemsCounters jc = jemsCountersRepository
				.findByOrganization(organization);
		Integer i = jc.getQCount();
		jc.setQCount(i + 1);
		jemsCountersRepository.save(jc);
		return i;
	}

	public Integer nextInvoiceNumber(JemsOrganization organization) {
		JemsCounters jc = jemsCountersRepository
				.findByOrganization(organization);
		Integer i = jc.getICount();
		jc.setICount(i + 1);
		jemsCountersRepository.save(jc);
		return i;
	}
}
