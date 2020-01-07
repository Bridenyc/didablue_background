package com.dida.controller;

import com.dida.entity.VendingGoods;
import com.dida.entity.VendingMachine;
import com.dida.impl.VendingGoodsService;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lqh
 * @since 2019-03-22
 */
@RestController
@RequestMapping("/dida/vendingGoods")
public class VendingGoodsController {

    @Autowired
    private VendingGoodsService vendingGoodsService;

    /**
     * 查询产品列表
     * @return
     */
/*    @RequestMapping(value = "/findAll/{page}/{size}",method= RequestMethod.GET)
    public Result findAll(@PathVariable int page, @PathVariable int size){
        return new Result(true, StatusCode.OK,"查询成功",vendingGoodsService.findAll(page, size));
    }*/
    @RequestMapping(value = "/findAll",method= RequestMethod.GET)
    public Result findAll(){
        return new Result(true, StatusCode.OK,"查询成功",vendingGoodsService.findAll());
    }

    /**
     * 根据产品名称查询
     * @return
     */
    @RequestMapping(value = "/findByGoodsName",method= RequestMethod.GET)
    public List<VendingGoods> findByGoodsName(@RequestParam String goodsName){
        if(goodsName == null){
            throw new RuntimeException("查询条件为空!");
        }
        return vendingGoodsService.findByGoodsName(goodsName);
    }

    /**
     * 增加产品
     * @param goods
     */
    @RequestMapping(value="/addGoods",method= RequestMethod.POST)
    public Result addGoods(@RequestBody VendingGoods goods){
        vendingGoodsService.addGoods(goods);
        return new Result(true,StatusCode.OK,"新增成功");
    }

    /**
     * 根据产品id修改
     * @return
     */
    @RequestMapping(value = "/updGoods",method= RequestMethod.PUT)
    public Result updGoods(@RequestBody VendingGoods goods){
        if(goods.getId() == null){
            throw new RuntimeException("产品id为空!");
        }
        vendingGoodsService.updGoods(goods);
        return new Result(true,StatusCode.OK,"修改成功");
    }

    /**
     * 根据产品id删除
     * @return
     */
    @RequestMapping(value = "/delGoods/{id}",method= RequestMethod.PUT)
    public Result delGoods(@PathVariable String id){
        if(id == null){
            throw new RuntimeException("产品id为空!");
        }
        vendingGoodsService.delGoods(id);
        return new Result(true,StatusCode.OK,"删除成功");
    }

    /**
     * 检查产品名称是否可用
     * @param goodsName ID
     * @return
     */
    @RequestMapping(value="/checkId",method= RequestMethod.GET)
    public Boolean checkId(@RequestParam String goodsName){
        List<VendingGoods> vendingGoods = vendingGoodsService.findByGoodsName(goodsName);
        if(vendingGoods == null){
            //设备编号未重复，可以使用
            return true;
        }
        //设备编号重复，不可以使用
        return false;
    }
}
