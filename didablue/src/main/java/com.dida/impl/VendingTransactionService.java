package com.dida.impl;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.Date;

import com.aliyuncs.DefaultAcsClient;
import com.dida.dao.VendingGoodsDao;
import com.dida.dao.VendingTransactionDao;
import com.dida.dao.VendingTransactionTempDao;
import com.dida.dto.OrderDTO;
import com.dida.dto.ProductDTO;
import com.dida.entity.EditService;
import com.dida.entity.VendingGoods;
import com.dida.entity.VendingTransaction;
import com.dida.entity.VendingTransactionTemp;
import com.dida.param.VendingOrderParams;
import com.dida.param.VendingTransactionParams;
import com.dida.vo.VendingTransactionVO;
import com.github.wxpay.sdk.WXPayUtil;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import util.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lqh
 * @since 2019-03-22
 */
@Service
public class VendingTransactionService {
    @Autowired
    private VendingGoodsDao vendingGoodsDao;

    @Autowired
    private VendingTransactionDao transactionDao;

    @Autowired
    private VendingTransactionTempDao transactionTempDao;

    @Autowired
    private IdWorker idWorker;

    /** 微信公众号:微信公众账号或开放平台APP的唯一标识 */
   private String appid = "wx937452aeebe916da";
    /** 商户账号 */
    private String partner = "1534214461";
    /** 商户密钥 */
    private String partnerkey = "wx937452aeebe916da15342144612019";
    /** 统一下单请求地址 */
    private String unifiedorder = "https://api.mch.weixin.qq.com/pay/unifiedorder";

    /**
     * 根据条件查询列表
     * @param params
     * @return
     */
    public List<VendingTransactionVO> findAllByExample(VendingTransactionParams params) {
        List<VendingTransaction> transaction = findVendingTransaction(params);
        return BeanUtils.copyList(transaction, VendingTransactionVO.class);
    }


    public List<VendingTransaction> findVendingTransaction(VendingTransactionParams params){
        return transactionDao.findAll(new Specification<VendingTransaction>() {

            @Override
            public Predicate toPredicate(Root<VendingTransaction> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                String paramsVendingId = params.getVendingId();
                String paramsAisleId = params.getAisleId();
                String[] split = paramsVendingId.split(",");
                String[] split2 = paramsAisleId.split(",");
                List<Predicate> predicateList = new ArrayList<Predicate>();

                if (Objects.nonNull(params.getOrderNum())) {
                    predicateList.add(cb.like(root.get("orderNum").as(String.class), "%" + params.getOrderNum() + "%"));
                }
                if (Objects.nonNull(params.getCardNum())) {
                    predicateList.add(cb.like(root.get("cardNum").as(String.class), "%" + params.getCardNum() + "%"));
                }

                //(a in (1,2) or b in (3,4))
                if (Objects.nonNull(params.getVendingId()) || Objects.nonNull(params.getAisleId())) {

                    CriteriaBuilder.In<String> in1 = cb.in(root.get("vendingId"));
                    for (String id : split) {
                        in1.value(id);
                    }

                    CriteriaBuilder.In<String> in2 = cb.in(root.get("aisleId"));
                    for (String id : split2) {
                        in2.value(id);
                    }

                    predicateList.add(cb.or(in1, in2));
                }

                if (Objects.nonNull(params.getTraWay())) {
                    predicateList.add(cb.equal(root.get("traWay").as(String.class), params.getTraWay()));
                }
                if (Objects.nonNull(params.getDeliver())) {
                    predicateList.add(cb.equal(root.get("deliver").as(String.class), params.getDeliver()));
                }
                if (Objects.nonNull(params.getStartDate()) && Objects.nonNull(params.getEndDate())) {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String param1 = simpleDateFormat.format(params.getStartDate());
                    String param2 = simpleDateFormat.format(params.getEndDate());

                    predicateList.add(cb.between(root.get("traTime").as(String.class), param1, param2));
                }
                return cb.and(predicateList.toArray(new Predicate[predicateList.size()]));
            }
        });
    }

    /**
     * 生成微信支付二维码
     * @param outTradeNo 订单交易号
     * @param totalFee 金额(分)
     * @return 集合
     */
    public Map<String, String> genPayCode(String outTradeNo, String totalFee,Date date){
        SimpleDateFormat sd = new SimpleDateFormat("yyyyMMddHHmmss");
        String format = sd.format(new Date(date.getTime() + (long) 30 * 60 * 1000));

        /** 创建Map集合封装请求参数 */
        Map<String, String> param = new HashMap<>();
        /** 公众号 */
        param.put("appid", appid);
        /** 商户号 */
        param.put("mch_id", partner);
        /** 随机字符串 */
        param.put("nonce_str", WXPayUtil.generateNonceStr());
        /** 商品描述 */
        param.put("body", "自动售货机");
        /** 商户订单交易号 */
        param.put("out_trade_no", outTradeNo);
        /** 交易结束时间 */
        param.put("time_expire", format);
        /** 总金额（分） */
        param.put("total_fee",totalFee);
        /** IP地址 */
        param.put("spbill_create_ip", "127.0.0.1");
        /** 回调地址(随意写) */
        param.put("notify_url", "http://www.baidu.com");
        /** 交易类型 */
        param.put("trade_type", "NATIVE");
        try {
            /** 根据商户密钥签名生成XML格式请求参数 */
            String xmlParam = WXPayUtil.generateSignedXml(param, partnerkey);
            System.out.println("请求参数：" + xmlParam);
            /** 创建HttpClientUtils对象发送请求 */
            HttpClientUtils client = new HttpClientUtils(true);
            /** 发送请求，得到响应数据 */
            String result = client.sendPost(unifiedorder, xmlParam);
            System.out.println("响应数据：" + result);
            /** 将响应数据XML格式转化成Map集合 */
            Map<String,String> resultMap = WXPayUtil.xmlToMap(result);
            /** 创建Map集合封装返回数据 */
            Map<String,String> data = new HashMap<>();
            /** 支付地址(二维码中的URL) */
            data.put("codeUrl", resultMap.get("code_url"));
            /** 总金额 */
            data.put("totalFee", totalFee);
            /** 订单交易号 */
            data.put("outTradeNo", outTradeNo);
            return data;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * 生成订单二维码
     * @param params
     * @return
     */
    public OrderDTO createOrder(VendingOrderParams params,Date date) {

        List<ProductDTO> productDTOS = params.getProductDTOS();
        Long traNum = idWorker.nextId();
        Long orderNum = idWorker.nextId();

        Double account = 0.00;
        String goodsName = "";

        for (int i = 0; i < productDTOS.size(); i++) {
            ProductDTO productDTO = productDTOS.get(i);
            if(productDTO.getId() == null){
                throw new RuntimeException("输入商品id为空!");
            }

            String id = productDTO.getId();
            Integer count = productDTO.getCount();

            VendingGoods vendingGoodsById = vendingGoodsDao.findVendingGoodsById(id);
            if(vendingGoodsById == null){
                throw new RuntimeException("商品id");
            }
            Double goodsPrice = vendingGoodsById.getGoodsPrice();
            if(goodsName.equals("")){
                goodsName = vendingGoodsById.getGoodsName();
            }else {
                goodsName += "," + vendingGoodsById.getGoodsName();
            }
            account += goodsPrice * count;
        }


        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String newTime = sd.format(date);


        VendingTransaction vendingTransaction = new VendingTransaction();
        vendingTransaction.setTraNum(traNum.toString());
        vendingTransaction.setOrderNum(orderNum.toString());
        vendingTransaction.setTraTime(newTime);
        vendingTransaction.setAccount(account);
        vendingTransaction.setVendingId(params.getVendingId());
        vendingTransaction.setAisleId(params.getAisleId());
        vendingTransaction.setGoodsName(goodsName);

        VendingTransactionTemp vendingTransactionTemp = new VendingTransactionTemp();
        vendingTransactionTemp.setOrderNum(orderNum.toString());
        vendingTransactionTemp.setTraTime(newTime);
        vendingTransactionTemp.setVendingId(params.getVendingId());

        VendingTransaction transaction = transactionDao.save(vendingTransaction);
        VendingTransactionTemp transactionTemp = transactionTempDao.save(vendingTransactionTemp);

        System.out.println(transaction);
        System.out.println(transactionTemp);

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setOutTradeNo(orderNum.toString());
        orderDTO.setTotalFee(account);

        return orderDTO;
    }


    /**
     * 查询支付状态
     * @param orderNum 订单交易号
     * @return 集合
     */
    public Map<String, String> queryPayStatus(String orderNum) {
        /** 创建Map集合封装请求参数 */
        Map<String, String> param = new HashMap<>(5);
        /** 公众号 */
        param.put("appid", appid);
        /** 商户号 */
        param.put("mch_id", partner);
        /** 订单交易号 */
        param.put("out_trade_no", orderNum);
        /** 随机字符串 */
        param.put("nonce_str", WXPayUtil.generateNonceStr());
        try {
            /** 根据商户密钥签名生成XML格式请求参数 */
            String xmlParam = WXPayUtil.generateSignedXml(param, partnerkey);
            System.out.println("请求参数：" + xmlParam);
            /** 创建HttpClientUtils对象发送请求 */
            HttpClientUtils client = new HttpClientUtils(true);
            /** 发送请求，得到响应数据 */
            String result = client.sendPost("https://api.mch.weixin.qq.com/pay/orderquery", xmlParam);
            System.out.println("响应数据：" + result);
            /** 将响应数据XML格式转化成Map集合 */
            return WXPayUtil.xmlToMap(result);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
