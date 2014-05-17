package com.dubaidrums.jems.repository;

import java.util.List;

import com.dubaidrums.jems.domain.JemsOrganization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface JemsOrganizationRepository extends JpaSpecificationExecutor<JemsOrganization>, JpaRepository<JemsOrganization, Long> {

	List<JemsOrganization> findByActive(boolean active);
}
