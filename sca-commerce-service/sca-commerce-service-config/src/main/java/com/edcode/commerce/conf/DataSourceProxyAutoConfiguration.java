package com.edcode.commerce.conf;

import com.zaxxer.hikari.HikariDataSource;
import io.seata.rm.datasource.DataSourceProxy;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * @author eddie.lee
 * @blog blog.eddilee.cn
 * @description Seata 所需要的数据源代理配置类
 */
@Configuration
@RequiredArgsConstructor
public class DataSourceProxyAutoConfiguration {

	private final DataSourceProperties dataSourceProperties;

	/**
	 * 配置数据源代理, 用于 Seata 全局事务回滚
	 * 
	 * before image + after image -> undo_log
	 */
	@Primary
	@Bean("dataSource")
	public DataSource dataSource() {
		HikariDataSource dataSource = new HikariDataSource();
		dataSource.setJdbcUrl(dataSourceProperties.getUrl());
		dataSource.setUsername(dataSourceProperties.getUsername());
		dataSource.setPassword(dataSourceProperties.getPassword());
		dataSource.setDriverClassName(dataSourceProperties.getDriverClassName());
		return new DataSourceProxy(dataSource);
	}
}
