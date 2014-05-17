package com.dubaidrums.jems.service;

import com.dubaidrums.jems.domain.JemsCurrency;
import java.util.List;

public interface JemsCurrencyService {

	public abstract long countAllJemsCurrencys();


	public abstract void deleteJemsCurrency(JemsCurrency jemsCurrency);


	public abstract JemsCurrency findJemsCurrency(Long id);


	public abstract List<JemsCurrency> findAllJemsCurrencys();


	public abstract List<JemsCurrency> findJemsCurrencyEntries(int firstResult, int maxResults);


	public abstract void saveJemsCurrency(JemsCurrency jemsCurrency);


	public abstract JemsCurrency updateJemsCurrency(JemsCurrency jemsCurrency);


	public abstract List<JemsCurrency> findActiveJemsCurrencys();

}
