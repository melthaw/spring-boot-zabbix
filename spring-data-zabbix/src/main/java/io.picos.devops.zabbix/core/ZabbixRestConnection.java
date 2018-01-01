package io.picos.devops.zabbix.core;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.PermissionDeniedDataAccessException;
import org.springframework.http.ResponseEntity;
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
     */
    @Override
    public void init() {
        if (!this.initialized) {
            //do auth
            ZabbixRequest request = RequestBuilder.newBuilder()
                                                  .paramEntry("user", this.connectOptions.getUsername())
                                                  .paramEntry("password", this.connectOptions.getPassword())
                                                  .method("user.login")
                                                  .build();

            ZabbixResponse zabbixResponse = this.restTemplate.postForObject(this.connectOptions.getUrl(),
                                                                            request,
                                                                            ZabbixResponse.class);

            JSONObject jsonObject = zabbixResponse.getResult();
            String auth = jsonObject.toString();
            if (auth != null && !auth.isEmpty()) {
                this.authToken = auth;
                this.initialized = true;
            }
            else {
                throw new ZabbixException("Incorrect username or credential");
            }
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
