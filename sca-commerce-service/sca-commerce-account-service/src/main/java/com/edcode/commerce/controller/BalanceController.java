package com.edcode.commerce.controller;

import com.edcode.commerce.account.BalanceInfo;
import com.edcode.commerce.service.IBalanceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * @author eddie.lee
 * @blog blog.eddilee.cn
 * @description 用户余额服务
 */
@Api(tags = "用户余额服务")
@Slf4j
@RestController
@RequestMapping("/balance")
@RequiredArgsConstructor
public class BalanceController {

	private final IBalanceService balanceService;

	@ApiOperation(value = "当前用户", notes = "获取当前用户余额信息", httpMethod = "GET")
	@GetMapping("/current-balance")
	public BalanceInfo getCurrentUserBalanceInfo() {
		return balanceService.getCurrentUserBalanceInfo();
	}

	@ApiOperation(value = "扣减", notes = "扣减用于余额", httpMethod = "PUT")
	@PutMapping("/deduct-balance")
	public BalanceInfo deductBalance(@RequestBody BalanceInfo balanceInfo) {
		return balanceService.deductBalance(balanceInfo);
	}
}
