package com.dubaidrums.jems.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.dubaidrums.jems.domain.JemsUser;

public interface JemsUserService extends UserDetailsService {
	public String encrypt(String text);

	public abstract long countAllJemsUsers();

	public abstract void deleteJemsUser(JemsUser jemsUser);

	public abstract JemsUser findJemsUser(Long id);

	public abstract List<JemsUser> findAllJemsUsers();

	public abstract List<JemsUser> findJemsUserEntries(int firstResult,
			int maxResults);

	public abstract void saveJemsUser(JemsUser jemsUser);

	public abstract JemsUser updateJemsUser(JemsUser jemsUser);

	public List<JemsUser> findEnabledJemsUsers();

	public JemsUser findJemsUserByUserName(String userName);

	public JemsUser findJemsUserByUserNameIgnoreCase(String userName);

}
