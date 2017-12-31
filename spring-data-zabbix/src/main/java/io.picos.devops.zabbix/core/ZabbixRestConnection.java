package io.picos.devops.zabbix.core;

import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.web.client.RestTemplate;

/**
 * @author dz
 */
public class ZabbixRestConnection implements ZabbixConnection {

    private boolean initialized = false;

    private ConnectOptions connectOptions;

    private RestTemplate restTemplate;

    private String authToken;

    public ZabbixRestConnection(RestTemplate restTemplate, ConnectOptions connectOptions) {
        this.restTemplate = restTemplate;
        this.connectOptions = connectOptions;
    }

    /**
     * Init the connection.
     *
     * @throws DataAccessException
     */
    @Override
    public void init() throws DataAccessException {
        if (!this.initialized) {
            //do auth
            ZabbixRequest authRequest = new ZabbixRequest();


        }
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

    @Override
    public ZabbixResponse execute(ZabbixRequest zabbixRequest) {
        if (StringUtils.isEmpty(zabbixRequest.getAuth())) {
            if (!this.initialized) {
                throw new ZabbixException("The connection is not initialized.");
            }

            zabbixRequest.setAuth(this.authToken);
        }

        //do rest quest
        //get
        //post
        //put
        //delete
        //others
        throw new ZabbixException(String.format("Unsupported zabbix request method %s", zabbixRequest.getMethod()));
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
