package com.dubaidrums.jems.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.dubaidrums.jems.domain.JemsRole;

@Repository
public interface JemsRoleRepository extends JpaRepository<JemsRole, Long>,
		JpaSpecificationExecutor<JemsRole> {
	JemsRole findByRoleName(String rolename);
}
