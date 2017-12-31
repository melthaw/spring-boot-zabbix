package io.picos.devops.zabbix.core;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.client.RestTemplate;

/**
 * @author dz
 */
public class ZabbixSimpleConnectionFactory extends AbstractZabbixConnectionFactory implements InitializingBean, DisposableBean, ZabbixConnectionFactory {

    private RestTemplate restTemplate;

    public ZabbixSimpleConnectionFactory(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.exceptionTranslator = new ZabbixExceptionTranslator();
    }

    @Override
    public ZabbixConnection getConnection() {
        ConnectOptions connectOptions = new ConnectOptions();
        connectOptions.setUrl(this.getUrl());
        connectOptions.setUsername(this.getUsername());
        connectOptions.setPassword(this.getPassword());
        return new ZabbixRestConnection(restTemplate, connectOptions);
    }

    protected ZabbixConnection postProcessConnection(ZabbixConnection connection) {
        return connection;
    }

}
