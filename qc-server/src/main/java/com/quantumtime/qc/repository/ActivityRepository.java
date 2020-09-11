package com.quantumtime.qc.repository;

import com.quantumtime.qc.entity.activity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Description:活动持久层 Program:qc-api Created on 2019-12-13 15:42
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {

  @Query(value = "select * from activity where now()>start_time AND  now()<end_time and  status=0 and type=1 and address_type=0 and address_id=?1", nativeQuery = true)
  Activity isAddressId(long addressId);

  @Query(value = "select * from activity where now()>start_time AND  now()<end_time and  status=0 and type=0 and address_type=0 and address_id=?1", nativeQuery = true)
  Activity isBackstage( Long address_id);

  @Query(value = "select * from activity where now()>start_time AND  now()<end_time and  status=0 and type=0 and address_type=1 and address_name=?1", nativeQuery = true)
  Activity isBackstageName( String addressName);


  Activity findByAddressId(long addressId);


  @Query(value = "SELECT * FROM activity WHERE start_time <= ?1 AND lottery_time >= ?1" , nativeQuery = true)
    List<Activity> findActive(LocalDateTime now);
}
