package com.dida.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class DeviceUserDto {
	private String id;
	private String nickName;
	private String avatarUrl;
	private Timestamp event_time;// 最后一次绑定/解绑时间


}
