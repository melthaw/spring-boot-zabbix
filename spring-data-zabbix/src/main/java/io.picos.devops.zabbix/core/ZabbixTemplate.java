package io.picos.devops.zabbix.core;

import com.alibaba.fastjson.JSONObject;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.Assert;

/**
 * @author dz
 */
public class ZabbixTemplate extends ZabbixAccessor implements ZabbixOperations {

    private boolean enableTransactionSupport = false;
    private boolean exposeConnection = false;
    private boolean initialized = false;
    private boolean enableDefaultSerializer = true;

    private ZabbixConnection zabbixConnection;

    public void afterPropertiesSet() {
        super.afterPropertiesSet();

        if (enableDefaultSerializer) {

        }

        initialized = true;
    }

    public <T> T execute(ZabbixCallback<T> action) {
        return execute(action, isExposeConnection());
    }

    /**
     * Executes the given action object within a connection, which can be exposed or not.
     *
     * @param <T>              return type
     * @param action           callback object that specifies the Zabbix action
     * @param exposeConnection whether to enforce exposure of the native Zabbix Connection to callback code
     * @return object returned by the action
     */
    public <T> T execute(ZabbixCallback<T> action, boolean exposeConnection) {
        return execute(action, exposeConnection, false);
    }

    /**
     * Executes the given action object within a connection that can be exposed or not. Additionally, the connection can
     * be pipelined. Note the results of the pipeline are discarded (making it suitable for write-only scenarios).
     *
     * @param <T>              return type
     * @param action           callback object to execute
     * @param exposeConnection whether to enforce exposure of the native Zabbix Connection to callback code
     * @param pipeline         whether to pipeline or not the connection for the execution
     * @return object returned by the action
     */
    public <T> T execute(ZabbixCallback<T> action, boolean exposeConnection, boolean pipeline) {
        Assert.isTrue(initialized, "template not initialized; call afterPropertiesSet() before using it");
        Assert.notNull(action, "Callback object must not be null");

        ZabbixConnectionFactory factory = getConnectionFactory();
        ZabbixConnection conn = null;
        try {

            if (enableTransactionSupport) {
                // only bind resources in case of potential transaction synchronization
                conn = ZabbixConnectionUtils.bindConnection(factory, enableTransactionSupport);
            }
            else {
                conn = ZabbixConnectionUtils.getConnection(factory);
            }

            boolean existingConnection = TransactionSynchronizationManager.hasResource(factory);

            ZabbixConnection connToUse = preProcessConnection(conn, existingConnection);

            boolean pipelineStatus = connToUse.isPipelined();
            if (pipeline && !pipelineStatus) {
                connToUse.openPipeline();
            }

            ZabbixConnection connToExpose = (exposeConnection ? connToUse : createZabbixConnectionProxy(connToUse));
            T result = action.doInZabbix(connToExpose);

            // close pipeline
            if (pipeline && !pipelineStatus) {
                connToUse.closePipeline();
            }

            // TODO: any other connection processing?
            return postProcessResult(result, connToUse, existingConnection);
        } finally {

            if (!enableTransactionSupport) {
                ZabbixConnectionUtils.releaseConnection(conn, factory);
            }
        }
    }

    /**
     * Processes the connection (before any settings are executed on it). Default implementation returns the connection as
     * is.
     *
     * @param connection zabbix connection
     */
    protected ZabbixConnection preProcessConnection(ZabbixConnection connection, boolean existingConnection) {
        return connection;
    }

    protected <T> T postProcessResult(T result, ZabbixConnection conn, boolean existingConnection) {
        return result;
    }

    /**
     * Returns whether to expose the native Zabbix connection to ZabbixCallback code, or rather a connection proxy (the
     * default).
     *
     * @return whether to expose the native Zabbix connection or not
     */
    public boolean isExposeConnection() {
        return exposeConnection;
    }

    /**
     * Sets whether to expose the Zabbix connection to {@link ZabbixCallback} code. Default is "false": a proxy will be
     * returned, suppressing <tt>quit</tt> and <tt>disconnect</tt> calls.
     *
     * @param exposeConnection
     */
    public void setExposeConnection(boolean exposeConnection) {
        this.exposeConnection = exposeConnection;
    }

    /**
     * @return Whether or not the default serializer should be used. If not, any serializers not explicilty set will
     * remain null and values will not be serialized or deserialized.
     */
    public boolean isEnableDefaultSerializer() {
        return enableDefaultSerializer;
    }

    /**
     * @param enableDefaultSerializer Whether or not the default serializer should be used. If not, any serializers not
     *                                explicilty set will remain null and values will not be serialized or deserialized.
     */
    public void setEnableDefaultSerializer(boolean enableDefaultSerializer) {
        this.enableDefaultSerializer = enableDefaultSerializer;
    }


}
