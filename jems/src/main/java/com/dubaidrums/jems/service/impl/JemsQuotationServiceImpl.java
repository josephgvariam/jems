package com.dubaidrums.jems.service.impl;

import com.dubaidrums.jems.domain.JemsEvent;
import com.dubaidrums.jems.domain.JemsQuotation;
import com.dubaidrums.jems.repository.JemsQuotationRepository;
import com.dubaidrums.jems.service.JemsCountersService;
import com.dubaidrums.jems.service.JemsEventService;
import com.dubaidrums.jems.service.JemsQuotationService;
import com.dubaidrums.jems.service.JemsSearchService;
import com.dubaidrums.jems.service.JemsUtilService;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class JemsQuotationServiceImpl implements JemsQuotationService {
	
	@Autowired
    JemsEventService jemsEventService;
	
	@Autowired
	JemsCountersService jemsCountersService;

	@Autowired
    JemsQuotationRepository jemsQuotationRepository;
	
	@Autowired
    JemsSearchService jemsSearchService;	
	
	@Autowired
    JemsUtilService jemsUtilService;	

	public long countAllJemsQuotations() {
        return jemsQuotationRepository.count();
    }

	public void deleteJemsQuotation(JemsQuotation jemsQuotation) {
		//jemsSearchService.deleteJemsEventDocument(jemsEventService.findJemsEvent(jemsEventId));
        jemsQuotationRepository.delete(jemsQuotation);
    }

	public JemsQuotation findJemsQuotation(Long id) {
        return jemsQuotationRepository.findOne(id);
    }

	public List<JemsQuotation> findAllJemsQuotations() {
        return jemsQuotationRepository.findAll();
    }

	public List<JemsQuotation> findJemsQuotationEntries(int firstResult, int maxResults) {
        return jemsQuotationRepository.findAll(new org.springframework.data.domain.PageRequest(firstResult / maxResults, maxResults)).getContent();
    }

//	public void saveJemsQuotation(JemsQuotation jemsQuotation, Long jemsEventId) {
//        jemsQuotationRepository.save(jemsQuotation);
//        jemsSearchService.createJemsEventDocument(jemsEventService.findJemsEvent(jemsEventId));
//    }

	public JemsQuotation updateJemsQuotation(JemsQuotation jemsQuotation) {
		//JemsEvent je = jemsEventService.findJemsEvent(jemsEventId);
		//jemsQuotation.setJemsEvent(je);
		
		JemsQuotation newJq = jemsQuotation.buildCopy();
		Date nd = new Date();
		newJq.setCreatedDate(nd);
		newJq.setModifiedDate(nd);
		newJq.setActive(true);
		newJq.setQDate(jemsUtilService.getCurrentDate());
		//newJq.setQNumber(jemsCountersService.nextQuotaionNumber(jemsQuotation.getJemsEvent().getOrganization()));
		newJq.setQNumber(jemsQuotation.getQNumber());
		newJq.setRevisionNumber(jemsQuotation.getRevisionNumber()+1);
		newJq = jemsQuotationRepository.save(newJq);
		//je.setJemsQuotation(newJq);
		//jemsEventService.updateJemsEvent(je);
		
		
		JemsQuotation oldJq = findJemsQuotation(jemsQuotation.getId());
		oldJq.setModifiedDate(nd);
		oldJq.setModifiedUser(newJq.getModifiedUser());
		oldJq.setActive(false);		
		jemsQuotationRepository.save(oldJq);
		
        return newJq;
    }

	public synchronized void saveNewJemsQuotation(JemsQuotation jemsQuotation) {
		jemsQuotation.setQNumber(jemsCountersService.nextQuotaionNumber(jemsQuotation.getJemsEvent().getOrganization()));
		jemsQuotationRepository.save(jemsQuotation);
        

	}

	@Override
	public List<JemsQuotation> findByQDateBetween(Date start, Date end) {
		return jemsQuotationRepository.findByQDateBetween(start,end);
	}

	@Override
	public List<JemsQuotation> findByActiveAndJemsEvent(boolean active, JemsEvent e) {
		return jemsQuotationRepository.findByActiveAndJemsEvent(active, e);
	}
}
