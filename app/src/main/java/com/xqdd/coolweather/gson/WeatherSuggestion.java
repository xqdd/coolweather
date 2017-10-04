package com.xqdd.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 10324 on 2017/10/4.
 */

public class WeatherSuggestion {
    public Location location;

    public Suggestion suggestion;

    @SerializedName("last_update")
    public String lastUpdate;
}
