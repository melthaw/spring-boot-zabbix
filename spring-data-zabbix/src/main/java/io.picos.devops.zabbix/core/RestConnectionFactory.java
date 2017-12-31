package io.picos.devops.zabbix.core;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.client.RestTemplate;

/**
 * @author dz
 */
public class RestConnectionFactory implements InitializingBean, DisposableBean, ZabbixConnectionFactory {

    private RestTemplate restTemplate;

    @Override
    public ZabbixConnection getConnection() {
        return null;
    }

    protected ZabbixConnection postProcessConnection(ZabbixConnection connection) {
        return connection;
    }

    /**
     * Invoked by a BeanFactory on destruction of a singleton.
     *
     * @throws Exception in case of shutdown errors.
     *                   Exceptions will get logged but not rethrown to allow
     *                   other beans to release their resources too.
     */
    @Override
    public void destroy() throws Exception {

    }

    /**
     * Invoked by a BeanFactory after it has set all bean properties supplied
     * (and satisfied BeanFactoryAware and ApplicationContextAware).
     * <p>This method allows the bean instance to perform initialization only
     * possible when all bean properties have been set and to throw an
     * exception in the event of misconfiguration.
     *
     * @throws Exception in the event of misconfiguration (such
     *                   as failure to set an essential property) or if initialization fails.
     */
    @Override
    public void afterPropertiesSet() throws Exception {

    }

    /**
     * Translate the given runtime exception thrown by a persistence framework to a
     * corresponding exception from Spring's generic
     * {@link DataAccessException} hierarchy, if possible.
     * <p>Do not translate exceptions that are not understood by this translator:
     * for example, if coming from another persistence framework, or resulting
     * from user code or otherwise unrelated to persistence.
     * <p>Of particular importance is the correct translation to
     * DataIntegrityViolationException, for example on constraint violation.
     * Implementations may use Spring JDBC's sophisticated exception translation
     * to provide further information in the event of SQLException as a root cause.
     *
     * @param ex a RuntimeException to translate
     * @return the corresponding DataAccessException (or {@code null} if the
     * exception could not be translated, as in this case it may result from
     * user code rather than from an actual persistence problem)
     * @see DataIntegrityViolationException
     * @see org.springframework.jdbc.support.SQLExceptionTranslator
     */
    @Override
    public DataAccessException translateExceptionIfPossible(RuntimeException ex) {
        return null;
    }
}
