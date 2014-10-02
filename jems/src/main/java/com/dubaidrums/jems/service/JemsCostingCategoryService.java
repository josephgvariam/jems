package com.dubaidrums.jems.service;

import java.util.List;

import com.dubaidrums.jems.domain.JemsCostingCategory;

public interface JemsCostingCategoryService {

	public abstract long countAllJemsCostingCategorys();

	public abstract void deleteJemsCostingCategory(
			JemsCostingCategory jemsCostingCategory);

	public abstract JemsCostingCategory findJemsCostingCategory(Long id);

	public abstract List<JemsCostingCategory> findAllJemsCostingCategorys();

	public abstract List<JemsCostingCategory> findJemsCostingCategoryEntries(
			int firstResult, int maxResults);

	public abstract void saveJemsCostingCategory(
			JemsCostingCategory jemsCostingCategory);

	public abstract JemsCostingCategory updateJemsCostingCategory(
			JemsCostingCategory jemsCostingCategory);

	public abstract List<JemsCostingCategory> findAllJemsCostingCategorysByOrganizationId(
			Long oid);

}
