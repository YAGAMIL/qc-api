package com.quantumtime.qc.repository;

import com.quantumtime.qc.entity.report.ReportUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReportUserRepository extends JpaRepository<ReportUser, Long> {
    @Query(value = "select * from report_user where uid=?1 and report_uid=?2", nativeQuery = true)
    ReportUser findExist(String uid, String reportUid);
}
