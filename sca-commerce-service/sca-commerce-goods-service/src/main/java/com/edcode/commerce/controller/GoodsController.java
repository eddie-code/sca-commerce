package com.edcode.commerce.controller;

import com.edcode.commerce.common.TableId;
import com.edcode.commerce.goods.DeductGoodsInventory;
import com.edcode.commerce.goods.GoodsInfo;
import com.edcode.commerce.goods.SimpleGoodsInfo;
import com.edcode.commerce.service.IGoodsService;
import com.edcode.commerce.vo.PageSimpleGoodsInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author eddie.lee
 * @blog blog.eddilee.cn
 * @description 商品微服务对外暴露的功能服务 API 接口
 */
@Api(tags = "商品微服务功能接口")
@Slf4j
@RestController
@RequestMapping("/goods")
@RequiredArgsConstructor
public class GoodsController {

	private final IGoodsService goodsService;

	@ApiOperation(value = "详细商品信息", notes = "根据 TableId 查询详细商品信息", httpMethod = "POST")
	@PostMapping("/goods-info")
	public List<GoodsInfo> getGoodsInfoByTableId(@RequestBody TableId tableId) {
		return goodsService.getGoodsInfoByTableId(tableId);
	}

	@ApiOperation(value = "简单商品信息", notes = "获取分页的简单商品信息", httpMethod = "GET")
	@GetMapping("/page-simple-goods-info")
	public PageSimpleGoodsInfo getSimpleGoodsInfoByPage(@RequestParam(required = false, defaultValue = "1") int page) {
		return goodsService.getSimpleGoodsInfoByPage(page);
	}

	@ApiOperation(value = "简单商品信息", notes = "根据 TableId 查询简单商品信息", httpMethod = "POST")
	@PostMapping("/simple-goods-info")
	public List<SimpleGoodsInfo> getSimpleGoodsInfoByTableId(@RequestBody TableId tableId) {
		return goodsService.getSimpleGoodsInfoByTableId(tableId);
	}

	@ApiOperation(value = "扣减商品库存", notes = "扣减商品库存", httpMethod = "PUT")
	@PutMapping("/deduct-goods-inventory")
	public Boolean deductGoodsInventory(@RequestBody List<DeductGoodsInventory> deductGoodsInventories) {
		return goodsService.deductGoodsInventory(deductGoodsInventories);
	}
}
