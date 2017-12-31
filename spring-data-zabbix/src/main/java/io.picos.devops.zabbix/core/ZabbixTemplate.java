package io.picos.devops.zabbix.core;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.support.PersistenceExceptionTranslator;
import org.springframework.util.Assert;

/**
 * @author dz
 */
public class ZabbixTemplate implements ZabbixOperations, InitializingBean, ApplicationContextAware {

    private ApplicationEventPublisher applicationContext;

    private ZabbixConnectionFactory connectionFactory;

    private final PersistenceExceptionTranslator exceptionTranslator;

    public ZabbixTemplate(ZabbixConnectionFactory zabbixConnectionFactory) {
        Assert.notNull(zabbixConnectionFactory);

        this.connectionFactory = zabbixConnectionFactory;
        this.exceptionTranslator = zabbixConnectionFactory.getExceptionTranslator();
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public ZabbixConnectionFactory getConnectionFactory() {
        return connectionFactory;
    }

    public PersistenceExceptionTranslator getExceptionTranslator() {
        return exceptionTranslator;
    }

    public void afterPropertiesSet() {
        Assert.notNull(connectionFactory, "ZabbixConnectionFactory is required");
    }

    @Override
    public <T> T execute(ZabbixCallback<T> action) {
        Assert.notNull(action);

        try {
            ZabbixConnection conn = this.getConnection();
            return postProcessResult(action.doInZabbix(conn));
        } catch (RuntimeException e) {
            throw potentiallyConvertRuntimeException(e, exceptionTranslator);
        }
    }

    @Override
    public String apiVersion() {
        return null;
    }

    private ZabbixConnection getConnection() {
        return getConnectionFactory().getConnection();
    }

    private static RuntimeException potentiallyConvertRuntimeException(RuntimeException ex,
                                                                       PersistenceExceptionTranslator exceptionTranslator) {
        RuntimeException resolved = exceptionTranslator.translateExceptionIfPossible(ex);
        return resolved == null ? ex : resolved;
    }

    protected <T> T postProcessResult(T result) {
        return result;
    }

}
