package com.dida.dao;

import com.dida.entity.VendingMachine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author lqh
 * @since 2019-03-22
 */
public interface VendingMachineDao extends JpaRepository<VendingMachine,String>,JpaSpecificationExecutor<VendingMachine> {

    VendingMachine findVendingMachineById(String id);

}