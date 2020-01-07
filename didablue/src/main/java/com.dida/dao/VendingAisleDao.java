package com.dida.dao;

import com.dida.entity.VendingAisle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author lqh
 * @since 2019-03-22
 */
public interface VendingAisleDao extends JpaRepository<VendingAisle,String>,JpaSpecificationExecutor<VendingAisle> {

    List<VendingAisle> findVendingAisleByMacId(String macId);

    VendingAisle findVendingAisleById(String id);

}