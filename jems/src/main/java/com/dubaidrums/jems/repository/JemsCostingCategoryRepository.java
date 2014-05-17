package com.dubaidrums.jems.repository;

import java.util.List;

import com.dubaidrums.jems.domain.JemsCostingCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface JemsCostingCategoryRepository extends JpaSpecificationExecutor<JemsCostingCategory>, JpaRepository<JemsCostingCategory, Long> {

	List<JemsCostingCategory> findByOrganizationId(Long oid);
}