package com.dubaidrums.jems.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dubaidrums.jems.domain.JemsCostingCategory;
import com.dubaidrums.jems.domain.JemsCostingItem;
import com.dubaidrums.jems.domain.JemsCostingSubCategory;
import com.dubaidrums.jems.domain.JemsEvent;
import com.dubaidrums.jems.repository.JemsCostingItemRepository;
import com.dubaidrums.jems.service.JemsCostingCategoryService;
import com.dubaidrums.jems.service.JemsCostingItemService;
import com.dubaidrums.jems.service.JemsCostingSubCategoryService;
import com.dubaidrums.jems.service.JemsEventService;

@Service
@Transactional
public class JemsCostingItemServiceImpl implements JemsCostingItemService {

	@Autowired
	JemsCostingItemRepository jemsCostingItemRepository;

	@Autowired
	JemsCostingCategoryService jemsCostingCategoryService;

	@Autowired
	JemsCostingSubCategoryService jemsCostingSubCategoryService;

	@Autowired
	JemsEventService jemsEventService;

	public long countAllJemsCostingItems() {
		return jemsCostingItemRepository.count();
	}

	public void deleteJemsCostingItem(JemsCostingItem jemsCostingItem) {
		jemsCostingItemRepository.delete(jemsCostingItem);
	}

	public JemsCostingItem findJemsCostingItem(Long id) {
		return jemsCostingItemRepository.findOne(id);
	}

	public List<JemsCostingItem> findAllJemsCostingItems() {
		return jemsCostingItemRepository.findAll();
	}

	public List<JemsCostingItem> findJemsCostingItemEntries(int firstResult,
			int maxResults) {
		return jemsCostingItemRepository.findAll(
				new org.springframework.data.domain.PageRequest(firstResult
						/ maxResults, maxResults)).getContent();
	}

	public void saveJemsCostingItem(JemsCostingItem jemsCostingItem) {
		jemsCostingItemRepository.save(jemsCostingItem);
	}

	public JemsCostingItem updateJemsCostingItem(JemsCostingItem jemsCostingItem) {
		return jemsCostingItemRepository.save(jemsCostingItem);
	}

	@Override
	public List<JemsCostingItem> findByJemsEventId(Long id) {
		List<JemsCostingItem> costings = jemsCostingItemRepository
				.findByJemsEventId(id);
		if (costings == null || costings.size() == 0) {
			costings = new ArrayList<JemsCostingItem>();

			JemsEvent je = jemsEventService.findJemsEvent(id);
			List<JemsCostingCategory> categorys = jemsCostingCategoryService
					.findAllJemsCostingCategorysByOrganizationId(je
							.getOrganization().getId());
			for (JemsCostingCategory category : categorys) {
				List<JemsCostingSubCategory> subcategorys = jemsCostingSubCategoryService
						.findAllJemsCostingSubCategorysByCategoryId(category
								.getId());
				for (JemsCostingSubCategory subcategory : subcategorys) {
					JemsCostingItem jci = new JemsCostingItem();
					jci.setCategory(category.getName());
					jci.setSubCategory(subcategory.getName());
					jci.setOrganization(je.getOrganization());
					jci.setJemsEvent(je);
					jci.setRate(subcategory.getRate());
					jci.setQuantity(0.0);

					costings.add(jci);
				}
			}
		}

		return costings;
	}
}
