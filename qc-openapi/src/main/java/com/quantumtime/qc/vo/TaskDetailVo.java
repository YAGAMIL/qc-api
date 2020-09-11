package com.quantumtime.qc.vo;

import com.quantumtime.qc.entity.task.Task;
import lombok.Data;

@Data
public class TaskDetailVo  extends Task {
  //0代表已经领取过了，1代表没有
  private   Integer taskStatus ;
  //0代表已经领取过了，1代表没有
  private   Integer integralStatus  ;
  //积分
  private Long score;

}
