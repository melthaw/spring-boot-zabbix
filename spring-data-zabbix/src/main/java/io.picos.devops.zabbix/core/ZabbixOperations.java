package io.picos.devops.zabbix.core;

/**
 * @author dz
 */
public interface ZabbixOperations<K, V> {

    /**
     * Executes the given action within a Zabbix connection. Application exceptions thrown by the action object get
     * propagated to the caller (can only be unchecked) whenever possible. Zabbix exceptions are transformed into
     * appropriate DAO ones. Allows for returning a result object, that is a domain object or a collection of domain
     * objects. Performs automatic serialization/deserialization for the given objects to and from binary data suitable
     * for the Zabbix server. Note: Callback code is not supposed to handle transactions itself! Use an appropriate
     * transaction manager. Generally, callback code must not touch any Connection lifecycle methods, like close, to let
     * the template do its work.
     *
     * @param <T>    return type
     * @param action callback object that specifies the Zabbix action
     * @return a result object returned by the action or <tt>null</tt>
     */
    <T> T execute(ZabbixCallback<T> action);

    String apiVersion();
}

