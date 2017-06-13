package com.shaohuan.coolweather;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.shaohuan.coolweather.gson.Forecast;
import com.shaohuan.coolweather.gson.Weather;
import com.shaohuan.coolweather.util.HttpUtil;
import com.shaohuan.coolweather.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {

    private ScrollView weatherLayout;
    private TextView titleCity;
    private TextView updateTime;
    private TextView degree;
    private TextView weatherInfo;
    private LinearLayout forecastLayout;
    private TextView aqi;
    private TextView pm25;
    private TextView comfort;
    private TextView carWash;
    private TextView sport;
    private ImageView imageView;
    public SwipeRefreshLayout swipeRefresh;
    private String weatherId;
    public DrawerLayout drawerLayout;
    private Button btNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Android5.0以上版本适用
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_weather);
        //初始化控件
        initUI();

        btNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String bing_pic = prefs.getString("bing_pic", null);
        if (bing_pic != null) {
            Glide.with(this).load(bing_pic).into(imageView);
        } else {
            loadBingPic();
        }

        String weatherString = prefs.getString("weather", null);
        if (weatherString != null) {
            //从缓存中直接解析天气数据
            Weather weather = Utility.handleWeatherResponse(weatherString);
            weatherId = weather.basic.weatherId;
            showWeatherInfo(weather);
        } else {
            //无缓存从服务器获取天气信息
            weatherId = getIntent().getStringExtra("weather_id");
            weatherLayout.setVisibility(View.INVISIBLE);
            //Log.e("weather",weatherId);//CN101220202
            requestWeather(weatherId);
        }
        //下拉刷新，更新天气信息
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                String weatherString = prefs.getString("weather", null);
                Weather weather = Utility.handleWeatherResponse(weatherString);
                weatherId=weather.basic.weatherId;
                requestWeather(weatherId);
            }
        });


    }

    private void loadBingPic() {
        String requestBingPic = "http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //获取到背景图片地址
                final String bingPicUrl = response.body().string();
                SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                edit.putString("bing_pic", bingPicUrl);
                edit.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(WeatherActivity.this).load(bingPicUrl).into(imageView);
                    }
                });
            }
        });
    }

    public void requestWeather(final String weatherId) {
        String weatherUrl = "http://guolin.tech/api/weather?cityid="
                + weatherId + "&key=bc0418b57b2d4918819d3974ac1285d9";

        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();

                        //隐藏下拉刷新图标
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                Log.e("responseText", responseText);

                final Weather weather = Utility.handleWeatherResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather != null && "ok".equals(weather.status)) {
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                            editor.putString("weather", responseText);
                            editor.apply();
                            showWeatherInfo(weather);

                        } else {
                            Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                        }
                        //隐藏下拉刷新图标
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        });
        //每次刷新天气信息时都更新背景图片
        loadBingPic();
    }

    private void initUI() {
        weatherLayout = (ScrollView) findViewById(R.id.sv_weather);
        titleCity = (TextView) findViewById(R.id.tv_city_name);
        updateTime = (TextView) findViewById(R.id.tv_update_time);
        degree = (TextView) findViewById(R.id.tv_degree);
        weatherInfo = (TextView) findViewById(R.id.tv_weather_info);
        forecastLayout = (LinearLayout) findViewById(R.id.ll_forecast);
        aqi = (TextView) findViewById(R.id.tv_aqi);
        pm25 = (TextView) findViewById(R.id.tv_pm25);
        comfort = (TextView) findViewById(R.id.tv_comfort);
        carWash = (TextView) findViewById(R.id.tv_wash_car);
        sport = (TextView) findViewById(R.id.tv_sport);
        imageView = (ImageView) findViewById(R.id.iv_background);
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        btNav = (Button) findViewById(R.id.bt_nav);
    }

    private void showWeatherInfo(Weather weather) {
        String cityName = weather.basic.cityName;
        String updateTime = weather.basic.update.updateTime.split(" ")[1];
        String degree = weather.now.temperature + "℃";
        String weatherInfo = weather.now.more.info;
        titleCity.setText(cityName);
        this.updateTime.setText(updateTime);
        this.degree.setText(degree);
        this.weatherInfo.setText(weatherInfo);

        forecastLayout.removeAllViews();
        for (Forecast forecast : weather.forecastList) {
            //动态添加
            View view = LayoutInflater.from(this).inflate(R.layout.forecast_item, forecastLayout, false);
            TextView date = (TextView) view.findViewById(R.id.tv_date);
            TextView info = (TextView) view.findViewById(R.id.tv_info);
            TextView max = (TextView) view.findViewById(R.id.tv_max);
            TextView min = (TextView) view.findViewById(R.id.tv_min);
            date.setText(forecast.date);
            info.setText(forecast.more.info);
            max.setText(forecast.temperature.max);
            min.setText(forecast.temperature.min);
            forecastLayout.addView(view);
        }
        if (weather.aqi != null) {
            aqi.setText(weather.aqi.city.aqi);
            pm25.setText(weather.aqi.city.pm25);
        }
        String comfort = "舒适度：" + weather.suggestion.comfort.info;
        String carWash = "洗车指数：" + weather.suggestion.carWash.info;
        String sport = "运动指数：" + weather.suggestion.sport.info;
        this.comfort.setText(comfort);
        this.carWash.setText(carWash);
        this.sport.setText(sport);

        weatherLayout.setVisibility(View.VISIBLE);

    }

}
