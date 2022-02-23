package com.fish1208.sdk.entity;

import java.io.Serializable;

/**
 * Created by 余昌鸿 on 2018/10/17.
 */
public class Org implements Serializable {

    private static final long serialVersionUID = -1L;

    private int id; // required

    private boolean tls; // required

    private String domainName;

    private String mspId; // required

    private int leagueId; // required

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isTls() {
        return tls;
    }

    public void setTls(boolean tls) {
        this.tls = tls;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public String getMspId() {
        return mspId;
    }

    public void setMspId(String mspId) {
        this.mspId = mspId;
    }

    public int getLeagueId() {
        return leagueId;
    }

    public void setLeagueId(int leagueId) {
        this.leagueId = leagueId;
    }

}
