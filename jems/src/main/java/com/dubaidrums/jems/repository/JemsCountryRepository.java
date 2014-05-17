package com.dubaidrums.jems.repository;

import java.util.List;

import com.dubaidrums.jems.domain.JemsCountry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface JemsCountryRepository extends JpaRepository<JemsCountry, Long>, JpaSpecificationExecutor<JemsCountry> {

	List<JemsCountry> findByActiveOrderByNameAsc(boolean active);
}
