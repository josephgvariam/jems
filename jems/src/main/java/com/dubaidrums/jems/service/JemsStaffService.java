package com.dubaidrums.jems.service;

import java.util.List;

import com.dubaidrums.jems.domain.JemsStaff;

public interface JemsStaffService {
	public abstract long countAllJemsStaffs();

	public abstract void deleteJemsStaff(JemsStaff jemsStaff);

	public abstract JemsStaff findJemsStaff(Long id);

	public abstract List<JemsStaff> findAllJemsStaffs();

	public abstract List<JemsStaff> findJemsStaffEntries(int firstResult,
			int maxResults);

	public abstract void saveJemsStaff(JemsStaff jemsStaff);

	public abstract JemsStaff updateJemsStaff(JemsStaff jemsStaff);

	public abstract List<JemsStaff> findActiveJemsStaffs();
}
