package com.dida.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class UserDeviceDTO {
	// 设备信息
	private String deviceId;
	private String deviceName;
	private Integer type;
	private String name;

	// 设备绑定表id
	private String id;
	private Timestamp eventTime;

	// 用户信息
	private String nickName;
	private String gender;
	private String avatarUrl;
	private String city;
	private String country;
	private String language;
	private String province;
	
	//绑定状态
	private Integer status;

}
