package io.picos.devops.zabbix.autoconfigure;

import io.picos.devops.zabbix.core.ZabbixTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;


/**
 * @author dz
 */
@Configuration
@ConditionalOnClass(ZabbixTemplate.class)
@EnableConfigurationProperties(ZabbixProperties.class)
public class ZabbixApiAutoConfiguration {
}
