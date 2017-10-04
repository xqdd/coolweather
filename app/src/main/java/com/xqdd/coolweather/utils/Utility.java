package com.xqdd.coolweather.utils;

import android.text.TextUtils;
import android.util.Log;

import com.xqdd.coolweather.db.Area;
import com.xqdd.coolweather.db.City;
import com.xqdd.coolweather.db.Province;

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
                    area.setAreaId(areaObject.getInt("id"));
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


}
