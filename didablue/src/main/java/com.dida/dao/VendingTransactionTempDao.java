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
public interface VendingTransactionTempDao extends JpaRepository<VendingTransactionTemp,String>,JpaSpecificationExecutor<VendingTransactionTemp> {

    @Query(value = "Select * from vending_transaction_temp where vending_id = ?1 order by tra_time desc limit 1",nativeQuery = true)
    VendingTransactionTemp findTransactionByVendingId(String vendingId);
}