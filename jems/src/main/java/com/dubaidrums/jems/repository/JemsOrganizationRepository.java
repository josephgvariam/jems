package com.dubaidrums.jems.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.dubaidrums.jems.domain.JemsOrganization;

@Repository
public interface JemsOrganizationRepository extends
		JpaSpecificationExecutor<JemsOrganization>,
		JpaRepository<JemsOrganization, Long> {

	List<JemsOrganization> findByActive(boolean active);
}
