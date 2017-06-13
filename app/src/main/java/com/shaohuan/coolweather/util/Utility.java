package com.shaohuan.coolweather.util;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.shaohuan.coolweather.db.City;
import com.shaohuan.coolweather.db.County;
import com.shaohuan.coolweather.db.Province;
import com.shaohuan.coolweather.gson.Weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 创建者： Administrator
 * <p>
 * 创建时间： 2017/6/10
 * <p>
 * 描述：
 */


public class Utility {

    public static boolean handleProvinceResponse(String response){
        if (!TextUtils.isEmpty(response)){
            try {
                JSONArray provinces = new JSONArray(response);
                for (int i=0;i<provinces.length();i++){
                    JSONObject provinceObject = provinces.getJSONObject(i);
                    Province province = new Province();
                    province.setProvinceName(provinceObject.getString("name"));
                    province.setProvinceCode(provinceObject.getInt("id"));
                    province.save();

                }
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static boolean handleCityResponse(String response,int provinceId){
        if (!TextUtils.isEmpty(response)){
            try {
                JSONArray cities = new JSONArray(response);
                for (int i=0;i<cities.length();i++){
                    JSONObject object = cities.getJSONObject(i);
                    City city = new City();
                    city.setCityName(object.getString("name"));
                    city.setCityCode(object.getInt("id"));
                    city.setProvinceId(provinceId);
                    city.save();

                }
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }


    public static boolean handleCountyResponse(String response,int cityId){
        if (!TextUtils.isEmpty(response)){
            try {
                JSONArray counties = new JSONArray(response);
                for (int i=0;i<counties.length();i++){
                    JSONObject object = counties.getJSONObject(i);
                    County county = new County();
                    county.setCountyName(object.getString("name"));
                    county.setWeatherId(object.getString("weather_id"));
                    county.setCityId(cityId);
                    county.save();

                }
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }


    public static Weather handleWeatherResponse(String response){
        try {
            JSONObject object = new JSONObject(response);
            JSONArray jsonArray = object.getJSONArray("HeWeather");
            //该jsonArray就一个对象，转换成string用Gson解析
            String weatherContent = jsonArray.getJSONObject(0).toString();
            return new Gson().fromJson(weatherContent,Weather.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
