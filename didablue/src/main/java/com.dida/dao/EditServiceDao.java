package com.dida.dao;

import com.dida.entity.EditService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface EditServiceDao  extends JpaRepository<EditService,String>,JpaSpecificationExecutor<EditService> {

	@Query(value = "select * FROM edit_service where open_id = ?1 limit 1", nativeQuery = true)
	EditService getService(String openId);

}
