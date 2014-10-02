package com.dubaidrums.jems.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.dubaidrums.jems.domain.JemsUser;

@Repository
public interface JemsUserRepository extends JpaSpecificationExecutor<JemsUser>,
		JpaRepository<JemsUser, Long> {
	JemsUser findByUserName(String username);

	List<JemsUser> findByEnabled(boolean enabled);

	JemsUser findByUserNameIgnoreCase(String username);
}
