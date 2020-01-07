package com.dida.dto;

import lombok.Data;

@Data
public class DeviceNameDTO {
	private String deviceId;
	private String deviceName;
	private Integer isInit;
	private Integer type;
	private String name;

	private Integer isBind;
	private Integer isAdmin;

}
