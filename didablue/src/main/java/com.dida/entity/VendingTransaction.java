package com.dida.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 		实体类
 * </p>
 */
//@Data , lombok插件注解，运用后无需写get/set方法，还有@ToString方法
@Data
@Entity
@Table(name="vending_transaction")
public class VendingTransaction implements Serializable  {

    @Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
    /**
     * 交易编号
     */
	@Column(name = "tra_num")
	private String traNum;
    /**
     * 订单号
     */
	@Column(name = "order_num")
	private String orderNum;
    /**
     * 交易时间
     */
	@Column(name = "tra_time")
	private String traTime;
    /**
     * 卡号
     */
	@Column(name = "card_num")
	private String cardNum;
    /**
     * 金额
     */
	private Double account;
    /**
     * 售货机号
     */
	@Column(name = "vending_id")
	private String vendingId;
    /**
     * 货道号
     */
	@Column(name = "aisle_id")
	private String aisleId;
    /**
     * 每页行数
     */
	@Column(name = "page_row")
	private Integer pageRow;
    /**
     * 交易类型(0-现金,1-刷卡)
     */
	@Column(name = "tra_way")
	private String traWay;
    /**
     * 交易状态(0-失败,1-成功)
     */
	@Column(name = "tra_status")
	private String traStatus;

	/**
	 * 投入硬币
	 */
	private BigDecimal coin;
	/**
	 * 投入纸币
	 */
	private BigDecimal paper;
	/**
	 * 找零
	 */
	@Column(name = "change_coin")
	private BigDecimal changeCoin;
	/**
	 * 产品名称
	 */
	@Column(name = "goods_name")
	private String goodsName;
	/**
	 * 是否出货成功(0-失败,1-成功)
	 */
	private String deliver;
	/**
	 * 总数
	 */
	private Integer total;

	/**
	 * 当前页
	 */
	private Integer page;

	/**
	 * 总页数
	 */
	private Integer size;
}
