package com.quantumtime.qc.repository;

import com.quantumtime.qc.entity.poi.Business;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Description:商圈持久层接口 Program:qc-api Created on 2019-09-17 16:21
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo/a>
 * @version 1.0
 */
public interface BusinessRepository extends JpaRepository<Business, Long> {
  /**
   * Author: Tablo
   *
   * <p>Description:[根据商圈名称查询商圈] Created on 16:23 2019/09/17
   *
   * @param business 商圈名称
   * @param adCode 区Id
   * @return com.quantumtime.qc.entity.poi.Business
   */
  Business findBusinessByBusinessAreaAndAdCode(String business, String adCode);

  @Query(value = "select * from business_area as ba inner join poi_business as pb on ba.business_area_id=pb.business_area_id where pb.poi_id=?1", nativeQuery = true)
  List<Business> selectByPoiId(String poiId);
}
