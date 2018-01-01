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
        ZabbixRestConnection result = new ZabbixRestConnection(restTemplate, connectOptions);
        postProcessConnection(result);
        return result;
    }

    protected ZabbixConnection postProcessConnection(ZabbixRestConnection connection) {
        connection.init();
        return connection;
    }

}
