package com.edcode.commerce.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executor;

/**
 * @author eddie.lee
 * @blog blog.eddilee.cn
 * @description 通过 nacos 下发动态路由配置, 监听 Nacos 中路由配置变更
 */
@Slf4j
@Component
// @DependsOn 在另外一个 Bean 初始化之后，在初始化当前 Bean
@DependsOn({"gatewayConfig"})
@RequiredArgsConstructor
public class DynamicRouteServiceImplByNacos {

    /**
     * Nacos 配置服务
     */
    private ConfigService configService;

    private final DynamicRouteServiceImpl dynamicRouteService;

    /**
     * Bean 在容器中构造完成之后会执行 init 方法
     */
    @PostConstruct
    public void init() {
        log.info("初始化网关路由....");

        try {
            // 初始化 Nacos 配置客户端
            configService = initConfigService();
            if (null == configService) {
                log.error("初始化配置服务失败");
                return;
            }

            // 通过 Nacos Config 并指定路由配置路径去获取路由配置
            String configInfo = configService.getConfig(
                    GatewayConfig.NACOS_ROUTE_DATA_ID,
                    GatewayConfig.NACOS_ROUTE_GROUP,
                    GatewayConfig.DEFAULT_TIMEOUT
            );

            log.info("获取当前网关配置: [{}]", configInfo);
            List<RouteDefinition> definitionList = JSON.parseArray(configInfo, RouteDefinition.class);

            if (CollectionUtils.isNotEmpty(definitionList)) {
                for (RouteDefinition definition : definitionList) {
                    log.info("初始化网关配置: [{}]", definition.toString());
                    dynamicRouteService.addRouteDefinition(definition);
                }
            }

        } catch (Exception ex) {
            log.error("网关路由初始化有一些错误: [{}]", ex.getMessage(), ex);
        }

        // 设置监听器
        dynamicRouteByNacosListener(GatewayConfig.NACOS_ROUTE_DATA_ID, GatewayConfig.NACOS_ROUTE_GROUP);
    }
    /**
     * 初始化 Nacos Config
     */
    private ConfigService initConfigService() {

        try {
            Properties properties = new Properties();
            properties.setProperty("serverAddr", GatewayConfig.NACOS_SERVER_ADDR);
            properties.setProperty("namespace", GatewayConfig.NACOS_NAMESPACE);
            return configService = NacosFactory.createConfigService(properties);
        } catch (Exception ex) {
            log.error("初始化网关nacos配置错误: [{}]", ex.getMessage(), ex);
            return null;
        }
    }

    /**
     * 监听 Nacos 下发的动态路由配置
     */
    private void dynamicRouteByNacosListener(String dataId, String group) {

        try {
            // 给 Nacos Config 客户端增加一个监听器
            configService.addListener(dataId, group, new Listener() {

                /**
                 * 自己提供线程池执行操作
                 * */
                @Override
                public Executor getExecutor() {
                    return null;
                }

                /**
                 * 监听器收到配置更新
                 * @param configInfo Nacos 中最新的配置定义
                 * */
                @Override
                public void receiveConfigInfo(String configInfo) {

                    log.info("开始更新配置: [{}]", configInfo);
                    List<RouteDefinition> definitionList = JSON.parseArray(configInfo, RouteDefinition.class);
                    log.info("更新路由: [{}]", definitionList.toString());
                    dynamicRouteService.updateList(definitionList);
                }
            });
        } catch (NacosException ex) {
            log.error("动态更新网关配置错误: [{}]", ex.getMessage(), ex);
        }
    }
}
