package com.dubaidrums.jems.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.dubaidrums.jems.domain.JemsEvent;
import com.dubaidrums.jems.domain.JemsInvoice;

@Repository
public interface JemsInvoiceRepository extends
		JpaSpecificationExecutor<JemsInvoice>, JpaRepository<JemsInvoice, Long> {
	public List<JemsInvoice> findByIDateBetween(Date start, Date end);

	public List<JemsInvoice> findByActiveAndJemsEvent(boolean active,
			JemsEvent e);
}
