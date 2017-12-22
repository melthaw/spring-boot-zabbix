package io.picos.devops.zabbix;

import com.alibaba.fastjson.JSONObject;

public interface ZabbixOperation {

	void init();

	void destroy();

	String apiVersion();

	JSONObject call(Request request);

	boolean login(String user, String password);

}
