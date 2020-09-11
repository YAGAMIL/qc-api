package com.quantumtime.qc.repository;

import com.quantumtime.qc.entity.UserBlacklist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserBlacklistRepository extends JpaRepository<UserBlacklist, Long> {
    @Query(value = "select * from user_blacklist where from_uid=?1 and to_uid=?2", nativeQuery = true)
    UserBlacklist findExist(String fromUid, String toUid);

    @Query(value = "select * from user_blacklist where from_uid=?1 or to_uid=?1", nativeQuery = true)
    List<UserBlacklist> getAll(String fromUid);
}
