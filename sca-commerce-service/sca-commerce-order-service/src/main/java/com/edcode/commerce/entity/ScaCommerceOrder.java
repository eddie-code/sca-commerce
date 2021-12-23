package com.edcode.commerce.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.GenerationType;

import lombok.Builder;
import org.hibernate.annotations.GenericGenerator;
import java.io.Serializable;
import javax.persistence.EntityListeners;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

/**
 * @description 用户订单表(t_scacommerce_order)表实体类
 * @author eddie.lee
 * @date 2021-12-20 20:35:06
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "t_scacommerce_order")
@EntityListeners(AuditingEntityListener.class) // 作用：自动更新时间, 需要配合 @EnableJpaAuditing 使用
public class ScaCommerceOrder implements Serializable {

	/**
	 * 自增主键
	 */
	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 * 用户 id
	 */
	@Column(name = "user_id", nullable = false)
	private Long userId;

	/**
	 * 用户地址记录 id
	 */
	@Column(name = "address_id", nullable = false)
	private Long addressId;

	/**
	 * 订单详情(json 存储, goodsId, count)
	 */
	@Column(name = "order_detail", nullable = false)
	private String orderDetail;

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
