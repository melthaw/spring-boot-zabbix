package io.picos.devops.zabbix.core;

import org.springframework.dao.DataAccessException;

/**
 * @author dz
 */
public interface ZabbixConnection extends ZabbixCommands {

    /**
     * Init the connection.
     *
     * @throws DataAccessException
     */
    void init() throws DataAccessException;

    /**
     * Indicates whether the underlying connection is initialized or not.
     *
     * @return true if the connection is initialized, false otherwise.
     */
    boolean isInitialized();

    /**
     * Closes (or quits) the connection.
     *
     * @throws DataAccessException
     */
    void close() throws DataAccessException;

    /**
     * Indicates whether the underlying connection is closed or not.
     *
     * @return true if the connection is closed, false otherwise.
     */
    boolean isClosed();

}
