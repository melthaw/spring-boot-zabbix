package io.picos.devops.zabbix.autoconfigure;

import io.picos.devops.zabbix.core.ZabbixConnection;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author dz
 */
@Configuration
@ConditionalOnClass(ZabbixConnection.class)
@EnableConfigurationProperties(ZabbixProperties.class)
public class ZabbixDataAutoConfiguration {
}
