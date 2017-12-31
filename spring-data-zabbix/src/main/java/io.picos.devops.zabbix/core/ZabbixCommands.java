package io.picos.devops.zabbix.core;

/**
 * @author dz
 */
public interface ZabbixCommands {

    ZabbixResponse execute(ZabbixRequest zabbixRequest);
}
