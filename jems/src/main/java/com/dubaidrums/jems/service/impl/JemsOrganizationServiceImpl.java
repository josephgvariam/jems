package com.dubaidrums.jems.service.impl;

import com.dubaidrums.jems.domain.JemsOrganization;
import com.dubaidrums.jems.repository.JemsOrganizationRepository;
import com.dubaidrums.jems.service.JemsOrganizationService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class JemsOrganizationServiceImpl implements JemsOrganizationService {

	@Autowired
    JemsOrganizationRepository jemsOrganizationRepository;

	public long countAllJemsOrganizations() {
        return jemsOrganizationRepository.count();
    }

	public void deleteJemsOrganization(JemsOrganization jemsOrganization) {
        jemsOrganizationRepository.delete(jemsOrganization);
    }

	public JemsOrganization findJemsOrganization(Long id) {
        return jemsOrganizationRepository.findOne(id);
    }

	public List<JemsOrganization> findAllJemsOrganizations() {
        return jemsOrganizationRepository.findAll();
    }

	public List<JemsOrganization> findJemsOrganizationEntries(int firstResult, int maxResults) {
        return jemsOrganizationRepository.findAll(new org.springframework.data.domain.PageRequest(firstResult / maxResults, maxResults)).getContent();
    }

	public void saveJemsOrganization(JemsOrganization jemsOrganization) {
        jemsOrganizationRepository.save(jemsOrganization);
    }

	public JemsOrganization updateJemsOrganization(JemsOrganization jemsOrganization) {
        return jemsOrganizationRepository.save(jemsOrganization);
    }

	@Override
	public List<JemsOrganization> findActiveJemsOrganizations() {
		return jemsOrganizationRepository.findByActive(true);
	}
}
