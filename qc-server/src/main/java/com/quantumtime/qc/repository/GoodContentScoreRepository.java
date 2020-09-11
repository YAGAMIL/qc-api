package com.quantumtime.qc.repository;

import com.quantumtime.qc.entity.task.GoodContentScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GoodContentScoreRepository extends JpaRepository<GoodContentScore, Long> {

    @Query(value = "select * from good_content_score where task_user_fetch_id=?1", nativeQuery = true)
    List<GoodContentScore> getByTaskUserFetchId(Long id);
}
