package com.mrsun.coolweather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by MrSun on 2017/3/28.
 * 城市实体类
 */

public class City extends DataSupport {
    //城市ID
    private int id;
    //城市名字
    private String cityName;
    //城市代号
    private int cityCode;
    //省份Id
    private int provinceId;

    public void setId(int id) {
        this.id = id;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public void setCityCode(int cityCode) {
        this.cityCode = cityCode;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    public int getId() {
        return id;
    }

    public String getCityName() {
        return cityName;
    }

    public int getCityCode() {
        return cityCode;
    }

    public int getProvinceId() {
        return provinceId;
    }
}
