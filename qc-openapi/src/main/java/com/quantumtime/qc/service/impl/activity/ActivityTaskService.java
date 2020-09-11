package com.quantumtime.qc.service.impl.activity;

/**
 * <p>
 * Description:活动任务关系业务层接口
 * Program:qc-api
 * </p>
 * Created on 2019-12-13 15:47
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
public interface ActivityTaskService {


    //斗地主点赞任务手动获取积分
    Boolean  receiveIntegral ( String uid );

}
