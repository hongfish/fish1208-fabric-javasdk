package com.fish1208.sdk.entity;

import org.springframework.core.io.Resource;

import java.io.Serializable;

/**
 * Created by 余昌鸿 on 2018/10/17.
 */
public class CA implements Serializable {

    private static final long serialVersionUID = -1L;

    private String name;

    private Resource sk;

    private Resource certificate;

    private String flag; // optional

    private int peerId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Resource getSk() {
        return sk;
    }

    public void setSk(Resource sk) {
        this.sk = sk;
    }

    public Resource getCertificate() {
        return certificate;
    }

    public void setCertificate(Resource certificate) {
        this.certificate = certificate;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public int getPeerId() {
        return peerId;
    }

    public void setPeerId(int peerId) {
        this.peerId = peerId;
    }

}
