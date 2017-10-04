package com.xqdd.coolweather.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by 10324 on 2017/10/4.
 */

public class WeatherDaily {
    public Location location;

    @SerializedName("daily")
    public List<Daily> dailyList;

    @SerializedName("last_update")
    public String lastUpdate;
}
