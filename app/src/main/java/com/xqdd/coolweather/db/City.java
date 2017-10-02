package com.xqdd.coolweather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by 10324 on 2017/10/2.
 */

public class City extends DataSupport {
    private Integer id;
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
