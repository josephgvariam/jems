package com.dubaidrums.jems.repository;

import java.util.List;

import com.dubaidrums.jems.domain.JemsCostingItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface JemsCostingItemRepository extends JpaSpecificationExecutor<JemsCostingItem>, JpaRepository<JemsCostingItem, Long> {
	public List<JemsCostingItem> findByJemsEventId(Long id);
}
