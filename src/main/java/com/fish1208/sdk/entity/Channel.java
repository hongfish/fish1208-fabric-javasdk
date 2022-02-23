package com.fish1208.sdk.entity;

import java.io.Serializable;

/**
 * Created by 余昌鸿 on 2018/10/17.
 */
public class Channel implements Serializable {

    private static final long serialVersionUID = -1L;

    private String name; // required

    private boolean blockListener; // required

    private String callbackLocation; // required

    private int peerId; // required

    private int height;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isBlockListener() {
        return blockListener;
    }

    public void setBlockListener(boolean blockListener) {
        this.blockListener = blockListener;
    }

    public String getCallbackLocation() {
        return callbackLocation;
    }

    public void setCallbackLocation(String callbackLocation) {
        this.callbackLocation = callbackLocation;
    }

    public int getPeerId() {
        return peerId;
    }

    public void setPeerId(int peerId) {
        this.peerId = peerId;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
