package com.edcode.commerce.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.GenerationType;
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
 * @description 用户表(t_scacommerce_user)表实体类
 * @author eddie.lee
 * @date 2021-10-28 16:41:49
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "t_scacommerce_user")
@EntityListeners(AuditingEntityListener.class) // 作用：自动更新时间, 需要配合 @EnableJpaAuditing 使用
public class ScaCommerceUser implements Serializable {

	/**
	 * 自增主键
	 */
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 * 用户名
	 */
	@Column(name = "username")
	private String username;

	/**
	 * MD5 加密之后的密码
	 */
	@Column(name = "password")
	private String password;

	/**
	 * 额外的信息
	 */
	@Column(name = "extra_info")
	private String extraInfo;

	/**
	 * 创建时间
	 */
	@CreatedDate
	@Column(name = "create_time")
	private Date createTime;

	/**
	 * 更新时间
	 */
	@LastModifiedDate
	@Column(name = "update_time")
	private Date updateTime;

}
