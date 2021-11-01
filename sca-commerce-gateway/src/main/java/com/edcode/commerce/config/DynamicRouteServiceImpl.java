package com.edcode.commerce.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author eddie.lee
 * @blog blog.eddilee.cn
 * @description 事件推送 Aware：动态更新路由网关 Service
 */
@Slf4j
@Service
@SuppressWarnings("all")
@RequiredArgsConstructor
public class DynamicRouteServiceImpl implements ApplicationEventPublisherAware {

	/**
	 * 写路由定义
	 */
	private final RouteDefinitionWriter routeDefinitionWriter;

	/**
	 * 获取路由定义
	 */
	private final RouteDefinitionLocator routeDefinitionLocator;

	/**
	 * 事件发布
	 */
	private ApplicationEventPublisher publisher;

	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		// 完成事件推送句柄的初始化
		this.publisher = applicationEventPublisher;
	}

	/**
	 * 增加路由定义
	 */
	public String addRouteDefinition(RouteDefinition definition) {

		log.info("网关添加路由: [{}]", definition);

		// 保存路由配置并发布
		routeDefinitionWriter.save(Mono.just(definition)).subscribe();
		// 发布事件通知给 Gateway, 同步新增的路由定义
		this.publisher.publishEvent(new RefreshRoutesEvent(this));

		return "成功";
	}

	/**
	 * 更新路由
	 */
	public String updateList(List<RouteDefinition> definitions) {

		log.info("网关更新路由: [{}]", definitions);

		// 先拿到当前 Gateway 中存储的路由定义
		List<RouteDefinition> routeDefinitionsExits = routeDefinitionLocator.getRouteDefinitions().buffer().blockFirst();
		if (!CollectionUtils.isEmpty(routeDefinitionsExits)) {
			// 清除掉之前所有的 "旧的" 路由定义
			routeDefinitionsExits.forEach(rd -> {
				log.info("删除路由定义: [{}]", rd);
				deleteById(rd.getId());
			});
		}

		// 把更新的路由定义同步到 gateway 中
		definitions.forEach(definition -> updateByRouteDefinition(definition));
		return "成功";
	}

	/**
	 * 根据路由 id 删除路由配置
	 */
	private String deleteById(String id) {

		try {
			log.info("网关删除路由id: [{}]", id);
			this.routeDefinitionWriter.delete(Mono.just(id)).subscribe();
			// 发布事件通知给 gateway 更新路由定义
			this.publisher.publishEvent(new RefreshRoutesEvent(this));
			return "删除路由成功";
		} catch (Exception ex) {
			log.error("网关删除路由失败: [{}]", ex.getMessage(), ex);
			return "删除路由失败";
		}
	}

	/**
	 * 更新路由
	 * 更新的实现策略比较简单: 删除 + 新增 = 更新
	 */
	private String updateByRouteDefinition(RouteDefinition definition) {

		try {
			log.info("网关更新路由: [{}]", definition);
			this.routeDefinitionWriter.delete(Mono.just(definition.getId()));
		} catch (Exception ex) {
			return "更新失败，找不到路由ID: " + definition.getId();
		}

		try {
			this.routeDefinitionWriter.save(Mono.just(definition)).subscribe();
			this.publisher.publishEvent(new RefreshRoutesEvent(this));
			return "更新路由成功";
		} catch (Exception ex) {
			return "更新路由失败";
		}
	}

}
