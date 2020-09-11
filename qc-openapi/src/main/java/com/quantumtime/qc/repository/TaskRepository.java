package com.quantumtime.qc.repository;

import com.quantumtime.qc.entity.task.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query(value = "select t.* from task t where t.enable=1 AND NOW()<end_time", nativeQuery = true)
    List<Task> getEnableTask();

    @Query(value = "select t.* from task t where t.enable=1 and t.fetch=1 and (t.area_name=?1 or t.area_name=?2 or t.area_type=2) and start_time<=now() and end_time>=now() order by create_time desc", nativeQuery = true)
    List<Task> getTaskByArea(String province, String city);

        @Query(value = "  select * from task where id in?1  ", nativeQuery = true)
    List<Task> findAllById(List<Long> taskIds);

}
