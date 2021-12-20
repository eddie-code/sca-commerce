package com.edcode.commerce.dao;

import com.edcode.commerce.entity.ScaCommerceOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author eddie.lee
 * @blog blog.eddilee.cn
 * @description ScaCommerceOrder Dao 接口定义
 */
public interface ScaCommerceOrderDao extends PagingAndSortingRepository<ScaCommerceOrder, Long> {

	/**
	 * 根据 userId 查询分页订单
     * select * from t_ecommerce_order where user_id = ? order by ... desc/asc limit x offset y
	 */
	Page<ScaCommerceOrder> findAllByUserId(Long userId, Pageable pageable);
}