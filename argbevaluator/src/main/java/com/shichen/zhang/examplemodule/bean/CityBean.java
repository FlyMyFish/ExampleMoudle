package com.shichen.zhang.examplemodule.bean;

/**
 * @author shichen 754314442@qq.com
 * Created by shichen on 2018/8/30.
 */
public class CityBean {
    String cityName;
    String provinceName;

    public CityBean(String cityName, String provinceName) {
        this.cityName = cityName;
        this.provinceName = provinceName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }
}
