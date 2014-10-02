package com.dubaidrums.jems.service;

import java.util.List;

import com.dubaidrums.jems.domain.JemsRole;

public interface JemsRoleService {
	public JemsRole findByRoleName(String rolename);

	public abstract long countAllJemsRoles();

	public abstract void deleteJemsRole(JemsRole jemsRole);

	public abstract JemsRole findJemsRole(Long id);

	public abstract List<JemsRole> findAllJemsRoles();

	public abstract List<JemsRole> findJemsRoleEntries(int firstResult,
			int maxResults);

	public abstract void saveJemsRole(JemsRole jemsRole);

	public abstract JemsRole updateJemsRole(JemsRole jemsRole);

}
