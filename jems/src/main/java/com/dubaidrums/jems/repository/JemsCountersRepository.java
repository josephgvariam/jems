package com.dubaidrums.jems.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.dubaidrums.jems.domain.JemsCounters;
import com.dubaidrums.jems.domain.JemsOrganization;

@Repository
public interface JemsCountersRepository extends
		JpaSpecificationExecutor<JemsCounters>,
		JpaRepository<JemsCounters, Long> {

	JemsCounters findByOrganization(JemsOrganization organization);
}
