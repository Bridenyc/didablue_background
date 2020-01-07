package com.dida.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name="edit_service")
public class EditService {

    @Id
    @Column(name = "open_id")
    private String openId;

    @Column(name = "access_key_id")
    private String accessKeyID;

    @Column(name = "access_key_secret")
    private String accessKeySecret;

    @Column(name = "region_id")
    private String regionId;

    @Column(name = "product_code")
    private String productCode;

    @Column(name = "domain")
    private String domain;
}
