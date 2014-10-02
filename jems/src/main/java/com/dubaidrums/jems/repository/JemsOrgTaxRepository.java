package com.dubaidrums.jems.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.dubaidrums.jems.domain.JemsOrgTax;

@Repository
public interface JemsOrgTaxRepository extends
		JpaSpecificationExecutor<JemsOrgTax>, JpaRepository<JemsOrgTax, Long> {
}
