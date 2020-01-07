package com.dida.dto;

import lombok.Data;

@Data
public class OrderDTO {

    /**
     * 订单编号
     */
    private String outTradeNo;

    /**
     * 订单金额
     */
    private Double totalFee;

}
