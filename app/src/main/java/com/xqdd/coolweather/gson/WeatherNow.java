package com.xqdd.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 10324 on 2017/10/4.
 */

public class WeatherNow {
    public Location location;
    public Now now;
    @SerializedName("last_update")
    public String lastUpdate;
}
