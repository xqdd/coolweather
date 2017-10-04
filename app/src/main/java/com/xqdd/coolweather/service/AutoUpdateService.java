package com.xqdd.coolweather.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.xqdd.coolweather.utils.HttpUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class AutoUpdateService extends Service {

    private final String TAG = "AutoUpdateService";

    public AutoUpdateService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        updateWeather();
        updateBingPic();
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int eightHour = 8 * 60 * 60 * 1000;
        long triggerAtTime = SystemClock.elapsedRealtime() + eightHour;
        Intent i = new Intent(this, AutoUpdateService.class);
        //获取之前的PendingIntent或者新的
        PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
        //取消之前的
        manager.cancel(pi);
        //开启新的
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        return super.onStartCommand(intent, flags, startId);
    }

    private void updateBingPic() {


        final SharedPreferences pres = PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this);
        final SharedPreferences.Editor editor = pres.edit();

        String weatherCode = pres.getString("weatherCode", null);
        String weatherNowUrl = "https://api.seniverse.com/v3/weather/now.json?key=dz8fyfnz0jjaarcn&location=" + weatherCode + "&language=zh-Hans&unit=c";
        String weatherDailyUrl = "https://api.seniverse.com/v3/weather/daily.json?key=dz8fyfnz0jjaarcn&location=" + weatherCode + "&language=zh-Hans&unit=c&start=0&days=5";
        String weatherSuggestionUrl = "https://api.seniverse.com/v3/life/suggestion.json?key=dz8fyfnz0jjaarcn&location=" + weatherCode + "&language=zh-Hans";



        if (weatherCode == null) {
            return;
        }

        /**
         * 获取今日天气
         */
        HttpUtil.sendOkHttpRequest(weatherNowUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "onFailure: ", e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String weatherNowResponse = response.body().string();
                if (!TextUtils.isEmpty(weatherNowResponse) && !weatherNowResponse.contains("status_code")) {
                    editor.putString("weatherNowResponse", weatherNowResponse);
                    editor.apply();
                }
            }
        });

        /**
         * 获取逐日天气
         */
        HttpUtil.sendOkHttpRequest(weatherDailyUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "onFailure: ", e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String weatherDailyResponse = response.body().string();
                if (!TextUtils.isEmpty(weatherDailyResponse) && !weatherDailyResponse.contains("status_code")) {
                    editor.putString("weatherDailyResponse", weatherDailyResponse);
                    editor.apply();

                }
            }
        });

        /**
         * 获取天气指数
         */
        HttpUtil.sendOkHttpRequest(weatherSuggestionUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "onFailure: ", e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String weatherSuggestionResponse = response.body().string();
                if (!TextUtils.isEmpty(weatherSuggestionResponse) && !weatherSuggestionResponse.contains("status_code")) {
                    editor.putString("weatherSuggestionResponse", weatherSuggestionResponse);
                    editor.apply();

                }
            }
        });
    }

    private void updateWeather() {
        String requestBingPic = "http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("AutoUpdateService", "onFailure: ", e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this).edit();
                editor.putString("bing_pic", response.body().string());
                editor.apply();
            }
        });
    }
}
