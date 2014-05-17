package com.dubaidrums.jems.repository;

import com.dubaidrums.jems.domain.JemsRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface JemsRoleRepository extends JpaRepository<JemsRole, Long>, JpaSpecificationExecutor<JemsRole> {
	JemsRole findByRoleName(String rolename);
}
