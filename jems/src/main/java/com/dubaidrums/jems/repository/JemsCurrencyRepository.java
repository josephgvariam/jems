package com.dubaidrums.jems.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.dubaidrums.jems.domain.JemsCurrency;

@Repository
public interface JemsCurrencyRepository extends
		JpaSpecificationExecutor<JemsCurrency>,
		JpaRepository<JemsCurrency, Long> {

	List<JemsCurrency> findByActive(boolean active);
}
