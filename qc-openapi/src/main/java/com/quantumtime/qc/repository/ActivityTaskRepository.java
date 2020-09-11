package com.quantumtime.qc.repository;

import com.quantumtime.qc.entity.activity.ActivityTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Description:活动任务关系持久层 Program:qc-api Created on 2019-12-13 15:44
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
@Repository
public interface ActivityTaskRepository extends JpaRepository<ActivityTask, Long> {

    @Query(value = "select * from  activity_task where is_main=2 and activity_id=?1 " , nativeQuery = true)
    ActivityTask ActivityIs(Long activityId);

    @Query(value = "select task_id from  activity_task where activity_id=?1 " , nativeQuery = true)
    List<Long> findByActivityId(Long activityId);

    ActivityTask findByTaskId(long taskId);

    List<ActivityTask> findActivityTaskByActivityId(Long activityId);
}
