package com.dida.impl;

import com.dida.dao.VendingGoodsDao;
import com.dida.entity.VendingGoods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import util.IdWorker;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lqh
 * @since 2019-03-22
 */
@Service
public class VendingGoodsService {

    @Autowired
    private VendingGoodsDao vendingGoodsDao;
    @Autowired
    private IdWorker idWorker;

    /**
     * 查询设备列表，分页
     * @return
     */
/*    public Page<VendingGoods> findAll(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page-1, size);
        return vendingGoodsDao.findAll(pageRequest);
    }*/
    public List<VendingGoods> findAll() {
        return vendingGoodsDao.findAll();
    }

    /**
     * 根据产品名称查询
     * @return
     */
    public List<VendingGoods> findByGoodsName(String goodsName) {
        return vendingGoodsDao.findVendingGoodsByGoodsName(goodsName);
    }

    /**
     * 根据产品名称查询
     * @return
     */
    public void addGoods(VendingGoods goods) {
        if(goods == null){
            throw new RuntimeException("新增产品为空!");
        }
        Long id = idWorker.nextId();
        String s = id.toString();
        goods.setId(s);
        vendingGoodsDao.saveAndFlush(goods);
    }

    /**
     * 根据产品id修改
     * @return
     */
    public void updGoods(VendingGoods goods) {
        VendingGoods vendingGoodsById = vendingGoodsDao.findVendingGoodsById(goods.getId());
        if(goods.getGoodsName() != null) {
            vendingGoodsById.setGoodsName(goods.getGoodsName());
        }
        if(goods.getPic() != null) {
            vendingGoodsById.setPic(goods.getPic());
        }
        if(goods.getGoodsPrice() != null){
            vendingGoodsById.setGoodsPrice(goods.getGoodsPrice());
        }
        if(goods.getDetail() != null) {
            vendingGoodsById.setDetail(goods.getDetail());
        }
        vendingGoodsDao.saveAndFlush(goods);
    }

    /**
     * 根据产品名称查询
     * @return
     */
    @Transactional
    public void delGoods(String id) {
        vendingGoodsDao.deleteVendingGoodsById(id);
    }
}
