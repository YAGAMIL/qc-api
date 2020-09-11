package com.quantumtime.qc.repository;

import com.quantumtime.qc.entity.Config;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfigRepository extends JpaRepository<Config, Long> {
}
