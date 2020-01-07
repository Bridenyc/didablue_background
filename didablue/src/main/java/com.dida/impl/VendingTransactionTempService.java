package com.dida.impl;

import com.dida.dao.VendingTransactionTempDao;
import com.dida.entity.VendingTransactionTemp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lqh
 * @since 2019-03-22
 */
@Service
public class VendingTransactionTempService {
    @Autowired
    private VendingTransactionTempDao vendingTransactionTempDao;

    public VendingTransactionTemp findByVendingId(String vendingId) {
        return vendingTransactionTempDao.findTransactionByVendingId(vendingId);
    }
}
