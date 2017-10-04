package com.xqdd.coolweather.gson;

/**
 * Created by 10324 on 2017/10/4.
 */

public class Weather {

    public Weather(WeatherNow weatherNow, WeatherSuggestion weatherSuggestion, WeatherDaily weatherDaily) {
        this.weatherDaily = weatherDaily;
        this.weatherSuggestion = weatherSuggestion;
        this.weatherNow = weatherNow;
    }

    private WeatherDaily weatherDaily;
    private WeatherSuggestion weatherSuggestion;
    private WeatherNow weatherNow;

    public WeatherDaily getWeatherDaily() {
        return weatherDaily;
    }

    public void setWeatherDaily(WeatherDaily weatherDaily) {
        this.weatherDaily = weatherDaily;
    }

    public WeatherSuggestion getWeatherSuggestion() {
        return weatherSuggestion;
    }

    public void setWeatherSuggestion(WeatherSuggestion weatherSuggestion) {
        this.weatherSuggestion = weatherSuggestion;
    }

    public WeatherNow getWeatherNow() {
        return weatherNow;
    }

    public void setWeatherNow(WeatherNow weatherNow) {
        this.weatherNow = weatherNow;
    }
}
