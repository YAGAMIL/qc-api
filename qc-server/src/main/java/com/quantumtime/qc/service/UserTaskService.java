package com.quantumtime.qc.service;

import com.quantumtime.qc.entity.activity.Activity;
import com.quantumtime.qc.entity.task.Task;
import com.quantumtime.qc.entity.task.TaskUserFetch;

import java.text.ParseException;
import java.util.List;

public interface UserTaskService {
    String processGoodContent(Task task) throws ParseException;

    String processInvite(Task task);

    List<Task> fetchTaskList(String province, String city);

    TaskUserFetch fetchOne(Long taskId) throws Exception;

    //斗地主点赞任务领取
    Boolean likeTaskStart(long taskId,String uid);
}
