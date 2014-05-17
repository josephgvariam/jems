package com.dubaidrums.jems.service;

import com.dubaidrums.jems.domain.JemsOrganization;
import java.util.List;

public interface JemsOrganizationService {

	public abstract long countAllJemsOrganizations();


	public abstract void deleteJemsOrganization(JemsOrganization jemsOrganization);


	public abstract JemsOrganization findJemsOrganization(Long id);


	public abstract List<JemsOrganization> findAllJemsOrganizations();


	public abstract List<JemsOrganization> findJemsOrganizationEntries(int firstResult, int maxResults);


	public abstract void saveJemsOrganization(JemsOrganization jemsOrganization);


	public abstract JemsOrganization updateJemsOrganization(JemsOrganization jemsOrganization);


	public abstract List<JemsOrganization> findActiveJemsOrganizations();

}
