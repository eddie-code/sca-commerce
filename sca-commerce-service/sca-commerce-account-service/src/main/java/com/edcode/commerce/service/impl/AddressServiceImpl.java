package com.edcode.commerce.service.impl;

import com.alibaba.fastjson.JSON;
import com.edcode.commerce.account.AddressInfo;
import com.edcode.commerce.common.TableId;
import com.edcode.commerce.dao.ScaCommerceAddressDao;
import com.edcode.commerce.entity.ScaCommerceAddress;
import com.edcode.commerce.filter.AccessContext;
import com.edcode.commerce.service.IAddressService;
import com.edcode.commerce.vo.LoginUserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author eddie.lee
 * @blog blog.eddilee.cn
 * @description 用户地址相关服务接口实现
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class AddressServiceImpl implements IAddressService {

    private final ScaCommerceAddressDao addressDao;

	/**
	 * 存储多个地址信息
	 */
	@Override
	public TableId createAddressInfo(AddressInfo addressInfo) {

        // 不能直接从参数中获取用户的 id 信息
		LoginUserInfo loginUserInfo = AccessContext.getLoginUserInfo();

        // jdk7写法：将传递的参数转换成实体对象
//		List<AddressInfo.AddressItem> addressItems = addressInfo.getAddressItems();
//		List<ScaCommerceAddress> scaCommerceAddresses = new ArrayList<>();
//
//		for (AddressInfo.AddressItem a : addressItems) {
//			scaCommerceAddresses.add(ScaCommerceAddress.to(loginUserInfo.getId(), a));
//		}

        // jdk8写法（将对象转换为其他对象）：将传递的参数转换成实体对象
        List<ScaCommerceAddress> scaCommerceAddresses = addressInfo.getAddressItems().stream()
                .map(a -> ScaCommerceAddress.to(loginUserInfo.getId(), a))
                .collect(Collectors.toList());

        // 保存到数据表并把返回记录的 id 给调用方
        List<ScaCommerceAddress> savedRecords = addressDao.saveAll(scaCommerceAddresses);

        List<Long> ids = savedRecords.stream().map(ScaCommerceAddress::getId).collect(Collectors.toList());

        log.info("创建地址信息: [{}], [{}]", loginUserInfo.getId(), JSON.toJSONString(ids));

		return new TableId(
				// TODO TableId.Id::new
				ids.stream().map(TableId.Id::new).collect(Collectors.toList())
		);
	}

	@Override
	public AddressInfo getCurrentAddressInfo() {
        LoginUserInfo loginUserInfo = AccessContext.getLoginUserInfo();

        // 根据 userId 查询到用户的地址信息, 再实现转换
        List<ScaCommerceAddress> scaCommerceAddresses = addressDao.findAllByUserId(
                loginUserInfo.getId()
        );
        List<AddressInfo.AddressItem> addressItems = scaCommerceAddresses.stream()
//                .map(a -> a.toAddressItem())
                .map(ScaCommerceAddress::toAddressItem)
                .collect(Collectors.toList());

        return new AddressInfo(loginUserInfo.getId(), addressItems);
	}

	@Override
	public AddressInfo getAddressInfoById(Long id) {

		ScaCommerceAddress scaCommerceAddress = addressDao.findById(id).orElse(null);
		if (null == scaCommerceAddress) {
			throw new RuntimeException("地址不存在");
		}

		return new AddressInfo(
				scaCommerceAddress.getUserId(),
				Collections.singletonList(scaCommerceAddress.toAddressItem())
		);
	}

	@Override
	public AddressInfo getAddressInfoByTableId(TableId tableId) {

		List<Long> ids = tableId.getIds().stream()
//				.map(e -> e.getId()).collect(Collectors.toList());
				.map(TableId.Id::getId).collect(Collectors.toList());
		log.info("按表id获取地址信息: [{}]", JSON.toJSONString(ids));

		List<ScaCommerceAddress> scaCommerceAddresses = addressDao.findAllById(ids);
		if (CollectionUtils.isEmpty(scaCommerceAddresses)) {
			return new AddressInfo(-1L, Collections.emptyList());
		}

		List<AddressInfo.AddressItem> addressItems = scaCommerceAddresses.stream()
//				.map(s -> s.toAddressItem())
				.map(ScaCommerceAddress::toAddressItem)
				.collect(Collectors.toList());

		return new AddressInfo(
				scaCommerceAddresses.get(0).getUserId(), addressItems
		);
	}

}
