package com.dida.dao;

import com.dida.entity.VendingTransaction;
import com.dida.entity.VendingTransactionTemp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author lqh
 * @since 2019-03-22
 */
public interface VendingTransactionDao extends JpaRepository<VendingTransaction,String>,JpaSpecificationExecutor<VendingTransaction> {

}