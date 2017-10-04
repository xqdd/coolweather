package com.xqdd.coolweather.utils;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.xqdd.coolweather.db.Area;
import com.xqdd.coolweather.db.City;
import com.xqdd.coolweather.db.Province;
import com.xqdd.coolweather.gson.Weather;
import com.xqdd.coolweather.gson.WeatherDaily;
import com.xqdd.coolweather.gson.WeatherNow;
import com.xqdd.coolweather.gson.WeatherSuggestion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by 10324 on 2017/10/2.
 */

public class Utility {
    private static final String TAG = "Utility";

    /**
     * 解析和处理服务器返回的省级数据
     */

    public static boolean handleProvinceResponse(String response) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONObject responseObject = new JSONObject(response);
                JSONArray allProvinces = responseObject.getJSONArray("data");
                for (int i = 0; i < allProvinces.length(); i++) {
                    JSONObject provinceObject = allProvinces.getJSONObject(i);
                    Province province = new Province();
                    province.setName(provinceObject.getString("name"));
                    province.setProvinceId(provinceObject.getInt("id"));
                    province.save();
                }
                return true;
            } catch (JSONException e) {
                Log.d("Utility", "handleProvinceResponse: " + response);
                Log.e(TAG, "handleProvinceResponse: ", e);
            }

        }
        return false;
    }


    /**
     * 解析和处理服务器返回的城市数据
     */

    public static boolean handleCityResponse(String response, Integer provinceId) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONObject responseObject = new JSONObject(response);
                JSONArray cityArray = responseObject.getJSONArray("data");
                for (int i = 0; i < cityArray.length(); i++) {
                    JSONObject cityObject = cityArray.getJSONObject(i);
                    City city = new City();
                    city.setProvinceId(provinceId);
                    city.setName(cityObject.getString("name"));
                    city.setCityId(cityObject.getInt("id"));
                    city.save();
                }
                return true;
            } catch (JSONException e) {
                Log.d("Utility", "handleCityResponse: " + response);
                Log.e(TAG, "handleCityResponse: ", e);
            }
        }
        return false;
    }


    /**
     * 解析和处理服务器返回的县区数据
     */

    public static boolean handleAreaResponse(String response, Integer cityId) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONObject responseObject = new JSONObject(response);
                JSONArray areaArray = responseObject.getJSONArray("data");
                for (int i = 0; i < areaArray.length(); i++) {
                    JSONObject areaObject = areaArray.getJSONObject(i);
                    Area area = new Area();
                    area.setCityId(cityId);
                    area.setName(areaObject.getString("name"));
                    area.setCode(areaObject.getString("code"));
                    area.save();
                }
                return true;
            } catch (JSONException e) {
                Log.d("Utility", "handleAreaResponse: " + response);
                Log.e(TAG, "handleAreaResponse: ", e);
            }
        }
        return false;
    }


    public static WeatherNow handleWeatherNowResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("results");
            String resultContent = jsonArray.getJSONObject(0).toString();
            return new Gson().fromJson(resultContent, WeatherNow.class);

        } catch (JSONException e) {
            Log.e(TAG, "handleWeatherNowResponse: ", e);
        }
        return null;
    }

    public static WeatherDaily handleWeatherDailyResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("results");
            String resultContent = jsonArray.getJSONObject(0).toString();
            return new Gson().fromJson(resultContent, WeatherDaily.class);

        } catch (JSONException e) {
            Log.e(TAG, "handleWeatherNowResponse: ", e);
        }
        return null;
    }

    public static WeatherSuggestion handleWeatherSuggestionResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("results");
            String resultContent = jsonArray.getJSONObject(0).toString();
            return new Gson().fromJson(resultContent, WeatherSuggestion.class);

        } catch (JSONException e) {
            Log.e(TAG, "handleWeatherNowResponse: ", e);
        }
        return null;
    }


    public static Weather handleWeatherInfo(String weatherNowResponse,
                                            String weatherSuggestionResponse,
                                            String weatherDailyResponse) {
        if (weatherDailyResponse == null || weatherNowResponse == null || weatherSuggestionResponse == null) {
            return null;
        }
        return new Weather(handleWeatherNowResponse(weatherNowResponse),
                handleWeatherSuggestionResponse(weatherSuggestionResponse),
                handleWeatherDailyResponse(weatherDailyResponse));
    }
}
