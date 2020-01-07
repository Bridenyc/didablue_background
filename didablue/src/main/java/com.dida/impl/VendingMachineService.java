package com.dida.impl;

import com.aliyuncs.AcsResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.RpcAcsRequest;
import com.aliyuncs.iot.model.v20170420.PubRequest;
import com.aliyuncs.iot.model.v20170420.PubResponse;
import com.dida.dao.VendingMachineDao;
import com.dida.entity.VendingMachine;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import util.LogUtil;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lqh
 * @since 2019-03-22
 */
@Service
public class VendingMachineService {

    @Autowired
    private VendingMachineDao vendingMachineDao;

    /**
     * 增加
     * @param machine
     */
    public void add(VendingMachine machine) {
        Date date = new Date();
        machine.setLaunchTime(date);
        vendingMachineDao.saveAndFlush(machine);
    }


    /**
     * 查询设备VendingMachine列表
     * @return
     */
/*    public Page<VendingMachine> findAll(int page, int size) {
        PageRequest pageRequest =  PageRequest.of(page-1, size);
        return vendingMachineDao.findAll(pageRequest);
    }*/
    public List<VendingMachine> findAll() {
        return vendingMachineDao.findAll();
    }


    /**
     * 根据ID查询实体
     * @param id
     * @return
     */
    public VendingMachine findById(String id) {
        return vendingMachineDao.findVendingMachineById(id);
    }

    /**
     * 修改
     * @param machine
     */
    public void updVending(VendingMachine machine) {
        VendingMachine machineById = vendingMachineDao.findVendingMachineById(machine.getId());

        machineById.setId(machine.getId());
        if(machine.getDeviceName() != null) {
            machineById.setDeviceName(machine.getDeviceName());
        }
        if(machine.getAddress() != null){
            machineById.setAddress(machine.getAddress());
        }
        machineById.setAccount(machine.getAccount());
        machineById.setType(machine.getType());
        machineById.setServiceCall(machine.getServiceCall());
        machineById.setLongitude(machine.getLongitude());
        machineById.setLatitude(machine.getLatitude());
        machineById.setAllowTrade(machine.getAllowTrade());

        Date date = new Date();
        machineById.setMacUpdateTime(date);

        vendingMachineDao.saveAndFlush(machineById);
    }

    /**
     * 获取设备信息
     * @param client
     * @param request
     * @return
     */
    public AcsResponse executeTest(DefaultAcsClient client, RpcAcsRequest request) {
        AcsResponse response = null;
        try {
            response = client.getAcsResponse(request);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.print("执行失败：e:" + e.getMessage());
        }
        return response;
    }

    /**
     * pub消息
     *
     * @param productKey pk
     * @param topic      topic
     * @param msg        消息内容
     */
    public void pub(DefaultAcsClient client,String productKey, String topic, String msg) {
        PubRequest request = new PubRequest();
        request.setProductKey(productKey);
        request.setTopicFullName(topic);
        request.setMessageContent(Base64.encodeBase64String(msg.getBytes()));
        request.setQos(1);
        PubResponse response = (PubResponse) executeTest(client,request);
        if (response != null && response.getSuccess() != false) {
            LogUtil.print("发送消息成功！messageId：" + response.getMessageId());
        } else {
            LogUtil.error("发送消息失败！requestId:" + response.getRequestId() + "原因：" + response.getErrorMessage());
        }
    }
}
