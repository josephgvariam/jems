package com.dubaidrums.jems.service;

import java.util.List;

import com.dubaidrums.jems.domain.JemsCountry;
import com.dubaidrums.jems.domain.JemsRegion;

public interface JemsRegionService {

	public abstract long countAllJemsRegions();

	public abstract void deleteJemsRegion(JemsRegion jemsRegion);

	public abstract JemsRegion findJemsRegion(Long id);

	public abstract List<JemsRegion> findAllJemsRegions();

	public abstract List<JemsRegion> findJemsRegionEntries(int firstResult,
			int maxResults);

	public abstract void saveJemsRegion(JemsRegion jemsRegion);

	public abstract JemsRegion updateJemsRegion(JemsRegion jemsRegion);

	public abstract List<JemsRegion> findActiveJemsRegions();

	public abstract List<JemsRegion> findByCountry(JemsCountry country);

}
