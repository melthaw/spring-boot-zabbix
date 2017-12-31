package io.picos.devops.zabbix.autoconfigure;

import io.picos.devops.zabbix.core.ZabbixConnectionFactory;
import io.picos.devops.zabbix.core.ZabbixSimpleConnectionFactory;
import io.picos.devops.zabbix.core.ZabbixTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.net.UnknownHostException;

/**
 * @author dz
 */
@Configuration
@ConditionalOnClass({RestTemplate.class})
@EnableConfigurationProperties
public class ZabbixDataAutoConfiguration {

    @Bean(name = {"io.picos.devops.zabbix.autoconfigure.ZabbixProperties"})
    @ConditionalOnMissingBean
    public ZabbixProperties zabbixProperties() {
        return new ZabbixProperties();
    }

    protected abstract static class AbstractZabbixConfiguration {

        @Autowired
        protected ZabbixProperties properties;

        protected AbstractZabbixConfiguration() {
        }

        protected final ZabbixSimpleConnectionFactory applyProperties(ZabbixSimpleConnectionFactory factory) {
            factory.setUrl(this.properties.getUrl());
            factory.setUsername(this.properties.getUsername());
            factory.setPassword(this.properties.getPassword());
            return factory;
        }

    }

//    @Configuration
//    @ConditionalOnClass({GenericObjectPool.class})
//    protected static class ZabbixPooledConnectionConfiguration extends ZabbixDataAutoConfiguration.AbstractZabbixConfiguration {
//        protected ZabbixPooledConnectionConfiguration() {
//        }
//
//        @Bean
//        @ConditionalOnMissingBean({ZabbixConnectionFactory.class})
//        public ZabbixConnectionFactory zabbixConnectionFactory() throws UnknownHostException {
//            return this.applyProperties(this.createZabbixPooledConnectionFactory());
//        }
//
//        private ZabbixConnectionFactory createZabbixPooledConnectionFactory() {
//            return this.properties.getPool() !=
//                    null ? new ZabbixPooledConnectionFactory() : new ZabbixSimpleConnectionFactory();
//        }
//
//    }

    @Configuration
    @ConditionalOnMissingClass({"org.apache.commons.pool2.impl.GenericObjectPool"})
    protected static class ZabbixConnectionConfiguration extends ZabbixDataAutoConfiguration.AbstractZabbixConfiguration {

        protected ZabbixConnectionConfiguration() {
        }

        @Bean
        @ConditionalOnMissingBean({ZabbixConnectionFactory.class})
        public ZabbixConnectionFactory zabbixConnectionFactory(RestTemplate restTemplate) throws UnknownHostException {
            return this.applyProperties(new ZabbixSimpleConnectionFactory(restTemplate));
        }

    }


    @Configuration
    protected static class ZabbixConfiguration {

        protected ZabbixConfiguration() {
        }

        @Bean
        @ConditionalOnMissingBean(
                name = {"zabbixTemplate"}
        )
        public ZabbixTemplate zabbixTemplate(ZabbixConnectionFactory zabbixConnectionFactory) throws
                                                                                              UnknownHostException {
            return new ZabbixTemplate(zabbixConnectionFactory);
        }

    }

}
