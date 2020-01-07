package com.dida.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class DeviceDTO {
	private String deviceId;
	@JsonIgnore
	private String passwd;
	private String code;
	@JsonIgnore
	private String initPasswd;
	private String deviceName;
	private Integer power;
	private Timestamp createTime;
	private Timestamp updateTime;
	private Integer isInit;
	private Long productId;
	private Integer status;
	private Integer type;

}
