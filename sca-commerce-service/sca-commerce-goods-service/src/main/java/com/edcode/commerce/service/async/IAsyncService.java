package com.edcode.commerce.service.async;

import com.edcode.commerce.goods.GoodsInfo;

import java.util.List;

/**
 * @author eddie.lee
 * @blog blog.eddilee.cn
 * @description 异步服务接口定义
 */
public interface IAsyncService {

	/**
	 * 异步将商品信息保存下来
	 * 
	 * @param goodsInfos
	 * @param taskId
	 */
	void asyncImportGoods(List<GoodsInfo> goodsInfos, String taskId);
}
