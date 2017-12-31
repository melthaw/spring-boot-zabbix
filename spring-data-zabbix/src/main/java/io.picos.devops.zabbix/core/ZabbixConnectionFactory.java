package io.picos.devops.zabbix.core;

import org.springframework.dao.support.PersistenceExceptionTranslator;

/**
 * @author dz
 */
public interface ZabbixConnectionFactory {

    /**
     * @return
     */
    ZabbixConnection getConnection();

    /**
     * Exposes a shared {@link ZabbixExceptionTranslator}.
     *
     * @return will never be {@literal null}.
     */
    PersistenceExceptionTranslator getExceptionTranslator();

}
