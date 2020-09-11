package com.quantumtime.qc.repository;

import com.quantumtime.qc.entity.information.InformationFlowLike;
import org.springframework.data.jpa.repository.JpaRepository;


public interface InformationFlowLikeRepository extends JpaRepository<InformationFlowLike, Long> {

    InformationFlowLike findByinformationFlowIdAndCreateId(Long informationFlowId, String createId);

}
