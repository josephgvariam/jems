package com.dubaidrums.jems.service;

import java.util.List;

import com.dubaidrums.jems.domain.JemsClient;
import com.dubaidrums.jems.domain.JemsEvent;

public interface JemsClientService {
	public abstract long countAllJemsClients();

	public abstract void deleteJemsClient(JemsClient jemsClient);

	public abstract JemsClient findJemsClient(Long id);

	public abstract List<JemsClient> findAllJemsClients();

	public abstract List<JemsClient> findJemsClientEntries(int firstResult,
			int maxResults);

	public abstract JemsClient findJemsClientByCompany(String company);

	public abstract void saveJemsClient(JemsClient jemsClient);

	public abstract JemsClient updateJemsClient(JemsClient jemsClient);

	public abstract List<JemsClient> findActiveJemsClients();

	public abstract void eventDeleted(JemsEvent jemsEvent);

	public abstract void eventSavedOrUpdated(JemsEvent jemsEvent);

	public JemsClient findJemsClientByEvent(JemsEvent e);
}
