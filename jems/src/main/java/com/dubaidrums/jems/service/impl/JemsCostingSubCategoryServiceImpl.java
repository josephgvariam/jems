package com.dubaidrums.jems.service.impl;

import com.dubaidrums.jems.domain.JemsCostingSubCategory;
import com.dubaidrums.jems.repository.JemsCostingSubCategoryRepository;
import com.dubaidrums.jems.service.JemsCostingSubCategoryService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class JemsCostingSubCategoryServiceImpl implements JemsCostingSubCategoryService {

	@Autowired
    JemsCostingSubCategoryRepository jemsCostingSubCategoryRepository;

	public long countAllJemsCostingSubCategorys() {
        return jemsCostingSubCategoryRepository.count();
    }

	public void deleteJemsCostingSubCategory(JemsCostingSubCategory jemsCostingSubCategory) {
        jemsCostingSubCategoryRepository.delete(jemsCostingSubCategory);
    }

	public JemsCostingSubCategory findJemsCostingSubCategory(Long id) {
        return jemsCostingSubCategoryRepository.findOne(id);
    }

	public List<JemsCostingSubCategory> findAllJemsCostingSubCategorys() {
        return jemsCostingSubCategoryRepository.findAll();
    }

	public List<JemsCostingSubCategory> findJemsCostingSubCategoryEntries(int firstResult, int maxResults) {
        return jemsCostingSubCategoryRepository.findAll(new org.springframework.data.domain.PageRequest(firstResult / maxResults, maxResults)).getContent();
    }

	public void saveJemsCostingSubCategory(JemsCostingSubCategory jemsCostingSubCategory) {
        jemsCostingSubCategoryRepository.save(jemsCostingSubCategory);
    }

	public JemsCostingSubCategory updateJemsCostingSubCategory(JemsCostingSubCategory jemsCostingSubCategory) {
        return jemsCostingSubCategoryRepository.save(jemsCostingSubCategory);
    }

	@Override
	public List<JemsCostingSubCategory> findAllJemsCostingSubCategorysByCategoryId(Long cid) {
		return jemsCostingSubCategoryRepository.findByCategoryId(cid);
	}
}
