package com.dubaidrums.jems.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.dubaidrums.jems.domain.JemsCountry;
import com.dubaidrums.jems.domain.JemsRegion;

@Repository
public interface JemsRegionRepository extends
		JpaSpecificationExecutor<JemsRegion>, JpaRepository<JemsRegion, Long> {

	List<JemsRegion> findByActive(boolean active);

	List<JemsRegion> findByCountry(JemsCountry country);
}
