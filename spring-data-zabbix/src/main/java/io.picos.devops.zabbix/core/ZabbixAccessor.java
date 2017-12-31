package io.picos.devops.zabbix.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * @author dz
 */
public class ZabbixAccessor implements InitializingBean {

    /**
     * Logger available to subclasses
     */
    protected final Log logger = LogFactory.getLog(getClass());

    private ZabbixConnectionFactory connectionFactory;

    public void afterPropertiesSet() {
        Assert.notNull(getConnectionFactory(), "ZabbixConnectionFactory is required");
    }

    /**
     * Returns the connectionFactory.
     *
     * @return Returns the connectionFactory
     */
    public ZabbixConnectionFactory getConnectionFactory() {
        return connectionFactory;
    }

    /**
     * Sets the connection factory.
     *
     * @param connectionFactory The connectionFactory to set.
     */
    public void setConnectionFactory(ZabbixConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }
}
