package com.fish1208.sdk.entity;

import org.springframework.core.io.Resource;

import java.io.Serializable;

/**
 * Created by 余昌鸿 on 2018/10/17.
 */
public class Orderer implements Serializable {

    private static final long serialVersionUID = -1L;

    private String name; // required

    private String location; // required

    private String domainName; // required

    private Resource serverCrtPath;

    private Resource clientCertPath;

    private Resource clientKeyPath;

    private int orgId; // required

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

    public int getOrgId() {
        return orgId;
    }

    public void setOrgId(int orgId) {
        this.orgId = orgId;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }
}
