package io.picos.devops.zabbix.core;

import org.springframework.dao.DataAccessException;
import org.springframework.web.client.RestTemplate;

/**
 * @author dz
 */
public class ZabbixRestConnection implements ZabbixConnection {

    private boolean initialized = false;

    private RestTemplate restTemplate;

    public ZabbixRestConnection(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Init the connection.
     *
     * @throws DataAccessException
     */
    @Override
    public void init() throws DataAccessException {

    }

    /**
     * Indicates whether the underlying connection is initialized or not.
     *
     * @return true if the connection is initialized, false otherwise.
     */
    @Override
    public boolean isInitialized() {
        return this.initialized;
    }

    /**
     * Closes (or quits) the connection.
     *
     * @throws DataAccessException
     */
    @Override
    public void close() throws DataAccessException {

    }

    /**
     * Indicates whether the underlying connection is closed or not.
     *
     * @return true if the connection is closed, false otherwise.
     */
    @Override
    public boolean isClosed() {
        return false;
    }
}
