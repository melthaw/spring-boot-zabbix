package io.picos.devops.zabbix.core;

import org.springframework.dao.DataAccessException;

/**
 * @author dz
 */
public interface ZabbixCallback<T> {

    /**
     * Gets called by {@link ZabbixTemplate} with an active Zabbix connection. Does not need to care about activating or
     * closing the connection or handling exceptions.
     *
     * @param connection active Zabbix connection
     * @return a result object or {@code null} if none
     * @throws DataAccessException
     */
    T doInZabbix(ZabbixConnection connection) throws DataAccessException;

}
