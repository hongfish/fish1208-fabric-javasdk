package com.fish1208.sdk.entity;

import java.io.Serializable;

/**
 * Created by 余昌鸿 on 2018/10/17.
 */
public class Chaincode implements Serializable {

    private static final long serialVersionUID = -1L;

    private String name; // required

    private String source; // optional

    private String path; // optional

    private String policy; // optional

    private String version; // required

    private int proposalWaitTime = 90000; // required

    private int channelId; // required

    private String cc; // optional

    private boolean chaincodeEventListener; // required

    private String callbackLocation; // required

    private String events;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPolicy() {
        return policy;
    }

    public void setPolicy(String policy) {
        this.policy = policy;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getProposalWaitTime() {
        return proposalWaitTime;
    }

    public void setProposalWaitTime(int proposalWaitTime) {
        this.proposalWaitTime = proposalWaitTime;
    }

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public boolean isChaincodeEventListener() {
        return chaincodeEventListener;
    }

    public void setChaincodeEventListener(boolean chaincodeEventListener) {
        this.chaincodeEventListener = chaincodeEventListener;
    }

    public String getCallbackLocation() {
        return callbackLocation;
    }

    public void setCallbackLocation(String callbackLocation) {
        this.callbackLocation = callbackLocation;
    }

    public String getEvents() {
        return events;
    }

    public void setEvents(String events) {
        this.events = events;
    }

}
