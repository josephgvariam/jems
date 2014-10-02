package com.dubaidrums.jems.service;

import java.util.List;

import com.dubaidrums.jems.domain.JemsCostingItem;

public interface JemsCostingItemService {

	public abstract long countAllJemsCostingItems();

	public abstract void deleteJemsCostingItem(JemsCostingItem jemsCostingItem);

	public abstract JemsCostingItem findJemsCostingItem(Long id);

	public abstract List<JemsCostingItem> findAllJemsCostingItems();

	public abstract List<JemsCostingItem> findJemsCostingItemEntries(
			int firstResult, int maxResults);

	public abstract void saveJemsCostingItem(JemsCostingItem jemsCostingItem);

	public abstract JemsCostingItem updateJemsCostingItem(
			JemsCostingItem jemsCostingItem);

	public List<JemsCostingItem> findByJemsEventId(Long id);
}
