package io.picos.devops.zabbix.core;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author dz
 */
public class ZabbixSimpleConnectionFactory extends AbstractZabbixConnectionFactory implements ZabbixConnectionFactory {

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

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();

        List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
        //Add the Jackson Message converter
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        // Note: here we are making this converter to process any kind of response,
        // not only application/*json, which is the default behaviour
        converter.setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON_UTF8, MediaType.TEXT_HTML));
        messageConverters.add(converter);
        restTemplate.setMessageConverters(messageConverters);

//        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
//        restTemplate.setInterceptors(Collections.singletonList(new RestTemplateHeaderInterceptor()));
    }

}
