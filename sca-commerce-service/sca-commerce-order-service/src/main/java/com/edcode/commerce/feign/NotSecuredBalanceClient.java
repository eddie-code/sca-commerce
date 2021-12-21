package com.edcode.commerce.feign;

import com.edcode.commerce.account.BalanceInfo;
import com.edcode.commerce.vo.CommonResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author eddie.lee
 * @blog blog.eddilee.cn
 * @description 用户账户服务 Feign 接口 （不做兜底）
 */
@FeignClient(contextId = "NotSecuredBalanceClient", value = "sca-commerce-account-service")
public interface NotSecuredBalanceClient {

	@RequestMapping(value = "/scacommerce-account-service/balance/deduct-balance", method = RequestMethod.PUT)
	CommonResponse<BalanceInfo> deductBalance(@RequestBody BalanceInfo balanceInfo);

}
