package com.edcode.commerce.controller;

import com.edcode.commerce.account.AddressInfo;
import com.edcode.commerce.common.TableId;
import com.edcode.commerce.service.IAddressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author eddie.lee
 * @blog blog.eddilee.cn
 * @description 用户地址服务
 */
@Api(tags = "用户地址服务")
@Slf4j
@RestController
@RequestMapping("/address")
@RequiredArgsConstructor
public class AddressController {

	private final IAddressService addressService;

	/**
	 * value 是简述, notes 是详细的描述信息
	 */
	@ApiOperation(value = "创建", notes = "创建用户地址信息", httpMethod = "POST")
	@PostMapping("/create-address")
	public TableId createAddressInfo(@RequestBody AddressInfo addressInfo) {
		return addressService.createAddressInfo(addressInfo);
	}

	@ApiOperation(value = "当前用户", notes = "获取当前登录用户地址信息", httpMethod = "GET")
	@GetMapping("/current-address")
	public AddressInfo getCurrentAddressInfo() {
		return addressService.getCurrentAddressInfo();
	}

	@ApiOperation(value = "获取用户地址信息", notes = "通过 id 获取用户地址信息, id 是 ScaCommerceAddress 表的主键", httpMethod = "GET")
	@GetMapping("/address-info")
	public AddressInfo getAddressInfoById(@RequestParam Long id) {
		return addressService.getAddressInfoById(id);
	}

	@ApiOperation(value = "获取用户地址信息", notes = "通过 TableId 获取用户地址信息", httpMethod = "POST")
	@PostMapping("/address-info-by-table-id")
	public AddressInfo getAddressInfoByTablesId(@RequestBody TableId tableId) {
		return addressService.getAddressInfoByTableId(tableId);
	}
}
