package io.picos.devops.zabbix.autoconfigure;

import io.picos.devops.zabbix.core.ConnectOptions;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for Zabbix.
 *
 * @author dz
 */
@ConfigurationProperties(prefix = "io.picos.devops.zabbix")
public class ZabbixProperties extends ConnectOptions {

    private ZabbixProperties.Pool pool;

    public Pool getPool() {
        return pool;
    }

    public void setPool(Pool pool) {
        this.pool = pool;
    }

    public static class Pool {

        private int maxIdle = 8;
        private int minIdle = 0;
        private int maxActive = 8;
        private int maxWait = -1;

        public Pool() {
        }

        public int getMaxIdle() {
            return this.maxIdle;
        }

        public void setMaxIdle(int maxIdle) {
            this.maxIdle = maxIdle;
        }

        public int getMinIdle() {
            return this.minIdle;
        }

        public void setMinIdle(int minIdle) {
            this.minIdle = minIdle;
        }

        public int getMaxActive() {
            return this.maxActive;
        }

        public void setMaxActive(int maxActive) {
            this.maxActive = maxActive;
        }

        public int getMaxWait() {
            return this.maxWait;
        }

        public void setMaxWait(int maxWait) {
            this.maxWait = maxWait;
        }
    }
}
