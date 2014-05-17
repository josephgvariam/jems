package com.dubaidrums.jems.repository;

import java.util.List;

import com.dubaidrums.jems.domain.JemsCurrency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface JemsCurrencyRepository extends JpaSpecificationExecutor<JemsCurrency>, JpaRepository<JemsCurrency, Long> {

	List<JemsCurrency> findByActive(boolean active);
}
