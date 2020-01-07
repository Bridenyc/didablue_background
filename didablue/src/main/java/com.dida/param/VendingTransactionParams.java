package com.dida.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@ApiModel(value = "订单详情")
@Data
public class VendingTransactionParams {

    //private PageRequest pageRequest;

    @ApiModelProperty(value = "订单编号")
    private String orderNum;

    @ApiModelProperty(value = "交易查询开始时间")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startDate;

    @ApiModelProperty(value = "交易查询结束时间")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endDate;

    @ApiModelProperty(value = "卡号")
    private String cardNum;

    @ApiModelProperty(value = "售货机号")
    private String vendingId;

    @ApiModelProperty(value = "货道编号")
    private String aisleId;

    @ApiModelProperty(value = "交易类型")
    private String traWay;

    @ApiModelProperty(value = "是否成功")
    private String deliver;
}
