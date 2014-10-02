package com.dubaidrums.jems.service;

import java.util.List;

import com.dubaidrums.jems.domain.JemsCostingSubCategory;

public interface JemsCostingSubCategoryService {

	public abstract long countAllJemsCostingSubCategorys();

	public abstract void deleteJemsCostingSubCategory(
			JemsCostingSubCategory jemsCostingSubCategory);

	public abstract JemsCostingSubCategory findJemsCostingSubCategory(Long id);

	public abstract List<JemsCostingSubCategory> findAllJemsCostingSubCategorys();

	public abstract List<JemsCostingSubCategory> findJemsCostingSubCategoryEntries(
			int firstResult, int maxResults);

	public abstract void saveJemsCostingSubCategory(
			JemsCostingSubCategory jemsCostingSubCategory);

	public abstract JemsCostingSubCategory updateJemsCostingSubCategory(
			JemsCostingSubCategory jemsCostingSubCategory);

	public abstract List<JemsCostingSubCategory> findAllJemsCostingSubCategorysByCategoryId(
			Long cid);

}
