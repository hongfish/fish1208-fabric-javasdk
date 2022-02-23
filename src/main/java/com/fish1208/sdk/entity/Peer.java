package com.fish1208.sdk.entity;

import org.springframework.core.io.Resource;

import java.io.Serializable;

/**
 * Created by 余昌鸿 on 2018/10/17.
 */
public class Peer implements Serializable {

    private static final long serialVersionUID = -1L;

    private String name; // required

    private String location; // required

    //fabric1.3以上的版本，已经将7053这个事件逻辑删除，将事件通知移植到了channel上面，也就是说在1.3以后的版本不需要使用addEventHub为每个节点增加事件。
    private String eventHubLocation; // 1.2版需要配置，1.3版本后不需要配置（配置会出错）

    private int orgId; // required

    private Resource serverCrtPath;

    private Resource clientCertPath;

    private Resource clientKeyPath;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getEventHubLocation() {
        return eventHubLocation;
    }

    public void setEventHubLocation(String eventHubLocation) {
        this.eventHubLocation = eventHubLocation;
    }

    public int getOrgId() {
        return orgId;
    }

    public void setOrgId(int orgId) {
        this.orgId = orgId;
    }

    public Resource getServerCrtPath() {
        return serverCrtPath;
    }

    public void setServerCrtPath(Resource serverCrtPath) {
        this.serverCrtPath = serverCrtPath;
    }

    public Resource getClientCertPath() {
        return clientCertPath;
    }

    public void setClientCertPath(Resource clientCertPath) {
        this.clientCertPath = clientCertPath;
    }

    public Resource getClientKeyPath() {
        return clientKeyPath;
    }

    public void setClientKeyPath(Resource clientKeyPath) {
        this.clientKeyPath = clientKeyPath;
    }
}
