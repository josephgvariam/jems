package com.dubaidrums.jems.service.impl;

import com.dubaidrums.jems.domain.JemsCurrency;
import com.dubaidrums.jems.repository.JemsCurrencyRepository;
import com.dubaidrums.jems.service.JemsCurrencyService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class JemsCurrencyServiceImpl implements JemsCurrencyService {

	@Autowired
    JemsCurrencyRepository jemsCurrencyRepository;

	public long countAllJemsCurrencys() {
        return jemsCurrencyRepository.count();
    }

	public void deleteJemsCurrency(JemsCurrency jemsCurrency) {
        jemsCurrencyRepository.delete(jemsCurrency);
    }

	public JemsCurrency findJemsCurrency(Long id) {
        return jemsCurrencyRepository.findOne(id);
    }

	public List<JemsCurrency> findAllJemsCurrencys() {
        return jemsCurrencyRepository.findAll();
    }

	public List<JemsCurrency> findJemsCurrencyEntries(int firstResult, int maxResults) {
        return jemsCurrencyRepository.findAll(new org.springframework.data.domain.PageRequest(firstResult / maxResults, maxResults)).getContent();
    }

	public void saveJemsCurrency(JemsCurrency jemsCurrency) {
        jemsCurrencyRepository.save(jemsCurrency);
    }

	public JemsCurrency updateJemsCurrency(JemsCurrency jemsCurrency) {
        return jemsCurrencyRepository.save(jemsCurrency);
    }

	public List<JemsCurrency> findActiveJemsCurrencys() {
		return jemsCurrencyRepository.findByActive(true);
	}
}
