package com.dida.dto;

import lombok.Data;

@Data
public class GetSignatureDTO {
	private String timestamp;
	private String noncestr;
	private String signature;
	private String appId;
	
}
