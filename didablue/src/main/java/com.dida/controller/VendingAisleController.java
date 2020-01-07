package com.dida.controller;

import com.dida.entity.VendingAisle;
import com.dida.impl.VendingAisleService;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  货道前端控制器
 * </p>
 *
 * @author lqh
 * @since 2019-03-22
 */
@RestController
@RequestMapping("/dida/vendingAisle")
public class VendingAisleController {

    @Autowired
    private VendingAisleService vendingAisleService;

    /**
     * 查看总货道列表
     */
    @RequestMapping(value = "/findAisleList",method = RequestMethod.GET)
    public Result findAisleList(){
        List<VendingAisle> aisleList = vendingAisleService.findAisleList();
        return new Result(true,StatusCode.OK,"查询成功",aisleList);
    }

    /**
     * 根据货道id查询货道列表
     * @return
     */
    @RequestMapping(value = "/findByMacId/{macId}",method= RequestMethod.GET)
    public Result findByMacId(@PathVariable String macId){
        return new Result(true, StatusCode.OK,"查询成功",vendingAisleService.findByMacId(macId));
    }

    /**
     * 根据终端编号/货道号修改货道数据
     * @return
     */
    @RequestMapping(value = "/updateAisle",method= RequestMethod.PUT)
    public Result updateAisle(@RequestBody VendingAisle vendingAisle){
        if(vendingAisle.getId() == null){
            throw new RuntimeException("id不能为空！");
        }
        vendingAisleService.updateAisle(vendingAisle);
        return new Result(true,StatusCode.OK,"修改成功");
    }

    /**
     * 快捷上货
     * @return
     */
    @RequestMapping(value = "/updCount/{macId}",method= RequestMethod.PUT)
    public Result updCount(@PathVariable String macId){
        if(macId == null){
            throw new RuntimeException("macId不能为空！");
        }
        vendingAisleService.updCount(macId);
        return new Result(true,StatusCode.OK,"修改成功");
    }




}
