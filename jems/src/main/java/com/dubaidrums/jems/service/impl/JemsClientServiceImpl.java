package com.dubaidrums.jems.service.impl;

import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dubaidrums.jems.domain.JemsClient;
import com.dubaidrums.jems.domain.JemsEvent;
import com.dubaidrums.jems.repository.JemsClientRepository;
import com.dubaidrums.jems.service.JemsClientService;

@Service
@Transactional
public class JemsClientServiceImpl implements JemsClientService {
	Logger log = LogManager.getLogger(JemsClientServiceImpl.class);
	
	@Autowired
    JemsClientRepository jemsClientRepository;
	
	public long countAllJemsClients() {
        return jemsClientRepository.count();
    }

	public void deleteJemsClient(JemsClient jemsClient) {
        jemsClientRepository.delete(jemsClient);
    }

	public JemsClient findJemsClient(Long id) {
        return jemsClientRepository.findOne(id);
    }

	public List<JemsClient> findAllJemsClients() {
        return jemsClientRepository.findAll(new Sort(Sort.Direction.ASC, "company"));
    }

	public List<JemsClient> findJemsClientEntries(int firstResult, int maxResults) {
        return jemsClientRepository.findAll(new org.springframework.data.domain.PageRequest(firstResult / maxResults, maxResults)).getContent();
    }

	public void saveJemsClient(JemsClient jemsClient) {
        jemsClientRepository.save(jemsClient);
    }

	public JemsClient updateJemsClient(JemsClient jemsClient) {
        return jemsClientRepository.save(jemsClient);
    }

	public List<JemsClient> findActiveJemsClients() {
		return jemsClientRepository.findByActive(true);
	}

	@Async
	public void eventDeleted(JemsEvent e) {
		
	}

	@Async
	public void eventSavedOrUpdated(JemsEvent e) {
		if(e.getClientCompany()!=null && e.getClientCompany().length()!=0){
			JemsClient c = new JemsClient();
			c.setActive(true);
			c.setAddress(e.getClientAddress());
			c.setCompany(e.getClientCompany());
			c.setContactPerson(e.getClientContactPerson());
			c.setEmail(e.getClientEmail());
			c.setPhone(e.getClientPhone());
			c.getEvents().add(e);
			addorUpdateClient(c,e);				
		}
		
		if(e.getHiringAgentCompany()!=null && e.getHiringAgentCompany().length()!=0){
			JemsClient c = new JemsClient();
			c.setActive(true);
			c.setAddress(e.getHiringAgentAddress());
			c.setCompany(e.getHiringAgentCompany());
			c.setContactPerson(e.getHiringAgentContactPerson());
			c.setEmail(e.getHiringAgentEmail());
			c.setPhone(e.getHiringAgentPhone());
			c.getEvents().add(e);
			addorUpdateClient(c, e);				
		}
	}

	private void addorUpdateClient(JemsClient c, JemsEvent e) {
		JemsClient oc = findJemsClientByCompany(c.getCompany()); 
		if(oc==null){
			oc = findJemsClientByEvent(e);
			
			if(oc==null){
				//save
				saveJemsClient(c);
			}else{
				updateClient(oc,c);
			}
		}else{
			updateClient(oc, c);

		}
	}



	private void updateClient(JemsClient oc, JemsClient c) {
		oc.setAddress(c.getAddress());
		oc.setCompany(c.getCompany());
		oc.setContactPerson(c.getContactPerson());
		oc.setEmail(c.getEmail());
		oc.setPhone(c.getPhone());
		
		oc.getEvents().addAll(c.getEvents());
		
		updateJemsClient(oc);
	}

	public JemsClient findJemsClientByEvent(JemsEvent e) {
		List<JemsClient> clients = findAllJemsClients();
		for (JemsClient c : clients) {
			if(c.getEvents().contains(e)){
				return c;
			}
		}
		return null;
	}

	public JemsClient findJemsClientByCompany(String company) {
		return jemsClientRepository.findJemsClientByCompanyIgnoreCase(company.trim());
	}

}
