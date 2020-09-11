package com.quantumtime.qc.repository;

import com.quantumtime.qc.entity.task.TaskRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TaskRuleRepository extends JpaRepository<TaskRule, Long> {
    @Query(value = "select t.* from task_rule t where t.task_id=?1 order by sort asc", nativeQuery = true)
    List<TaskRule> getByTaskId(Long id);
}
