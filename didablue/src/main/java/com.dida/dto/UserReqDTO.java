package com.dida.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class UserReqDTO {
	// 设备信息
	private String deviceId;
	private String deviceName;
	private Integer type;
	private String name;

	// 请求id
	private String bId;
	private Timestamp eventTime;// 请求时间

	// 用户信息
	private String uId;
	private String nickName;
	private String gender;
	private String avatarUrl;
	private String city;
	private String country;
	private String language;
	private String province;
	
}
