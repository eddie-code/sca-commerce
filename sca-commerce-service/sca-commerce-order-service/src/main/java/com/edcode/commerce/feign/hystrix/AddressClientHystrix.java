package com.edcode.commerce.feign.hystrix;

import com.alibaba.fastjson.JSON;
import com.edcode.commerce.account.AddressInfo;
import com.edcode.commerce.common.TableId;
import com.edcode.commerce.feign.AddressClient;
import com.edcode.commerce.vo.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;

/**
 * @author eddie.lee
 * @blog blog.eddilee.cn
 * @description 账户服务熔断降级兜底策略
 */
@Slf4j
@Component
public class AddressClientHystrix implements AddressClient {

	@Override
	public CommonResponse<AddressInfo> getAddressInfoByTablesId(TableId tableId) {
		log.error("[订单服务中的帐户客户端外部请求错误] get address info" + "error: [{}]", JSON.toJSONString(tableId));
		return new CommonResponse<>(-1, "[订单服务中的帐户客户端外部请求错误]", new AddressInfo(-1L, Collections.emptyList()));
	}
}