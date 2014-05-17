package com.dubaidrums.jems.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.dubaidrums.jems.domain.JemsClient;

@Repository
public interface JemsClientRepository  extends JpaSpecificationExecutor<JemsClient>, JpaRepository<JemsClient, Long> {
	List<JemsClient> findByActive(boolean active);

	JemsClient findJemsClientByCompanyIgnoreCase(String company);
}
