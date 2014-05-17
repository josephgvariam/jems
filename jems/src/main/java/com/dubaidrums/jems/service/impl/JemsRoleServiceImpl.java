package com.dubaidrums.jems.service.impl;

import com.dubaidrums.jems.domain.JemsRole;
import com.dubaidrums.jems.repository.JemsRoleRepository;
import com.dubaidrums.jems.service.JemsRoleService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class JemsRoleServiceImpl implements JemsRoleService {
	
	public JemsRole findByRoleName(String rolename){
		return jemsRoleRepository.findByRoleName(rolename);
	}

	@Autowired
    JemsRoleRepository jemsRoleRepository;

	public long countAllJemsRoles() {
        return jemsRoleRepository.count();
    }

	public void deleteJemsRole(JemsRole jemsRole) {
        jemsRoleRepository.delete(jemsRole);
    }

	public JemsRole findJemsRole(Long id) {
        return jemsRoleRepository.findOne(id);
    }

	public List<JemsRole> findAllJemsRoles() {
        return jemsRoleRepository.findAll();
    }

	public List<JemsRole> findJemsRoleEntries(int firstResult, int maxResults) {
        return jemsRoleRepository.findAll(new org.springframework.data.domain.PageRequest(firstResult / maxResults, maxResults)).getContent();
    }

	public void saveJemsRole(JemsRole jemsRole) {
        jemsRoleRepository.save(jemsRole);
    }

	public JemsRole updateJemsRole(JemsRole jemsRole) {
        return jemsRoleRepository.save(jemsRole);
    }
}
