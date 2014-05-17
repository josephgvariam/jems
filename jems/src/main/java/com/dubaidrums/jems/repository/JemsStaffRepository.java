package com.dubaidrums.jems.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.dubaidrums.jems.domain.JemsStaff;


@Repository
public interface JemsStaffRepository  extends JpaSpecificationExecutor<JemsStaff>, JpaRepository<JemsStaff, Long> {
	List<JemsStaff> findByActiveOrderByNameAsc(boolean active);

	JemsStaff findByNameIgnoreCase(String name);
}
