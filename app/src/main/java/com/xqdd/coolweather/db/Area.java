package com.xqdd.coolweather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by 10324 on 2017/10/2.
 */

public class Area extends DataSupport {
    private Integer id;
    private String code;
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
