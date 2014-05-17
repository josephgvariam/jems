package com.dubaidrums.jems.service.impl;

import com.dubaidrums.jems.domain.JemsTax;
import com.dubaidrums.jems.repository.JemsTaxRepository;
import com.dubaidrums.jems.service.JemsTaxService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class JemsTaxServiceImpl implements JemsTaxService {

	@Autowired
    JemsTaxRepository jemsTaxRepository;

	public long countAllJemsTaxes() {
        return jemsTaxRepository.count();
    }

	public void deleteJemsTax(JemsTax jemsTax) {
        jemsTaxRepository.delete(jemsTax);
    }

	public JemsTax findJemsTax(Long id) {
        return jemsTaxRepository.findOne(id);
    }

	public List<JemsTax> findAllJemsTaxes() {
        return jemsTaxRepository.findAll();
    }

	public List<JemsTax> findJemsTaxEntries(int firstResult, int maxResults) {
        return jemsTaxRepository.findAll(new org.springframework.data.domain.PageRequest(firstResult / maxResults, maxResults)).getContent();
    }

	public void saveJemsTax(JemsTax jemsTax) {
        jemsTaxRepository.save(jemsTax);
    }

	public JemsTax updateJemsTax(JemsTax jemsTax) {
        return jemsTaxRepository.save(jemsTax);
    }
}
