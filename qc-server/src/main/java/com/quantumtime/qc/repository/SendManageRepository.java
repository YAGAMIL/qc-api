package com.quantumtime.qc.repository;

import com.quantumtime.qc.entity.undo.SendManage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;


public interface SendManageRepository extends JpaRepository<SendManage, Long> {



    @Query(value = "select count(s) from SendManage as s where s.identificationCode = ?1 and s.isSend = true and s.sendUser = ?2")
    Long verifyQrcode(String identificationCode, String userId);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "update tb_sendmanage set is_send = ?2 where id = ?1")
    Integer updateIsSend(Long id, Boolean isSend);
}
