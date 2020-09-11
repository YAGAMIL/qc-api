package com.quantumtime.qc.jobhandler;
import com.alibaba.fastjson.JSON;
import com.quantumtime.qc.service.ClickContentService;
import com.quantumtime.qc.vo.RobotClickVo;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
@JobHandler(value="robotClickHandler")
@Component
public class RobotClickHandler extends IJobHandler {
    @Autowired
    private ClickContentService clickContentService;
    @Override
    public ReturnT<String> execute(String param) throws Exception {
        RobotClickVo robotClickVo= clickContentService.robotClick(Integer.parseInt(param));
        XxlJobLogger.log("row="+robotClickVo.getRow()+"usingTime"+robotClickVo.getUsingTime());
        return SUCCESS;
    }

}
