package com.quantumtime.qc.jobhandler;

import com.quantumtime.qc.entity.task.Task;
import com.quantumtime.qc.repository.TaskRepository;
import com.quantumtime.qc.service.UserTaskService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.List;

@JobHandler(value="userTaskHandler")
@Component
@Slf4j
public class UserTaskHandler extends IJobHandler {
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserTaskService userTaskService;
    @Override
    public ReturnT<String> execute(String param) throws Exception {
        String response = processAll();
        XxlJobLogger.log(response);
        return SUCCESS;
    }

    private String processAll() throws ParseException {
        StringBuilder sb = new StringBuilder();
        String msg = "开始处理用户任务...\n";
        sb.append(msg);
        log.info(msg);
        //找出所有进行当中的任务
        List<Task> tasks = taskRepository.getEnableTask();
        if (tasks == null || tasks.size() == 0) {
            msg = "没有可执行任务 \n";
            sb.append(msg);
            log.info(msg);
            return sb.toString();
        }
        for (Task task : tasks) {
            switch (task.getType()) {
                case Task.TASK_TYPE_GOOD_CONTENT :
                    sb.append(userTaskService.processGoodContent(task));
                    break;
                case Task.TASK_TYPE_INVITE :
                    sb.append(userTaskService.processInvite(task));
                    break;
                default:
            }
        }
        return sb.toString();
    }
}
