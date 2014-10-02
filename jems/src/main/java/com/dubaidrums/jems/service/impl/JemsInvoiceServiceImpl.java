package com.dubaidrums.jems.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dubaidrums.jems.domain.JemsEvent;
import com.dubaidrums.jems.domain.JemsInvoice;
import com.dubaidrums.jems.domain.JemsQuotation;
import com.dubaidrums.jems.repository.JemsInvoiceRepository;
import com.dubaidrums.jems.repository.JemsQuotationRepository;
import com.dubaidrums.jems.service.JemsCountersService;
import com.dubaidrums.jems.service.JemsEventService;
import com.dubaidrums.jems.service.JemsInvoiceService;
import com.dubaidrums.jems.service.JemsSearchService;
import com.dubaidrums.jems.service.JemsUtilService;

@Service
@Transactional
public class JemsInvoiceServiceImpl implements JemsInvoiceService {

	@Autowired
	JemsEventService jemsEventService;

	@Autowired
	JemsCountersService jemsCountersService;

	@Autowired
	JemsInvoiceRepository jemsInvoiceRepository;

	@Autowired
	JemsQuotationRepository jemsQuotationRepository;

	@Autowired
	JemsSearchService jemsSearchService;

	@Autowired
	JemsUtilService jemsUtilService;

	public long countAllJemsInvoices() {
		return jemsInvoiceRepository.count();
	}

	public void deleteJemsInvoice(JemsInvoice jemsInvoice) {
		// jemsSearchService.updateJemsEventDocument(jemsEventService.findJemsEvent(jemsEventId));
		jemsInvoiceRepository.delete(jemsInvoice);
	}

	public JemsInvoice findJemsInvoice(Long id) {
		return jemsInvoiceRepository.findOne(id);
	}

	public List<JemsInvoice> findAllJemsInvoices() {
		return jemsInvoiceRepository.findAll();
	}

	public List<JemsInvoice> findJemsInvoiceEntries(int firstResult,
			int maxResults) {
		return jemsInvoiceRepository.findAll(
				new org.springframework.data.domain.PageRequest(firstResult
						/ maxResults, maxResults)).getContent();
	}

	// public void saveJemsInvoice(JemsInvoice jemsInvoice, Long jemsEventId) {
	// jemsInvoiceRepository.save(jemsInvoice);
	// jemsSearchService.createJemsEventDocument(jemsEventService.findJemsEvent(jemsEventId));
	// }

	public JemsInvoice updateJemsInvoice(JemsInvoice jemsInvoice) {
		// JemsEvent je = jemsEventService.findJemsEvent(jemsEventId);
		// jemsInvoice.setJemsEvent(je);

		JemsInvoice newJi = jemsInvoice.buildCopy();
		Date nd = new Date();
		newJi.setCreatedDate(nd);
		newJi.setModifiedDate(nd);
		newJi.setActive(true);
		newJi.setIDate(jemsUtilService.getCurrentDate());
		// newJi.setINumber(jemsCountersService.nextInvoiceNumber(je.getOrganization()));
		newJi.setINumber(jemsInvoice.getINumber());
		newJi.setRevisionNumber(jemsInvoice.getRevisionNumber() + 1);
		newJi = jemsInvoiceRepository.save(newJi);
		// je.setJemsInvoice(newJi);
		// jemsEventService.updateJemsEvent(je);

		JemsInvoice oldJi = findJemsInvoice(jemsInvoice.getId());
		oldJi.setActive(false);
		oldJi.setModifiedDate(nd);
		oldJi.setModifiedUser(newJi.getModifiedUser());
		jemsInvoiceRepository.save(oldJi);

		return newJi;
	}

	public synchronized void saveNewJemsInvoice(JemsInvoice jemsInvoice) {
		JemsQuotation jq = jemsInvoice.getJemsQuotation();

		jemsInvoice
				.setINumber(jemsCountersService.nextInvoiceNumber(jemsInvoice
						.getJemsEvent().getOrganization()));
		jemsInvoice = jemsInvoiceRepository.save(jemsInvoice);

		if (jq != null) {
			jq.setJemsInvoice(jemsInvoice);
			jq = jemsQuotationRepository.save(jemsInvoice.getJemsQuotation());
			jemsInvoice.setJemsQuotation(jq);
			jemsInvoiceRepository.save(jemsInvoice);
		}

		// jemsSearchService.createJemsEventDocument(jemsEventService.findJemsEvent(jemsEventId));
	}

	@Override
	public List<JemsInvoice> findByIDateBetween(Date start, Date end) {
		return jemsInvoiceRepository.findByIDateBetween(start, end);
	}

	@Override
	public List<JemsInvoice> findByActiveAndJemsEvent(boolean active,
			JemsEvent e) {
		return jemsInvoiceRepository.findByActiveAndJemsEvent(active, e);
	}
}
