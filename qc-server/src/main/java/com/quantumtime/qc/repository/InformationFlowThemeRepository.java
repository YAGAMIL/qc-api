package com.quantumtime.qc.repository;

import com.quantumtime.qc.entity.information.InformationFlowTheme;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface InformationFlowThemeRepository extends JpaRepository<InformationFlowTheme, Long> {

    List<InformationFlowTheme> findAllByAddressId(Long addressId);
}
