package com.quantumtime.qc.repository;

import com.quantumtime.qc.entity.information.InformationFlow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;


public interface InformationFlowRepository extends JpaRepository<InformationFlow, Long>, JpaSpecificationExecutor<InformationFlow> {

    @Modifying
    @Transactional
    @Query(value = "update InformationFlow set likeNum = likeNum + ?2 where id = ?1")
    void updateLikeNum(Long id, Long num);


    @Modifying
    @Transactional
    @Query(value = "update InformationFlow set commentNum = commentNum + ?2 where id = ?1")
    void updateCommitNum(Long id, Long num);


    @Modifying
    @Transactional
    @Query(value = "update InformationFlow set forwardNum = forwardNum + ?2 where id = ?1")
    void updateForwardNum(Long id, Long num);
}
