package io.picos.devops.zabbix.core;

import org.springframework.dao.*;
import org.springframework.dao.support.PersistenceExceptionTranslator;
import org.springframework.util.ClassUtils;

/**
 * Simple {@link PersistenceExceptionTranslator} for Zabbix. Convert the given runtime exception to an appropriate
 * exception from the {@code org.springframework.dao} hierarchy. Return {@literal null} if no translation is
 * appropriate: any other exception may have resulted from user code, and should not be translated.
 *
 * @author dz
 */
public class ZabbixExceptionTranslator implements PersistenceExceptionTranslator {

    /*
     * (non-Javadoc)
     * @see org.springframework.dao.support.PersistenceExceptionTranslator#translateExceptionIfPossible(java.lang.RuntimeException)
     */
    @Override
    public DataAccessException translateExceptionIfPossible(RuntimeException ex) {

        String exception = ClassUtils.getShortName(ClassUtils.getUserClass(ex.getClass()));

        //TODO

        // All other ZabbixExceptions
        if (ex instanceof ZabbixException) {
            //TODO
        }

        // If we get here, we have an exception that resulted from user code,
        // rather than the persistence provider, so we return null to indicate
        // that translation should not occur.
        return null;
    }

    public static final class ZabbixErrorCodes {

    }
}
