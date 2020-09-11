package com.quantumtime.qc.jobhandler;

import com.quantumtime.qc.service.impl.activity.VideoHeatService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Description:视频热度排序 Program:qc-api Created on 2019-12-19 16:36
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
@JobHandler(value = "VideoHeatSort")
@Component
public class HeatSorterHandler extends IJobHandler {

    @Resource
    private VideoHeatService videoHeatService;
    /**
     * execute handler, invoked when executor receives a scheduling request
     *
     * @param param 定时参数
     * @return 处理结果
     */
    @Override
    public ReturnT<String> execute(String param) {
        videoHeatService.sortHeatVideos();
        return ReturnT.SUCCESS;
    }
}
