package com.dida.impl;

import com.dida.dao.VendingAisleDao;
import com.dida.entity.VendingAisle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author lqh
 * @since 2019-03-22
 */
@Service
public class VendingAisleService {

    @Autowired
    private VendingAisleDao vendingAisleDao;

    /**
     * 查看缺货列表
     */
    public List<VendingAisle> findAisleList() {
        return vendingAisleDao.findAll();
    }

    /**
     * 根据货道id查询货道列表
     *
     * @return
     */
    public List<VendingAisle> findByMacId(String macId) {
        return vendingAisleDao.findVendingAisleByMacId(macId);
    }

    /**
     * 根据终端编号/货道号修改货道数据
     */
    public void updateAisle(VendingAisle vendingAisle) {
        VendingAisle vendingAisleById = vendingAisleDao.findVendingAisleById(vendingAisle.getId());

        vendingAisleById.setId(vendingAisle.getId());
        if(vendingAisle.getMacId() != null){
            vendingAisleById.setMacId(vendingAisle.getMacId());
        }
        if(vendingAisle.getAisleId() != null){
            vendingAisleById.setAisleId(vendingAisle.getAisleId());
        }
        if(vendingAisle.getAisleName() != null){
            vendingAisleById.setAisleName(vendingAisle.getAisleName());
        }
        if(vendingAisle.getGoodsCount() != null){
            vendingAisleById.setGoodsCount(vendingAisle.getGoodsCount());
        }
        if(vendingAisle.getAisleCapacity() != null){
            vendingAisleById.setAisleCapacity(vendingAisle.getAisleCapacity());
        }
        if(vendingAisle.getPrice() != null){
            vendingAisleById.setPrice(vendingAisle.getPrice());
        }

        Date date = new Date();
        vendingAisleById.setAisUpdateTime(date);
        vendingAisleDao.saveAndFlush(vendingAisleById);
    }



    /**
     * 快捷上货
     */
    public void updCount(String macId) {
        List<VendingAisle> vendingAisleByMacId = vendingAisleDao.findVendingAisleByMacId(macId);
        for (VendingAisle vendingAisle : vendingAisleByMacId) {
            vendingAisle.setGoodsCount(vendingAisle.getAisleCapacity());
            vendingAisleDao.saveAndFlush(vendingAisle);
        }
    }

}
