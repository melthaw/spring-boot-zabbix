package io.picos.devops.zabbix.service.impl;

import io.picos.devops.zabbix.core.ZabbixTemplate;
import io.picos.devops.zabbix.service.ZabbixService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author dz
 */
@Service
public class ZabbixServiceImpl implements ZabbixService {

    @Autowired
    private ZabbixTemplate zabbixTemplate;

    @Override
    public String getApiVersion() {
        return zabbixTemplate.apiVersion();
    }
}
