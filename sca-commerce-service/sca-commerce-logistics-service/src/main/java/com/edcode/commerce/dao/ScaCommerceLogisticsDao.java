package com.edcode.commerce.dao;

import com.edcode.commerce.entity.ScaCommerceLogistics;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author eddie.lee
 * @description ScaCommerceLogistics Dao 接口定义
 */
public interface ScaCommerceLogisticsDao extends JpaRepository<ScaCommerceLogistics, Long> {
}
