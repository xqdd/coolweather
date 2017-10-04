package com.xqdd.coolweather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.xqdd.coolweather.gson.Daily;
import com.xqdd.coolweather.gson.Suggestion;
import com.xqdd.coolweather.gson.Weather;
import com.xqdd.coolweather.utils.HttpUtil;
import com.xqdd.coolweather.utils.Utility;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class WeatherActivity extends AppCompatActivity {


    private ImageView bingPicImg;
    private ScrollView weatherLayout;
    private TextView titleCity;
    private TextView titleUpdateTime;
    private TextView degreeText;
    private TextView weatherKInfoText;
    private LinearLayout forecastLayout;
    private TextView aqiText;
    private TextView pm25Text;
    private TextView carWashText;
    private TextView sportText;
    private TextView dressingText;
    private TextView fluText;
    private TextView travelText;
    private TextView uvText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            //设置Activity全屏
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            //设置状态栏为透明
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        setContentView(R.layout.activity_weather);

        bingPicImg = findViewById(R.id.bing_pic_img);
        weatherLayout = findViewById(R.id.weather_layout);
        titleCity = findViewById(R.id.title_city);
        titleUpdateTime = findViewById(R.id.title_update_time);
        degreeText = findViewById(R.id.degree_text);
        weatherKInfoText = findViewById(R.id.weather_info_text);
        forecastLayout = findViewById(R.id.forecast_layout);
        aqiText = findViewById(R.id.aqi_text);
        pm25Text = findViewById(R.id.pm25_text);
        dressingText = findViewById(R.id.dressing_text);
        sportText = findViewById(R.id.sport_text);
        carWashText = findViewById(R.id.car_washing_text);
        uvText = findViewById(R.id.uv_text);
        fluText = findViewById(R.id.flu_text);
        travelText = findViewById(R.id.travel_text);


        SharedPreferences prefs = getDefaultSharedPreferences(this);

        String bingPic = prefs.getString("bing_pic", null);
        if (bingPic != null) {
            Glide.with(this).load(bingPic).into(bingPicImg);
        } else {
            loadBingPic();
        }

        Weather weather = Utility.handleWeatherInfo(prefs.getString("weatherNowResponse", null),
                prefs.getString("weatherSuggestionResponse", null),
                prefs.getString("weatherDailyResponse", null));


        if (weather == null) {
            String weatherCode = getIntent().getStringExtra("weatherCode");
            weatherLayout.setVisibility(View.INVISIBLE);
            requestWeather(weatherCode);
        } else {
            showWeatherInfo(weather);
        }

    }

    private void loadBingPic() {
        String requestBingPic = "http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("WeatherActivity", "onFailure: ", e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPic = response.body().string();
                SharedPreferences.Editor editor = PreferenceManager
                        .getDefaultSharedPreferences(WeatherActivity.this).edit();
                editor.putString("bing_pic", bingPic);
                editor.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(WeatherActivity.this).load(bingPic).into(bingPicImg);
                    }
                });

            }
        });


    }

    private void requestWeather(String weatherCode) {
        String weatherNowUrl = "https://api.seniverse.com/v3/weather/now.json?key=dz8fyfnz0jjaarcn&location=" + weatherCode + "&language=zh-Hans&unit=c";
        String weatherDailyUrl = "https://api.seniverse.com/v3/weather/daily.json?key=dz8fyfnz0jjaarcn&location=" + weatherCode + "&language=zh-Hans&unit=c&start=0&days=5";
        String weatherSuggestionUrl = "https://api.seniverse.com/v3/life/suggestion.json?key=dz8fyfnz0jjaarcn&location=" + weatherCode + "&language=zh-Hans";


        /**
         * 获取今日天气
         */
        HttpUtil.sendOkHttpRequest(weatherNowUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String weatherNowResponse = response.body().string();
                if (!TextUtils.isEmpty(weatherNowResponse) && !weatherNowResponse.contains("status_code")) {
                    SharedPreferences pres = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this);
                    SharedPreferences.Editor editor = pres.edit();
                    editor.putString("weatherNowResponse", weatherNowResponse);
                    editor.apply();
                    final String weatherDailyResponse = (pres.getString("weatherDailyResponse", null));
                    final String weatherSuggestionResponse = (pres.getString("weatherSuggestionResponse", null));
                    if (weatherDailyResponse != null && weatherSuggestionResponse != null) {
                        runOnUiThread(weatherNowResponse, weatherSuggestionResponse, weatherDailyResponse);
                    }
                } else {
                    showGetWeatherInfoFailedToast();
                }
            }
        });

        /**
         * 获取逐日天气
         */
        HttpUtil.sendOkHttpRequest(weatherDailyUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String weatherDailyResponse = response.body().string();
                if (!TextUtils.isEmpty(weatherDailyResponse) && !weatherDailyResponse.contains("status_code")) {
                    SharedPreferences pres = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this);
                    SharedPreferences.Editor editor = pres.edit();
                    editor.putString("weatherDailyResponse", weatherDailyResponse);
                    editor.apply();
                    final String weatherNowResponse = (pres.getString("weatherNowResponse", null));
                    final String weatherSuggestionResponse = (pres.getString("weatherSuggestionResponse", null));
                    if (weatherNowResponse != null && weatherSuggestionResponse != null) {
                        runOnUiThread(weatherNowResponse, weatherSuggestionResponse, weatherDailyResponse);
                    }
                } else {
                    showGetWeatherInfoFailedToast();
                }
            }
        });

        /**
         * 获取天气指数
         */
        HttpUtil.sendOkHttpRequest(weatherSuggestionUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String weatherSuggestionResponse = response.body().string();
                if (!TextUtils.isEmpty(weatherSuggestionResponse) && !weatherSuggestionResponse.contains("status_code")) {
                    SharedPreferences pres = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this);
                    SharedPreferences.Editor editor = pres.edit();
                    editor.putString("weatherSuggestionResponse", weatherSuggestionResponse);
                    editor.apply();
                    final String weatherDailyResponse = (pres.getString("weatherDailyResponse", null));
                    final String weatherNowResponse = (pres.getString("weatherNowResponse", null));
                    if (weatherDailyResponse != null && weatherNowResponse != null) {
                        runOnUiThread(weatherNowResponse, weatherSuggestionResponse, weatherDailyResponse);
                    }
                } else {
                    showGetWeatherInfoFailedToast();
                }
            }
        });

        loadBingPic();


    }

    private void runOnUiThread(final String weatherNowResponse, final String weatherSuggestionResponse, final String weatherDailyResponse) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Weather weather = Utility.handleWeatherInfo(weatherNowResponse,
                        weatherSuggestionResponse, weatherDailyResponse);
                showWeatherInfo(weather);
            }
        });
    }

    private void showWeatherInfo(Weather weather) {
        String cityName = weather.getWeatherNow().location.name;
        String updateTime = weather.getWeatherNow().lastUpdate;

        updateTime = updateTime.substring(11, 16);

        String degree = weather.getWeatherNow().now.temperature + "°C";
        String weatherInfo = weather.getWeatherNow().now.text;
        titleCity.setText(cityName);
        titleUpdateTime.setText(updateTime);
        degreeText.setText(degree);
        weatherKInfoText.setText(weatherInfo);
        forecastLayout.removeAllViews();
        List<Daily> dailyList = weather.getWeatherDaily().dailyList;
        for (int i = 1; i < dailyList.size(); i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.forecast_item,
                    forecastLayout, false);
            TextView dateText = view.findViewById(R.id.date_text);
            TextView infoText = view.findViewById(R.id.info_text);
            TextView maxText = view.findViewById(R.id.max_text);
            TextView minText = view.findViewById(R.id.min_text);
            Daily daily = dailyList.get(i);

            dateText.setText(daily.date);
            infoText.setText(daily.text_day);
            maxText.setText(daily.high);
            minText.setText(daily.low);

            forecastLayout.addView(view);
        }
        Suggestion suggestion = weather.getWeatherSuggestion().suggestion;
        String dress = "穿衣：" + suggestion.dressing.brief;
        String carWash = "洗车：" + suggestion.carWashing.brief;
        String travel = "旅行：" + suggestion.travel.brief;
        String sport = "运动：" + suggestion.sport.brief;
        String flu = "流感：" + suggestion.flu.brief;
        String uv = "紫外线：" + suggestion.ultravioletRay.brief;

        dressingText.setText(dress);
        carWashText.setText(carWash);
        travelText.setText(travel);
        sportText.setText(sport);
        fluText.setText(flu);
        uvText.setText(uv);

        weatherLayout.setVisibility(View.VISIBLE);
    }

    public void showGetWeatherInfoFailedToast() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(WeatherActivity.this, "暂不支持此地区的天气信息，请重新选择", Toast.LENGTH_SHORT).show();
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                editor.remove("weatherCode");
                editor.apply();
                startActivity(new Intent(WeatherActivity.this, MainActivity.class));
                finish();
            }
        });
    }
}
