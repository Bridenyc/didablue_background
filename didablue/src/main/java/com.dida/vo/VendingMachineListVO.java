package com.dida.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@ApiModel(value = "售货机列表VO")
@Data
public class VendingMachineListVO {

    /**
     * 终端编号
     */
    @ApiModelProperty(value = "终端编号")
    private String id;
    /**
     * 终端名称
     */
    @ApiModelProperty(value = "终端名称")
    private String deviceName;
    /**
     * 地址
     */
    @ApiModelProperty(value = "地址")
    private String address;
    /**
     * 实时状态
     */
    @ApiModelProperty(value = "实时状态")
    private String realTimeStatus;
    /**
     * 连接状态
     */
    @ApiModelProperty(value = "连接状态")
    private String connectStatus;
    /**
     * 允许交易
     */
    @ApiModelProperty(value = "允许交易")
    private String allowTrade;
    /**
     * GPRS信号
     */
    @ApiModelProperty(value = "GPRS信号")
    private String gprs;
    /**
     * 货道数量
     */
    @ApiModelProperty(value = "货道数量")
    private Integer account;
    /**
     * 售货机型号
     */
    @ApiModelProperty(value = "售货机型号")
    private String type;
    /**
     * 服务电话
     */
    @ApiModelProperty(value = "服务电话")
    private Long serviceCall;
    /**
     * 售货机经度
     */
    @ApiModelProperty(value = "售货机经度")
    private String longitude;
    /**
     * 售货机纬度
     */
    @ApiModelProperty(value = "售货机纬度")
    private String latitude;
    /**
     * 上线时间
     */
    @ApiModelProperty(value = "上线时间")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date launchTime;
    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date macUpdateTime;
    /**
     * 活动编号
     */
    @ApiModelProperty(value = "活动编号")
    private Integer activityId;
    /**
     * 温馨提示信息
     */
    @ApiModelProperty(value = "温馨提示信息")
    private String warmMessage;

}
