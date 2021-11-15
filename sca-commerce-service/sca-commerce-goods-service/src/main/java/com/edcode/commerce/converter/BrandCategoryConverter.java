package com.edcode.commerce.converter;

import com.edcode.commerce.constant.BrandCategory;

import javax.persistence.AttributeConverter;

/**
 * @author eddie.lee
 * @blog blog.eddilee.cn
 * @description 品牌分类枚举属性转换器
 */
public class BrandCategoryConverter implements AttributeConverter<BrandCategory, String> {

	@Override
	public String convertToDatabaseColumn(BrandCategory brandCategory) {
		return brandCategory.getCode();
	}

	@Override
	public BrandCategory convertToEntityAttribute(String code) {
		return BrandCategory.of(code);
	}
}
