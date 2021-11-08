package com.edcode.commerce.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.GenerationType;

import com.edcode.commerce.account.AddressInfo;
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
 * @description 用户地址表(t_scacommerce_address)表实体类
 * @author eddie.lee
 * @date 2021-11-04 12:44:32
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "t_scacommerce_address")
@EntityListeners(AuditingEntityListener.class) // 作用：自动更新时间, 需要配合 @EnableJpaAuditing 使用
public class ScaCommerceAddress implements Serializable {

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
	 * 用户名
	 */
	@Column(name = "username", nullable = false)
	private String username;

	/**
	 * 电话号码
	 */
	@Column(name = "phone", nullable = false)
	private String phone;

	/**
	 * 省
	 */
	@Column(name = "province", nullable = false)
	private String province;

	/**
	 * 市
	 */
	@Column(name = "city", nullable = false)
	private String city;

	/**
	 * 详细地址
	 */
	@Column(name = "address_detail", nullable = false)
	private String addressDetail;

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
	 * 根据 userId + AddressItem 得到 ScaCommerceAddress
	 */
	public static ScaCommerceAddress to(Long userId, AddressInfo.AddressItem addressItem) {
		ScaCommerceAddress scaCommerceAddress = new ScaCommerceAddress();
		scaCommerceAddress.setUserId(userId);
		scaCommerceAddress.setUsername(addressItem.getUsername());
		scaCommerceAddress.setPhone(addressItem.getPhone());
		scaCommerceAddress.setProvince(addressItem.getProvince());
		scaCommerceAddress.setCity(addressItem.getCity());
		scaCommerceAddress.setAddressDetail(addressItem.getAddressDetail());
		return scaCommerceAddress;
	}

	/**
	 * 将 ScaCommerceAddress 对象转成 AddressInfo
	 */
	public AddressInfo.AddressItem toAddressItem() {
		AddressInfo.AddressItem addressItem = new AddressInfo.AddressItem();
		addressItem.setId(this.id);
		addressItem.setUsername(this.username);
		addressItem.setPhone(this.phone);
		addressItem.setProvince(this.province);
		addressItem.setCity(this.city);
		addressItem.setAddressDetail(this.addressDetail);
		addressItem.setCreateTime(this.createTime);
		addressItem.setUpdateTime(this.updateTime);
		return addressItem;
	}

}
