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
@Table(name="vending_aisle")
public class VendingAisle implements Serializable {

	/**
     * 主键id
     */
    @Id
	private String id;
	/**
	 * 终端编号
	 */
	@Column(name = "mac_id")
	private String macId;
	/**
     * 货道号
     */
	@Column(name = "aisle_id")
	private String aisleId;
	/**
	 * 货道名称
	 */
	@Column(name = "aisle_name")
	private String aisleName;
	/**
     * 货物数量
     */
	@Column(name = "goods_count")
	private Integer goodsCount;
    /**
     * 货道容量
     */
	@Column(name = "aisle_capacity")
	private Integer aisleCapacity;
    /**
     * 价格
     */
	private BigDecimal price;
    /**
     * 更新时间
     */
	@Column(name = "ais_update_time")
	@JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	private Date aisUpdateTime;
    /**
     * 故障(0-有,1-无)
     */
	private String fault;
    /**
     * 故障信息
     */
	@Column(name = "fault_massage")
	private String faultMassage;
    /**
     * 故障时间
     */
	@Column(name = "fault_time")
	@JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	private Date faultTime;

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
