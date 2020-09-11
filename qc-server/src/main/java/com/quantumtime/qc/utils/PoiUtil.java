package com.quantumtime.qc.utils;

import com.quantumtime.qc.entity.poi.Address;
import com.quantumtime.qc.entity.poi.Business;
import com.quantumtime.qc.service.IBusinessService;
import com.quantumtime.qc.vo.recommend.Poi;

import java.util.ArrayList;
import java.util.List;

public class PoiUtil {

    public static List<Poi> createPoiList(Address address, IBusinessService businessService) {
        List<Poi> poiList = new ArrayList<>();
        //最小Poi
        Poi detailPoi = new Poi();
        detailPoi.setPoiId(address.getPoiId());
        detailPoi.setPoiName(address.getPoiName());
        detailPoi.setPoiType("poi");
        poiList.add(detailPoi);
        //街道乡镇
        Poi townPoi = new Poi();
        townPoi.setPoiId(address.getTownCode());
        townPoi.setPoiName(address.getTownship());
        townPoi.setPoiType("town_code");
        poiList.add(townPoi);
        //区
        Poi districtPoi = new Poi();
        districtPoi.setPoiId(address.getAdCode());
        districtPoi.setPoiName(address.getDistrict());
        districtPoi.setPoiType("district_code");
        poiList.add(districtPoi);
        //市
        Poi cityPoi = new Poi();
        cityPoi.setPoiId(address.getCityCode());
        cityPoi.setPoiName(address.getCity());
        cityPoi.setPoiType("city_code");
        poiList.add(cityPoi);
        //省
        Poi provincePoi = new Poi();
        provincePoi.setPoiId(address.getProvinceCode());
        provincePoi.setPoiName(address.getProvince());
        provincePoi.setPoiType("province_code");
        poiList.add(provincePoi);
        //国
        Poi countryPoi = new Poi();
        countryPoi.setPoiId(address.getCountryCode());
        countryPoi.setPoiName(address.getCountry());
        countryPoi.setPoiType("country_code");
        poiList.add(countryPoi);
        //商圈
        List<Business> businessList = businessService.getAllByPoiId(address.getPoiId());
        if (businessList != null && businessList.size() > 0) {
            for (Business business : businessList) {
                Poi businessPoi = new Poi();
                businessPoi.setPoiType("business_code");
                businessPoi.setPoiId(business.getAdCode());
                businessPoi.setPoiName(business.getBusinessArea());
                poiList.add(businessPoi);
            }
        }
        return poiList;
    }
}
