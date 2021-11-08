package com.edcode.commerce.service;

import com.alibaba.fastjson.JSON;
import com.edcode.commerce.account.AddressInfo;
import com.edcode.commerce.common.TableId;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;

/**
 * @author eddie.lee
 * @blog blog.eddilee.cn
 * @description 用户地址相关服务功能测试
 */
@Slf4j
public class AddressServiceTest extends BaseTest {

	@Autowired
	private IAddressService addressService;

	/**
	 * 测试创建用户地址信息
	 */
	@Test
	public void testCreateAddressInfo() {
		AddressInfo.AddressItem addressItem = new AddressInfo.AddressItem();
		addressItem.setUsername("EddieTest");
		addressItem.setPhone("12345678910");
		addressItem.setProvince("广东省");
		addressItem.setCity("广州市");
		addressItem.setAddressDetail("广州塔");

		log.info("测试创建地址信息: [{}]", JSON.toJSONString(addressService.createAddressInfo(
		        new AddressInfo(loginUserInfo.getId(), Collections.singletonList(addressItem))
        )));
	}

	/**
	 * 测试获取当前登录用户地址信息
	 */
	@Test
	public void testGetCurrentAddressInfo() {
		log.info("测试获取当前用户信息: [{}]", JSON.toJSONString(
				addressService.getCurrentAddressInfo()
		));
	}

	/**
	 * 测试通过 id 获取用户地址信息
	 */
	@Test
	public void testGetAddressInfoById() {

		log.info("测试按id获取地址信息: [{}]", JSON.toJSONString(
				addressService.getAddressInfoById(1L)
		));
	}

	/**
	 * 测试通过 TableId 获取用户地址信息
	 */
	@Test
	public void testGetAddressInfoByTableId() {

		log.info("测试按表id获取地址信息: [{}]", JSON.toJSONString(
				addressService.getAddressInfoByTableId(
						new TableId(Collections.singletonList(new TableId.Id(1L)))
				)
		));
	}

}
