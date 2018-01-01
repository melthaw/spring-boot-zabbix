package io.picos.devops.zabbix.web.controller;

import io.picos.devops.zabbix.core.ZabbixResponse;
import io.picos.devops.zabbix.web.support.ZabbixRestSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author dz
 */
@RestController
public class ZabbixRestController {

    @Autowired
    private ZabbixRestSupport zabbixRestSupport;

    @RequestMapping(value = "/apiinfo/version", method = RequestMethod.GET)
    public String getApiVerison() {
        return zabbixRestSupport.getApiVerison();
    }


}
