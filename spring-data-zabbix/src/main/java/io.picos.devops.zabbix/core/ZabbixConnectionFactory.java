package io.picos.devops.zabbix.core;

import org.springframework.dao.support.PersistenceExceptionTranslator;

/**
 * @author dz
 */
public interface ZabbixConnectionFactory extends PersistenceExceptionTranslator {

    ZabbixConnection getConnection();

}
