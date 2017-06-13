package com.shaohuan.coolweather.gson;

/**
 * 创建者： Administrator
 * <p>
 * 创建时间： 2017/6/12
 * <p>
 * 描述：
 */


public class AQI {
    public AQICity city;
    public class AQICity{
        public String aqi;
        public String pm25;
    }
}
