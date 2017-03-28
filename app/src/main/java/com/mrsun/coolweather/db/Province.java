package com.mrsun.coolweather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by MrSun on 2017/3/28.
 * 省份实体类
 */

public class Province extends DataSupport {
    //id
    private int id;
    //省份名字
    private String provinceName;
    //编码
    private int provinceCode;

    public void setId(int id) {
        this.id = id;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public void setProvinceCode(int provinceCode) {
        this.provinceCode = provinceCode;
    }

    public int getId() {
        return id;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public int getProvinceCode() {
        return provinceCode;
    }
}
