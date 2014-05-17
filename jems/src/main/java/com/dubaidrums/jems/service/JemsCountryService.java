package com.dubaidrums.jems.service;

import com.dubaidrums.jems.domain.JemsCountry;
import java.util.List;

public interface JemsCountryService {

	public abstract long countAllJemsCountrys();


	public abstract void deleteJemsCountry(JemsCountry jemsCountry);


	public abstract JemsCountry findJemsCountry(Long id);


	public abstract List<JemsCountry> findAllJemsCountrys();


	public abstract List<JemsCountry> findJemsCountryEntries(int firstResult, int maxResults);


	public abstract void saveJemsCountry(JemsCountry jemsCountry);


	public abstract JemsCountry updateJemsCountry(JemsCountry jemsCountry);


	public abstract List<JemsCountry> findActiveJemsCountrys();

}
