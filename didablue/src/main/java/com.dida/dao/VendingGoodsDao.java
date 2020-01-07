package com.dida.dao;

import com.dida.entity.VendingGoods;
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
public interface VendingGoodsDao extends JpaRepository<VendingGoods,String>,JpaSpecificationExecutor<VendingGoods> {

    List<VendingGoods> findVendingGoodsByGoodsName(String name);

    VendingGoods findVendingGoodsById(String id);

    void deleteVendingGoodsById(String id);

}