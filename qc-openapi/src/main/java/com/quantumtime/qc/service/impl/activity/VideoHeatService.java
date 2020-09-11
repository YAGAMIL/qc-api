package com.quantumtime.qc.service.impl.activity;

import com.quantumtime.qc.entity.activity.HeatRule;

/**
 * <p>
 * Description:
 * Program:qc-api
 * </p>
 * Created on 2019-12-19 15:31
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
public interface VideoHeatService {
    void sortHeatVideos();

    void makeHeat(Long activityId);

    void doMakeHeat(HeatRule heatRule);
}
