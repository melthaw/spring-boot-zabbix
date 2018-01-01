package io.picos.devops.zabbix.web.support.impl;

import io.picos.devops.zabbix.service.ZabbixService;
import io.picos.devops.zabbix.web.support.ZabbixRestSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author dz
 */
@Component
public class ZabbixRestSupportImpl implements ZabbixRestSupport {

    @Autowired
    private ZabbixService zabbixService;

    @Override
    public String getApiVerison() {
        return zabbixService.getApiVersion();
    }

}
