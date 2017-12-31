package io.picos.devops.zabbix.core;

/**
 * @author dz
 */
public class ConnectOptions {

    private String url = "http://127.0.0.1";

    private String username = "Admin";

    private String password = "zabbix";

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
