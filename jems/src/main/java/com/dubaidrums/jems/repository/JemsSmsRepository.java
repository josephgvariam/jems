package com.dubaidrums.jems.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.dubaidrums.jems.domain.JemsSms;

@Repository
public interface JemsSmsRepository extends JpaSpecificationExecutor<JemsSms>,
		JpaRepository<JemsSms, Long> {
}
