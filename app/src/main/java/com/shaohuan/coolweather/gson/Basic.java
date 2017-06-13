package com.shaohuan.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * 创建者： Administrator
 * <p>
 * 创建时间： 2017/6/12
 * <p>
 * 描述：
 */


public class Basic {

    @SerializedName("city")  //gson提供功能：使city和cityName之间产生映射
    public String cityName;

    @SerializedName("id")
    public String weatherId;

    public Update update;

    public class Update{
       @SerializedName("loc")
        public String updateTime;
    }
}
