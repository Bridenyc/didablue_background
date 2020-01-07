package com.dida.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 		实体类
 * </p>
 */
//@Data , lombok插件注解，运用后无需写get/set方法，还有@ToString方法
@Data
@Entity
@Table(name="vending_goods")
public class VendingGoods implements Serializable  {

    /**
     * 产品序号
     */
    @Id
	private String id;
    /**
     * 产品名称
     */
	@Column(name = "goods_name")
	private String goodsName;
    /**
     * 图片地址
     */
	private String pic;
    /**
     * 参考价格
     */
	@Column(name = "goods_price")
	private Double goodsPrice;
    /**
     * 详情
     */
	private String detail;

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
