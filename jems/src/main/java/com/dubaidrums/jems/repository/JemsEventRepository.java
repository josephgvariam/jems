package com.dubaidrums.jems.repository;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.dubaidrums.jems.domain.JemsEvent;
import com.dubaidrums.jems.domain.JemsOrganization;
import com.dubaidrums.jems.repository.custom.JemsEventRepositoryCustom;

import org.joda.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface JemsEventRepository extends JpaSpecificationExecutor<JemsEvent>, JpaRepository<JemsEvent, Long>, JemsEventRepositoryCustom {
	public List<JemsEvent> findByStartDateTimeBetweenAndOrganizationInAndActiveTrue(Date start, Date end, Set<JemsOrganization> orgs);

	public List<JemsEvent> findByStartDateTimeBetweenAndOrganizationIdAndActiveTrue(Date start, Date end, Long orgId);

	public Page<JemsEvent> findByOrganizationInAndActiveTrue(Set<JemsOrganization> organizations, Pageable pageRequest);
	
	public List<JemsEvent> findByActiveTrue();

}
