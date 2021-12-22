package com.edcode.commerce.feign;

import com.edcode.commerce.common.TableId;
import com.edcode.commerce.feign.hystrix.GoodsClientHystrix;
import com.edcode.commerce.goods.SimpleGoodsInfo;
import com.edcode.commerce.vo.CommonResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @author eddie.lee
 * @blog blog.eddilee.cn
 * @description 安全的商品服务 Feign 接口
 */
@FeignClient(contextId = "SecuredGoodsClient", value = "e-commerce-goods-service", fallback = GoodsClientHystrix.class)
public interface SecuredGoodsClient {

	/**
	 * 根据 ids 查询简单的商品信息
	 */
	@RequestMapping(value = "/scacommerce-goods-service/goods/simple-goods-info", method = RequestMethod.POST)
	CommonResponse<List<SimpleGoodsInfo>> getSimpleGoodsInfoByTableId(@RequestBody TableId tableId);
}