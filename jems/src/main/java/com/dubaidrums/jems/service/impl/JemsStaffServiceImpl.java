package com.dubaidrums.jems.service.impl;

import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dubaidrums.jems.domain.JemsStaff;
import com.dubaidrums.jems.repository.JemsStaffRepository;
import com.dubaidrums.jems.service.JemsStaffService;

@Service
@Transactional
public class JemsStaffServiceImpl implements JemsStaffService {

	Logger log = LogManager.getLogger(JemsStaffServiceImpl.class);

	@Autowired
	JemsStaffRepository jemsStaffRepository;

	public long countAllJemsStaffs() {
		return jemsStaffRepository.count();
	}

	public void deleteJemsStaff(JemsStaff jemsStaff) {
		jemsStaffRepository.delete(jemsStaff);
	}

	public JemsStaff findJemsStaff(Long id) {
		return jemsStaffRepository.findOne(id);
	}

	public List<JemsStaff> findAllJemsStaffs() {
		return jemsStaffRepository.findAll();
	}

	public List<JemsStaff> findJemsStaffEntries(int firstResult, int maxResults) {
		return jemsStaffRepository.findAll(
				new org.springframework.data.domain.PageRequest(firstResult
						/ maxResults, maxResults)).getContent();
	}

	public void saveJemsStaff(JemsStaff jemsStaff) {
		jemsStaffRepository.save(jemsStaff);
	}

	public JemsStaff updateJemsStaff(JemsStaff jemsStaff) {
		return jemsStaffRepository.save(jemsStaff);
	}

	public List<JemsStaff> findActiveJemsStaffs() {
		return jemsStaffRepository.findByActiveOrderByNameAsc(true);
	}

}
