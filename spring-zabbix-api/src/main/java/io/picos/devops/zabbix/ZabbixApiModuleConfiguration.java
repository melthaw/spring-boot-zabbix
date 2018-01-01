package io.picos.devops.zabbix;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author dz
 */
@Configuration
@ComponentScan({"io.picos.devops.zabbix.web", "io.picos.devops.zabbix.service"})
public class ZabbixApiModuleConfiguration {
}
