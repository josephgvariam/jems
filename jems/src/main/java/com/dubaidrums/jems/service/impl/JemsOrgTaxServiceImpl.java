package com.dubaidrums.jems.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dubaidrums.jems.domain.JemsOrgTax;
import com.dubaidrums.jems.repository.JemsOrgTaxRepository;
import com.dubaidrums.jems.service.JemsOrgTaxService;

@Service
@Transactional
public class JemsOrgTaxServiceImpl implements JemsOrgTaxService {

	@Autowired
	JemsOrgTaxRepository jemsOrgTaxRepository;

	public long countAllJemsOrgTaxes() {
		return jemsOrgTaxRepository.count();
	}

	public void deleteJemsOrgTax(JemsOrgTax jemsOrgTax) {
		jemsOrgTaxRepository.delete(jemsOrgTax);
	}

	public JemsOrgTax findJemsOrgTax(Long id) {
		return jemsOrgTaxRepository.findOne(id);
	}

	public List<JemsOrgTax> findAllJemsOrgTaxes() {
		return jemsOrgTaxRepository.findAll();
	}

	public List<JemsOrgTax> findJemsOrgTaxEntries(int firstResult,
			int maxResults) {
		return jemsOrgTaxRepository.findAll(
				new org.springframework.data.domain.PageRequest(firstResult
						/ maxResults, maxResults)).getContent();
	}

	public void saveJemsOrgTax(JemsOrgTax jemsOrgTax) {
		jemsOrgTaxRepository.save(jemsOrgTax);
	}

	public JemsOrgTax updateJemsOrgTax(JemsOrgTax jemsOrgTax) {
		return jemsOrgTaxRepository.save(jemsOrgTax);
	}
}
