package com.dubaidrums.jems.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dubaidrums.jems.domain.JemsCostingCategory;
import com.dubaidrums.jems.repository.JemsCostingCategoryRepository;
import com.dubaidrums.jems.service.JemsCostingCategoryService;

@Service
@Transactional
public class JemsCostingCategoryServiceImpl implements
		JemsCostingCategoryService {

	@Autowired
	JemsCostingCategoryRepository jemsCostingCategoryRepository;

	public long countAllJemsCostingCategorys() {
		return jemsCostingCategoryRepository.count();
	}

	public void deleteJemsCostingCategory(
			JemsCostingCategory jemsCostingCategory) {
		jemsCostingCategoryRepository.delete(jemsCostingCategory);
	}

	public JemsCostingCategory findJemsCostingCategory(Long id) {
		return jemsCostingCategoryRepository.findOne(id);
	}

	public List<JemsCostingCategory> findAllJemsCostingCategorys() {
		return jemsCostingCategoryRepository.findAll();
	}

	public List<JemsCostingCategory> findJemsCostingCategoryEntries(
			int firstResult, int maxResults) {
		return jemsCostingCategoryRepository.findAll(
				new org.springframework.data.domain.PageRequest(firstResult
						/ maxResults, maxResults)).getContent();
	}

	public void saveJemsCostingCategory(JemsCostingCategory jemsCostingCategory) {
		jemsCostingCategoryRepository.save(jemsCostingCategory);
	}

	public JemsCostingCategory updateJemsCostingCategory(
			JemsCostingCategory jemsCostingCategory) {
		return jemsCostingCategoryRepository.save(jemsCostingCategory);
	}

	@Override
	public List<JemsCostingCategory> findAllJemsCostingCategorysByOrganizationId(
			Long oid) {
		return jemsCostingCategoryRepository.findByOrganizationId(oid);
	}
}
