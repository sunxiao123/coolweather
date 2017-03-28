package com.mrsun.coolweather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by MrSun on 2017/3/28.
 * 县实体类
 */

public class County extends DataSupport {
    //id
    private int id;
    //县的名字
    private String countyName;
    //天气Id
    private String weatherId;
    //城市Id
    private int cityId;

    public void setId(int id) {
        this.id = id;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public int getId() {
        return id;
    }

    public String getCountyName() {
        return countyName;
    }

    public String getWeatherId() {
        return weatherId;
    }

    public int getCityId() {
        return cityId;
    }
}
