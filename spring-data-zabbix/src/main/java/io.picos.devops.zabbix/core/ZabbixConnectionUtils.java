package io.picos.devops.zabbix.core;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.transaction.support.ResourceHolder;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.Assert;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Helper class featuring {@link ZabbixConnection} handling, allowing for reuse of instances within
 * 'transactions'/scopes.
 *
 * @author dz
 */
public class ZabbixConnectionUtils {

    private static final Log log = LogFactory.getLog(ZabbixConnectionUtils.class);

    /**
     * Binds a new Zabbix connection (from the given factory) to the current thread, if none is already bound.
     *
     * @param factory connection factory
     * @return a new Zabbix connection without transaction support.
     */
    public static ZabbixConnection bindConnection(ZabbixConnectionFactory factory) {
        return bindConnection(factory, false);
    }

    /**
     * Binds a new Zabbix connection (from the given factory) to the current thread, if none is already bound and enables
     * transaction support if {@code enableTranactionSupport} is set to {@literal true}.
     *
     * @param factory                 connection factory
     * @param enableTranactionSupport
     * @return a new Zabbix connection with transaction support if requested.
     */
    public static ZabbixConnection bindConnection(ZabbixConnectionFactory factory, boolean enableTranactionSupport) {
        return doGetConnection(factory, true, true, enableTranactionSupport);
    }

    /**
     * Gets a Zabbix connection from the given factory. Is aware of and will return any existing corresponding connections
     * bound to the current thread, for example when using a transaction manager. Will always create a new connection
     * otherwise.
     *
     * @param factory connection factory for creating the connection
     * @return an active Zabbix connection without transaction management.
     */
    public static ZabbixConnection getConnection(ZabbixConnectionFactory factory) {
        return getConnection(factory, false);
    }

    /**
     * Gets a Zabbix connection from the given factory. Is aware of and will return any existing corresponding connections
     * bound to the current thread, for example when using a transaction manager. Will always create a new connection
     * otherwise.
     *
     * @param factory                 connection factory for creating the connection
     * @param enableTranactionSupport
     * @return an active Zabbix connection with transaction management if requested.
     */
    public static ZabbixConnection getConnection(ZabbixConnectionFactory factory, boolean enableTranactionSupport) {
        return doGetConnection(factory, true, false, enableTranactionSupport);
    }

    /**
     * Gets a Zabbix connection. Is aware of and will return any existing corresponding connections bound to the current
     * thread, for example when using a transaction manager. Will create a new Connection otherwise, if
     * {@code allowCreate} is <tt>true</tt>.
     *
     * @param factory                  connection factory for creating the connection
     * @param allowCreate              whether a new (unbound) connection should be created when no connection can be found for the
     *                                 current thread
     * @param bind                     binds the connection to the thread, in case one was created
     * @param enableTransactionSupport
     * @return an active Zabbix connection
     */
    public static ZabbixConnection doGetConnection(ZabbixConnectionFactory factory, boolean allowCreate, boolean bind,
                                                   boolean enableTransactionSupport) {

        Assert.notNull(factory, "No ZabbixConnectionFactory specified");

        ZabbixConnectionHolder connHolder = (ZabbixConnectionHolder) TransactionSynchronizationManager.getResource(
                factory);

        if (connHolder != null) {
            if (enableTransactionSupport) {
                potentiallyRegisterTransactionSynchronisation(connHolder, factory);
            }
            return connHolder.getConnection();
        }

        if (!allowCreate) {
            throw new IllegalArgumentException("No connection found and allowCreate = false");
        }

        if (log.isDebugEnabled()) {
            log.debug("Opening ZabbixConnection");
        }

        ZabbixConnection conn = factory.getConnection();

        if (bind) {

            ZabbixConnection connectionToBind = conn;
            if (enableTransactionSupport && isActualNonReadonlyTransactionActive()) {
                connectionToBind = createConnectionProxy(conn, factory);
            }

            connHolder = new ZabbixConnectionHolder(connectionToBind);

            TransactionSynchronizationManager.bindResource(factory, connHolder);
            if (enableTransactionSupport) {
                potentiallyRegisterTransactionSynchronisation(connHolder, factory);
            }

            return connHolder.getConnection();
        }

        return conn;
    }

    private static void potentiallyRegisterTransactionSynchronisation(ZabbixConnectionHolder connHolder,
                                                                      final ZabbixConnectionFactory factory) {

        if (isActualNonReadonlyTransactionActive()) {

            if (!connHolder.isTransactionSyncronisationActive()) {
                connHolder.setTransactionSyncronisationActive(true);

                ZabbixConnection conn = connHolder.getConnection();
                conn.multi();

                TransactionSynchronizationManager.registerSynchronization(new ZabbixTransactionSynchronizer(connHolder,
                                                                                                            conn,
                                                                                                            factory));
            }
        }
    }

    private static boolean isActualNonReadonlyTransactionActive() {
        return TransactionSynchronizationManager.isActualTransactionActive()
                && !TransactionSynchronizationManager.isCurrentTransactionReadOnly();
    }

    private static ZabbixConnection createConnectionProxy(ZabbixConnection connection,
                                                          ZabbixConnectionFactory factory) {

        ProxyFactory proxyFactory = new ProxyFactory(connection);
        proxyFactory.addAdvice(new ConnectionSplittingInterceptor(factory));

        return ZabbixConnection.class.cast(proxyFactory.getProxy());
    }

    /**
     * Closes the given connection, created via the given factory if not managed externally (i.e. not bound to the
     * thread).
     *
     * @param conn    the Zabbix connection to close
     * @param factory the Zabbix factory that the connection was created with
     */
    public static void releaseConnection(ZabbixConnection conn, ZabbixConnectionFactory factory) {

        if (conn == null) {
            return;
        }

        ZabbixConnectionHolder connHolder = (ZabbixConnectionHolder) TransactionSynchronizationManager.getResource(
                factory);

        if (connHolder != null && connHolder.isTransactionSyncronisationActive()) {
            if (log.isDebugEnabled()) {
                log.debug("Zabbix Connection will be closed when transaction finished.");
            }
            return;
        }

        // Only release non-transactional/non-bound connections.
        if (!isConnectionTransactional(conn, factory)) {
            if (log.isDebugEnabled()) {
                log.debug("Closing Zabbix Connection");
            }
            conn.close();
        }
    }

    /**
     * Unbinds and closes the connection (if any) associated with the given factory.
     *
     * @param factory Zabbix factory
     */
    public static void unbindConnection(ZabbixConnectionFactory factory) {

        ZabbixConnectionHolder connHolder = (ZabbixConnectionHolder) TransactionSynchronizationManager
                .unbindResourceIfPossible(factory);

        if (connHolder != null) {
            if (connHolder.isTransactionSyncronisationActive()) {
                if (log.isDebugEnabled()) {
                    log.debug("Zabbix Connection will be closed when outer transaction finished.");
                }
            }
            else {
                if (log.isDebugEnabled()) {
                    log.debug("Closing bound connection.");
                }
                ZabbixConnection connection = connHolder.getConnection();
                connection.close();
            }
        }

    }

    /**
     * Return whether the given Zabbix connection is transactional, that is, bound to the current thread by Spring's
     * transaction facilities.
     *
     * @param conn        Zabbix connection to check
     * @param connFactory Zabbix connection factory that the connection was created with
     * @return whether the connection is transactional or not
     */
    public static boolean isConnectionTransactional(ZabbixConnection conn, ZabbixConnectionFactory connFactory) {
        if (connFactory == null) {
            return false;
        }
        ZabbixConnectionHolder connHolder = (ZabbixConnectionHolder) TransactionSynchronizationManager
                .getResource(connFactory);

        return (connHolder != null && conn == connHolder.getConnection());
    }

    /**
     * A {@link TransactionSynchronizationAdapter} that makes sure that the associated ZabbixConnection is released after
     * the transaction completes.
     *
     * @author Christoph Strobl
     * @author Thomas Darimont
     */
    private static class ZabbixTransactionSynchronizer extends TransactionSynchronizationAdapter {

        private final ZabbixConnectionHolder connHolder;
        private final ZabbixConnection connection;
        private final ZabbixConnectionFactory factory;

        /**
         * Creates a new {@link ZabbixTransactionSynchronizer}.
         *
         * @param connHolder
         * @param connection
         * @param factory
         */
        private ZabbixTransactionSynchronizer(ZabbixConnectionHolder connHolder, ZabbixConnection connection,
                                              ZabbixConnectionFactory factory) {

            this.connHolder = connHolder;
            this.connection = connection;
            this.factory = factory;
        }

        @Override
        public void afterCompletion(int status) {

            try {
                switch (status) {

                    case TransactionSynchronization.STATUS_COMMITTED:
                        connection.exec();
                        break;

                    case TransactionSynchronization.STATUS_ROLLED_BACK:
                    case TransactionSynchronization.STATUS_UNKNOWN:
                    default:
                        connection.discard();
                }
            } finally {

                if (log.isDebugEnabled()) {
                    log.debug("Closing bound connection after transaction completed with " + status);
                }

                connHolder.setTransactionSyncronisationActive(false);
                connection.close();
                TransactionSynchronizationManager.unbindResource(factory);
            }
        }
    }

    /**
     * @author Christoph Strobl
     * @since 1.3
     */
    static class ConnectionSplittingInterceptor implements MethodInterceptor,
            org.springframework.cglib.proxy.MethodInterceptor {

        private final ZabbixConnectionFactory factory;

        public ConnectionSplittingInterceptor(ZabbixConnectionFactory factory) {
            this.factory = factory;
        }

        @Override
        public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {

            ZabbixCommand commandToExecute = ZabbixCommand.failsafeCommandLookup(method.getName());

            if (isPotentiallyThreadBoundCommand(commandToExecute)) {

                if (log.isDebugEnabled()) {
                    log.debug(String.format("Invoke '%s' on bound conneciton", method.getName()));
                }

                return invoke(method, obj, args);
            }

            if (log.isDebugEnabled()) {
                log.debug(String.format("Invoke '%s' on unbound conneciton", method.getName()));
            }

            ZabbixConnection connection = factory.getConnection();

            try {
                return invoke(method, connection, args);
            } finally {
                // properly close the unbound connection after executing command
                if (!connection.isClosed()) {
                    connection.close();
                }
            }
        }

        private Object invoke(Method method, Object target, Object[] args) throws Throwable {

            try {
                return method.invoke(target, args);
            } catch (InvocationTargetException e) {
                throw e.getCause();
            }
        }

        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            return intercept(invocation.getThis(), invocation.getMethod(), invocation.getArguments(), null);
        }

        private boolean isPotentiallyThreadBoundCommand(ZabbixCommand command) {
            return ZabbixCommand.UNKNOWN.equals(command) || !command.isReadonly();
        }
    }

    /**
     * @author Christoph Strobl
     */
    private static class ZabbixConnectionHolder implements ResourceHolder {

        private boolean unbound;
        private final ZabbixConnection conn;
        private boolean transactionSyncronisationActive;

        public ZabbixConnectionHolder(ZabbixConnection conn) {
            this.conn = conn;
        }

        public boolean isVoid() {
            return unbound;
        }

        public ZabbixConnection getConnection() {
            return conn;
        }

        public void reset() {
            // no-op
        }

        public void unbound() {
            this.unbound = true;
        }

        /**
         * @return
         * @since 1.3
         */
        public boolean isTransactionSyncronisationActive() {
            return transactionSyncronisationActive;
        }

        /**
         * @param transactionSyncronisationActive
         * @since 1.3
         */
        public void setTransactionSyncronisationActive(boolean transactionSyncronisationActive) {
            this.transactionSyncronisationActive = transactionSyncronisationActive;
        }
    }
}
