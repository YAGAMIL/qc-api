package com.quantumtime.qc.vo;

import com.quantumtime.qc.entity.activity.Activity;
import com.quantumtime.qc.entity.task.Task;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ActivityDetailVo  implements Serializable {

    private  List<TaskDetailVo> taskList;
    private  Activity activity;

}
