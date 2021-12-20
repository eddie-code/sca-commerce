package com.edcode.commerce.service;

import com.edcode.commerce.common.TableId;
import com.edcode.commerce.order.OrderInfo;
import com.edcode.commerce.vo.PageSimpleOrderDetail;

/**
 * @author eddie.lee
 * @blog blog.eddilee.cn
 * @description 订单相关服务接口定义
 */
public interface IOrderService {

	/**
	 * 下单(分布式事务): 创建订单 -> 扣减库存 -> 扣减余额 -> 创建物流信息(Stream + Kafka)
	 * 
	 * @param orderInfo
	 * @return
	 */
	TableId createOrder(OrderInfo orderInfo);

	/**
	 * 获取当前用户的订单信息: 带有分页
	 * 
	 * @param page
	 * @return
	 */
	PageSimpleOrderDetail getSimpleOrderDetailByPage(int page);
}
