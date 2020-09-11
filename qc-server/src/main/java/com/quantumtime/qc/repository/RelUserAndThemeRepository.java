package com.quantumtime.qc.repository;

import com.quantumtime.qc.entity.undo.RelUserAndTheme;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RelUserAndThemeRepository extends JpaRepository<RelUserAndTheme, Long> {

    RelUserAndTheme findByUserIdAndThemeId(String userId, Long themeId);


    Long deleteByThemeIdAndUserIdAndAddressId(Long themeId, String userId, Long addressId);

}
