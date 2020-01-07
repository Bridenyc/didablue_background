package com.dida.controller;

import com.aliyuncs.DefaultAcsClient;
import com.dida.dao.EditServiceDao;
import com.dida.dto.OrderDTO;
import com.dida.entity.EditService;
import com.dida.entity.VendingTransactionTemp;
import com.dida.impl.VendingMachineService;
import com.dida.impl.VendingTransactionService;
import com.dida.impl.VendingTransactionTempService;
import com.dida.param.VendingOrderParams;
import com.dida.param.VendingTransactionParams;
import com.dida.vo.VendingTransactionVO;
import entity.Result;
import entity.StatusCode;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import util.AliyunClientUtil;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author lqh
 * @since 2019-03-22
 */
@RestController
@RequestMapping("/dida/vendingTransaction")
public class VendingTransactionController {
    @Autowired
    private VendingTransactionService vendingTransactionService;
    @Autowired
    private EditServiceDao editServiceDao;
    @Autowired
    private VendingMachineService vendingMachineService;
    @Autowired
    private VendingTransactionTempService vendingMachineServiceTemp;

    /**
     * 生成EXCEL
     * @param params
     * @param response
     * @throws IOException
     */
    @RequestMapping("/export/vendingTransactionVO")
    public void export(@RequestBody VendingTransactionParams params, HttpServletResponse response) throws IOException {
        if (params == null) {
            throw new RuntimeException("搜索条件不能为空!");
        }
        List<VendingTransactionVO> allByExample = vendingTransactionService.findAllByExample(params);

        // 2. ApachePOI导出
        //a. 创建工作簿(导出07)
        Workbook workbook = new XSSFWorkbook();
        //b. 创建工作表
        Sheet sheet = workbook.createSheet("AisleId");
        //c. 创建第一行
        Row row = sheet.createRow(0);
        //d. 创建第一行的每一列
        String[] titles = {"ID", "交易编号", " 订单号","交易时间","卡号","金额","交易类型(0-现金,1-刷卡)"
                ,"投入硬币","投入纸币","找零","售货机号","货道号","产品名称","交易状态(0-失败,1-成功)","每页行数"
                ,"是否出货成功(0-失败,1-成功)"};
        for(int i=0; i<titles.length;i++){
            row.createCell(i).setCellValue(titles[i]);
        }
        //e.导出数据行
        int index = 1;
        for(VendingTransactionVO p : allByExample){
            // 创建行
            row = sheet.createRow(index++);
            if(p.getId()!=null) {
                row.createCell(0).setCellValue(p.getId());
            }
            if(p.getTraNum()!=null) {
                row.createCell(1).setCellValue(p.getTraNum());
            }
            if(p.getOrderNum()!=null) {
                row.createCell(2).setCellValue(p.getOrderNum());
            }
            if(p.getTraTime()!=null) {
                row.createCell(3).setCellValue(p.getTraTime());
            }
            if(p.getCardNum()!=null) {
                row.createCell(4).setCellValue(p.getCardNum());
            }
            if(p.getAccount()!=null) {
                row.createCell(5).setCellValue(p.getAccount().doubleValue());
            }
            if(p.getVendingId()!=null) {
                row.createCell(6).setCellValue(p.getVendingId());
            }
            if(p.getAisleId()!=null) {
                row.createCell(7).setCellValue(p.getAisleId());
            }
            if(p.getTraWay()!=null) {
                row.createCell(8).setCellValue(p.getTraWay());
            }
            if(p.getTraStatus()!=null) {
                row.createCell(9).setCellValue(p.getTraStatus());
            }
            if(p.getCoin()!=null) {
                row.createCell(10).setCellValue(p.getCoin().doubleValue());
            }
            if(p.getPaper()!=null) {
                row.createCell(11).setCellValue(p.getPaper().doubleValue());
            }
            if(p.getChange()!=null) {
                row.createCell(12).setCellValue(p.getChange().doubleValue());
            }
            if(p.getGoodsName()!=null) {
                row.createCell(13).setCellValue(p.getGoodsName());
            }
            if(p.getDeliver()!=null) {
                row.createCell(14).setCellValue(p.getDeliver());
            }
        }

        // 3. 下载
        response.setCharacterEncoding("UTF-8");
        String fileName = "订单信息.xlsx";
        fileName = URLEncoder.encode(fileName,"UTF-8");
        response.setHeader("content-disposition","attachment;fileName="+fileName);
        ServletOutputStream out = response.getOutputStream();
        // 导出
        workbook.write(out);
        workbook.close();

    }


    /**
     * 查询订单列表
     *
     * @return
     */
/*    @RequestMapping(value = "/findAll/{index}/{size}",method= RequestMethod.POST)
    public Result findAll(@RequestBody VendingTransactionParams params,@PathVariable int index,@PathVariable int size){
        if(params == null){
            throw new RuntimeException("搜索条件不能为空!");
        }
        PageRequest pageRequest = new PageRequest(index, size);
        return new Result(true, StatusCode.OK,"查询成功",vendingTransactionService.findAllByExample(params,pageRequest));
    }*/
    @RequestMapping(value = "/findAll", method = RequestMethod.POST)
    public Result findAll(@RequestBody VendingTransactionParams params) {
        if (params == null) {
            throw new RuntimeException("搜索条件不能为空!");
        }
        return new Result(true, StatusCode.OK, "查询成功", vendingTransactionService.findAllByExample(params));
    }





    /**
     * 生成订单二维码
     */
    @RequestMapping(value = "/createOrder",method = RequestMethod.POST)
    public Result createOrder(@RequestBody VendingOrderParams params) throws Exception {
        Date date = new Date();
        OrderDTO order = vendingTransactionService.createOrder(params,date);
        String outTradeNo = order.getOutTradeNo();
        Double total = order.getTotalFee() * 100;
        String fee = total.toString();
        String totalFee = fee.substring(0, fee.length() - 2);

        Map<String, String> map = vendingTransactionService.genPayCode(outTradeNo, totalFee,date);
        String codeUrl = map.get("codeUrl");

        EditService editService = editServiceDao.getService("test1");
        DefaultAcsClient client = AliyunClientUtil.aliyunClient(editService.getAccessKeyID(), editService.getAccessKeySecret(), editService.getRegionId(), editService.getProductCode(), editService.getDomain());
        vendingMachineService.pub(client,"a1yd7xem0UP","/sys/a1yd7xem0UP/HB-VEND_001/thing/event/property/post",codeUrl);

        return new Result(true, StatusCode.OK, "生成成功",map);
    }



    /** 查询支付状态 */
    @RequestMapping(value = "/queryPayStatus",method = RequestMethod.GET)
    public String queryPayStatus() throws Exception {

        VendingTransactionTemp byVendingId = vendingMachineServiceTemp.findByVendingId("1");
        String orderNum = byVendingId.getOrderNum();

        //交易时间超时
        String data = "3";

        //交易时间
        String traTime = byVendingId.getTraTime();

        //五分钟后时间
        Date now = new Date();
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String nowTime = sd.format(now);
        Date parse = sd.parse(traTime);
        //两分钟内支付
        String rightTime = sd.format(new Date(parse.getTime() + (long)2 * 60 * 1000));

        if(sd.parse(nowTime).after(sd.parse(rightTime))){
            EditService editService = editServiceDao.getService("test1");
            DefaultAcsClient client = AliyunClientUtil.aliyunClient(editService.getAccessKeyID(), editService.getAccessKeySecret(), editService.getRegionId(), editService.getProductCode(), editService.getDomain());
            vendingMachineService.pub(client,"a1yd7xem0UP","/sys/a1yd7xem0UP/HB-VEND_001/thing/event/property/post",data);
            return data;
        }

        try{
            // 调用查询订单接口
            Map<String,String> resMap = vendingTransactionService.queryPayStatus(orderNum);
            if (resMap != null && resMap.size() > 0){
                // 判断是否支付成功
                if ("SUCCESS".equals(resMap.get("trade_state"))){
                    data = "1";
                }
                if ("NOTPAY".equals(resMap.get("trade_state"))){
                    data = "2";
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }

        EditService editService = editServiceDao.getService("test1");
        DefaultAcsClient client = AliyunClientUtil.aliyunClient(editService.getAccessKeyID(), editService.getAccessKeySecret(), editService.getRegionId(), editService.getProductCode(), editService.getDomain());
        vendingMachineService.pub(client,"a1yd7xem0UP","/sys/a1yd7xem0UP/HB-VEND_001/thing/event/property/post",data);

        return data;
    }

}
