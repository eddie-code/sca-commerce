package com.edcode.commerce.feign.hystrix;

import com.alibaba.fastjson.JSON;
import com.edcode.commerce.common.TableId;
import com.edcode.commerce.feign.SecuredGoodsClient;
import com.edcode.commerce.goods.SimpleGoodsInfo;
import com.edcode.commerce.vo.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * @author eddie.lee
 * @blog blog.eddilee.cn
 * @description 商品服务熔断降级兜底
 */
@Slf4j
@Component
public class GoodsClientHystrix implements SecuredGoodsClient {

	@Override
	public CommonResponse<List<SimpleGoodsInfo>> getSimpleGoodsInfoByTableId(TableId tableId) {

		log.error("[订单服务中的商品客户端外部请求错误] get simple goods" + "错误: [{}]",
				JSON.toJSONString(tableId));
		return new CommonResponse<>(-1, "[订单服务中的商品客户端外部请求错误]", Collections.emptyList());
	}
}
