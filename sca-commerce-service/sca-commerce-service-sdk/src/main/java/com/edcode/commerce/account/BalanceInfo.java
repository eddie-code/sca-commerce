package com.edcode.commerce.account;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author eddie.lee
 * @blog blog.eddilee.cn
 * @description 用户账户余额信息
 */
@ApiModel(description = "用户账户余额信息")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BalanceInfo {

    @ApiModelProperty(value = "用户主键 id")
    private Long userId;

    @ApiModelProperty(value = "用户账户余额")
    private Long balance;
}
