package com.edcode.commerce.converter;

import com.edcode.commerce.constant.GoodsCategory;

import javax.persistence.AttributeConverter;

/**
 * @author eddie.lee
 * @blog blog.eddilee.cn
 * @description 商品类别枚举属性转换器
 */
public class GoodsCategoryConverter implements AttributeConverter<GoodsCategory, String> {

	@Override
	public String convertToDatabaseColumn(GoodsCategory goodsCategory) {
		return goodsCategory.getCode();
	}

	@Override
	public GoodsCategory convertToEntityAttribute(String code) {
		return GoodsCategory.of(code);
	}
}
