package com.dubaidrums.jems.service.impl;

import com.dubaidrums.jems.domain.JemsSms;
import com.dubaidrums.jems.repository.JemsSmsRepository;
import com.dubaidrums.jems.service.JemsSmsService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class JemsSmsServiceImpl implements JemsSmsService {

	@Autowired
    JemsSmsRepository jemsSmsRepository;

	public long countAllJemsSmses() {
        return jemsSmsRepository.count();
    }

	public void deleteJemsSms(JemsSms jemsSms) {
        jemsSmsRepository.delete(jemsSms);
    }

	public JemsSms findJemsSms(Long id) {
        return jemsSmsRepository.findOne(id);
    }

	public List<JemsSms> findAllJemsSmses() {
        return jemsSmsRepository.findAll();
    }

	public List<JemsSms> findJemsSmsEntries(int firstResult, int maxResults) {
        return jemsSmsRepository.findAll(new org.springframework.data.domain.PageRequest(firstResult / maxResults, maxResults)).getContent();
    }

	public void saveJemsSms(JemsSms jemsSms) {
        jemsSmsRepository.save(jemsSms);
    }

	public JemsSms updateJemsSms(JemsSms jemsSms) {
        return jemsSmsRepository.save(jemsSms);
    }
}
