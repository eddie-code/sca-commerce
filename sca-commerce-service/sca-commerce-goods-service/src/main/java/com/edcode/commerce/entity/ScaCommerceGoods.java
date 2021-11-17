package com.edcode.commerce.entity;

import java.util.Date;
import javax.persistence.*;

import com.alibaba.fastjson.JSON;
import com.edcode.commerce.constant.BrandCategory;
import com.edcode.commerce.constant.GoodsCategory;
import com.edcode.commerce.constant.GoodsStatus;
import com.edcode.commerce.converter.BrandCategoryConverter;
import com.edcode.commerce.converter.GoodsCategoryConverter;
import com.edcode.commerce.converter.GoodsStatusConverter;
import com.edcode.commerce.goods.GoodsInfo;
import com.edcode.commerce.goods.SimpleGoodsInfo;
import org.hibernate.annotations.GenericGenerator;
import java.io.Serializable;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

/**
 * @description 商品表(t_scacommerce_goods)表实体类
 * @author eddie.lee
 * @date 2021-11-15 15:28:28
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "t_scacommerce_goods")
@EntityListeners(AuditingEntityListener.class) // 作用：自动更新时间, 需要配合 @EnableJpaAuditing 使用
public class ScaCommerceGoods implements Serializable {

	/**
	 * 自增主键
	 */
	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 * 商品类别
	 */
	@Column(name = "goods_category", nullable = false)
	// JPA有的，其实就是自动拆装箱的操作，可以转换类型存入DB
	@Convert(converter = GoodsCategoryConverter.class)
	private GoodsCategory goodsCategory;

	/**
	 * 品牌分类
	 */
	@Column(name = "brand_category", nullable = false)
	// JPA有的，其实就是自动拆装箱的操作，可以转换类型存入DB
	@Convert(converter = BrandCategoryConverter.class)
	private BrandCategory brandCategory;

	/**
	 * 商品名称
	 */
	@Column(name = "goods_name", nullable = false)
	private String goodsName;

	/**
	 * 商品图片
	 */
	@Column(name = "goods_pic", nullable = false)
	private String goodsPic;

	/**
	 * 商品描述信息
	 */
	@Column(name = "goods_description", nullable = false)
	private String goodsDescription;

	/**
	 * 商品状态
	 */
	@Column(name = "goods_status", nullable = false)
	// JPA有的，其实就是自动拆装箱的操作，可以转换类型存入DB
	@Convert(converter = GoodsStatusConverter.class)
	private GoodsStatus goodsStatus;

	/**
	 * 商品价格: 单位: 分、厘
	 */
	@Column(name = "price", nullable = false)
	private Integer price;

	/**
	 * 总供应量
	 */
	@Column(name = "supply", nullable = false)
	private Long supply;

	/**
	 * 库存
	 */
	@Column(name = "inventory", nullable = false)
	private Long inventory;

	/**
	 * 商品属性, json 字符串存储
	 */
	@Column(name = "goods_property", nullable = false)
	private String goodsProperty;

	/**
	 * 创建时间
	 */
	@CreatedDate
	@Column(name = "create_time", nullable = false)
	private Date createTime;

	/**
	 * 更新时间
	 */
	@LastModifiedDate
	@Column(name = "update_time", nullable = false)
	private Date updateTime;

	/**
	 * 将 GoodsInfo 转成实体对象
	 */
	public static ScaCommerceGoods to(GoodsInfo goodsInfo) {
		ScaCommerceGoods scaCommerceGoods = new ScaCommerceGoods();
		scaCommerceGoods.setGoodsCategory(GoodsCategory.of(goodsInfo.getGoodsCategory()));
		scaCommerceGoods.setBrandCategory(BrandCategory.of(goodsInfo.getBrandCategory()));
		scaCommerceGoods.setGoodsName(goodsInfo.getGoodsName());
		scaCommerceGoods.setGoodsPic(goodsInfo.getGoodsPic());
		scaCommerceGoods.setGoodsDescription(goodsInfo.getGoodsDescription());
		// TODO 可以增加一个审核的过程: 后续有空在搞
		scaCommerceGoods.setGoodsStatus(GoodsStatus.ONLINE);
		scaCommerceGoods.setPrice(goodsInfo.getPrice());
		// 总供应量、 库存 初始化时候应该一样
		scaCommerceGoods.setSupply(goodsInfo.getSupply());
		scaCommerceGoods.setInventory(goodsInfo.getSupply());
		scaCommerceGoods.setGoodsProperty(
				JSON.toJSONString(goodsInfo.getGoodsProperty())
		);

		return scaCommerceGoods;
	}

	/**
	 * 将实体对象转成 GoodsInfo 对象
	 */
	public GoodsInfo toGoodsInfo() {
		GoodsInfo goodsInfo = new GoodsInfo();
		goodsInfo.setId(this.id);
		goodsInfo.setGoodsCategory(this.goodsCategory.getCode());
		goodsInfo.setBrandCategory(this.brandCategory.getCode());
		goodsInfo.setGoodsName(this.goodsName);
		goodsInfo.setGoodsPic(this.goodsPic);
		goodsInfo.setGoodsDescription(this.goodsDescription);
		goodsInfo.setGoodsStatus(this.goodsStatus.getStatus());
		goodsInfo.setPrice(this.price);
		goodsInfo.setGoodsProperty(
				JSON.parseObject(this.goodsProperty, GoodsInfo.GoodsProperty.class)
		);
		goodsInfo.setSupply(this.supply);
		goodsInfo.setInventory(this.inventory);
		goodsInfo.setCreateTime(this.createTime);
		goodsInfo.setUpdateTime(this.updateTime);

		return goodsInfo;
	}

	/**
	 * <h2>将实体对象转成 SimpleGoodsInfo 对象</h2>
	 * */
	public SimpleGoodsInfo toSimple() {
		SimpleGoodsInfo goodsInfo = new SimpleGoodsInfo();
		goodsInfo.setId(this.id);
		goodsInfo.setGoodsName(this.goodsName);
		goodsInfo.setGoodsPic(this.goodsPic);
		goodsInfo.setPrice(this.price);
		return goodsInfo;
	}
}