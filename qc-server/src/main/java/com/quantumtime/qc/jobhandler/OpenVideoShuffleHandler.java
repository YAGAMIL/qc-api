package com.quantumtime.qc.jobhandler;

import com.quantumtime.qc.service.IVideoService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@JobHandler(value="openVideoShuffleHandler")
@Component
public class OpenVideoShuffleHandler extends IJobHandler {
    @Autowired
    private IVideoService videoService;
    @Override
    public ReturnT<String> execute(String param) throws Exception {
        videoService.shuffleOpenVideos(param);
        XxlJobLogger.log("上传视频刷新成功，param:" + param);
        return SUCCESS;
    }
}
