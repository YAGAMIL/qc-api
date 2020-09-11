package com.quantumtime.qc.repository;

import com.quantumtime.qc.entity.undo.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;


public interface NotificationRepository extends JpaRepository<Notification, Long> {


    List<Notification> findAllByNotifiUserIdAndStatusAndType(String userId, Integer status, Integer type);

    @Modifying
    @Transactional
    @Query(value = "update Notification set status=?1 where id in ?2")
    void updateStatusByIds(Integer status, List<Long> ids);
}
