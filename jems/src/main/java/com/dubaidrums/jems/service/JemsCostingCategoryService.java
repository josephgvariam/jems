package com.dubaidrums.jems.service;

import com.dubaidrums.jems.domain.JemsCostingCategory;
import java.util.List;

public interface JemsCostingCategoryService {

	public abstract long countAllJemsCostingCategorys();


	public abstract void deleteJemsCostingCategory(JemsCostingCategory jemsCostingCategory);


	public abstract JemsCostingCategory findJemsCostingCategory(Long id);


	public abstract List<JemsCostingCategory> findAllJemsCostingCategorys();


	public abstract List<JemsCostingCategory> findJemsCostingCategoryEntries(int firstResult, int maxResults);


	public abstract void saveJemsCostingCategory(JemsCostingCategory jemsCostingCategory);


	public abstract JemsCostingCategory updateJemsCostingCategory(JemsCostingCategory jemsCostingCategory);


	public abstract List<JemsCostingCategory> findAllJemsCostingCategorysByOrganizationId(Long oid);

}
