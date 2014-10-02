package com.dubaidrums.jems.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.dubaidrums.jems.domain.JemsEvent;
import com.dubaidrums.jems.domain.JemsQuotation;

@Repository
public interface JemsQuotationRepository extends
		JpaRepository<JemsQuotation, Long>,
		JpaSpecificationExecutor<JemsQuotation> {
	public List<JemsQuotation> findByQDateBetween(Date start, Date end);

	public List<JemsQuotation> findByActiveAndJemsEvent(boolean active,
			JemsEvent e);
}
