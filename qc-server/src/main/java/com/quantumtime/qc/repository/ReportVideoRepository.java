package com.quantumtime.qc.repository;

import com.quantumtime.qc.entity.report.ReportVideo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReportVideoRepository extends JpaRepository<ReportVideo, Long> {
    @Query(value = "select * from report_video where video_id=?1 and report_uid=?2", nativeQuery = true)
    ReportVideo findExist(String videoId, String reportUid);
}
