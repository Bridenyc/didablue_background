package com.dida.dto;

import lombok.Data;

@Data
public class GetServiceDTO {
    private String openid;
    private String accessKeyID;
    private String accessKeySecret;
    private String regionId;
    private String productCode;
    private String domain;
}
