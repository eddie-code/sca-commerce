package com.edcode.commerce.dao;

import com.edcode.commerce.constant.BrandCategory;
import com.edcode.commerce.constant.GoodsCategory;
import com.edcode.commerce.entity.ScaCommerceGoods;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

/**
 * @author eddie.lee
 * @blog blog.eddilee.cn
 * @description ScaCommerceGoods Dao 接口定义
 *
 *              PagingAndSortingRepository 实现分页和排序的功能
 *
 */
public interface EcommerceGoodsDao extends PagingAndSortingRepository<ScaCommerceGoods, Long> {

	/**
	 * 根据查询条件查询商品表, 并限制返回结果
	 * 
	 * @param goodsCategory
	 * @param brandCategory
	 * @param goodsName
	 * @return
	 * 
	 *    select * from t_scacommerce_goods
     *      where goods_category = ? and brand_category = ? and goods_name = ?
     *    limit 1;
	 */
	Optional<ScaCommerceGoods> findFirst1ByGoodsCategoryAndBrandCategoryAndGoodsName(
	        GoodsCategory goodsCategory,
			BrandCategory brandCategory,
            String goodsName
    );
}
