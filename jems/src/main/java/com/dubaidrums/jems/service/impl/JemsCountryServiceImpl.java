package com.dubaidrums.jems.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dubaidrums.jems.domain.JemsCountry;
import com.dubaidrums.jems.repository.JemsCountryRepository;
import com.dubaidrums.jems.service.JemsCountryService;

@Service
@Transactional
public class JemsCountryServiceImpl implements JemsCountryService {

	@Autowired
	JemsCountryRepository jemsCountryRepository;

	public long countAllJemsCountrys() {
		return jemsCountryRepository.count();
	}

	public void deleteJemsCountry(JemsCountry jemsCountry) {
		jemsCountryRepository.delete(jemsCountry);
	}

	public JemsCountry findJemsCountry(Long id) {
		return jemsCountryRepository.findOne(id);
	}

	public List<JemsCountry> findAllJemsCountrys() {
		return jemsCountryRepository.findAll(new Sort("name"));
	}

	public List<JemsCountry> findJemsCountryEntries(int firstResult,
			int maxResults) {
		return jemsCountryRepository.findAll(
				new org.springframework.data.domain.PageRequest(firstResult
						/ maxResults, maxResults)).getContent();
	}

	public void saveJemsCountry(JemsCountry jemsCountry) {
		jemsCountryRepository.save(jemsCountry);
	}

	public JemsCountry updateJemsCountry(JemsCountry jemsCountry) {
		return jemsCountryRepository.save(jemsCountry);
	}

	public List<JemsCountry> findActiveJemsCountrys() {
		return jemsCountryRepository.findByActiveOrderByNameAsc(true);
	}
}
