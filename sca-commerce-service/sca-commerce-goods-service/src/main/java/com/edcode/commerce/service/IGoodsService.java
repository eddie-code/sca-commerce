package com.edcode.commerce.service;

import com.edcode.commerce.common.TableId;
import com.edcode.commerce.goods.DeductGoodsInventory;
import com.edcode.commerce.goods.GoodsInfo;
import com.edcode.commerce.goods.SimpleGoodsInfo;
import com.edcode.commerce.vo.PageSimpleGoodsInfo;

import java.util.List;

/**
 * @author eddie.lee
 * @blog blog.eddilee.cn
 * @description 商品微服务相关服务接口定义
 */
public interface IGoodsService {

	/**
	 * 根据 TableId 查询商品详细信息
	 * 
	 * @param tableId
	 * @return
	 */
	List<GoodsInfo> getGoodsInfoByTableId(TableId tableId); // GoodsInfo 是对外的 sdk 实体

	/**
	 * 获取分页的商品信息
	 * 
	 * @param page
	 * @return
	 */
	PageSimpleGoodsInfo getSimpleGoodsInfoByPage(int page);

	/**
	 * 根据 TableId 查询简单商品信息
	 * 
	 * @param tableId
	 * @return
	 */
	List<SimpleGoodsInfo> getSimpleGoodsInfoByTableId(TableId tableId);

	/**
	 * 扣减商品库存
	 * 
	 * @param deductGoodsInventories
	 * @return
	 */
	Boolean deductGoodsInventory(List<DeductGoodsInventory> deductGoodsInventories);
}
