package com.dubaidrums.jems.service;

import java.util.List;

import com.dubaidrums.jems.domain.JemsSms;

public interface JemsSmsService {

	public abstract long countAllJemsSmses();

	public abstract void deleteJemsSms(JemsSms jemsSms);

	public abstract JemsSms findJemsSms(Long id);

	public abstract List<JemsSms> findAllJemsSmses();

	public abstract List<JemsSms> findJemsSmsEntries(int firstResult,
			int maxResults);

	public abstract void saveJemsSms(JemsSms jemsSms);

	public abstract JemsSms updateJemsSms(JemsSms jemsSms);

}
