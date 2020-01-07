package com.dida.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

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
@Table(name="vending_transaction_temp")
public class VendingTransactionTemp implements Serializable  {

    @Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
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
     * 售货机号
     */
	@Column(name = "vending_id")
	private String vendingId;
}
