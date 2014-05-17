package com.dubaidrums.jems.repository;

import com.dubaidrums.jems.domain.JemsTax;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface JemsTaxRepository extends JpaSpecificationExecutor<JemsTax>, JpaRepository<JemsTax, Long> {
}
