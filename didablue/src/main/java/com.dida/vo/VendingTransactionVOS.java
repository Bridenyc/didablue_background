package com.dida.vo;

import com.wordnik.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

@ApiModel(value = "订单列表VO")
@Data
public class VendingTransactionVOS {

    private List<VendingTransactionVO> vendingTransactionVO;

}
