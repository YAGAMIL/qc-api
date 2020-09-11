package com.quantumtime.qc.jobhandler;

import com.quantumtime.qc.service.IVideoService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@JobHandler(value="videoStatusHandler")
@Component
public class VideoStatusHandler extends IJobHandler {
    @Autowired
    private IVideoService videoService;
    @Override
    public ReturnT<String> execute(String param) throws Exception {
        String response = videoService.checkVideoStatus();
        XxlJobLogger.log(response);
        return SUCCESS;
    }
}
