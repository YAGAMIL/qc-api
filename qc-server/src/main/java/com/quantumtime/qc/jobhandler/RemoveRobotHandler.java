package com.quantumtime.qc.jobhandler;
import com.quantumtime.qc.service.ClickContentService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
@JobHandler(value="removeRobotHandler")
@Component
public class RemoveRobotHandler extends IJobHandler {
    @Autowired
    private ClickContentService clickContentService;

    @Override
    public ReturnT<String> execute(String param) throws Exception {
       clickContentService.removeRobot();
        return SUCCESS;
    }}
