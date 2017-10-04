package com.xqdd.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 10324 on 2017/10/4.
 */

public class Daily {
    public String date;

    public String text_day;

    public String code_day;

    public String text_night;

    public String code_night;

    public String high;

    public String low;

    public String precip;

    @SerializedName("wind_direction")
    public String windDirection;

    @SerializedName("windDirectionDegree")
    public String windDirectionDegree;

    @SerializedName("wind_speed")
    public String windSpeed;

    @SerializedName("wind_scale")
    public String windScale;
}
