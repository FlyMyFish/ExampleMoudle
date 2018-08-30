package com.shichen.zhang.examplemodule.utils;

import android.content.Context;

import com.shichen.zhang.examplemodule.bean.CityBean;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * @author shichen 754314442@qq.com
 * Created by shichen on 2018/8/30.
 */
public class LocalUtil {
    //所有国家名称List
    private static final List<String> COUNTRY_REGION = new ArrayList<String>();
    private static LocalUtil localutil;
    private SAXReader reader;
    private Document document;
    private Element rootElement;        //根元素

    //初始化
    private LocalUtil(Context context) {
        //1.读取
        reader = new SAXReader();
        try {
            document = reader.read(context.getAssets().open("LocList.xml"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        //2.获得根元素
        rootElement = document.getRootElement();
        //3.初始化所有国家名称列表
        Iterator it = rootElement.elementIterator();
        Element ele = null;
        while (it.hasNext()) {
            ele = (Element) it.next();
            COUNTRY_REGION.add(ele.attributeValue("Name"));
        }
    }

    /**
     * @return String[]
     * @author LiuJinan
     * @TODO 功能： 获取所有国家名称
     * @time 2016-8-26 上午9:02:05
     */
    public List<String> getCountry() {
        return COUNTRY_REGION;
    }

    /**
     * @param countryName 国家名，从getCountry()从取出
     * @return List<Element>
     * @author LiuJinan
     * @TODO 功能： 根据国家名获取该国所有省份
     * @time 2016-8-26 上午9:07:21
     */
    private List<Element> provinces(String countryName) {
        Iterator it = rootElement.elementIterator();
        List<Element> provinces = new ArrayList<Element>();
        Element ele = null;
        while (it.hasNext()) {
            ele = (Element) it.next();
            COUNTRY_REGION.add(ele.attributeValue("Name"));
            if (ele.attributeValue("Name").equals(countryName)) {
                provinces = ele.elements();
                break;
            }
        }
        return provinces;
    }

    /**
     * @param countryName 国家名，从getCountry()从取出
     * @return List<Element>
     * @author LiuJinan
     * @TODO 功能： 根据国家名获取该国所有省份
     * @time 2016-8-26 上午9:07:21
     */
    public List<String> getProvinces(String countryName) {
        List<Element> tmp = this.provinces(countryName);
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < tmp.size(); i++) {
            list.add(tmp.get(i).attributeValue("Name"));
        }
        return list;
    }

    /**
     * @param countryName
     * @param provinceName
     * @return
     * @author LiuJinan
     * @TODO 功能：根据国家名和省份名，获取该省城市名列表
     * @time 2016-8-26 上午9:15:24
     */
    private List<Element> cities(String countryName, String provinceName) {
        List<Element> provinces = this.provinces(countryName);
        List<Element> cities = new ArrayList<Element>();
        if (provinces == null || provinces.size() == 0) {     //没有这个城市
            return cities;
        }

        for (int i = 0; i < provinces.size(); i++) {
            if (provinces.get(i).attributeValue("Name").equals(provinceName)) {
                cities = provinces.get(i).elements();
                break;
            }
        }
        return cities;
    }

    /**
     * @param countryName
     * @param provinceName
     * @return List<String>
     * @author LiuJinan
     * @TODO 功能：根据国家名和省份名获取城市列表
     * @time 2016-8-26 下午4:55:55
     */
    public List<CityBean> getCities(String countryName, String provinceName) {
        List<Element> tmp = this.cities(countryName, provinceName);
        List<CityBean> cities = new ArrayList<>();
        for (int i = 0; i < tmp.size(); i++) {
            cities.add(new CityBean(tmp.get(i).attributeValue("Name"), provinceName));
        }
        return cities;
    }

    /**
     * 获取所有城市
     * @return
     */
    public List<CityBean> getAllCity() {
        List<String> provinces = getProvinces("中国");
        List<CityBean> allCity = new ArrayList<>();
        for (int i = 0; i < provinces.size(); i++) {
            allCity.addAll(getCities("中国", provinces.get(i)));
        }
        return allCity;
    }

    public static LocalUtil getInstance(Context context) {
        if (localutil == null) {
            localutil = new LocalUtil(context);
        }
        return localutil;
    }
}
