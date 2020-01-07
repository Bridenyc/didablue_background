package com.dida.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;


/**
 * <p>
 * 		实体类
 * </p>
 */
//@Data , lombok插件注解，运用后无需写get/set方法，还有@ToString方法
@Data
@Entity
@Table(name="vending_machine")
public class VendingMachine implements Serializable  {

    /**
     * 终端编号
     */
    @Id
	private String id;
    /**
     * 终端名称
     */
	@Column(name = "device_name")
	private String deviceName;
    /**
     * 地址
     */
	private String address;
    /**
     * 实时状态
     */
	@Column(name = "real_time_status")
	private String realTimeStatus;
    /**
     * 连接状态
     */
	@Column(name = "connect_status")
	private String connectStatus;
    /**
     * 允许交易
     */
	@Column(name = "allow_trade")
	private String allowTrade;
    /**
     * GPRS信号
     */
	@Column(name = "GPRS")
	private String gprs;
    /**
     * 货道数量
     */
	private Integer account;
    /**
     * 售货机型号
     */
	private String type;
    /**
     * 服务电话
     */
	@Column(name = "service_call")
	private Long serviceCall;
    /**
     * 售货机经度
     */
	private String longitude;
    /**
     * 售货机纬度
     */
	private String latitude;
    /**
     * 上线时间
     */
	@Column(name = "launch_time")
	@JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	private Date launchTime;
    /**
     * 更新时间
     */
	@Column(name = "mac_update_time")
	@JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	private Date macUpdateTime;
    /**
     * 活动编号
     */
	@Column(name = "activity_id")
	private Integer activityId;
    /**
     * 温馨提示信息
     */
	@Column(name = "warm_message")
	private String warmMessage;

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
