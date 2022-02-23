package com.fish1208.sdk.entity;

import java.io.Serializable;

/**
 * Created by 余昌鸿 on 2018/10/17.
 */
public class League implements Serializable {

    private static final long serialVersionUID = -1L;

    private int id; // required

    private String name; // required

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
