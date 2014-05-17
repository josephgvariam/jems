package com.dubaidrums.jems.service;

import java.util.List;

import com.dubaidrums.jems.domain.JemsTax;

public interface JemsTaxService {

	public abstract long countAllJemsTaxes();


	public abstract void deleteJemsTax(JemsTax jemsTax);


	public abstract JemsTax findJemsTax(Long id);


	public abstract List<JemsTax> findAllJemsTaxes();


	public abstract List<JemsTax> findJemsTaxEntries(int firstResult, int maxResults);


	public abstract void saveJemsTax(JemsTax jemsTax);


	public abstract JemsTax updateJemsTax(JemsTax jemsTax);

}
