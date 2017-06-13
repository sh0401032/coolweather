package com.shaohuan.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * 创建者： Administrator
 * <p>
 * 创建时间： 2017/6/12
 * <p>
 * 描述：
 */


public class Now {
    @SerializedName("tmp")
    public String temperature;

    @SerializedName("cond")
    public More more;

    public class More{
        @SerializedName("txt")
        public String info;
    }
}
