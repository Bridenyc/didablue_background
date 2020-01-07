package com.dida.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@ApiModel(value = "订单列表VO")
@Data
public class VendingTransactionVO{

	@ApiModelProperty(value = "订单ID")
	private String id;

	@ApiModelProperty(value = "交易编号")
	private String traNum;

	@ApiModelProperty(value = "订单号")
	private String orderNum;

	@ApiModelProperty(value = "交易时间")
	@JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	private Date traTime;

	@ApiModelProperty(value = "卡号")
	private String cardNum;

	@ApiModelProperty(value = "金额")
	private BigDecimal account;

	@ApiModelProperty(value = "售货机号")
	private String vendingId;

	@ApiModelProperty(value = "货道号")
	private String aisleId;

	@ApiModelProperty(value = "每页行数")
	private Integer pageRow;

	@ApiModelProperty(value = "交易类型(0-现金,1-刷卡)")
	private String traWay;

	@ApiModelProperty(value = "交易状态(0-失败,1-成功)")
	private String traStatus;

	@ApiModelProperty(value = "投入硬币")
	private BigDecimal coin;

	@ApiModelProperty(value = "投入纸币")
	private BigDecimal paper;

	@ApiModelProperty(value = "找零")
	private BigDecimal change;

	@ApiModelProperty(value = "产品名称")
	private String goodsName;

	@ApiModelProperty(value = "是否出货成功(0-失败,1-成功)")
	private String deliver;

	@ApiModelProperty(value = "总数")
	private Integer total;

	@ApiModelProperty(value = "当前页")
	private Integer page;

	@ApiModelProperty(value = "总页数")
	private Integer size;
}
