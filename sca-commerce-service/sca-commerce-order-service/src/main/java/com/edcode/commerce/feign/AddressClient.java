package com.edcode.commerce.feign;

import com.edcode.commerce.account.AddressInfo;
import com.edcode.commerce.common.TableId;
import com.edcode.commerce.feign.hystrix.AddressClientHystrix;
import com.edcode.commerce.vo.CommonResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author eddie.lee
 * @blog blog.eddilee.cn
 * @description 用户账户服务 Feign 接口(安全的)
 */
@FeignClient(contextId = "AddressClient", value = "sca-commerce-account-service", fallback = AddressClientHystrix.class)
public interface AddressClient {

	/**
	 * 根据 id 查询地址信息
	 */
	@RequestMapping(value = "/scacommerce-account-service/address/address-info-by-table-id", method = RequestMethod.POST)
    CommonResponse<AddressInfo> getAddressInfoByTablesId(@RequestBody TableId tableId);
}