package com.quantumtime.qc.service;

import com.quantumtime.qc.entity.activity.Activity;
import com.quantumtime.qc.entity.poi.Address;
import com.quantumtime.qc.vo.ActivityDetailVo;
import com.quantumtime.qc.vo.PoiIdAndActivityVo;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Description: 活动业务层接口 Program:qc-api Created on 2019-12-13 15:47
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
public interface ActivityService {

    Activity activityList( Address address);

    ActivityDetailVo activityDetail(String poiId );


    List<PoiIdAndActivityVo> nearbyActivity(List<String> poiId);


    List<Activity> queryActive(LocalDateTime now);
}
