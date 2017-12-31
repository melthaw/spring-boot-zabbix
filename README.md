# Overview

[Zabbix API 3.0](https://www.zabbix.com/documentation/3.0/manual/api) is supported.

## Versions


module name | latest version
------|------
spring-data-zabbix | 0.0.1-SNAPSHOT
spring-boot-starter-zabbix | 0.0.1-SNAPSHOT
spring-zabbix-api | 0.0.1-SNAPSHOT

## Maven

```
    <dependency>
        <groupId>io.picos.devops</groupId>
        <artifactId>spring-data-zabbix</artifactId>
        <version>${spring-data-zabbix-version}</version>
    </dependency>

    <dependency>
        <groupId>io.picos.devops</groupId>
        <artifactId>spring-boot-starter-zabbix</artifactId>
        <version>${spring-boot-starter-zabbix-version}</version>
    </dependency>

    <dependency>
        <groupId>io.picos.devops</groupId>
        <artifactId>spring-zabbix-api</artifactId>
        <version>${spring-zabbix-api-version}</version>
    </dependency>
```

## Gradle

```
    compile "io.picos.devops:spring-data-zabbix:${spring-data-zabbix-version}"
    compile "io.picos.devops:spring-boot-starter-zabbix:${spring-boot-starter-zabbix-version}"
    compile "io.picos.devops:spring-zabbix-api:${spring-zabbix-api-version}"
```

## Get Started


```
cd spring-zabbix-api-server
gradle clean bootRun
```
