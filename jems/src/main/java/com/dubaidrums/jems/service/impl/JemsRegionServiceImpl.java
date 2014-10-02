package com.dubaidrums.jems.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dubaidrums.jems.domain.JemsCountry;
import com.dubaidrums.jems.domain.JemsRegion;
import com.dubaidrums.jems.repository.JemsRegionRepository;
import com.dubaidrums.jems.service.JemsRegionService;

@Service
@Transactional
public class JemsRegionServiceImpl implements JemsRegionService {

	@Autowired
	JemsRegionRepository jemsRegionRepository;

	public long countAllJemsRegions() {
		return jemsRegionRepository.count();
	}

	public void deleteJemsRegion(JemsRegion jemsRegion) {
		jemsRegionRepository.delete(jemsRegion);
	}

	public JemsRegion findJemsRegion(Long id) {
		return jemsRegionRepository.findOne(id);
	}

	public List<JemsRegion> findAllJemsRegions() {
		return jemsRegionRepository.findAll();
	}

	public List<JemsRegion> findJemsRegionEntries(int firstResult,
			int maxResults) {
		return jemsRegionRepository.findAll(
				new org.springframework.data.domain.PageRequest(firstResult
						/ maxResults, maxResults)).getContent();
	}

	public void saveJemsRegion(JemsRegion jemsRegion) {
		jemsRegionRepository.save(jemsRegion);
	}

	public JemsRegion updateJemsRegion(JemsRegion jemsRegion) {
		return jemsRegionRepository.save(jemsRegion);
	}

	public List<JemsRegion> findActiveJemsRegions() {
		return jemsRegionRepository.findByActive(true);
	}

	@Override
	public List<JemsRegion> findByCountry(JemsCountry country) {
		return jemsRegionRepository.findByCountry(country);
	}
}
