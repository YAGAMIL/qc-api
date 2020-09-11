package com.quantumtime.qc.repository;

import com.quantumtime.qc.entity.task.TaskUserFetch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface TaskUserFetchRepository extends JpaRepository<TaskUserFetch, Long> {

    @Query(value = "select t.* from task_user_fetch t where task_id=?1 and t.create_time>?2 and t.create_time<?3 and status=0", nativeQuery = true)
    List<TaskUserFetch> findToday(Long taskId, Date startTime, Date endTime);

    @Query(value = "select t.* from task_user_fetch t where t.task_id=?1 and t.user_id=?2 order by create_time desc limit 1", nativeQuery = true)
    TaskUserFetch findExist(Long taskId, String uid);

    @Query(value = "select * from task_user_fetch where user_id=?1 and task_id=?2 order by create_time desc limit 1", nativeQuery = true)
    TaskUserFetch findCurrentTask( String uid,long taskId);

    @Query(
            value =
                    "select * from qc.task_user_fetch where to_days(create_time) = to_days(now())and user_id=?1 AND expire_time > now() AND task_id = ?2 AND activity_id = ?3",
            nativeQuery = true)
    TaskUserFetch findTodayTask(String uid, Long taskId,Long activityId);
}
