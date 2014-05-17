package com.dubaidrums.jems.repository;

import java.util.List;

import com.dubaidrums.jems.domain.JemsCostingSubCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface JemsCostingSubCategoryRepository extends JpaSpecificationExecutor<JemsCostingSubCategory>, JpaRepository<JemsCostingSubCategory, Long> {

	List<JemsCostingSubCategory> findByCategoryId(Long cid);
}
