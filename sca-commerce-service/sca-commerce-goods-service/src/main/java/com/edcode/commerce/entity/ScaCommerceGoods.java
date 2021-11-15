package com.edcode.commerce.entity;

import java.util.Date;
import javax.persistence.*;

import com.edcode.commerce.converter.BrandCategoryConverter;
import com.edcode.commerce.converter.GoodsCategoryConverter;
import com.edcode.commerce.converter.GoodsStatusConverter;
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
	private String goodsCategory;

	/**
	 * 品牌分类
	 */
	@Column(name = "brand_category", nullable = false)
	// JPA有的，其实就是自动拆装箱的操作，可以转换类型存入DB
	@Convert(converter = BrandCategoryConverter.class)
	private String brandCategory;

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
	private Integer goodsStatus;

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

}
