package com.dida.controller;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.iot.model.v20180120.QueryDevicePropertyStatusRequest;
import com.aliyuncs.iot.model.v20180120.QueryDevicePropertyStatusResponse;
import com.dida.dao.EditServiceDao;
import com.dida.entity.EditService;
import com.dida.entity.VendingMachine;
import com.dida.impl.VendingMachineService;
import com.google.gson.Gson;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import util.AliyunClientUtil;
import util.LogUtil;

/**
 * <p>
 *  终端控制器
 * </p>
 */
@RestController
@RequestMapping("/dida/vendingMachine")
public class VendingMachineController {
    @Autowired
    private VendingMachineService vendingMachineService;
    @Autowired
    private EditServiceDao editServiceDao;

    /**
     * 添加终端
     * @param machine
     */
    @RequestMapping(value = "/addVending",method= RequestMethod.POST)
    public Result add(@RequestBody VendingMachine machine){
        vendingMachineService.add(machine);
        return new Result(true,StatusCode.OK,"增加成功");
    }


    /**
     * 查询终端列表
     * @return
     */
/*    @RequestMapping(value = "/findAll/{page}/{size}",method= RequestMethod.GET)
    public Result findAll(@PathVariable int page, @PathVariable int size){
        return new Result(true,StatusCode.OK,"查询成功",vendingMachineService.findAll(page, size));
    }*/
    @RequestMapping(value = "/findAll",method= RequestMethod.GET)
    public Result findAll(){
        return new Result(true,StatusCode.OK,"查询成功",vendingMachineService.findAll());
    }

    /**
     * 检查终端编号是否可用
     * @param id ID
     * @return
     */
    @RequestMapping(value="/checkId/{id}",method= RequestMethod.GET)
    public Boolean checkId(@PathVariable String id){
        VendingMachine vendingMachine = vendingMachineService.findById(id);
        if(vendingMachine == null){
            //设备编号未重复，可以使用
            return true;
        }
        //设备编号重复，不可以使用
        return false;
    }

    /**
     * 根据终端编号查询属性
     * @param id ID
     * @return
     */
    @RequestMapping(value="/findId/{id}",method= RequestMethod.GET)
    public VendingMachine findId(@PathVariable String id){
        VendingMachine vendingMachine = vendingMachineService.findById(id);
        if(vendingMachine == null){
            //设备编号未重复，可以使用
            throw new RuntimeException("终端设备不存在!");
        }
        //设备编号重复，不可以使用
        return vendingMachine;
    }

    /**
     * 修改设备属性
     * @param machine
     */
    @RequestMapping(value="/updVending",method= RequestMethod.PUT)
    public Result updVending(@RequestBody VendingMachine machine){
        if(machine.getId() == null){
            throw new RuntimeException("设备id不能为空!");
        }
        vendingMachineService.updVending(machine);
        return new Result(true,StatusCode.OK,"更新成功");
    }



    /**
     * 获取设备状态
     *
     */
    @RequestMapping("/getDeviceStatus")
    @ResponseBody
    public String getDeviceStatus(String openId,String deviceName,String productKey) throws Exception{

        EditService editService = editServiceDao.getService(openId);

        DefaultAcsClient client = AliyunClientUtil.aliyunClient(editService.getAccessKeyID(), editService.getAccessKeySecret(), editService.getRegionId(), editService.getProductCode(), editService.getDomain());

        QueryDevicePropertyStatusRequest request = new QueryDevicePropertyStatusRequest();
        request.setProductKey(productKey);
        request.setDeviceName(deviceName);
        QueryDevicePropertyStatusResponse response = (QueryDevicePropertyStatusResponse) vendingMachineService.executeTest(client, request);
        if (response != null && response.getSuccess() != false) {
            System.out.println(new Gson().toJson(response));
            return  new Gson().toJson(response);
        } else {
            LogUtil.error("查询设备属性失败！requestId:" + response.getRequestId() + "原因：" + response.getErrorMessage());
        }
        return null;
    }

    /**
     * Pub发送请求
     */
    @RequestMapping("/getPub")
    @ResponseBody
    public Result getPub(String openId,String deviceName,String productKey,String msg) throws Exception{
        EditService editService = editServiceDao.getService(openId);
        DefaultAcsClient client = AliyunClientUtil.aliyunClient(editService.getAccessKeyID(), editService.getAccessKeySecret(), editService.getRegionId(), editService.getProductCode(), editService.getDomain());
        vendingMachineService.pub(client,productKey,"/sys/"+productKey+"/"+deviceName+"/thing/event/property/post",msg);

        return new Result(true,StatusCode.OK,"发送成功");
    }
}
