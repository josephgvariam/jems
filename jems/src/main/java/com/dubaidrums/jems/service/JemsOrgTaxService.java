package com.dubaidrums.jems.service;

import java.util.List;

import com.dubaidrums.jems.domain.JemsOrgTax;

public interface JemsOrgTaxService {

	public abstract long countAllJemsOrgTaxes();

	public abstract void deleteJemsOrgTax(JemsOrgTax jemsOrgTax);

	public abstract JemsOrgTax findJemsOrgTax(Long id);

	public abstract List<JemsOrgTax> findAllJemsOrgTaxes();

	public abstract List<JemsOrgTax> findJemsOrgTaxEntries(int firstResult,
			int maxResults);

	public abstract void saveJemsOrgTax(JemsOrgTax jemsOrgTax);

	public abstract JemsOrgTax updateJemsOrgTax(JemsOrgTax jemsOrgTax);

}
