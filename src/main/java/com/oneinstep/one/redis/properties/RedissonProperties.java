package com.hst.bss.light.redis.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * redisson 配置类
 *
 * @author aaron.shaw
 * @date 2023-05-24 15:18
 **/
@ConfigurationProperties(prefix = "bss.light.redisson")
public class RedissonProperties {
    /**
     * 是否开启Redisson
     */
    private boolean enable = false;
    /**
     * 模式
     * 0-单机
     * 1-集群
     */
    private int mode = 1;
    /**
     * 节点地址
     */
    private String nodeAddress;
    /**
     * master 连接池数量
     */
    private int masterConnectionPoolSize = 64;
    /**
     * slave 连接池数量
     */
    private int slaveConnectionPoolSize = 64;
    /**
     * 数据库
     * 单机模式有用
     */
    private int database = 0;
    /**
     * Password for Redis authentication. Should be null if not needed
     */
    private String password;
    /**
     * 用户名
     */
    private String username;
    /**
     * Timeout during connecting to any Redis server. Value in milliseconds.
     */
    private int connectTimeout = 10000;

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public String getNodeAddress() {
        return nodeAddress;
    }

    public void setNodeAddress(String nodeAddress) {
        this.nodeAddress = nodeAddress;
    }

    public int getMasterConnectionPoolSize() {
        return masterConnectionPoolSize;
    }

    public void setMasterConnectionPoolSize(int masterConnectionPoolSize) {
        this.masterConnectionPoolSize = masterConnectionPoolSize;
    }

    public int getSlaveConnectionPoolSize() {
        return slaveConnectionPoolSize;
    }

    public void setSlaveConnectionPoolSize(int slaveConnectionPoolSize) {
        this.slaveConnectionPoolSize = slaveConnectionPoolSize;
    }

    public int getDatabase() {
        return database;
    }

    public void setDatabase(int database) {
        this.database = database;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }
}
