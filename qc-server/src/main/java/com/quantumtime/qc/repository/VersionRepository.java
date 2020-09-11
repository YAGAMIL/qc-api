package com.quantumtime.qc.repository;
import com.quantumtime.qc.entity.Version;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface VersionRepository  extends JpaRepository<Version, Long> {

    Version findByVersionCode (Long versionCode);
    @Query(value = "select v.* from version as v where v.version_code=(select MAX(version_code) from version) ; ",nativeQuery = true )
    Version newVersion();
}
