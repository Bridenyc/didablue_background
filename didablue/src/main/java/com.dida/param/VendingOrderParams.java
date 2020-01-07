package com.dida.param;

import com.dida.dto.ProductDTO;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@ApiModel(value = "生成订单二维码")
@Data
public class VendingOrderParams {

    /**
     * 售货机号
     */
    @ApiModelProperty(value = "售货机号",required = true)
    @NotNull(message = "售货机号不能为空")
    private String vendingId;

    /**
     * 货道号
     */
    @ApiModelProperty(value = "货道号",required = true)
    @NotNull(message = "货道号不能为空")
    private String aisleId;

    /**
     * 购买商品列表
     */
    private List<ProductDTO> productDTOS;

}
