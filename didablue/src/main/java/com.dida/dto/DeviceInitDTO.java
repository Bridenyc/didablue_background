package com.dida.dto;

import lombok.Data;

@Data
public class DeviceInitDTO {

	private String deviceId;
	private String passwd;
	private String code;
	private String deviceName;
	private Integer isInit;
	private Integer type;

}
