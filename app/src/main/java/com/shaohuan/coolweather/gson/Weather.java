package com.shaohuan.coolweather.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 创建者： Administrator
 * <p>
 * 创建时间： 2017/6/12
 * <p>
 * 描述：
 */


public class Weather {
    public String status;
    public Basic basic;
    public AQI aqi;
    public Now now;
    public Suggestion suggestion;

    @SerializedName("daily_forecast")
    public List<Forecast> forecastList;
}
